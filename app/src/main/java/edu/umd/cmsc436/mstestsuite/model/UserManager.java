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
    private static final String KEY_GENDER = "gender";

    /**
     * Use when no users are available
     * @param c current Context
     * @param patient_id Patient id
     * @param handedness enum of which hand they use
     * @param dateOfBirth any old string for now, should use real dates later
     */
    public static void initWithUser (Context c, String patient_id, Handedness handedness, String dateOfBirth, Gender gender) {
        SharedPreferences prefs = c.getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE);

        HashSet<String> startSet = new HashSet<>();
        startSet.add(patient_id);
        prefs.edit()
                .putString(KEY_CUR_USER, patient_id)
                .putStringSet(KEY_ALL_USERS, startSet)
                .apply();

        prefs = c.getSharedPreferences(patient_id, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_HANDEDNESS, handedness.ordinal())
                .putString(KEY_DATE_OF_BIRTH, dateOfBirth)
                .putInt(KEY_GENDER, gender.ordinal())
                .apply();
    }

    public UserManager (Context c) {
        mContext = c;

        SharedPreferences prefs = c.getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE);

        mAllUsers = prefs.getStringSet(KEY_ALL_USERS, new HashSet<String>());

        if (mAllUsers.size() == 0) {
            mCurUser = null;
        }

        String curUserId = prefs.getString(KEY_CUR_USER, "");

        if (curUserId.equals("")) {
            mCurUser = null;
        } else {
            mCurUser = readUser(curUserId);
        }
    }

    public void onUserCreated (String id, Handedness handedness, String dateOfBirth, Gender gender) {
        SharedPreferences prefs = mContext.getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE);

        mAllUsers.add(id);
        prefs.edit().putStringSet(KEY_ALL_USERS, mAllUsers).apply();
        writeUser(new User(id, handedness.ordinal(), dateOfBirth, gender.ordinal()));
        onUserSelected(id);
    }

    public void onUserSelected (String id) {
        if (!mAllUsers.contains(id)) {
            return;
        }

        mCurUser = readUser(id);
        mContext.getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_CUR_USER, id)
                .apply();
    }

    private User readUser(String name) {
        SharedPreferences prefs = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        int handedness = prefs.getInt(KEY_HANDEDNESS, 0);
        String dob = prefs.getString(KEY_DATE_OF_BIRTH, "1/1/1970");
        int gender = prefs.getInt(KEY_GENDER, 0);

        return new User(name, handedness, dob, gender);
    }

    private void writeUser(User u) {
        SharedPreferences prefs = mContext.getSharedPreferences(u.id, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_HANDEDNESS, u.handedness.ordinal())
                .putString(KEY_DATE_OF_BIRTH, u.dateOfBirth)
                .putInt(KEY_GENDER, u.gender.ordinal())
                .apply();
    }

    public String getCurUserID () {
        if (mCurUser == null) {
            return null;
        }

        return mCurUser.id;
    }

    public Set<String> getAllUsers () {
        return mAllUsers;
    }

    private static class User {
        String id;
        Handedness handedness;
        String dateOfBirth; // TODO change to a real type for dates
        Gender gender;

        User (String id, int handedness, String dateOfBirth, int gender) {
            this.id = id;
            this.handedness = Handedness.values()[handedness];
            this.dateOfBirth = dateOfBirth;
            this.gender = Gender.values()[gender];
        }
    }

    public enum Handedness {
        RIGHT,
        LEFT,
        AMBIDEXTROUS,
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER,
    }
}
