package at.htl.server.entity;

import at.htl.common.fx.StudentView;
import at.htl.common.io.FileUtils;
import at.htl.server.Server;
import at.htl.server.Settings;
import com.sun.tools.classfile.Opcode;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import org.apache.logging.log4j.Level;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * @timeline Student
 * 26.10.2015: MET 001  crated class
 * 26.10.2015: MET 005  Name des Schülers, Verzeichnis der Screenshots
 * 31.10.2015: MET 005  Funktion für Verzeichnis der Screenshots verbessert
 * 31.12.2015: PHI 001  created Getter and Setter of locs and times.
 * 05.01.2016: PHI 065  "Series" will be saved with the student.
 * 06.01.2016: PHI 015  fixed bug(adding new series).
 * 25.03.2016: PHI 010  lines of code will be shown in the student-info TAB
 * 14.04.2016: MET 003  setPathOfImages corrected
 * 04.05.2016: PHI 005  added new variables
 * 07.05.2016: PHI 085  implemented methods to shown how many lines of code for each filter was found in the directory
 * 08.05.2016: PHI 035  fixed bug in stackedAreaChart with the method finishSeries. + fixed addNewestToChart-Method
 * 12.06.2016: PHI 002  added the InetAddress methods.
 * 16.06.2016: PHI 120  implemented the LoC again. Shows only the last 10 points.
 * 17.06.2016: PHI 055  fixed the Chart.
 * 17.06.2016: PHI 005  connected the points in the chart with the user-settings
 */
public class Student {

    private String name;
    private String pathOfWatch;
    private String pathOfImages;
    private String firstName;
    private String enrolmentID;
    private int catalogNumber;

    private InetAddress studentAddress;

    private Server server;
    private String[] filter;
    private Interval interval;

    private List<Long> locs = new LinkedList<>();
    private List<Long> times = new LinkedList<>();
    private List<List<XYChart.Series<Number, Number>>> filterSeries = new LinkedList<>();

    public Student(String name) {
        this.name = name;
    }

    //region Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String[] getFilter() {
        return filter;
    }

    public void setFilter(String[] filter) {
        this.filter = filter;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public void setPathOfImages(String path) {
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

    public InetAddress getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(InetAddress studentAddress) {
        this.studentAddress = studentAddress;
    }

    /**
     * For each filter will be a new series created.
     * <br>
     * is called when:  a student logs in
     */
    public void addSeries() {

        Platform.runLater(() -> {
            if (((Button)StudentView.getInstance().getLv().getSelectionModel().getSelectedItem()).getId()
                    .equals(studentAddress.toString())) {
                Settings.getInstance().getChart().getData().clear();
            }
        });
        filterSeries.clear();

        List<XYChart.Series<Number, Number>> list = new LinkedList<>();
        for (String aFilter : getFilter()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(getName() + "/" + aFilter + "/" + LocalTime.now().toString());
            list.add(series);
        }
        filterSeries.add(list);

    }

    public List<List<XYChart.Series<Number, Number>>> getSeries() {
        return filterSeries;
    }

    /**
     * Adds the LinesOfCodes from each filter to its series.
     *
     * @param locs          Specifies the number of lines in the code for each filter.
     */
    public void addValueToLast(long[] locs) {
        try {
            Platform.runLater(() -> {
                if (filterSeries.size() > 0) {
                    List<XYChart.Series<Number, Number>> list = filterSeries.get(filterSeries.size() - 1);
                    for (int i = 0; i < list.size(); i++) {
                        XYChart.Series<Number, Number> actual = list.get(i);

                        if (actual.getData().size() > Settings.getInstance().getPoints()) {
                            pushData(actual, locs[i]);
                        } else {
                            XYChart.Data<Number, Number> data = new XYChart.Data<>(actual.getData().size(), locs[i]);
                            actual.getData().add(data);
                        }
                    }
                }
            });
        } catch (Exception e) {
            at.htl.server.Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getMessage());
        }
    }

    /**
     * removes the first data object and add one new at the end of the series.
     *
     * @param series    the operating series
     * @param loc       the amount of lines of code
     */
    public void pushData(XYChart.Series<Number, Number> series, long loc) {
        int i;
        for (i = 0; i < series.getData().size() - 1; i++) {
            series.getData().get(i).setYValue(series.getData().get(i+1).getYValue());
        }
        series.getData().get(i).setYValue(loc);
    }

    /**
     * adds for each series the data-point y=0.
     * <br>
     * (if a series in a stackedAreaChart doesn't end at y=0, it will produce
     *      a graphical bug)
     */
    public void finishSeries() {
        try {
            Platform.runLater(() -> {
                if (filterSeries.size() > 0) {
                    List<XYChart.Series<Number, Number>> list = filterSeries.get(filterSeries.size() - 1);
                    for (XYChart.Series<Number, Number> actual : list) {
                        long time = (long) actual.getData().get(actual.getData().size() - 1).getXValue() + 1;

                        XYChart.Data<Number, Number> data = new XYChart.Data<>(time, 0);
                        actual.getData().add(data);
                    }
                }
            });
        } catch (Exception e) {
            at.htl.server.Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getMessage());
        }
    }

    /**
     * adds the latest series to the chart.
     */
    public void addNewestToChart() {
        try {
            Platform.runLater(() -> {
                if (filterSeries.size() > 0) {
                    List<XYChart.Series<Number, Number>> list = filterSeries.get(filterSeries.size() - 1);
                    for (XYChart.Series<Number, Number> actual : list) {
                        // an empty series will produce an exception
                        if (actual.getData().size() == 0) {
                            long time = at.htl.server.Settings.getInstance().calculateTime();
                            actual.getData().add(new XYChart.Data<>(time, 0));
                        }
                        at.htl.server.Settings.getInstance().getChart().getData().add(actual);
                    }
                }
            });
        } catch (Exception e) {
            at.htl.server.Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getMessage());
        }
    }

    public void setPathOfWatch(String pathOfWatch) {
        this.pathOfWatch = pathOfWatch;
    }
    //endregion

    /**
     * To remember the Lines of Code for exactly this client.
     * Saves the Lines of Code.
     *
     * @param _loc  Specifies the lines of code at an specific quickinfo.
     * @param _time Specifies the quickinfo when the program counted the lines.
     */
    public void addLoC_Time(long _loc, long _time) {
        locs.add(_loc);
        times.add(_time);
    }

    @Override
    public String toString() {
        return name;
    }
}