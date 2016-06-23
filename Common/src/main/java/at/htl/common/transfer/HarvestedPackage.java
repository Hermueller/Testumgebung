package at.htl.common.transfer;


import java.io.Serializable;

/**
 * @timeline HarvestedPackage
 * 13.01.2016: PHI 001  Erstellen der Serializable-Klasse.
 * 21.04.2016: PHI 001  added the finished-Variable
 * 07.05.2016: PHI 001  added the loc-Array
 */
@Deprecated
public class HarvestedPackage implements Serializable {
    protected static final Long serialVersionUID = 1L;

    private byte[] image;
    private boolean finished = false;
    private long[] lines;

    public HarvestedPackage(byte[] image, boolean finished, long[] lines) {
        this.image = image;
        this.finished = finished;
        this.lines = lines;
    }

    public long[] getLines() {
        return lines;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isFinished() {
        return finished;
    }
}
