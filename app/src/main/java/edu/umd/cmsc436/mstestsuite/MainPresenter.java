package edu.umd.cmsc436.mstestsuite;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.umd.cmsc436.mstestsuite.data.Action;
import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;
import edu.umd.cmsc436.mstestsuite.model.UserManager;
import edu.umd.cmsc436.mstestsuite.util.PackageChecker;
import edu.umd.cmsc436.mstestsuite.util.PackageUtil;
import edu.umd.cmsc436.sheets.DriveApkTask;
import edu.umd.cmsc436.sheets.Sheets;

/**
 * Do the logic of things without worrying about the view
 */

class MainPresenter implements MainContract.Presenter, TestApp.Events,
        Sheets.OnPrescriptionFetchedListener, PackageChecker.OnCheckFinishListener {

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
    private List<File> mToInstall;

    private UserManager mUserManager;
    private ActionsAdapter mMainAdapter;
    private ActionsAdapter mPracticeModeAdapter;
    private Sheets mSheet;
    private TestApp[] mAllApps;

    MainPresenter(MainContract.View v) {
        mView = v;
        mView.hideBottomSheet();

        isPractice = false;

        mUserManager = new UserManager(mView.getContext());
        if (mUserManager.getCurUserID() == null) {
            UserManager.initWithUser(mView.getContext(), "default patient", UserManager.Handedness.RIGHT, "1/1/1970", UserManager.Gender.MALE);
            mUserManager = new UserManager(mView.getContext());
        }

        mAllApps = loadAppInfo();
        mToInstall = new ArrayList<>();

        mMainAdapter = new ActionsAdapter(actions, mView.getContext().getString(R.string.main_actions_header, mUserManager.getCurUserID()));
        mPracticeModeAdapter = new ActionsAdapter(mAllApps, mView.getContext().getString(R.string.practice_mode_header_text));

        mMainAdapter.setEnabled(0, false);

        mView.loadActions(mMainAdapter);

        mSheet = new Sheets(mView.getHost(),
                mView.getActivity(),
                mView.getContext().getString(R.string.app_name),
                mView.getContext().getString(R.string.prescription_spreadsheet_id),
                mView.getContext().getString(R.string.prescription_spreadsheet_id));
        mSheet.fetchPrescription(mUserManager.getCurUserID(), this);
    }

    private TestApp[] loadAppInfo() {
        Resources res = mView.getContext().getResources();
        TypedArray package_names = res.obtainTypedArray(R.array.test_prefixes);
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
        // nothing
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mSheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPackageInstalled() {
        // TODO install the rest
        mView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainAdapter.setEnabled(0, true);
            }
        });
    }

    @Override
    public void onAppSelected(TestApp app) {
        try {
            mView.startPracticeMode(app.getPackageName());
        } catch (ActivityNotFoundException anfe) {
            mView.showToast(app.getDisplayName() + " not found");
        }
    }

    @Override
    public void onPrescriptionFetched(@Nullable List<String> list) {
        // Ensure apps installed
        // check if update
        // reveal proper apps
        // check frequency and last completed in settings to determine if ready
        // store difficulty list and number of trials

        if (list == null) {
            Log.e(getClass().getCanonicalName(), "Null prescription");
            return;
        }

        ArrayList<TestApp> desired_apps = new ArrayList<>();
        for (int i = 0; i < mAllApps.length; i++) {
            try {
                int difficulty = Integer.parseInt(list.get(i + 4));
                if (difficulty > 0) {
                    desired_apps.add(mAllApps[i]);
                }
            } catch (NumberFormatException nfe) {
                // not desired
            }
        }

        boolean update;
        try {
            update = Integer.parseInt(list.get(4 + mAllApps.length)) == 1;
        } catch (NumberFormatException nfe) {
            update = false;
        }

        PackageUtil packageUtil = new PackageUtil(mView.getContext());
        PackageChecker packageChecker = new PackageChecker(update, packageUtil, this);
        packageChecker.execute(desired_apps.toArray(new TestApp[desired_apps.size()]));
    }

    @Override
    public void onCheckFinished(Map<String, Float> versionMap) {
        mSheet.fetchApks(mView.getContext().getString(R.string.test_apk_folder_id), versionMap, new DriveApkTask.OnFinishListener() {
            @Override
            public void onFinish(List<File> list) {
                for (File f : list) {
                    Log.i(MainPresenter.class.getCanonicalName(), "DOWNLOADED FILE: " + f.getAbsolutePath());
                }

                mToInstall = list;
                File first = list.remove(0);
                try {
                    mView.installPackage(first);
                } catch (IOException e) {
                    Log.e(getClass().getCanonicalName(), "install failed for " + first.getAbsolutePath());
                }
            }
        });
    }
}
