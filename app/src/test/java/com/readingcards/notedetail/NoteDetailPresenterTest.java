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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link NoteDetailPresenter}
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = ProjectApplication.class)
@RunWith(RobolectricTestRunner.class)
public class NoteDetailPresenterTest {

    public static final String TITLE_TEST = "title";

    public static final String DESCRIPTION_TEST = "description";

    public static final String INVALID_NOTE_ID = "";

    public static final Note ACTIVE_NOTE = new Note(TITLE_TEST, DESCRIPTION_TEST, true);

    public static final Note COMPLETED_NOTE = new Note(TITLE_TEST, DESCRIPTION_TEST, true);

    @Mock
    private NotesRepository mNotesRepository;

    @Mock
    private NoteDetailContract.View mNoteDetailView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<NoteDataSource.GetNoteCallback> mGetNoteCallbackCaptor;

    private NoteDetailPresenter mNoteDetailPresenter;

    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter won't update the view unless it's active.
        when(mNoteDetailView.isActive()).thenReturn(true);
    }

    @Test
    public void getActiveNoteFromRepositoryAndLoadIntoView() {
        // When notes presenter is asked to open a note
        mNoteDetailPresenter = new NoteDetailPresenter(
                ACTIVE_NOTE.getId(), mNotesRepository, mNoteDetailView);
        mNoteDetailPresenter.start();

        // Then note is loaded from model, callback is captured and progress indicator is shown
        verify(mNotesRepository).getNote(eq(ACTIVE_NOTE.getId()), mGetNoteCallbackCaptor.capture());
        InOrder inOrder = inOrder(mNoteDetailView);
        inOrder.verify(mNoteDetailView).setLoadingIndicator(true);

        // When note is finally loaded
        mGetNoteCallbackCaptor.getValue().onSuccess(ACTIVE_NOTE); // Trigger callback

        // Then progress indicator is hidden and title, description and are shown in UI
        inOrder.verify(mNoteDetailView).setLoadingIndicator(false);
        verify(mNoteDetailView).showTitle(TITLE_TEST);
        verify(mNoteDetailView).showDescription(DESCRIPTION_TEST);
    }

    @Test
    public void getCompletedNoteFromRepositoryAndLoadIntoView() {
        mNoteDetailPresenter = new NoteDetailPresenter(
                COMPLETED_NOTE.getId(), mNotesRepository, mNoteDetailView);
        mNoteDetailPresenter.start();

        // Then note is loaded from model, callback is captured and progress indicator is shown
        verify(mNotesRepository).getNote(
                eq(COMPLETED_NOTE.getId()), mGetNoteCallbackCaptor.capture());
        InOrder inOrder = inOrder(mNoteDetailView);
        inOrder.verify(mNoteDetailView).setLoadingIndicator(true);

        // When note is finally loaded
        mGetNoteCallbackCaptor.getValue().onSuccess(COMPLETED_NOTE); // Trigger callback

        // Then progress indicator is hidden and title, description are shown in UI
        inOrder.verify(mNoteDetailView).setLoadingIndicator(false);
        verify(mNoteDetailView).showTitle(TITLE_TEST);
        verify(mNoteDetailView).showDescription(DESCRIPTION_TEST);
    }

    @Test
    public void getUnknownNoteFromRepositoryAndLoadIntoView() {
        // When loading of a note is requested with an invalid ID.
        mNoteDetailPresenter = new NoteDetailPresenter(
                INVALID_NOTE_ID, mNotesRepository, mNoteDetailView);
        mNoteDetailPresenter.start();
        verify(mNoteDetailView).showMissingNote();
    }

    @Test
    public void deleteNote() {
        // Given an initialized NoteDetailPresenter with stubbed note
        Note note = new Note(TITLE_TEST, DESCRIPTION_TEST, true);

        // When the deletion of a note is requested
        mNoteDetailPresenter = new NoteDetailPresenter(
                note.getId(), mNotesRepository, mNoteDetailView);
        mNoteDetailPresenter.deleteNote();

        // Then the repository and the view are notified
        verify(mNotesRepository).deleteNote(note);
    }

    @Test
    public void completeNote() {
        // Given an initialized presenter with an active note
        Note note = new Note(TITLE_TEST, DESCRIPTION_TEST, true);
        mNoteDetailPresenter = new NoteDetailPresenter(
                note.getId(), mNotesRepository, mNoteDetailView);
        mNoteDetailPresenter.start();

        // When the presenter is asked to complete the note
        mNoteDetailPresenter.completeNote();

        // Then a request is sent to the note repository and the UI is updated
        verify(mNotesRepository).completeNote(note.getId());
        verify(mNoteDetailView).showNoteMarkedComplete();
    }

    @Test
    public void activeNoteIsShownWhenEditing() {
        // When the edit of an ACTIVE_NOTE is requested
        mNoteDetailPresenter = new NoteDetailPresenter(
                ACTIVE_NOTE.getId(), mNotesRepository, mNoteDetailView);
        mNoteDetailPresenter.editNote();

        // Then the view is notified
        verify(mNoteDetailView).showEditNote(ACTIVE_NOTE.getId());
    }

    @Test
    public void invalidNoteIsNotShownWhenEditing() {
        // When the edit of an invalid note id is requested
        mNoteDetailPresenter = new NoteDetailPresenter(
                INVALID_NOTE_ID, mNotesRepository, mNoteDetailView);
        mNoteDetailPresenter.editNote();

        // Then the edit mode is never started
        verify(mNoteDetailView, never()).showEditNote(INVALID_NOTE_ID);
        // instead, the error is shown.
        verify(mNoteDetailView).showMissingNote();
    }
}