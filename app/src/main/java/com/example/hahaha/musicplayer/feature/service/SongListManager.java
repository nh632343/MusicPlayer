package com.example.hahaha.musicplayer.feature.service;

import android.util.SparseArray;
import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.model.enumeration.SongListType;
import com.example.hahaha.musicplayer.tools.ScanTools;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscription;

public class SongListManager {
  private static SongListManager sSongListManager;

  public static SongListManager getInstance() {
    if (sSongListManager == null) {
      sSongListManager = new SongListManager();
    }
    return sSongListManager;
  }

  private Subscription mScanSubscription;

  private SparseArray<ArrayList<Song>> mSongListMap;
  private int mCurrentSongIndex;
  private List<Song> mCurrentList;
  private int mCurrentType;

  private ArrayList<Song> temp;
  private boolean done;

  private SongListManager() {
    mSongListMap = new SparseArray<>();
    mCurrentSongIndex = -1;
    mCurrentType = -1;
  }

  public ArrayList<Song> getSongList(int type) {
    ArrayList<Song> result = mSongListMap.get(type);
    if (result != null) return result;
    Observable<ArrayList<Song>> observable = null;
    switch (type) {
      case SongListType.ALL:
        observable = ScanTools.scanAllMusic();
        break;
    }
    if (observable == null) return null;
    done = false;
    temp = null;
    observable
        .doAfterTerminate(() -> done = true)
        .subscribe(songs -> temp = songs);
    while (!done);
    if (temp == null) return null;
    mSongListMap.put(type, temp);
    return temp;
  }


  public Song setCurrentSong(int type, int index) {
    if (mCurrentType != type) {
      mCurrentList = mSongListMap.get(type);
    }
    mCurrentSongIndex = index;
    return mCurrentList.get(index);
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
