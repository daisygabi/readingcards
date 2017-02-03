package com.readingcards.data.repository;

import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.data.source.CollectionDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gabrielaradu on 31/01/2017.
 */
public class CollectionsRepositoryTest {

    private CollectionsRepository repository;
    private CollectionDataSource mockDatasource;
    private CardCollection collection;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockDatasource = mock(CollectionDataSource.class);
        repository = CollectionsRepository.getInstance(mockDatasource);
        collection = new CardCollection("Title", "Description", new ArrayList<Note>());

        when(repository.hasCollection(any(CardCollection.class))).thenReturn(true);
    }

    @Test
    public void saveCollection() throws Exception {
        CardCollection cardCollection = mock(CardCollection.class);
        List<Note> notes = new ArrayList<Note>();
        notes.add(new Note("title", "description", false));
        notes.add(new Note("title2", "description2", true));
        cardCollection.setNotes(notes);

        doNothing().when(mockDatasource).saveCollection(cardCollection);
        repository.saveCollection(cardCollection);
        verify(mockDatasource).saveCollection(cardCollection);

        assertTrue(repository.hasCollection(cardCollection));
    }

    @Test
    public void updateCollection() throws Exception {
        //CardCollection cardCollection = mock(CardCollection.class);
        collection.setTitle("Updated");

        doNothing().when(mockDatasource).updateCollection(collection);
        repository.updateCollection(collection);
        verify(mockDatasource).updateCollection(collection);

        //assertTrue(repository.hasCollection(cardCollection));
    }
}