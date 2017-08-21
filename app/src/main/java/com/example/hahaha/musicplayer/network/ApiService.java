package com.example.hahaha.musicplayer.network;

import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.network.api.DownloadApi;
import com.example.hahaha.musicplayer.network.api.LrcApi;
import com.example.hahaha.musicplayer.network.api.SongApi;
import retrofit2.Retrofit;
import starter.kit.retrofit.Network;

public class ApiService {
  private static Retrofit sMusicRetrofit = MusicApp.getsMusicRetrofit();
  private static Retrofit sLrcRetrofit = MusicApp.getLrcRetrofit();

  public static LrcApi createLrcApi() {
    return sLrcRetrofit.create(LrcApi.class);
  }

  public static DownloadApi createDownloadApi() {
    return sLrcRetrofit.create(DownloadApi.class);
  }

  public static SongApi createSongApi() {
    return sMusicRetrofit.create(SongApi.class);
  }
}
