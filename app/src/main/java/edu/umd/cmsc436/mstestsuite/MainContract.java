package edu.umd.cmsc436.mstestsuite;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.model.UserManager;
import edu.umd.cmsc436.sheets.Sheets;

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
        Sheets.Host getHost ();
        Activity getActivity ();
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
        void onPrescriptionReady (List<String> raw_data);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }
}
