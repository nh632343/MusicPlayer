package com.example.hahaha.musicplayer.feature.play.songlrc;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.model.entity.internal.PlayStateInfo;
import com.example.hahaha.musicplayer.model.entity.internal.PositionInfo;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.manager.lrc.LrcCallback;
import com.example.hahaha.musicplayer.manager.lrc.LrcManager;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import com.example.hahaha.musicplayer.tools.LrcTools;
import com.example.hahaha.musicplayer.widget.ComSubscriber;
import java.util.List;

public class LrcPresenter extends BasePresenter<LrcFragment>
  implements LrcCallback {
  private MusicManagerProxy mMusicManager;
  private ComSubscriber<PlayStateInfo> mSongChangeListener;
  private ComSubscriber<PositionInfo> mPositionListener;
  private Uri mCurrentUri;
  private List<LrcLineInfo> mLrcList;
  private int mCurrentPos;
  private LrcManager mLrcManager;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mMusicManager = MusicManagerProxy.getInstance();
    mLrcManager = new LrcManager();
    mCurrentPos = -1;
    initSongChangeStub();
    initPositionStub();
  }

  private void initSongChangeStub() {
    mSongChangeListener = new ComSubscriber<PlayStateInfo>() {
      @Override public void onChange(PlayStateInfo playStateInfo) {
        if (playStateInfo == null) return;
        LrcFragment fragment = getView();
        if (fragment == null) return;
        Song song = playStateInfo.getSong();
        if (mCurrentUri != null && mCurrentUri.equals(song.getUri())) return;
        mCurrentUri = song.getUri();
        fragment.showLoadView();
        mLrcManager.findFirstLrc(mCurrentUri.toString(), song.getName(), LrcPresenter.this/*LrcCallback*/);
      }
    };
  }

  private void initPositionStub() {
    mPositionListener = new ComSubscriber<PositionInfo>() {
      @Override public void onChange(PositionInfo positionInfo) {
        if (mLrcList == null) return;
        LrcFragment fragment = getView();
        if (fragment == null) return;
        int pos = LrcTools.getIndexByTime(mLrcList, positionInfo.getCurrentTime());
        if (pos == mCurrentPos) return;
        if (mCurrentPos >= 0 && mCurrentPos < mLrcList.size()) {
          mLrcList.get(mCurrentPos).toNormalColor();
        }
        mLrcList.get(pos).toLightColor();
        mCurrentPos = pos;
        fragment.highLight(mCurrentPos);
      }
    };
  }

  @Override protected void onTakeView(LrcFragment lrcFragment) {
    super.onTakeView(lrcFragment);
    mMusicManager.addSongChangeListener(mSongChangeListener);
    mMusicManager.addPositionListener(mPositionListener);
  }

  @Override protected void onDropView() {
    super.onDropView();
    mMusicManager.removePositionListener(mPositionListener);
    mMusicManager.removeSongChangeListener(mSongChangeListener);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mLrcManager.close();
    mLrcManager = null;
  }

  @Override public void lrcSuccess(List<List<LrcLineInfo>> result) {
    mLrcList = result.get(0);
    for (LrcLineInfo lrcLineInfo : mLrcList) {
      lrcLineInfo.toNormalColor();
    }
    LrcFragment fragment = getView();
    if (fragment == null) return;
    fragment.setLrcLineInfoList(mLrcList);
    //log
    List<LrcLineInfo> list = result.get(0);
    for (int i = 0; i < list.size(); ++i) {
      Log.d("xyz", "time: " + String.valueOf(list.get(i).getTime()) + " " + list.get(i).getContent());
    }
  }

  @Override public void lrcFailure() {
    LrcFragment fragment = getView();
    if (fragment == null) return;
    fragment.showEmptyView();
  }
}
