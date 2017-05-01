package edu.umd.cmsc436.mstestsuite.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.umd.cmsc436.mstestsuite.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mCurPatient = extras.getString(KEY_PID, "default patient");
            mNumTrials = extras.getInt(KEY_N_TRIALS);
        } else {
            finish();
        }
    }
}
