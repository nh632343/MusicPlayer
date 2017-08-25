package com.example.hahaha.musicplayer.network.api;

import com.example.hahaha.musicplayer.model.entity.api.DoubanSong;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSongQuery;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SongApi {
  @GET("search")
  Call<DoubanSongQuery> querySong(@Query("q") String query);

  @GET("{songId}")
  Call<DoubanSong> getSongDetail(@Path("songId") String id);
}
