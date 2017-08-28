package com.example.hahaha.musicplayer.manager.lrc;

import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rx.functions.Func0;

public class LrcManager {
  private static final int MAX_THREAD_COUNT = 2;

  static LrcLruCache sLrcLruCache = new LrcLruCache();
  static LrcDiskCache sLrcDiskCache = new LrcDiskCache();
  private static ExecutorService sThreadPool = Executors.newFixedThreadPool(MAX_THREAD_COUNT);

  private LrcCallbackWrapper mCallbackWrapper;

  public LrcManager() {
    sLrcDiskCache.open();
  }

  public void findFirstLrc(String path, String name, LrcCallback callback) {
    if (mCallbackWrapper != null) {
      mCallbackWrapper.cancel();
      mCallbackWrapper = null;
    }

    List<LrcLineInfo> lrcList = null;
    lrcList = sLrcLruCache.get(path);
    if (lrcList != null) {
      List<List<LrcLineInfo>> result = new ArrayList<>();
      result.add(lrcList);
      callback.lrcSuccess(result);
      return;
    }

    mCallbackWrapper = new LrcCallbackWrapper(callback);
    sThreadPool.execute(new FindFirstLrc(path, name.trim(), mCallbackWrapper, mCallbackWrapper));
  }

  public void findAllLrc(String songName, String artistName, LrcCallback callback) {
    if (mCallbackWrapper != null) {
      mCallbackWrapper.cancel();
      mCallbackWrapper = null;
    }

    mCallbackWrapper = new LrcCallbackWrapper(callback);
    sThreadPool.execute(new FindAllLrc(songName, artistName, mCallbackWrapper, mCallbackWrapper));
  }

  public void close() {
    if (mCallbackWrapper != null) {
      mCallbackWrapper.cancel();
      mCallbackWrapper = null;
    }
    sLrcDiskCache.close();
  }


  private static class LrcCallbackWrapper
      implements LrcCallback , Func0<Boolean> {
    LrcCallback mCallback;
    volatile boolean cancel;

    LrcCallbackWrapper(LrcCallback callback) {
      mCallback = callback;
      cancel = false;
    }

    @Override public void lrcSuccess(List<List<LrcLineInfo>> result) {
      if (mCallback == null) return;
      mCallback.lrcSuccess(result);
    }

    @Override public void lrcFailure() {
      if (mCallback == null) return;
      mCallback.lrcFailure();
    }

    @Override public Boolean call() {
      return cancel;
    }

    void cancel() {
      cancel = true;
      mCallback = null;
    }
  }
}
