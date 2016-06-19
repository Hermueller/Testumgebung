package at.htl.server.advanced;

import at.htl.common.MyUtils;
import at.htl.common.io.FileUtils;
import at.htl.server.Settings;
import javafx.scene.control.Label;
import org.apache.logging.log4j.Level;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @timeline AdvancedSettingsPackage
 * 17.06.2016: PHI 035  created class
 * 17.06.2016: PHI 005  implemented the points
 * 17.06.2016: PHI 005  scale can be changed by the user
 * 18.06.2016: PHI 002  added the saveDataPoint.
 * 19.06.2016: PHI 020  implemented the port.
 */

/**
 * Singleton for all variables from the advanced settings.
 *
 * @author Philipp
 */
public class AdvancedSettingsPackage {

    private static AdvancedSettingsPackage instance = null;

    private boolean random = false;
    private boolean jpgFormat = true;
    private double imageScale = 1.0;
    private double imageQuality = 0.2;
    private String filterSet = "ALL";
    private int points = 5;
    private boolean saveDataPoints = true;
    private int port = 50555;
    private Label lbAddress = null;

    private AdvancedSettingsPackage() {

    }

    public static AdvancedSettingsPackage getInstance() {
        if (instance == null) {
            instance = new AdvancedSettingsPackage();
        }
        return instance;
    }

    public double getImageQuality() {
        return imageQuality;
    }

    public void setImageQuality(double imageQuality) {
        this.imageQuality = imageQuality;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public boolean isJpgFormat() {
        return jpgFormat;
    }

    public void setJpgFormat(boolean jpgFormat) {
        this.jpgFormat = jpgFormat;
    }

    public double getImageScale() {
        return imageScale;
    }

    public void setImageScale(double imageScale) {
        this.imageScale = imageScale;
    }

    public String getFilterSet() {
        return filterSet;
    }

    public void setFilterSet(String filterSet) {
        this.filterSet = filterSet;
    }

    public boolean isSaveDataPoints() {
        return saveDataPoints;
    }

    public void setSaveDataPoints(boolean saveDataPoints) {
        this.saveDataPoints = saveDataPoints;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        showIP_Address();
    }

    public void setLbAddress(Label lbAddress) {
        this.lbAddress = lbAddress;
        showIP_Address();
    }

    /**
     * shows the IP-Address of the Teacher.
     */
    public void showIP_Address() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            FileUtils.log(this, Level.ERROR, "No IP-Address found " + MyUtils.exToStr(e));
            Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getMessage());
        }
        lbAddress.setText(ip + " : " + getPort());
    }
}
