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
	
	public void commande(String commande)
	{
		if(commande.equals("listeconsultant"))
		{			
			ArrayList<Consultant> listeConsultants = ConsultantService.listeConsultants();
			mainView.afficher("Liste des consultants :");
			for(Consultant consultant:listeConsultants)
			{
				mainView.afficher(consultant.toString());
			}
		}
		else if(commande.equals("listeconsultantlibre"))
		{
			mainView.afficher("Liste des consultants actuellement libre :");
		}
		else if(commande.equals("clear"))
		{
			mainView.effacer();
		}		
		//cas ou la commande n'est pas reconnue
		else
		{
			mainView.afficher("commande '"+commande+"' inconnue.");
		}
			
	}
	
	
}
