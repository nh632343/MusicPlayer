package com.example.hahaha.musicplayer.feature.chooselrc;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.manager.lrc.LrcCallback;
import com.example.hahaha.musicplayer.manager.lrc.LrcManager;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import java.util.List;
import support.ui.utilities.ToastUtils;

public class ChooseLrcPresenter extends BasePresenter<ChooseLrcActivity> {
  private LrcManager mLrcManager;
  private LrcCallback mCallback;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mLrcManager = new LrcManager();
    mCallback = new LrcCallback() {
      @Override public void lrcSuccess(List<List<LrcLineInfo>> result) {
        ChooseLrcActivity activity = getView();
        if (activity == null) return;
        activity.showContent(result);
      }

      @Override public void lrcFailure() {
        ChooseLrcActivity activity = getView();
        if (activity == null) return;
        activity.showFail();
      }
    };
  }

  void search(String songName, String artistName, ChooseLrcActivity activity) {
    if (TextUtils.isEmpty(songName)) {
      ToastUtils.toast("songName is empty");
      return;
    }
    activity.showLoading();
    mLrcManager.findAllLrc(songName, artistName, mCallback);
  }
}
