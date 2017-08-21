package com.example.hahaha.musicplayer.model.entity.internal;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Song extends DataSupport
    implements Parcelable {
  private long id;
  private String name;
  private String artist;
  private String album;
  private String doubanId;
  private String imgUrl;

  @Column(unique = true, nullable = false)
  private String filePath;

  public Song(String name, String artist, String album, Uri uri) {
    this.name = name;
    this.artist = artist;
    this.album = album;
    this.filePath = uri.toString();
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getArtist() {
    return artist;
  }

  public String getAlbum() {
    return album;
  }

  public String getFilePath() {
    return filePath;
  }

  public Uri getUri() {
    return Uri.parse(filePath);
  }

  public String getDoubanId() {
    return doubanId;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setDoubanId(String doubanId) {
    this.doubanId = doubanId;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  @Override public String toString() {
    return "Song{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", artist='" + artist + '\'' +
        ", album='" + album + '\'' +
        ", filePath='" + filePath + '\'' +
        '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.artist);
    dest.writeString(this.filePath);
  }

  public Song() {
  }

  public Song(Parcel in) {
    this.name = in.readString();
    this.artist = in.readString();
    this.filePath = in.readString();
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
