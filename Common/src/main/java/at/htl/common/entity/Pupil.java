package at.htl.common.entity;

import java.io.Serializable;

public class Pupil implements Serializable {

    private final static long serialVersionUID = 1L;


    private int catalogNumber;
    private String enrolmentID;
    private String firstName;
    private String lastName;
    private String testFolder;

    public Pupil(int catalogNumber, String enrolmentID, String firstName, String lastName, String testFolder) {
        this.catalogNumber = catalogNumber;
        this.enrolmentID = enrolmentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.testFolder = testFolder;
    }

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

    public String getTestFolder() {
        return testFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pupil)) return false;

        Pupil pupil = (Pupil) o;

        return catalogNumber == pupil.catalogNumber &&
                enrolmentID.equals(pupil.enrolmentID) &&
                firstName.equals(pupil.firstName) &&
                lastName.equals(pupil.lastName) &&
                testFolder.equals(pupil.testFolder);
    }

    @Override
    public int hashCode() {
        int result = catalogNumber;
        result = 31 * result + enrolmentID.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%2d: %s", catalogNumber, lastName);
    }
}
