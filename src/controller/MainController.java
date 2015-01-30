package controller;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import model.Mission;
import service.ConsultantService;
import service.MissionService;
import view.MainView;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.of;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static service.ConsultantService.listeConsultants;

public class MainController {

    private MainView mainView;
    
    private ArrayList<Mission> missions;

    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;
        
        this.missions = new ArrayList<Mission>();
    }

    public void commande(String commande) {

		String[] splitCommande = commande.split(";");

        if (splitCommande[0].equals("listeconsultant")) {
            ArrayList<Consultant> listeConsultants = listeConsultants();
            mainView.afficher("Liste des consultants :");
            for (Consultant consultant : listeConsultants) {
                mainView.afficher(consultant.toString());
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
