package com.example.hahaha.musicplayer.model.entity.api;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoubanSongQuery {
  ArrayList<DoubanSong> musics;

  public DoubanSongQuery() {
  }

  public ArrayList<DoubanSong> getList() {
    return musics;
  }
}
