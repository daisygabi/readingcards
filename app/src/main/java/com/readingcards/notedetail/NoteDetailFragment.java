/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.readingcards.notedetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.readingcards.R;
import com.readingcards.addeditnote.AddEditNoteActivity;
import com.readingcards.addeditnote.AddEditNoteFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the note detail screen.
 */
public class NoteDetailFragment extends Fragment implements NoteDetailContract.View {

    @NonNull
    public static final String ARGUMENT_NOTE_ID = "NOTE_ID";

    @NonNull
    private static final int REQUEST_EDIT_NOTE = 1;

    private Unbinder unbinder;
    private NoteDetailContract.Presenter presenter;

    @BindView(R.id.note_detail_title)
    TextView mDetailTitle;

    @BindView(R.id.note_detail_description)
    TextView mDetailDescription;

    @BindView(R.id.note_detail_complete)
    CheckBox mDetailCompleteStatus;

    public static NoteDetailFragment newInstance(@Nullable String noteId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_NOTE_ID, noteId);
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notedetail_frag, container, false);
        unbinder = ButterKnife.bind(this, root);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editNote();
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void setPresenter(@NonNull NoteDetailContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteNote();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notedetail_fragment_menu, menu);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mDetailTitle.setText("");
            mDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void hideDescription() {
        mDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void hideTitle() {
        mDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(@NonNull String description) {
        mDetailDescription.setVisibility(View.VISIBLE);
        mDetailDescription.setText(description);
    }

    @Override
    public void showEditNote(@NonNull String noteId) {
        Intent intent = new Intent(getContext(), AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID, noteId);
        ActivityCompat.startActivityForResult(getActivity(), intent, REQUEST_EDIT_NOTE, null);
    }

    public void showNoteMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.note_marked_complete), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_NOTE) {
            // If the note was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void showTitle(@NonNull String title) {
        mDetailTitle.setVisibility(View.VISIBLE);
        mDetailTitle.setText(title);
    }

    @Override
    public void showMissingNote() {
        mDetailTitle.setText("");
        mDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void successfullyDeletedNote() {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}