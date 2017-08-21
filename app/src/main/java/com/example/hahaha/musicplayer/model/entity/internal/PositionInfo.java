package com.example.hahaha.musicplayer.model.entity.internal;

import android.os.Parcel;
import android.os.Parcelable;

public class PositionInfo implements Parcelable {
  private int currentTime;
  private int duration;

  public PositionInfo() {
  }

  public PositionInfo(int currentTime, int duration) {
    this.currentTime = currentTime;
    this.duration = duration;
  }

  public int getCurrentTime() {
    return currentTime;
  }

  public int getDuration() {
    return duration;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.currentTime);
    dest.writeInt(this.duration);
  }

  protected PositionInfo(Parcel in) {
    this.currentTime = in.readInt();
    this.duration = in.readInt();
  }

  public static final Parcelable.Creator<PositionInfo> CREATOR =
      new Parcelable.Creator<PositionInfo>() {
        @Override public PositionInfo createFromParcel(Parcel source) {
          return new PositionInfo(source);
        }

        @Override public PositionInfo[] newArray(int size) {
          return new PositionInfo[size];
        }
      };
}
