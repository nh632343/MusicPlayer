package com.example.hahaha.musicplayer.feature.main.bar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.model.entity.internal.PlayStateInfo;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import com.example.hahaha.musicplayer.widget.ComSubscriber;

public class ControllBarPresenter extends BasePresenter<ControlBarFragment> {

  private ComSubscriber<PlayStateInfo> mSongChangeListener;
  private MusicManagerProxy mMusicManager;

  @Override protected void onCreate(@Nullable Bundle savedState) {
    super.onCreate(savedState);
    mMusicManager = MusicManagerProxy.getInstance();
    initSongChangeStub();
  }

  private void initSongChangeStub() {
    mSongChangeListener = new ComSubscriber<PlayStateInfo>() {
      @Override public void onChange(PlayStateInfo playStateInfo) {
        ControlBarFragment fragment = getView();
        if (fragment == null) return;
        if (playStateInfo == null) {
          fragment.showEmptyView();
          return;
        }

        fragment.showContent();
        fragment.setSongName(playStateInfo.getSong().getName());
        fragment.setPlayState(playStateInfo.isPlaying());
      }};
  }

  @Override protected void onTakeView(ControlBarFragment controlBarFragment) {
    super.onTakeView(controlBarFragment);
    mMusicManager.addSongChangeListener(mSongChangeListener);
  }

  @Override protected void onDropView() {
    super.onDropView();
    mMusicManager.removeSongChangeListener(mSongChangeListener);
  }
}
