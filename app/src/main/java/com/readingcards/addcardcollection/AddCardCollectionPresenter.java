package com.readingcards.addcardcollection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.data.repository.CollectionsRepository;
import com.readingcards.data.repository.NotesRepository;
import com.readingcards.data.source.CollectionDataSource;
import com.readingcards.data.source.NoteDataSource;
import com.readingcards.util.CardCollectionAlreadyExistsException;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link AddCardCollectionFragment}), retrieves the data
 * and updates the UI as required.
 */
public class AddCardCollectionPresenter implements AddCardCollectionContract.Presenter, CollectionDataSource.SaveOrUpdateCollectionCallback {

    @NonNull
    private CollectionsRepository collectionRepository;

    @NonNull
    private NotesRepository notesRepository;

    @NonNull
    private AddCardCollectionContract.View addCollectionView;

    private boolean isDataMissing;

    @Nullable
    private String collectionId;

    private List<Note> notesToAppend;

    /**
     * @param collectionId           if id has value it's the card collection that needs to be
     *                               edited, if null is a new card collection entry
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddCardCollectionPresenter(String collectionId, CollectionsRepository collectionRepository,
                                      NotesRepository notesRepository,
                                      AddCardCollectionContract.View addCardCollectionFragment, boolean shouldLoadDataFromRepo) {
        this.collectionId = collectionId;
        this.collectionRepository = checkNotNull(collectionRepository);
        this.notesRepository = checkNotNull(notesRepository);
        this.addCollectionView = checkNotNull(addCardCollectionFragment);
        this.isDataMissing = shouldLoadDataFromRepo;
    }

    @Override
    public void saveOrUpdateCollection(CardCollection cardCollection) {
        if (isNewCardCollection()) {
            try {
                createCardCollection(cardCollection);
            } catch (CardCollectionAlreadyExistsException e) {
                e.printStackTrace();
            }
        } else {
            updateCardCollection(cardCollection);
        }
    }

    private void createCardCollection(CardCollection cardCollection) throws CardCollectionAlreadyExistsException {
        if (cardCollection.isEmpty()) {
            addCollectionView.showEmptyCollectionError();
        } else {
            collectionRepository.saveCollection(cardCollection, new CollectionDataSource.SaveOrUpdateCollectionCallback() {

                @Override
                public void onSuccess(CardCollection cardCollection) {
                    checkNotNull(addCollectionView);
                    addCollectionView.showCollectionList();
                }

                @Override
                public void onError(String message) {
                    checkNotNull(addCollectionView);
                    addCollectionView.showErrorMessage("Woops, something went wrong. Error creating a new Card.");
                }
            });
        }
    }

    private void updateCardCollection(CardCollection cardCollection) {
        if (cardCollection.isEmpty()) {
            addCollectionView.showEmptyCollectionError();
        } else {
            collectionRepository.updateCollection(cardCollection, new CollectionDataSource.SaveOrUpdateCollectionCallback() {

                @Override
                public void onSuccess(CardCollection cardCollection) {
                }

                @Override
                public void onError(String message) {
                    addCollectionView.showErrorMessage("Woops, something went wrong. Error updating Card.");
                }
            });
            addCollectionView.showCollectionList();
        }
    }

    private boolean isNewCardCollection() {
        return collectionId == null ? true : false;
    }

    @Override
    public void populateCollection() {
        if (isNewCardCollection()) {
            throw new RuntimeException("populateCollection() was called but card collection is new.");
        }
        collectionRepository.getCollection(collectionId, new CollectionDataSource.GetCollectionCallback() {

            @Override
            public void onSuccess(CardCollection cardCollection) {
                addCollectionView.showCollectionList();
            }

            @Override
            public void onError(String message) {
                addCollectionView.showErrorMessage("Error showing collection");
            }
        });
    }

    @Override
    public List<Note> loadNoteListToAppendToCollection() {
        checkNotNull(notesRepository);
        notesRepository.getNotes(new NoteDataSource.LoadNotesCallback() {

            @Override
            public void onSuccess(List<Note> notes) {
                checkNotNull(notes);
                notesToAppend = notes;
                addCollectionView.showNotesToAppendList(notes);
            }

            @Override
            public void onError(String message) {

            }
        });
        return notesToAppend;
    }

    @Override
    public void start() {
        if (!isNewCardCollection() && isDataMissing) {
            populateCollection();
        }
    }


    @Override
    public void onSuccess(CardCollection cardCollection) {
        checkNotNull(addCollectionView);
        addCollectionView.showCollectionList();
    }

    @Override
    public void onError(String message) {
        checkNotNull(addCollectionView);
        addCollectionView.showErrorMessage("Woops, something went wrong. Error creating a new Card.");
    }
}