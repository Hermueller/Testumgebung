package at.htl.client;

import at.htl.common.Pupil;

/**
 * @timeline .
 * 12.02.2016: MET 005  created class with serverIP, port and pupil
 */
public class Exam {

    private static Exam instance = null;

    private String serverIP;
    private int port;
    private Pupil pupil;

    protected Exam() {
    }

    public static Exam getInstance() {
        if (instance == null) {
            instance = new Exam();
        }
        return instance;
    }

    //region Getter and Setter
    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public void setPupil(Pupil pupil) {
        this.pupil = pupil;
    }
    //endregion

}
