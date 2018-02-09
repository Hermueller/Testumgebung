package at.htl.server.feature;

import at.htl.common.enums.StudentState;
import at.htl.common.fx.StudentView;
import at.htl.server.entity.Student;
import at.htl.server.view.Controller;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.net.InetAddress;

public class StudentManagementController {

    private static StudentManagementController instance;

    public static StudentManagementController getInstance(){
        if(instance == null)
            instance = new StudentManagementController();
        return instance;
    }
}
