package at.htl.server.advanced;

/**
 * @timeline AdvancedSettingsPackage
 * 17.06.2016: PHI 035  created class
 * 17.06.2016: PHI 005  implemented the points
 * 17.06.2016: PHI 005  scale can be changed by the user
 */
public class AdvancedSettingsPackage {

    private static AdvancedSettingsPackage instance = null;

    private boolean random = false;
    private boolean jpgFormat = true;
    private double imageScale = 1.0;
    private double imageQuality = 0.2;
    private String filterSet = "ALL";
    private int points = 5;

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
}
