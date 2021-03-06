package controller;

import DAO.UserDAO;
import model.Client;
import model.Consultant;
import model.Mission;
import model.User;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormatter;
import service.ConsultantService;
import view.MainView;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static service.ConsultantService.consultants;

public class MainController {
    private MainView mainView;

    private User loggedIn = null;
    private HashMap<String, ArrayList<String>> managers;

    private HashMap<String, Consultant> consultants;
    private HashMap<String, Client> clients;
    private HashMap<String, Mission> missions;

    private ArrayList<String> commandes;

    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;

        this.commandes = new ArrayList<String>();
        this.managers = new HashMap<String, ArrayList<String>>();

        ArrayList<String> listeConsultants = new ArrayList<String>();
        listeConsultants.add("Moulin");
        listeConsultants.add("Martin");

        this.managers.put("roger", listeConsultants); //TODO remove when database is available

        File f = new File("Consultant.itl");
        if (f.exists()) {
            this.consultants = lireConsultant();
        } else {
            this.consultants = consultants();
        }

        f = new File("Mission.itl");
        if (f.exists()) {
            this.missions = lireMission();
        } else {
            this.missions = new HashMap<String, Mission>();
        }

        f = new File("Client.itl");
        if (f.exists()) {
            this.clients = lireClient();
        } else {
            this.clients = new HashMap<String, Client>();
        }
    }

    public void commande(String commande) {
        String[] splitCommande = commande.split(";");


        if (loggedIn == null) {
            if (splitCommande[0].equals("login")) {
                login(splitCommande);
            } else {
                afficherSyntaxe("notloggedin");
            }
        } else {
            commandes.add(commande);

            if (splitCommande[0].equals("listeconsultant")) {
                listeConsultant();

            } else if (splitCommande[0].equals("creerclient")) {
                creerClient(splitCommande);

            } else if (splitCommande[0].equals("creerconsultant")) {
                creerConsultant(splitCommande);

            } else if (splitCommande[0].equals("listeconsultantlibre")) {
                mainView.afficher("Liste des consultants actuellement libres :");

            } else if (splitCommande[0].equals("clear")) {
                mainView.effacer();

            } else if (splitCommande[0].equals("creermission")) {
                creerMission(splitCommande);

            } else if (splitCommande[0].equals("listemissionsvacantes")) {
                listeMissionsVacantes();

            } else if (splitCommande[0].equals("envoyermission")) {
                envoyerMission(splitCommande);

            } else if (splitCommande[0].equals("retourmission")) {
                retourMission(splitCommande);

            } else if (splitCommande[0].equals("historique")) {
                Historique();

            } else if (splitCommande[0].equals("consultantsdisponibles")) {
                consultantsDisponibles();

            } else if (splitCommande[0].equals("consultantsdisponiblesdate")) {
                consultantsDisponiblesDateT(splitCommande);

            } else if (splitCommande[0].equals("listemission")) {
                listeMission();

            } else if (splitCommande[0].equals("listeclient")) {
                listeClient();

            } else if (splitCommande[0].equals("disposconsultant")) {
                disposConsultant(splitCommande);

            } else if (splitCommande[0].equals("deconnexion")) {
                loggedIn = null;
                mainView.afficher("Vous êtes maintenant déconnecté.");

            } else if (splitCommande[0].equals("ajoutcompetence")) {
                nouvelleComp(splitCommande);

            } else if (splitCommande[0].equals("renvoyerconsultant")) {
                renvoyerConsultant(splitCommande);

            } else if (splitCommande[0].equals("supprcompetence")) {
                supprComp(splitCommande);

            } else { //cas ou la commande n'est pas reconnue
                mainView.afficher("commande '" + commande + "' inconnue.");
            }
        }
    }

    private void renvoyerConsultant(String[] splitCommande) {
        if (splitCommande.length != 2) {
            afficherSyntaxe(splitCommande[0]);
        } else {
            if (loggedIn.getRole().equals("directeur")) {
                renvoiAlgorithme(splitCommande);

            } else {
                boolean isFound = false;

                for (Iterator<String> iterator = managers.get(loggedIn.getLogin()).iterator(); iterator.hasNext() && !isFound; ) {
                    String consultantDuManager = iterator.next();
                    if (splitCommande[1].equals(consultantDuManager)) {
                        isFound = true;
                    }
                }

                if (isFound) {
                    renvoiAlgorithme(splitCommande);
                }
            }
        }
    }

    private void renvoiAlgorithme(String[] splitCommande) {
        //Remove from missions
        for (Map.Entry<String, Mission> entry : missions.entrySet()) {
            if (entry.getValue().getConsultant() != null) {
                if (entry.getValue().getConsultant().getNom().equals(splitCommande[1])) {
                    entry.getValue().setConsultant(null);
                }
            }
        }

        //Remove from managers
        for (Map.Entry<String, ArrayList<String>> entry : managers.entrySet()) {
            for (String consultant : entry.getValue()) {
                if (consultant.equals(splitCommande[1])) {
                    entry.getValue().remove(consultant);
                }
            }
        }

        //Remove from consultants
        consultants.remove(splitCommande[1]);

        mainView.afficher("Le consultant " + splitCommande[1] + " a été correctement renvoyé.");
    }

    private void login(String[] splitCommande) {

        if (splitCommande.length != 3) {
            afficherSyntaxe(splitCommande[0]);
        } else {
            List<User> listeUsers;
            try {
                listeUsers = UserDAO.getAllUsers();

                //listeUsers.add(new User("directeur", "password", "directeur"));
                //listeUsers.add(new User("roger", "password", "manager"));

                boolean connected = false;

                for (User user : listeUsers) {
                    if (splitCommande[1].equals(user.getLogin()) && splitCommande[2].equals(user.getPassword())) {
                        loggedIn = user;//getUserStatus(user.getKey());
                        mainView.afficher("Vous êtes maintenant connecté en tant que " + user.getLogin()
                                + "\nPour vous déconnecter, utilisez la commande 'deconnexion'.");

                        connected = true;
                    }
                }

                if (!connected) {
                    mainView.afficher("Identifiants erronés. Veuillez réessayer.");
                }
            } catch (SQLException e) {
                mainView.afficher("Un problème a été rencontré lors de l'accès à la base de données des utilisateurs.\nVeuillez réessayer ultérieurement.");

                e.printStackTrace();
            }
        }
    }

    private void disposConsultant(String[] commande) {

        if (commande.length < 2) {
            afficherSyntaxe("disposconsultant");
        } else {

            Consultant consultant = consultants.get(commande[1]);

            if (consultant == null) {
                mainView.afficher("Le consultant " + commande[1] + "n'existe pas.");
            } else {
                if (loggedIn.getRole().equals("directeur")) {
                    ArrayList<DateTime[]> dispos = new ArrayList<DateTime[]>();

                    for (Map.Entry<String, Mission> entry : missions.entrySet()) {
                        Mission mission = entry.getValue();

                        if (mission.getConsultant().getNom().equals(consultant.getNom())) {
                            DateTime[] dates = {mission.getDebut(), mission.getFin()};

                            dispos.add(dates);
                        }
                    }

                    if (dispos.isEmpty()) {
                        mainView.afficher("Le consultant " + consultant.getNom() + " est actuellement totalement disponible.");
                    } else {
                        mainView.afficher("Le consultant " + consultant.getNom() + " est indisponible entre les dates suivantes :");

                        for (DateTime[] dates : dispos) {
                            mainView.afficher("Du " + dates[0] + " au " + dates[1]);
                        }
                    }
                } else {
                    boolean isFound = false;

                    for (Iterator<String> iterator = managers.get(loggedIn.getLogin()).iterator(); iterator.hasNext() && !isFound; ) {
                        String consultantDuManager = iterator.next();
                        if (consultant.getNom().equals(consultantDuManager)) {
                            isFound = true;
                        }
                    }

                    if (isFound) {
                        ArrayList<DateTime[]> dispos = new ArrayList<DateTime[]>();

                        for (Map.Entry<String, Mission> entry : missions.entrySet()) {
                            Mission mission = entry.getValue();

                            if (mission.getConsultant().getNom().equals(consultant.getNom())) {
                                DateTime[] dates = {mission.getDebut(), mission.getFin()};

                                dispos.add(dates);
                            }
                        }

                        if (dispos.isEmpty()) {
                            mainView.afficher("Le consultant " + consultant.getNom() + " est actuellement totalement disponible.");
                        } else {
                            mainView.afficher("Le consultant " + consultant.getNom() + " est indisponible entre les dates suivantes :");

                            for (DateTime[] dates : dispos) {
                                mainView.afficher("Du " + dates[0] + " au " + dates[1]);
                            }
                        }
                    } else {
                        mainView.afficher("Vous n'avez pas de visibilité sur ce consultant.");
                    }
                }
            }
        }
    }

    private void consultantsDisponibles() {
        if (!ConsultantService.consultantsDisponibles(this.consultants, this.missions).isEmpty()) {

            if (loggedIn.getRole().equals("directeur")) {
                mainView.afficher("Consultants disponibles :");
                for (Map.Entry<String, Consultant> entry : ConsultantService.consultantsDisponibles(this.consultants, this.missions).entrySet()) {
                    Consultant unConsultant = entry.getValue();
                    mainView.afficher(unConsultant.toString());
                }
            } else {
                mainView.afficher("Consultants disponibles :");
                for (Map.Entry<String, Consultant> entry : ConsultantService.consultantsDisponibles(this.consultants, this.missions).entrySet()) {
                    for (String consultant : managers.get(loggedIn.getLogin())) {
                        if (entry.getValue().getNom().equals(consultant)) {
                            Consultant unConsultant = entry.getValue();
                            mainView.afficher(unConsultant.toString());
                        }
                    }
                }
            }
        }
    }

    private void consultantsDisponiblesDateT(String[] commande) {

        if (commande.length == 2) {
            try {
                DateTime parsedDate = DateTime.parse(commande[1], forPattern("ddMMyyyy"));

                if (!ConsultantService.consultantsDisponiblesPourDate(this.consultants, this.missions, parsedDate).isEmpty()) {

                    mainView.afficher("Consultants disponibles pour la date + " + parsedDate + " :");
                    for (Map.Entry<String, Consultant> entry : ConsultantService.consultantsDisponibles(this.consultants, this.missions).entrySet()) {
                        Consultant unConsultant = entry.getValue();
                        mainView.afficher(unConsultant.toString());
                    }
                } else {
                    mainView.afficher("Aucun consultant n'est disponible pour cette date.");
                }
            } catch (IllegalFieldValueException e) {
                afficherSyntaxe("date");
            } catch (IllegalArgumentException e) {
                afficherSyntaxe(commande[0]);
            }
        } else {
            afficherSyntaxe(commande[0]);
        }
    }

    private void Historique() {
        mainView.afficher("Commandes effectuées :");

        for (String commande : commandes) {
            mainView.afficher(commande);
        }
    }

    private void listeConsultant() {
        mainView.afficher("Liste des consultants :");

        if (loggedIn.getRole().equals("directeur")) {
            for (Map.Entry<String, Consultant> entry : consultants.entrySet()) {
                mainView.afficher(entry.getValue().toString());
            }
        } else {
            for (Map.Entry<String, Consultant> entry : consultants.entrySet()) {
                for (String consultant : managers.get(loggedIn.getLogin())) {
                    if (entry.getValue().getNom().equals(consultant)) {
                        mainView.afficher(entry.getValue().toString());
                    }
                }
            }
        }
    }

    private void listeMission() {
        mainView.afficher("Liste des missions :");
        for (Map.Entry<String, Mission> entry : missions.entrySet()) {
            mainView.afficher(entry.getValue().toString());
        }
    }

    private void listeClient() {
        mainView.afficher("Liste des clients :");
        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            mainView.afficher((entry.getValue().toString()));
        }
    }

    private void creerClient(String[] splitCommande) {
        if (splitCommande.length == 5) {
            Client nouveauClient = new Client(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

            clients.put(nouveauClient.getNom(), nouveauClient);

            enregistrerListeClient();

            mainView.afficher("Client ajouté : " + nouveauClient);

        } else {
            afficherSyntaxe("creerclient");
        }
    }

    private void listeMissionsVacantes() {
        int nbMissionVacantes = 0;
        // Compte le nombre de missions vacantes
        for (Map.Entry<String, Mission> mission : missions.entrySet()) {
            if (mission.getValue().isVaccante())
                nbMissionVacantes++;
        }

        // Affichage des missions vacantes, ou pas
        if (nbMissionVacantes == 0) {
            mainView.afficher("Aucune mission vacante");
        } else {
            mainView.afficher("Liste des missions vacantes :");

            for (Map.Entry<String, Mission> entry : missions.entrySet()) {
                Mission mission = entry.getValue();

                if (mission.isVaccante())
                    mainView.afficher(mission.toString());
            }
        }
    }

    private void creerConsultant(String[] splitCommande) {

        try {
            Consultant consultant = new Consultant(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

            consultants.put(splitCommande[1], consultant);

            enregistrerListeConsultant();

            mainView.afficher("Consultant créé : " + consultant);
        } catch (IndexOutOfBoundsException e) {
            afficherSyntaxe(splitCommande[0]);
        }
    }

    private void creerMission(String[] splitCommande) {

        DateTimeFormatter formatter = forPattern("ddMMyyyy");

        switch (splitCommande.length) {
            case 6:
                Consultant consultantMission = consultants.get(splitCommande[1]);

                Client clientMission = clients.get(splitCommande[5]);

                if (consultantMission == null) {
                    mainView.afficher("Le consultant " + splitCommande[1] + " n'existe pas.");
                    break;
                } else if (clientMission == null) {
                    mainView.afficher("Le client " + splitCommande[5] + " n'existe pas.");
                    break;
                }

                try {
                    Mission mission = new Mission(
                            consultantMission,
                            DateTime.parse(splitCommande[2], formatter),
                            DateTime.parse(splitCommande[3], formatter),
                            splitCommande[4],
                            clientMission
                    );

                    missions.put(mission.getIntitule(), mission);

                    enregistrerListeMission();

                    mainView.afficher("Mission ajoutée\n" + mission);

                } catch (IllegalFieldValueException e) {
                    afficherSyntaxe("date");
                }

                break;

            case 5:
                Client client = clients.get(splitCommande[4]);

                if (client == null) {
                    mainView.afficher("Le client " + splitCommande[4] + " n'existe pas.");
                    break;
                }

                try {
                    Mission nouvelleMission = new Mission(
                            DateTime.parse(splitCommande[1], formatter),
                            DateTime.parse(splitCommande[2]),
                            splitCommande[3],
                            client
                    );


                    missions.put(nouvelleMission.getIntitule(), nouvelleMission);

                    enregistrerListeMission();

                    mainView.afficher("Mission ajoutée\n" + nouvelleMission);

                } catch (IllegalFieldValueException e) {
                    afficherSyntaxe("date");
                }

                break;

            default:
                afficherSyntaxe(splitCommande[0]);
        }
    }

    private void envoyerMission(String[] splitCommande) {

        if (splitCommande.length == 3) {
            if (missions.containsKey(splitCommande[1])) {
                if (missions.get(splitCommande[1]).isVaccante()) {
                    if (loggedIn.getRole().equals("directeur")) {
                        if (consultants.containsKey(splitCommande[2])) {
                            Consultant consultantSend = consultants.get(splitCommande[2]);
                            Mission missionSend = missions.get(splitCommande[1]);
                            missionSend.setConsultant(consultantSend);
                            missions.put(splitCommande[1], missionSend);
                            mainView.afficher("Consultant envoyé en mission " + missionSend);
                        } else {
                            mainView.afficher("Le consultant n'existe pas");
                        }
                    } else {
                        boolean isFound = false;

                        for (Iterator<String> iterator = managers.get(loggedIn.getLogin()).iterator(); iterator.hasNext() && !isFound; ) {
                            String consultantDuManager = iterator.next();
                            if (splitCommande[2].equals(consultantDuManager)) {
                                isFound = true;
                            }
                        }

                        if (isFound) {
                            if (consultants.containsKey(splitCommande[2])) {
                                Consultant consultantSend = consultants.get(splitCommande[2]);
                                Mission missionSend = missions.get(splitCommande[1]);
                                missionSend.setConsultant(consultantSend);
                                missions.put(splitCommande[1], missionSend);
                                mainView.afficher("Consultant envoyé en mission " + missionSend);
                            } else {
                                mainView.afficher("Le consultant n'existe pas");
                            }
                        } else {
                            mainView.afficher("Vous n'avez pas de visibilité sur ce consultant.");
                        }
                    }
                } else {
                    mainView.afficher("Un consultant est déjà affecté à cette mission");
                }
            } else {
                mainView.afficher("La mission n'existe pas");
            }
        } else {
            afficherSyntaxe(splitCommande[0]);
        }
    }

    private void retourMission(String[] splitCommande) {

        if (splitCommande.length == 2) {
            if (missions.containsKey(splitCommande[1])) {

                if (loggedIn.getRole().equals("directeur")) {
                    if (!missions.get(splitCommande[1]).isVaccante()) {
                        Mission missionSend = missions.get(splitCommande[1]);
                        missionSend.setConsultant(null);
                        missions.put(splitCommande[1], missionSend);
                        mainView.afficher("Consultant revenu de mission " + missionSend);
                        enregistrerListeMission();
                    } else {
                        mainView.afficher("Aucun consultant affecté à cette mission");
                    }
                } else {
                    boolean isFound = false;

                    for (Iterator<String> iterator = managers.get(loggedIn.getLogin()).iterator(); iterator.hasNext() && !isFound; ) {
                        String consultantDuManager = iterator.next();
                        if (missions.get(splitCommande[1]).getConsultant().getNom().equals(consultantDuManager)) {
                            isFound = true;
                        }
                    }

                    if (isFound) {
                        if (!missions.get(splitCommande[1]).isVaccante()) {
                            Mission missionSend = missions.get(splitCommande[1]);
                            missionSend.setConsultant(null);
                            missions.put(splitCommande[1], missionSend);
                            mainView.afficher("Consultant revenu de mission " + missionSend);
                            enregistrerListeMission();
                        } else {
                            mainView.afficher("Aucun consultant affecté à cette mission");
                        }
                    } else {
                        mainView.afficher("Vous n'avez pas de visibilité sur ce consultant.");
                    }
                }
            } else {
                mainView.afficher("La mission n'existe pas");
            }
        } else {
            afficherSyntaxe(splitCommande[0]);
        }
    }

    private void afficherSyntaxe(String commande) {

        if (commande.equals("creermission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreermission;Nom consultant (facultatif);Date début jjmmaaaa;Date fin jjmmaaaa;Libellé;Nom du client");

        } else if (commande.equals("creerconsultant")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreerconsultant;Nom;Prenom;Adresse;Telephone");

        } else if (commande.equals("creerclient")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreerclient;Nom;Prenom;Adresse;Telephone");

        } else if (commande.equals("envoyermission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nenvoyermission;Intitulé de la mission;Nom du consultant");

        } else if (commande.equals("retourmission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nretourmission;Libellé de la mission");

        } else if (commande.equals("consultantsdisponiblesdate")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nconsultantsdisponiblesdate;Date jjmmaaaa");

        } else if (commande.equals("disposconsultant")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ndisposconsultant;Nom consultant");

        } else if (commande.equals("login")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nlogin;Identifiant;Mot de passe");

        } else if (commande.equals("ajoutcompetence")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\najoutcompetence;nomConsultant;compétence");

        } else if (commande.equals("supprcompetence")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nsupprcompetence;Nom consultant;compétence");

        } else if (commande.equals("renvoyerconsultant")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nrenvoyerconsultant;Nom consultant");

        } else if (commande.equals("date")) {
            mainView.afficher("Format de date incorrect. Le format valide est jjmmaaaa.\nPour le 1er mars 2015, la syntaxe est : 01032015");

        } else if (commande.equals("notloggedin")) {
            mainView.afficher("Vous n'êtes actuellement pas connecté.\nPour remédier à cela, tapez la commande login;Identifiant;Mot de passe");

        } else if (commande.equals("ajoutcompetence")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\najoutcompetence;nomConsultant;compétence");

        } else if (commande.equals("supprcompetence")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nsupprcompetence;nomConsultant;compétence");
        }
    }

    private void enregistrerListeConsultant() {
        try {
            OutputStream file = new FileOutputStream("consultant.itl");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(consultants);
            output.close();
            buffer.close();
            file.close();
        } catch (IOException ex) {
            mainView.afficher("Certaines données n'ont pas pu être enregistrées, il se peut que des données soient perdues.");
        }

    }

    private void enregistrerListeClient() {
        try {
            OutputStream file = new FileOutputStream("Client.itl");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(clients);
            output.close();
            buffer.close();
            file.close();
        } catch (IOException ex) {
            mainView.afficher("Certaines données n'ont pas pu être enregistrées, il se peut que des données soient perdues.");
        }
    }

    private void enregistrerListeMission() {
        try {
            OutputStream file = new FileOutputStream("Mission.itl");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(missions);
            output.close();
            buffer.close();
            file.close();
        } catch (IOException ex) {
            mainView.afficher("Certaines données n'ont pas pu être enregistrées, il se peut que des données soient perdues.");
        }


    }

    private HashMap<String, Consultant> lireConsultant() {
        HashMap<String, Consultant> transfert = new HashMap<String, Consultant>();
        try {
            InputStream file = new FileInputStream("Consultant.itl");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            transfert = (HashMap<String, Consultant>) input.readObject();
            input.close();
            buffer.close();
            file.close();

        } catch (IOException ex) {
            mainView.afficher("Certaines données n'ont pas pus être lues, il peut manquer certaines informations.");
            this.consultants = new HashMap<String, Consultant>();
        } catch (ClassNotFoundException ex) {
            mainView.afficher("Certaines données n'ont pas pus être lues, il peut manquer certaines informations.");
            this.consultants = new HashMap<String, Consultant>();
        }

        return transfert;
    }

    private HashMap<String, Mission> lireMission() {
        HashMap<String, Mission> transfert = new HashMap<String, Mission>();
        try {
            InputStream file = new FileInputStream("Mission.itl");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            transfert = (HashMap<String, Mission>) input.readObject();
            input.close();
            buffer.close();
            file.close();

        } catch (IOException ex) {
            mainView.afficher("Certaines données n'ont pas pus être lues, il peut manquer certaines informations.");
            this.missions = new HashMap<String, Mission>();
        } catch (ClassNotFoundException ex) {
            mainView.afficher("Certaines données n'ont pas pus être lues, il peut manquer certaines informations.");
            this.missions = new HashMap<String, Mission>();
        }

        return transfert;
    }

    private HashMap<String, Client> lireClient() {
        HashMap<String, Client> transfert = new HashMap<String, Client>();
        try {
            InputStream file = new FileInputStream("Client.itl");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            transfert = (HashMap<String, Client>) input.readObject();
            input.close();
            buffer.close();
            file.close();

        } catch (IOException ex) {
            mainView.afficher("Certaines données n'ont pas pus être lues, il peut manquer certaines informations.");
            this.clients = new HashMap<String, Client>();
        } catch (ClassNotFoundException ex) {
            mainView.afficher("Certaines données n'ont pas pus être lues, il peut manquer certaines informations.");
            this.clients = new HashMap<String, Client>();
        }

        return transfert;
    }

    private void nouvelleComp(String[] commande) {
        if (commande.length == 3) {
            consultants.get(commande[1]).ajouterCompetence(commande[2]);
            enregistrerListeConsultant();
            mainView.afficher("Compétence : " + commande[2] + " ajouté.");
        } else {
            afficherSyntaxe(commande[0]);
        }
    }

    private void supprComp(String[] commande) {
        boolean passage;

        if (commande.length == 3) {
            passage = consultants.get(commande[1]).retirerCompetence(commande[2]);
            if (passage) {
                mainView.afficher("Compétence : " + commande[2] + " supprimer.");
                enregistrerListeConsultant();
            } else {

                mainView.afficher("Compétence : " + commande[2] + " inconnue.");
            }


        } else {
            afficherSyntaxe(commande[0]);
        }
    }

    private void clientSansMission(String[] commande) {

        ArrayList<Client> clientMissions = new ArrayList<Client>();
        ArrayList<Client> lesClients = new ArrayList<Client>();
        boolean passer = false;

        for (Map.Entry<String, Mission> Entry : missions.entrySet()) {
            clientMissions.add(Entry.getValue().getClient());
        }

        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            passer = false;
            for (Client c : clientMissions) {
                if (c.equals(entry.getValue())) {
                    passer = true;
                }
            }
            if (!passer) {
                lesClients.add(entry.getValue());
            }
        }

        mainView.afficher("Les clients sans mission sont : ");
        for (Client c : lesClients) {
            mainView.afficher(c.getNom());
        }
    }
}





