package com.readingcards.collectiondetail;

import com.google.common.base.Strings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.repository.CollectionsRepository;
import com.readingcards.data.source.CollectionDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Present all information from a specific collection
 */
public class CollectionDetailPresenter implements CollectionDetailContract.Presenter {

    @NonNull
    private CollectionsRepository repository;

    @NonNull
    private CollectionDetailContract.View detailView;

    private String collectionId;

    public CollectionDetailPresenter(@Nullable String collectionId,
                                     @NonNull CollectionsRepository repository,
                                     @NonNull CollectionDetailContract.View detailView) {
        this.collectionId = collectionId;
        this.repository = checkNotNull(repository, "collection repository cannot be null!");
        this.detailView = checkNotNull(detailView, "collection view cannot be null!");

        this.detailView.setPresenter(this);
    }

    @Override
    public void editCollection() {
        if (Strings.isNullOrEmpty(collectionId)) {
            detailView.showMissingCollection();
            return;
        }
        detailView.setLoadingIndicator(true);
        detailView.showEditCollection(collectionId);
    }

    @Override
    //TODO refactor this
    public void updateCollection(@NonNull final String collectionId, @NonNull final CardCollection collection) {
        checkNotNull(collection);
        repository.getCollection(collectionId, new CollectionDataSource.GetCollectionCallback() {
            @Override
            public void onSuccess(CardCollection cardCollection) {
                cardCollection.setTitle(collection.getTitle());
                cardCollection.setDescription(collection.getDescription());
                cardCollection.setNotes(collection.getNotes());
                repository.updateCollection(cardCollection, new CollectionDataSource.SaveOrUpdateCollectionCallback() {

                    @Override
                    public void onSuccess(CardCollection cardCollection) {
                        detailView.showEditCollection(cardCollection.getId());
                    }

                    @Override
                    public void onError(String message) {
                        detailView.showErrorMessage(message);
                    }
                });
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void start() {
        showCollectionDetail();
    }

    // TODO refactor this
    private void showCollectionDetail() {
        repository.getCollection(collectionId, new CollectionDataSource.GetCollectionCallback() {

            @Override
            public void onSuccess(CardCollection cardCollection) {
                if (null == cardCollection || Strings.isNullOrEmpty(collectionId)) {
                    detailView.showMissingCollection();
                } else {
                    detailView.setLoadingIndicator(true);
                    if (null == cardCollection) {
                        detailView.showMissingCollection();
                    } else {
                        showCollection(cardCollection);
                    }
                }
            }

            @Override
            public void onError(String message) {
                detailView.setLoadingIndicator(false);
                detailView.showMissingCollection();
            }
        });
    }

    private void showCollection(CardCollection cardCollection) {
        String title = cardCollection.getTitle();
        String description = cardCollection.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            detailView.hideTitle();
        } else {
            detailView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            detailView.hideDescription();
        } else {
            detailView.showDescription(description);
        }

        if (cardCollection.getNotes() == null || cardCollection.getNotes().isEmpty()) {
            detailView.hideNotes();
        } else {
            detailView.showNotes(cardCollection.getNotes());
        }
    }
}