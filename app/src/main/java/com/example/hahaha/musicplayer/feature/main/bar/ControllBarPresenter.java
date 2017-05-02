package com.example.hahaha.musicplayer.feature.main.bar;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.feature.base.BaseServicePresenter;
import com.example.hahaha.musicplayer.feature.service.aid.SongChangeListener;

public class ControllBarPresenter extends BaseServicePresenter<ControlBarFragment> {

  private String mCurrentSongName;
  private SongChangeListener mSongChangeStub;

  @Override protected void onCreate(@Nullable Bundle savedState) {
    super.onCreate(savedState);
    initSongChangeStub();
  }

  private void initSongChangeStub() {
    mSongChangeStub = new SongChangeListener.Stub() {
      @Override public void onPreparing() throws RemoteException {
        ControlBarFragment fragment = getView();
        if (fragment == null) return;
        fragment.showLoadView();
      }

      @Override public void onSongChange(String songName, boolean isPlaying, int playOrder)
          throws RemoteException {
        ControlBarFragment fragment = getView();
        if (fragment == null) return;
        fragment.showContent();
        if (! TextUtils.equals(songName, mCurrentSongName)) {
          mCurrentSongName = songName;
          fragment.setSongName(songName);
        }
        fragment.setPlayState(isPlaying);
      }
    };
  }

  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    super.onServiceConnected(name, service);
    try {
      mMusicManager.registerSongChangeListener(mSongChangeStub);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override protected void onDropView() {
    super.onDropView();
    try {
      mMusicManager.unregisterSongChangeListener(mSongChangeStub);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}
