package com.example.hahaha.musicplayer.feature.lrc.seek;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.example.hahaha.musicplayer.feature.base.BaseServicePresenter;
import com.example.hahaha.musicplayer.feature.service.ServiceMessageHelper;
import com.example.hahaha.musicplayer.feature.service.aid.PositionListener;

public class SeekBarPresenter extends BaseServicePresenter<SeekBarFragment> {
  private PositionListener mPositionStub;
  private int mDuration;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    initPositionStub();
  }

  private void initPositionStub() {
    mPositionStub = new PositionListener.Stub() {
      @Override public void onPositionChange(int position, int duration) throws RemoteException {
        mDuration = duration;
        SeekBarFragment fragment = getView();
        if (fragment == null) return;
        int progress = position * 100 / duration;
        fragment.setProgress(progress);
      }};
  }

  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    super.onServiceConnected(name, service);
    try {
      mMusicManager.registerPositionListener(mPositionStub);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override protected void onTakeView(SeekBarFragment seekBarFragment) {
    super.onTakeView(seekBarFragment);
    if (mMusicManager == null) return;
    try {
      mMusicManager.registerPositionListener(mPositionStub);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override public void dropView() {
    super.dropView();
    if (mMusicManager == null) return;
    try {
      mMusicManager.unregisterPositionListener(mPositionStub);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  void setProgress(int progress) {
    int position = progress * mDuration / 100;
    ServiceMessageHelper.setPosition(position);
  }
}
