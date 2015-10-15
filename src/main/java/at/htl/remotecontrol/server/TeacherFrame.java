package at.htl.remotecontrol.server;

import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

public class TeacherFrame extends JFrame {
  private final TeacherServer server;
  private final SocketWriterThread writer;
  private final JLabel iconLabel = new JLabel();

  public TeacherFrame(String studentName, TeacherServer server,
                      SocketWriterThread writer) {
    super("Screen from " + studentName);
    this.server = server;
    this.writer = writer;

    add(new JScrollPane(iconLabel));
    iconLabel.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        TeacherFrame.this.writer.clickEvent(e);
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowActivated(WindowEvent e) {
        TeacherFrame.this.writer.setActive(true);
      }

      public void windowDeactivated(WindowEvent e) {
        TeacherFrame.this.writer.setActive(false);
      }

      public void windowClosing(WindowEvent e) {
        TeacherFrame.this.server.shutdown();
      }
    });

    pack();
    setVisible(true);
  }

  public void showScreenShot(byte[] bytes) throws IOException {
    final BufferedImage img = ImageIO.read(
        new ByteArrayInputStream(bytes));
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        iconLabel.setIcon(new ImageIcon(img));
        pack();
      }
    });
  }
}