package edu.umd.cmsc436.mstestsuite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import edu.umd.cmsc436.mstestsuite.data.Action;
import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;
import edu.umd.cmsc436.mstestsuite.model.PharmacistService;

/**
 * Do the logic of things without worrying about the view
 */

class MainPresenter implements MainContract.Presenter, TestApp.Events {

    private static final String GLOBAL_PREFS_NAME = "GLOBAL";
    private static final String KEY_CUR_USER = "cur user";
    private static final String KEY_ALL_USERS = "all users";

    private final Action[] actions = new Action[] {
            new Action("Practice", R.mipmap.ic_launcher, new Runnable() {
                @Override
                public void run() {
                    isPractice = true;
                    mView.loadTestApps(mPracticeModeAdapter);
                }
            }),
            new Action("Switch User", R.mipmap.ic_launcher_round, new Runnable() {
                @Override
                public void run() {
                    mView.showUserSwitcher(mAllUsers.toArray(new String[mAllUsers.size()]));
                }
            }),
            new Action("Help", R.mipmap.ic_launcher, null),
            new Action("History", R.mipmap.ic_launcher_round, null),
            new Action("Feedback", R.mipmap.ic_launcher, null),
    };

    private MainContract.View mView;

    private boolean isPractice;
    private String mCurUser;
    private Set<String> mAllUsers;
    private ActionsAdapter mMainAdapter;
    private ActionsAdapter mPracticeModeAdapter;

    MainPresenter(MainContract.View v) {
        mView = v;
        mView.hideBottomSheet();

        isPractice = false;

        LocalBroadcastManager.getInstance(mView.getContext()).registerReceiver(mLocalReceiver,
                PharmacistService.ON_FINISH_FILTER);

        SharedPreferences prefs = mView.getContext().getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE);

        mCurUser = prefs.getString(KEY_CUR_USER, "patient");

        HashSet<String> defaultSet = new HashSet<>();
        defaultSet.add(mCurUser);
        mAllUsers = prefs.getStringSet(KEY_ALL_USERS, defaultSet);

        TestApp[] apps = loadAppInfo();

        mMainAdapter = new ActionsAdapter(actions, mView.getContext().getString(R.string.main_actions_header, mCurUser));
        mPracticeModeAdapter = new ActionsAdapter(apps, mView.getContext().getString(R.string.practice_mode_header_text));

        mMainAdapter.setEnabled(0, false);

        mView.loadTestApps(mMainAdapter);

        Intent i = new Intent(mView.getContext(), PharmacistService.class);
        i.putExtra(PharmacistService.KEY_PATIENT_ID, mCurUser);
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
            mView.loadTestApps(mMainAdapter);
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
        mCurUser = patient_id;
        mAllUsers.add(mCurUser);
        SharedPreferences prefs = mView.getContext().getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_CUR_USER, mCurUser)
                .putStringSet(KEY_ALL_USERS, mAllUsers)
                .apply();
        mMainAdapter.setHeader(mView.getContext().getString(R.string.main_actions_header, mCurUser));
    }

    private BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mMainAdapter.setEnabled(0, true);
        }
    };

    @Override
    public void onAppSelected(TestApp app) {
        mView.showToast(app.getDisplayName());
//        mView.startActivity(app.getPackageName());
    }
}
