package com.example.hahaha.musicplayer.feature.service.interact;

public interface SongListener {
  void onPreparing();

  void onSongChange(String songName, boolean isPlaying);
}
