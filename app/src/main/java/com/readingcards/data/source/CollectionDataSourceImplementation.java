package com.readingcards.data.source;


import android.support.annotation.NonNull;

import com.readingcards.data.domain.CardCollection;

import java.util.List;

import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main entry point for accessing collection data.
 */
public class CollectionDataSourceImplementation implements CollectionDataSource {

    private static CollectionDataSourceImplementation INSTANCE;

    public static CollectionDataSourceImplementation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CollectionDataSourceImplementation();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private CollectionDataSourceImplementation() {
    }

    @Override
    public void getCollections(@NonNull CollectionDataSource.LoadCollectionsCallback callback) {
        List<CardCollection> objects = new RushSearch().find(CardCollection.class);
        if (objects != null && !objects.isEmpty()) {
            callback.onSuccess(objects);
        } else {
            callback.onError("Error loading all collections");
        }
    }



    @Override
    public void getCollection(@NonNull String collectionId, @NonNull CollectionDataSource.GetCollectionCallback callback) {
        /*
         * getId() will return object id or null if it has not been saved
         */
        CardCollection collection = new RushSearch().whereId(collectionId).findSingle(CardCollection.class);
        if (collection != null) {
            callback.onSuccess(collection);
        } else {
            callback.onError("Error finding the collection");
        }
    }

    @Override
    public void saveCollection(@NonNull final CardCollection cardCollection, @NonNull CollectionDataSource.SaveOrUpdateCollectionCallback callback) {
        /* Save asynchronously */
        checkNotNull(cardCollection);
        cardCollection.save();
        callback.onSuccess(cardCollection);
    }

    @Override
    public void updateCollection(final @NonNull CardCollection cardCollection, @NonNull final CollectionDataSource.SaveOrUpdateCollectionCallback callback) {
        checkNotNull(cardCollection);
        cardCollection.save(new RushCallback() {
            @Override
            public void complete() {
                if(cardCollection.getId() != null) {
                    callback.onSuccess(cardCollection);
                } else {
                    callback.onError("Error updating collection.");
                }
            }
        });
    }

    @Override
    public void deleteCollection(@NonNull CardCollection cardCollection) {
        checkNotNull(cardCollection);
        RushCore.getInstance().delete(cardCollection);
    }

    @Override
    public void deleteAllCollections(RushCallback callback) {
        RushCore.getInstance().deleteAll(CardCollection.class, callback);
    }

    @Override
    public boolean hasCollection(final CardCollection collection) {
        CardCollection foundCollection = new RushSearch().whereId(collection.getId()).findSingle(CardCollection.class);
        if(foundCollection != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<CardCollection> getCollections() {
        return null;
    }

    @Override
    public CardCollection getCollection(String collectionId) {
        return new RushSearch().whereId(collectionId).findSingle(CardCollection.class);
    }


    @Override
    public void saveCollection(@NonNull CardCollection cardCollection) {
        checkNotNull(cardCollection);
        cardCollection.save();
    }

    @Override
    public void deleteAllCollections() {
        RushCore.getInstance().deleteAll(CardCollection.class, new RushCallback() {
            @Override
            public void complete() {

            }
        });
    }

    @Override
    public void updateCollection(final CardCollection cardCollection) {
        checkNotNull(cardCollection);
        cardCollection.save(new RushCallback() {
            @Override
            public void complete() {
            }
        });
    }
}