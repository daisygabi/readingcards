package com.readingcards.data.repository;

import com.google.common.collect.Lists;

import android.os.Build;

import com.readingcards.BuildConfig;
import com.readingcards.ProjectApplication;
import com.readingcards.data.domain.Note;
import com.readingcards.data.source.NoteDataSource;
import com.readingcards.notes.NotesContract;
import com.readingcards.notes.NotesPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by gabrielaradu on 23/01/2017.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M, application = ProjectApplication.class)
@RunWith(RobolectricTestRunner.class)
public class NotesRepositoryTest {

    private NotesPresenter presenter;

    @Mock
    private NotesRepository repository;

    @Mock
    private NotesContract.View view;

    @Mock
    private NoteDataSource.LoadNotesCallback loadNotesCallback;
    @Captor
    private ArgumentCaptor<NoteDataSource.LoadNotesCallback> loadNotesCallbackArgumentCaptor;

    private static List<Note> NOTES = Lists.newArrayList(new Note("Title1", "Description1", false),
            new Note("Title2", "Description2", false));
    private static Note note = new Note("title", "description", false);

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        presenter = new NotesPresenter(repository, view);
    }

    @Test
    public void getNotes() throws Exception {
        presenter.loadNotes(true);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // Callback is captured and invoked with stubbed notes
        verify(repository).getNotes(loadNotesCallbackArgumentCaptor.capture());
        loadNotesCallbackArgumentCaptor.getValue().onSuccess(NOTES);

        // Then progress indicator is hidden and collections are shown in UI
        verify(view).setLoadingIndicator(false);
        verify(view).showNotes(NOTES);
    }

    @Test
    public void openNoteDetails() throws Exception {
        presenter.openNoteDetails(note);
        verify(view).showNoteDetailsUi(note.getId());
    }
}