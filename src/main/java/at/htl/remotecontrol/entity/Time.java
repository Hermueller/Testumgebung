package at.htl.remotecontrol.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.*;

/**
 * Gnadlinger:  15.Oktober.2015  Verwaltung der Gui-Eingabewerte inplementiert
 * Philipp   :  19.Oktober.2015  erweitert um eine Liste der verbundenen Studenten
 *
 */
public class Time {
    public static Time instance = null;
    private static long time = 3000;
    private ObservableList users = FXCollections.observableList(new LinkedList());
    private List<String> ips = new LinkedList();

    /**
     * Eine Stunde mit 3000 milliSekunden sind ca. 1 200MB von Screenshots
     *
     */
    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    public static void setTime(long newtime) {
        time=newtime;
    }

    public long getTime() {
        return time;
    }

    public ObservableList getObservableList() {
        return users;
    }

    public void addUser(String user) {
        users.add(user);
    }

    public List getIps() {
        return ips;
    }

    public void addIP(String ip) {
        ips.add(ip);
    }
}
