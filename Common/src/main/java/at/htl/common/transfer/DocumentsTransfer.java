package at.htl.common.transfer;

import at.htl.common.MyUtils;
import at.htl.common.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.net.SocketException;

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

    //region Deprecated

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
            FileUtils.log(DocumentsTransfer.class, Level.ERROR, "can not send screenshot to teacher" + MyUtils.exceptionToString(e));
        }
        return sent;
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
    //endregion

    /**
     * sends a packet
     *
     * @param oos    the stream from the socket to the client
     * @param object specialises the HandOutPackage which will be sent to the clients
     * @return TRUE if it was a success.
     */
    public static boolean sendObject(ObjectOutputStream oos, Object object) {
        if (object != null) {
            try {
                oos.writeObject(object);
                oos.reset();
                oos.flush();
                FileUtils.log(DocumentsTransfer.class, Level.INFO, "object sent!");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                FileUtils.log(DocumentsTransfer.class, Level.ERROR, "can't send the object! " + MyUtils.exceptionToString(e));
            }
        }
        return false;
    }


    /**
     * receives a packet
     *
     * @param ois ObjectInputStream
     * @return received object
     */
    public static Object receiveObject(ObjectInputStream ois) {
        try {
            Object object = ois.readObject();
            FileUtils.log(DocumentsTransfer.class, Level.INFO, "object received!");
            return object;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            FileUtils.log(DocumentsTransfer.class, Level.ERROR, "can't receive the object! " + MyUtils.exceptionToString(e));
        }
        return false;
    }

}
