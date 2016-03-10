package at.htl.timemonitoring.server;

import at.htl.timemonitoring.common.MyUtils;
import at.htl.timemonitoring.common.Student;
import at.htl.timemonitoring.common.io.FileUtils;
import at.htl.timemonitoring.common.trasfer.HarvestedPackage;
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
 * @timeline .
 * 31.10.2015: MET 010  Änderung
 */
class SocketReaderThread extends Thread {

    private final Student student;
    private final ObjectInputStream in;
    private final Server server;

    private long priorValue = 0;

    public SocketReaderThread(Student student,
                              ObjectInputStream in,
                              Server server) {
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

                byte[] img = harvestedPackage.getImage();
                server.saveImage(img, student);

                //save and show Lines of Code
                Settings.getInstance().addValue(harvestedPackage.getLoc(), student, priorValue);
                priorValue = harvestedPackage.getLoc();

            } catch (Exception ex) {
                FileUtils.log(this, Level.ERROR, "canceled " + MyUtils.exToStr(ex));
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
            FileUtils.log(this, Level.ERROR, "Error by closing of ObjectInputStream!" + MyUtils.exToStr(e));
        }
    }
}