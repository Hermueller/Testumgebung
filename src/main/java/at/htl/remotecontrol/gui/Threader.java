package at.htl.remotecontrol.gui;

import at.htl.remotecontrol.server.TeacherServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Philipp on 15.10.15.
 */
public class Threader implements Runnable {

    public Threader() {

    }

    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket socket = ss.accept();
                System.out.println("Connection From " + socket);
                new TeacherServer(socket);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
