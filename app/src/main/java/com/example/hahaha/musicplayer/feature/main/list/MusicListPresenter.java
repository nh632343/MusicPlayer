package com.example.hahaha.musicplayer.feature.main.list;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.base.BaseServicePresenter;
import com.example.hahaha.musicplayer.feature.service.aid.IMusicManager;
import com.example.hahaha.musicplayer.feature.service.ServiceMessageHelper;
import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.model.enumeration.SongListType;
import java.util.List;
import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

public class MusicListPresenter extends BaseServicePresenter<MusicListFragment> {
  private static final int GET_SONG_LIST = 2000;
  private List<Song> mSongList;
  private boolean mHasRequest = false;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    restartableFirst(GET_SONG_LIST,
        () -> createObservable(),
        (musicListFragment, songList) -> onGetSongList(songList),
        (musicListFragment, throwable) -> getSongListError());
  }

  private Observable<List<Song>> createObservable() {
    return Observable.just(mMusicManager)
                     .map((iMusicManager -> {
                       try {
                         return iMusicManager.getSongList(SongListType.ALL);
                       } catch (RemoteException e) {
                         e.printStackTrace();
                         throw Exceptions.propagate(new Throwable());
                       }
                     }))
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread());
  }

  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    super.onServiceConnected(name, service);
    if (mHasRequest) return;
    mHasRequest = true;
    getView().showLoadView();
    start(GET_SONG_LIST);
  }

  private void onGetSongList(List<Song> songList) {
    mSongList = songList;
    MusicListFragment musicListFragment = getView();
    if (musicListFragment == null) return;
    musicListFragment.showSongList(songList);
    ServiceMessageHelper.prepareIfFirstTime(SongListType.ALL, 0);
  }

  private void getSongListError() {
    MusicListFragment musicListFragment = getView();
    if (musicListFragment == null) return;
    musicListFragment.showEmptyView();
  }

  void play(int position) {
    ServiceMessageHelper.playSong(SongListType.ALL, position);
  }
}
