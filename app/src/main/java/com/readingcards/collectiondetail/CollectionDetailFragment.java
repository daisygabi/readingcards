package com.readingcards.collectiondetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.readingcards.R;
import com.readingcards.collectiondetail.adapter.RecyclerAdapter;
import com.readingcards.collections.CardCollectionActivity;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by gabrielaradu on 15/12/2016.
 */
public class CollectionDetailFragment extends Fragment implements CollectionDetailContract.View {

    @NonNull
    public static final String ARGUMENT_EDIT_COLLECTION_ID = "EDIT_COLLECTION_ID";
    @NonNull
    private static final int REQUEST_EDIT_COLLECTION = 1;

    @BindView(R.id.collection_title_view)
    EditText titleTxt;

    @BindView(R.id.collection_description_view)
    EditText descriptionTxt;

    @BindView(R.id.notes_layout)
    LinearLayout notesLayout;

    private RecyclerAdapter adapter;
    private Unbinder unbinder;
    private FloatingActionButton fab;
    private ArrayList<Note> editedNoteList;

    @NonNull
    private CollectionDetailContract.Presenter presenter;

    @BindView(R.id.notes_recycler_view)
    RecyclerView recyclerView;

    public static CollectionDetailFragment newInstance(@Nullable String collectionId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_EDIT_COLLECTION_ID, collectionId);
        CollectionDetailFragment fragment = new CollectionDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.collectiondetail_frag, container, false);
        unbinder = ButterKnife.bind(this, root);

        editedNoteList = new ArrayList<Note>();

        // Set up floating action button
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit__detail_collection);
        fab.setImageResource(R.drawable.ic_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fab.setImageResource(R.mipmap.ic_save);
                final String collectionId = getArguments().getString(ARGUMENT_EDIT_COLLECTION_ID);

                CardCollection newCollection = new CardCollection(
                        titleTxt.getText().toString().trim(),
                        descriptionTxt.getText().toString().trim(),
                        editedNoteList
                );

                if (presenter != null && collectionId != null) {
                    presenter.updateCollection(collectionId, newCollection);
                    fab.setImageResource(R.mipmap.ic_add_collection);

                }
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.start();
        }
    }

    /**
     * Called when the fragment is no longer in use.  This is called after {@link #onStop()} and
     * before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMissingCollection() {

    }

    @Override
    public void hideTitle() {
        titleTxt.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        titleTxt.setVisibility(View.VISIBLE);
        titleTxt.setText(title);
    }

    @Override
    public void hideDescription() {
        descriptionTxt.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        descriptionTxt.setVisibility(View.VISIBLE);
        descriptionTxt.setText(description);
    }

    @Override
    public void showEditCollection(@NonNull String collectionId) {
        Intent intent = new Intent(getContext(), CardCollectionActivity.class);
        ActivityCompat.startActivity(getActivity(), intent, null);
    }

    @Override
    public void showNotes(List<Note> notes) {
        adapter = new RecyclerAdapter(notes, itemListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (adapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideNotes() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(titleTxt, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(CollectionDetailContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_COLLECTION) {
            // If the note was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

    public interface RecyclerNoteItem {
        void onClick(Note note);
        void onUnclick(Note note);
    }

    RecyclerNoteItem itemListener = new RecyclerNoteItem() {
        @Override
        public void onClick(Note note) {
            checkNotNull(note);
            editedNoteList.add(note);
        }

        @Override
        public void onUnclick(Note note) {
            checkNotNull(note);
            editedNoteList.remove(note);
        }
    };
}