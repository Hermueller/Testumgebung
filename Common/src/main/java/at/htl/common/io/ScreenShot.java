package at.htl.common.io;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @timeline ScreenShot
 * 30.10.2015: MET 001  created class
 * 30.10.2015: MET 015  improved saving of images and creating screenshots
 * 14.11.2015: MET 030  added custom format, quality, scale and suffix validation
 * 14.11.2015: MET 010  provided class with comments
 * 14.11.2015: MET 005  created enum with toString()
 * 02.01.2016: MET 005  improved saving images by using FileUtils
 * 06.06.2016: PHI 030  dynamical image properties from the user.
 * 17.06.2016: PHI 030  scale can be changed by the user
 */
public class ScreenShot implements Serializable {

    public Format DEFAULT_FORMAT = Format.JPG;
    public float DEFAULT_QUALITY = 0.2f;
    public double DEFAULT_SCALE = 0.2;

    @SuppressWarnings("unused")
    public ScreenShot(Format DEFAULT_FORMAT, float DEFAULT_QUALITY, double DEFAULT_SCALE) {
        this.DEFAULT_FORMAT = DEFAULT_FORMAT;
        this.DEFAULT_QUALITY = DEFAULT_QUALITY;
        this.DEFAULT_SCALE = DEFAULT_SCALE;
    }

    public ScreenShot() {
    }

    //region Getter-Setter

    public void setDEFAULT_FORMAT(Format DEFAULT_FORMAT) {
        this.DEFAULT_FORMAT = DEFAULT_FORMAT;
    }

    public void setDEFAULT_QUALITY(float DEFAULT_QUALITY) {
        this.DEFAULT_QUALITY = DEFAULT_QUALITY;
    }

    public void setDEFAULT_SCALE(double DEFAULT_SCALE) {
        this.DEFAULT_SCALE = DEFAULT_SCALE;
    }

    //endregion

    /**
     * picture format for the sreenshots
     */
    // TODO Auswahl des Formates (PNG oder JPEG) in Property-File
    public enum Format {
        JPG, PNG;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    /**
     * take a screenshot and returns it as a specific format
     *
     * @return ByteArray (Screenshot)
     */
    public byte[] get() {
        return get(null, DEFAULT_FORMAT, DEFAULT_QUALITY, DEFAULT_SCALE);
    }

    /**
     * take a screenshot and returns it as a specific format
     *
     * @param robot   Robot (can also be null)
     * @param format  image format (e.g. jpg)
     * @param quality image quality
     * @param scale   factor for reduction or enlargement
     * @return ByteArray (Screenshot)
     */
    public byte[] get(
            Robot robot, Format format, float quality, double scale) {
        Rectangle shotArea = new Rectangle(
                Toolkit.getDefaultToolkit().getScreenSize());
        try {
            Robot r = (robot == null ? new Robot() : robot);
            BufferedImage img = r.createScreenCapture(shotArea);
            return convert(getScaledImage(img, scale), format, quality);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("failed to make a screenshot");
        }
        return null;
    }

    /**
     * reduced or enlarged BufferedImages
     *
     * @param img   to altered BufferedImage
     * @param scale factor for reduction or enlargement
     * @return BufferedImage with altered size
     */
    private BufferedImage getScaledImage(BufferedImage img, double scale) {
        if (scale != 1.0) {
            int width = (int) (img.getWidth() * scale);
            int height = (int) (img.getHeight() * scale);

            Image scaled = img.getScaledInstance(width, height,
                    BufferedImage.SCALE_AREA_AVERAGING);
            img = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_RGB);
            img.createGraphics().drawImage(
                    scaled, 0, 0, width, height, null);
        }
        return img;
    }

    /**
     * converts a BufferedImage to a image format with custom size
     *
     * @param img     to be converted image
     * @param format  image format (e.g. jpg)
     * @param quality image quality
     * @return ByteArray for later writing as a FileOutputStream
     * @throws IOException
     */
    private byte[] convert(BufferedImage img,
                                  Format format,
                                  float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName(
                format.toString()).next();
        // TODO: PNG-Format isn't working
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(quality);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.setOutput(new MemoryCacheImageOutputStream(baos));
        writer.write(null, new IIOImage(img, null, null), writeParam);
        writer.dispose();
        baos.flush();
        return baos.toByteArray();
    }

    /**
     * stores a ByteArray as a file on the hard disk
     *
     * @param img      to be saved image
     * @param fileName path with filename (e.g. .../Screenshots/example.jpg)
     * @return Successfully saved?
     */
    public boolean save(byte[] img, String fileName) {
        return validSuffix(fileName) && FileUtils.saveAsFile(img, fileName);
    }

    /**
     * checks whether the extension is correct
     *
     * @param fileName to be checked fileName
     * @return Valid fileName?
     */
    public boolean validSuffix(String fileName) {
        String[] split = fileName.split("\\.");
        String suffix = split[split.length - 1];
        try {
            Format.valueOf(suffix.toUpperCase());
            return true;
        } catch (Exception e) {
            System.out.println(String.format("Suffix %s is incorrect!", suffix));
        }
        return false;
    }

}
