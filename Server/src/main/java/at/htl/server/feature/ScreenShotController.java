package at.htl.server.feature;

import at.htl.common.fx.StudentView;
import at.htl.common.io.ScreenShot;
import at.htl.server.Settings;
import at.htl.server.StudentList;
import at.htl.server.entity.Student;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenShotController {

    /**
     * It redirects to save and show the screenshot.
     *
     * @param image   Specifies the image which should be saved.
     * @param student Specifies the client from which the screenshot is.
     */
    public static void saveImage(byte[] image, Student student) {
        LocalDateTime date = LocalDateTime.now();
        //SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd'_'HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm-ss");
        String formattedTime = formatter.format(date);
        String path = String.format("%s/%s-%s." + Settings.getInstance().getScreenShot().getFormat().toString(),
                Settings.getInstance().getPathOfImages() + "/" + student.getPupil().getLastName(),
                student.getPupil().getLastName(),
                formattedTime);

        System.out.println(String.format("Screenshot von %s erhalten",student.getPupil().getFullName()));

        ScreenShot screenShot = Settings.getInstance().getScreenShot();
        screenShot.save(image, path);

        showImage(path, student);
    }

    /**
     * It shows the Image on the Teacher-GUI.
     *
     * @param fileName Specifies the path of the file (screenshot).
     * @param student  Specifies the client from which the screenshot is.
     */
    public static void showImage(final String fileName, final Student student) {
        Platform.runLater(() -> {
            Student selected= StudentList.getStudentList().getSelectedStudent();
            if (selected != null && !Settings.getInstance().isLooksAtScreenshots()) {
                //ist der Screenshot vom ausgewählten Studenten?
                (StudentView.getInstance().getIv())
                        .setImage(new javafx.scene.image.Image("file:" + fileName));
                Settings.getInstance().addScreenshot("file:" + fileName);
                Settings.getInstance().setActualScreenshot(fileName);
            }
        });
    }
}
