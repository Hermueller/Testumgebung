package at.htl.client.business;

import at.htl.client.Client;
import at.htl.client.Exam;
import at.htl.common.Pupil;
import at.htl.common.actions.IpConnection;
import at.htl.common.actions.LineCounter;
import at.htl.common.io.FileUtils;
import at.htl.common.transfer.Address;
import at.htl.common.transfer.Packet;
import org.apache.logging.log4j.Level;

import java.time.LocalTime;

public class TestController {

    Client client;

    boolean loggedIn;

    public boolean login() {

        if (IpConnection.isIpReachable(Exam.getInstance().getServerIP(), Exam.getInstance().getPort(), true, false)) {
            if (isExamValid() && !loggedIn) {
                try {
                    LocalTime toTime;
                    Packet packet = new Packet(Packet.Action.LOGIN, "LoginPackage");
                    packet.put(Packet.Resource.PUPIL, new Pupil(
                            Exam.getInstance().getPupil().getCatalogNumber(),
                            Exam.getInstance().getPupil().getEnrolmentID(),
                            Exam.getInstance().getPupil().getFirstName(),
                            Exam.getInstance().getPupil().getLastName(),
                            Exam.getInstance().getPupil().getPathOfProject()
                    ));
                    packet.put(Packet.Resource.ADDRESS, new Address(
                            Exam.getInstance().getServerIP(),
                            Exam.getInstance().getPort()
                    ));
                    client = new Client(packet);
                    loggedIn = client.start();
                    toTime = client.getEndTime();
                    if (loggedIn) {
                        System.out.println("Signed in!");
                        return true;
                    }
                } catch (Exception e) {
                    FileUtils.log(this, Level.ERROR, e.getMessage());
                    System.out.println("Login failed!");
                }
            }
        } else {
            System.out.println("Connecting to server failed!");
        }
        return false;
    }

    public void logout() {
        System.out.println("Test successfully submitted");
        if (client != null)
            client.stop();
    }

    /**
     * Saves the valid values in the repository "Exam"
     */
    public void setExam(
            String serverIpText,
            String portText,
            String enrolementIdText,
            String catalogNr,
            String firstName,
            String lastName,
            String pathOfProject) {

        Exam.getInstance().setServerIP(serverIpText);
        Exam.getInstance().setPort(Integer.valueOf(portText));
        Exam.getInstance().setPupil(new Pupil(
                Integer.valueOf(catalogNr),
                enrolementIdText,
                firstName,
                lastName,
                pathOfProject)
        );
    }

    public boolean isExamValid() {
        return true;
    }

    public void toggleFinishedTest() {
        LineCounter.getInstance().setFinished(!LineCounter.getInstance().isFinished());
    }

    public boolean isTestFinished(){
        return LineCounter.getInstance().isFinished();
    }
}
