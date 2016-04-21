package at.htl.common.trasfer;


import java.io.Serializable;

/**
 * @timeline .
 * 13.01.2016: PHI 001  Erstellen der Serializable-Klasse.
 * 21.04.2016: PHI 001  added the finished-Variable
 */
public class HarvestedPackage implements Serializable {
    protected static final Long serialVersionUID = 1L;

    private byte[] image;
    private long loc;
    private boolean finished = false;

    public HarvestedPackage(byte[] image, long loc, boolean finished) {
        this.image = image;
        this.loc = loc;
        this.finished = finished;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getLoc() {
        return loc;
    }

    public void setLoc(long loc) {
        this.loc = loc;
    }

    public boolean isFinished() {
        return finished;
    }
}
