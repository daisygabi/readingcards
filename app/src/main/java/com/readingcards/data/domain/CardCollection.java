package com.readingcards.data.domain;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushList;

/**
 * POJO class for a CardCollection of notes
 */
public class CardCollection extends RushObject implements Parcelable {

    @Nullable
    private String title;

    @Nullable
    private String description;

    @Nullable
    /* Lists must have @RushList annotation with classType,
            listType can also be added the default is ArrayList */
    @RushList(classType = Note.class)
    private List<Note> notes;

    public CardCollection() {}

    public CardCollection(@NonNull String title, @NonNull String description,
                          @Nullable List<Note> notes) {
        this.title = title;
        this.description = description;
        this.notes = notes;
    }

    protected CardCollection(Parcel in) {
        notes = new ArrayList<Note>(); //non-null reference is required
        in.readTypedList(notes, Note.CREATOR);
        title = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getId());
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeTypedList(notes);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public CardCollection createFromParcel(Parcel in) {

            return new CardCollection(in);
        }

        @Override
        public CardCollection[] newArray(int size) {
            return new CardCollection[size];
        }
    };


    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public List<Note> getNotes() {
        return notes;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCollection cardCollection = (CardCollection) o;
        return Objects.equal(title, cardCollection.title) &&
                Objects.equal(description, cardCollection.description) &&
                Objects.equal(notes, cardCollection.notes);
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getId() {
        return RushCore.getInstance().getId(this);
    }

    public void setNotes(@Nullable List<Note> notes) {
        this.notes = notes;
    }
}
