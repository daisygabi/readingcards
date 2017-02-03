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

import android.support.annotation.NonNull;

import com.readingcards.BasePresenter;
import com.readingcards.BaseView;
import com.readingcards.data.domain.Note;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface NotesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showAddNote();

        void showNoteDetailsUi(String noteId);

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showMessage(String message);

        boolean isActive();

        void showNotes(List<Note> notes);

        void showCompletedNotes(List<Note> notes);

        void showFilteringPopUpMenu();

        void showNoNotes();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadNotes(boolean forceUpdate);

        void addNewNote();

        void openNoteDetails(@NonNull Note requestedNote);

        void completeNote(@NonNull Note completedNote);

        void showFiltered(NotesFilterType requestType);

        NotesFilterType getFiltering();

        void deleteAllNotes();

        void setFiltering(NotesFilterType type);
    }
}
