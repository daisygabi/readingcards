package com.readingcards.data.repository;

import com.readingcards.collections.CollectionContract;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.data.source.CollectionDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by gabrielaradu on 31/01/2017.
 */
public class CollectionsRepositoryTest {

    private CollectionsRepository repository;
    private CollectionDataSource mockDatasource;
    private CardCollection collection;
    private CollectionContract.View collectionsView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockDatasource = mock(CollectionDataSource.class);
        repository = CollectionsRepository.getInstance(mockDatasource);
        collection = mock(CardCollection.class);//new CardCollection("Title", "Description", new ArrayList<Note>());
        collectionsView = mock(CollectionContract.View.class);

        when(repository.hasCollection(any(CardCollection.class))).thenReturn(true);
    }

    @Test
    public void saveCollection() throws Exception {
        List<Note> notes = new ArrayList<Note>();
        notes.add(new Note("title", "description", false));
        notes.add(new Note("title2", "description2", true));
        collection.setNotes(notes);

        doNothing().when(mockDatasource).saveCollection(collection);
        repository.saveCollection(collection);

        assertTrue(repository.hasCollection(collection));
    }

    @Test
    public void openCollection() throws Exception {
        collectionsView.showCardCollectionDetailsUi(collection.getId());
    }

    @Test
    public void deleteAllCollections() throws Exception {
        doNothing().when(mockDatasource).deleteAllCollections();
        repository.deleteAllCollections();
        assertEquals(0, repository.getCollections().size());
    }
}