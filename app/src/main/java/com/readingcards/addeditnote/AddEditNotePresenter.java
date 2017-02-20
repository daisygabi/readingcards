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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.readingcards.data.domain.Note;
import com.readingcards.data.repository.NotesRepository;
import com.readingcards.data.source.NoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link AddEditNoteFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditNotePresenter implements AddEditNoteContract.Presenter {

    @NonNull
    private NotesRepository notesRepository;

    @NonNull
    private AddEditNoteContract.View addNoteView;

    @Nullable
    private String noteId;

    private boolean isDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param noteId                 ID of the note to edit or null for a new note
     * @param notesRepository        a repository of data for notes
     * @param addNoteView            the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditNotePresenter(@Nullable String noteId, @NonNull NotesRepository notesRepository,
                                @NonNull AddEditNoteContract.View addNoteView, boolean shouldLoadDataFromRepo) {
        this.noteId = noteId;
        this.notesRepository = checkNotNull(notesRepository);
        this.addNoteView = checkNotNull(addNoteView);
        isDataMissing = shouldLoadDataFromRepo;
    }

    @Override
    public void start() {
        if (!isNewNote() && isDataMissing) {
            populateNote();
        }
    }

    @Override
    public void saveOrUpdateNote(Note note) {
        if (isNewNote()) {
            createNote(note);
        } else {
            updateNote(note);
        }
    }

    @Override
    public void populateNote() {
        if (isNewNote()) {
            throw new RuntimeException("populateNote() was called but note is new.");
        }
        notesRepository.getNote(noteId, new NoteDataSource.GetNoteCallback() {
            @Override
            public void onSuccess(Note note) {
                addNoteView.setDescription(note.getDescription());
                addNoteView.setTitle(note.getTitle());
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    @Override
    public boolean isDataMissing() {
        addNoteView.showErrorMessage("Data not found.");
        return isDataMissing;
    }

    public boolean isNewNote() {
        return noteId == null;
    }

    private void createNote(Note newNote) {
        if (newNote == null && newNote.isEmpty()) {
            addNoteView.showErrorMessage("Error creating new Note");
        } else {
            Note note = checkNotNull(newNote);
            checkNotNull(notesRepository);
            checkNotNull(addNoteView);
            notesRepository.saveNote(note, new NoteDataSource.SaveOrUpdateNoteCallback() {
                @Override
                public void onSuccess(Note note) {
                }

                @Override
                public void onError(String message) {
                    addNoteView.showErrorMessage("Error creating new Note");
                }
            });
            addNoteView.showNoteList();
        }
    }

    private void updateNote(final Note newNote) {
        if (isNewNote()) {
            throw new RuntimeException("updateNote() was called but note is new.");
        }
        checkNotNull(notesRepository);
        checkNotNull(addNoteView);
        notesRepository.getNote(noteId, new NoteDataSource.GetNoteCallback() {
            @Override
            public void onSuccess(Note note) {
                note.setCompleted(newNote.isCompleted());
                note.setTitle(newNote.getTitle());
                note.setDescription(newNote.getDescription());
                notesRepository.updateNote(note, new NoteDataSource.SaveOrUpdateNoteCallback() {
                    @Override
                    public void onSuccess(Note note) {
                        addNoteView.showNote(note.getId());
                    }

                    @Override
                    public void onError(String message) {
                        addNoteView.showErrorMessage(message);
                    }
                });
            }

            @Override
            public void onError(String message) {
                addNoteView.showErrorMessage(message);
            }
        });
    }
}