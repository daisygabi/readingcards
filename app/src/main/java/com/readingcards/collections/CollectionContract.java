package com.readingcards.collections;

import android.support.annotation.NonNull;

import com.readingcards.BasePresenter;
import com.readingcards.BaseView;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.util.CardCollectionDoesNotExistsException;

import java.util.List;

/**
 * Created by gabrielaradu on 26/12/2016.
 */

public interface CollectionContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showCollections(List<CardCollection> collections);

        void showAddCollection();

        void showNoCollections();

        void showSuccessfullySavedMessage(String message);

        void showCardCollectionDetailsUi(String collectionId);
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadCollections(boolean forceUpdate);

        void openCollection(CardCollection collection);

        void addNewCollection();

        void deleteCollection(CardCollection collection) throws CardCollectionDoesNotExistsException;

        void deleteAllCollections();

        void setGreeting(@NonNull final String greeting);
    }
}