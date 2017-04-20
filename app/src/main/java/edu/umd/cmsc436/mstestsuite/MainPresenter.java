package edu.umd.cmsc436.mstestsuite;

import android.content.ActivityNotFoundException;
import android.support.design.widget.BottomSheetBehavior;

import edu.umd.cmsc436.mstestsuite.data.PracticeModeAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;

/**
 * Do the logic of things without worrying about the view
 */

class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private int curBottomState;
    private final TestApp[] apps = new TestApp[]{
            new TestApp("edu.umd.cmsc436.mstestsuite", "Friendly", R.mipmap.ic_launcher),
            new TestApp("edu.umd.cmsc436.mstestsuite", "Name", R.mipmap.ic_launcher_round)
    };

    MainPresenter(MainContract.View v) {
        mView = v;
        mView.hideBottomSheet();

        // pretend loading
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // whatever
                }

                mView.expandBottomSheet();
            }
        }).start();
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
        curBottomState = newState;

        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
            mView.loadTestApps(new PracticeModeAdapter(apps, new PracticeModeAdapter.Events() {
                @Override
                public void appSelected(TestApp app) {
                    try {
                        mView.startActivity(app.getPackageName());
                    } catch (ActivityNotFoundException anfe) {
                        mView.showToast(app.getDisplayName() + " not found");
                    }
                }
            }));
        }
    }
}
