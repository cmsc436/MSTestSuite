package edu.umd.cmsc436.mstestsuite.util;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import edu.umd.cmsc436.mstestsuite.data.TestApp;

/**
 * Asynchronously check installed packages
 *
 * Don't want to clog that UI thread do we now
 */

public class PackageChecker extends AsyncTask<TestApp, Void, Map<TestApp, Integer>> {

    public static int INSTALL = 0;
    public static int UPDATE = 1;

    private boolean should_update;
    private PackageUtil packageUtil;
    private OnCheckFinishListener listener;

    public PackageChecker(boolean should_update, PackageUtil packageUtil, OnCheckFinishListener listener) {
        this.should_update = should_update;
        this.packageUtil = packageUtil;
        this.listener = listener;
    }

    @Override
    protected Map<TestApp, Integer> doInBackground(TestApp... params) {
        Map<TestApp, Integer> results = new HashMap<>();

        for (TestApp app : params) {
            if (packageUtil.wouldSucceed(app.getPackageName())) {
                if (should_update) {
                    results.put(app, UPDATE);
                }
            } else {
                results.put(app, INSTALL);
            }
        }

        return results;
    }

    @Override
    protected void onPostExecute(Map<TestApp, Integer> testAppIntegerMap) {
        if (listener != null) {
            listener.onCheckFinished(testAppIntegerMap);
        }
    }

    public interface OnCheckFinishListener {
        void onCheckFinished(Map<TestApp, Integer> results);
    }
}
