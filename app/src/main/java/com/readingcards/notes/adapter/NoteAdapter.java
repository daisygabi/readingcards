package com.readingcards.notes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.readingcards.R;
import com.readingcards.data.domain.Note;
import com.readingcards.notes.NotesFragment;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.readingcards.R.id.complete;

/**
 *
 */
public class NoteAdapter extends BaseAdapter {

    private List<Note> mNotes;
    private NotesFragment.NoteItemListener mItemListener;

    public NoteAdapter(List<Note> notes, NotesFragment.NoteItemListener itemListener) {
        setList(notes);
        mItemListener = itemListener;
    }

    public void replaceData(List<Note> notes) {
        setList(notes);
        notifyDataSetChanged();
    }

    private void setList(List<Note> notes) {
        mNotes = checkNotNull(notes);
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Note getItem(int i) {
        return mNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.note_item, viewGroup, false);
        }

        final Note note = getItem(i);
        if (note != null) {
            final TextView titleTxt = (TextView) rowView.findViewById(R.id.title);
            titleTxt.setText(note.getTitleForList());

            TextView contentTxt = (TextView) rowView.findViewById(R.id.content);
            contentTxt.setText(note.getDescription());

            CheckBox completeCheckbox = (CheckBox) rowView.findViewById(complete);

            completeCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!note.isCompleted()) {
                        mItemListener.onCompleteNoteClick(note);
                    }
                }
            });
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onNoteClick(note);
                }
            });
        }
        return rowView;
    }
}