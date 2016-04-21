package at.htl.common.actions;

import at.htl.common.io.ScreenShot;
import at.htl.common.trasfer.HarvestedPackage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * This Harvester counts the lines of code in the test-project and
 * creates a screenshot from the students screen.
 * <p>
 * The lines-of-code -Number and the screenshot will be moved into a package
 * and will be sent to the teacher.
 *
 * @timeline .
 * 13.01.2016: PHI 055  Es werden neben Screenshots auch die Lines of Code eingelesen und versendet.
 */
public class LittleHarvester implements RobotAction {

    // this is used on the client JVM to optimize transfers
    private static final ThreadLocal<byte[]> previous =
            new ThreadLocal<>();

    private final String studentName;
    private final String studentPath;

    public LittleHarvester(String studentName, String studentPath) {
        this.studentName = studentName;
        this.studentPath = studentPath;
    }

    public Object execute(Robot robot) throws IOException {
        byte[] bytes = ScreenShot.get();
        // only send it if the picture has actually changed
        byte[] prev = previous.get();
        if (prev != null && Arrays.equals(bytes, prev)) {
            return null;
        }
        previous.set(bytes);

        LineCounter lc = new LineCounter();
        long loc = lc.countLinesInFilesFromFolder(new File(studentPath));

        return new HarvestedPackage(bytes, loc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LittleHarvester that = (LittleHarvester) o;

        if (studentName != null ? !studentName.equals(that.studentName) : that.studentName != null) return false;
        return studentPath != null ? studentPath.equals(that.studentPath) : that.studentPath == null;

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
