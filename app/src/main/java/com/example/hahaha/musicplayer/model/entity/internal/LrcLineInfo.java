package com.example.hahaha.musicplayer.model.entity.internal;

import android.content.pm.ProviderInfo;
import android.support.v4.content.ContextCompat;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;

public class LrcLineInfo {
  private static final int COLOR_NORMAL = ContextCompat.getColor(MusicApp.appContext(), R.color.lrc_normal);
  private static final int COLOR_LIGHT = ContextCompat.getColor(MusicApp.appContext(), R.color.lrc_light);

  public long time;
  public String content;
  private int color = COLOR_NORMAL;
  public int lineNum;  //歌词所占的行数
                         //应由自定义View来设置

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
}
