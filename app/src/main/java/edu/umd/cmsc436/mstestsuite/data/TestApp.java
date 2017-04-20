package edu.umd.cmsc436.mstestsuite.data;

/**
 * Hold app information, not prescription info
 */

public class TestApp {
    private String packageName;
    private String displayName;
    private int iconResource;

    public TestApp(String packageName, String displayName, int iconResource) {
        this.packageName = packageName;
        this.displayName = displayName;
        this.iconResource = iconResource;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDisplayName() {
        return displayName;
    }

    int getIconResource() {
        return iconResource;
    }
}
