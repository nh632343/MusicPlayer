package com.example.hahaha.musicplayer.feature.service;

import android.media.MediaPlayer;
import android.net.Uri;
import com.example.hahaha.musicplayer.app.MusicApp;
import java.io.IOException;
import rx.functions.Action0;

public class Player {
  private static Player sPlayer;

  public static Player getInstance() {
    if (sPlayer == null) {
      sPlayer = new Player();
    }
    return sPlayer;
  }

  private MediaPlayer mMediaPlayer;
  private Action0 mCompleteListener;
  private Action0 mErrorListener;

  private boolean mNeedPlay;

  private Player() {
    mMediaPlayer = new MediaPlayer();
    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override public void onPrepared(MediaPlayer mp) {
        if (! mNeedPlay) return;
        mp.start();
      }});
    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override public void onCompletion(MediaPlayer mp) {
        if (mCompleteListener == null) return;
        mCompleteListener.call();
      }
    });
    mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
      @Override public boolean onError(MediaPlayer mp, int what, int extra) {
        mMediaPlayer.reset();
        if (mErrorListener == null) return true;
        mErrorListener.call();
        return true;
      }
    });
  }

  /**
   * 播放歌曲
   * @param songUri
   * @return true表示找到歌曲， 正在准备    false表示找不到歌曲
   */
  private boolean prepareSong(Uri songUri) {
    mMediaPlayer.reset();
    try {
      mMediaPlayer.setDataSource(MusicApp.appContext(), songUri);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    mMediaPlayer.prepareAsync();
    return true;
  }

  public boolean prepare(Uri songUri) {
    mNeedPlay = false;
    return prepareSong(songUri);
  }

  public boolean play(Uri songUri) {
    mNeedPlay = true;
    return prepareSong(songUri);
  }

  public boolean isPlaying() {
    return mMediaPlayer.isPlaying();
  }

  public void start() {
    mMediaPlayer.start();
  }

  public void pause() {
    mMediaPlayer.pause();
  }

  public void setLoop(boolean loop) {
    mMediaPlayer.setLooping(loop);
  }

  public int getDuration() {
    return mMediaPlayer.getDuration();
  }

  public int getCurrentProgress() {
    return mMediaPlayer.getCurrentPosition();
  }

  public void setProgress(int progress) {
    mMediaPlayer.seekTo(progress);
  }

  public void finish() {
    mMediaPlayer.release();
    mMediaPlayer = null;
    sPlayer = null;
  }

  //---------------- Listener -------------------------------
  public void setCompleteListener(Action0 listener) {
    mCompleteListener = listener;
  }

  public void setErrorListener(Action0 listener) {
    mErrorListener = listener;
  }
}
