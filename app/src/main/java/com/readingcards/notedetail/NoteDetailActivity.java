/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.readingcards.notedetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.readingcards.Injection;
import com.readingcards.R;
import com.readingcards.util.ActivityUtils;

/**
 * Displays note details screen.
 */
public class NoteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notedetail_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle(getResources().getString(R.string.note_details));

        // Get the requested note id
        String noteId = getIntent().getStringExtra(EXTRA_NOTE_ID);

        NoteDetailFragment noteDetailFragment = (NoteDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (noteDetailFragment == null) {
            noteDetailFragment = NoteDetailFragment.newInstance(noteId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    noteDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new NoteDetailPresenter(
                noteId,
                Injection.provideNotesRepository(getApplicationContext()),
                noteDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
