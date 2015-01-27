package controller;

import java.util.ArrayList;

import model.Consultant;

import service.ConsultantService;
import view.MainView;

public class MainController {

    private MainView mainView;

    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;
    }

    public void commande(String commande) {

		String[] splitCommande = commande.split(" ");

        if (splitCommande[0].equals("listeconsultant")) {
            ArrayList<Consultant> listeConsultants = ConsultantService.listeConsultants();
            mainView.afficher("Liste des consultants :");
            for (Consultant consultant : listeConsultants) {
                mainView.afficher(consultant.toString());
            }

        } else if (splitCommande[0].equals("listeconsultantlibre")) {
            mainView.afficher("Liste des consultants actuellement libre :");

        } else if (splitCommande[0].equals("clear")) {
            mainView.effacer();

        } else if (splitCommande[0].equals("creermission")) {


        } else { //cas ou la commande n'est pas reconnue
            mainView.afficher("commande '" + commande + "' inconnue.");
        }

    }


}
