package com.example.hahaha.musicplayer.model.entity.internal;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayStateInfo implements Parcelable {
  private Song song;
  private boolean isPlaying;
  private int playOrder;

  public PlayStateInfo(Song song, boolean isPlaying, int playOrder) {
    this.song = song;
    this.isPlaying = isPlaying;
    this.playOrder = playOrder;
  }

  public boolean isPlaying() {
    return isPlaying;
  }

  public int getPlayOrder() {
    return playOrder;
  }

  public Song getSong() {
    return song;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.isPlaying ? (byte) 1 : (byte) 0);
    dest.writeInt(this.playOrder);
  }

  public PlayStateInfo() {
  }

  public PlayStateInfo(Parcel in) {
    this.isPlaying = in.readByte() != 0;
    this.playOrder = in.readInt();
  }

  public static final Parcelable.Creator<PlayStateInfo> CREATOR = new Parcelable.Creator<PlayStateInfo>() {
    @Override public PlayStateInfo createFromParcel(Parcel source) {
      return new PlayStateInfo(source);
    }

    @Override public PlayStateInfo[] newArray(int size) {
      return new PlayStateInfo[size];
    }
  };
}
