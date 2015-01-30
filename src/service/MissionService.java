package service;

import model.Client;
import model.Consultant;
import model.Mission;

import java.util.ArrayList;

import static service.ConsultantService.listeConsultants;

public class MissionService {

	public static ArrayList<Mission> tests() {
		ArrayList<Consultant> consultants = listeConsultants();

		ArrayList<Mission> listeMissions = new ArrayList<Mission>();
		listeMissions.add(new Mission(consultants.get(0), DateTime.now().withDate(2015, 1, 27), DateTime.now().withDate(2015, 2, 13), "Mission 1", new Client())); //TODO put actual clients
		listeMissions.add(new Mission(consultants.get(1), DateTime.now().withDate(2015, 2, 11), DateTime.now().withDate(2015, 3, 15), "Mission 2", new Client()));
		listeMissions.add(new Mission(consultants.get(3), DateTime.now().withDate(2015, 1, 7), DateTime.now().withDate(2015, 2, 21), "Mission 3", new Client()));

		return listeMissions;
	}
}