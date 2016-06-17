package at.htl.server.advanced;

/**
 * @timeline AdvancedSettingsPackage
 * 17.06.2016: PHI 035  created class
 */
public class AdvancedSettingsPackage {

    private static AdvancedSettingsPackage instance = null;

    private boolean random = false;
    private boolean jpgFormat = true;
    private double imageScale = 0.2;
    private String filterSet;

    private AdvancedSettingsPackage() {

    }

    public static AdvancedSettingsPackage getInstance() {
        if (instance == null) {
            instance = new AdvancedSettingsPackage();
        }
        return instance;
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
