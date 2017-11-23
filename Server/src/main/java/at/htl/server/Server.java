package at.htl.server;

import at.htl.common.Pupil;
import at.htl.common.fx.StudentView;
import at.htl.common.io.FileUtils;
import at.htl.common.io.ScreenShot;
import at.htl.common.transfer.Packet;
import at.htl.server.entity.Student;
import javafx.application.Platform;
import javafx.util.Duration;
import org.apache.logging.log4j.Level;
import org.controlsfx.control.Notifications;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static at.htl.common.transfer.Packet.Resource;

/**
 * Die Hauptklasse ist der TeacherServer. Wenn ein Schüler sich mit ihm verbindet,
 * schickt er ein LoginPacket. Sobald dieses Packet verarbeitet wurde, wird der
 * SocketReaderThread und der SocketWriterThread erzeugt, mit denen dann die
 * Netzwerkkommunikation ermöglicht ist.
 *
 * @timeline Server
 * 21.10.2015: PHI 020  Einfügen der "saveImage()"-Methode zum Speichern der Screenshots
 * 26.10.2015: MET 010  Verbesserung der Methode saveImage()
 * 27.10.2015: PHI 080  Live ÜberwachungsBild wird gesetzt
 * 28.10.2015: PHI 015  Live ÜberwachungsBild wird NUR für den ausgewählten Benutzer gesetzt
 * 29.11.2015: PHI 060  Umänderung auf TextField-liste für die farbige Studentenausgabe
 * 12.12.2015: PHI 010  Kommentieren von Methoden
 * 22.12.2015: PHI 001  Ändern von "Hinzufügen" von Schülern zu "Einloggen" von Schülern.
 * 06.01.2016: PHI 025  Fehler gefunden und geändert bei der Anmeldung eines Schülers der schon gespeichert ist.
 * 21.05.2016: PHI 015  shows a notification if a student logs in.
 * 06.06.2016: GNA 030  Changed path of screenshots
 * 06.06.2016: PHI 003  Creates the path extension dynamically.
 * 12.06.2016: PHI 003  removed duplicate code (HandOutPackage was sent twice -> bug)
 * 12.06.2016: PHI 030  the server differs between the IPAddress and not the lastname now.
 * 18.06.2016: PHI 035  a csv-file is created for the lines of code.
 * 21.06.2016: PHI 003  the directory of the student is logged.
 */
public class Server {

    public static int PORT = 50555;

    private final SocketWriterThread writer;
    private final SocketReaderThread reader;

    public Socket socket;

    public Server(Socket socket) throws IOException, ClassNotFoundException {

        this.socket = socket;
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        FileUtils.log(this, Level.INFO, "waiting for client name ...");

        Packet packet = (Packet) in.readObject();
        Pupil pupil = (Pupil) packet.get(Resource.PUPIL);

        Student student;
        String studentNameBefore;
        boolean newStudent = false;

        if (Settings.getInstance().findStudentByAddress(socket.getInetAddress().toString()) != null) {
            student = Settings.getInstance().findStudentByAddress(socket.getInetAddress().toString());
            student.setPupil(pupil);
        } else {
            student = new Student(pupil);
            student.setStudentAddress(socket.getInetAddress());
            Settings.getInstance().addStudent(student);
            newStudent = true;
        }
        studentNameBefore = student.getPupil().getLastName()  + " " + student.getPupil().getFirstName().substring(0,3);
        FileUtils.log(this, Level.INFO, "I got the Package: " + pupil.getPathOfProject());
        Settings.getInstance().loginStudent(student, studentNameBefore);
        student.setServer(this);
        student.setPathOfImages(Settings.getInstance().getPathOfImages());
        student.setFilter(Settings.getInstance().getEndings());
        student.setInterval(Settings.getInstance().getIntervalObject());
        if (newStudent) {
            student.createLocFile();
        }

        reader = new SocketReaderThread(student, in, this);
        writer = new SocketWriterThread(student, out);

        reader.setDaemon(true);
        writer.setDaemon(true);

        reader.start();
        writer.start();

        FileUtils.log(this, Level.INFO, "finished connecting to " + socket);
        Settings.getInstance().printErrorLine(
                Level.INFO, student.getPupil().getLastName() + " logged in!", true, "CONNECT");
        Settings.getInstance().printErrorLine(
                Level.INFO, student.getPupil().getLastName()
                        + ": " + student.getPupil().getPathOfProject(), false, "OTHER");
        Platform.runLater(() -> Notifications.create()
                .title("Student logged in")
                .text("The student '".concat(student.getPupil().getLastName())
                        .concat(" " + student.getPupil().getFirstName())
                        .concat("' logged in."))
                .hideAfter(Duration.seconds(5))
                .showInformation());
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        Server.PORT = PORT;
    }

    /**
     * It redirects to save and show the screenshot.
     *
     * @param image   Specifies the image which should be saved.
     * @param student Specifies the client from which the screenshot is.
     */
    public void saveImage(byte[] image, Student student) {
        LocalDateTime date = LocalDateTime.now();
        //SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd'_'HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String formattedTime = formatter.format(date);
        String path = String.format("%s/%s-%s." + Settings.getInstance().getScreenShot().getFormat().toString(),
                Settings.getInstance().getPathOfImages() + "/" + student.getPupil().getLastName(),
                student.getPupil().getLastName(),
                formattedTime);

        ScreenShot screenShot = Settings.getInstance().getScreenShot();
        screenShot.save(image, path);

        showImage(path, student);
    }

    /**
     * It shows the Image on the Teacher-GUI.
     *
     * @param fileName Specifies the path of the file (screenshot).
     * @param student  Specifies the client from which the screenshot is.
     */
    public void showImage(final String fileName, final Student student) {
        Platform.runLater(() -> {
            /*Button selected = (Button) StudentView.getInstance().getLv()
                    .getSelectionModel().getSelectedItem();*/
            Student selected=StudentList.getStudentList().getSelectedStudent();
            if (selected != null && !Settings.getInstance().isLooksAtScreenshots()) {
                //ist der Screenshot vom ausgewählten Studenten?
                (StudentView.getInstance().getIv())
                        .setImage(new javafx.scene.image.Image("file:" + fileName));
                Settings.getInstance().addScreenshot("file:" + fileName);
                Settings.getInstance().setActualScreenshot(fileName);

            }
        });
    }

    /**
     * Close the Socket-Reader and the Socket-Writer.
     */
    public void shutdown() {
        writer.interrupt();
        reader.close();
    }

}