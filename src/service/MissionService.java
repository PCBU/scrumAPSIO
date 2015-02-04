package service;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import static service.ConsultantService.consultants;

public class MissionService {

	public static HashMap<String, Mission> tests() {
		HashMap<String, Consultant> consultants = consultants();

		HashMap<String, Mission> missions = new HashMap<String, Mission>();
		missions.put("Mission 1", new Mission(consultants.get("Nguyen"), DateTime.now().withDate(2015, 1, 27), DateTime.now().withDate(2015, 2, 13), "Mission 1", new Client())); //TODO put actual clients
		missions.put("Mission 2", new Mission(consultants.get("Martin"), DateTime.now().withDate(2015, 2, 11), DateTime.now().withDate(2015, 3, 15), "Mission 2", new Client()));
		missions.put("Mission 3", new Mission(consultants.get("Roda"), DateTime.now().withDate(2015, 1, 7), DateTime.now().withDate(2015, 2, 21), "Mission 3", new Client()));
		missions.put("Mission 4", new Mission(DateTime.now().withDate(2015, 2, 8), DateTime.now().withDate(2015, 2, 28), "Mission 4", new Client()));


		return missions;
	}

	/**
	 * Parcours les missions et supprime les consultants qui sont en mission à la date en question
	 * @param consultants
	 * @param missions
	 * @param date
	 * @return HashMap<String, Consultant>
	 */
	public static HashMap<String, Consultant> consultantsDisponiblesPourDate(HashMap<String, Consultant> consultants, HashMap<String, Mission> missions, DateTime date) {
		HashMap<String, Consultant> consultantsDispo = consultants;

		for(Map.Entry<String, Mission> entry : missions.entrySet()){
			Mission uneMission = entry.getValue();
			if(!uneMission.isVaccante()){
				if(uneMission.getDebut().compareTo(date) <= 0 && uneMission.getFin().compareTo(date) >= 0){
					consultantsDispo.remove(uneMission.getConsultant().getNom());
				}
			}
		}
		return consultantsDispo;
	}
	public static HashMap<String, Consultant> consultantsDisponibles(HashMap<String, Consultant> consultants, HashMap<String, Mission> missions) {
		return consultantsDisponiblesPourDate(consultants, missions, DateTime.now());
	}
}