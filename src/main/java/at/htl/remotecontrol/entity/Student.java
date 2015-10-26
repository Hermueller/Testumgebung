package at.htl.remotecontrol.entity;

import java.io.File;

/**
 * Tobias:   26.10.2015     Klasse erstellt
 */
public class Student {

    private String name;
    private String directory;

    public Student(String studentName) {
        this.name = studentName;
        setDirectory(Session.getInstance().getImagePath() + studentName);
    }

    //region Getter and Setter
    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }

    private void setDirectory(String directory) {
        this.directory = directory;
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            System.out.println("Verzeichnis ist vorhanden!");
        }
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (!name.equals(student.name)) return false;
        return directory.equals(student.directory);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + directory.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

}
