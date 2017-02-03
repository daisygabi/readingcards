package com.readingcards.addcardcollection;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.readingcards.Injection;
import com.readingcards.R;
import com.readingcards.data.repository.CollectionsRepository;
import com.readingcards.util.ActivityUtils;

import static com.readingcards.addeditnote.AddEditNoteActivity.SHOULD_LOAD_DATA_FROM_REPO_KEY;

/**
 * Created by gabrielaradu on 09/01/2017.
 */

public class AddCardCollectionActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_COLLECTION = 1;
    private AddCardCollectionPresenter addEditCollectionPresenter;
    private CollectionsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcollection_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddCardCollectionFragment addCardCollectionFragment =
                (AddCardCollectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String collectionId = getIntent().getStringExtra(AddCardCollectionFragment.ARGUMENT_EDIT_COLLECTION_ID);

        if (addCardCollectionFragment == null) {
            addCardCollectionFragment = AddCardCollectionFragment.newInstance();

            if (getIntent().hasExtra(AddCardCollectionFragment.ARGUMENT_EDIT_COLLECTION_ID)) {
                actionBar.setTitle(R.string.edit_collection);
                Bundle bundle = new Bundle();
                bundle.putString(AddCardCollectionFragment.ARGUMENT_EDIT_COLLECTION_ID, collectionId);
                addCardCollectionFragment.setArguments(bundle);
            } else {
                actionBar.setTitle(R.string.add_collection);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addCardCollectionFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        addEditCollectionPresenter = new AddCardCollectionPresenter(
                collectionId,
                Injection.provideCollectionRepository(getApplicationContext()),
                Injection.provideNotesRepository(getApplicationContext()),
                addCardCollectionFragment,
                shouldLoadDataFromRepo);

        addCardCollectionFragment.setPresenter(addEditCollectionPresenter);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity as appropriate.
     */
    @Override
    public void onBackPressed() {

    }
}
