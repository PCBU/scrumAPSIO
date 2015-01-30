package controller;

import java.util.ArrayList;

import model.Consultant;

import model.Mission;
import service.ConsultantService;
import service.MissionService;
import view.MainView;

public class MainController {

    private MainView mainView;
    private ArrayList<Mission> missions;

    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;
        this.missions = MissionService.listeMissions();
    }

    public void commande(String commande) {

		String[] splitCommande = commande.split(";");

        if (splitCommande[0].equals("listeconsultant")) {
            ArrayList<Consultant> listeConsultants = ConsultantService.listeConsultants();
            mainView.afficher("Liste des consultants :");
            for (Consultant consultant : listeConsultants) {
                mainView.afficher(consultant.toString());
            }

        } else if (splitCommande[0].equals("listeconsultantlibre")) {
            mainView.afficher("Liste des consultants actuellement libres :");

        } else if (splitCommande[0].equals("clear")) {
            mainView.effacer();

        } else if (splitCommande[0].equals("creermission")) {
			//Afficher consultants disponibles


			//Vérifier nombre d'arguments



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


}
