package com.example.hahaha.musicplayer.feature.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.example.hahaha.musicplayer.feature.service.aid.IMusicManager;
import com.example.hahaha.musicplayer.feature.service.aid.PositionListener;
import com.example.hahaha.musicplayer.feature.service.aid.SongChangeListener;
import com.example.hahaha.musicplayer.feature.service.listener.ListenerManager;
import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.app.Navigator;
import java.util.List;

public class MusicService extends Service {

  private IMusicManager mMusicManager;
  private Player mPlayer;
  private SongListManager mSongListManager;
  private ListenerManager mListenerManager;
  private boolean mHasPrepare = false;

  @Override public void onCreate() {
    super.onCreate();
    mSongListManager = SongListManager.getInstance();
    mListenerManager = new ListenerManager(
        () -> mPlayer.getCurrentPosition(), () -> mPlayer.getDuration());
    initPlayer();
    initMusicManager();
  }

  private void initPlayer() {
    mPlayer = new Player();
    mPlayer.setCompleteListener(() -> playNextSong());
    mPlayer.setErrorListener(() -> playNextSong());
  }

  private void initMusicManager() {
    mMusicManager = new IMusicManager.Stub() {
      @Override public List<Song> getSongList(int songListType) throws RemoteException {
        return mSongListManager.getSongList(songListType);
      }

      @Override public void registerSongChangeListener(SongChangeListener listener)
          throws RemoteException {
        mListenerManager.registerSongChangeListener(listener,
            mSongListManager.getCurrentSong(), mPlayer.isPlaying(), mSongListManager.getPlayOrder());
      }

      @Override public void unregisterSongChangeListener(SongChangeListener listener)
          throws RemoteException {
        mListenerManager.unregisterSongChangeListener(listener);
      }

      @Override public void registerPositionListener(PositionListener listener)
          throws RemoteException {
        mListenerManager.registerPositionListener(listener);
      }

      @Override public void unregisterPositionListener(PositionListener listener)
          throws RemoteException {
        mListenerManager.unregisterPositionListener(listener);
      }
    };
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
        mListenerManager.dismissNoti();
        mPlayer.pause();
        break;
      case Navigator.NEXT_SONG:
        playNextSong();
        break;
      case Navigator.PREVIOUS_SONG:
        playPrevSong();
        break;
      case Navigator.PLAY_PAUSE:
        playOrPause();
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
    mListenerManager.finish();
    mPlayer.finish();
    mPlayer = null;
  }

  private void play(int type, int index) {
    Song song = mSongListManager.setCurrentSong(type, index);
    if (mPlayer.play(song.uri)) {
      mListenerManager.broadcastSongChange(song, true, mSongListManager.getPlayOrder());
      return;
    }
    playNextSong();
  }

  private void prepare(int type, int index) {
    Song song = mSongListManager.setCurrentSong(type, index);
    while(! mPlayer.prepare(song.uri)) {
      song = mSongListManager.nextSong();
    }
    mListenerManager.broadcastSongChange(song, false, mSongListManager.getPlayOrder());
  }

  private void playNextSong() {
    Song song = null;
    do {
      song = mSongListManager.nextSong();
    } while (! mPlayer.play(song.uri));
    mListenerManager.broadcastSongChange(song, true, mSongListManager.getPlayOrder());
  }

  private void playPrevSong() {
    Song song = null;
    do {
      song = mSongListManager.prevSong();
    } while (! mPlayer.play(song.uri));
    mListenerManager.broadcastSongChange(song, true, mSongListManager.getPlayOrder());
  }

  private void playOrPause() {
    boolean isPlaying = mPlayer.isPlaying();
    if (isPlaying) {
      mPlayer.pause();
    } else {
      mPlayer.start();
    }
    mListenerManager.broadcastSongChange(
        mSongListManager.getCurrentSong(), !isPlaying, mSongListManager.getPlayOrder());
  }
}

