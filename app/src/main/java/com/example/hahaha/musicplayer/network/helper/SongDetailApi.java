package com.example.hahaha.musicplayer.network.helper;

import android.text.TextUtils;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSong;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSongQuery;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.network.ApiService;
import com.example.hahaha.musicplayer.network.api.SongApi;
import java.io.IOException;
import java.util.ArrayList;
import rx.Observable;
import rx.Subscriber;

public class SongDetailApi {
  static class QueryOnSubscribe implements Observable.OnSubscribe<String> {
    private Song mSong;

    public QueryOnSubscribe(Song song) {
      this.mSong = song;
    }

    @Override public void call(Subscriber<? super String> subscriber) {
      SongApi songApi = ApiService.createSongApi();
      DoubanSongQuery songQuery = null;
      try {
        songQuery = songApi.querySong(mSong.getName()).execute().body();
      } catch (IOException e) {
        e.printStackTrace();
        subscriber.onError(e);
      }
      if (songQuery == null) return;
      DoubanSong result = null;
      for (DoubanSong doubanSong : songQuery.getList()) {
        if (TextUtils.equals(doubanSong.getAuthor().getName(), mSong.getArtist())) {
          result = doubanSong;
          break;
        }
      }
      if (result == null) {
        subscriber.onError(new Throwable("can not find song"));
        return;
      }
      mSong.setDoubanId(result.getId());
      mSong.setImgUrl(result.getImageUrl());
      mSong.save();
      subscriber.onNext(result.getId());
      subscriber.onCompleted();
    }
  }

  private static Observable<String> getQueryObservable(Song song) {
    return Observable.create(new QueryOnSubscribe(song));
  }

  public static Observable<DoubanSong> getSongDetail(Song song) {
    return Observable.just(song)
        .flatMap(song1 -> {
          if (! TextUtils.isEmpty(song.getDoubanId()))
            return Observable.just(song.getDoubanId());

          return getQueryObservable(song);
        })
        .flatMap(doubanId -> {
          return ApiService.createSongApi().getSongDetail(doubanId);
        });
  }
}
