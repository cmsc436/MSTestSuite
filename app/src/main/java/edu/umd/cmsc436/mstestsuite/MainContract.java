package edu.umd.cmsc436.mstestsuite;

import android.content.ActivityNotFoundException;
import android.content.Context;

import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.model.UserManager;

/**
 * Define which part of the app can do what
 */

interface MainContract {

    // things the view can do
    interface View {
        void expandBottomSheet ();
        void collapseBottomSheet ();
        void hideBottomSheet ();
        void loadActions(ActionsAdapter adapter);
        void showToast (String message);
        void startPracticeMode(String packageName) throws ActivityNotFoundException;
        Context getContext ();
        void showUserSwitcher (String[] users);
    }

    // things the presenter responds to
    interface Presenter {
        void onDailyStart ();
        void onCloseBottomSheet ();
        void onBottomSheetSlide ();
        void onBottomSheetStateChange (int newState);
        boolean onBackPressed ();
        void onDestroy ();
        void onUserSelected (String patient_id);
        void onUserCreated (String patient_id, UserManager.Handedness h, String dateOfBirth, UserManager.Gender gender);
    }
}
