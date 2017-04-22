package edu.umd.cmsc436.mstestsuite.model;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Because it gets prescriptions
 */

public class PharmacistService extends IntentService {

    private static final String FINISH_INTENT_MESSAGE = "finished";

    public static final String KEY_PATIENT_ID = "patient id";
    public static final IntentFilter ON_FINISH_FILTER = new IntentFilter(FINISH_INTENT_MESSAGE);

    public PharmacistService () {
        super("pharmacist");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PharmacistService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("TAG", "start");
        if (intent == null) {
            return;
        }
        Log.i("TAG", "have an intent");

        String patient_id = intent.getStringExtra(KEY_PATIENT_ID);

        // pretend work
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // nothing
        }

        Intent i = new Intent(FINISH_INTENT_MESSAGE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
