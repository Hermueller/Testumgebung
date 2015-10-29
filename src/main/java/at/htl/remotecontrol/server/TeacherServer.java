package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.Image;
import at.htl.remotecontrol.entity.Student;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Philipp:  21.10.2015   Einfügen der "saveImage()"-Methode zum speichern der Screenshots
 * Tobias :  26.10.2015   Verbesserung der von saveImage()
 */
public class TeacherServer {

    public static final int PORT = 5555;

    private final SocketWriterThread writer;
    private final SocketReaderThread reader;

    public TeacherServer(Socket socket)
            throws IOException, ClassNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        socket.getInputStream()));
        System.out.println("waiting for student name ...");

        Student student = new Student((String) in.readObject());
        reader = new SocketReaderThread(student, in, this);
        writer = new SocketWriterThread(student, out);

        reader.setDaemon(true);
        writer.setDaemon(true);

        reader.start();
        writer.start();

        System.out.println("finished connecting to " + socket);
    }

    public void saveImage(BufferedImage image, Student student) {
        String path = String.format("%s/%s-%s.jpg",
                student.getDirectory(),
                student.getName(),
                LocalDateTime.now());
        Image.save(image, path, student);
    }

    public void shutdown() {
        writer.interrupt();
        reader.close();
    }

}