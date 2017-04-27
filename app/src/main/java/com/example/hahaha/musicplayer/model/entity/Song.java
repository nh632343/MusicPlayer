package com.example.hahaha.musicplayer.model.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    public long id;
    public String name;
    public String artist;
    public Uri uri;

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.artist);
        dest.writeParcelable(this.uri, flags);
    }

    public Song() {
    }

    protected Song(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.artist = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
