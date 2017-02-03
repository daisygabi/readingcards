package com.readingcards.collectiondetail;

import android.support.annotation.NonNull;

import com.readingcards.BasePresenter;
import com.readingcards.BaseView;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;

import java.util.List;

/**
 * Created by gabrielaradu on 26/12/2016.
 */

public class CollectionDetailContract {

    interface View extends BaseView<CollectionDetailContract.Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingCollection();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showEditCollection(@NonNull String collectionId);

        void showNotes(List<Note> notes);

        void hideNotes();

        void showErrorMessage(String message);
    }

    interface Presenter extends BasePresenter {

        void editCollection();

        void updateCollection(@NonNull String collectionId, @NonNull CardCollection collection);
    }
}