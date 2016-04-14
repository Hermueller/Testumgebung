package at.htl.server;

import at.htl.common.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @timeline .
 * 15.10.2015: PHI 001  created class
 * 15.10.2015: PHI 045  Akzeptieren von Students durch Thread implementiert
 * 26.10.2015: MET 030  Verbesserung des Codes
 * 27.10.2015: PHI 015  Socketproblem und Portproblem gelöst (continue)
 * 31.10.2015: MET 005  statt continue interrupt()
 * 01.12.2015: PHI 003  Umstrukturierung für bessere Testfreundlichkeit (mocking)
 * 21.03.2016: PHI 001  catch all exceptions and print them into the log
 */
public class Threader implements Runnable {

    private ServerSocket ss = null;
    private boolean isContinue = true;

    public Threader() {
    }

    public void run() {
        try {
            ss = createServerSocket();
            while (isContinue) {
                Socket socket = ss.accept();
                System.out.println("Connection from " + socket);
                createTeacherServer(socket);
            }
        } catch (IOException e) {
            FileUtils.log(this, Level.INFO, e.getMessage());
        }
    }

    /**
     * creates a socket which will work as an server.
     *
     * @return the server-socket.
     */
    public ServerSocket createServerSocket() {
        try {
            return new ServerSocket(Server.getPORT());
        } catch (IOException e) {
            FileUtils.log(this, Level.ERROR, e.getMessage());
            return null;
        }
    }

    /**
     * creates a new server for a client which will work as a teacher.
     *
     * @param socket the socket which will communicate with the students.
     * @return the success of it.
     */
    public boolean createTeacherServer(Socket socket) {
        try {
            new Server(socket);
        } catch (IOException | ClassNotFoundException e) {
            FileUtils.log(this, Level.ERROR, e.getMessage());
        }
        return true;
    }

    /**
     * stops the server and closes it.
     */
    public void stop() {
        isContinue = false;
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException e) {
                FileUtils.log(this, Level.WARN, e.getMessage());
                System.out.println("can't close ServerSocket");
            }
        }
    }
}
