package service;

import model.Client;
import model.Consultant;
import model.Mission;
import org.joda.time.DateTime;

import java.util.HashMap;

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
}