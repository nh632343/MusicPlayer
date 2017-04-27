package com.example.hahaha.musicplayer.feature.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.RemoteViews;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.app.Navigator;

/**
 * 负责管理通知栏
 */
public class MusicNotiManager {
  private static MusicNotiManager singleton = new MusicNotiManager();

  public static MusicNotiManager getInstance() {
    return singleton;
  }

  private Context mContext;
  private NotificationManager mManager;
  private RemoteViews mBigContentView;
  private RemoteViews mContentView;
  private Notification mNotification;

  private MusicNotiManager() {
    mContext = MusicApp.appContext();
    mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    mNotification = createEmptyNotification();
    mBigContentView = createBigContentView();
    mContentView = createContentView();
    mNotification.bigContentView = mBigContentView;
    mNotification.contentView = mContentView;
  }

  public void show(String songName, String artist, boolean isPlaying) {
    mBigContentView.setTextViewText(R.id.txt_song_name, songName);
    mBigContentView.setTextViewText(R.id.txt_artist, artist);
    if (isPlaying) {
      mBigContentView.setImageViewResource(R.id.img_play_pause, R.drawable.ic_noti_pause_white);
    } else {
      mBigContentView.setImageViewResource(R.id.img_play_pause, R.drawable.ic_noti_play_white);
    }
    mManager.notify(Navigator.MUSIC_NOTI_ID, mNotification);
  }

  public void changePlayState(boolean isPlaying) {
    if (isPlaying) {
      mBigContentView.setImageViewResource(R.id.img_play_pause, R.drawable.ic_noti_play_white);
    } else {
      mBigContentView.setImageViewResource(R.id.img_play_pause, R.drawable.ic_noti_pause_white);
    }
    mManager.notify(Navigator.MUSIC_NOTI_ID, mNotification);
  }

  public void dismiss() {
    mManager.cancel(Navigator.MUSIC_NOTI_ID);
  }

  private Notification createEmptyNotification() {
    return new Notification
        .Builder(mContext)
        .setOngoing(true)
        .setPriority(Notification.PRIORITY_MAX)
        .setSmallIcon(R.drawable.ic_noti)
        .setContentIntent(
            ServiceMessageHelper.getNotiClickPendingIntent())
        .build();
  }

  private RemoteViews createBigContentView() {
    RemoteViews bigContentView = new RemoteViews(mContext.getPackageName(), R.layout.noti_big_content_view);
    //cancel
    bigContentView.setOnClickPendingIntent(
        R.id.img_cancel, ServiceMessageHelper.getDismissPendingIntent());
    //上一首点击事件
    bigContentView.setOnClickPendingIntent(
        R.id.img_prev, ServiceMessageHelper.getPrevSongPendingIntent());
    //下一首点击事件
    bigContentView.setOnClickPendingIntent(
        R.id.img_next, ServiceMessageHelper.getNextSongPendingIntent());
    //暂停或播放点击事件
    bigContentView.setOnClickPendingIntent(
        R.id.img_play_pause, ServiceMessageHelper.getPlayPausePendingIntent());

    return bigContentView;
  }

  private RemoteViews createContentView() {
    RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.noti_content_view);
    return contentView;
  }
}
