package at.htl.common.transfer;


import java.io.Serializable;

/**
 * @timeline HarvestedPackage
 * 13.01.2016: PHI 001  Erstellen der Serializable-Klasse.
 * 21.04.2016: PHI 001  added the finished-Variable
 * 07.05.2016: PHI 001  added the loc-Array
 */
public class HarvestedPackage implements Serializable {
    protected static final Long serialVersionUID = 1L;

    private byte[] image;
    private Long[] loc;
    private boolean finished = false;

    public HarvestedPackage(byte[] image, Long[] loc, boolean finished) {
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

    public Long[] getLoc() {
        return loc;
    }

    public void setLoc(Long[] loc) {
        this.loc = loc;
    }

    public boolean isFinished() {
        return finished;
    }
}
