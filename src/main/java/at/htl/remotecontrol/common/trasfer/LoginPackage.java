package at.htl.remotecontrol.common.trasfer;

import java.io.Serializable;

/**
 * @timeline Text
 * 31.10.2015: MET 005  Klasse erstellt
 * 19.11.2015: PON 002  Port hinzugefügt
 */
public class LoginPackage implements Serializable {

    private String userName;
    private String password;
    private String serverIP;
    private String dirOfWatch;
    private int port;

    /**
     * is the package the client sends the teacher.
     * the trasfer contains information from the client.
     *
     * @param userName   Specialises the name of the client.
     * @param password   Specialises the password the client typed in.
     * @param serverIP   Specialises the ip-address from the teacher.
     * @param dirOfWatch Specialises the directory where the client will work.
     * @param port       Specialises the port where the two will communicate.
     */
    public LoginPackage(String userName,
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