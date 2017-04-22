package edu.umd.cmsc436.mstestsuite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import edu.umd.cmsc436.mstestsuite.data.Action;
import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.data.PracticeModeAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;
import edu.umd.cmsc436.mstestsuite.model.PharmacistService;

/**
 * Do the logic of things without worrying about the view
 */

class MainPresenter implements MainContract.Presenter {

    private final TestApp[] apps = new TestApp[]{
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 1", R.mipmap.ic_launcher),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 2", R.mipmap.ic_launcher_round),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 3", R.mipmap.ic_launcher),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 4", R.mipmap.ic_launcher_round),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 5", R.mipmap.ic_launcher),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 6", R.mipmap.ic_launcher_round),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 7", R.mipmap.ic_launcher),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 8", R.mipmap.ic_launcher_round),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 9", R.mipmap.ic_launcher),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 10", R.mipmap.ic_launcher_round),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 11", R.mipmap.ic_launcher),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Test 12", R.mipmap.ic_launcher_round),
    };
    private final Action[] actions = new Action[] {
            new Action("Practice", R.mipmap.ic_launcher, new Runnable() {
                @Override
                public void run() {
                    isPractice = true;
                    mView.loadTestApps(new PracticeModeAdapter(apps, new PracticeModeAdapter.Events() {
                        @Override
                        public void appSelected(TestApp app) {
                            mView.showToast(app.getDisplayName());
                        }
                    }));
                }
            }),
            new Action("Switch User", R.mipmap.ic_launcher_round, null),
            new Action("Help", R.mipmap.ic_launcher, null),
            new Action("History", R.mipmap.ic_launcher_round, null),
            new Action("Feedback", R.mipmap.ic_launcher, null),
    };

    private MainContract.View mView;

    private boolean isPractice;

    MainPresenter(MainContract.View v) {
        mView = v;
        mView.hideBottomSheet();

        isPractice = false;
        mView.loadTestApps(new ActionsAdapter(actions));

        LocalBroadcastManager.getInstance(mView.getContext()).registerReceiver(mLocalReceiver,
                PharmacistService.ON_FINISH_FILTER);

        Intent i = new Intent(mView.getContext(), PharmacistService.class);
        i.putExtra(PharmacistService.KEY_PATIENT_ID, "patient"); // TODO "patent"
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
            // should really reuse these
            isPractice = false;
            mView.loadTestApps(new ActionsAdapter(actions));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(mView.getContext()).unregisterReceiver(mLocalReceiver);
    }

    private BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mView.showToast("done");
        }
    };
}
