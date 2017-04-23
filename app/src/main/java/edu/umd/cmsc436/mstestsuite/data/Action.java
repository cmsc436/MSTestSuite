package edu.umd.cmsc436.mstestsuite.data;

import android.support.annotation.Nullable;

/**
 * A generic action, a runnable with more info
 */

public class Action {

    private Runnable mAction;
    private int mIconResource;
    private String mDisplayName;
    private boolean mEnabled;

    public Action (String displayName, int iconResource, @Nullable Runnable runnable) {
        this.mAction = runnable;
        this.mIconResource = iconResource;
        this.mDisplayName = displayName;
        this.mEnabled = true;
    }

    void run() {
        if (mAction != null) {
            mAction.run();
        }
    }

    void setAction (Runnable r) {
        mAction = r;
    }

    void setEnabled(boolean b) {
        mEnabled = b;
    }

    boolean isEnabled () {
        return mEnabled;
    }

    int getIconResource() {
        return mIconResource;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
}
