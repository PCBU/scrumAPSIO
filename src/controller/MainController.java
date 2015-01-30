package controller;

import model.Consultant;
import service.ConsultantService;
import view.MainView;

import java.util.ArrayList;

public class MainController {

    private MainView mainView;
    private ArrayList<Consultant> consultants;

    //Constructeur-------------------------------------------------------
    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;

        consultants = ConsultantService.listeConsultants();
    }

    //Les commandes ---------------------------------------------------
    public void commande(String commande) {

        String[] splitCommande = commande.split(";");

        if (splitCommande[0].equals("listeconsultant")) {
            mainView.afficher("Liste des consultants :");
            for (Consultant consultant : consultants) {
                mainView.afficher(consultant.toString());
            }

        } else if (splitCommande[0].equals("listeconsultantlibre")) {
            mainView.afficher("Liste des consultants actuellement libre :");

        } else if (splitCommande[0].equals("clear")) {
            mainView.effacer();

        } else if (splitCommande[0].equals("creermission")) {


            // Ajoute un consultant, les paramètres sont le nom, prenom, adresse, telephone et doivent être séparer par ';'
        } else if (splitCommande[0].equals("creerconsultant")) {
            if (splitCommande.length == 5) {
                ConsultantService.ajoutConsultant(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

            } else {
                mainView.afficher("Il y as une erreur de syntaxe. Commande : creerconsultant;nom;prenom;adresse;telephone");

            }

        } else { //cas ou la commande n'est pas reconnue
            mainView.afficher("commande '" + commande + "' inconnue.");
        }

    }


}
