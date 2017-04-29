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
import com.example.hahaha.musicplayer.feature.service.interact.ServiceMessageHelper;
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
        case Navigator.REGISTER_LISTENER:
          mListenerManager.register(msg.replyTo,
              mSongListManager.getCurrentSong(), mPlayer.isPlaying());
          break;
        case Navigator.UN_REGISTER_LISTENER:
          mListenerManager.unregister(msg.replyTo);
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }

  private Player mPlayer;
  private SongListManager mSongListManager;
  private ListenerManager mListenerManager;
  private boolean mHasPrepare = false;
  private HandlerThread mWorkerThread;
  private Messenger mMessenger;

  @Override public void onCreate() {
    super.onCreate();
    mSongListManager = SongListManager.getInstance();
    mListenerManager = new ListenerManager();

    mPlayer = Player.getInstance();
    mPlayer.setCompleteListener(() -> playNextSong());
    mPlayer.setErrorListener(() -> playNextSong());

    mWorkerThread = new HandlerThread("Music Service");
    mWorkerThread.start();
    mMessenger = new Messenger(new ServiceHandler(mWorkerThread.getLooper()));
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
    return mMessenger.getBinder();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mWorkerThread.quit();
    mListenerManager.finish();
    mPlayer.finish();
    mPlayer = null;
  }

  private void play(int type, int index) {
    Song song = mSongListManager.setCurrentSong(type, index);
    if (mPlayer.play(song.uri)) {
      mListenerManager.broadcast(song, true);
      return;
    }
    playNextSong();
  }

  private void prepare(int type, int index) {
    Song song = mSongListManager.setCurrentSong(type, index);
    while(! mPlayer.prepare(song.uri)) {
      song = mSongListManager.nextSong();
    }
    mListenerManager.broadcast(song, false);
  }

  private void playNextSong() {
    Song song = null;
    do {
      song = mSongListManager.nextSong();
    } while (! mPlayer.play(song.uri));
    mListenerManager.broadcast(song, true);
  }

  private void playPrevSong() {
    Song song = null;
    do {
      song = mSongListManager.prevSong();
    } while (! mPlayer.play(song.uri));
    mListenerManager.broadcast(song, true);
  }

  private void playOrPause() {
    boolean isPlaying = mPlayer.isPlaying();
    if (isPlaying) {
      mPlayer.pause();
    } else {
      mPlayer.start();
    }
    mListenerManager.broadcast(
        mSongListManager.getCurrentSong(), !isPlaying);
  }
}

