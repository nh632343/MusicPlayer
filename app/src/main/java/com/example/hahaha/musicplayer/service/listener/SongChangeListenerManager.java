package com.example.hahaha.musicplayer.service.listener;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.example.hahaha.musicplayer.service.aid.SongChangeListener;

public class SongChangeListenerManager {
  private static final int BROADCAST_ERROR = 1;
  private static final int BROADCAST_COMPLETE = 2;
  private static final int BROADCAST_STATE_CHANGE = 3;

  private RemoteCallbackList<SongChangeListener> mListenerList;

  public SongChangeListenerManager() {
    mListenerList = new RemoteCallbackList<>();
  }

  public void register(SongChangeListener listener) {
    mListenerList.register(listener);
  }

  public void unregister(SongChangeListener listener) {
    mListenerList.unregister(listener);
  }

  public void broadcastError() {
    broadcast(BROADCAST_ERROR, false);
  }

  public void broadcastComplete() {
    broadcast(BROADCAST_COMPLETE, false);
  }

  public void broadcastStateChange(boolean play) {
    broadcast(BROADCAST_STATE_CHANGE, play);
  }

  private void broadcast(int broadcastType, boolean play) {
    int length = mListenerList.beginBroadcast();
    for (int i = 0; i < length; ++i) {
      try {
        if (broadcastType == BROADCAST_ERROR)
        {mListenerList.getBroadcastItem(i).onError();}
        if (broadcastType == BROADCAST_COMPLETE)
        {mListenerList.getBroadcastItem(i).onComplete();}
        if (broadcastType == BROADCAST_STATE_CHANGE)
        {mListenerList.getBroadcastItem(i).onStateChange(play);}
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
