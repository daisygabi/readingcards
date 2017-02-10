package com.readingcards.util;

import com.google.common.base.Strings;

import android.content.Context;
import android.content.SharedPreferences;

import com.readingcards.R;

/**
 * Most common operations with SharedPrefs. This class should have only one instance in the entire app
 */
public class SharedPrefsUtils {

    private SharedPreferences sharedPreferences;

    public SharedPrefsUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    /**
     * Add a new String value in memory
     * @param key
     * @param value
     * @return
     */
    public boolean addStringValue(String key, String value) {
        if (sharedPreferences != null && Strings.isNullOrEmpty(key) && Strings.isNullOrEmpty(value)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();

            return true;
        }
        return false;
    }

    /**
     * Add a new String value in memory
     * @param key
     * @param value
     * @return
     */
    public void addBooleanValue(String key, boolean value) {
        if (sharedPreferences != null && Strings.isNullOrEmpty(key)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    /**
     * Update a value of a contained key from memory
     * @param key
     * @param value
     * @return
     */
    public boolean updateStringValue(String key, String value) {
        if(sharedPreferences != null && sharedPreferences.contains(key)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();

            return true;
        }
        return false;
    }

    /**
     * Update a value of a contained key from memory
     * @param key
     * @param value
     * @return
     */
    public void updateBooleanValue(String key, boolean value) {
        if(sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } else {
            addBooleanValue(key, true);
        }
    }

    /**
     * Verify if the key exists in memory already
     * @param key
     * @return
     */
    public boolean isValueInMemory(String key) {
        if(Strings.isNullOrEmpty(key)) {
            return false;
        }
        return sharedPreferences.contains(key) ? true : false;
    }

    /**
     * Get value of key from memory
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        if(Strings.isNullOrEmpty(key)) {
            return false;
        }
        if(isValueInMemory(key)) {
            return sharedPreferences.getBoolean(key, false);
        }
        return true;
    }
}