package com.example.hahaha.musicplayer.network.helper;

import android.text.TextUtils;
import android.util.Log;
import com.example.hahaha.musicplayer.model.entity.api.Author;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSong;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSongQuery;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.network.ApiService;
import com.example.hahaha.musicplayer.network.api.SongApi;
import java.io.IOException;
import java.util.ArrayList;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class SongDetailApi {
  static class QueryFunc implements Func1<Song, String> {
    @Override public String call(Song song) {
      if (!TextUtils.isEmpty(song.getDoubanId())) return song.getDoubanId();
      SongApi songApi = ApiService.createSongApi();
      DoubanSongQuery songQuery = null;
      try {
        songQuery = songApi.querySong(song.getName()).execute().body();
      } catch (Exception e) {
        e.printStackTrace();
        throw Exceptions.propagate(e);
      }

      DoubanSong result = null;
      String artist = song.getArtist();
      for (DoubanSong doubanSong : songQuery.getList()) {
        for (Author author : doubanSong.getAuthorList()) {
          if (TextUtils.equals(author.getName(), artist)) {
            result = doubanSong;
            break;
          }
        }
        if (result != null) break;
      }
      if (result == null) {
        Log.d("xyz", "can not find song "+song.getName());
         throw Exceptions.propagate(new Throwable("can not find suitable song"));
      }
      Log.d("xyz", "find song "+song.getName()+" doubanId = "+result.getId());
      song.setDoubanId(result.getId());
      song.setImgUrl(result.getImageUrl());
      song.save();
      return result.getId();
    }
  }

  public static Observable<DoubanSong> getSongDetail(Song song) {
    return Observable.just(song)
        .map(new QueryFunc())
        .map(doubanId -> {
          try {
            return ApiService.createSongApi().getSongDetail(doubanId).execute().body();
          } catch (Exception e) {
            e.printStackTrace();
            throw Exceptions.propagate(e);
          }
        });
  }
}
