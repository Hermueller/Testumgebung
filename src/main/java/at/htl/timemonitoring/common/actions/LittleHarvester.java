package at.htl.timemonitoring.common.actions;

import at.htl.timemonitoring.common.LineCounter;
import at.htl.timemonitoring.common.trasfer.HarvestedPackage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Liest die Zeilen of Code und erstellt Screenshots bei dem PC
 * des Students und sendet diese verpackt in einem Package zurück.
 *
 * @timeline .
 * 13.01.2016: PHI 055  Es werden neben Screenshots auch die Lines of Code eingelesen und versendet.
 */
public class LittleHarvester implements RobotAction {

    // this is used on the client JVM to optimize transfers
    private static final ThreadLocal<byte[]> previous =
            new ThreadLocal<>();
    private static final float JPG_QUALITY = 1.0f;

    private final double scale = 1.0;

    private final String studentName;
    private final String studentPath;

    public LittleHarvester(String studentName, String studentPath) {
        this.studentName = studentName;
        this.studentPath = studentPath;
    }

    public Object execute(Robot robot) throws IOException {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Rectangle shotArea = new Rectangle(
                defaultToolkit.getScreenSize());
        BufferedImage image = robot.createScreenCapture(shotArea);

        byte[] bytes = convertToJPG(image);
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

    private byte[] convertToJPG(BufferedImage img)
            throws IOException {
        ImageWriter writer =
                ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(JPG_QUALITY);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        writer.setOutput(new MemoryCacheImageOutputStream(bout));
        writer.write(null, new IIOImage(img, null, null), iwp);
        writer.dispose();
        bout.flush();
        return bout.toByteArray();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LittleHarvester that = (LittleHarvester) o;
        return Double.compare(that.scale, scale) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(scale);
        return (int) (temp ^ (temp >>> 32));
    }

    public String toString() {
        return "I'm harvesting " + studentName + ".";
    }

}
