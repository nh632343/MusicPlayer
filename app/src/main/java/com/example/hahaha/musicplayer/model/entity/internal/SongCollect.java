package com.example.hahaha.musicplayer.model.entity.internal;

import android.support.annotation.Nullable;
import org.litepal.crud.DataSupport;

public class SongCollect extends DataSupport {
  private long songId;
  private long collectId;

  public SongCollect(long songId, long collectId) {
    this.songId = songId;
    this.collectId = collectId;
  }

  public long getSongId() {
    return songId;
  }

  public long getCollectId() {
    return collectId;
  }

  public @Nullable Song getSong() {
    return DataSupport.find(Song.class, songId);
  }
}
