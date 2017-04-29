package com.example.hahaha.musicplayer.feature.main.bar;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.service.interact.ClientListenerHelper;
import com.example.hahaha.musicplayer.feature.service.interact.ServiceMessageHelper;
import com.example.hahaha.musicplayer.feature.service.interact.SongListener;
import nucleus.presenter.Presenter;

public class ControllBarPresenter extends Presenter<ControlBarFragment>
   implements SongListener {

  private ClientListenerHelper mListenerHelper;
  private ServiceConnection mServiceConn;
  private String mCurrentSongName;

  private class ServiceConn implements ServiceConnection {
    @Override public void onServiceConnected(ComponentName name, IBinder service) {
      mListenerHelper.register(service);
    }

    @Override public void onServiceDisconnected(ComponentName name) {}
  }

  @Override protected void onCreate(@Nullable Bundle savedState) {
    super.onCreate(savedState);
    mListenerHelper = new ClientListenerHelper(this);
    mServiceConn = new ServiceConn();
  }

  @Override protected void onTakeView(ControlBarFragment controlBarFragment) {
    super.onTakeView(controlBarFragment);
    MusicApp.appContext().bindService(ServiceMessageHelper.createIntent(),
        mServiceConn, Context.BIND_AUTO_CREATE);
  }

  @Override protected void onDropView() {
    super.onDropView();
    mListenerHelper.unregister();
    MusicApp.appContext().unbindService(mServiceConn);
  }

  @Override public void onSongChange(String songName, boolean isPlaying) {
    ControlBarFragment fragment = getView();
    if (fragment == null) return;
    fragment.showContent();
    if (! TextUtils.equals(songName, mCurrentSongName)) {
      mCurrentSongName = songName;
      fragment.setSongName(songName);
    }
    fragment.setPlayState(isPlaying);
  }

  @Override public void onPreparing() {
    ControlBarFragment fragment = getView();
    if (fragment == null) return;
    fragment.showLoadView();
  }
}
