package at.htl.remotecontrol.gui;

import at.htl.remotecontrol.entity.Time;
import at.htl.remotecontrol.server.TeacherServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Philipp:  15.Oktober.2015  akzeptieren von Students durch Thread implementiert
 */
public class Threader implements Runnable {

    public Threader() {

    }

    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(TeacherServer.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket socket = ss.accept();
                System.out.println("Connection From " + socket);
                Time.getInstance().addIP(socket.getInetAddress().toString());
                new TeacherServer(socket);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
