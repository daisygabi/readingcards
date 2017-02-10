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

package com.readingcards.statistics;

import android.support.annotation.NonNull;

import com.readingcards.data.repository.StatisticsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 */
public class StatisticsPresenter implements StatisticsContract.Presenter {

    @NonNull
    private StatisticsRepository repository;

    public StatisticsPresenter(StatisticsRepository repository) {
        this.repository = checkNotNull(repository, "repository cannot be null");
    }
    @Override
    public int getAllCardCollectionsSize() {
        return repository.getCardCollectionsSize();
    }

    @Override
    public int getAllNotesSize() {
        return repository.getNotesSize();
    }

    @Override
    public void start() {
    }
}