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

package com.readingcards.cards;

import com.readingcards.BasePresenter;
import com.readingcards.BaseView;
import com.readingcards.data.domain.Note;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ReadNotesAsCardContract {

    interface View extends BaseView<Presenter> {

        void showEmptyNoteError();

        void setLoadingIndicator(boolean active);

        void showNotesAsCards(List<Note> notes);

        void showNoNotes();

        void showSuccessfullyLoadedNotes();

        void showNoTimerFound();
    }

    interface Presenter extends BasePresenter {

        void loadNotes(boolean forceUpdate);

        void scheduleCardReading(String time);
    }
}
