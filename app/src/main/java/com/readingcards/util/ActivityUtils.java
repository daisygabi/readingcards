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

package com.readingcards.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.readingcards.R;
import com.readingcards.statistics.StatisticsPresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static AlertDialog.Builder showCalltoActionDialog(final Activity context, String title, String message,
                                                             String negativeMessage) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negativeMessage, null);
        builder.setCancelable(false);

        return builder;
    }

    public static void setAppStatistics(NavigationView navigationView, final StatisticsPresenter statsPresenter) {
        View header = navigationView.getHeaderView(0);
        TextView notesStatsTextView = (TextView) header.findViewById(R.id.notes_statistics_nr_view);
        TextView collectionsStatsTextView = (TextView) header.findViewById(R.id.collections_statistics_nr_view);

        int notesSize = statsPresenter.getAllNotesSize();
        notesStatsTextView.setText(String.valueOf(notesSize));

        int collectionsSize = statsPresenter.getAllCardCollectionsSize();
        collectionsStatsTextView.setText(String.valueOf(collectionsSize));
    }
}