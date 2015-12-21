package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.Image;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;
import at.htl.remotecontrol.entity.StudentView;
import at.htl.remotecontrol.packets.LoginPacket;
import javafx.application.Platform;
import javafx.scene.control.TextField;

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
 * 26.10.2015: MET ???  Verbesserung der Methode saveImage()
 * 27.10.2015: PHI 080  Live ÜberwachungsBild wird gesetzt
 * 28.10.2015: PHI 015  Live ÜberwachungsBild wird NUR für den ausgewählten Benutzer gesetzt
 * 29.11.2015: PHI 060  Umänderung auf TextField-liste für die farbige Studentenausgabe
 * 12.12.2015: PHI 010  Kommentieren von Methoden
 */
public class TeacherServer {

    public static int PORT = 5555;

    private final SocketWriterThread writer;
    private final SocketReaderThread reader;

    public TeacherServer(Socket socket) throws IOException, ClassNotFoundException {


        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        socket.getInputStream()));
        System.out.println("waiting for student name ...");

        LoginPacket packet = (LoginPacket) in.readObject();
        Student student = new Student(packet.getUserName(), packet.getDirOfWatch());
        System.out.println("I got the Package: " + packet.getDirOfWatch());
        Session.getInstance().addStudent(student);

        reader = new SocketReaderThread(student, in, this);
        writer = new SocketWriterThread(student, out);

        reader.setDaemon(true);
        writer.setDaemon(true);

        writer.handOut();

        reader.start();
        writer.start();

        System.out.println("finished connecting to " + socket);
    }

    /**
     * It redirects to save and show the screenshot.
     *
     * @param image   Specifies the image which should be saved.
     * @param student Specifies the student from which the screenshot is.
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
     * @param student  Specifies the student from which the screenshot is.
     */
    public void showImage(final String fileName, final Student student) {
        Platform.runLater(() -> {
            TextField selected = (TextField) StudentView.getInstance().getLv()
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