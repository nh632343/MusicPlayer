package com.example.hahaha.musicplayer.feature.service.listener;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.example.hahaha.musicplayer.feature.service.aid.SongChangeListener;
import com.example.hahaha.musicplayer.model.entity.Song;

public class SongChangeListenerManager {
  private RemoteCallbackList<SongChangeListener> mListenerList;

  public SongChangeListenerManager() {
    mListenerList = new RemoteCallbackList<>();
  }

  public void register(SongChangeListener listener,
      @Nullable Song song, boolean isPlaying, int playOrder) {
    if (mListenerList.register(listener) == false) return;
    if (song == null) {
      try {
        listener.onPreparing();
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      return;
    }
    try {
      listener.onSongChange(song.name, isPlaying, playOrder);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void unregister(SongChangeListener listener) {
    mListenerList.unregister(listener);
  }

  public void broadcast(Song song, boolean isPlaying, int playOrder) {
    int length = mListenerList.beginBroadcast();
    for (int i = 0; i < length; ++i) {
      try {
        mListenerList.getBroadcastItem(i).onSongChange(song.name, isPlaying, playOrder);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
    mListenerList.finishBroadcast();
  }

  public void finish() {
    mListenerList.kill();
  }
}
