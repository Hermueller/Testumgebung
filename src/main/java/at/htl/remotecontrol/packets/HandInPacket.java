package at.htl.remotecontrol.packets;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 31.10.2015:  Tobias      ??? Klasse erstellt
 */
public class HandInPacket implements Serializable {

    private File file;
    private String comment;
    private LocalDateTime finishedTime;

    public HandInPacket(File file, String comment) {
        this.file = file;
        this.comment = comment;
        setFinishedTime(LocalDateTime.now());
    }

    //region Getter and Setter
    public File getFile() {
        return file;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(LocalDateTime finishedTime) {
        this.finishedTime = finishedTime;
    }
    //endregion

}
