package com.readingcards;

import android.support.test.espresso.ViewInteraction;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by gabrielaradu on 27/01/2017.
 */

public class ExpressoUtils {

    public static ViewInteraction matchToolbarTitle(
            CharSequence title) {

        return onView(allOf(
                isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(title.toString())));
    }

    public static ViewInteraction matchNullToolbarTitle(
            CharSequence title) {

        if(title == null) {
            return onView(allOf(
                    isAssignableFrom(TextView.class),
                    withParent(isAssignableFrom(Toolbar.class))))
                    .check(matches(nullValue()));
        }

        return onView(allOf(
                isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(title.toString())));
    }
}
