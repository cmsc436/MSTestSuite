package edu.umd.cmsc436.mstestsuite;

import edu.umd.cmsc436.mstestsuite.data.Action;
import edu.umd.cmsc436.mstestsuite.data.ActionsAdapter;
import edu.umd.cmsc436.mstestsuite.data.PracticeModeAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;

/**
 * Do the logic of things without worrying about the view
 */

class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
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

    MainPresenter(MainContract.View v) {
        mView = v;
        mView.hideBottomSheet();

        mView.loadTestApps(new ActionsAdapter(actions));
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
}
