package com.readingcards.util;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Use this instead of Enum for proficiency reasons
 */

public class SharedPrefsTypeDescription {

    public final String itemType;

    @Retention(SOURCE)
    @StringDef({
            TIMER_VALUE,
            FIRST_TIME_LOAD
    })
    public @interface Constants {
    }

    public static final String TIMER_VALUE = "timer";
    public static final String FIRST_TIME_LOAD = "yes";

    public SharedPrefsTypeDescription(@Constants String itemType) {
        this.itemType = itemType;
    }
}