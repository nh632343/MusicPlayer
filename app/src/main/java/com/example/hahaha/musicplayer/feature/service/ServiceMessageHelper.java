package com.example.hahaha.musicplayer.feature.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.feature.lrc.LrcActivity;
import com.example.hahaha.musicplayer.feature.service.MusicService;

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
    Intent intent = new Intent(MusicApp.appContext(), LrcActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return PendingIntent.getActivity(MusicApp.appContext(), 0,
        intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
    intent.setAction(Navigator.PREPARE_IF_FIRST_TIME);
    intent.putExtra(Navigator.EXTRA_SONG_LIST_TYPE, type);
    intent.putExtra(Navigator.EXTRA_SONG_INDEX, index);
    MusicApp.appContext().startService(intent);
  }

  public static void setPosition(int position) {
    Intent intent = createIntent();
    intent.setAction(Navigator.SET_POSITION);
    intent.putExtra(Navigator.EXTRA_POSITION, position);
    MusicApp.appContext().startService(intent);
  }

}
