/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.readingcards.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
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
import com.readingcards.addeditnote.AddEditNoteActivity;
import com.readingcards.data.domain.Note;
import com.readingcards.notedetail.NoteDetailActivity;
import com.readingcards.notes.adapter.NoteAdapter;
import com.readingcards.util.ActivityUtils;
import com.readingcards.util.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Note}s. User can choose to view all or completed notes.
 */
public class NotesFragment extends Fragment implements NotesContract.View {

    private NotesContract.Presenter presenter;
    private NoteAdapter adapter;
    private Unbinder unbinder;

    @BindView(R.id.no_notes_layout)
    LinearLayout noNotesLayout;

    @BindView(R.id.all_notes_layout)
    LinearLayout allNotesLayout;

    @BindView(R.id.no_data_view)
    TextView noDataView;

    @BindView(R.id.empty_list_view)
    TextView emptyListViewTxt;

    @BindView(R.id.filtering_label_view)
    TextView filteringLabelView;

    @BindView(R.id.notes_list)
    ListView listView;

    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    private SharedPrefsUtils sharedPrefsUtils;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NoteAdapter(new ArrayList<Note>(0), mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notes_frag, container, false);
        unbinder = ButterKnife.bind(this, root);

        // Set up notes view
        if (listView != null) {
            listView.setAdapter(adapter);
        }
        emptyListViewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNote();
            }
        });

        // Create dummy data at first run of the app to make UI look more appealing
        sharedPrefsUtils = new SharedPrefsUtils(getActivity().getApplicationContext());
         if(sharedPrefsUtils.getBooleanValue(getActivity().getString(R.string.preference_first_run))) {
            saveDummyNotes();
        }

        // Set up floating action button
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_note);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNewNote();
            }
        });

        // Set up progress indicator
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadNotes(false);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    private void saveDummyNotes() {
        presenter.createDummyNotesAtFirstRun();
        sharedPrefsUtils.updateBooleanValue(getActivity().getString(R.string.preference_first_run), false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_delete_all:
                // Show dialog to be sure user wants to delete all collections
                AlertDialog.Builder builder =
                        ActivityUtils.showCalltoActionDialog(getActivity(), getActivity().getResources().getString(R.string.info_label),
                                getActivity().getResources().getString(R.string.delete_all_notes_confirmation),
                                getActivity().getResources().getString(android.R.string.cancel));
                builder.setPositiveButton(getActivity().getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.deleteAllNotes();
                        showNoNotes();
                    }
                });
                builder.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_notes, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.completed:
                        presenter.showFiltered(NotesFilterType.COMPLETED);
                        break;
                    default:
                        presenter.showFiltered(NotesFilterType.ALL);
                        break;
                }
                return true;
            }
        });
        popup.show();
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
    public void showNotes(List<Note> notes) {
        adapter.replaceData(notes);
        allNotesLayout.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        noNotesLayout.setVisibility(View.GONE);
    }

    @Override
    public void showCompletedNotes(List<Note> notes) {
        if(!notes.isEmpty()) {
            adapter.replaceData(notes);
        } else {
            allNotesLayout.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            noNotesLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoNotes() {
        noNotesLayout.setVisibility(View.VISIBLE);
        emptyListViewTxt.setVisibility(View.VISIBLE);
        allNotesLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showCompletedFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_completed));
        filteringLabelView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAllFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_all));
        filteringLabelView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddNote() {
        Intent intent = new Intent(getContext(), AddEditNoteActivity.class);
        ActivityCompat.startActivityForResult(getActivity(), intent, AddEditNoteActivity.REQUEST_ADD_NOTE, null);
    }

    @Override
    public void showNoteDetailsUi(String noteId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId);
        ActivityCompat.startActivity(getContext(), intent, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) {
            presenter.start();
        }
        setLoadingIndicator(false);
        if (adapter != null) {
            presenter.loadNotes(true);
        }
    }

    @Override
    public void setPresenter(@NonNull NotesContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public interface NoteItemListener {

        void onNoteClick(Note clickedNote);

        void onCompleteNoteClick(Note completedNote);
    }

    /**
     * Listener for clicks on notes in the ListView.
     */
    NoteItemListener mItemListener = new NoteItemListener() {
        @Override
        public void onNoteClick(Note clickedNote) {
            presenter.openNoteDetails(clickedNote);
        }

        @Override
        public void onCompleteNoteClick(Note completedNote) {
            completedNote.setNoteCompleted(true);
            presenter.completeNote(completedNote);
        }
    };
}