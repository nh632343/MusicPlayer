package com.example.hahaha.musicplayer.manager.database;

import android.os.Handler;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.widget.ComObservable;
import com.example.hahaha.musicplayer.widget.ComSubscriber;

public class DatabaseManager {
  private static final DatabaseManager sDatabaseManager = new DatabaseManager();
  public static DatabaseManager getInstance() {
    return sDatabaseManager;
  }

  private Handler mainHandler;
  private ComObservable<Object> mCollectObservable;
  private ComObservable<Long> mCollectDetailObservable;

  private DatabaseManager() {
    mainHandler = MusicApp.appHandler();
    mCollectObservable = new ComObservable<>();
    mCollectDetailObservable = new ComObservable<>();
  }

  public void addCollectListener(ComSubscriber<Object> listener) {
    mCollectObservable.addSubscriber(listener);
  }

  public void removeCollectListener(ComSubscriber<Object> listener) {
    mCollectObservable.removeSubscriber(listener);
  }

  public void addCollectDetailListener(ComSubscriber<Long> listener) {
    mCollectDetailObservable.addSubscriber(listener);
  }

  public void removeCollectDetailListener(ComSubscriber<Long> listener) {
    mCollectDetailObservable.removeSubscriber(listener);
  }

  public void broadcastCollectChange() {
    mainHandler.post(() -> mCollectObservable.broadcast(new Object()));
  }

  public void broadcastCollectDetailChange(final long collectId) {
    mainHandler.post(() -> mCollectDetailObservable.broadcast(collectId));
  }
}
