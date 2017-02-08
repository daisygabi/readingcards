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

package com.readingcards.addeditnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.readingcards.R;
import com.readingcards.data.domain.Note;
import com.readingcards.notes.MainNotesActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the add note screen. Users can enter a note title and description.
 */
public class AddEditNoteFragment extends Fragment implements AddEditNoteContract.View {

    public static final String ARGUMENT_EDIT_NOTE_ID = "EDIT_NOTE_ID";

    private AddEditNoteContract.Presenter presenter;

    private EditText titleTxt;
    private EditText contentTxt;
    private static String id;

    public static AddEditNoteFragment newInstance() {
        return new AddEditNoteFragment();
    }

    public AddEditNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) {
            presenter.start();
        }
    }

    @Override
    public void setPresenter(@NonNull AddEditNoteContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_note_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(titleTxt.getText().toString(), contentTxt.getText().toString(),
                        false);
                presenter.saveOrUpdateNote(note);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addnote_frag, container, false);
        titleTxt = (EditText) root.findViewById(R.id.add_note_title);
        contentTxt = (EditText) root.findViewById(R.id.add_note_description);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void showErrorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showNoteList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        titleTxt.setText(title);
    }

    @Override
    public void setDescription(String description) {
        contentTxt.setText(description);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNote(@NonNull String collectionId) {
        Intent intent = new Intent(getContext(), MainNotesActivity.class);
        ActivityCompat.startActivity(getActivity(), intent, null);
    }
}