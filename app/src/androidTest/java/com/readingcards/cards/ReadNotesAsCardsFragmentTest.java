package com.readingcards.cards;

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
public class ReadNotesAsCardsFragmentTest {

    @Rule
    public ActivityTestRule<ReadNotesAsCardsActivity> activityRule = new ActivityTestRule<>(ReadNotesAsCardsActivity.class);

    @Test
    public void toolbarTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext()
                .getString(R.string.read_as_card_note);
        ExpressoUtils.matchToolbarTitle(title);
    }

    @Test
    public void toolbarNullTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext()
                .getString(R.string.read_as_card_note);
        ExpressoUtils.matchNullToolbarTitle(title);
    }

}