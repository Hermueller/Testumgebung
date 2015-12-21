package at.htl.remotecontrol.entity;

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
 * 26.10.2015: MET ???  Klasse erstellt
 * 30.10.2015: MET ???  Speichern von Images verbessert
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
            System.out.println("Image failed to save!");
            System.out.println(e.getMessage());
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
