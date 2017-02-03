package com.readingcards.cards.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.readingcards.R;

import java.util.List;

/**
 * Created by gabrielaradu on 29/08/2016.
 */
public class SwipeCardsAdapter extends BaseAdapter {

    private List<String> mData;
    private Activity activity;

    public SwipeCardsAdapter(List<String> data, Activity activity) {
        this.mData = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.note_as_card, parent, false);
        }

        TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
        textViewCard.setText(mData.get(position));

        return convertView;
    }
}