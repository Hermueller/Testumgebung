package at.htl.common.transfer;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @timeline HandInPackage
 * 31.10.2015: MET 005  created class
 */
public class HandInPackage implements Serializable {

    private File file;
    private String comment;
    private LocalDateTime finishedTime;

    /**
     * the transfer the client sends the teacher back with information from their test.
     *
     * @param file    Specialises the file of test.
     * @param comment Specialises a comment from the client.
     */
    public HandInPackage(File file, String comment) {
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
