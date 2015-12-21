package at.htl.remotecontrol.packets;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Diese Klasse verwaltet alle Informationen, die für den
 * "Client" bzw. für den Schüler relevant sind.
 *
 * @timeline Text
 * 31.10.2015: MET ???  Klasse erstellt
 */
public class HandOutPacket implements Serializable {

    private File file;
    private LocalDateTime endTime;
    private String comment;

    /**
     * A package with information for the test.
     *
     * @param file    Specialises the file where the test-questions are listed.
     * @param endTime Specialises the time the test ends.
     * @param comment Specialises a comment from the teacher to the student for the test.
     */
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
