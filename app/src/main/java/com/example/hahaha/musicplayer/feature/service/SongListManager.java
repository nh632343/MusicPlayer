package com.example.hahaha.musicplayer.feature.service;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.model.enumeration.SongListType;
import com.example.hahaha.musicplayer.tools.ScanTools;
import java.util.ArrayList;
import java.util.List;

public class SongListManager {
  private static SongListManager sSongListManager;

  public static SongListManager getInstance() {
    if (sSongListManager == null) {
      sSongListManager = new SongListManager();
    }
    return sSongListManager;
  }

  private SparseArray<ArrayList<Song>> mSongListMap;
  private int mCurrentSongIndex;
  private List<Song> mCurrentList;
  private int mCurrentType;

  private SongListManager() {
    mSongListMap = new SparseArray<>();
    mCurrentSongIndex = -1;
    mCurrentType = -1;
  }

  public @Nullable ArrayList<Song> getSongList(int type) {
    ArrayList<Song> result = mSongListMap.get(type);
    if (result != null) return result;
    switch (type) {
      case SongListType.ALL:
        result =  ScanTools.scanAllMusic();
        break;
    }
    if (result == null) return null;
    mSongListMap.put(type, result);
    return result;
  }


  public Song setCurrentSong(int type, int index) {
    if (mCurrentType != type) {
      mCurrentList = mSongListMap.get(type);
    }
    mCurrentSongIndex = index;
    return mCurrentList.get(index);
  }

  @Nullable public Song getCurrentSong() {
    if (mCurrentList == null || mCurrentSongIndex == -1) return null;
    return mCurrentList.get(mCurrentSongIndex);
  }

  public Song nextSong() {
    ++mCurrentSongIndex;
    if (mCurrentSongIndex >= mCurrentList.size()) {
      mCurrentSongIndex = 0;
    }
    return mCurrentList.get(mCurrentSongIndex);
  }

  public Song prevSong() {
    --mCurrentSongIndex;
    if (mCurrentSongIndex == -1) {
      mCurrentSongIndex = mCurrentList.size() - 1;
    }
    return mCurrentList.get(mCurrentSongIndex);
  }

  public void finish() {
    sSongListManager = null;
  }
}
