package com.example.hahaha.musicplayer.service.proxy;

import android.support.annotation.Nullable;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.model.enumeration.PlayOrder;
import java.util.ArrayList;
import java.util.List;

class SongListManager {

  private int mCurrentSongIndex;
  private List<Song> mCurrentList;
  private PlayOrderHandler mOrderHandler;

  SongListManager() {
    mCurrentSongIndex = -1;
    mOrderHandler = PlayOrderHandler.getHandler(PlayOrder.NORMAL);
  }

  public Song setCurrentSong(long songId, List<Song> songList) {
    mCurrentList = songList;
    Song result = null;
    for (int i = 0; i < songList.size(); ++i) {
      if (songList.get(i).getId() == songId) {
        mCurrentSongIndex = i;
        result = songList.get(i);
      }
    }
    if (result != null) return result;
    result = songList.get(0);
    mCurrentSongIndex = 0;
    return result;
  }

  /**
   *
   * @param songId
   * @return if null, means the song don't exist
   */
  public @Nullable Song setCurrentSong(long songId) {
    Song result = null;
    for (int i = 0; i < mCurrentList.size(); ++i) {
      if (mCurrentList.get(i).getId() == songId) {
        mCurrentSongIndex = i;
        result = mCurrentList.get(i);
      }
    }
    if (result != null) return result;
    return null;
  }

  @Nullable public Song getCurrentSong() {
    if (mCurrentList == null || mCurrentSongIndex == -1) return null;
    return mCurrentList.get(mCurrentSongIndex);
  }

  public Song nextSong() {
    if (mCurrentList == null) return null;
    mCurrentSongIndex = mOrderHandler.next(mCurrentSongIndex, mCurrentList);
    return mCurrentList.get(mCurrentSongIndex);
  }

  public Song prevSong() {
    if (mCurrentList == null) return null;
    mCurrentSongIndex = mOrderHandler.prev(mCurrentSongIndex, mCurrentList);
    return mCurrentList.get(mCurrentSongIndex);
  }

  public void setPlayOrder(int playOrder) {
    mOrderHandler = PlayOrderHandler.getHandler(playOrder);
  }

  public int getPlayOrder() {
    return mOrderHandler.getOrder();
  }

  public List<Song> getCurrentList() {
    if (mCurrentList == null) return new ArrayList<>();
    return mCurrentList;
  }
}
