package com.example.hahaha.musicplayer.model.entity.internal;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Collect extends DataSupport {
  private long id;
  @Column(unique = true, nullable = false)
  private String name;

  public Collect(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getSongNum() {
    return DataSupport
        .where("collectId = ?", String.valueOf(id))
        .find(SongCollect.class)
        .size();
  }
}
