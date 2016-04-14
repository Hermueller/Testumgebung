package at.htl.server;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;


/**
 * @timeline Text
 * 10.03.2016: PON 060  Patrol Mode: Switcht alle Schüler durch
 */
public class PatrolMode extends Thread {

    volatile boolean running = false;
    private ListView<Button> lv;
    private int actualPos = 0;

    public void setLv(ListView<Button> lv) {
        this.lv = lv;
    }

    public void stopIt() {
        running = false;
    }

    public void run() {
        running = true;
        while (running == true) {
            lv.getSelectionModel().select(actualPos);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(actualPos >= lv.getItems().size()) {
                actualPos = 0;
            } else {
                actualPos++;
            }
        }

    }
}
