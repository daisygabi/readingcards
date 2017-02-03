package com.readingcards.addcardcollection;

import com.readingcards.BasePresenter;
import com.readingcards.BaseView;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;

import java.util.List;

/**
 * Created by gabrielaradu on 09/01/2017.
 */

public class AddCardCollectionContract {

    interface View extends BaseView<Presenter> {

        void showEmptyCollection();

        void showEmptyNoteList();

        void showEmptyCollectionError();

        void showErrorMessage(String message);

        void showCollectionList();

        void showNotesToAppendList(List<Note> notesToAppend);

        void setTitle(String title);

        void setDescription(String description);

    }

    // Database layer
    interface Presenter extends BasePresenter {

        void saveOrUpdateCollection(CardCollection collection);

        void populateCollection();

        List<Note> loadNoteListToAppendToCollection();
    }
}
