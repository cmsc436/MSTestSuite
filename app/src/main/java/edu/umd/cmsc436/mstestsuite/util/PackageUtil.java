package edu.umd.cmsc436.mstestsuite.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import edu.umd.cmsc436.mstestsuite.data.TestApp;

/**
 * Helper to check what packages are installed that respond to a given criteria
 */

public class PackageUtil {

    private static final String PREFS_VERSIONS = "Version info";

    private PackageManager pm;
    private SharedPreferences mVersionPrefs;

    public PackageUtil(Context c) {
        pm = c.getPackageManager();
        mVersionPrefs = c.getSharedPreferences(PREFS_VERSIONS, Context.MODE_PRIVATE);
    }

    private boolean wouldSucceed (Intent i) {
        return pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * Checks if something can handle the action
     * @param app the TestApp
     * @return true if something exists, else false
     */
    public boolean wouldSucceed (TestApp app) {
        Intent i = new Intent(app.getPackageName() + ".action.PRACTICE");
        return wouldSucceed(i);
    }

    public float getVersion (TestApp app) {
        return mVersionPrefs.getFloat(app.getPackageName(), 0f);
    }

    public void setVersion (TestApp app, float version) {
        mVersionPrefs.edit()
                .putFloat(app.getPackageName(), version)
                .apply();
    }
}
