package at.htl.remotecontrol.server;

import java.io.*;

class SocketReaderThread extends Thread {
  private final String studentName;
  private final ObjectInputStream in;
  private final TeacherServer server;

  public SocketReaderThread(
      String studentName,
      ObjectInputStream in,
      TeacherServer server) {
    super("Reader from " + studentName);
    this.studentName = studentName;
    this.in = in;
    this.server = server;
  }

  public void run() {
    while (true) {
      try {
        byte[] img = (byte[]) in.readObject();
        System.out.println("Received screenshot of " +
            img.length + " bytes from " + studentName);
      } catch (Exception ex) {
        System.out.println("Exception occurred: " + ex);
        ex.printStackTrace();
        server.shutdown();
        return;
      }
    }
  }

  public void close() {
    try {
      in.close();
    } catch (IOException ignore) {
    }
  }
}