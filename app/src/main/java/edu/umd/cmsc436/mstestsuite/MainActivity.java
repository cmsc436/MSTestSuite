package edu.umd.cmsc436.mstestsuite;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.umd.cmsc436.mstestsuite.data.PracticeModeAdapter;
import edu.umd.cmsc436.mstestsuite.data.TestApp;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private TextView mLoadingText;
    private ProgressBar mProgressBar;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        BottomSheetBehavior b = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        b.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mLoadingText = (TextView) findViewById(R.id.loading_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mRecyclerView = (RecyclerView) findViewById(R.id.practice_test_recyclerview);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                int w = mRecyclerView.getWidth();
                int image_w = (int) getResources().getDimension(R.dimen.practice_icon_size);
                mLayoutManager.setSpanCount(w/((int) (image_w * 1.5)));
            }
        });

        // TODO get from cache or sheets eventually
        TestApp[] apps = new TestApp[] {
                new TestApp("edu.umd.cmsc436.mstestsuite", "Friendly", R.mipmap.ic_launcher),
                new TestApp("edu.umd.cmsc436.mstestsuite", "Name", R.mipmap.ic_launcher_round)
        };

        mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return mLayoutManager.getSpanCount();
                }

                return 1;
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new PracticeModeAdapter(apps, new PracticeModeAdapter.Events() {
            @Override
            public void toast(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mToast.setText(message);
                        mToast.show();
                    }
                });
            }
        }));

        hideLoading();
    }

    private void hideLoading () {
        mLoadingText.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }
}
