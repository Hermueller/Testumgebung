package at.htl.server;

import at.htl.server.entity.Student;
import at.htl.common.actions.TimeShower;
import at.htl.common.fx.StudentView;
import at.htl.common.io.FileUtils;
import at.htl.common.trasfer.HandOutPackage;
import at.htl.server.entity.Interval;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @timeline .
 * 15.10.2015: GNA 001  created class (name: "Time")
 * 15.10.2015: GNA 010  Verwaltung der Gui-Eingabewerte inplementiert
 * 19.10.2015: PHI 015  extended by a list of connected Students (String)
 * 24.10.2015: PHI 003  extended to the String "pathOfImages" (location of the test)
 * 26.10.2015: MET 005  Singleton-Pattern corrected and renamed class from "Time" to "Setting"
 * 26.10.2015: MET 003  list of students instead of String (better because storage options)
 * 27.10.2015: PHI 035  adding and deleting of "Pupils"
 * 30.10.2015: PHI 030  implemented random time between screenshots
 * 30.10.2015: MET 005  created fixed/random "Interval" between screenshots
 * 31.10.2015: MET 010  functionS implemented: set test start and test end
 * 31.10.2015: MET 005  expansion to handOutFile and getHandOutPacket ()
 * 06.11.2015: PON 005  extension by password
 * 29.11.2015: PHI 040  changed the remove- and add-method of the students. (colored textfields)
 * 10.12.2015: PHI 025  created methods which are used for the Lines-of-Code.
 * 12.12.2015: PHI 035  commented methods and changes class-structure.
 * 22.12.2015: PHI 020  displays the student on the teacher-GUI differently.
 * 29.12.2015: PHI 050  fixed bug in the student LogOn and LogOut method. Makes sound if student logs out.
 * 31.12.2015: PHI 020  Method for the search of a student created and changed LineChart.
 * 01.01.2016: PHI 055  Fixed bug in the LineChart and saves students.
 * 02.01.2016: PHI 005  Chart-Hover implemented.
 * 06.01.2016: PHI 045  fixed bug in the method for saving the LineChart-Series.
 * 10.02.2016: PON 005  Für Testzwecke wird überprüft ob eine Listview in Studentview initializiert wurde
 * 10.02.2016: PON 001  Bug fixed: Sceenshots -> Screenshots
 * 21.03.2016: PHI 020  write error to the log in the application
 */
public class Settings {

    private static Settings instance = null;

    private ObservableList<Button> students;
    private List<Student> studentsList = new LinkedList<>();
    private AnchorPane logArea;
    private File handOutFile;
    private LocalTime startTime;
    private LocalTime endTime;
    private Interval interval;
    private String path;
    private String pathOfImages;
    private String pathOfHandOutFiles;
    private String pathOfExports;
    private String password;
    private LocalDateTime starting = null;
    private LineChart<Number, Number> chart;
    private String[] endings;
    private MediaPlayer mediaPlayer = null;
    private Label lbLoc;
    private boolean looksAtScreenshots;
    private String actualScreenshot;
    private List<String> ListOfScreenshots;

    private Settings() {
        students = FXCollections.observableList(new LinkedList<>());
        endings = ("*.java; *.fxml; *.css; *.xhtml; *.html").split(";");
        ListOfScreenshots = new ArrayList<>();
        looksAtScreenshots = false;
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public List<String> getListOfScreenshots() {
        return ListOfScreenshots;
    }

    public void addScreenshot(String screenshot) {
        ListOfScreenshots.add(screenshot);
    }

    public boolean isLooksAtScreenshots() {
        return looksAtScreenshots;
    }

    public void setLooksAtScreenshots(boolean looksAtScreenshots) {
        this.looksAtScreenshots = looksAtScreenshots;
    }

    public String getActualScreenshot() {
        return actualScreenshot;
    }

    public void setActualScreenshot(String actualScreenshot) {
        this.actualScreenshot = actualScreenshot;
    }

    //region Getter and Setter

    /**
     * @return the list of students.
     */
    public ObservableList<Button> getObservableList() {
        return students;
    }

    /**
     * @return      the TextArea of the logging
     */
    public AnchorPane getLogArea() {
        return logArea;
    }

    /**
     * sets the TextArea which is used for the logging
     *
     * @param logArea   the TextArea where the log will be shown
     */
    public void setLogArea(AnchorPane logArea) {
        this.logArea = logArea;
    }

    /**
     * @return the label of the "Lines-of-code"
     */
    public Label getLbLoc() {
        return lbLoc;
    }

    /**
     * @param lbLoc Specifies the label for the lines-of-code-Number
     */
    public void setLbLoc(Label lbLoc) {
        this.lbLoc = lbLoc;
    }

    /**
     * @return the file for the test with the test-questions.
     */
    public File getHandOutFile() {
        return handOutFile;
    }

    /**
     * @param handOutFile Specialises the file for the test.
     */
    public void setHandOutFile(File handOutFile) {
        this.handOutFile = handOutFile;
    }

    /**
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password Specialises the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the package of information for the client.
     */
    public HandOutPackage getHandOutPacket() {
        // Prüfung, ob nötige Daten vorhanden fehlt
        // funktioniert noch nicht
        return new HandOutPackage(handOutFile, endTime, "Good Luck!");
    }

    /**
     * @return the time when the test starts.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime Specialises the time when the test starts.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the time when the test ends.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime Specialises the time when the test ends.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the time to wait for the next screenshot.
     */
    public long getInterval() {
        return interval.getValue();
    }

    /**
     * @return the object which includes the time interval
     */
    public Interval getIntervalObject() {
        return interval;
    }

    /**
     * @param interval Specialises the class with the calculations for the next interval-time.
     */
    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    /**
     * @return the root-path of the directory of the images and finished tests directory.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the path of the directory of screenshots
     */
    public String getPathOfImages() {
        return pathOfImages;
    }

    /**
     * @return the path of the directory of finished tests
     */
    public String getPathOfHandInFiles() {
        return pathOfHandOutFiles;
    }

    /**
     * PathOfExports includes the Logs, JAR-files and the LineCharts of the Students.
     *
     * @return  the path of the directory of the exports
     */
    public String getPathOfExports() {
        return pathOfExports;
    }

    /**
     * sets the path for the directory of the screenshots and finished tests.
     *
     * @param path Specifies the root-path of the screenshots and finished tests
     */
    public void setPath(String path) {
        this.path = path;
        pathOfImages = path + "/screenshots";
        FileUtils.createDirectory(pathOfImages);
        pathOfHandOutFiles = path + "/submissions";
        FileUtils.createDirectory(pathOfHandOutFiles);
        pathOfExports = path + "/exports";
        FileUtils.createDirectory(pathOfExports);

        System.out.println(pathOfImages);
    }

    /**
     * @param chart Specialises the chart which is shown on the screen of the teacher.
     */
    public void setChart(LineChart<Number, Number> chart) {
        this.chart = chart;
    }

    public LineChart<Number, Number> getChart() {
        return chart;
    }

    /**
     * @return the endings of the files (in which the lines are counted).
     */
    public String[] getEndings() {
        return endings;
    }

    /**
     * @param endings The endings of files in which we count the lines.
     */
    public void setEndings(String[] endings) {
        this.endings = endings;
    }

    /**
     * @return the last series from the chart.
     */
    public XYChart.Series<Number, Number> getLastSeries(Student st) {
        if (st.getSeries().size() > 0) {
            return st.getSeries().get(st.getSeries().size() - 1);
        }
        return null;
    }

    //endregion

    //region Methods

    /**
     * Adds a client to the list of all students and colours him red.
     *
     * @param student Specialises the client which will be added.
     */
    public void addStudent(final Student student) {
        studentsList.add(student);

        if (StudentView.getInstance().getLv() != null)
            Platform.runLater(() -> {
                Button btn = new Button(student.getName());
                btn.setOnAction(event -> StudentView.getInstance().getLv().getSelectionModel().select(btn));
                btn.setPrefWidth(StudentView.getInstance().getLv().getPrefWidth() - 50);
                btn.setStyle("-fx-background-color: crimson");
                students.add(btn);
            });
    }

    public List<Student> getStudentsList() {
        return studentsList;
    }

    /**
     * Notifies the teacher that the client has logged in.
     *
     * @param student the client who logged in.
     */
    public void loginStudent(final Student student) {
        Platform.runLater(() -> {
            for (Button btn : students) {
                if (btn.getText().equals(student.getName())) {
                    btn.setStyle("-fx-background-color: yellow");
                    break;
                }
            }
        });
    }

    /**
     * Notifies the Teacher that the client has finished the test.
     * Colors him green in the list.
     *
     * @param student the client who finished the test.
     */
    public void finishStudent(final Student student) {
        Platform.runLater(() -> {
            for (Button btn : students) {
                if (btn.getText().equals(student.getName())) {
                    btn.setStyle("-fx-background-color: lawngreen");
                    break;
                }
            }
        });
    }

    /**
     * Removes a client from the list of all students and/or colours him red.
     *
     * @param studentName Specialises the client to remove from the list.
     */
    public void removeStudent(final String studentName) {
        Platform.runLater(() -> {
            for (Button btn : students) {
                if (btn.getText().equals(studentName)) {
                    btn.setStyle("-fx-background-color: crimson");
                    break;
                }
            }
        });
    }

    /**
     * Add the Number of Lines in the code to the chart.
     *
     * @param _loc    Specialises the number of lines in the code.
     * @param student Specialises the client who owes the file of code.
     */
    public void addValue(Long _loc, Student student, Long priorValue) {
        //set start-time
        if (starting == null) {
            starting = LocalDateTime.now();
        }
        LocalDateTime now = starting;

        //calculate time in seconds
        Long _hours = now.until(LocalDateTime.now(), ChronoUnit.HOURS);
        now = now.plusHours(_hours);

        Long _minutes = now.until(LocalDateTime.now(), ChronoUnit.MINUTES);
        now = now.plusMinutes(_minutes);

        Long _seconds = now.until(LocalDateTime.now(), ChronoUnit.SECONDS);

        Long _time = _seconds + _minutes * 60 + _hours * 60 * 60;

        //saves values to the client
        Student toModify = findStudentByName(student.getName());
        toModify.addLoC_Time(_loc, _time);
        toModify.addValueToLast(_loc, _time, priorValue);

        //show point in the chart
        Platform.runLater(() -> {
            Button selected = (Button) StudentView.getInstance().getLv()
                    .getSelectionModel().getSelectedItem();
            if (selected != null) {
                //if the client is in the Live-View -> show new point
                if (student.getName().equals(selected.getText())) {
                    XYChart.Series<Number, Number> actual = getLastSeries(student);

                    //show time if the cursor is located on this point
                    XYChart.Data<Number, Number> data = new XYChart.Data<>(_time, _loc);
                    data.setNode(
                            new TimeShower(
                                    priorValue,
                                    _loc,
                                    LocalDateTime.now()
                                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            .split("T")[1]
                                            .split("\\.")[0]
                            )
                    );
                    actual.getData().add(data);
                    getLbLoc().setText(Long.toString(_loc));
                }
            }
        });
    }

    /**
     * plays a sound to notify the user about an event
     * plays only the first 3 seconds.
     */
    public void notification() {
        final File file = new File("src/main/resources/sound/Fall.mp3");
        final Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        //mediaPlayer.setStartTime(Duration.seconds(0));
        //mediaPlayer.setStopTime(Duration.seconds(3));
        mediaPlayer.setCycleCount(4);

        mediaPlayer.play();
    }

    /**
     * searches for a student by his/her name
     *
     * @param name  of the Student
     * @return      the StudentObject with the correct name
     */
    public Student findStudentByName(String name) {
        for (Student _student : studentsList) {
            if (_student.getName().equals(name)) {
                return _student;
            }
        }
        return null;
    }

    public void addStudentsFromCsv(File file) throws IOException {
        BufferedReader bis = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), Charset.forName("UTF-16")));

        int nameColumn = 0;
        String line;
        String[] words = bis.readLine().split(";");


        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("Familienname")) {
                nameColumn = i;
            }
        }
        while ((line = bis.readLine()) != null) {
            Settings.getInstance().addStudent(new Student(line.split(";")[nameColumn]));
        }
    }

    /**
     * creates the at.htl.client.styles for the error/warning/info.
     * <p>
     * ERROR's in RED.
     * WARNING's in YELLOW.
     * INFO's in WHITE.
     *
     * @param level     Specifies the level of the error.
     * @return          the at.htl.client.styles for the textfield.
     */
    public String getStyle(Level level) {
        String styleString = "-fx-background-color: transparent;";
        if (level == Level.ERROR) {
            styleString += "-fx-text-fill: crimson";
        } else if (level == Level.WARN) {
            styleString += "-fx-text-fill: yellow";
        } else if (level == Level.INFO) {
            styleString += "-fx-text-fill: white";
        }
        return styleString;
    }

    /**
     * prints the error into the Log in the application.
     *
     * @param t     the thread who caught the error
     * @param e     the error
     */
    public void printMessage(Thread t, Throwable e) {
        AnchorPane log = Settings.getInstance().getLogArea();
        if (log != null) {
            Platform.runLater(() -> {
                String msg2 =
                          t.getName() + " - "
                        + e.getClass().toString() + "\n";
                TextField tf = new TextField(msg2);
                tf.setEditable(false);
                tf.setStyle(getStyle(Level.ERROR));
                tf.setPrefHeight(30);
                ((VBox) log.getChildren().get(0)).getChildren().add(tf);

                log.setMinHeight(((VBox) log.getChildren().get(0)).getChildren().size() * 30);
            });
        }
    }

    //endregion
}