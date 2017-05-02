package com.example.hahaha.musicplayer.app;

import com.squareup.leakcanary.LeakCanary;
import starter.kit.app.StarterApp;
import starter.kit.model.entity.Account;

public class MusicApp extends StarterApp {
  @Override public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
  }

  @Override public Account provideAccount(String accountJson) {
    return null;
  }
}
