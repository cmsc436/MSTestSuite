package edu.umd.cmsc436.mstestsuite.model;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Because it gets prescriptions
 */

public class PharmacistService extends IntentService {

    public static final String KEY_PATIENT_ID = "patient id";

    private PharmacistBinder mBinder = new PharmacistBinder();
    private HashMap<String, Runnable> finishListeners = new HashMap<>();

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
        if (intent == null) {
            return;
        }

        String patient_id = intent.getStringExtra(KEY_PATIENT_ID);

        // pretend work
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // nothing
        }

        Runnable finishListener = finishListeners.get(patient_id);
        if (finishListener != null) {
            finishListener.run();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void addRunnable(String patient_id, Runnable onFinishListener) {
        finishListeners.put(patient_id, onFinishListener);
    }

    class PharmacistBinder extends Binder {
        PharmacistService getService () {
            return PharmacistService.this;
        }
    }
}
