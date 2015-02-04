package controller;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import service.ConsultantService;
import service.MissionService;
import view.MainView;

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

    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;

        consultants = ConsultantService.consultants();

        this.consultants = consultants();
        this.missions = MissionService.tests(); //new HashMap<String, Mission>();
        this.clients = new HashMap<String, Client>();
    }

    public void commande(String commande) {
        String[] splitCommande = commande.split(";");

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

        } else { //cas ou la commande n'est pas reconnue
            mainView.afficher("commande '" + commande + "' inconnue.");
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
            consultants.put(splitCommande[1], new Consultant(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]));

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

    private void afficherSyntaxe(String commande) {

        if (commande.equals("creermission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreermission;Nom consultant (facultatif);Date début jjmmaaaa;Date fin jjmmaaaa;Libellé;Nom du client");

        } else if (commande.equals("creerconsultant")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreerconsultant;Nom;Prenom;Adresse;Telephone");

        } else if (commande.equals("envoyermission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\nenvoyermission;Nom du consultant;Libellé de la mission");
        }
    }
}
