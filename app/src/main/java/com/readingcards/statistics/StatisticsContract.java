package com.readingcards.statistics;

import com.readingcards.BasePresenter;

/**
 * Created by gabrielaradu on 10/02/2017.
 */

public class StatisticsContract {

    interface Presenter extends BasePresenter {
        int getAllCardCollectionsSize();
        int getAllNotesSize();
    }
}
