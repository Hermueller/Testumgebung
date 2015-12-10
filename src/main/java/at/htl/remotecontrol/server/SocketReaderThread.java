package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.FileStream;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Der SocketReaderThread liest Screenshots von unserem Studenten
 * und reicht sie dann an dem TeacherServer weiter, der sie auch
 * sofort anzeigt.
 * <p>
 * 31.10.2015:  Tobias      ??? Änderung
 */
class SocketReaderThread extends Thread {

    private final Student student;
    private final ObjectInputStream in;
    private final TeacherServer server;

    public SocketReaderThread(Student student,
                              ObjectInputStream in,
                              TeacherServer server) {
        super("Reader from " + student.getName());
        this.student = student;
        this.in = in;
        this.server = server;
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                byte[] img = (byte[]) in.readObject();
                final BufferedImage image = ImageIO.read(new ByteArrayInputStream(img));
                server.saveImage(image, student);
            } catch (Exception ex) {
                System.out.println("canceled");
                FileStream.receive(in, String.format("%s/%s.zip",
                        Session.getInstance().getPathOfHandInFiles(), student.getName()));
                server.shutdown();
                return;
            }
        }
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Error by closing of ObjectInputStream!");
        }
    }
}