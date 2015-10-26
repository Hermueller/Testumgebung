package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.Student;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

class SocketReaderThread extends Thread {

    private final Student student;
    private final ObjectInputStream in;
    private final TeacherServer server;

    public SocketReaderThread(
            Student student,
            ObjectInputStream in,
            TeacherServer server) {
        super("Reader from " + student.getName());
        this.student = student;
        this.in = in;
        this.server = server;
    }

    public void run() {
        while (true) {
            try {
                byte[] img = (byte[]) in.readObject();
                final BufferedImage image = ImageIO.read(
                        new ByteArrayInputStream(img));
                server.saveImage(image, student);
            } catch (Exception ex) {
                System.out.println("Exception occurred: " + ex);
                ex.printStackTrace();
                server.shutdown();
                return;
            }
        }
    }

    public void close() {
        try {
            in.close();
        } catch (IOException ignore) {
        }
    }

}