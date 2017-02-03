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

package com.readingcards.data.repository;

import android.support.annotation.NonNull;

import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.source.CollectionDataSource;
import com.readingcards.util.CardCollectionAlreadyExistsException;

import java.util.List;

import co.uk.rushorm.core.RushCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load collections from the data sources
 */
public class CollectionsRepository implements CollectionDataSource {

    private static CollectionsRepository INSTANCE = null;
    private final CollectionDataSource dataSource;

    // Prevent direct instantiation.
    private CollectionsRepository(@NonNull CollectionDataSource dataSource) {
        this.dataSource = checkNotNull(dataSource);
    }

    public static CollectionsRepository getInstance(CollectionDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CollectionsRepository(dataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getCollections(@NonNull CollectionDataSource.LoadCollectionsCallback callback) {
        dataSource.getCollections(callback);
    }

    @Override
    public void getCollection(@NonNull String collectionId, @NonNull CollectionDataSource.GetCollectionCallback callback) {
        dataSource.getCollection(collectionId, callback);
    }

    @Override
    public void saveCollection(@NonNull CardCollection cardCollection, @NonNull final CollectionDataSource.SaveOrUpdateCollectionCallback callback)
            throws CardCollectionAlreadyExistsException {
        if (dataSource.hasCollection(cardCollection)) {
            throw new CardCollectionAlreadyExistsException();
        }
        dataSource.saveCollection(cardCollection, callback);
    }

    @Override
    public void updateCollection(@NonNull CardCollection cardCollection, @NonNull CollectionDataSource.SaveOrUpdateCollectionCallback callback) {
        dataSource.updateCollection(cardCollection, callback);
    }

    public void deleteCollection(@NonNull CardCollection cardCollection) {
//        if (!dataSource.hasCollection(cardCollection)) {
//            throw new CardCollectionDoesNotExistsException();
//        }
        dataSource.deleteCollection(cardCollection);
    }

    @Override
    public void deleteAllCollections(@NonNull RushCallback callback) {
        dataSource.deleteAllCollections(callback);
    }

    @Override
    public boolean hasCollection(CardCollection collection) {
        return dataSource.hasCollection(collection);
    }

    @Override
    public List<CardCollection> getCollections() {
        return dataSource.getCollections();
    }

    @Override
    public CardCollection getCollection(String collectionId) {
        return dataSource.getCollection(collectionId);
    }

    @Override
    public void saveCollection(@NonNull CardCollection cardCollection) {
        dataSource.saveCollection(cardCollection);
    }

    @Override
    public void deleteAllCollections() {
        dataSource.deleteAllCollections();
    }

    @Override
    public void updateCollection(CardCollection collection) {
        dataSource.updateCollection(collection);
    }
}