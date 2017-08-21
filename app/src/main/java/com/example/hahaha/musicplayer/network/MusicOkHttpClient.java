package com.example.hahaha.musicplayer.network;

import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import starter.kit.retrofit.DefaultHeaderInterceptor;
import support.ui.app.SupportApp;
import support.ui.utilities.AppInfo;

public class MusicOkHttpClient {

  public static OkHttpClient newInstance() {

    Interceptor interceptor = new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control",
                String.format(Locale.CHINA, "max-age=%d", 60))
            .build();
      }
    };

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
        new HttpLoggingInterceptor.Logger() {
          @Override public void log(String message) {
            Log.d("retrofit", message);
          }
        });

    HttpLoggingInterceptor.Level level =
        HttpLoggingInterceptor.Level.BODY;
    loggingInterceptor.setLevel(level);

    return new OkHttpClient.Builder().addInterceptor(new DefaultHeaderInterceptor(defaultHeader()))
        .addInterceptor(loggingInterceptor)
        .addInterceptor(interceptor)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build();
  }

  private static Headers.Builder defaultHeader() {
    final AppInfo appInfo = SupportApp.appInfo();
    Headers.Builder builder = new Headers.Builder();
    builder.add("Content-Encoding", "gzip")
        .add("X-Client-Build", String.valueOf(appInfo.versionCode))
        .add("X-Client-Version", appInfo.version)
        .add("X-Client", appInfo.deviceId)
        .add("X-Language-Code", appInfo.languageCode)
        .add("X-Client-Type", "android");

    final String channel = appInfo.channel;
    if (!TextUtils.isEmpty(channel)) {
      builder.add("X-Client-Channel", channel);
    }
    return builder;
  }
}