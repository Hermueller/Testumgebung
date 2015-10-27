package at.htl.remotecontrol.actions;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Philipp:  18.10.2015   Implementieren der Gui
 */
public class ScreenShot implements RobotAction {

    // this is used on the student JVM to optimize transfers
    private static final ThreadLocal<byte[]> previous =
            new ThreadLocal<byte[]>();
    private static final float JPG_QUALITY = 1.0f;

    private final double scale;

    public ScreenShot(double scale) {
        this.scale = scale;
    }

    public ScreenShot() {
        this(1.0);
    }

    public Object execute(Robot robot) throws IOException {
        //long time = System.currentTimeMillis();
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Rectangle shotArea = new Rectangle(
                defaultToolkit.getScreenSize());
        BufferedImage image = robot.createScreenCapture(shotArea);
        if (scale != 1.0) {
            image = getScaledInstance(image);
        }

        byte[] bytes = convertToJPG(image);
        //time = System.currentTimeMillis() - time;
        //System.out.println("time = " + time);
        // only send it if the picture has actually changed
        byte[] prev = previous.get();
        if (prev != null && Arrays.equals(bytes, prev)) {
            return null;
        }
        previous.set(bytes);

        return bytes;
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

    public BufferedImage getScaledInstance(BufferedImage src) {
        int width = (int) (src.getWidth() * scale);
        int height = (int) (src.getHeight() * scale);

        Image scaled = src.getScaledInstance(width, height,
                BufferedImage.SCALE_AREA_AVERAGING);
        BufferedImage result = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB
        );
        result.createGraphics().drawImage(
                scaled, 0, 0, width, height, null);

        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenShot that = (ScreenShot) o;
        return Double.compare(that.scale, scale) == 0;
    }

    public int hashCode() {
        long temp = scale != +0.0d ? Double.doubleToLongBits(scale) : 0L;
        return (int) (temp ^ (temp >>> 32));
    }

    public String toString() {
        return "ScreenShot(" + scale + ")";
    }

}
