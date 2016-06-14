package at.htl.common.transfer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.LocalTime;

/**
 * @timeline HandOutPackage
 * 31.10.2015: MET 005  created class
 * 11.06.2016: PHI 020  fixed the File problem in the socket
 * 11.06.2016: PHI 015  fixed the null-value File problem
 * 12.06.2016: PHI 010  added the file extension.
 */

/**
 * Diese Klasse verwaltet alle Informationen, die für den
 * "Client" bzw. für den Schüler relevant sind.
 */
public class HandOutPackage implements Serializable {

    private byte[] file = new byte[0];
    private String fileExtension;
    private LocalTime endTime;
    private String comment;

    /**
     * A package with information for the test.
     *
     * @param file    Specialises the file where the test-questions are listed.
     * @param endTime Specialises the quickinfo the test ends.
     * @param comment Specialises a comment from the teacher to the client for the test.
     */
    public HandOutPackage(File file, LocalTime endTime, String comment) {
        setFile(file);
        setFileExtension(file);
        this.endTime = endTime;
        this.comment = comment;
    }

    //region Getter ans Setter
    public byte[] getFile() {
        return file;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getComment() {
        return comment;
    }

    public void setFile(File file) {
        try {
            if (file != null) {
                this.file = Files.readAllBytes(file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(File file) {
        fileExtension = file.getName().split("\\.")[1];
    }

    //endregion

}
