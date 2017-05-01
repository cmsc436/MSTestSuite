package edu.umd.cmsc436.mstestsuite.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import edu.umd.cmsc436.mstestsuite.R;
import edu.umd.cmsc436.mstestsuite.data.TestApp;
import edu.umd.cmsc436.mstestsuite.util.PackageUtil;

public class CoordinatorActivity extends AppCompatActivity {

    private static final String KEY_PID = "patient id";
    private static final String KEY_DIFFICULTIES = "difficulties";
    private static final String KEY_N_TRIALS = "number of trials";

    public static void start(Context context, String patientId, float[] difficulties, int n_trials) {
        Intent starter = new Intent(context, CoordinatorActivity.class);
        starter.putExtra(KEY_PID, patientId);
        starter.putExtra(KEY_DIFFICULTIES, difficulties);
        starter.putExtra(KEY_N_TRIALS, n_trials);
        context.startActivity(starter);
    }

    private String mCurPatient;
    private int mNumTrials;
    private TestApp[] mDailyApps;
    private Float[] mDailyDifficulties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(R.layout.activity_coordinator);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mCurPatient = extras.getString(KEY_PID, "default patient");
            mNumTrials = extras.getInt(KEY_N_TRIALS);

            float[] difficulties = extras.getFloatArray(KEY_DIFFICULTIES);
            TestApp[] allApps = PackageUtil.loadAppInfo(this, null);
            if (difficulties == null || difficulties.length != allApps.length) {
                Log.e(getClass().getCanonicalName(), "lengths do not match");
                finish();
                return;
            }

            ArrayList<TestApp> filteredApps = new ArrayList<>();
            ArrayList<Float> filteredDifficulties = new ArrayList<>();
            for (int i = 0; i < difficulties.length; i++) {
                if (difficulties[i] > 0) {
                    filteredApps.add(allApps[i]);
                    filteredDifficulties.add(difficulties[i]);
                }
            }

            mDailyApps = filteredApps.toArray(new TestApp[filteredApps.size()]);
            mDailyDifficulties = filteredDifficulties.toArray(new Float[filteredDifficulties.size()]);
        } else {
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
