package com.readingcards.collections;

import com.google.common.collect.Lists;

import android.os.Build;

import com.readingcards.BuildConfig;
import com.readingcards.ProjectApplication;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.repository.CollectionsRepository;
import com.readingcards.data.source.CollectionDataSource;
import com.readingcards.util.CardCollectionDoesNotExistsException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import co.uk.rushorm.core.RushCallback;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by gabrielaradu
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = ProjectApplication.class)
@RunWith(RobolectricTestRunner.class)
public class CollectionPresenterTest {

    private CollectionPresenter presenter;

    @Mock
    private CollectionsRepository repository;

    @Mock
    private CollectionContract.View mockView;


    private CollectionDataSource.LoadCollectionsCallback loadCollectionsCallback;
    @Captor
    private ArgumentCaptor<CollectionDataSource.LoadCollectionsCallback> loadCollectionsCallbackArgumentCaptor;

    @Mock
    private CollectionDataSource.SaveOrUpdateCollectionCallback saveOrUpdateCollectionCallback;
    @Captor
    private ArgumentCaptor<CollectionDataSource.SaveOrUpdateCollectionCallback> saveOrUpdateCollectionCallbackArgumentCaptor;

    @Mock
    private RushCallback rushCallback;
    @Captor
    private ArgumentCaptor<RushCallback> rushCallbackArgumentCaptor;

    @Mock
    public ProjectApplication application;

    private static List<CardCollection> collections;
    private static CardCollection collection;

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mockView = mock(CollectionContract.View.class);

        // Get a reference to the class under test
        presenter = new CollectionPresenter(repository, mockView);

        collections = Lists.newArrayList(new CardCollection("Title1", "Description1", null),
                new CardCollection("Title2", "Description2", null));
        collection = new CardCollection("title", "description", null);
    }

    @Test(expected = NullPointerException.class)
    public void testShowingUIWhenViewIsNull() {
        presenter = new CollectionPresenter(repository, null);
        verify(mockView).showAddCollection();
    }

    @Test
    public void loadCollectionsFromRepositoryAndLoadIntoView() {
        // Given an initialized presenter with initialized collections
        // When loading of Collections is requested
        presenter.loadCollections(false);

        Mockito.verify(mockView).setLoadingIndicator(true);
        verify(repository).getCollections(loadCollectionsCallbackArgumentCaptor.capture());
        loadCollectionsCallback = loadCollectionsCallbackArgumentCaptor.getValue();

        // Then progress indicator is hidden and collections are shown in UI
        Mockito.verify(mockView).showCollections(anyListOf(CardCollection.class));
    }

    @Test
    public void addNewCollection() {
        // When adding a new collection
        presenter.addNewCollection();

        // Show in UI
        verify(mockView).showAddCollection();
    }

    @Test
    public void deleteOneCollection() throws CardCollectionDoesNotExistsException{
        // When deleting a collection
        presenter.deleteCollection(collection);
    }

    @Test
    public void deleteAllCollections() {
        // When deleting ALL collections
        presenter.deleteAllCollections();

        verify(repository).deleteAllCollections(rushCallbackArgumentCaptor.capture());

        //Show no collections screen
        verify(mockView).showNoCollections();
    }

    @Test
    public void clickOnCollection_ShowsDetailUi() {
        // Given a stubbed collection
        CardCollection collection = new CardCollection("Details Requested", "For this note", null);

        // When open collection details is requested
        presenter.openCollection(collection);

        // Then collection detail UI is shown
        verify(mockView).showCardCollectionDetailsUi(any(String.class));
    }

    @Test
    public void start() {
        presenter.loadCollections(false);
        verifyNoMoreInteractions(mockView);
    }

}