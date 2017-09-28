package at.htl.server.advanced;

import at.htl.common.io.FileUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.apache.logging.log4j.Level;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static java.lang.System.out;

/**
 * Singleton for all variables from the advanced settings.
 *
 * @timeline AdvancedSettingsPackage
 * 17.06.2016: PHI 035  created class
 * 17.06.2016: PHI 005  implemented the points
 * 17.06.2016: PHI 005  scale can be changed by the user
 * 18.06.2016: PHI 002  added the saveDataPoint.
 * 19.06.2016: PHI 020  implemented the port.
 * 20.06.2016: PHI 005  added the random time and fix time to the properties file.
 */
public class AdvancedSettingsPackage {
    public static final String INIT_IP="Not Found";
    private static AdvancedSettingsPackage instance = null;

    private boolean random = false;
    private boolean jpgFormat = true;
    private double imageScale = 1.0;
    private double imageQuality = 0.5;
    private String filterSet = "ALL";
    private int points = 5;
    private boolean saveDataPoints = true;
    private int port = 50555;
    private int time = 10;
    private String IP=INIT_IP;
    private Label lbAddress = null;
    private Slider timeSlider = null;
    private Button testMode = null;

    private AdvancedSettingsPackage() {

    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        timeSlider.setValue(time);
        this.time = time;
    }

    public void setTestMode(boolean visible) {
        testMode.setVisible(visible);
    }

    public void setTestMode(Button testMode) {
        this.testMode = testMode;
    }

    public void setTimeSlider(Slider timeSlider) {
        this.timeSlider = timeSlider;
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
        byte[] hwAddr = null ;
        try {
           findNetworkinterfaces();
        }
        catch (Exception e){
            FileUtils.log(Level.FATAL,e.getMessage());
        }
        lbAddress.setText(getIP() + " : " + getPort());
    }
     void findNetworkinterfaces() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            if(!netint.getDisplayName().toLowerCase().contains("virtual")&&!netint.isLoopback())
                gatherInterfaceInfo(netint);
    }

     void gatherInterfaceInfo(NetworkInterface netint) throws SocketException {
        int c=0;
        if (netint.isUp()) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress!=null)
                    c++;
                if(c>0) {
                    out.printf("Display name: %s\n", netint.getDisplayName());
                    out.printf("Name: %s\n", netint.getName());
                    out.printf("InetAddress: %s\n", inetAddress);
                    String pattern = "[a-zA-Z]";
                    String ip=inetAddress.toString().substring(1,inetAddress.toString().length());
                    // Create a Pattern object
                    Pattern reg = Pattern.compile(pattern);
                    // Now create matcher object.
                    Matcher m = reg.matcher(ip);
                    if (getIP().equals(INIT_IP)&&!m.find())
                    setIP(ip);
                }
            }
        }
    }
}
