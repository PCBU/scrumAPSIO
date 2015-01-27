package service;

import java.util.ArrayList;

import model.Consultant;

public class ConsultantService {

	public static ArrayList<Consultant> listeConsultants()
	{
		ArrayList<Consultant> listeConsultants = new ArrayList<Consultant>();
		listeConsultants.add(new Consultant("Nguyen","Eric","35, impasse Francois Mitterand 31100 Toulouse","0545842539"));
		listeConsultants.add(new Consultant("Martin","Thomas","41, Avenue du tournesol 31500 Toulouse","0522332539"));
		listeConsultants.add(new Consultant("Moulin","Etienne","12, rue du pinson 31540 Toulouse","0522339865"));
		listeConsultants.add(new Consultant("Roda","Sophie","11, Boulevard des Etats-Unis 31440 Toulouse","0422332539"));
		
		return listeConsultants;		
	}
}
