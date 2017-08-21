package com.example.hahaha.musicplayer.app;

import com.example.hahaha.musicplayer.network.MusicOkHttpClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import okhttp3.OkHttpClient;
import org.litepal.LitePal;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import starter.kit.app.StarterApp;
import starter.kit.model.entity.Account;
import starter.kit.retrofit.Network;

public class MusicApp extends StarterApp {
  private static Retrofit sMusicRetrofit;
  private static Retrofit sLrcRetrofit;
  private static OkHttpClient sOkHttpClient;

  @Override public void onCreate() {
    super.onCreate();
    //LeakCanary.install(this);
    LitePal.initialize(this);
    Fresco.initialize(this);

    OkHttpClient okHttpClient = MusicOkHttpClient.newInstance();
    sOkHttpClient = okHttpClient;
    new Network.Builder()
        .networkDebug(true)
        .client(okHttpClient)
        .baseUrl(Constant.API_BASE_DOUBAN)
        .build();

    sMusicRetrofit = new Retrofit.Builder()
        .baseUrl(Constant.API_BASE_DOUBAN)
        .client(MusicOkHttpClient.newInstance())
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    sLrcRetrofit = new Retrofit.Builder()
                               .baseUrl(Constant.API_BASE_LRC)
                               .client(MusicOkHttpClient.newInstance())
                               .addConverterFactory(JacksonConverterFactory.create())
                               .build();
  }

  public static Retrofit getLrcRetrofit() {
    return sLrcRetrofit;
  }

  public static Retrofit getsMusicRetrofit() {
    return sMusicRetrofit;
  }

  public static OkHttpClient getOkHttpClient() {
    return sOkHttpClient;
  }

  @Override public Account provideAccount(String accountJson) {
    return null;
  }
}
