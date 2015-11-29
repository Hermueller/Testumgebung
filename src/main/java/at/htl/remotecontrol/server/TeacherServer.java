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
 * <p>
 * 21.10.2015:  Philipp     Einfügen der "saveImage()"-Methode zum Speichern der Screenshots
 * 26.10.2015:  Tobias      Verbesserung der Methode saveImage()
 * 27.10.2015:  Philipp     Live ÜberwachungsBild wird gesetzt
 * 28.10.2015:  Philipp     Live ÜberwachungsBild wird NUR für den ausgewählten Benutzer gesetzt
 * 29.11.2015:  Philipp     Umänderung auf TextField-liste für die farbige Studentenausgabe
 */
public class TeacherServer {

    public static int PORT = 5555;

    private final SocketWriterThread writer;
    private final SocketReaderThread reader;

    public TeacherServer(Socket socket)
            throws IOException, ClassNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        socket.getInputStream()));
        System.out.println("waiting for student name ...");

        LoginPacket packet = (LoginPacket) in.readObject();
        Student student = new Student(packet.getUserName(), packet.getDirOfWatch());
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

    public SocketWriterThread getWriter() {
        return writer;
    }

    public SocketReaderThread getReader() {
        return reader;
    }

    public void saveImage(BufferedImage image, Student student) {
        String path = String.format("%s/%s-%s.jpg",
                student.getPathOfImages(),
                student.getName(),
                LocalDateTime.now());
        Image.save(image, path);
        showImage(path, student);
    }

    public void showImage(final String fileName, final Student student) {
        Platform.runLater(new Runnable() {
            public void run() {
                TextField selected = (TextField) StudentView.getInstance().getLv()
                        .getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if (student.getName().equals(selected.getText())) {
                        (StudentView.getInstance().getIv())
                                .setImage(new javafx.scene.image.Image("file:" + fileName));
                    }
                }
            }
        });
    }

    public void shutdown() {
        writer.interrupt();
        reader.close();
    }

}