package at.htl.remotecontrol.entity;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Philipp:  10.12.2015   053 Anlegen der Grundfunktionen und implementieren dieser in andere Klassen(z.B. SocketWriterThread)
 */
public class LineCounter {
    private String filename = null;

    public LineCounter(String filename) {
        setFilename(filename);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public long countLines() {
        long lines = 0;

        try {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                lines = br.lines().count();
            }
        } catch (Exception exc) {
            System.out.println("File konnte nicht gelesen werden!!");
        }

        return lines;
    }
}
