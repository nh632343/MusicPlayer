package com.example.hahaha.musicplayer.feature.main.list;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.service.interact.ServiceMessageHelper;
import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.model.enumeration.SongListType;
import java.util.List;
import nucleus.presenter.RxPresenter;

public class MusicListPresenter extends RxPresenter<MusicListFragment> {
  private class ServiceConn implements ServiceConnection {
    @Override public void onServiceConnected(ComponentName name, IBinder service) {
      mIsBind = true;
      mServerMessenger = new Messenger(service);
      if (mHasRequest) return;
      mHasRequest = true;
      getView().showLoadView();
      ServiceMessageHelper.sendGetSongListMessage(
          SongListType.ALL, mServerMessenger, mClientMessenger);
    }

    @Override public void onServiceDisconnected(ComponentName name) {
      mIsBind = false;
    }
  }

  private ServiceConnection mServiceConn;
  private Messenger mServerMessenger;
  private Messenger mClientMessenger;
  private boolean mIsBind = false;
  private List<Song> mSongList;
  private boolean mHasRequest = false;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mServiceConn = new ServiceConn();
    mClientMessenger = new Messenger(new MusicListHandler(this));
  }

  @Override protected void onTakeView(MusicListFragment musicListFragment) {
    super.onTakeView(musicListFragment);
    MusicApp.appContext().bindService(ServiceMessageHelper.createIntent(),
        mServiceConn, Context.BIND_AUTO_CREATE);
  }

  @Override protected void onDropView() {
    super.onDropView();
    MusicApp.appContext().unbindService(mServiceConn);
  }

  void onGetSongList(List<Song> songList) {
    mSongList = songList;
    MusicListFragment musicListFragment = getView();
    if (musicListFragment == null) return;
    musicListFragment.showSongList(songList);
    if (! mIsBind) return;
    ServiceMessageHelper.prepareIfFirstTime(SongListType.ALL, 0);
  }

  void getSongListError() {
    MusicListFragment musicListFragment = getView();
    if (musicListFragment == null) return;
    musicListFragment.showEmptyView();
  }

  public void play(int position) {
    if (! mIsBind) return;
    ServiceMessageHelper.playSong(SongListType.ALL, position);
  }
}
