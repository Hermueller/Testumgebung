package at.htl.common.transfer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @timeline .
 * 19.06.2016: MET 001  created class
 * 21.06.2016: MET 030  implementation of a generalization of packets (LoginPackage, HarvestedPackage)
 * 23.06.2016: MET 110  implementation of a generalization of packets (HandOutPackage)
 */
public class Packet implements Serializable {

    public enum Action {
        TEST, GET_PUPIL, SCREENSHOT, HAND_OUT, HAND_IN, LOGIN, HARVEST
    }

    public enum Resource {
        ADDRESS, PUPIL, SCREENSHOT, FINISHED, LINES, FILE, TIME, FILE_EXTENSION, COMMENT
    }

    private HashMap<Resource, Object> resources;
    private Action action;
    private String message;
    private long idleTime;
    private final LocalDateTime timeStamp;

    public Packet(Action action, String message) {
        this.action = action;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
        this.resources = new HashMap<>();
        this.idleTime = 0;
    }

    //region Getter and Setter
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return waiting time in milliseconds (1 s = 1000 ms)
     */
    public long getIdleTime() {
        return idleTime * 1000;
    }

    /**
     * @param idleTime waiting time in seconds
     */
    public void setIdleTime(long idleTime) {
        this.idleTime = idleTime;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    //endregion

    public void put(Resource res, Object obj) {
        resources.put(res, obj);
    }

    public Object get(Resource res) {
        return resources.get(res);
    }

    public void remove(Resource res) {
        resources.remove(res);
    }
}
