package com.example.hahaha.musicplayer.feature.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.feature.main.list.MusicListHandler;
import com.example.hahaha.musicplayer.model.entity.Song;
import java.util.ArrayList;

public class MusicService extends Service {

  private class ServiceHandler extends Handler {
    ServiceHandler(Looper looper) {
      super(looper);
    }

    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case Navigator.GET_SONG_LIST:
          ArrayList<Song> songList = mSongListManager.getSongList(
              ServiceMessageHelper.getSongListType(msg));
          MusicListHandler.replySongList(songList, msg.replyTo);
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }

  private Player mPlayer;
  private SongListManager mSongListManager;
  private MusicNotiManager mMusicNotiManager;
  private boolean mHasPrepare = false;
  private HandlerThread mWorkerThread;
  private ServiceHandler mServiceHandler;
  private Messenger mMessenger;

  @Override public void onCreate() {
    super.onCreate();
    mSongListManager = SongListManager.getInstance();
    mMusicNotiManager = MusicNotiManager.getInstance();

    mPlayer = Player.getInstance();
    mPlayer.setCompleteListener(() -> playNextSong());
    mPlayer.setErrorListener(() -> playNextSong());

    mWorkerThread = new HandlerThread("Music Service");
    mWorkerThread.start();
    mServiceHandler = new ServiceHandler(mWorkerThread.getLooper());
    mMessenger = new Messenger(mServiceHandler);
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    if (intent == null) return START_STICKY;
    String action = intent.getAction();
    if (TextUtils.isEmpty(action)) return START_STICKY;
    switch (action) {
      case Navigator.PLAY_SONG:
        play(intent.getIntExtra(Navigator.EXTRA_SONG_LIST_TYPE, 0),
            intent.getIntExtra(Navigator.EXTRA_SONG_INDEX, 0));
        break;
      case Navigator.PREPARE_IF_FIRST_TIME:
        if (mHasPrepare) break;
        mHasPrepare = true;
        prepare(intent.getIntExtra(Navigator.EXTRA_SONG_LIST_TYPE, 0),
            intent.getIntExtra(Navigator.EXTRA_SONG_INDEX, 0));
        break;
      case Navigator.DISMISS:
        mMusicNotiManager.dismiss();
        mPlayer.pause();
        break;
      case Navigator.NEXT_SONG:
        playNextSong();
        break;
      case Navigator.PREVIOUS_SONG:
        playPrevSong();
        break;
      case Navigator.PLAY_PAUSE:
        boolean isPlaying = mPlayer.isPlaying();
        mMusicNotiManager.changePlayState(isPlaying);
        if (isPlaying) {
          mPlayer.pause();
        } else {
          mPlayer.start();
        }
        break;
    }
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mMessenger.getBinder();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mWorkerThread.quit();
    mPlayer.finish();
    mPlayer = null;
    mSongListManager.finish();
    mSongListManager = null;
    mMusicNotiManager = null;
  }

  private void play(int type, int index) {
    Song song = mSongListManager.setCurrentSong(type, index);
    if (mPlayer.play(song.uri)) {
      mMusicNotiManager.show(song.name, "", true);
      return;
    }
    playNextSong();
  }

  private void prepare(int type, int index) {
    Song song = mSongListManager.setCurrentSong(type, index);
    while(! mPlayer.prepare(song.uri)) {
      song = mSongListManager.nextSong();
    }
    mMusicNotiManager.show(song.name, "", false);
  }

  private void playNextSong() {
    Song song = null;
    do {
      song = mSongListManager.nextSong();
    } while (! mPlayer.play(song.uri));
    mMusicNotiManager.show(song.name, "", true);
  }

  private void playPrevSong() {
    Song song = null;
    do {
      song = mSongListManager.prevSong();
    } while (! mPlayer.play(song.uri));
    mMusicNotiManager.show(song.name, "", true);
  }
}

