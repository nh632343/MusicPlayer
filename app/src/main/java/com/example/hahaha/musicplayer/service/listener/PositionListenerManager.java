package com.example.hahaha.musicplayer.service.listener;

import android.os.Handler;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.example.hahaha.musicplayer.model.entity.internal.PositionInfo;
import com.example.hahaha.musicplayer.service.aid.PositionListener;
import java.lang.ref.WeakReference;
import rx.functions.Func0;

public class PositionListenerManager {

  private static class BroadcastTask implements Runnable {
    WeakReference<PositionListenerManager> mWeakRef;
    BroadcastTask(PositionListenerManager manager) {
      mWeakRef = new WeakReference<>(manager);
    }

    @Override public void run() {
      PositionListenerManager manager = mWeakRef.get();
      if (manager == null) return;
      manager.broadcast();
    }
  }

  private static final long DELAY_MILLIS = 600;

  private Func0<Integer> mGetPosition;
  private Func0<Integer> mGetDuration;
  private RemoteCallbackList<PositionListener> mListenerList;
  private Handler mMainHandler;
  private Boolean mHasMsg;
  private BroadcastTask mBroadcastTask;



  public PositionListenerManager(Func0<Integer> getPosition, Func0<Integer> getDuration) {
    this.mGetPosition = getPosition;
    this.mGetDuration = getDuration;
    mListenerList = new RemoteCallbackList<>();
    mMainHandler = new Handler();
    mBroadcastTask = new BroadcastTask(this);
    mHasMsg = false;
  }

  public void register(PositionListener listener) {
    mListenerList.register(listener);
    synchronized (this) {
      if (mHasMsg) return;
      mHasMsg = true;
      mMainHandler.postDelayed(mBroadcastTask, DELAY_MILLIS);
    }
  }

  public void unregister(PositionListener listener) {
    mListenerList.unregister(listener);
  }

  public void finish() {
    mListenerList.kill();
  }

  private void broadcast() {
    int length = mListenerList.beginBroadcast();
    synchronized (this) {
      if (length == 0) {
        mListenerList.finishBroadcast();
        mHasMsg = false;
        return;
      }
    }
    try {
      PositionInfo positionInfo = new PositionInfo(mGetPosition.call(), mGetDuration.call());
      for (int i = 0; i < length; ++i) {
        try {
          mListenerList.getBroadcastItem(i).onPositionChange(positionInfo);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {}
    finally {
      mListenerList.finishBroadcast();
      mMainHandler.postDelayed(mBroadcastTask, DELAY_MILLIS);
    }
  }

}
