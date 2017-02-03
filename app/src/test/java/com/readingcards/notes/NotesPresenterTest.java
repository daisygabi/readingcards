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

import com.google.common.collect.Lists;

import android.os.Build;

import com.readingcards.BuildConfig;
import com.readingcards.ProjectApplication;
import com.readingcards.data.domain.Note;
import com.readingcards.data.repository.NotesRepository;
import com.readingcards.data.source.NoteDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link NotesPresenter}
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = ProjectApplication.class)
@RunWith(RobolectricTestRunner.class)
public class NotesPresenterTest {

    private static List<Note> notes;

    @Mock
    private NotesRepository repository;

    @Mock
    private NotesContract.View view;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<NoteDataSource.LoadNotesCallback> loadNotesCallbackCaptor;

    private NotesPresenter presenter;

    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        presenter = new NotesPresenter(repository, view);

        // The presenter won't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        notes = Lists.newArrayList(new Note("Title1", "Description1", true),
                new Note("Title2", "Description2", true), new Note("Title3", "Description3", true));
    }

    @Test
    public void loadAllNotesFromRepositoryAndLoadIntoView() {
        // Given an initialized NotesPresenter with initialized notes
        // When loading of notes is requested
        presenter.setFiltering(NotesFilterType.ALL);
        presenter.loadNotes(true);

        // Callback is captured and invoked with stubbed tasks
        verify(repository).getNotes(loadNotesCallbackCaptor.capture());
        loadNotesCallbackCaptor.getValue().onSuccess(notes);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(view);
        inOrder.verify(view).setLoadingIndicator(true);
        // Then progress indicator is hidden and all notes are shown in UI
        inOrder.verify(view).setLoadingIndicator(false);
        ArgumentCaptor<List> showNotesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(view).showNotes(showNotesArgumentCaptor.capture());
        assertTrue(showNotesArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void completeNote_ShowsNoteMarkedComplete() {
        // Given a stubbed note
        Note note = new Note("Details Requested", "For this note", true);

        // When note is marked as complete
        presenter.completeNote(note);

        // Then repository is called and note marked complete UI is shown
        verify(repository).setNoteAsComplete(note);
        verify(view).showMessage("Note was set as complete.");
    }

    @Test
    public void loadCompletedNotesFromRepositoryAndLoadIntoView() {
        // Given an initialized NotesPresenter with initialized notes
        presenter.setFiltering(NotesFilterType.COMPLETED);
        presenter.loadNotes(true);

        // Callback is captured and invoked with stubbed notes
        verify(repository).getNotes(loadNotesCallbackCaptor.capture());
        loadNotesCallbackCaptor.getValue().onSuccess(notes);

        // Then progress indicator is hidden and completed notes are shown in UI
        verify(view).setLoadingIndicator(false);
        ArgumentCaptor<List> showNotesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(view).showNotes(showNotesArgumentCaptor.capture());
        assertTrue(showNotesArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void clickOnFab_ShowsAddNoteUi() {
        // When adding a new note
        presenter.addNewNote();

        // Then add note UI is shown
        verify(view).showAddNote();
    }

    @Test
    public void clickOnNote_ShowsDetailUi() {
        // Given a stubbed active note
        Note requestedNote = new Note("Details Requested", "For this note", false);

        // When open note details is requested
        presenter.openNoteDetails(requestedNote);

        // Then note detail UI is shown
        verify(view).showNoteDetailsUi(any(String.class));
    }

    @Test
    public void unavailableNotes_ShowsError() {
        presenter.setFiltering(NotesFilterType.ALL);
        presenter.loadNotes(true);

        // And the notes aren't available in the repository
        verify(repository).getNotes(loadNotesCallbackCaptor.capture());
        loadNotesCallbackCaptor.getValue().onError("no data");

        // Then an error message is shown
        verify(view).showNoNotes();
    }
}
