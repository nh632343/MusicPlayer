package com.example.hahaha.musicplayer.network.api;

import com.example.hahaha.musicplayer.model.entity.api.SearchLrc;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface LrcApi {
  @GET("lyric/{songName}")
  Call<SearchLrc> searchLrc(@Path("songName") String songName);
}
