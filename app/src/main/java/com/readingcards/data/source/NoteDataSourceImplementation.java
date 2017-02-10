package com.readingcards.data.source;


import android.support.annotation.NonNull;

import com.readingcards.data.domain.Note;

import java.util.List;

import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main entry point for accessing note data.
 */
public class NoteDataSourceImplementation implements NoteDataSource {

    private static NoteDataSourceImplementation INSTANCE;

    public static NoteDataSourceImplementation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoteDataSourceImplementation();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private NoteDataSourceImplementation() {
    }

    @Override
    public void getNotes(@NonNull LoadNotesCallback callback) {
        List<Note> notes = new RushSearch().find(Note.class);
        if (notes != null && !notes.isEmpty()) {
            callback.onSuccess(notes);
        } else {
            callback.onError("Error loading all notes");
        }
    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback) {
        /*
         * getId() will return object id or null if it has not been saved
         */
        Note note = new RushSearch().whereId(noteId).findSingle(Note.class);
        if (note != null && callback != null) {
            callback.onSuccess(note);
        } else {
            callback.onError("Error finding the note");
        }
    }

    @Override
    public void saveNote(@NonNull final Note note, @NonNull SaveOrUpdateNoteCallback callback) {
        /* Save asynchronously */
        checkNotNull(note);
        note.save();
        if (callback != null) {
            callback.onSuccess(note);
        }
    }

    @Override
    public void updateNote(final @NonNull Note note, @NonNull final SaveOrUpdateNoteCallback callback) {
        checkNotNull(note);
        note.save(new RushCallback() {
            @Override
            public void complete() {
                if (note.getId() != null) {
                    callback.onSuccess(note);
                } else {
                    callback.onError("Error updating note.");
                }
            }
        });
    }

    @Override
    public void deleteNote(@NonNull Note note) {
        checkNotNull(note);
        RushCore.getInstance().delete(note);
    }

    @Override
    public void deleteNoteById(@NonNull String noteId) {
        checkNotNull(noteId);
        Note foundObject = (Note) new RushSearch().whereId(noteId).find(Note.class).get(0);
        checkNotNull(foundObject);
        RushCore.getInstance().delete(foundObject);
    }

    @Override
    public void deleteAllNotes(@NonNull List<Note> notes, @NonNull RushCallback callback) {
        checkNotNull(notes);
        RushCore.getInstance().delete(notes, callback);
    }

    @Override
    public void completeNote(@NonNull String noteId) {
        getNote(noteId, new GetNoteCallback() {
            @Override
            public void onSuccess(Note note) {
                note.setCompleted(true);
                updateNote(note, null);
            }

            @Override
            public void onError(String message) {

            }
        });

    }

    @Override
    public void getCompletedNotes(@NonNull LoadNotesCallback callback) {
        List<Note> notes = new RushSearch().whereEqual("completed", true).find(Note.class);
        if(callback != null) {
            callback.onSuccess(notes);
        }
    }

    @Override
    public void setNoteAsComplete(@NonNull Note note) {
        checkNotNull(note);
        note.setCompleted(true);
        note.save();
    }

    @Override
    public int getAllNotesSize() {
        return new RushSearch().find(Note.class).size();
    }
}