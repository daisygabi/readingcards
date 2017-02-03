package com.readingcards.collectiondetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.readingcards.Injection;
import com.readingcards.R;
import com.readingcards.util.ActivityUtils;

/**
 * Created by gabrielaradu on 14/01/2017.
 */

public class CollectionDetailActivity extends AppCompatActivity {

    public static final String EXTRA_COLLECTION_ID = "COLLECTION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collectiondetail_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle(R.string.edit_collection);

        // Get the requested collection id
        String collectionId = getIntent().getStringExtra(EXTRA_COLLECTION_ID);

        CollectionDetailFragment collectionDetailFragment = (CollectionDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (collectionDetailFragment == null) {
            collectionDetailFragment = CollectionDetailFragment.newInstance(collectionId);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    collectionDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new CollectionDetailPresenter(collectionId,
                Injection.provideCollectionRepository(getApplicationContext()), collectionDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
