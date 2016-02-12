package at.htl.remotecontrol.common;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @timeline .
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
    private LocalDate startTime;

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
        this.startTime = LocalDate.now();
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

    public void setPathOfProject(String pathOfProject) {
        this.pathOfProject = pathOfProject;
    }

    public LocalDate getStartTime() {
        return startTime;
    }
    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pupil)) return false;

        Pupil pupil = (Pupil) o;

        if (catalogNumber != pupil.catalogNumber) return false;
        if (!enrolmentID.equals(pupil.enrolmentID)) return false;
        if (!firstName.equals(pupil.firstName)) return false;
        if (!lastName.equals(pupil.lastName)) return false;
        if (!pathOfProject.equals(pupil.pathOfProject)) return false;
        return startTime.equals(pupil.startTime);

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

}
