package at.htl.common.transfer;

import java.io.Serializable;

/**
 * @timeline LoginPackage
 * 31.10.2015: MET 005  created class
 * 19.11.2015: PON 002  added port
 */
public class LoginPackage implements Serializable {

    private String firstname;
    private String lastname;
    private int catalogNr;
    private String enrolmentID;
    private String serverIP;
    private String dirOfWatch;
    private int port;

    /**
     * is the package the client sends the teacher.
     * the transfer contains information from the client.
     *
     * @param firstname  Specialises the firstname of the client.
     * @param lastname   Specialises the name of the client.
     * @param catalogNr  Specialises the catalog number of the client.
     * @param enrolmentID Specialises the enrolmentID of the client.
     * @param serverIP   Specialises the ip-address from the teacher.
     * @param dirOfWatch Specialises the directory where the client will work.
     * @param port       Specialises the port where the two will communicate.
     */
    public LoginPackage(String firstname,
                        String lastname,
                        int catalogNr,
                        String enrolmentID,
                        String serverIP,
                        String dirOfWatch,
                        int port) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.catalogNr = catalogNr;
        this.enrolmentID = enrolmentID;
        this.serverIP = serverIP;
        this.dirOfWatch = dirOfWatch;
        this.port = port;
    }

    //region Getter and Setter


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getCatalogNr() {
        return catalogNr;
    }

    public String getEnrolmentID() {
        return enrolmentID;
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