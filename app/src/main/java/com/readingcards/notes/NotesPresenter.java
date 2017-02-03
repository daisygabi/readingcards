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

package com.readingcards.notes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.readingcards.addcardcollection.AddCardCollectionActivity;
import com.readingcards.data.domain.Note;
import com.readingcards.data.repository.NotesRepository;
import com.readingcards.data.source.NoteDataSource;

import java.util.List;

import co.uk.rushorm.core.RushCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link NotesFragment}), retrieves the data and updates the
 * UI as required.
 */
public class NotesPresenter implements NotesContract.Presenter {

    @NonNull
    private NotesRepository repository;

    @NonNull
    private NotesContract.View notesView;

    private boolean mFirstLoad = true;

    public NotesPresenter(NotesRepository repository, NotesContract.View notesFragment) {
        this.repository = checkNotNull(repository, "repository cannot be null");
        this.notesView = checkNotNull(notesFragment, "view cannot be null!");

        notesView.setPresenter(this);
    }

    @Override
    public void loadNotes(boolean forceUpdate) {
        loadNotes(forceUpdate || mFirstLoad, forceUpdate);
        mFirstLoad = false;
    }

    @Override
    public void openNoteDetails(Note note) {
        checkNotNull(note, "Note cannot be null!");
        notesView.showNoteDetailsUi(note.getId());
    }

    @Override
    public void addNewNote() {
        notesView.showAddNote();
    }

    @Override
    public void deleteAllNotes() {
        repository.getNotes(new NoteDataSource.LoadNotesCallback() {

            @Override
            public void onSuccess(final List<Note> notes) {
                repository.deleteAllNotes(notes, new RushCallback() {
                    @Override
                    public void complete() {
                        notesView.showNoNotes();
                    }
                });
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    @Override
    public void setFiltering(NotesFilterType type) {

    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link NoteDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadNotes(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            notesView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            repository.getNotes(new NoteDataSource.LoadNotesCallback() {
                @Override
                public void onSuccess(List<Note> notes) {
                    processNotes(notes);
                }

                @Override
                public void onError(String message) {
                    notesView.showNoNotes();
                }
            });
            notesView.setLoadingIndicator(false);
        }
    }

    private void processNotes(final List<Note> notes) {
        if (notes.isEmpty()) {
            // Show a message indicating there are no notes for that filter type.
            notesView.showNoNotes();
        } else {
            // Show the list of collections
            notesView.showNotes(notes);
        }
    }

    @Override
    public void start() {
        this.loadNotes(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a note was successfully added, show message
        if (AddCardCollectionActivity.REQUEST_ADD_COLLECTION == requestCode && Activity.RESULT_OK == resultCode) {
            notesView.showMessage("result?");
        }
    }

    @Override
    public void completeNote(@NonNull Note completedNote) {
        repository.setNoteAsComplete(completedNote);
        notesView.showMessage("Note was set as complete.");
    }

    @Override
    public void showFiltered(NotesFilterType requestType) {

        if(requestType.equals(NotesFilterType.COMPLETED)) {
            repository.getCompletedNotes(new NoteDataSource.LoadNotesCallback() {
                @Override
                public void onSuccess(List<Note> notes) {
                    notesView.showCompletedNotes(notes);
                    notesView.showCompletedFilterLabel();
                }

                @Override
                public void onError(String message) {
                }
            });
        } else if(requestType.equals(NotesFilterType.ALL)) {
            repository.getNotes(new NoteDataSource.LoadNotesCallback() {
                @Override
                public void onSuccess(List<Note> notes) {
                    notesView.showNotes(notes);
                    notesView.showAllFilterLabel();
                }

                @Override
                public void onError(String message) {
                }
            });
        }
    }

    @Override
    public NotesFilterType getFiltering() {
        return null;
    }
}