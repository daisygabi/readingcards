package com.readingcards.addeditnote;

import com.readingcards.data.domain.Note;
import com.readingcards.data.repository.NotesRepository;
import com.readingcards.data.source.NoteDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLooper;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by gabrielaradu on 23/01/2017.
 */
public class AddEditNotePresenterTest {

    private AddEditNotePresenter presenter;
    private Note note;

    @Mock
    private NotesRepository repository;

    @Mock
    private AddEditNoteContract.View view;

    @Mock
    private NoteDataSource.GetNoteCallback getNoteCallback;
    @Captor
    private ArgumentCaptor<NoteDataSource.GetNoteCallback> getNoteCallbackArgumentCaptor;

    @Mock
    private NoteDataSource.SaveOrUpdateNoteCallback saveOrUpdateNoteCallback;
    @Captor
    private ArgumentCaptor<NoteDataSource.SaveOrUpdateNoteCallback> saveOrUpdateNoteCallbackArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        note = new Note("Title", "Description", false);
        presenter = new AddEditNotePresenter(note.getId(), repository, view, false);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void start() throws Exception {
        presenter.start();
    }

    @Test
    public void saveOrUpdateNote() throws Exception {
        presenter.saveOrUpdateNote(note);
        if (presenter.isNewNote()) {
            verify(repository).saveNote(eq(note), saveOrUpdateNoteCallbackArgumentCaptor.capture());
        } else {
            verify(repository).updateNote(eq(note), saveOrUpdateNoteCallbackArgumentCaptor.capture());
        }
        saveOrUpdateNoteCallbackArgumentCaptor.getValue().onSuccess((Note) Mockito.any());
        verify(view).showNoteList(); // shown in the UI
    }

    @Test
    public void saveNote_emptyNoteShowsErrorUi() {
        Note note = new Note("", "", false);
        presenter.saveOrUpdateNote(note);
        if (presenter.isNewNote()) {
            verify(repository).saveNote(eq(note), saveOrUpdateNoteCallbackArgumentCaptor.capture());
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            saveOrUpdateNoteCallbackArgumentCaptor.getValue().onError("Error creating new Note");

            // Then an empty not error is shown in the UI
            verify(view).showErrorMessage("Error creating new Note");
        }
    }

    @Test
    public void populateNote() throws Exception {
        if (presenter.isNewNote()) {
            System.out.println("populateNote() was called but note is new.");
        } else {
            presenter.populateNote();
            verify(repository).getNote(note.getId(), getNoteCallbackArgumentCaptor.capture());
            getNoteCallbackArgumentCaptor.getValue().onSuccess(note);
        }
    }

    @Test
    public void isDataMissing() throws Exception {
        presenter.isDataMissing();
        verify(view).showErrorMessage("Data not found.");
    }
}