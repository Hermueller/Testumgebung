package at.htl.remotecontrol.common.trasfer;


import java.io.Serializable;

/**
 * @timeline .
 * 13.01.2016: PHI 001  Erstellen der Serializable-Klasse.
 */
public class HarvestedPackage implements Serializable {
    protected static final Long serialVersionUID = 1L;

    private byte[] image;
    private long loc;

    public HarvestedPackage(byte[] image, long loc) {
        this.image = image;
        this.loc = loc;
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
}
