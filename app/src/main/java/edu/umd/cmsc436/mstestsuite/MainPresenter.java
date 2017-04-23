package edu.umd.cmsc436.mstestsuite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import edu.umd.cmsc436.mstestsuite.data.Action;
import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;
import edu.umd.cmsc436.mstestsuite.model.PharmacistService;
import edu.umd.cmsc436.mstestsuite.model.UserManager;

/**
 * Do the logic of things without worrying about the view
 */

class MainPresenter implements MainContract.Presenter, TestApp.Events {

    private final TestApp[] apps = new TestApp[]{
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 1", R.mipmap.ic_launcher, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 2", R.mipmap.ic_launcher_round, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 3", R.mipmap.ic_launcher, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 4", R.mipmap.ic_launcher_round, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 5", R.mipmap.ic_launcher, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 6", R.mipmap.ic_launcher_round, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 7", R.mipmap.ic_launcher, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 8", R.mipmap.ic_launcher_round, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 9", R.mipmap.ic_launcher, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 10", R.mipmap.ic_launcher_round, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 11", R.mipmap.ic_launcher, this),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 12", R.mipmap.ic_launcher_round, this),
    };
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
                    mView.showUserSwitcher(mUserManager.getAllUsers().toArray(new String[mUserManager.getAllUsers().size()]));
                }
            }),
            new Action("Help", R.mipmap.ic_launcher, null),
            new Action("History", R.mipmap.ic_launcher_round, null),
            new Action("Feedback", R.mipmap.ic_launcher, null),
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
            UserManager.initWithUser(mView.getContext(), "default patient", UserManager.Handedness.RIGHT, "1/1/1970");
            mUserManager = new UserManager(mView.getContext());
        }

        mMainAdapter = new ActionsAdapter(actions, mView.getContext().getString(R.string.main_actions_header, mUserManager.getCurUserID()));
        mPracticeModeAdapter = new ActionsAdapter(apps, mView.getContext().getString(R.string.practice_mode_header_text));

        mMainAdapter.setEnabled(0, false);

        mView.loadTestApps(mMainAdapter);

        Intent i = new Intent(mView.getContext(), PharmacistService.class);
        i.putExtra(PharmacistService.KEY_PATIENT_ID, mUserManager.getCurUserID());
        mView.getContext().startService(i);
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
        mUserManager.onUserSelected(patient_id);
        mMainAdapter.setHeader(mView.getContext().getString(R.string.main_actions_header, mUserManager.getCurUserID()));
    }

    @Override
    public void onUserCreated(String patient_id, UserManager.Handedness h, String dateOfBirth) {
        mUserManager.onUserCreated(patient_id, h, dateOfBirth);
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
        mView.showToast(app.getDisplayName());
//        mView.startActivity(app.getPackageName());
    }
}
