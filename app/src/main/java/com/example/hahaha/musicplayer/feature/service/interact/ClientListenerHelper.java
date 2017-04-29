package com.example.hahaha.musicplayer.feature.service.interact;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.example.hahaha.musicplayer.app.Navigator;
import java.lang.ref.WeakReference;
import java.util.IllegalFormatCodePointException;

public class ClientListenerHelper {
  private Messenger mServerMessenger;
  private Messenger mClientMessenger;
  private SongListener mSongListener;

  public ClientListenerHelper(@NonNull SongListener songListener) {
    mClientMessenger = new Messenger(new ListenerHandler(this));
    mSongListener = songListener;
  }

  public void register(IBinder serverBinder) {
    mServerMessenger = new Messenger(serverBinder);
    Message message = Message.obtain();
    message.what = Navigator.REGISTER_LISTENER;
    message.replyTo = mClientMessenger;
    try {
      mServerMessenger.send(message);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void unregister() {
    if (mServerMessenger == null) return;
    Message message = Message.obtain();
    message.what = Navigator.UN_REGISTER_LISTENER;
    message.replyTo = mClientMessenger;
    try {
      mServerMessenger.send(message);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }


  public static class ListenerHandler extends Handler {
    private WeakReference<ClientListenerHelper> mWeakRef;

    public ListenerHandler(ClientListenerHelper helper) {
      mWeakRef = new WeakReference<ClientListenerHelper>(helper);
    }

    @Override public void handleMessage(Message msg) {
      ClientListenerHelper helper = mWeakRef.get();
      if (helper == null) return;
      Bundle data = msg.getData();
      switch (msg.what) {
        case Navigator.ON_SONG_CHANGE:
          helper.mSongListener.onSongChange(
              data.getString(Navigator.EXTRA_SONG_NAME),
              data.getBoolean(Navigator.EXTRA_IS_PLAYING)
          );
          break;
        case Navigator.ON_PREPARING:
          helper.mSongListener.onPreparing();
          break;
      }
      super.handleMessage(msg);
    }
  }
}
