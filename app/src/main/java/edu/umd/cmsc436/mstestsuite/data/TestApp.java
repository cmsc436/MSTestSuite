package edu.umd.cmsc436.mstestsuite.data;

/**
 * Hold app information, not prescription info
 */

public class TestApp extends Action {
    private String packageName;

    public TestApp(String packageName, String displayName, int iconResource) {
        super(displayName, iconResource, null);
        this.packageName = packageName;

    }

    public String getPackageName() {
        return packageName;
    }
}
