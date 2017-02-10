package com.readingcards.data.source;


import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;

import co.uk.rushorm.core.RushSearch;

/**
 * Main entry point for accessing collection data.
 */
public class StatisticsDataSourceImplementation implements StatisticsDataSource {

    private static StatisticsDataSourceImplementation INSTANCE;

    public static StatisticsDataSourceImplementation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatisticsDataSourceImplementation();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private StatisticsDataSourceImplementation() {
    }

    @Override
    public int getCardCollectionsSize() {
        return new RushSearch().find(CardCollection.class).size();
    }

    @Override
    public int getNotesSize() {
        return new RushSearch().find(Note.class).size();
    }
}