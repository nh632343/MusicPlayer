package com.example.hahaha.musicplayer.feature.main.allsong;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.feature.play.PlayActivity;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import com.example.hahaha.musicplayer.tools.ScanTools;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllSongPresenter extends BasePresenter<AllSongFragment> {
  private static final int REQUEST_SCAN = 1000;
  private MusicManagerProxy mMusicManager;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mMusicManager = MusicManagerProxy.getInstance();
    restartableFirst(REQUEST_SCAN,
        () -> {return ScanTools.scanAllMusic()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread());},
        (allSongFragment, songs) -> allSongFragment.setSongList(songs),
        (allSongFragment, throwable) -> allSongFragment.showEmptyView());
  }

  @Override protected void onFirstTakeView(AllSongFragment allSongFragment) {
    allSongFragment.showLoadView();
    start(REQUEST_SCAN);
  }

  void onSongClick(Song song, Context context) {
    List<Song> songList = null;
    if (!song.save()) {
      Log.d("xyz", "save song fail "+song.toString());
      songList = DataSupport.where("filePath = ?", song.getFilePath()).find(Song.class);
      if (songList.isEmpty()) {
        throw new NullPointerException(
            "can not insert song, but can not find filepath = "+song.getFilePath());
      }
      song = songList.get(0);
    } else {
      songList = new ArrayList<>();
      songList.add(song);
    }

    mMusicManager.setCurrentSong(song.getId(), songList, true);
    PlayActivity.start(context);
  }
}
