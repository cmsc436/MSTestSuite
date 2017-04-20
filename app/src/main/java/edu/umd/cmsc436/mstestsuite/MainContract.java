package edu.umd.cmsc436.mstestsuite;

import android.content.ActivityNotFoundException;

import edu.umd.cmsc436.mstestsuite.data.PracticeModeAdapter;

/**
 * Define which part of the app can do what
 */

interface MainContract {

    // things the view can do
    interface View {
        void expandBottomSheet ();
        void collapseBottomSheet ();
        void hideBottomSheet ();
        void loadTestApps (PracticeModeAdapter adapter);
        void showToast (String message);
        void startActivity (String packageName) throws ActivityNotFoundException;
    }

    // things the presenter responds to
    interface Presenter {
        void onDailyStart ();
        void onCloseBottomSheet ();
        void onBottomSheetSlide ();
        void onBottomSheetStateChange (int newState);
    }
}
