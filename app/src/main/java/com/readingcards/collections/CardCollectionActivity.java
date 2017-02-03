package com.readingcards.collections;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.readingcards.Injection;
import com.readingcards.R;
import com.readingcards.notes.MainNotesActivity;
import com.readingcards.util.ActivityUtils;
import com.readingcards.util.EspressoIdlingResource;

/**
 * Created by gabrielaradu on 08/01/2017.
 */

public class CardCollectionActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private CollectionPresenter collectionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collections_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.collection_cards_title);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Create the fragment
        CardCollectionFragment collectionFragment = (CardCollectionFragment) getSupportFragmentManager().findFragmentById(R.id.collection_content_frame);
        if(collectionFragment == null) {
            collectionFragment = new CardCollectionFragment().newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), collectionFragment, R.id.collection_content_frame);
        }

        // Create the presenter
        collectionPresenter = new CollectionPresenter(Injection.provideCollectionRepository(getApplicationContext()),
                collectionFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.notes_navigation_menu_item:
                                ActivityCompat.startActivity(CardCollectionActivity.this,
                                        new Intent(CardCollectionActivity.this, MainNotesActivity.class), null);
                                break;
                            case R.id.collection_cards_navigation_menu_item:
                                ActivityCompat.startActivity(CardCollectionActivity.this,
                                        new Intent(CardCollectionActivity.this, CardCollectionActivity.class), null);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
