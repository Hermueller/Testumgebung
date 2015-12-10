package at.htl.remotecontrol.packets;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Diese Klasse verwaltet alle Informationen, die für den
 * "Client" bzw. für den Schüler relevant sind.
 *
 * 31.10.2015:  Tobias      ??? Klasse erstellt
 */
public class HandOutPacket implements Serializable {

    private File file;
    private LocalDateTime endTime;
    private String comment;

    public HandOutPacket(File file, LocalDateTime endTime, String comment) {
        this.file = file;
        this.endTime = endTime;
        this.comment = comment;
    }

    //region Getter ans Setter
    public File getFile() {
        return file;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getComment() {
        return comment;
    }

    //endregion

}
