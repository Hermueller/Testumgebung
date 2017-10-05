package at.htl.server;

import at.htl.server.entity.Student;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class StudentList {

    private VBox vbStudentList;

    private static StudentList instance;

    private List<Student> curStudentList;

    private StudentList(VBox vbStudentList) {
        this.vbStudentList = vbStudentList;

        vbStudentList.widthProperty().addListener((observable, oldValue, newValue) -> {
            for (Node node : vbStudentList.getChildren()) {
                ((Button) node).setPrefWidth((double) newValue);
            }
        });
    }


    public void refreshList(List<Student> newStudentList) {
        curStudentList = newStudentList;

        updateButtons();
    }

    private void updateButtons() {
        List<Student> studentList = new ArrayList<>(curStudentList);

        vbStudentList.getChildren().clear();
        for (Student student : studentList) {
            String label = student.getPupil().getLastName() + " " + student.getPupil().getFirstName();

            Button btnStudent = new Button(label);

            switch (student.getStudentState()) {
                case Normal:
                    btnStudent.setStyle("-fx-background-color: #00d474");
                    break;
                case Finished:
                    btnStudent.setStyle("-fx-background-color: #00c2d4");
                    break;
                case ConnectionLost:
                    btnStudent.setStyle("-fx-background-color: #d45500");
                    break;
                default:
                    btnStudent.setStyle("-fx-background-color: #d4006f");
                    break;
            }

            btnStudent.setTextFill(Paint.valueOf("white"));

            vbStudentList.getChildren().add(btnStudent);
        }
    }


    public static void createStudentList(VBox vbStudentList) {
        StudentList.instance = new StudentList(vbStudentList);
    }

    public static StudentList getStudentList() {
        return StudentList.instance;
    }
}
