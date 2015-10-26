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
 * Tobias:  26.10.2015  Klasse erstellt
 */
public class Image {

    public static void save(BufferedImage img, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            if (fileName.contains(".jpg"))
                fos.write(convertToJpg(img));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] convertToJpg(BufferedImage img) throws IOException {
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
