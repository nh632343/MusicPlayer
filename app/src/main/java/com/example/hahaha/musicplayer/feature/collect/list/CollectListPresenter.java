package com.example.hahaha.musicplayer.feature.collect.list;

import android.os.Bundle;
import android.util.Log;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.manager.database.DatabaseManager;
import com.example.hahaha.musicplayer.model.entity.internal.Collect;
import com.example.hahaha.musicplayer.model.entity.internal.SongCollect;
import com.example.hahaha.musicplayer.widget.ComSubscriber;
import java.util.List;
import org.litepal.crud.DataSupport;
import support.ui.utilities.ToastUtils;

public class CollectListPresenter extends BasePresenter<CollectListFragment> {
  private long[] mSongIds;
  private ComSubscriber<Object> mListener;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mListener = new ComSubscriber<Object>() {
      @Override public void onChange(Object ignore) {
        CollectListFragment fragment = getView();
        if (fragment == null) return;
        fragment.setCollectList(DataSupport.findAll(Collect.class));
      }
    };
  }

  @Override protected void onFirstTakeView(CollectListFragment collectListFragment) {
    collectListFragment.setCollectList(DataSupport.findAll(Collect.class));
  }

  @Override protected void onTakeView(CollectListFragment fragment) {
    super.onTakeView(fragment);
    DatabaseManager.getInstance().addCollectListener(mListener);
  }

  @Override protected void onDropView() {
    super.onDropView();
    DatabaseManager.getInstance().removeCollectListener(mListener);
  }

  void setSongIdArray(long[] songIds) {
    mSongIds = songIds;
  }

  void addSongToCollect(Collect collect, CollectListFragment fragment) {
    int failCount = 0;
    long collectId = collect.getId();
    for (long songId : mSongIds) {
      List<SongCollect> songCollect =
          DataSupport.where("songId = ? and collectId = ?", String.valueOf(songId), String.valueOf(collectId))
                     .find(SongCollect.class);
      if (!songCollect.isEmpty()) {
        Log.d("xyz",
            "songId = "+String.valueOf(songId)+" collect = "+String.valueOf(collectId)+" already exist");
        failCount++;
        continue;
      }
      Log.d("xyz", "songId = "+String.valueOf(songId)+" collect = "+String.valueOf(collectId)+" add");
      new SongCollect(songId, collectId).save();
    }

    String s = MusicApp.appResources().getString(
        R.string.add_result, mSongIds.length - failCount, failCount);
    ToastUtils.toast(s);

    fragment.backPress();
    DatabaseManager.getInstance().broadcastCollectDetailChange(collectId);
  }
}
