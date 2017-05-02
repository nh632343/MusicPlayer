package com.example.hahaha.musicplayer.feature.service.listener;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.feature.service.aid.PositionListener;
import com.example.hahaha.musicplayer.feature.service.aid.SongChangeListener;
import com.example.hahaha.musicplayer.model.entity.Song;
import rx.functions.Func0;

public class ListenerManager {
  private MusicNotiManager mMusicNotiManager;
  private SongChangeListenerManager mSongListenerManager;
  private PositionListenerManager mPositionListenerManager;

  public ListenerManager(Func0<Integer> getPosition, Func0<Integer> getDuration) {
    mMusicNotiManager = new MusicNotiManager();
    mSongListenerManager = new SongChangeListenerManager();
    mPositionListenerManager = new PositionListenerManager(getPosition, getDuration);
  }

  public void registerSongChangeListener(SongChangeListener listener,
      @Nullable Song song, boolean isPlaying, int playOrder) {
    mSongListenerManager.register(listener, song, isPlaying, playOrder);
  }

  public void unregisterSongChangeListener(SongChangeListener listener) {
    mSongListenerManager.unregister(listener);
  }

  public void broadcastSongChange(Song song, boolean isPlaying, int playOrder) {
    mMusicNotiManager.show(song.name, "", isPlaying);
    mSongListenerManager.broadcast(song, isPlaying, playOrder);
  }

  public void registerPositionListener(PositionListener listener) {
    mPositionListenerManager.register(listener);
  }

  public void unregisterPositionListener(PositionListener listener) {
    mPositionListenerManager.unregister(listener);
  }

  public void dismissNoti() {
    mMusicNotiManager.dismiss();
  }

  public void finish() {
    mMusicNotiManager.dismiss();
    mSongListenerManager.finish();
    mPositionListenerManager.finish();
  }
}
