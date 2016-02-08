package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.FileUtils;
import at.htl.remotecontrol.entity.MyUtils;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;
import at.htl.remotecontrol.packets.HarvestedPackage;
import org.apache.logging.log4j.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Der SocketReaderThread liest Screenshots von unserem Studenten
 * und reicht sie dann an dem TeacherServer weiter, der sie auch
 * sofort anzeigt.
 *
 * @timeline Text
 * 31.10.2015: MET 010  Änderung
 */
class SocketReaderThread extends Thread {

    private final Student student;
    private final ObjectInputStream in;
    private final TeacherServer server;

    private long priorValue = 0;

    public SocketReaderThread(Student student,
                              ObjectInputStream in,
                              TeacherServer server) {
        super("Reader from " + student.getName());
        this.student = student;
        this.in = in;
        this.server = server;
    }

    /**
     * reads images from stream and gives them the teacher-server to save them.
     * counts the lines of code.
     */
    public void run() {
        while (!isInterrupted()) {
            try {

                HarvestedPackage harvestedPackage = (HarvestedPackage) in.readObject();

                // save and show Screenshot
                final BufferedImage image = ImageIO.read(
                        new ByteArrayInputStream(harvestedPackage.getImage()));
                server.saveImage(image, student);

                //save and show Lines of Code
                Session.getInstance().addValue(harvestedPackage.getLoc(), student, priorValue);
                priorValue = harvestedPackage.getLoc();

            } catch (Exception ex) {
                FileUtils.log(this, Level.ERROR, "canceled "+MyUtils.convert(ex));
                /*boolean fetchTest = false;
                try {
                    fetchTest = in.readBoolean();
                } catch (IOException e) {
                    System.out.println("Next item wasn't a Boolean!");
                }
                System.out.println("I GOT " + fetchTest);
                if (fetchTest) {
                    FileStream.receive(in, String.format("%s/%s.zip",
                            Session.getInstance().getPathOfHandInFiles(), student.getName()));
                }*/
                server.shutdown();
                return;
            }
        }
    }

    /**
     * closes the stream.
     */
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            File
            System.out.println("Error by closing of ObjectInputStream!");
        }
    }
}