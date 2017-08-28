package com.example.hahaha.musicplayer.model.entity.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;

public class LrcLineInfo implements Parcelable {
  private static final int COLOR_NORMAL = ContextCompat.getColor(MusicApp.appContext(), R.color.lrc_normal);
  private static final int COLOR_LIGHT = ContextCompat.getColor(MusicApp.appContext(), R.color.lrc_light);

  long time;
  String content;
  private int color = COLOR_NORMAL;

  public LrcLineInfo() {
    color = ContextCompat.getColor(MusicApp.appContext(), R.color.lrc_normal);
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getColor() {
    return color;
  }

  public void toNormalColor() {
    color = COLOR_NORMAL;
  }

  public void toLightColor() {
    color = COLOR_LIGHT;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(this.time);
    dest.writeString(this.content);
  }

  protected LrcLineInfo(Parcel in) {
    this.time = in.readLong();
    this.content = in.readString();
  }

  public static final Parcelable.Creator<LrcLineInfo> CREATOR =
      new Parcelable.Creator<LrcLineInfo>() {
        @Override public LrcLineInfo createFromParcel(Parcel source) {
          return new LrcLineInfo(source);
        }

        @Override public LrcLineInfo[] newArray(int size) {
          return new LrcLineInfo[size];
        }
      };
}
