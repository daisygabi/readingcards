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

package com.readingcards.data.repository;

import android.support.annotation.NonNull;

import com.readingcards.data.source.StatisticsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load stats from the data sources
 */
public class StatisticsRepository implements StatisticsDataSource {

    private static StatisticsRepository INSTANCE = null;
    private final StatisticsDataSource dataSource;

    // Prevent direct instantiation.
    private StatisticsRepository(@NonNull StatisticsDataSource dataSource) {
        this.dataSource = checkNotNull(dataSource);
    }

    public static StatisticsRepository getInstance(StatisticsDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new StatisticsRepository(dataSource);
        }
        return INSTANCE;
    }

    @Override
    public int getCardCollectionsSize() {
        return dataSource.getCardCollectionsSize();
    }

    @Override
    public int getNotesSize() {
        return dataSource.getNotesSize();
    }
}