package com.example.hahaha.musicplayer.feature.play.control;

import android.os.Bundle;
import android.util.Log;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.feature.play.PlayActivity;
import com.example.hahaha.musicplayer.model.entity.internal.PlayStateInfo;
import com.example.hahaha.musicplayer.model.entity.internal.PositionInfo;
import com.example.hahaha.musicplayer.model.enumeration.PlayOrder;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import com.example.hahaha.musicplayer.tools.PlayOrderTools;
import com.example.hahaha.musicplayer.widget.ComSubscriber;

public class PlayControlPresenter extends BasePresenter<PlayControlFragment> {
  private MusicManagerProxy mMusicManager;
  private ComSubscriber<PositionInfo> mPositionListener;
  private ComSubscriber<PlayStateInfo> mSongChangeListener;
  private int mDuration;
  private int mPlayOrder;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mPlayOrder = PlayOrder.NORMAL;
    mMusicManager = MusicManagerProxy.getInstance();
    initPositionStub();
    mSongChangeListener = new ComSubscriber<PlayStateInfo>() {
      @Override public void onChange(PlayStateInfo playStateInfo) {
        if (playStateInfo == null) return;
        PlayControlFragment fragment = getView();
        if (fragment == null) return;
        Log.d("xyz", "playstate change playorder = "+String.valueOf(playStateInfo.getPlayOrder()));
        fragment.setPlayOrder(PlayOrderTools.getDrawableRes(playStateInfo.getPlayOrder()));
        fragment.setIsPlaying(playStateInfo.isPlaying());
      }
    };
  }

  private void initPositionStub() {
    mPositionListener = new ComSubscriber<PositionInfo>() {
      @Override public void onChange(PositionInfo positionInfo) {
        mDuration = positionInfo.getDuration();
        PlayControlFragment fragment = getView();
        if (fragment == null) return;
        int progress = positionInfo.getCurrentTime() * 100 / mDuration;
        fragment.setProgress(progress, positionInfo.getCurrentTime(), mDuration);
      }
    };
  }


  @Override protected void onTakeView(PlayControlFragment playControlFragment) {
    super.onTakeView(playControlFragment);
    mMusicManager.addPositionListener(mPositionListener);
    mMusicManager.addSongChangeListener(mSongChangeListener);
  }

  @Override protected void onDropView() {
    super.onDropView();
    mMusicManager.removePositionListener(mPositionListener);
    mMusicManager.removeSongChangeListener(mSongChangeListener);
  }

  void setProgress(int progress) {
    int position = progress * mDuration / 100;
    mMusicManager.setPosition(position);
  }

  void playOrderClick() {
    Log.d("xyz", "play order click "+String.valueOf(mPlayOrder));
    switch (mPlayOrder) {
      case PlayOrder.NORMAL: mPlayOrder = PlayOrder.RANDOM; break;
      case PlayOrder.RANDOM: mPlayOrder = PlayOrder.SINGLE; break;
      case PlayOrder.SINGLE: mPlayOrder = PlayOrder.NORMAL; break;
    }
    Log.d("xyz", "after play order click "+String.valueOf(mPlayOrder));
    mMusicManager.setPlayOrder(mPlayOrder);
  }

  void prevSong() {
    mMusicManager.playPrevious();
  }

  void nextSong() {
    mMusicManager.playNext();
  }

  void playPause() {
    mMusicManager.playOrPause();
  }
}
