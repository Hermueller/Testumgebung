package at.htl.remotecontrol.entity;

import java.io.*;

/**
 * @timeline Text
 * 01.11.2015: MET ???  Klasse erstellt
 * 01.11.2015: MET ???  Senden und Empfangen von Dateien
 */
public class FileStream {

    private static final int BUFFER_SIZE = 16384;

    /**
     * sends a file.
     *
     * @param out  Specifies the stream which is used for sending the file.
     * @param file Specifies the file to send.
     * @return the success of it.
     */
    public static boolean send(ObjectOutputStream out, File file) {
        boolean sent = false;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            System.out.println(String.format("sending %s ... ", file.getName()));
            InputStream fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buffer)) > 0)
                out.write(buffer, 0, len);
            fis.close();
            System.out.println("sending completed: " + file.getName());
            sent = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sent;
    }

    /**
     * gets a file and saves it.
     *
     * @param in   Specifies the stream which is used for receiving the file.
     * @param path Specifies the path where the file is saved.
     * @return the success of it.
     */
    public static boolean receive(ObjectInputStream in, String path) {
        boolean received = false;
        File file = new File(path);
        try {
            System.out.println(String.format("fetching file %s ... ", file.getName()));
            OutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0)
                fos.write(buffer, 0, len);
            fos.close();
            System.out.println("fetching completed: " + file.getName());
            received = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return received;
    }

}
