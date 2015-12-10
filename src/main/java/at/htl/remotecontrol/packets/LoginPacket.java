package at.htl.remotecontrol.packets;

import java.io.Serializable;

/**
 * 31.10.2015:  Tobias      ??? Klasse erstellt
 * 19.11.2015:  Patrick     ??? Port hinzugefügt
 */
public class LoginPacket implements Serializable {

    private String userName;
    private String password;
    private String serverIP;
    private String dirOfWatch;
    private int port;

    public LoginPacket(String userName,
                       String password,
                       String serverIP,
                       String dirOfWatch,
                       int port) {
        this.userName = userName;
        this.password = password;
        this.serverIP = serverIP;
        this.dirOfWatch = dirOfWatch;
        this.port = port;
    }

    //region Getter and Setter
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getDirOfWatch() {
        return dirOfWatch;
    }

    public int getPort() {
        return port;
    }

    //endregion

}