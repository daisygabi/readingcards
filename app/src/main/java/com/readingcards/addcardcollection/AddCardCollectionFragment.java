package com.readingcards.addcardcollection;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.readingcards.R;
import com.readingcards.addcardcollection.adapter.CollectionNotesAdapter;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by gabrielaradu on 09/01/2017.
 */
public class AddCardCollectionFragment extends Fragment implements AddCardCollectionContract.View {

    public static final String ARGUMENT_EDIT_COLLECTION_ID = "EDIT_COLLECTION_ID";
    private CollectionNotesAdapter adapter;
    private List<Note> notes;
    private List<Note> notesToAppendToCollecton;

    @BindView(R.id.collection_title_view)
    EditText titleTxt;

    @BindView(R.id.collection_description_view)
    EditText descriptionTxt;

    @BindView(R.id.notes_list)
    ListView notesListView;

    @BindView(R.id.empty_notes_list_view)
    TextView emptyNotesListView;

    private Unbinder unbinder;

    @NonNull
    public AddCardCollectionContract.Presenter presenter;

    public AddCardCollectionFragment() {}

    public static AddCardCollectionFragment newInstance() {
        return new AddCardCollectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addcollection_frag, container, false);
        unbinder = ButterKnife.bind(this, root);

        notesToAppendToCollecton = new ArrayList<Note>();
        notes = presenter.loadNoteListToAppendToCollection();
        if (notes != null) {
            adapter = new CollectionNotesAdapter(notes, itemListener);
        }
        if (notesListView != null) {
            notesListView.setAdapter(adapter);
        }

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_collection);
        fab.setImageResource(R.mipmap.ic_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CardCollection collection = new CardCollection(titleTxt.getText().toString(),
                        descriptionTxt.getText().toString(), notesToAppendToCollecton);
                presenter.saveOrUpdateCollection(collection);
            }
        });
        setHasOptionsMenu(true);
        addListerners();
        return root;
    }

    @Override
    public void showEmptyCollection() {
        Snackbar.make(titleTxt, "empty collection", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyNoteList() {
        notesListView.setEmptyView(emptyNotesListView);
        Snackbar.make(titleTxt, "empty notes", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyCollectionError() {
        Snackbar.make(titleTxt, "empty collection", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(titleTxt, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showCollectionList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showNotesToAppendList(List<Note> notesToAppend) {
        checkNotNull(notesToAppend);
        adapter = new CollectionNotesAdapter(notesToAppend, itemListener);

        checkNotNull(notesListView);
        notesListView.setAdapter(adapter);
        notesListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(String title) {
        titleTxt.setText(title);
    }

    @Override
    public void setDescription(String description) {
        descriptionTxt.setText(description);
    }

    @Override
    public void setPresenter(AddCardCollectionContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void addListerners() {
        titleTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        descriptionTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.  You should place your
     * menu items in to <var>menu</var>.  For this method to be called, you must have first called
     * {@link #setHasOptionsMenu}.  See {@link Activity#onCreateOptionsMenu(Menu)
     * Activity.onCreateOptionsMenu} for more information.
     *
     * @param menu The options menu in which you place your items.
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface NoteItemListener {

        void onNoteClick(Note clickedNote);

        void onUnClick(Note note);
    }

    /**
     * Listener for clicks on notes in the ListView.
     */
    NoteItemListener itemListener = new NoteItemListener() {

        @Override
        public void onNoteClick(Note note) {
            notesToAppendToCollecton.add(note);
        }

        @Override
        public void onUnClick(Note note) {
            if (notesToAppendToCollecton.contains(note)) {
                notesToAppendToCollecton.remove(note);
            }
        }
    };
}