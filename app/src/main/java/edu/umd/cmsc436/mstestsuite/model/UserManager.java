package edu.umd.cmsc436.mstestsuite.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper functions to deal with users
 */

public class UserManager {

    private Context mContext;
    private User mCurUser;
    private Set<String> mAllUsers;

    private static final String GLOBAL_PREFS_NAME = "GLOBAL";
    private static final String KEY_CUR_USER = "cur user";
    private static final String KEY_ALL_USERS = "all users";
    private static final String KEY_HANDEDNESS = "handedness";
    private static final String KEY_DATE_OF_BIRTH = "dob";

    private static final String DEFAULT_PATIENT_ID = "default patient";

    public UserManager (Context c) {
        mContext = c;

        SharedPreferences prefs = c.getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE);

        HashSet<String> defaultSet = new HashSet<>();
        defaultSet.add(DEFAULT_PATIENT_ID);
        mAllUsers = prefs.getStringSet(KEY_ALL_USERS, defaultSet);

        String curUserId = prefs.getString(KEY_CUR_USER, DEFAULT_PATIENT_ID);
        mCurUser = readUser(curUserId);
    }

    public void onUserSelected (String id) {
        if (mAllUsers.contains(id)) {
            mCurUser = readUser(id);
            return;
        }

        mAllUsers.add(id);
        mCurUser = new User(id, 0, "1/1/1970"); // TODO actually get that data from UI
        writeUser(mCurUser);
    }

    private User readUser(String name) {
        SharedPreferences prefs = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        int handedness = prefs.getInt(KEY_HANDEDNESS, 0);
        String dob = prefs.getString(KEY_DATE_OF_BIRTH, "1/1/1970");

        return new User(name, handedness, dob);
    }

    private void writeUser(User u) {
        SharedPreferences prefs = mContext.getSharedPreferences(u.id, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_HANDEDNESS, u.handedness.ordinal())
                .putString(KEY_DATE_OF_BIRTH, u.dateOfBirth)
                .apply();
    }

    public String getCurUserID () {
        return mCurUser.id;
    }

    public Set<String> getAllUsers () {
        return mAllUsers;
    }

    private static class User {
        String id;
        Handedness handedness;
        String dateOfBirth; // TODO change to a real type for dates

        User (String id, int handedness, String dateOfBirth) {
            this.id = id;
            this.handedness = Handedness.values()[handedness];
            this.dateOfBirth = dateOfBirth;
        }
    }

    private enum Handedness {
        RIGHT,
        LEFT,
        AMBIDEXTROUS,
    }
}
