package controller;

import java.util.ArrayList;

import model.Client;
import model.Consultant;
import service.ConsultantService;
import view.MainView;

public class MainController {
    private MainView mainView;
    ArrayList<Client> listeClients = new ArrayList<Client>();
    public MainController(MainView mainView) {
        super();
        this.mainView = mainView;

    }

    public void commande(String commande) {

		String[] splitCommande = commande.split(";");

        if (splitCommande[0].equals("listeconsultant")) {
            ArrayList<Consultant> listeConsultants = ConsultantService.listeConsultants();
            mainView.afficher("Liste des consultants :");
            for (Consultant consultant : listeConsultants) {
                mainView.afficher(consultant.toString());
            }

        } else if (splitCommande[0].equals("creerclient")) {
            if(splitCommande.length == 5) {
                Client nouveauClient = new Client(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

                listeClients.add(nouveauClient);

                for (Client client : listeClients) {
                    mainView.afficher("Liste des clients actuels : ");
                    mainView.afficher(client.toString());
                }
            }
            else  mainView.afficher("syntaxe incorrecte : creerclient;nom;prenom;adresse;telephone ");

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
