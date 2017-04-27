package com.example.hahaha.musicplayer.feature.service;

public class LrcManager {
  private static LrcManager sLrcManager;

  public LrcManager getInstance() {
    if (sLrcManager == null) {
      sLrcManager = new LrcManager();
    }
    return sLrcManager;
  }
}
