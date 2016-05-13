package at.htl.common.transfer;

import java.io.Serializable;

/**
 * @timeline ResponsePackage
 * 31.10.2015: MET 001  created class
 * 31.10.2016: MET 002  added field "value"
 */
public class ResponsePackage implements Serializable {

    private boolean value;

    public ResponsePackage(boolean value) {
        this.value = value;
    }

    //region Getter and Setter
    public boolean isValue() {
        return value;
    }
    //endregion

}
