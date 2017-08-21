package com.example.hahaha.musicplayer.model.entity.internal;

import android.support.annotation.Nullable;
import org.litepal.crud.DataSupport;

public class SongRecord extends DataSupport {
  private long songId;

  public SongRecord(long songId) {
    this.songId = songId;
  }

  public @Nullable Song getSong() {
    return DataSupport.find(Song.class, songId);
  }
}
