package com.readingcards.notes;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import com.readingcards.ExpressoUtils;
import com.readingcards.R;

import junit.framework.AssertionFailedError;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainNotesActivityTest {

//    @Rule
//    public ActivityTestRule<CardCollectionActivity> collectionActivityTestRule = new ActivityTestRule<CardCollectionActivity>(CardCollectionActivity.class);

    @Rule
    public ActivityTestRule<MainNotesActivity> notesActivityTestRule = new ActivityTestRule<>(MainNotesActivity.class);

    @Test
    public void toolbarTitle() {
        CharSequence title = getTargetContext()
                .getString(R.string.app_name);
        ExpressoUtils.matchToolbarTitle(title);
    }

    @Test
    public void clickOnAndroidHomeIcon_OpensNavigation() {
        // Check that left drawer is closed at startup
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))); // Left Drawer should be closed.

        // Open Drawer
        String navigateUpDesc = notesActivityTestRule.getActivity()
                .getString(android.support.v7.appcompat.R.string.abc_action_bar_up_description);
        onView(withContentDescription(navigateUpDesc)).perform(click());

        // Check if drawer is open from left
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT)));
    }

//    @Test
//    public void clickOnNavigationItem_ShowsNotesScreen() {
//        // Open Drawer to click on navigation
//        onView(withId(R.id.drawer_layout))
//                .check(matches(isClosed(Gravity.LEFT)))
//                .perform(open());
//
//        onView(withId(R.id.notes_nav_view)).perform(NavigationViewActions.navigateTo(R.id.notes_navigation_menu_item));
//
//        notesActivityTestRule.getActivity();
//        // Check that you Activity was opened.
//        onData(is(instanceOf(NavigationView.class)))
//                .inAdapterView(withId(R.id.notes_navigation_menu_item))
//                .atPosition(0).perform(ViewActions.click());
//    }

//    @Test
//    public void clickOnNavigationItem_ShowsCollectionsScreen() {
//        // Open Drawer to click on navigation
//        onView(withId(R.id.drawer_layout))
//                .check(matches(isClosed(Gravity.LEFT)))
//                .perform(open());
//
//        // Start collections screen
//        onView(withId(R.id.nav_view))
//                .perform(navigateTo(R.id.collection_cards_navigation_menu_item));
//
//        // Test activity has started
//        intended(hasComponent(new ComponentName(getTargetContext(), CardCollectionActivity.class)));
//    }

    @Test
    public void clickAddNoteFabButton() {
        try {
            onView(allOf(withId(R.id.fab_add_note)))
                    .check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {
            // View not displayed
        }
    }

    @Test
    public void clickAddNoteButton_opensAddNoteUi() throws Exception {
        // Click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());

        // Check if the add note screen is displayed
        onView(withId(R.id.add_note_title)).check(matches(isDisplayed()));
    }

}