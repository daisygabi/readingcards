package com.readingcards.collections;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.readingcards.ExpressoUtils;
import com.readingcards.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by gabrielaradu on 08/01/2017.
 */
@RunWith(AndroidJUnit4.class)
public class CardCollectionFragmentTest {

    @Rule
    public ActivityTestRule<CardCollectionActivity> activityRule = new ActivityTestRule<>(CardCollectionActivity.class);

    @Test
    public void toolbarTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext()
                .getString(R.string.collection_cards_title);
        ExpressoUtils.matchToolbarTitle(title);
    }

}