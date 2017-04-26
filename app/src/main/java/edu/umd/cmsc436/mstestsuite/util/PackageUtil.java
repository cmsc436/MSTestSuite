package edu.umd.cmsc436.mstestsuite.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Helper to check what packages are installed that respond to a given criteria
 */

public class PackageUtil {

    private PackageManager pm;

    public PackageUtil(Context c) {
        pm = c.getPackageManager();
    }

    private boolean wouldSucceed (Intent i) {
        return pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * Checks if something can handle the action
     * @param test_name the generic prefix, .action.PRACTICE is appended
     * @return true if something exists, else false
     */
    public boolean wouldSucceed (String test_name) {
        Intent i = new Intent(test_name + ".action.PRACTICE");
        return wouldSucceed(i);
    }
}
