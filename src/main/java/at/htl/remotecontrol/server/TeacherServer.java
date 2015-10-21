package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.Time;
import at.htl.remotecontrol.gui.controller.ControllerTeacher;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.*;

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

  public void shutdown() {
    writer.interrupt();
    reader.close();
  }
}