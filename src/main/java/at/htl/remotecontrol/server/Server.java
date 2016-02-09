package at.htl.remotecontrol.server;

import at.htl.remotecontrol.common.io.FileUtils;
import at.htl.remotecontrol.common.entity.*;
import at.htl.remotecontrol.common.trasfer.LoginPackage;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.apache.logging.log4j.Level;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Die Hauptklasse ist der TeacherServer. Wenn ein Schüler sich mit ihm verbindet,
 * schickt er ein LoginPacket. Sobald dieses Packet verarbeitet wurde, wird der
 * SocketReaderThread und der SocketWriterThread erzeugt, mit denen dann die
 * Netzwerkkommunikation ermöglicht ist.
 *
 * @timeline Text
 * 21.10.2015: PHI 020  Einfügen der "saveImage()"-Methode zum Speichern der Screenshots
 * 26.10.2015: MET 010  Verbesserung der Methode saveImage()
 * 27.10.2015: PHI 080  Live ÜberwachungsBild wird gesetzt
 * 28.10.2015: PHI 015  Live ÜberwachungsBild wird NUR für den ausgewählten Benutzer gesetzt
 * 29.11.2015: PHI 060  Umänderung auf TextField-liste für die farbige Studentenausgabe
 * 12.12.2015: PHI 010  Kommentieren von Methoden
 * 22.12.2015: PHI 001  Ändern von "Hinzufügen" von Schülern zu "Einloggen" von Schülern.
 * 06.01.2016: PHI 025  Fehler gefunden und geändert bei der Anmeldung eines Schülers der schon gespeichert ist.
 */
public class Server {

    public static int PORT = 5555;

    private final SocketWriterThread writer;
    private final SocketReaderThread reader;

    public Server(Socket socket) throws IOException, ClassNotFoundException {


        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        socket.getInputStream()));
        FileUtils.log(this, Level.INFO,"waiting for client name ...");

        LoginPackage packet = (LoginPackage) in.readObject();

        Student student;
        if (Session.getInstance().findStudentByName(packet.getUserName()) != null) {
            student = Session.getInstance().findStudentByName(packet.getUserName());
            if (student.getPathOfWatch() == null) {
                student.setPathOfWatch(packet.getDirOfWatch());
            }
        } else {
            student = new Student(packet.getUserName(), packet.getDirOfWatch());
            Session.getInstance().addStudent(student);
        }
        FileUtils.log(this,Level.INFO,"I got the Package: " + packet.getDirOfWatch());
        Session.getInstance().loginStudent(student);

        reader = new SocketReaderThread(student, in, this);
        writer = new SocketWriterThread(student, out);

        reader.setDaemon(true);
        writer.setDaemon(true);

        writer.handOut();

        reader.start();
        writer.start();

        FileUtils.log(this,Level.INFO,"finished connecting to " + socket);
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
    public void saveImage(BufferedImage image, Student student) {
        String path = String.format("%s/%s-%s.jpg",
                Session.getInstance().getPathOfImages() + "/" + student.getName(),
                student.getName(),
                LocalDateTime.now());
        Image.save(image, path);
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
            Button selected = (Button) StudentView.getInstance().getLv()
                    .getSelectionModel().getSelectedItem();
            if (selected != null) {
                //ist der Screenshot vom ausgewählten Studenten?
                if (student.getName().equals(selected.getText())) {
                    (StudentView.getInstance().getIv())
                            .setImage(new javafx.scene.image.Image("file:" + fileName));
                }
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