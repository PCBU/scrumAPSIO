package controller;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import service.ConsultantService;
import service.MissionService;
import view.MainView;

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

        consultants = ConsultantService.listeConsultants();
        File f = new File("consultant.itl");
        if (f.exists()) {
            this.consultants = lireConsultant();
        } else {
            this.consultants = listeConsultants();
        }

        f = new File("cission.itl");
        if (f.exists()) {
            this.missions = lireMission();
        } else {
            this.missions = new ArrayList<Mission>();
        }

        f = new File("client.itl");
        if (f.exists()) {
            this.clients = lireClient();
        } else {
            this.clients = new ArrayList<Client>();
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

        } else if (splitCommande[0].equals("listecommandes")) {
            listeCommandes();

        } else if (splitCommande[0].equals("consultantsdisponibles")) {
            consultantsDisponibles();
        } else { //cas ou la commande n'est pas reconnue
            mainView.afficher("commande '" + commande + "' inconnue.");
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

    private void creerClient(String[] splitCommande) {
        if (splitCommande.length == 5) {
            Client nouveauClient = new Client(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

            clients.put(nouveauClient.getNom(), nouveauClient);

            mainView.afficher("Liste des clients actuels : ");

            for (Map.Entry<String, Client> client : clients.entrySet()) {
                mainView.afficher(client.getValue().toString());
            }

        } else {
            mainView.afficher("syntaxe incorrecte : creerclient;nom;prenom;adresse;telephone ");
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

            mainView.afficher(consultant.toString());
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

                Mission mission = new Mission(consultantMission.get(),
                        DateTime.parse(splitCommande[2], formatter),
                        DateTime.parse(splitCommande[3], formatter),
                        splitCommande[4],
                        clientMission.get());

                missions.put(mission.getIntitule(), mission);

                mainView.afficher("Mission ajoutée\n" + mission);

                break;

            case 5:
                //Optional<Client> clientMission = ClientService.getFirstClientByNom(consultants, splitCommande[5]);
                Optional<Client> client = of(new Client());

                Mission nouvelleMission = new Mission(DateTime.parse(splitCommande[1], formatter),
                        DateTime.parse(splitCommande[2]),
                        splitCommande[3],
                        client.get());

                missions.put(nouvelleMission.getIntitule(), nouvelleMission);

                mainView.afficher("Mission ajoutée\n" + nouvelleMission);

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

    private void afficherSyntaxe(String commande) {

        if (commande.equals("creermission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreermission;Nom consultant (facultatif);Date début jjmmaaaa;Date fin jjmmaaaa;Libellé;Nom du client");

        } else if (commande.equals("creerconsultant")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreerconsultant;Nom;Prenom;Adresse;Telephone");

        } else if (commande.equals("envoyermission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nenvoyermission;Libellé de la mission;Nom du consultant");
        }
    }

    private void enregistrerListeConsultant() throws IOException {
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
            ex.printStackTrace();
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

    private ArrayList<Consultant> lireConsultant() {
        ArrayList<Consultant> transfert = new ArrayList<Consultant>();
        try {
            InputStream file = new FileInputStream("Consultant.itl");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            transfert = (ArrayList<Consultant>) input.readObject();
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

    private ArrayList<Mission> lireMission() {
        ArrayList<Mission> transfert = new ArrayList<Mission>();
        try {
            InputStream file = new FileInputStream("Mission.itl");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            transfert = (ArrayList<Mission>) input.readObject();
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

    private ArrayList<Client> lireClient() {
        ArrayList<Client> transfert = new ArrayList<Client>();
        try {
            InputStream file = new FileInputStream("Client.itl");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            transfert = (ArrayList<Client>) input.readObject();
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
