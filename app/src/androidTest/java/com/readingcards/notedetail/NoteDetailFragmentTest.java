package com.readingcards.notedetail;

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
public class NoteDetailFragmentTest {
    @Rule
    public ActivityTestRule<NoteDetailActivity> activityRule = new ActivityTestRule<>(NoteDetailActivity.class);

    @Test
    public void toolbarTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext()
                .getString(R.string.note_details);
        ExpressoUtils.matchToolbarTitle(title);
    }
}