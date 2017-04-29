package com.example.hahaha.musicplayer.feature.service;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.model.entity.Song;

public class ListenerManager {
  private RemoteCallbackList<IInterface> mListenerList;
  private MusicNotiManager mMusicNotiManager;

  public ListenerManager() {
    mMusicNotiManager = new MusicNotiManager();
  }

  public void register(Messenger clientMessenger, @Nullable Song song, boolean isPlaying) {
    checkListIfNull();
    mListenerList.register(new MessengerWrapper(clientMessenger));
    if (song == null) {
      sendPreparingMessage(clientMessenger);
      return;
    }
    sendBroadcast(clientMessenger, song.name, isPlaying);
  }

  public void unregister(Messenger clientMessenger) {
    if (mListenerList == null) return;
    mListenerList.unregister(new MessengerWrapper(clientMessenger));
  }

  public void broadcast(Song song, boolean isPlaying) {
    mMusicNotiManager.show(song.name, song.artist, isPlaying);
    if (mListenerList == null) return;
    mListenerList.beginBroadcast();
    int length = mListenerList.getRegisteredCallbackCount();
    for (int i = 0; i < length; ++i) {
      sendBroadcast(mListenerList.getBroadcastItem(i), song.name, isPlaying);
    }
    mListenerList.finishBroadcast();
  }

  public void dismissNoti() {
    mMusicNotiManager.dismiss();
  }

  public void finish() {
    mMusicNotiManager.dismiss();
    if (mListenerList == null) return;
    mListenerList.kill();
  }

  private void checkListIfNull() {
    if (mListenerList != null) return;
    synchronized (this) {
      if (mListenerList == null) mListenerList = new RemoteCallbackList<>();
    }
  }

  private void sendPreparingMessage(Messenger clientMessenger) {
    Message message = Message.obtain();
    message.what = Navigator.ON_PREPARING;
    try {
      clientMessenger.send(message);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private void sendBroadcast(IInterface wrapper, String songName, boolean isPlaying) {
    sendBroadcast(((MessengerWrapper) wrapper).mClientMessenger, songName, isPlaying);
  }

  private void sendBroadcast(Messenger clientMessenger, String songName, boolean isPlaying) {
    Bundle data = new Bundle();
    data.putString(Navigator.EXTRA_SONG_NAME, songName);
    data.putBoolean(Navigator.EXTRA_IS_PLAYING, isPlaying);
    Message message = Message.obtain();
    message.what = Navigator.ON_SONG_CHANGE;
    message.setData(data);
    try {
      clientMessenger.send(message);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  static class MessengerWrapper implements IInterface {
    Messenger mClientMessenger;

    MessengerWrapper(Messenger messenger) {
      mClientMessenger = messenger;
    }

    @Override public IBinder asBinder() {
      return mClientMessenger.getBinder();
    }
  }
}
