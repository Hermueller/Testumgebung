package at.htl.timemonitoring.common.actions;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;

public interface RobotAction extends Serializable {
    Object execute(Robot robot) throws IOException;
}
