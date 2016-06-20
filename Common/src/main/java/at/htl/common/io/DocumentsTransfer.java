package at.htl.common.io;

import at.htl.common.MyUtils;
import at.htl.common.transfer.HandOutPackage;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.file.Files;

/**
 * @timeline DocumentsTransfer
 * 01.11.2015: MET 001  created class
 * 01.11.2015: MET 040  sending and receiving files
 * 08.02.2016: GNA 020  Errors und Infos in LogFile gespeichert
 * 11.06.2016: PHI 070  Objects will now be sent through sockets.
 * 12.06.2016: PHI 010  Every file extension type can be saved now.
 */
public class DocumentsTransfer {

    private static final int BUFFER_SIZE = 16384;

    /**
     * Instructions (Testangabe) are downloaded to the clients
     *
     * @param out  Specifies the stream which is used for sending the file.
     * @param file Specifies the file to send.
     * @return the success of it.
     */
    @Deprecated
    public static boolean send(ObjectOutputStream out, File file) {
        boolean sent = false;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            FileUtils.log(DocumentsTransfer.class, Level.INFO, String.format("sending %s ... ", file.getName()));
            InputStream fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buffer)) > 0)
                out.write(buffer, 0, len);
            fis.close();
            FileUtils.log(DocumentsTransfer.class, Level.INFO, "sending completed: " + file.getName());
            sent = true;
        } catch (IOException e) {
            FileUtils.log(DocumentsTransfer.class, Level.ERROR, "can not send screenshot to teacher" + MyUtils.exToStr(e));
        }
        return sent;
    }

    /**
     * sends the HandOutPackage to its users.
     *
     * @param oos           The stream from the socket to the client.
     * @param outPackage    Specialises the HandOutPackage which will be sent to the clients
     * @return              TRUE if it was a success.
     */
    public static boolean sendObject(ObjectOutputStream oos, HandOutPackage outPackage) {
        try {
            //Thread.sleep(30000);
            oos.writeObject(outPackage);
            oos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            FileUtils.log(DocumentsTransfer.class, Level.ERROR, "can't send the handOutPackage! " + MyUtils.exToStr(e));
        }
        return false;
    }

    /**
     * gets a file and saves it.
     * Test results (Testabgaben) are collected from the clients
     *
     * @param in   Specifies the stream which is used for receiving the file.
     * @param path Specifies the path where the file is saved.
     * @return the success of it.
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static boolean receive(ObjectInputStream in, String path) {
        boolean received = false;
        File file = new File(path);
        try {
            FileUtils.log(DocumentsTransfer.class, Level.INFO, String.format("fetching file %s ... ", file.getName()));

            OutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0)
                fos.write(buffer, 0, len);
            fos.close();
            FileUtils.log(DocumentsTransfer.class, Level.INFO, "fetching completed: " + file.getName());
            received = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return received;
    }

    /**
     * extracts the information from the received HandOutPackage.
     * Saves the file from the package.
     *
     * @param obj   the received HandOutPackage.
     * @param path  the path where the file will be created.
     * @return      the received package.
     */
    public static HandOutPackage receiveObject(Object obj, String path, String fileName) {
        try {

            HandOutPackage handOutPackage = (HandOutPackage)obj;

            byte[] handout = handOutPackage.getFile();

            if (handout.length != 0) {
                path += "/" + fileName + "." + handOutPackage.getFileExtension();
                File placeToSave = new File(path);
                Files.write(placeToSave.toPath(), handout);
            }

            System.out.println(handOutPackage.getComment());

            return handOutPackage;

        } catch (IOException e) {
            FileUtils.log(DocumentsTransfer.class, Level.ERROR, "failed at receiving the object! " + MyUtils.exToStr(e));
        }
        return null;
    }

}
