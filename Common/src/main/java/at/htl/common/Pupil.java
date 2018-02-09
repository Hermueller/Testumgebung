package at.htl.common;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @timeline Pupil
 * 26.10.2015: MET 001  created class
 * 26.10.2015: MET 005  fields: catalogNumber, firstName, lastName, path, startTime
 * 13.11.2015: MET 002  added field enrolmentID (Matrikelnummer)
 */
public class Pupil implements Serializable {

    private final static long serialVersionUID = 1L;

    private int catalogNumber;
    private String enrolmentID;
    private String firstName;
    private String lastName;
    private String pathOfProject;
    private LocalDateTime startTime;

    /**
     * @param catalogNumber specialises the catalog number of the client.
     * @param enrolmentID   specialises the enrolmentID (Matrikelnummer) of the client
     * @param firstName     specialises the first name of the client
     * @param lastName      specialises the last name of the client
     * @param pathOfProject specialises the directory where the client will work
     */
    public Pupil(int catalogNumber,
                 String enrolmentID,
                 String firstName,
                 String lastName,
                 String pathOfProject) {
        this.catalogNumber = catalogNumber;
        this.enrolmentID = enrolmentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pathOfProject = pathOfProject;
        this.startTime = LocalDateTime.now();
    }

    //region Getter and Setter
    public int getCatalogNumber() {
        return catalogNumber;
    }

    public String getEnrolmentID() {
        return enrolmentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPathOfProject() {
        return pathOfProject;
    }

    @SuppressWarnings("unused")
    public void setPathOfProject(String pathOfProject) {
        this.pathOfProject = pathOfProject;
    }

    @SuppressWarnings("unused")
    public LocalDateTime getStartTime() {
        return startTime;
    }
    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pupil)) return false;

        Pupil pupil = (Pupil) o;

        return catalogNumber == pupil.catalogNumber &&
                enrolmentID.equals(pupil.enrolmentID) &&
                firstName.equals(pupil.firstName) &&
                lastName.equals(pupil.lastName) &&
                pathOfProject.equals(pupil.pathOfProject) &&
                startTime.equals(pupil.startTime);
    }

    @Override
    public int hashCode() {
        int result = catalogNumber;
        result = 31 * result + enrolmentID.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + pathOfProject.hashCode();
        result = 31 * result + startTime.hashCode();
        return result;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return String.format("%2d: %s", catalogNumber, lastName);
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
