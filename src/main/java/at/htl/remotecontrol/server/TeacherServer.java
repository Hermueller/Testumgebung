package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.Time;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class TeacherServer {
  public static final int PORT = 5555;

  private final SocketWriterThread writer;
  private final SocketReaderThread reader;

  public TeacherServer(Socket socket)
      throws IOException, ClassNotFoundException {
    ObjectOutputStream out = new ObjectOutputStream(
        socket.getOutputStream());
    ObjectInputStream in = new ObjectInputStream(
        new BufferedInputStream(
            socket.getInputStream()));
    System.out.println("waiting for student name ...");
    String studentName = (String) in.readObject();


    reader = new SocketReaderThread(studentName, in, this);
    writer = new SocketWriterThread(studentName, out);

    reader.setDaemon(true);
    writer.setDaemon(true);

    reader.start();
    writer.start();

    System.out.println("finished connecting to " + socket);
  }

  public void saveImage(BufferedImage image, String studentname) {

    LocalDateTime dateTime = LocalDateTime.now();

    FileOutputStream fos = null;
    try {
        File f = new File(Time.getInstance().getScreenshotPath() + "/Sceenshots/" + studentname);
        if(!f.exists()) {
            f.mkdirs();
        }



        fos = new FileOutputStream(f.getPath() +
                "/" +studentname +
                "-" +
                dateTime +
                ".jpg"
        );

      fos.write(convertToJPG(image));

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private byte[] convertToJPG(BufferedImage img)
          throws IOException {
    ImageWriter writer =
            ImageIO.getImageWritersByFormatName("jpg").next();
    ImageWriteParam iwp = writer.getDefaultWriteParam();
    iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    iwp.setCompressionQuality(1.0f);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    writer.setOutput(new MemoryCacheImageOutputStream(bout));
    writer.write(null, new IIOImage(img, null, null), iwp);
    writer.dispose();
    bout.flush();
    return bout.toByteArray();
  }

  public void shutdown() {
    writer.interrupt();
    reader.close();
  }
}