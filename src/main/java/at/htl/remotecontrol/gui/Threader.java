package at.htl.remotecontrol.gui;

import at.htl.remotecontrol.server.TeacherServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Philipp:  15.10.2015     Akzeptieren von Students durch Thread implementiert
 * Tobias:   26.10.2015     Verbesserung des Codes
 */
public class Threader implements Runnable {

    public Threader() {
    }

    public void run() {
        try {
            ServerSocket ss = new ServerSocket(5555);
            while (true) {
                Socket socket = ss.accept();
                System.out.println("Connection from " + socket);
                new TeacherServer(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
