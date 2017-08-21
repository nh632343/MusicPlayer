package com.example.hahaha.musicplayer.network.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DownloadApi {
  @GET
  Call<ResponseBody> download(@Url String url);
}
