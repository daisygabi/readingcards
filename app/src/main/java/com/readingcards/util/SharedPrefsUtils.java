package com.readingcards.util;

import com.google.common.base.Strings;

import android.app.Application;
import android.content.SharedPreferences;

import com.readingcards.sharedprefs.DaggerSharedPreferenceComponent;
import com.readingcards.sharedprefs.SharedPreferenceModule;

/**
 * Most common operations with SharedPrefs. This class should have only one instance in the entire app
 */
public class SharedPrefsUtils {

    private SharedPreferences sharedPreferences;

    public SharedPrefsUtils(Application application) {
        // Dagger%COMPONENT_NAME%
        sharedPreferences = DaggerSharedPreferenceComponent.builder()
                .sharedPreferenceModule(new SharedPreferenceModule(application))
                .build().getSharedPreference();
    }

    /**
     * Add a new String value in memory
     * @param key
     * @param value
     * @return
     */
    public boolean addStringValue(String key, String value) {
        if (Strings.isNullOrEmpty(key) && Strings.isNullOrEmpty(value)) {
            return false;
        }

        return false;
    }

    /**
     * Delete a key-value from memory
     * @param key
     */
    public void deleteValue(String key) {

    }

    /**
     * Update a value of a contained key from memory
     * @param key
     * @param value
     * @return
     */
    public boolean updateStringValue(String key, String value) {
        return false;
    }

    /**
     * Add a new Int value in memory
     * @param key
     * @param value
     * @return
     */
    public boolean addIntValue(String key, int value) {
        if (Strings.isNullOrEmpty(key) && value > 0) {
            return false;
        }

        return false;
    }

    /**
     * Update an int value of a contained key from memory
     * @param key
     * @param value
     * @return
     */
    public boolean updateIntegerValue(String key, int value) {
        return false;
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
}