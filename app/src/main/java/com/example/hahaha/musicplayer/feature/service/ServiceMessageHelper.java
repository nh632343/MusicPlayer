package com.example.hahaha.musicplayer.feature.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.app.Navigator;

public class ServiceMessageHelper {
  public static PendingIntent getDismissPendingIntent() {
    return getPendingIntent(Navigator.DISMISS);
  }

  public static PendingIntent getNextSongPendingIntent() {
    return getPendingIntent(Navigator.NEXT_SONG);
  }

  public static PendingIntent getPrevSongPendingIntent() {
    return getPendingIntent(Navigator.PREVIOUS_SONG);
  }

  public static PendingIntent getPlayPausePendingIntent() {
    return getPendingIntent(Navigator.PLAY_PAUSE);
  }

  public static PendingIntent getNotiClickPendingIntent() {
    return getPendingIntent(Navigator.NOTI_CLICK);
  }

  public static Intent createIntent() {
    Intent intent = new Intent(MusicApp.appContext(), MusicService.class);
    return intent;
  }

  private static PendingIntent getPendingIntent(String action) {
    Intent intent = createIntent();
    intent.setAction(action);
    return PendingIntent.getService(MusicApp.appContext(), 0,
        intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public static void prevSong() {
    Intent intent = createIntent();
    intent.setAction(Navigator.PREVIOUS_SONG);
    MusicApp.appContext().startService(intent);
  }

  public static void nextSong() {
    Intent intent = createIntent();
    intent.setAction(Navigator.NEXT_SONG);
    MusicApp.appContext().startService(intent);
  }

  public static void playOrPause() {
    Intent intent = createIntent();
    intent.setAction(Navigator.PLAY_PAUSE);
    MusicApp.appContext().startService(intent);
  }

  public static void playSong(int type, int index) {
    Intent intent = createIntent();
    intent.setAction(Navigator.PLAY_SONG);
    intent.putExtra(Navigator.EXTRA_SONG_LIST_TYPE, type);
    intent.putExtra(Navigator.EXTRA_SONG_INDEX, index);
    MusicApp.appContext().startService(intent);
  }

  public static void prepareIfFirstTime(int type, int index) {
    Intent intent = createIntent();
    intent.setAction(Navigator.PLAY_SONG);
    intent.putExtra(Navigator.EXTRA_SONG_LIST_TYPE, type);
    intent.putExtra(Navigator.EXTRA_SONG_INDEX, index);
    MusicApp.appContext().startService(intent);
  }

  public static void sendGetSongListMessage(
      int type, Messenger serverMessenger, Messenger clientMessenger) {
    Message message = Message.obtain();
    message.what = Navigator.GET_SONG_LIST;
    message.arg1 = type;
    message.replyTo = clientMessenger;
    try {
      serverMessenger.send(message);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public static int getSongListType(Message message) {
    return message.arg1;
  }


}
