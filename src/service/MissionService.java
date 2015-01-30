package service;

import model.Mission;

import java.util.ArrayList;

/**
 * Created by Dikyx on 30/01/2015.
 */
public class MissionService {

    public static ArrayList<Mission> listeMissions()
    {
        ArrayList<Mission> listeMissions = new ArrayList<Mission>();
        listeMissions.add(new Mission());

        return listeMissions;
    }
}
