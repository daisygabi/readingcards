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

package com.readingcards.data.domain;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushObject;

/**
 * Immutable model class for a Note.
 */
public class Note extends RushObject implements Parcelable {

//    @NonNull
//    private String noteId;

    @Nullable
    private String title;

    @Nullable
    private String description;

    @Nullable
    private boolean completed;

    @Nullable
    private boolean selected;

    public Note() {
    }

    /**
     * Use this constructor to specify a completed Note if the Note already has an id (copy of
     * another Note).
     *
     * @param title       title of the note
     * @param description description of the note
     * @param completed   true if the note is completed, false if it's active
     */
    public Note(@Nullable String title, @Nullable String description,
                @Nullable boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    protected Note(Parcel in) {
//        noteId = in.readString();
        title = in.readString();
        description = in.readString();
        completed = in.readByte() != 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(title)) {
            return title;
        } else {
            return description;
        }
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isActive() {
        return !completed;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(description);
    }

    public void setNoteCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return
                Objects.equal(title, note.title) &&
                Objects.equal(description, note.description);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hashCode(noteId, title, description);
//    }

    public void setCompleted(@Nullable boolean completed) {
        this.completed = completed;
    }

    @Override
    public String getId() {
        return RushCore.getInstance().getId(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(RushCore.getInstance().getId(this));
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeByte((byte) (completed ? 1 : 0));
    }
}