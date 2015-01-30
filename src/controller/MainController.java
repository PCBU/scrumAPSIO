package controller;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import service.ConsultantService;
import view.MainView;

import java.util.ArrayList;

public class MainController {
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.of;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static service.ConsultantService.listeConsultants;

public class MainController {
    private MainView mainView;
    private ArrayList<Consultant> consultants;

    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;

        consultants = ConsultantService.listeConsultants();

        this.missions = new ArrayList<Mission>();
        this.clients = new ArrayList<Client>();
    }

    public void commande(String commande) {

        String[] splitCommande = commande.split(";");

        if (splitCommande[0].equals("listeconsultant")) {
            ArrayList<Consultant> listeConsultants = ConsultantService.listeConsultants();
            mainView.afficher("Liste des consultants :");
            for (Consultant consultant : consultants) {
                mainView.afficher(consultant.toString());
            }

        } else if (splitCommande[0].equals("creerclient")) {
            if(splitCommande.length == 5) {
                Client nouveauClient = new Client(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

                clients.add(nouveauClient);

                for (Client client : clients) {
                    mainView.afficher("Liste des clients actuels : ");
                    mainView.afficher(client.toString());
                }
            } else {
                mainView.afficher("syntaxe incorrecte : creerclient;nom;prenom;adresse;telephone ");
            }
            // Ajoute un consultant, les paramètres sont le nom, prenom, adresse, telephone et doivent être séparer par ';'
        } else if (splitCommande[0].equals("creerconsultant")) {
            if (splitCommande.length == 5) {
                ConsultantService.ajoutConsultant(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

            } else {
                mainView.afficher("Il y as une erreur de syntaxe. Commande : creerconsultant;nom;prenom;adresse;telephone");

            }
            
        } else if (splitCommande[0].equals("listeconsultantlibre")) {
            mainView.afficher("Liste des consultants actuellement libres :");

        } else if (splitCommande[0].equals("clear")) {
            mainView.effacer();

        } else if (splitCommande[0].equals("creermission")) {

            DateTimeFormatter formatter = forPattern("dd-MM-yyyy");

            //Vérifier nombre d'arguments
            switch (splitCommande.length) {
                case 6:

                    ArrayList<Consultant> consultants = listeConsultants(); //TODO TEMPORARY, PLEASE DELETE AND REPLACE WITH ACTUAL LIST
                    Optional<Consultant> consultantMission = ConsultantService.getFirstConsultantByNom(consultants, splitCommande[1]);

                    if (!consultantMission.isPresent()) {
                        afficherSyntaxe(splitCommande[0]);
                        break;
                    }

                    //Optional<Client> clientMission = ClientService.getFirstClientByNom(consultants, splitCommande[5]);
                    Optional<Client> clientMission = of(new Client());

                    Mission mission = new Mission(consultantMission.get(),
                            DateTime.parse(splitCommande[2], formatter),
                            DateTime.parse(splitCommande[3], formatter),
                            splitCommande[4],
                            clientMission.get());

                    missions.add(mission);

                    mainView.afficher("Mission ajoutée\n" + mission);

                    break;

                case 5:

                    //Optional<Client> clientMission = ClientService.getFirstClientByNom(consultants, splitCommande[5]);
                    Optional<Client> client = of(new Client());

                    Mission nouvelleMission = new Mission(DateTime.parse(splitCommande[2], formatter),
                            DateTime.parse(splitCommande[3]),
                            splitCommande[4],
                            client.get());

                    missions.add(nouvelleMission);

                    mainView.afficher("Mission ajoutée\n" + nouvelleMission);

                    break;

                default:
                    afficherSyntaxe(splitCommande[0]);
            }


            //Créer mission

            //Afficher message validation

            //Si arguments invalides, afficher syntaxe correcte

        } else if(splitCommande[0].equals("listemissionsvacantes")){
            mainView.afficher("Liste des missions vacantes :");
            for (Mission mission : missions) {
                if (mission.isVaccante())
                    mainView.afficher(mission.toString());
            }

        } else { //cas ou la commande n'est pas reconnue
            mainView.afficher("commande '" + commande + "' inconnue.");
        }

    }

    private void afficherSyntaxe(String commande) {

        if (commande.equals("creermission")) {
            mainView.afficher("Syntaxe incorrecte. La syntaxe valide est :\ncreermission;Nom consultant (facultatif);Date début jj-mm-aaaa;Date fin jj-mm-aaaa;Libellé;Id client");
        }
    }
}
