package com.readingcards.collections;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.readingcards.addcardcollection.AddCardCollectionActivity;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.repository.CollectionsRepository;
import com.readingcards.data.source.CollectionDataSource;
import com.readingcards.util.CardCollectionDoesNotExistsException;

import java.util.List;

import javax.inject.Inject;

import co.uk.rushorm.core.RushCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Show list/grid/etc of all the existing collections. It's a general presentation of them.
 */
public class CollectionPresenter implements CollectionContract.Presenter {

    @NonNull
    private CollectionsRepository repository;

    @NonNull
    private CollectionContract.View collectionsView;

    private boolean mFirstLoad = true;

    @Inject
    public CollectionPresenter(CollectionsRepository repository, @NonNull CollectionContract.View collectionFragment) {
        this.repository = checkNotNull(repository, "repository cannot be null");
        this.collectionsView = checkNotNull(collectionFragment, "view cannot be null!");

        collectionsView.setPresenter(this);
    }

    @Override
    public void loadCollections(boolean forceUpdate) {
        loadCollections(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void openCollection(CardCollection collection) {
        checkNotNull(collection, "Card collection cannot be null!");
        collectionsView.showCardCollectionDetailsUi(collection.getId());
    }

    @Override
    public void addNewCollection() {
        collectionsView.showAddCollection();
    }

    @Override
    public void deleteCollection(CardCollection collection) throws CardCollectionDoesNotExistsException {
        deleteOneCollection(collection);
        // TODO show on view this modification?
    }

    private void deleteOneCollection(CardCollection collection) throws CardCollectionDoesNotExistsException {
        checkNotNull(collection);
        repository.deleteCollection(collection);
    }

    @Override
    public void deleteAllCollections() {
        repository.deleteAllCollections(new RushCallback() {
            @Override
            public void complete() {
                collectionsView.showNoCollections();
            }
        });
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link com.readingcards.data.source.NoteDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadCollections(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            collectionsView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            repository.getCollections(new CollectionDataSource.LoadCollectionsCallback() {
                @Override
                public void onSuccess(List<CardCollection> cardCollections) {
                    if (showLoadingUI) {
                        collectionsView.setLoadingIndicator(false);
                    }
                    processCollections(cardCollections);
                }

                @Override
                public void onError(String message) {
                    collectionsView.showNoCollections();
                    collectionsView.setLoadingIndicator(false);
                }
            });
        }
    }

    private void processCollections(final List<CardCollection> cardCollections) {
        if (cardCollections.isEmpty()) {
            // Show a message indicating there are no notes for that filter type.
            collectionsView.showNoCollections();
        } else {
            // Show the list of collections
            collectionsView.showCollections(cardCollections);
        }
    }

    @Override
    public void start() {
        loadCollections(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a note was successfully added, show message
        if (AddCardCollectionActivity.REQUEST_ADD_COLLECTION == requestCode && Activity.RESULT_OK == resultCode) {
            collectionsView.showSuccessfullySavedMessage("");
        }
    }

    @Override
    public void setGreeting(@NonNull final String greeting) {
        collectionsView.showSuccessfullySavedMessage(greeting);
    }
}