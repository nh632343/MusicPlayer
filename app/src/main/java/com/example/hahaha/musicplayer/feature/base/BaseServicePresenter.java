package com.example.hahaha.musicplayer.feature.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.service.ServiceMessageHelper;
import com.example.hahaha.musicplayer.feature.service.aid.IMusicManager;
import nucleus.presenter.RxPresenter;

public class BaseServicePresenter<ViewType> extends RxPresenter<ViewType>
    implements ServiceConnection{

  private boolean mHasBind = false;
  protected IMusicManager mMusicManager;

  @Override protected void onTakeView(ViewType viewType) {
    super.onTakeView(viewType);
    if (mHasBind) return;
    MusicApp.appContext().bindService(ServiceMessageHelper.createIntent(),
        this, Context.BIND_AUTO_CREATE);
  }

  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    mHasBind = true;
    mMusicManager = IMusicManager.Stub.asInterface(service);
  }

  @Override public void onServiceDisconnected(ComponentName name) {

  }

  @Override protected void onDestroy() {
    super.onDestroy();
    try {
      MusicApp.appContext().unbindService(this);
    } catch (Exception e) {e.printStackTrace();}
  }
}
