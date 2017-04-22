package edu.umd.cmsc436.mstestsuite.model;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.umd.cmsc436.mstestsuite.data.TestApp;

/**
 * Object to hold information about test apps
 */

public class Prescription {

    // package names are also used as keys, so
    // exclamation mark is to make sure nothing is trampled
    private static final String KEY_DATE = "!date";
    private static final String KEY_STATUS = "!status";
    private static final String KEY_FREQUENCY = "!frequency";
    private static final String KEY_KNOWN_TESTS = "!tests";

    private long date_assigned; // unix timestamp
    private Status status;
    private String frequency;
    private Map<String, Integer> difficulties;

    public Prescription (long date_assigned, Status status, String frequency) {
        this.date_assigned = date_assigned;
        this.status = status;
        this.frequency = frequency;

        this.difficulties = new HashMap<>();
    }

    public void putDifficulty (String test_package_name, int difficulty) {
        difficulties.put(test_package_name, difficulty);
    }

    public void putDifficulty (TestApp t, int difficulty) {
        putDifficulty(t.getPackageName(), difficulty);
    }

    public int getDifficulty (String test_package_name) {

        Integer difficulty = difficulties.get(test_package_name);

        if (difficulty == null) {
            return 0;
        }

        return difficulty;
    }

    public void write(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(KEY_DATE, date_assigned);
        editor.putString(KEY_FREQUENCY, frequency);
        editor.putInt(KEY_STATUS, status.ordinal());

        editor.putStringSet(KEY_KNOWN_TESTS, difficulties.keySet());

        for (String package_name : difficulties.keySet()) {
            editor.putInt(package_name, difficulties.get(package_name));
        }

        editor.apply();
    }

    public static Prescription from(SharedPreferences prefs) {
        long date_assigned = prefs.getLong(KEY_DATE, 0);
        Status status = Status.values()[prefs.getInt(KEY_STATUS, Status.INCOMPLETE.ordinal())];
        String frequency = prefs.getString(KEY_FREQUENCY, "");

        Prescription prescription = new Prescription(date_assigned, status, frequency);
        Set<String> known_tests = prefs.getStringSet(KEY_KNOWN_TESTS, new HashSet<String>());

        for (String package_name : known_tests) {
            // default to easiest difficulty
            prescription.putDifficulty(package_name, prefs.getInt(package_name, 1));
        }

        return prescription;
    }

    enum Status {
        INCOMPLETE,
        IGNORED,
        COMPLETED,
    }
}
