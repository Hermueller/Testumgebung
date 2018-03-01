package at.htl.server;

import at.htl.common.enums.StudentState;
import at.htl.common.io.FileUtils;
import at.htl.server.entity.Student;
import javafx.application.Platform;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @timeline PatrolMode
 * 10.03.2016: PON 060  Patrol Mode: Switcht alle Schüler durch
 * 14.04.2016: PHI 050  fixed bug
 */
public class PatrolMode extends Thread {

    volatile boolean running = false;
    private int actualPos = 0;

    public boolean isRunning() {
        return running;
    }

    public void stopIt() {
        running = false;
    }

    public void run() {
        running = true;

        while (running) {

            try {
                StudentList sl = StudentList.getStudentList();
                List<Student> students = sl.getCurStudentList()
                        .stream().filter(st -> st.getStudentState() == StudentState.NORMAL)
                        .collect(Collectors.toList());

                Platform.runLater(() -> sl.selectStudent(students.get(actualPos)));

                if(actualPos >= students.size() - 1)
                    actualPos = 0;
                else
                    actualPos++;

                long sleepTime = Settings.getInstance().getSleepTime();
                Thread.sleep(sleepTime);
                System.out.println(sleepTime);
            } catch (Exception e) {
                FileUtils.log(Level.WARN, e.getMessage());
                Settings.getInstance().printError(Level.WARN, e.getStackTrace(), "WARNINGS", e.getMessage());
            }

        }
    }
}
