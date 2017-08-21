package com.example.hahaha.musicplayer.manager.lrc;

import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.model.entity.api.LrcAddress;
import com.example.hahaha.musicplayer.model.entity.api.SearchLrc;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import com.example.hahaha.musicplayer.network.ApiService;
import com.example.hahaha.musicplayer.network.api.DownloadApi;
import com.example.hahaha.musicplayer.tools.LrcTools;
import com.example.hahaha.musicplayer.tools.StreamTools;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Func0;

class FindFirstLrc implements Runnable {
  private static final int MAX_VERIFY_LINES = 5;
  
  private String mName;
  private String mPath;
  private LrcCallback mCallback;
  private Func0<Boolean> mCheckCancel;

  /**
   * @param name 歌名
   * @param callback
   */
  FindFirstLrc(String path, String name, LrcCallback callback, Func0<Boolean> checkCancel) {
    mPath = path;
    this.mName = name;
    this.mCallback = callback;
    mCheckCancel = checkCancel;
  }

  @Override public void run() {
    if (mCheckCancel.call()) return;
    List<LrcLineInfo> lrcList = null;
    //first: get from memory
    lrcList = LrcManager.sLrcLruCache.get(mPath);
    if (lrcList != null) {
      postSuccess(lrcList);
      return;
    }
    //second: get from disk
    lrcList = LrcManager.sLrcDiskCache.get(mPath);
    if (mCheckCancel.call()) return;
    if (lrcList != null) {
      postSuccess(lrcList);
      return;
    }
    //third: get from internet
    //get all lrc address
    SearchLrc searchLrc = null;
    try {
      searchLrc = ApiService.createLrcApi().searchLrc(mName).execute().body();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (mCheckCancel.call()) return;
    if (searchLrc == null) {
      postFail();
      return;
    }
    
    InputStream inputStream = null;
    DownloadApi downloadApi = ApiService.createDownloadApi();
    for (LrcAddress address : searchLrc.getLrcAddressList()) {
      if (mCheckCancel.call()) return;
      try {
        inputStream = downloadApi.download(address.getAddress()).execute().body().byteStream();
        inputStream = StreamTools.toCache(inputStream);
        inputStream.mark(Integer.MAX_VALUE);
        if (!LrcTools.verifyLrc(inputStream, MAX_VERIFY_LINES)) {
          //unpass verify
          StreamTools.closeQuiet(inputStream);
          inputStream = null;
        }
      } catch (Exception e) {
        e.printStackTrace();
        StreamTools.closeQuiet(inputStream);
        inputStream = null;
      }
      //pass verify
      if (inputStream != null) break;
    }

    if (inputStream == null) {
      //can't find suitable inputstream
      if (mCheckCancel.call()) return;
      postFail();
      return;
    }

    try {
      inputStream.reset();
      lrcList = LrcTools.toLrcList(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (mCheckCancel.call()) return;
    if (lrcList == null) {
      postFail();
      return;
    }

    LrcManager.sLrcLruCache.put(mPath, lrcList);
    postSuccess(lrcList);
    try {
      inputStream.reset();
      LrcManager.sLrcDiskCache.put(mPath, inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }

    StreamTools.closeQuiet(inputStream);
  }

  private void postSuccess(List<LrcLineInfo> lrcList) {
    final ArrayList<List<LrcLineInfo>> lrcListCollection = new ArrayList<>();
    lrcListCollection.add(lrcList);
    MusicApp.appHandler().post(new Runnable() {
      @Override public void run() {
        if (mCheckCancel.call()) return;
        mCallback.lrcSuccess(lrcListCollection);
      }
    });
  }

  private void postFail() {
    MusicApp.appHandler().post(new Runnable() {
      @Override public void run() {
        if (mCheckCancel.call()) return;
        mCallback.lrcFailure();
      }
    });
  }
}
