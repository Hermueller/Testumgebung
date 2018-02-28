package at.htl.server;

import at.htl.server.entity.Student;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class StudentList {

    private VBox vbStudentList;

    private static StudentList instance;

    private List<Student> curStudentList;

    private SimpleObjectProperty<Student> selectedStudent;

    private Button selectedStudentBtn;

    private StudentList(VBox vbStudentList) {
        this.vbStudentList = vbStudentList;
        selectedStudent = new SimpleObjectProperty<>();

        vbStudentList.widthProperty().addListener((observable, oldValue, newValue) -> {
            for (Node node : vbStudentList.getChildren()) {
                ((Button) node).setPrefWidth((double) newValue);
            }
        });
        this.curStudentList = new ArrayList<>();
    }

    public void addStudent(Student student) {
        for (Student stud : curStudentList) {
            if (stud.getStudentIpAddress().equals(student.getStudentIpAddress())
                    || stud.getPupil().getEnrolmentID().equals(student.getPupil().getEnrolmentID())) {
                updateStudent(student);
                return;
            }
        }

        //Complete new User
        curStudentList.add(student);
        updateButtons();
    }

    public void removeStudent(Student student) {
        curStudentList.remove(student);
        updateButtons();
    }

    public Student findStudentByIpAddress(InetAddress ipAddress) {
        for (Student student : curStudentList) {
            if (student.getStudentIpAddress().equals(ipAddress))
                return student;
        }
        return null;
    }

    @Deprecated
    public Student findStudentByIpAddress(String ipAddress) {
        for (Student student : curStudentList) {
            if (student.getStudentIpAddress().toString().equals(ipAddress))
                return student;
        }
        return null;
    }

    public void sortList() {
        curStudentList.sort((o1, o2) ->
                o1.getStudentState().compareToStudentState(o2.getStudentState())
                        + o1.getPupil().getLastName().compareTo(o2.getPupil().getLastName())
        );
    }

    public void refreshList() {
        updateButtons();
    }

    private void updateButtons() {
        sortList();
        List<Student> studentList = new ArrayList<>(curStudentList);

        Platform.runLater(() -> {
            vbStudentList.getChildren().clear();
            for (Student student : studentList) {
                String label = student.getPupil().getLastName() + " " + student.getPupil().getFirstName();

                Button btnStudent = new Button(label);
                btnStudent.setId("" + student.getStudentIpAddress());


                switch (student.getStudentState()) {
                    case NORMAL:
                        btnStudent.setStyle("-fx-background-color: #00d474");
                        break;
                    case FINISHED:
                        btnStudent.setStyle("-fx-background-color: #00c2d4");
                        break;
                    case CONNECTION_LOST:
                        btnStudent.setStyle("-fx-background-color: #d45500");
                        break;
                    default:
                        btnStudent.setStyle("-fx-background-color: #d4006f");
                        break;
                }

                btnStudent.setTextFill(Paint.valueOf("white"));

                btnStudent.setPrefWidth(vbStudentList.getPrefWidth());

                vbStudentList.getChildren().add(btnStudent);

                btnStudent.setOnAction(event -> {
                    selectStudent(student, btnStudent);
                });
            }
        });
    }

    public void updateStudent(Student student) {
        for (Student stud : curStudentList) {
            if (stud.getStudentIpAddress().equals(student.getStudentIpAddress())
                    || stud.getPupil().getEnrolmentID().equals(student.getPupil().getEnrolmentID())) {
                stud = student;
                updateButtons();
                return;
            }
        }
        throw new IllegalArgumentException("Student " + student.getPupil().getEnrolmentID() + " existiert nicht");
    }

    public Student getSelectedStudent() {
        return selectedStudent.get();
    }

    public SimpleObjectProperty<Student> selectedStudentProperty() {
        return selectedStudent;
    }

    public static void createStudentList(VBox vbStudentList) {
        StudentList.instance = new StudentList(vbStudentList);
    }

    public static StudentList getStudentList() {
        return StudentList.instance;
    }

    private void selectStudent(Student student, Button btnStudent) {

        if (selectedStudentBtn != null) {
            switch (student.getStudentState()) {
                case NORMAL:
                    btnStudent.setStyle("-fx-background-color: #00d474");
                    break;
                case FINISHED:
                    btnStudent.setStyle("-fx-background-color: #00c2d4");
                    break;
                case CONNECTION_LOST:
                    btnStudent.setStyle("-fx-background-color: #d45500");
                    break;
                default:
                    btnStudent.setStyle("-fx-background-color: #d4006f");
                    break;
            }
        }
        selectedStudentBtn = btnStudent;
        btnStudent.setStyle("-fx-background-color: #2900d4");
        selectedStudent.set(student);
    }
}
