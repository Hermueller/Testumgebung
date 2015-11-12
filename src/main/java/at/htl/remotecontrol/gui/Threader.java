package at.htl.remotecontrol.gui;

import at.htl.remotecontrol.server.TeacherServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 15.10.2015:  Philipp     Akzeptieren von Students durch Thread implementiert
 * 26.10.2015:  Tobias      Verbesserung des Codes
 * 27.10.2015:  Philipp     Socketproblem und Portproblem gelöst (continue)
 * 31.10.2015:  Tobias      statt continue interrupt()
 */
public class Threader implements Runnable {

    private ServerSocket ss = null;
    private boolean isContinue = true;

    public Threader() {
    }

    public void run() {
        try {
            ss = new ServerSocket(TeacherServer.PORT);
            while (isContinue) {
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

    public void stop() {
        isContinue = false;
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException e) {
                System.out.println("can't close ServerSocket");
            }
        }
    }
    
}
