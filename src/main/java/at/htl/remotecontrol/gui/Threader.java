package at.htl.remotecontrol.gui;

import at.htl.remotecontrol.server.TeacherServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Philipp:  15.10.2015     Akzeptieren von Students durch Thread implementiert
 * Tobias:   26.10.2015     Verbesserung des Codes
 * Philipp:  27.10.2015     Socketproblem und Portproblem gelöst
 */
public class Threader implements Runnable {

    private ServerSocket ss = null;
    private boolean _continue = true;

    public Threader() {
    }

    public void run() {
        try {
            ss = new ServerSocket(TeacherServer.PORT);
            while (_continue) {
                Socket socket = ss.accept();
                System.out.println("Connection from " + socket);
                new TeacherServer(socket);
            }
        } catch (IOException e) {
            System.out.println("socket closed");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stopSS() {
        _continue = false;
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException e) {
                System.out.println("can't close ServerSocket");
            }
        }
    }
}
