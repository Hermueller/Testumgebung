package at.htl.remotecontrol.common.entity;

import at.htl.remotecontrol.common.io.FileUtils;
import org.apache.logging.log4j.Level;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @timeline Text
 * 26.10.2015: MET 001  Klasse erstellt
 * 30.10.2015: MET 015  Speichern von Images verbessert
 * 08.02.2016: GNA 005  Added Errors in LogFile
 */
public class Image {

    /**
     * save image to a specific location.
     *
     * @param img      Specifies the images which should be saved.
     * @param fileName Specifies the name and location of the file.
     */
    public static void save(BufferedImage img, String fileName) {
        try {
            if (fileName.contains(".jpg"))
                new FileOutputStream(fileName).write(convertToJpg(img));
        } catch (IOException e) {
            FileUtils.log(Image.class, Level.ERROR, "Image failed to save"+ MyUtils.convert(e));
        }
    }

    /**
     * converts a bufferedImage to a JPG
     *
     * @param img Specifies the image which should be converted
     * @return the JPG-File
     * @throws IOException
     */
    private static byte[] convertToJpg(BufferedImage img) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(1.0f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.setOutput(new MemoryCacheImageOutputStream(baos));
        writer.write(null, new IIOImage(img, null, null), writeParam);
        writer.dispose();
        baos.flush();
        return baos.toByteArray();
    }

}
