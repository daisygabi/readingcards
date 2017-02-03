package com.readingcards.addcardcollection;

import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.data.repository.CollectionsRepository;
import com.readingcards.data.repository.NotesRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

/**
 * Created by gabrielaradu on 23/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddCardCollectionPresenterTest {

    private AddCardCollectionPresenter presenter;

    @Mock
    private CollectionsRepository repository;

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private AddCardCollectionContract.View view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new AddCardCollectionPresenter(null, repository, notesRepository, view, false);
    }

    @Test
    public void saveOrUpdateCollection() throws Exception {
        CardCollection collection = new CardCollection("Title", "Description", null);
        presenter.saveOrUpdateCollection(collection);
    }

    @Test
    public void saveOrUpdateEmptyCollection() throws Exception {
        CardCollection collection = null;
        if (collection == null || collection.isEmpty()) {
            view.showEmptyCollectionError();
        }
    }

    @Test
    public void loadNoteListToAppendToCollection() throws Exception {
        List<Note> notes = presenter.loadNoteListToAppendToCollection();
        view.showNotesToAppendList(notes);
    }

    @After
    public void tearDown() throws Exception {

    }
}