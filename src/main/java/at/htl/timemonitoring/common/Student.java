package at.htl.timemonitoring.common;

import at.htl.timemonitoring.common.actions.TimeShower;
import at.htl.timemonitoring.common.io.FileUtils;
import at.htl.timemonitoring.server.Settings;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * @timeline .
 * 26.10.2015: MET 001  crated class
 * 26.10.2015: MET 005  Name des Schülers, Verzeichnis der Screenshots
 * 31.10.2015: MET 005  Funktion für Verzeichnis der Screenshots verbessert
 * 31.12.2015: PHI 001  Getter und Setter für locs und times erstellt.
 * 05.01.2016: PHI 065  "Series" werden nun beim Schüler gespeichert.
 * 06.01.2016: PHI 015  Fehler beim hinzufügen von "Series" entdeckt und ausgebessert.
 * 25.03.2016: PHI 010  lines of code will be shown in the student-info TAB
 */
public class Student {

    private String name;
    private String pathOfWatch;
    private String pathOfImages;
    private String firstName;
    private String enrolmentID;
    private int catalogNumber;

    private List<Long> locs = new LinkedList<>();
    private List<Long> times = new LinkedList<>();
    private List<XYChart.Series<Number, Number>> series = new LinkedList<>();

    public Student(String name, String pathOfWatch) {
        this.name = name;
        this.pathOfWatch = pathOfWatch;
        setPathOfImages(Settings.getInstance().getPathOfImages());
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEnrolmentID() {
        return enrolmentID;
    }

    public void setEnrolmentID(String enrolmentID) {
        this.enrolmentID = enrolmentID;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    private void setPathOfImages(String path) {
        path = String.format("%s/%s", path, name);
        if (FileUtils.createDirectory(path))
            this.pathOfImages = path;
    }

    public List<Long> getLocs() {
        return locs;
    }

    public List<Long> getTimes() {
        return times;
    }

    public void addSeries(XYChart.Series<Number, Number> numberSeries) {
        series.add(numberSeries);
    }

    public List<XYChart.Series<Number, Number>> getSeries() {
        return series;
    }

    /**
     * Adds the LinesOfCode-Number to the last series in the chart.
     *
     * @param loc           Specifies the number of lines in the code.
     * @param time          Specifies the time (in sec.) when to lines where counted.
     * @param priorValue    Specifies the prior number of lines in the code.
     */
    public void addValueToLast(Long loc, Long time, Long priorValue) {
        Platform.runLater(() -> {
            XYChart.Series<Number, Number> actual = series.get(series.size() - 1);

            XYChart.Data<Number, Number> data = new XYChart.Data<>(time, loc);
            data.setNode(
                    new TimeShower(
                            priorValue,
                            loc,
                            LocalDateTime.now()
                                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                    .split("T")[1]
                                    .split("\\.")[0]
                    )
            );
            actual.getData().add(data);

            Settings.getInstance().getLbLoc().setText(Long.toString(loc));
        });
    }

    public void setPathOfWatch(String pathOfWatch) {
        this.pathOfWatch = pathOfWatch;
        setPathOfImages(Settings.getInstance().getPathOfImages());
    }

    //endregion

    @Override
    public String toString() {
        return name;
    }

    /**
     * To remember the Lines of Code for exactly this client.
     * Saves the Lines of Code.
     *
     * @param _loc  Specifies the lines of code at an specific time.
     * @param _time Specifies the time when the program counted the lines.
     */
    public void addLoC_Time(Long _loc, Long _time) {
        locs.add(_loc);
        times.add(_time);
    }
}
