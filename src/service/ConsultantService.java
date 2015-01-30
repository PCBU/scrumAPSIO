package service;

import model.Consultant;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class ConsultantService {

	private static ArrayList<Consultant> listeConsultants = new ArrayList<Consultant>();
	
	public static ArrayList<Consultant> listeConsultants()
	{

		listeConsultants.add(new Consultant("Nguyen","Eric","35, impasse Francois Mitterand 31100 Toulouse","0545842539"));
		listeConsultants.add(new Consultant("Martin","Thomas","41, Avenue du tournesol 31500 Toulouse","0522332539"));
		listeConsultants.add(new Consultant("Moulin","Etienne","12, rue du pinson 31540 Toulouse","0522339865"));
		listeConsultants.add(new Consultant("Roda","Sophie","11, Boulevard des Etats-Unis 31440 Toulouse","0422332539"));
		
		return listeConsultants;		
	}

	public static void ajoutConsultant(String nom, String prenom, String adresse, String telephone)
	{
		listeConsultants.add(new Consultant(nom, prenom, adresse, telephone));

	}


	public static ArrayList<Consultant> consultantsDisponibles(ArrayList<Consultant> listeConsultants) {
		ArrayList<Consultant> consultantsDisponibles = new ArrayList<Consultant>();

		return consultantsDisponibles;
	}

	public static Optional<Consultant> getFirstConsultantByNom(ArrayList<Consultant> consultants, String nom) {

		Optional<Consultant> validConsultant = ofNullable(null);

		for (Consultant consultant : consultants) {
			if (consultant.getNom().equals(nom)) {
				validConsultant = of(consultant);
			}
		}

		return validConsultant;
	}

}
