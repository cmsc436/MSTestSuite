package edu.umd.cmsc436.mstestsuite.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cmsc436.frontendhelper.TrialMode;
import edu.umd.cmsc436.mstestsuite.R;
import edu.umd.cmsc436.mstestsuite.data.TestApp;
import edu.umd.cmsc436.mstestsuite.util.PackageUtil;
import edu.umd.cmsc436.sheets.Sheets;

public class CoordinatorActivity extends AppCompatActivity {

    private static final String KEY_PID = "patient id";
    private static final String KEY_DIFFICULTIES = "difficulties";
    private static final String KEY_N_TRIALS = "number of trials";

    public static void start(Context context, String patientId, int[] difficulties, int n_trials) {
        Intent starter = new Intent(context, CoordinatorActivity.class);
        starter.putExtra(KEY_PID, patientId);
        starter.putExtra(KEY_DIFFICULTIES, difficulties);
        starter.putExtra(KEY_N_TRIALS, n_trials);
        context.startActivity(starter);
    }

    private String mCurPatient;
    private int mNumTrials;
    private TestApp[] mDailyApps;
    private Integer[] mDailyDifficulties;
    private List<Intent> mLaunchIntents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(R.layout.activity_coordinator);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mCurPatient = extras.getString(KEY_PID, "default patient");
            mNumTrials = extras.getInt(KEY_N_TRIALS);

            int[] difficulties = extras.getIntArray(KEY_DIFFICULTIES);
            TestApp[] allApps = PackageUtil.loadAppInfo(this, null);
            if (difficulties == null || difficulties.length != allApps.length) {
                Log.e(getClass().getCanonicalName(), "lengths do not match");
                finish();
                return;
            }

            ArrayList<TestApp> filteredApps = new ArrayList<>();
            ArrayList<Integer> filteredDifficulties = new ArrayList<>();
            for (int i = 0; i < difficulties.length; i++) {
                if (difficulties[i] > 0) {
                    filteredApps.add(allApps[i]);
                    filteredDifficulties.add(difficulties[i]);
                }
            }

            mDailyApps = filteredApps.toArray(new TestApp[filteredApps.size()]);
            mDailyDifficulties = filteredDifficulties.toArray(new Integer[filteredDifficulties.size()]);
            createLaunchIntents();
        } else {
            finish();
        }
    }

    private void createLaunchIntents () {
        mLaunchIntents = new ArrayList<>();

        for (int i = 0; i < mDailyApps.length; i++) {
            TestApp app = mDailyApps[i];
            int difficulty = mDailyDifficulties[i];
            for (Sheets.TestType appendage : app.getSupportedAppendages()) {
                for (int cur_trial = 0; i < mNumTrials; i++) {
                    Intent launcher = new Intent(app.getPackageName() + ".action.TRIAL");
                    launcher.putExtra(TrialMode.KEY_APPENDAGE, appendage.ordinal());
                    launcher.putExtra(TrialMode.KEY_TRIAL_NUM, cur_trial);
                    launcher.putExtra(TrialMode.KEY_DIFFICULTY, difficulty);
                    launcher.putExtra(TrialMode.KEY_TRIAL_OUT_OF, mNumTrials);
                    launcher.putExtra(TrialMode.KEY_PATIENT_ID, mCurPatient);
                    mLaunchIntents.add(launcher);
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
