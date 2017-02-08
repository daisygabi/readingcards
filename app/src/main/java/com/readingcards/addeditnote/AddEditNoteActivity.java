/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.readingcards.addeditnote;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.readingcards.Injection;
import com.readingcards.R;
import com.readingcards.util.ActivityUtils;
import com.readingcards.util.EspressoIdlingResource;


/**
 * Displays an add or edit note screen.
 */
public class AddEditNoteActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_NOTE = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    private AddEditNotePresenter mAddEditNotePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddEditNoteFragment addEditNoteFragment =
                (AddEditNoteFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String noteId = getIntent().getStringExtra(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID);

        if (addEditNoteFragment == null) {
            addEditNoteFragment = AddEditNoteFragment.newInstance();

            if (getIntent().hasExtra(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID)) {
                actionBar.setTitle(R.string.edit_note);
                Bundle bundle = new Bundle();
                bundle.putString(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID, noteId);
                addEditNoteFragment.setArguments(bundle);
            } else {
                actionBar.setTitle(R.string.add_note);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditNoteFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditNotePresenter = new AddEditNotePresenter(
                noteId,
                Injection.provideNotesRepository(getApplicationContext()),
                addEditNoteFragment,
                shouldLoadDataFromRepo);

        addEditNoteFragment.setPresenter(mAddEditNotePresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditNotePresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
