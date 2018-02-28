package at.htl.server;

import at.htl.common.fx.StudentView;
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
    private Runnable listSizeChanged;

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
        onListSizeChanged();
        updateButtons();
    }

    public void removeStudent(Student student) {
        curStudentList.remove(student);
        onListSizeChanged();
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
        onListSizeChanged();
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

                btnStudent.setStyle("-fx-background-color: " + getStudentBackground(student));

                btnStudent.setTextFill(Paint.valueOf("white"));

                btnStudent.setPrefWidth(vbStudentList.getPrefWidth());

                vbStudentList.getChildren().add(btnStudent);

                Student selected = selectedStudent.get();
                if (selected != null && selected.getPupil() == student.getPupil())
                    selectStudent(student, btnStudent);

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
        if (selectedStudentBtn != null && selectedStudent.get() != null)
            selectedStudentBtn.setStyle("-fx-background-color: " + getStudentBackground(selectedStudent.get()));

        selectedStudentBtn = btnStudent;
        btnStudent.setStyle(btnStudent.getStyle() +"; -fx-border-color: black;\n"
                + "-fx-border-width: 1;\n");
        selectedStudent.set(student);
    }

    private String getStudentBackground(Student student) {
        if (student == null)
            return null;
        switch (student.getStudentState()) {
            case NORMAL:
                return "#00d474";
            case FINISHED:
                return  "#00c2d4";
            case CONNECTION_LOST:
                return "#d45500";
            default:
                return "#d4006f";
        }
        selectedStudentBtn = btnStudent;
        btnStudent.setStyle("-fx-background-color: #2900d4");
        selectedStudent.set(student);
    }

    public void setOnListSizeChanged(Runnable runnable){
        this.listSizeChanged = runnable;
    }

    private void onListSizeChanged(){
        if(this.listSizeChanged != null)
            this.listSizeChanged.run();
    }

    public int size() {
        return this.curStudentList.size();
    }
}
