package com.readingcards.addcardcollection.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.readingcards.R;
import com.readingcards.addcardcollection.AddCardCollectionFragment;
import com.readingcards.data.domain.Note;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by gabrielaradu on 13/01/2017.
 */

public class CollectionNotesAdapter extends BaseAdapter {

    private List<Note> notes = new ArrayList<Note>();
    private AddCardCollectionFragment.NoteItemListener itemListener;

    public CollectionNotesAdapter(List<Note> notes, AddCardCollectionFragment.NoteItemListener itemListener) {
        checkNotNull(notes);
        this.notes = notes;
        this.itemListener = itemListener;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<Note> getItems() {
        return notes;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.note_stripped_item, viewGroup, false);
        }

        final Note note = getItem(i);
        if (note != null) {
            final TextView titleTxt = (TextView) rowView.findViewById(R.id.title_view);
            titleTxt.setText(note.getTitleForList());

            final CheckBox rowCheckbox = (CheckBox) rowView.findViewById(R.id.checkbox_view);
            rowCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListener != null) {
                        itemListener.onNoteClick(note);
                    }
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!rowCheckbox.isChecked()) {
                        rowCheckbox.setChecked(true);

                        if(itemListener != null) {
                            itemListener.onNoteClick(note);
                        }
                    } else {
                        rowCheckbox.setChecked(false);

                        if(itemListener != null) {
                            itemListener.onUnClick(note);
                        }
                    }
                }
            });
        }
        return rowView;
    }
}