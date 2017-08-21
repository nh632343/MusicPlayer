package com.example.hahaha.musicplayer.feature.play;

import android.os.Bundle;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.feature.collect.list.CollectListActivity;
import com.example.hahaha.musicplayer.model.entity.internal.PlayStateInfo;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.network.helper.SongDetailApi;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import com.example.hahaha.musicplayer.widget.ComSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayPresenter extends BasePresenter<PlayActivity> {
  private Song mSong;
  private ComSubscriber<PlayStateInfo> mListener;
  private MusicManagerProxy mMusicManager;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    restartableFirst(1000,
        () -> SongDetailApi.getSongDetail(mSong)
                            .subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread()),
        (activity, doubanSong) -> activity.setDoubanInfo(doubanSong));

    mMusicManager = MusicManagerProxy.getInstance();
    mListener = new ComSubscriber<PlayStateInfo>() {
      @Override public void onChange(PlayStateInfo playStateInfo) {
        if (playStateInfo == null) return;
        PlayActivity activity = getView();
        if (activity == null) return;

        changePlayState(playStateInfo, activity);
        if (mSong != null &&
            TextUtils.equals(mSong.getFilePath(), playStateInfo.getSong().getFilePath())) return;
        mSong = playStateInfo.getSong();
        //get douban BriefInfoFragment
        start(1000);
      }
    };
  }

  @Override protected void onTakeView(PlayActivity playActivity) {
    super.onTakeView(playActivity);
    mMusicManager.addSongChangeListener(mListener);
  }

  @Override protected void onDropView() {
    super.onDropView();
    mMusicManager.removeSongChangeListener(mListener);
  }

  private void changePlayState(PlayStateInfo playStateInfo, PlayActivity activity) {
    activity.setTitle(playStateInfo.getSong().getName());
  }

  void addToCollect(PlayActivity activity) {
    if (mSong == null) return;
    CollectListActivity.startAdd(activity, new long[]{mSong.getId()});
  }
}
