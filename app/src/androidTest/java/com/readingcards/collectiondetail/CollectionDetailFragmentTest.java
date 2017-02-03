package com.readingcards.collectiondetail;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.readingcards.ExpressoUtils;
import com.readingcards.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by gabrielaradu on 27/01/2017.
 */
@RunWith(AndroidJUnit4.class)
public class CollectionDetailFragmentTest {

    @Rule
    public ActivityTestRule<CollectionDetailActivity> activityRule = new ActivityTestRule<>(CollectionDetailActivity.class);

    @Test
    public void toolbarTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext()
                .getString(R.string.edit_collection);
        ExpressoUtils.matchToolbarTitle(title);
    }
}