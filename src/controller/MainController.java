package controller;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import service.ConsultantService;
import view.MainView;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.of;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static service.ConsultantService.listeConsultants;

public class MainController {
    private MainView mainView;

    private ArrayList<Consultant> consultants;
    private ArrayList<Client> clients;
    private ArrayList<Mission> missions;

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

        if (splitCommande[0].equals("listeconsultant")) {
            mainView.afficher("Liste des consultants :");
            for (Consultant consultant : consultants) {
                mainView.afficher(consultant.toString());
            }

        } else if (splitCommande[0].equals("creerclient")) {
            if (splitCommande.length == 5) {
                Client nouveauClient = new Client(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]);

                clients.add(nouveauClient);
                enregistrerListeClient();

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
                consultants.add(new Consultant(splitCommande[1], splitCommande[2], splitCommande[3], splitCommande[4]));
                try {
                    enregistrerListeConsultant();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                afficherSyntaxe(splitCommande[0]);
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

                    missions.add(mission);
                    enregistrerListeMission();
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
                    enregistrerListeMission();
                    mainView.afficher("Mission ajoutée\n" + nouvelleMission);

                    break;

                default:
                    afficherSyntaxe(splitCommande[0]);
            }

        } else if (splitCommande[0].equals("listemissionsvacantes")) {
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
        } else if (commande.equals("creerconsultant")) {
            mainView.afficher("Il y a une erreur de syntaxe. Commande : creerconsultant;nom;prenom;adresse;telephone");
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
