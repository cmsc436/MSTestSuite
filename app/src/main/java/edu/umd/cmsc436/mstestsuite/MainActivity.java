package edu.umd.cmsc436.mstestsuite;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import edu.umd.cmsc436.mstestsuite.data.PracticeModeAdapter;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private Toast mToast;
    private Button mPeekButton;
    private BottomSheetBehavior mBottomSheet;
    private ImageView mCloseButton;

    private MainContract.Presenter mPresenter;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mPeekButton = (Button) findViewById(R.id.peeked_begin_button);
        mPeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mPresenter.onDailyStart();
                }
            }
        });

        mCloseButton = (ImageView) findViewById(R.id.close_bottom_sheet_button);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mPresenter.onCloseBottomSheet();
                }
            }
        });

        findViewById(R.id.expanded_practice_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onCloseBottomSheet();
            }
        });

        mBottomSheet = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        mBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                mPresenter.onBottomSheetStateChange(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mPeekButton.setAlpha(1-slideOffset);
                mCloseButton.setAlpha(slideOffset);
                mPresenter.onBottomSheetSlide();
            }
        });

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        mRecyclerView = (RecyclerView) findViewById(R.id.practice_test_recyclerview);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                int w = mRecyclerView.getWidth();
                int image_w = (int) getResources().getDimension(R.dimen.practice_icon_size);
                mLayoutManager.setSpanCount(w/((int) (image_w * 1.4)));
            }
        });

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

        mPresenter = new MainPresenter(this);
    }

    @Override
    public void expandBottomSheet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBottomSheet.setHideable(false);
                mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public void collapseBottomSheet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBottomSheet.setHideable(false);
                mBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    @Override
    public void hideBottomSheet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBottomSheet.setHideable(true);
                mBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    @Override
    public void loadTestApps(final PracticeModeAdapter adapter) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showToast(String message) {
        mToast.setText(message);
        mToast.show();
    }

    @Override
    public void startActivity(String packageName) throws ActivityNotFoundException {
        Intent i = new Intent(packageName);
        startActivity(i);
    }
}
