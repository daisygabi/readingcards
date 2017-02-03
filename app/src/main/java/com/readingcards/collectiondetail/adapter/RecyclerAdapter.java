package com.readingcards.collectiondetail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.readingcards.R;
import com.readingcards.collectiondetail.CollectionDetailFragment;
import com.readingcards.data.domain.Note;

import java.util.List;

/**
 * Created by gabrielaradu on 16/01/2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Note> noteList;
    private CollectionDetailFragment.RecyclerNoteItem itemListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox checkbox;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_view);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox_view);
        }
    }

    public RecyclerAdapter(List<Note> noteList, CollectionDetailFragment.RecyclerNoteItem itemListener) {
        this.noteList = noteList;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_stripped_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Note note = (Note) noteList.get(position);
        holder.title.setText(note.getTitle());

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkbox.setChecked(true);
                    note.setSelected(true);
                    itemListener.onClick(note);
                } else {
                    holder.checkbox.setChecked(false);
                    note.setSelected(false);
                    itemListener.onUnclick(note);
                }
            }
        });

        // Preselect items
        holder.checkbox.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public List<Note> getItems() {
        return noteList;
    }
}