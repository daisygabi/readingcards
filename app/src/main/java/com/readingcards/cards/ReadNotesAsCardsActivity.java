package com.readingcards.cards;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.readingcards.Injection;
import com.readingcards.R;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.util.ActivityUtils;
import com.readingcards.util.EspressoIdlingResource;
import com.readingcards.util.SharedPrefsUtils;

import java.util.List;

import static com.readingcards.addeditnote.AddEditNoteActivity.SHOULD_LOAD_DATA_FROM_REPO_KEY;

/**
 * Present all the notes boxed in a Card view. Swipe left and write to dismiss. On reaching the end
 * of the deck, user can refresh the cards and start again User can set a timer while reading the
 * cards
 */
public class ReadNotesAsCardsActivity extends AppCompatActivity {

    private ReadNotesAsCardsPresenter readNotesAsCardsPresenter;
    private CardCollection cardCollectionToShow;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_notes_as_cards_act);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cardCollectionToShow = extras.getParcelable("cardCollection");
            notes = extras.getParcelableArrayList("notes");

            // Need to override this because of a bug that sends the notes list as null values
            cardCollectionToShow.setNotes(notes);
        }

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        if (cardCollectionToShow != null && cardCollectionToShow.getTitle() != null) {
            ab.setTitle(cardCollectionToShow.getTitle().toString());
        } else {
            ab.setTitle(R.string.read_as_card_note);
        }

        ReadNotesAsCardsFragment readAsCardFragment =
                (ReadNotesAsCardsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (readAsCardFragment == null) {
            readAsCardFragment = ReadNotesAsCardsFragment.newInstance(cardCollectionToShow);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    readAsCardFragment, R.id.contentFrame);
        }

        // Prevent the presenter from loading data from the repository if this is a config change.
        boolean shouldLoadDataFromRepo = true;
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        readNotesAsCardsPresenter = new ReadNotesAsCardsPresenter(
                Injection.provideNotesRepository(getApplicationContext()),
                readAsCardFragment,
                shouldLoadDataFromRepo, new SharedPrefsUtils(getApplication()));

        readAsCardFragment.setPresenter(readNotesAsCardsPresenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}