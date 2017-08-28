package com.example.hahaha.musicplayer.manager.lrc;

import android.text.TextUtils;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.model.entity.api.LrcAddress;
import com.example.hahaha.musicplayer.model.entity.api.SearchLrc;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import com.example.hahaha.musicplayer.network.ApiService;
import com.example.hahaha.musicplayer.network.api.DownloadApi;
import com.example.hahaha.musicplayer.tools.LrcTools;
import com.example.hahaha.musicplayer.tools.StreamTools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Func0;

public class FindAllLrc implements Runnable {
  private String mSongName;
  private String mArtistName;
  private LrcCallback mCallback;
  private Func0<Boolean> mCheckCancel;

  public FindAllLrc(String mSongName, String mArtistName,
      LrcCallback mCallback, Func0<Boolean> mCheckCancel) {
    this.mSongName = mSongName;
    this.mArtistName = mArtistName;
    this.mCallback = mCallback;
    this.mCheckCancel = mCheckCancel;
    if (TextUtils.isEmpty(mSongName)) {
      throw new RuntimeException("songName is empty");
    }
  }

  @Override public void run() {
    if (mCheckCancel.call()) return;
    SearchLrc searchLrc = null;
    try {
      if (TextUtils.isEmpty(mArtistName)) {
        searchLrc = ApiService.createLrcApi().searchLrc(mSongName).execute().body();
      } else {
        searchLrc = ApiService.createLrcApi().searchLrc(mSongName, mArtistName).execute().body();
      }
    } catch (Exception e) {e.printStackTrace();}

    if (mCheckCancel.call()) return;
    if (searchLrc == null || searchLrc.getLrcAddressList() == null
        || searchLrc.getLrcAddressList().isEmpty()) {
      postFail();
      return;
    }

    List<List<LrcLineInfo>> result = new ArrayList<>();
    InputStream inputStream = null;
    DownloadApi downloadApi = ApiService.createDownloadApi();
    for (LrcAddress address : searchLrc.getLrcAddressList()) {
      if (mCheckCancel.call()) return;
      try {
        inputStream = downloadApi.download(address.getAddress()).execute().body().byteStream();
        inputStream = StreamTools.toCache(inputStream);
        inputStream.mark(Integer.MAX_VALUE);
        if (!LrcTools.verifyLrc(inputStream, Navigator.MAX_VERIFY_LINES)) {
          //unpass verify
          throw new Exception("unpass verify");
        }
        inputStream.reset();
        List<LrcLineInfo> toBeAdd = LrcTools.toLrcList(inputStream);
        if (toBeAdd != null) {
          result.add(toBeAdd);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        StreamTools.closeQuiet(inputStream);
        inputStream = null;
      }

    }

    if (mCheckCancel.call()) return;
    if (result.isEmpty()) {
      postFail();
      return;
    }
    final List<List<LrcLineInfo>> postResult = result;
    MusicApp.appHandler().post(() -> {
      if (mCheckCancel.call()) return;
      mCallback.lrcSuccess(postResult);
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
