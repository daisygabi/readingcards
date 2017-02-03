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

package com.readingcards.notedetail;

import com.google.common.base.Strings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.readingcards.data.domain.Note;
import com.readingcards.data.repository.NotesRepository;
import com.readingcards.data.source.NoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link NoteDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class NoteDetailPresenter implements NoteDetailContract.Presenter {

    private final NotesRepository repository;
    private final NoteDetailContract.View detailView;

    @Nullable
    private String noteId;

    public NoteDetailPresenter(@Nullable String noteId,
                               @NonNull NotesRepository repository,
                               @NonNull NoteDetailContract.View detailView) {
        this.noteId = noteId;
        this.repository = checkNotNull(repository, "repository cannot be null!");
        this.detailView = checkNotNull(detailView, "DetailView cannot be null!");

        detailView.setPresenter(this);
    }

    @Override
    public void start() {
        openNote();
    }

    private void openNote() {
        if (Strings.isNullOrEmpty(noteId)) {
            detailView.showMissingNote();
            return;
        }

        detailView.setLoadingIndicator(true);
        repository.getNote(noteId, new NoteDataSource.GetNoteCallback() {
            @Override
            public void onSuccess(Note note) {
                // The view may not be able to handle UI updates anymore
                if (!detailView.isActive()) {
                    return;
                }
                detailView.setLoadingIndicator(false);
                if (null == note) {
                    detailView.showMissingNote();
                } else {
                    showNote(note);
                }
            }

            @Override
            public void onError(String message) {
                // The view may not be able to handle UI updates anymore
                if (!detailView.isActive()) {
                    return;
                }
                detailView.showMissingNote();
            }
        });
    }

    @Override
    public void editNote() {
        if (Strings.isNullOrEmpty(noteId)) {
            detailView.showMissingNote();
            return;
        }
        detailView.showEditNote(noteId);
    }

    @Override
    public void deleteNote() {
        if (Strings.isNullOrEmpty(noteId)) {
            detailView.showMissingNote();
            return;
        } else {
            repository.deleteNoteById(noteId);
            detailView.successfullyDeletedNote();
        }
    }

    @Override
    public void completeNote() {
        if (Strings.isNullOrEmpty(noteId)) {
            detailView.showMissingNote();
            return;
        }
        repository.completeNote(noteId);
        detailView.showNoteMarkedComplete();
    }

    private void showNote(@NonNull Note note) {
        String title = note.getTitle();
        String description = note.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            detailView.hideTitle();
        } else {
            detailView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            detailView.hideDescription();
        } else {
            detailView.showDescription(description);
        }
    }
}