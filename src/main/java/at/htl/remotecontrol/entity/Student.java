package at.htl.remotecontrol.entity;

/**
 * 26.10.2015: Tobias   Klasse erstellt
 * 26.10.2015: Tobias   Name des Schülers, Verzeichnis der Screenshots
 * 31.10.2015: Tobias   Funktion für Verzeichnis der Screenshots verbessert
 */
public class Student {

    private String name;
    private String pathOfWatch;
    private String pathOfImages;

    public Student(String name, String pathOfWatch) {
        this.name = name;
        this.pathOfWatch = pathOfWatch;
        setPathOfImages(Session.getInstance().getPathOfImages());
    }

    //region Getter and Setter
    public String getName() {
        return name;
    }

    public String getPathOfWatch() {
        return pathOfWatch;
    }

    public String getPathOfImages() {
        return pathOfImages;
    }

    private void setPathOfImages(String path) {
        path = String.format("%s/%s", path, name);
        if (Directory.create(path))
            this.pathOfImages = path;
    }
    //endregion

    @Override
    public String toString() {
        return name;
    }

}
