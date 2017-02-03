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

package com.readingcards.cards;

import com.google.common.base.Strings;

import android.support.annotation.NonNull;

import com.readingcards.addeditnote.AddEditNoteFragment;
import com.readingcards.data.domain.Note;
import com.readingcards.data.source.NoteDataSource;
import com.readingcards.util.EspressoIdlingResource;
import com.readingcards.util.SharedPrefsTypeDescription;
import com.readingcards.util.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link AddEditNoteFragment}), retrieves the data and updates
 * the UI as required.
 */
public class ReadNotesAsCardsPresenter implements ReadNotesAsCardContract.Presenter {

    @NonNull
    private final NoteDataSource notesRepository;

    @NonNull
    private final ReadNotesAsCardContract.View readCardView;

    @NonNull
    private SharedPrefsUtils sharedPrefsUtils;

    private boolean mIsDataMissing;
    private boolean mFirstLoad = true;

    /**
     * Creates a presenter for the showing notes as card view
     *
     * @param notesRepository        a repository of data for notes
     * @param readCardView           cards as cards view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     * @param sharedPrefsUtils single instance using Dagger 2 of SharedPrefs utils class
     */
    public ReadNotesAsCardsPresenter(@NonNull NoteDataSource notesRepository,
                                     @NonNull ReadNotesAsCardContract.View readCardView, boolean shouldLoadDataFromRepo,
                                     @NonNull SharedPrefsUtils sharedPrefsUtils) {

        this.notesRepository = checkNotNull(notesRepository);
        this.readCardView = checkNotNull(readCardView);
        this.readCardView.setPresenter(this);
        this.mIsDataMissing = shouldLoadDataFromRepo;
        this.sharedPrefsUtils = sharedPrefsUtils;
    }

    @Override
    public void start() {
        if (mIsDataMissing) {
            loadNotes(mIsDataMissing);
        } else {
            readCardView.showEmptyNoteError();
        }
    }

    @Override
    public void loadNotes(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadNotesByFlag(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void scheduleCardReading(String time) {
        if (Strings.isNullOrEmpty(time)) {
            readCardView.showNoTimerFound();
        } else {
            if(sharedPrefsUtils.isValueInMemory(SharedPrefsTypeDescription.TIMER_VALUE)) {
                sharedPrefsUtils.updateStringValue(SharedPrefsTypeDescription.TIMER_VALUE, time);
            } else {
                sharedPrefsUtils.addStringValue(SharedPrefsTypeDescription.TIMER_VALUE, time);
            }
        }
    }

    public void loadNotesByFlag(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            readCardView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
//            notesRepository.refreshNotes();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        notesRepository.getNotes(new NoteDataSource.LoadNotesCallback() {
            @Override
            public void onSuccess(List<Note> notes) {
                List<Note> notesToShow = new ArrayList<Note>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the notes based on the requestType
                for (Note note : notes) {
                    notesToShow.add(note);
                }
                // The view may not be able to handle UI updates anymore
                if (showLoadingUI) {
                    readCardView.setLoadingIndicator(false);
                }

                processNotes(notesToShow);
            }

            @Override
            public void onError(String message) {
                readCardView.showEmptyNoteError();
            }
        });
    }

    private void processNotes(List<Note> notes) {
        if (notes.isEmpty()) {
            // Show a message indicating there are no notes for that filter type.
            processEmptyNotes();
        } else {
            // Show the list of notes
            readCardView.showNotesAsCards(notes);
        }
    }

    private void processEmptyNotes() {
        readCardView.showNoNotes();
    }
}