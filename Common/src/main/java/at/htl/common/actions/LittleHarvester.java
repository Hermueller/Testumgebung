package at.htl.common.actions;

import at.htl.common.io.ScreenShot;
import at.htl.common.transfer.Packet;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static at.htl.common.transfer.Packet.Action;
import static at.htl.common.transfer.Packet.Resource;

/**
 * @timeline .
 * 13.01.2016: PHI 055  Es werden neben Screenshots auch die Lines of Code eingelesen und versendet.
 * 21.04.2016: PHI 010  added the finished-Variable
 * 07.05.2016: PHI 002  remembers the number of lines for each filter
 */

/**
 * This Harvester counts the lines of code in the test-project and
 * creates a screenshot from the students screen.
 * <p>
 * The lines-of-code -Number and the screenshot will be moved into a package
 * and will be sent to the teacher.
 *
 * @author Philipp Hermueller
 */
public class LittleHarvester implements RobotAction {

    // this is used on the client JVM to optimize transfers
    private static final ThreadLocal<byte[]> previous =
            new ThreadLocal<>();

    private final String studentName;
    private final String studentPath;
    private final String[] filter;
    private final ScreenShot screenShot;

    public LittleHarvester(String studentName, String studentPath, String[] filter, ScreenShot screenShot) {
        this.studentName = studentName;
        this.studentPath = studentPath;
        this.filter = filter;
        this.screenShot = screenShot;
    }

    /**
     * creates screenshot and counts the lines of code.
     *
     * @param robot         The executing robot.
     * @return              The harvested package.
     * @throws IOException  can't open file
     */
    public Object execute(Robot robot) throws IOException {
        byte[] bytes = screenShot.get();
        // only send it if the picture has actually changed
        byte[] prev = previous.get();
        if (prev != null && Arrays.equals(bytes, prev)) {
            return null;
        }
        previous.set(bytes);

        long[] loc = LineCounter.getInstance().countLinesWithFilter(new File(studentPath), filter);

        Packet packet = new Packet(Action.HARVEST, "HarvestedPackage");
        packet.put(Resource.SCREENSHOT, bytes);
        packet.put(Resource.FINISHED, LineCounter.getInstance().isFinished());
        packet.put(Resource.LINES, loc);

        return packet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LittleHarvester that = (LittleHarvester) o;

        return studentName != null ? studentName.equals(that.studentName) : that.studentName == null &&
                (studentPath != null ? studentPath.equals(that.studentPath) : that.studentPath == null);

    }

    @Override
    public int hashCode() {
        int result = studentName != null ? studentName.hashCode() : 0;
        result = 31 * result + (studentPath != null ? studentPath.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "I'm harvesting " + studentName + ".";
    }

}
