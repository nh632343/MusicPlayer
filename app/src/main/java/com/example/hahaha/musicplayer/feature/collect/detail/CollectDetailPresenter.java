package com.example.hahaha.musicplayer.feature.collect.detail;

import android.content.Context;
import android.os.Bundle;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.feature.play.PlayActivity;
import com.example.hahaha.musicplayer.manager.database.DatabaseManager;
import com.example.hahaha.musicplayer.model.entity.internal.Collect;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.model.entity.internal.SongCollect;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import com.example.hahaha.musicplayer.widget.ComSubscriber;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;

public class CollectDetailPresenter extends BasePresenter<CollectDetailFragment> {
  private long mCollectId;
  private List<Song> mSongList = new ArrayList<>();
  private ComSubscriber<Long> mListener;

  void setCollectId(long id) {
    mCollectId = id;
  }

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mListener = new ComSubscriber<Long>() {
      @Override public void onChange(Long collectId) {
        if (mCollectId != collectId) return;
        getSongList();
      }
    };
  }

  @Override protected void onTakeView(CollectDetailFragment collectDetailFragment) {
    super.onTakeView(collectDetailFragment);
    DatabaseManager.getInstance().addCollectDetailListener(mListener);
  }

  @Override protected void onDropView() {
    super.onDropView();
    DatabaseManager.getInstance().removeCollectDetailListener(mListener);
  }

  @Override protected void onFirstTakeView(CollectDetailFragment collectDetailFragment) {
    getSongList();
    collectDetailFragment.setTitle(DataSupport.find(Collect.class, mCollectId).getName());
  }

  private void getSongList() {
    CollectDetailFragment fragment = getView();
    if (fragment == null) return;
    List<SongCollect> songCollectList =
        DataSupport.where("collectId = ?", String.valueOf(mCollectId)).find(SongCollect.class);
    List<Song> songList = new ArrayList<>();
    for (SongCollect songCollect : songCollectList) {
      Song song = songCollect.getSong();
      if (song == null) continue;
      songList.add(song);
    }
    mSongList = songList;
    fragment.setSongList(songList);
  }

  void onSongClick(Song song, Context context) {
    MusicManagerProxy.getInstance().setCurrentSong(song.getId(), mSongList, true);
    PlayActivity.start(context);
  }
}
