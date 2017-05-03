package com.example.hahaha.musicplayer.feature.lrc.songlrc;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.feature.base.BaseServicePresenter;
import com.example.hahaha.musicplayer.feature.service.aid.PositionListener;
import com.example.hahaha.musicplayer.feature.service.aid.SongChangeListener;
import com.example.hahaha.musicplayer.tools.LrcTools;
import com.example.hahaha.musicplayer.tools.MyLog;

public class LrcPresenter extends BaseServicePresenter<LrcFragment> {
  private static final int GET_LRC_LINE_LIST = 2000;
  private SongChangeListener mSongChangeStub;
  private PositionListener mPositionStub;
  private String mSongName;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    initSongChangeStub();
    initPositionStub();
    restartableFirst(GET_LRC_LINE_LIST,
        () -> LrcTools.getLrcLineInfoList(mSongName),
        (lrcFragment, list) -> lrcFragment.setLrcLineInfoList(list),
        (lrcFragment, throwable) -> { lrcFragment.showEmptyView();
          MyLog.d("xyz", throwable.getMessage()); });
  }

  private void initSongChangeStub() {
    mSongChangeStub = new SongChangeListener.Stub() {
      @Override public void onPreparing() throws RemoteException {
        LrcFragment lrcFragment = getView();
        if (lrcFragment == null) return;
        lrcFragment.showLoadView();
      }

      @Override public void onSongChange(String songName, boolean isPlaying, int playOrder)
          throws RemoteException {
        if (TextUtils.equals(songName, mSongName)) return;
        if (TextUtils.isEmpty(songName)) return;
        mSongName = songName;
        LrcFragment lrcFragment = getView();
        if (lrcFragment == null) return;
        lrcFragment.showLoadView();
        start(GET_LRC_LINE_LIST);
      }
    };
  }

  private void initPositionStub() {
    mPositionStub = new PositionListener.Stub() {
      @Override public void onPositionChange(int position, int duration) throws RemoteException {
        LrcFragment fragment = getView();
        if (fragment == null) return;
        fragment.setTime(position);
      }};
  }

  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    super.onServiceConnected(name, service);
    try {
      mMusicManager.registerSongChangeListener(mSongChangeStub);
      mMusicManager.registerPositionListener(mPositionStub);
    } catch (RemoteException e) {e.printStackTrace();}
  }

  @Override protected void onTakeView(LrcFragment lrcFragment) {
    super.onTakeView(lrcFragment);
    if (mMusicManager == null) return;
    try {
      mMusicManager.registerSongChangeListener(mSongChangeStub);
      mMusicManager.registerPositionListener(mPositionStub);
    } catch (RemoteException e) {e.printStackTrace();}
  }

  @Override protected void onDropView() {
    super.onDropView();
    if (mMusicManager == null) return;
    try {
      mMusicManager.unregisterSongChangeListener(mSongChangeStub);
      mMusicManager.unregisterPositionListener(mPositionStub);
    } catch (RemoteException e) {e.printStackTrace();}
  }
}
