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

package com.readingcards.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "ReadingNotes.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String LIST_STRING_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_NOTE_ENTRIES =
            "CREATE TABLE " + PersistenceContract.NoteEntry.TABLE_NAME + " (" +
                    PersistenceContract.NoteEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.NoteEntry.COLUMN_NAME_NOTE_ID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.NoteEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.NoteEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.NoteEntry.COLUMN_NAME_COMPLETED + BOOLEAN_TYPE +
                    " )";

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {

        String collectionQuery = "DROP TABLE IF EXISTS " + PersistenceContract.NoteEntry.TABLE_NAME;
        database.execSQL(collectionQuery);
        onCreate(database);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}