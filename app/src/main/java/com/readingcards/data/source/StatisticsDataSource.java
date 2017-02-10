package com.readingcards.data.source;


/**
 * Main entry point for accessing collection and notes data.
 */
public interface StatisticsDataSource {

    int getCardCollectionsSize();

    int getNotesSize();

}