package edu.umd.cmsc436.mstestsuite.data;

/**
 * Hold app information, not prescription info
 */

public class TestApp extends Action {
    private String packageName;

    public TestApp(final String packageName, String displayName, int iconResource, final Events callback) {
        super(displayName, iconResource, null);
        setAction(new Runnable() {
            @Override
            public void run() {
                callback.onAppSelected(TestApp.this);
            }
        });
        this.packageName = packageName;

    }

    public String getPackageName() {
        return packageName;
    }

    public interface Events {
        void onAppSelected (TestApp app);
    }
}
