package at.htl.remotecontrol.server;

import at.htl.remotecontrol.entity.Time;
import at.htl.remotecontrol.gui.controller.ControllerTeacher;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.*;

public class TeacherServer {
  public static final int PORT = 5555;

  private final SocketWriterThread writer;
  private final TeacherFrame frame;
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
    frame = new TeacherFrame(studentName, this, writer);

    reader.start();
    writer.start();

    System.out.println("finished connecting to " + socket);
  }

  public void showScreenShot(byte[] bytes) throws IOException {
    frame.showScreenShot(bytes);
  }

  public void shutdown() {
    writer.interrupt();
    reader.close();
  }

  //region Main
/*
  public static void main(String[] args) throws Exception {
    ServerSocket ss = new ServerSocket(PORT);
    while (true) {
      Socket socket = ss.accept();
      System.out.println("Connection From " + socket);
      new TeacherServer(socket);
    }
  }*/
  //endregion
}