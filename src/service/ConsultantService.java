package service;

import model.Consultant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class ConsultantService {

	public static HashMap<String, Consultant> consultants()
	{

		HashMap<String, Consultant> consultants = new HashMap<String, Consultant>();

		consultants.put("Nguyen", new Consultant("Nguyen", "Eric", "35, impasse Francois Mitterand 31100 Toulouse", "0545842539"));
		consultants.put("Martin", new Consultant("Martin", "Thomas", "41, Avenue du tournesol 31500 Toulouse", "0522332539"));
		consultants.put("Moulin", new Consultant("Moulin", "Etienne", "12, rue du pinson 31540 Toulouse", "0522339865"));
		consultants.put("Roda", new Consultant("Roda", "Sophie", "11, Boulevard des Etats-Unis 31440 Toulouse", "0422332539"));
		
		return consultants;
	}

	public static HashMap<String, Consultant> consultantsDisponibles(HashMap<String, Consultant> listeConsultants) {
		HashMap<String, Consultant> consultantsDisponibles = new HashMap<String, Consultant>();

		return consultantsDisponibles;
	}

	public static Optional<Consultant> getFirstConsultantByNom(HashMap<String, Consultant> consultants, String nom) {

		Optional<Consultant> validConsultant = ofNullable(null);

		for (Map.Entry<String, Consultant> entry : consultants.entrySet()) {
			Consultant consultant = entry.getValue();

			if (consultant.getNom().equals(nom)) {
				validConsultant = of(consultant);
			}
		}

		return validConsultant;
	}

}
