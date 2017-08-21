package com.example.hahaha.musicplayer.feature.boot;

import android.app.Activity;
import android.os.Bundle;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.main.MainActivity;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import rx.functions.Action0;

public class BootActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_boot);
    MusicManagerProxy.getInstance().initAsync(new Action0() {
      @Override public void call() {
        MainActivity.start(BootActivity.this);
        finish();
      }
    });
  }

  @Override public void onBackPressed() {
    MusicManagerProxy.getInstance().exit();
    super.onBackPressed();
  }
}
