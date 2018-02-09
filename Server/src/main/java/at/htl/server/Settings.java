package at.htl.server;

import at.htl.common.enums.StudentState;
import at.htl.common.fx.StudentView;
import at.htl.common.io.FileUtils;
import at.htl.common.io.ScreenShot;
import at.htl.common.transfer.Packet;
import at.htl.server.entity.Interval;
import at.htl.server.entity.Student;
import at.htl.server.view.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static at.htl.common.transfer.Packet.Action;
import static at.htl.common.transfer.Packet.Resource;

/**
 * @timeline Settings
 * 15.10.2015: GNA 001  created class (name: "Time")
 * 15.10.2015: GNA 020  Verwaltung der Gui-Eingabewerte inplementiert
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
 * 06.01.2016: GNA 150  fixed bugs that occur after the start
 * 10.02.2016: PON 005  Für Testzwecke wird überprüft ob eine Listview in Studentview initializiert wurde
 * 10.02.2016: PON 001  Bug fixed: Sceenshots to Screenshots
 * 21.03.2016: PHI 020  write error to the log in the application
 * 07.05.2016: PHI 020  improved the code from the chart (remembers how many lines of code for each filter)
 * 07.05.2016: PHI 035  improved exception handling
 * 08.05.2016: PHI 005  added new method (calculateTime).
 * 11.05.2016: PHI 015  added the "initialize-" Methods + fixed the inputs to the Log-View
 * 12.05.2016: PHI 035  added the Log-Filter Methods
 * 13.05.2016: PHI 001  changes the color of the students.
 * 06.06.2016: PHI 015  implemented the methods for the screenshot properties.
 * 11.06.2016: PHI 045  recovered lost code from the last merge AND implemented student count
 * 12.06.2016: PHI 020  improved student login method.
 * 14.04.2016: PHI 030  sorted list of the students + fixed bugs in the findStudentByAddress-Method
 * 17.04.2016: PHI 030  fixed sort-bug and implemented the ListView
 */
public class Settings {

    private static Settings instance = null;

    private ObservableList<Button> students;
    private List<Student> studentsList = new LinkedList<>();
    private AnchorPane logArea;
    private File handOutFile = null;
    private LocalTime startTime;
    private LocalTime endTime = LocalTime.now().plusHours(3);
    private Interval interval;
    private String path;
    private String pathOfImages;
    private String pathOfHandOutFiles;
    private String pathOfExports;
    private String password;
    private LocalDateTime starting = null;
    private StackedAreaChart<Number, Number> chart;
    private String[] endings;
    private MediaPlayer mediaPlayer = null;
    private Label lbLoc;
    private boolean looksAtScreenshots;
    private String actualScreenshot;
    private List<String> ListOfScreenshots;
    private HashMap<String, Color> filterColors = new HashMap<>();
    private HashMap<String, List<TextField>> logFields = new HashMap<>();
    private String currentLogFilter = "ALL";
    private long sleepTime = 5000;
    private ScreenShot screenShot = new ScreenShot();
    private Label lbCount;
    private int studentCount = 0;

    private Settings() {
        students = FXCollections.observableList(new LinkedList<>());
        endings = ("*.java; *.fxml; *.css; *.xhtml; *.html").split(";");
        ListOfScreenshots = new ArrayList<>();
        looksAtScreenshots = false;
        initializeColors();
        initializeFilters();
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    //region Getter and Setter

    public ScreenShot getScreenShot() {
        return screenShot;
    }


    public void setLbCount(Label lbCount) {
        this.lbCount = lbCount;
    }

    public void setCurrentLogFilter(String currentLogFilter) {
        this.currentLogFilter = currentLogFilter;
    }

    public HashMap<String, List<TextField>> getLogFields() {
        return logFields;
    }

    public HashMap<String, Color> getFilterColors() {
        return filterColors;
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

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * @return the list of students.
     */
    public ObservableList<Button> getObservableList() {
        return students;
    }

    /**
     * @return the TextArea of the logging
     */
    public AnchorPane getLogArea() {
        return logArea;
    }

    /**
     * sets the TextArea which is used for the logging
     *
     * @param logArea the TextArea where the log will be shown
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
    public Packet getPacket() {
        Packet packet = new Packet(Action.HAND_OUT, "HandOutPackage");
        packet.put(Resource.FILE, FileUtils.fileToByteArray(getHandOutFile()));
        packet.put(Resource.FILE_EXTENSION, FileUtils.getFileExtension(getHandOutFile()));
        packet.put(Resource.TIME, getEndTime());
        packet.put(Resource.COMMENT, "Good Luck!");
        return packet;
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

    public void incrementEndTime(int minutes) {
        this.endTime.plusMinutes(minutes);
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
     * @return the path of the directory of the exports
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
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
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
    public void setChart(StackedAreaChart<Number, Number> chart) {
        this.chart = chart;
    }

    public StackedAreaChart<Number, Number> getChart() {
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

    public List<Student> getStudentsList() {
        return studentsList;
    }

    //endregion

    //region Methods

    //region Student-Actions

    /**
     * shows the number of logged in students on the teacher GUI
     *
     * @param addOne if the counter increasing by one then TRUE.
     */
    public void changeStudentCount(boolean addOne) {
        String text = "";
        if (addOne) {
            studentCount++;
        } else {
            studentCount--;
        }
        if (studentCount < 10) {
            text = "0";
        }
        lbCount.setText(text + studentCount);
        System.out.println(text + studentCount);
    }

    /**
     * Notifies the teacher that the client has logged in.
     *
     * @param student the client who logged in.
     */
    public void loginStudent(final Student student, final String studentNameBefore) {
        Platform.runLater(() -> {
            student.setStudentState(StudentState.NORMAL);
            for (Button btn : students) {
                if (btn.getText().equals(studentNameBefore)) {
                    btn.setStyle("-fx-background-color: lawngreen");
                    if (!student.getPupil().getLastName().equals(studentNameBefore)) {
                        btn.setText(student.getPupil().getLastName() + " " + student.getPupil().getFirstName().substring(0, 3));
                    }
                    break;
                }
            }
            if (students.size() > 1) {
                sortList();
            }
            changeStudentCount(true);
            StudentList.getStudentList().refreshList(studentsList);
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
            for (Student stud : studentsList) {
                if (stud.getPupil().getEnrolmentID().equals(student.getPupil().getEnrolmentID())) {
                    stud.setStudentState(StudentState.FINISHED);
                }
            }
            StudentList.getStudentList().refreshList(studentsList);
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
            changeStudentCount(false);
            StudentList.getStudentList().refreshList(studentsList);
        });
    }

    /**
     * searches for a student by his/her name
     *
     * @param name of the Student
     * @return the StudentObject with the correct name
     */
    @Deprecated
    public Student findStudentByName(String name) {
        for (Student _student : studentsList) {
            if (_student.getPupil().getLastName().equals(name)) {
                return _student;
            }
        }
        return null;
    }

    /**
     * searches for a student by his InetAddress.
     *
     * @param address the address of the student.
     * @return the student with the correct address.
     */
    public Student findStudentByAddress(String address) {
        for (Student _student : studentsList) {
            if (_student.getStudentAddress().toString().equals(address)) {
                return _student;
            }
        }
        return null;
    }

    /**
     * Adds a client to the list of all students and colours him red.
     *
     * @param student Specialises the client which will be added.
     */
    public void addStudent(final Student student) {
        student.setStudentState(StudentState.NORMAL);
        studentsList.add(student);
        Controller conn = new Controller();
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem show = new MenuItem("show:");
        MenuItem name = new MenuItem(student.getPupil().getFirstName() + " " + student.getPupil().getLastName());
        MenuItem enrolmentID = new MenuItem(student.getPupil().getEnrolmentID());
        MenuItem foo = new MenuItem("remove");
        InetAddress abc = student.getStudentAddress();

        StudentList.getStudentList().refreshList(studentsList);

        if (StudentView.getInstance().getLv() != null)
            Platform.runLater(() -> {
                Button btn = new Button(student.getPupil().getLastName() + " " + student.getPupil().getFirstName().substring(0, 3));
                btn.setOnAction(event -> StudentView.getInstance().getLv().getSelectionModel().select(btn));
                btn.setPrefWidth(StudentView.getInstance().getLv().getPrefWidth() - 50);
                btn.setStyle("-fx-background-color: crimson");
                foo.setOnAction(event -> {
                    conn.kickStudent();
                    getObservableList().remove(btn);
                    studentsList.remove(student);
                });
                contextMenu.getItems().addAll(name, foo);
                btn.setContextMenu(contextMenu);
                btn.setId(student.getStudentAddress().toString());
                students.add(btn);

                sortList();
            });
    }

    /**
     * sorts the list of the students by their name. A -> Z
     */
    private void sortList() {
        studentsList.sort((o1, o2) -> o1.getStudentState().compareToStudentState(o2.getStudentState()));
    }

    //endregion

    //region Chart-Actions

    /**
     * initializes the colors for each file extension name.
     */
    private void initializeColors() {
        filterColors.put(".java", Color.PALEVIOLETRED);
        filterColors.put(".cs", Color.ORANGE);
        filterColors.put(".c", Color.LIGHTGOLDENRODYELLOW);
        filterColors.put(".py", Color.CADETBLUE);
        filterColors.put(".html", Color.PINK);
        filterColors.put(".js", Color.RED);
        filterColors.put(".xhtml", Color.CYAN);
        filterColors.put(".css", Color.DARKSLATEGREY);
        filterColors.put(".fxml", Color.WHITESMOKE);
        filterColors.put(".sql", Color.BLACK);
        filterColors.put(".cshtml", Color.WHITE);
        filterColors.put(".xml", Color.CHOCOLATE);
        filterColors.put(".xsd", Color.FORESTGREEN);
        filterColors.put(".xsl", Color.PURPLE);
    }

    /**
     * Add the Number of Lines in the code to the chart.
     *
     * @param locs    Specialises the number of lines in the code.
     * @param student Specialises the client who owes the file of code.
     */
    public void addValue(long[] locs, Student student) {

        if (locs.length < 1) {
            return;
        }

        //saves values to the client
        student.addValueToLast(locs, calculateTime());
    }

    /**
     * calculates the time (x-axis point) for the chart.
     *
     * @return the time for the chart in seconds. (x-axis point)
     */
    public long calculateTime() {
        //set start-time
        if (starting == null) {
            starting = LocalDateTime.now();
        }
        LocalDateTime now = starting;

        //calculate time in seconds
        long _hours = now.until(LocalDateTime.now(), ChronoUnit.HOURS);
        now = now.plusHours(_hours);

        long _minutes = now.until(LocalDateTime.now(), ChronoUnit.MINUTES);
        now = now.plusMinutes(_minutes);

        long _seconds = now.until(LocalDateTime.now(), ChronoUnit.SECONDS);

        return _seconds + _minutes * 60 + _hours * 60 * 60;
    }

    //endregion

    //region Exception-Handling (Log-View)

    /**
     * prints the error into the Log in the application.
     *
     * @param t the thread who caught the error
     * @param e the error
     */
    public void printMessage(Thread t, Throwable e) {
        printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getMessage());
    }

    /**
     * prints an error message into the Log-View
     *
     * @param level     Specifies the level of the error.
     * @param stackList Specifies all the messages of the error.
     * @param filter    Specifies the file extension name.
     */
    public void printError(Level level, StackTraceElement[] stackList, String filter, String message) {
        AnchorPane log = getLogArea();
        if (log != null) {
            boolean separator = true;
            for (StackTraceElement ste : stackList) {
                if (separator) {
                    printErrorLine(level, message, true, filter);
                    separator = false;
                } else {
                    printErrorLine(level, ste.toString(), false, filter);
                }
            }
        }
    }

    /**
     * prints one line into the Log-View
     *
     * @param level     Specifies the level of the error.
     * @param message   Specifies the message of the error.
     * @param separator if a white line should be created.
     * @param filter    file extension name.
     */
    public void printErrorLine(Level level, String message, boolean separator, String filter) {
        if (separator) {
            printErrorLine(Level.OFF, LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")), false, filter);
        }

        AnchorPane log = getLogArea();
        TextField tf = new TextField(message);
        tf.setEditable(false);
        tf.setStyle(FileUtils.getStyle(level));
        tf.setPrefHeight(30);
        logFields.get(filter).add(tf);
        logFields.get("ALL").add(tf);

        if (currentLogFilter.equals("ALL") || currentLogFilter.equals(filter)) {
            Platform.runLater(() -> {
                ((VBox) log.getChildren().get(0)).getChildren().add(tf);
                log.setMinHeight(((VBox) log.getChildren().get(0)).getChildren().size() * 30);
            });
        }
    }

    /**
     * initializes the HashMap with lists.
     */
    private void initializeFilters() {
        logFields.put("ALL", new LinkedList<>());
        logFields.put("CONNECT", new LinkedList<>());
        logFields.put("DISCONNECT", new LinkedList<>());
        logFields.put("ERRORS", new LinkedList<>());
        logFields.put("WARNINGS", new LinkedList<>());
        logFields.put("OTHER", new LinkedList<>());
    }

    //endregion

    //region Other-Actions

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

    //endregion

    //endregion
}