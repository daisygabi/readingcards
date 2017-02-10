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

package com.readingcards.notes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
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
import com.readingcards.ReadingCardsSplashScreen;
import com.readingcards.addeditnote.AddEditNoteActivity;
import com.readingcards.collections.CardCollectionActivity;
import com.readingcards.statistics.StatisticsPresenter;
import com.readingcards.util.ActivityUtils;
import com.readingcards.util.EspressoIdlingResource;
import com.stephentuso.welcome.WelcomeHelper;

import java.util.Arrays;

public class MainNotesActivity extends AppCompatActivity {

    private WelcomeHelper welcomeScreen;
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
    private DrawerLayout mDrawerLayout;
    private NotesPresenter notesPresenter;
    private StatisticsPresenter statsPresenter;

    @TargetApi(Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_act);

        createDynamicShortcuts();

        // Add Splash screen
        welcomeScreen = new WelcomeHelper(this, ReadingCardsSplashScreen.class);
        welcomeScreen.show(savedInstanceState);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getString(R.string.app_name));

        NotesFragment notesFragment =
                (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (notesFragment == null) {
            // Create the fragment
            notesFragment = NotesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), notesFragment, R.id.contentFrame);
        }

        // Create the presenter
        notesPresenter = new NotesPresenter(
                Injection.provideNotesRepository(getApplicationContext()), notesFragment);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.notes_nav_view);
        if (navigationView != null) {
            statsPresenter = new StatisticsPresenter(Injection.provideStatisticsRepository(getApplicationContext()));
            ActivityUtils.setAppStatistics(navigationView, statsPresenter);
            setupDrawerContent(navigationView);
        }
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void createDynamicShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

            ShortcutInfo cardCollectionShortcut = new ShortcutInfo.Builder(this, "Collections")
                    .setShortLabel("Collections")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_list))
                    .setIntents(
                            new Intent[]{
                                    new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, CardCollectionActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            })
                    .build();

            ShortcutInfo notesShortcut = new ShortcutInfo.Builder(this, "Add note")
                    .setShortLabel("Add Note")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_step1))
                    .setIntents(
                            new Intent[]{
                                    new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, AddEditNoteActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            })
                    .build();

            shortcutManager.setDynamicShortcuts(Arrays.asList(cardCollectionShortcut, notesShortcut));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, notesPresenter.getFiltering());
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
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
                                ActivityCompat.startActivity(MainNotesActivity.this,
                                        new Intent(MainNotesActivity.this, MainNotesActivity.class), null);
                                break;
                            case R.id.collection_cards_navigation_menu_item:
                                ActivityCompat.startActivity(MainNotesActivity.this,
                                        new Intent(MainNotesActivity.this, CardCollectionActivity.class), null);
                                break;
                            default:
                                ActivityCompat.startActivity(MainNotesActivity.this,
                                        new Intent(MainNotesActivity.this, MainNotesActivity.class), null);
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
