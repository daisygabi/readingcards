package com.readingcards.collections;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.readingcards.R;
import com.readingcards.addcardcollection.AddCardCollectionActivity;
import com.readingcards.collectiondetail.CollectionDetailActivity;
import com.readingcards.collections.adapter.CollectionAdapter;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.notes.ScrollChildSwipeRefreshLayout;
import com.readingcards.util.ActivityUtils;
import com.readingcards.util.CardCollectionDoesNotExistsException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.readingcards.R.id.collection_layout;

/**
 * Created by gabrielaradu on 08/01/2017.
 */
public class CardCollectionFragment extends Fragment implements CollectionContract.View {

    private CollectionContract.Presenter presenter;
    private CollectionAdapter adapter;
    private Unbinder unbinder;

    @BindView(R.id.no_collections_layout)
    LinearLayout noCollectionsLayout;

    @BindView(collection_layout)
    LinearLayout collectionsLayout;

    @BindView(R.id.collections_list)
    ListView collectionListView;

    @BindView(R.id.no_collections_main_text)
    TextView noCollectionsMainText;

    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout swipeRefreshLayout;

    public static CardCollectionFragment newInstance() {
        return new CardCollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CollectionAdapter(new ArrayList<CardCollection>(0), itemListener, getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.collection_frag, container, false);
        unbinder = ButterKnife.bind(this, root);

        collectionsLayout.setVisibility(View.VISIBLE);
        if (collectionListView != null && adapter != null) {
            collectionListView.setAdapter(adapter);
        }

        // Set up progress indicator
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(collectionListView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadCollections(false);
            }
        });

        // Set up floating action button
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_collection);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.addNewCollection();
                }
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showCollections(List<CardCollection> collections) {
        adapter.replaceData(collections);

        collectionListView.setAdapter(adapter);
        noCollectionsLayout.setVisibility(View.GONE);
        collectionsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddCollection() {
        Intent intent = new Intent(getContext(), AddCardCollectionActivity.class);
        ActivityCompat.startActivityForResult(getActivity(), intent, AddCardCollectionActivity.REQUEST_ADD_COLLECTION, null);
    }

    @Override
    public void showNoCollections() {
        noCollectionsLayout.setVisibility(View.VISIBLE);
        collectionsLayout.setVisibility(View.GONE);
    }

    @Override
    public void showSuccessfullySavedMessage(String message) {
        noCollectionsLayout.setVisibility(View.GONE);
        collectionsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCardCollectionDetailsUi(String collectionId) {
        Intent intent = new Intent(getContext(), CollectionDetailActivity.class);
        intent.putExtra(CollectionDetailActivity.EXTRA_COLLECTION_ID, collectionId);
        ActivityCompat.startActivity(getContext(), intent, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.start();
        }
        setLoadingIndicator(false);
        if (adapter != null && presenter != null) {
            presenter.loadCollections(true);
        }
    }

    @Override
    public void setPresenter(CollectionContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public interface CollectionItemListener {

        void onClick(CardCollection clickedCollection);

        void onDelete(CardCollection collection) throws CardCollectionDoesNotExistsException;
    }

    /**
     * Listener for clicks on collection in the ListView.
     */
    CollectionItemListener itemListener = new CardCollectionFragment.CollectionItemListener() {
        @Override
        public void onClick(CardCollection clickedCollection) {
            if (presenter != null) {
                presenter.openCollection(clickedCollection);
            }
        }

        @Override
        public void onDelete(CardCollection collection) throws CardCollectionDoesNotExistsException {
            checkNotNull(collection);
            presenter.deleteCollection(collection);
            presenter.loadCollections(true);
        }
    };

    /**
     * This hook is called whenever an item in your options menu is selected. The default
     * implementation simply returns false to have the normal processing happen (calling the item's
     * Runnable or sending a message to its Handler as appropriate).  You can use this method for
     * any items for which you would like to do processing without those other facilities.
     *
     * <p>Derived classes should call through to the base class for it to perform the default menu
     * handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it
     * here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                // Show dialog to be sure user wants to delete all collections
                AlertDialog.Builder builder =
                        ActivityUtils.showCalltoActionDialog(getActivity(), getActivity().getResources().getString(R.string.info_label),
                                getActivity().getResources().getString(R.string.delete_all_collections_confirmation),
                                getActivity().getResources().getString(android.R.string.cancel));
                builder.setPositiveButton(getActivity().getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.deleteAllCollections();
                        showNoCollections();
                    }
                });
                builder.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.  You should place your
     * menu items in to <var>menu</var>.  For this method to be called, you must have first called
     * {@link #setHasOptionsMenu}. Activity.onCreateOptionsMenu} for more information.
     *
     * @param menu The options menu in which you place your items.
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collections_fragment_menu, menu);
    }
}