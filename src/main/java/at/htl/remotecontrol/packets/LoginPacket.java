package at.htl.remotecontrol.packets;

import java.io.Serializable;

/**
 * 31.10.2015:  Tobias      Klasse erstellt
 */
public class LoginPacket implements Serializable {

    private String userName;
    private String password;
    private String serverIP;
    private String dirOfWatch;

    public LoginPacket(String userName,
                       String password,
                       String serverIP,
                       String dirOfWatch) {
        this.userName = userName;
        this.password = password;
        this.serverIP = serverIP;
        this.dirOfWatch = dirOfWatch;
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
    //endregion

}