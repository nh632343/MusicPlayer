package com.example.hahaha.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.service.aid.IMusicManager;
import com.example.hahaha.musicplayer.service.aid.PositionListener;
import com.example.hahaha.musicplayer.service.aid.SongChangeListener;
import com.example.hahaha.musicplayer.service.listener.MusicNotiManager;
import com.example.hahaha.musicplayer.service.listener.PositionListenerManager;
import com.example.hahaha.musicplayer.service.listener.SongChangeListenerManager;

public class MusicService extends Service {

  private IMusicManager mMusicManager;
  private Player mPlayer;
  private PositionListenerManager mPositionManager;
  private SongChangeListenerManager mSongChangeManager;
  private MusicNotiManager mNotiManager;
  private boolean mPrepare;

  @Override public void onCreate() {
    super.onCreate();
    mPrepare = false;
    mNotiManager = new MusicNotiManager();
    mSongChangeManager = new SongChangeListenerManager();
    mPositionManager = new PositionListenerManager(
        () -> {
          if (mPrepare) return mPlayer.getCurrentPosition();
          return 0;
        },
        () -> {
          if (mPrepare) return mPlayer.getDuration();
          return 1;
        });
    initPlayer();
    initMusicManager();
  }

  private void initPlayer() {
    mPlayer = new Player();
    mPlayer.setPrepareListener(() -> {
      mPrepare = true;
    });
    mPlayer.setCompleteListener(() -> {
      mPlayer.setPosition(0);
      mPlayer.pause();
      mSongChangeManager.broadcastComplete();
      mNotiManager.changePlayState(false);

    });
    mPlayer.setErrorListener(() -> {
      mPrepare = false;
      mSongChangeManager.broadcastError();
      mNotiManager.changePlayState(false);
    });
  }

  private void initMusicManager() {
    mMusicManager = new IMusicManager.Stub() {
      @Override public void setSong(Song song, boolean shouldPlay)
          throws RemoteException {
        playOrPrepare(song, shouldPlay);
      }

      @Override public void registerSongChangeListener(SongChangeListener listener)
          throws RemoteException {
        mSongChangeManager.register(listener);
      }

      @Override public void unregisterSongChangeListener(SongChangeListener listener)
          throws RemoteException {
        mSongChangeManager.unregister(listener);
      }

      @Override public void registerPositionListener(PositionListener listener)
          throws RemoteException {
        mPositionManager.register(listener);
      }

      @Override public void unregisterPositionListener(PositionListener listener)
          throws RemoteException {
        mPositionManager.unregister(listener);
      }
    };
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    if (intent == null) return START_STICKY;
    String action = intent.getAction();
    if (TextUtils.isEmpty(action)) return START_STICKY;
    switch (action) {
      case Navigator.DISMISS:
        mNotiManager.dismiss();
        mPlayer.pause();
        mSongChangeManager.broadcastStateChange(false);
        break;
      case Navigator.NEXT_SONG:
        mSongChangeManager.broadcastComplete();
        break;
      case Navigator.PLAY_PAUSE:
        playOrPause();
        break;
      case Navigator.SET_POSITION:
        mPlayer.setPosition(
            intent.getIntExtra(Navigator.EXTRA_POSITION, 0));
        break;
    }
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mMusicManager.asBinder();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mNotiManager.dismiss();
    mSongChangeManager.finish();
    mPositionManager.finish();
    mPlayer.finish();
    mPlayer = null;
  }

  private void playOrPrepare(Song song, boolean play) {
    mPrepare = false;
    if (play) {
      mPlayer.play(song.getUri());
    } else {
      mPlayer.prepare(song.getUri());
    }
    mNotiManager.show(song.getName(), song.getArtist(), play);
  }

  private void playOrPause() {
    boolean isPlaying = mPlayer.isPlaying();
    if (isPlaying) {
      mPlayer.pause();
    } else {
      mPlayer.start();
    }
    mNotiManager.changePlayState(!isPlaying);
    mSongChangeManager.broadcastStateChange(!isPlaying);
  }
}

