package com.readingcards.collections.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.readingcards.R;
import com.readingcards.cards.ReadNotesAsCardsActivity;
import com.readingcards.collections.CardCollectionFragment;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.util.ActivityUtils;
import com.readingcards.util.CardCollectionDoesNotExistsException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * CardCollection adapter that shows cards buckled up in groups Created by gabrielaradu on
 * 08/01/2017.
 */
public class CollectionAdapter extends BaseAdapter {

    private List<CardCollection> collections;
    private CardCollectionFragment.CollectionItemListener itemListener;

    private AppCompatImageView viewCollectionImg, deleteCollectionImg;
    private Activity context;

    public CollectionAdapter(List<CardCollection> collections,
                             CardCollectionFragment.CollectionItemListener itemListener, Activity context) {
        this.collections = collections;
        this.itemListener = itemListener;
        this.context = context;
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public CardCollection getItem(int i) {
        return collections.get(i);
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
            rowView = inflater.inflate(R.layout.cardcollection_item, viewGroup, false);
        }

        final CardCollection cardCollection = getItem(i);
        if (cardCollection != null) {
            TextView titleTxt = (TextView) rowView.findViewById(R.id.title_view);
            titleTxt.setText(cardCollection.getTitle());

            TextView descriptionTxt = (TextView) rowView.findViewById(R.id.description_view);
            descriptionTxt.setText(cardCollection.getDescription());

            TextView notesCountTxt = (TextView) rowView.findViewById(R.id.notes_count_view);
            notesCountTxt.setText((cardCollection.getNotes() != null ? cardCollection.getNotes().size() : "0")
            + context.getResources().getString(R.string.notes_label));

            TextView collectionDateTxt = (TextView) rowView.findViewById(R.id.collection_added_date_view);
            setDate(collectionDateTxt);

            MaterialLetterIcon iconImg = (MaterialLetterIcon) rowView.findViewById(R.id.avatar);
            iconImg.setLetter(titleTxt.getText() != null ? titleTxt.getText().toString().substring(0, 1) : "N");
            iconImg.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            iconImg.setShapeColor(context.getResources().getColor(R.color.colorPrimary));
            iconImg.setRoundRectRx(8);
            iconImg.setRoundRectRy(8);

            viewCollectionImg = (AppCompatImageView) rowView.findViewById(R.id.collection_view);
            viewCollectionImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Start reading as cards activity
                    Intent intent = new Intent(context, ReadNotesAsCardsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle extras = new Bundle();
                    extras.putParcelable("cardCollection", cardCollection);
                    extras.putParcelableArrayList("notes", (ArrayList<Note>)cardCollection.getNotes());
                    intent.putExtras(extras);
                    ActivityCompat.startActivity(context, intent, null);
                }
            });

            deleteCollectionImg = (AppCompatImageView) rowView.findViewById(R.id.collection_delete_view);
            deleteCollectionImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Show dialog to be sure user wants to delete collection
                    AlertDialog.Builder builder =
                            ActivityUtils.showCalltoActionDialog(context, "Info",
                                    "You are about to delete the collection (but not the individual notes). Are you sure?", "Cancel");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                itemListener.onDelete(cardCollection);
                            } catch (CardCollectionDoesNotExistsException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onClick(cardCollection);
                }
            });
        }
        return rowView;
    }

    private void setDate(TextView collectionDateTxt) {
        checkNotNull(collectionDateTxt);
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        String formattedDate = dateFormat.format(now);

        checkNotNull(formattedDate);
        collectionDateTxt.setText(formattedDate);
    }

    public List<CardCollection> getCollections() {
        return collections;
    }

    public void setCollections(List<CardCollection> collections) {
        this.collections = collections;
    }

    public void replaceData(List<CardCollection> collections) {
        this.collections = collections;
        if (!collections.isEmpty()) {
            notifyDataSetChanged();
        }
    }
}