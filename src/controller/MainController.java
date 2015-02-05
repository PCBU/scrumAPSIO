package controller;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormatter;
import service.ConsultantService;
import service.MissionService;
import view.MainView;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static service.ConsultantService.consultants;

public class MainController {
    private MainView mainView;

    private HashMap<String, Consultant> consultants;
    private HashMap<String, Client> clients;
    private HashMap<String, Mission> missions;

    private ArrayList<String> commandes;

    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;

        this.commandes = new ArrayList<String>();

        File f = new File("consultant.itl");
        if (f.exists()) {
            this.consultants = lireConsultant();
        } else {
            this.consultants = consultants();
        }

        f = new File("mission.itl");
        if (f.exists()) {
            this.missions = lireMission();
        } else {
            this.missions = new HashMap<String, Mission>();
        }

        f = new File("client.itl");
        if (f.exists()) {
            this.clients = lireClient();
        } else {
            this.clients = new HashMap<String, Client>();
        }

    }

    public void commande(String commande) {
        String[] splitCommande = commande.split(";");

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

        } else if (splitCommande[0].equals("listecommandes")) {
            listeCommandes();

        } else if (splitCommande[0].equals("consultantsdisponibles")) {
            consultantsDisponibles();

        } else if (splitCommande[0].equals("consultantsdisponiblesdate")) {
            consultantsDisponiblesDateT(splitCommande);

        } else if(splitCommande[0].equals("listemission")){
          listeMission();

        } else if(splitCommande[0].equals("listeclient")){
          listeClient();

        } else if (splitCommande[0].equals("disposconsultant")) {
            disposConsultant(splitCommande);

        } else { //cas ou la commande n'est pas reconnue
            mainView.afficher("commande '" + commande + "' inconnue.");
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
            }
        }
    }

    private void consultantsDisponibles() {
        if (!MissionService.consultantsDisponibles(this.consultants, this.missions).isEmpty()) {
            mainView.afficher("Consultants disponibles :");
            for(Map.Entry<String, Consultant> entry : MissionService.consultantsDisponibles(this.consultants, this.missions).entrySet()){
                Consultant unConsultant = entry.getValue();
                mainView.afficher(unConsultant.toString());
            }
        }
    }

    private void consultantsDisponiblesDateT(String[] commande) {

        if (commande.length >= 2) {
            try {

                DateTime parsedDate = DateTime.parse(commande[1], forPattern("ddMMyyyy"));

                if (!MissionService.consultantsDisponiblesPourDate(this.consultants, this.missions, parsedDate).isEmpty()) {

                    mainView.afficher("Consultants disponibles pour la date + " + parsedDate + " :");
                    for (Map.Entry<String, Consultant> entry : MissionService.consultantsDisponibles(this.consultants, this.missions).entrySet()) {
                        Consultant unConsultant = entry.getValue();
                        mainView.afficher(unConsultant.toString());
                    }
                }
            } catch (IllegalFieldValueException e) {
                afficherSyntaxe("date");
            }
        } else {
            afficherSyntaxe(commande[0]);
        }
    }

    private void listeCommandes() {
        mainView.afficher("Commandes effectuées :");

        for (String commande : commandes) {
            mainView.afficher(commande);
        }
    }

    private void listeConsultant() {
        mainView.afficher("Liste des consultants :");

        for (Map.Entry<String, Consultant> entry : consultants.entrySet()) {
            mainView.afficher(entry.getValue().toString());
        }
    }

    private void listeMission(){
        mainView.afficher("Liste des missions :");
        for (Map.Entry<String, Mission> entry : missions.entrySet()){
            mainView.afficher(entry.getValue().toString());
        }
    }

    private void listeClient(){
        mainView.afficher("Liste des clients :");
        for (Map.Entry<String, Client> entry : clients.entrySet()){
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
                Optional<Consultant> consultantMission = ConsultantService.getFirstConsultantByNom(consultants, splitCommande[1]);

                if (!consultantMission.isPresent()) {
                    afficherSyntaxe(splitCommande[0]);
                    break;
                }

                //Optional<Client> clientMission = ClientService.getFirstClientByNom(clients, splitCommande[5]);
                Optional<Client> clientMission = of(new Client());

                try {
                    Mission mission = new Mission(consultantMission.get(),
                            DateTime.parse(splitCommande[2], formatter),
                            DateTime.parse(splitCommande[3], formatter),
                            splitCommande[4],
                            clientMission.get());

                    missions.put(mission.getIntitule(), mission);

                    enregistrerListeMission();

                    mainView.afficher("Mission ajoutée\n" + mission);

                } catch (IllegalFieldValueException e) {
                    afficherSyntaxe("date");
                }

                break;

            case 5:
                //Optional<Client> clientMission = ClientService.getFirstClientByNom(consultants, splitCommande[5]);
                Optional<Client> client = of(new Client());

                try {
                    Mission nouvelleMission = new Mission(DateTime.parse(splitCommande[1], formatter),
                            DateTime.parse(splitCommande[2]),
                            splitCommande[3],
                            client.get());

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

        if(splitCommande.length == 3) {
            if (missions.containsKey(splitCommande[1])){
                if (missions.get(splitCommande[1]).isVaccante()){
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

        if(splitCommande.length == 2) {
            if (missions.containsKey(splitCommande[1])){
                if (!missions.get(splitCommande[1]).isVaccante()){
                        Mission missionSend = missions.get(splitCommande[1]);
                        missionSend.setConsultant(null);
                        missions.put(splitCommande[1], missionSend);
                        mainView.afficher("Consultant revenu de mission " + missionSend);
                } else {
                    mainView.afficher("Aucun consultant affecté à cette mission");
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
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nenvoyermission;Libellé de la mission;Nom du consultant");

        } else if (commande.equals("consultantsdisponiblesdate")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nconsultantsdisponiblesdate;Date jjmmaaaa");

        } else if (commande.equals("disposconsultant")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ndisposconsultant;Nom consultant");

        } else if (commande.equals("date")) {
            mainView.afficher("Format de date incorrect. Le format valide est jjmmaaaa.\nPour le 1er mars 2015, la syntaxe est : 01032015");

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
            mainView.afficher("Certaines données n'on pas pus être lue, il peut manquer certaine information--- IOException.");
        } catch (ClassNotFoundException ex) {
            mainView.afficher("Certaines données n'on pas pus être lue, il peut manquer certaine information--- ClassNotFoundException");
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
            mainView.afficher("Certaines données n'on pas pus être lue, il peut manquer certaine information--- IOException.");
        } catch (ClassNotFoundException ex) {
            mainView.afficher("Certaines données n'on pas pus être lue, il peut manquer certaine information--- ClassNotFoundException");
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
            mainView.afficher("Certaines données n'on pas pus être lue, il peut manquer certaine information--- IOException.");
        } catch (ClassNotFoundException ex) {
            mainView.afficher("Certaines données n'on pas pus être lue, il peut manquer certaine information--- ClassNotFoundException");
        }

        return transfert;
    }


}
