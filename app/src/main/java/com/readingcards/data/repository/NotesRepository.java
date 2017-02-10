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

package com.readingcards.data.repository;

import android.support.annotation.NonNull;

import com.readingcards.data.domain.Note;
import com.readingcards.data.source.NoteDataSource;

import java.util.List;

import co.uk.rushorm.core.RushCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load notes from the data sources
 */
public class NotesRepository implements NoteDataSource {

    private static NotesRepository INSTANCE = null;
    private final NoteDataSource dataSource;

    // Prevent direct instantiation.
    private NotesRepository(@NonNull NoteDataSource dataSource) {
        this.dataSource = checkNotNull(dataSource);
    }

    public static NotesRepository getInstance(NoteDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NotesRepository(dataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getNotes(@NonNull NoteDataSource.LoadNotesCallback callback) {
        dataSource.getNotes(callback);
    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback) {
        dataSource.getNote(noteId, callback);
    }

    @Override
    public void saveNote(@NonNull Note note, @NonNull final SaveOrUpdateNoteCallback callback) {
        dataSource.saveNote(note, callback);
    }

    @Override
    public void updateNote(@NonNull Note note, @NonNull SaveOrUpdateNoteCallback callback) {
        checkNotNull(note);
        dataSource.updateNote(note, callback);
    }

    @Override
    public void deleteNote(@NonNull Note note) {
        dataSource.deleteNote(note);
    }

    @Override
    public void deleteNoteById(@NonNull String noteId) {
        checkNotNull(noteId);
        dataSource.deleteNoteById(noteId);
    }

    @Override
    public void deleteAllNotes(@NonNull List<Note> notes, RushCallback callback) {
        dataSource.deleteAllNotes(notes, callback);
    }

    @Override
    public void completeNote(@NonNull String noteId) {
        dataSource.completeNote(noteId);
    }

    @Override
    public void getCompletedNotes(@NonNull LoadNotesCallback callback) {
        dataSource.getCompletedNotes(callback);
    }

    @Override
    public void setNoteAsComplete(@NonNull Note note) {
        dataSource.setNoteAsComplete(note);
    }

    @Override
    public int getAllNotesSize() {
        return dataSource.getAllNotesSize();
    }
}