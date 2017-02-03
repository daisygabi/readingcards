package com.readingcards.data.source;


import android.support.annotation.NonNull;

import com.readingcards.data.domain.CardCollection;
import com.readingcards.util.CardCollectionAlreadyExistsException;

import java.util.List;

import co.uk.rushorm.core.RushCallback;

/**
 * Main entry point for accessing collection data.
 */
public interface CollectionDataSource {

    interface LoadCollectionsCallback {

        void onSuccess(List<CardCollection> cardCollections);

        void onError(String message);
    }

    interface GetCollectionCallback {

        void onSuccess(CardCollection cardCollection);

        void onError(String message);
    }

    interface SaveOrUpdateCollectionCallback {

        void onSuccess(CardCollection cardCollection);

        void onError(String message);
    }

    void getCollections(@NonNull CollectionDataSource.LoadCollectionsCallback callback);

    void getCollection(@NonNull String collectionId, CollectionDataSource.GetCollectionCallback callback);

    void saveCollection(@NonNull CardCollection cardCollection,
                        @NonNull CollectionDataSource.SaveOrUpdateCollectionCallback callback)
            throws CardCollectionAlreadyExistsException;

    void updateCollection(@NonNull CardCollection cardCollection,
                          @NonNull CollectionDataSource.SaveOrUpdateCollectionCallback callback);

    void deleteCollection(@NonNull CardCollection cardCollection);

    void deleteAllCollections(@NonNull RushCallback callback);

    boolean hasCollection(CardCollection collection);

    List<CardCollection> getCollections();
    CardCollection getCollection(String collectionId);
    void saveCollection(@NonNull CardCollection cardCollection);
    void deleteAllCollections();
    void updateCollection(CardCollection collection);
}