package at.htl.remotecontrol.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

/**
 * Gnadlinger:  15.10.2015  Verwaltung der Gui-Eingabewerte inplementiert
 * Philipp   :  19.10.2015  Erweiterung um eine Liste der verbundenen Studenten
 * Patrick   :  24.10.2015  Erweiterung um den String "screenshotPath"
 * Tobias    :  26.10.2015  Singleton-Pattern korrigiert und Klasse umbenannt
 */
public class Session {

    private static Session instance = null;
    private ObservableList<String> users;
    private long time;
    private String screenshotPath;

    protected Session() {
        users = FXCollections.observableList(new LinkedList<String>());
    }

    /**
     * Eine Stunde mit 3000 Millisekunden sind ca. 1 200 MB von Screenshots
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void addUser(String user) {
        users.add(user);
    }

    //region Getter and Setter
    public ObservableList<String> getObservableList() {
        return users;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }
    //endregion

}
