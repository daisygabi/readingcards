package com.readingcards.data.source;


import android.support.annotation.NonNull;

import com.readingcards.data.domain.Note;

import java.util.List;

import co.uk.rushorm.core.RushCallback;

/**
 * Main entry point for accessing note data.
 */
public interface NoteDataSource {

    interface LoadNotesCallback {

        void onSuccess(List<Note> notes);

        void onError(String message);
    }

    interface GetNoteCallback {

        void onSuccess(Note note);

        void onError(String message);
    }

    interface SaveOrUpdateNoteCallback {

        void onSuccess(Note note);

        void onError(String message);
    }

    void getNotes(@NonNull NoteDataSource.LoadNotesCallback callback);

    void getNote(@NonNull String noteId, @NonNull NoteDataSource.GetNoteCallback callback);

    void saveNote(@NonNull Note note, @NonNull NoteDataSource.SaveOrUpdateNoteCallback callback);

    void updateNote(@NonNull Note note, @NonNull NoteDataSource.SaveOrUpdateNoteCallback callback);

    void deleteNote(@NonNull Note note);

    void deleteNoteById(@NonNull String noteId);

    void deleteAllNotes(@NonNull List<Note> notes, @NonNull RushCallback callback);

    void completeNote(@NonNull String noteId);

    void getCompletedNotes(@NonNull LoadNotesCallback callback);

    void setNoteAsComplete(@NonNull Note note);

    int getAllNotesSize();
}