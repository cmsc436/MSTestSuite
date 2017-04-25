package edu.umd.cmsc436.mstestsuite;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import edu.umd.cmsc436.mstestsuite.data.Action;
import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;
import edu.umd.cmsc436.mstestsuite.model.PharmacistService;
import edu.umd.cmsc436.mstestsuite.model.UserManager;

/**
 * Do the logic of things without worrying about the view
 */

class MainPresenter implements MainContract.Presenter, TestApp.Events {

    private final Action[] actions = new Action[] {
            new Action("Practice", R.drawable.ic_practice_mode, new Runnable() {
                @Override
                public void run() {
                    isPractice = true;
                    mView.loadActions(mPracticeModeAdapter);
                }
            }),
            new Action("Switch User", R.drawable.ic_switch_users, new Runnable() {
                @Override
                public void run() {
                    mView.showUserSwitcher(mUserManager.getAllUsers().toArray(new String[mUserManager.getAllUsers().size()]));
                }
            }),
            new Action("Help", R.drawable.ic_help, null),
            new Action("History", R.drawable.ic_history, null),
            new Action("Feedback", R.drawable.ic_feedback, null),
    };

    private MainContract.View mView;

    private boolean isPractice;
    private UserManager mUserManager;
    private ActionsAdapter mMainAdapter;
    private ActionsAdapter mPracticeModeAdapter;

    MainPresenter(MainContract.View v) {
        mView = v;
        mView.hideBottomSheet();

        isPractice = false;

        LocalBroadcastManager.getInstance(mView.getContext()).registerReceiver(mLocalReceiver,
                PharmacistService.ON_FINISH_FILTER);

        mUserManager = new UserManager(mView.getContext());
        if (mUserManager.getCurUserID() == null) {
            UserManager.initWithUser(mView.getContext(), "default patient", UserManager.Handedness.RIGHT, "1/1/1970", UserManager.Gender.MALE);
            mUserManager = new UserManager(mView.getContext());
        }

        TestApp[] apps = loadAppInfo();

        mMainAdapter = new ActionsAdapter(actions, mView.getContext().getString(R.string.main_actions_header, mUserManager.getCurUserID()));
        mPracticeModeAdapter = new ActionsAdapter(apps, mView.getContext().getString(R.string.practice_mode_header_text));

        mMainAdapter.setEnabled(0, false);

        mView.loadActions(mMainAdapter);

        Intent i = new Intent(mView.getContext(), PharmacistService.class);
        i.putExtra(PharmacistService.KEY_PATIENT_ID, mUserManager.getCurUserID());
        mView.getContext().startService(i);
    }

    private TestApp[] loadAppInfo() {
        Resources res = mView.getContext().getResources();
        TypedArray package_names = res.obtainTypedArray(R.array.package_names);
        TypedArray display_names = res.obtainTypedArray(R.array.display_names);
        TypedArray icons = res.obtainTypedArray(R.array.icons);

        TestApp[] apps = new TestApp[package_names.length()];
        if (package_names.length() == display_names.length() && display_names.length() == icons.length()) {
            for (int i = 0; i < package_names.length(); i++) {
                apps[i] = new TestApp(package_names.getString(i), display_names.getString(i), icons.getResourceId(i, R.mipmap.ic_launcher), this);
            }
        } else {
            Log.e(getClass().getCanonicalName(), "XML resource arrays not same length");
            apps = new TestApp[0];
        }

        package_names.recycle();
        display_names.recycle();
        icons.recycle();

        return apps;
    }

    @Override
    public void onDailyStart() {
        // TODO
    }

    @Override
    public void onCloseBottomSheet() {
        mView.collapseBottomSheet();
    }

    @Override
    public void onBottomSheetSlide() {
        // nothing
    }

    @Override
    public void onBottomSheetStateChange(int newState) {
        // nothing
    }

    @Override
    public boolean onBackPressed() {
        if (isPractice) {
            isPractice = false;
            mView.loadActions(mMainAdapter);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(mView.getContext()).unregisterReceiver(mLocalReceiver);
    }

    @Override
    public void onUserSelected(String patient_id) {
        mUserManager.onUserSelected(patient_id);
        mMainAdapter.setHeader(mView.getContext().getString(R.string.main_actions_header, mUserManager.getCurUserID()));
    }

    @Override
    public void onUserCreated(String patient_id, UserManager.Handedness h, String dateOfBirth, UserManager.Gender gender) {
        mUserManager.onUserCreated(patient_id, h, dateOfBirth, gender);
        mMainAdapter.setHeader(mView.getContext().getString(R.string.main_actions_header, mUserManager.getCurUserID()));
    }

    private BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mMainAdapter.setEnabled(0, true);
        }
    };

    @Override
    public void onAppSelected(TestApp app) {
        try {
            mView.startPracticeMode(app.getPackageName());
        } catch (ActivityNotFoundException anfe) {
            mView.showToast(app.getDisplayName() + " not found");
        }
    }
}
