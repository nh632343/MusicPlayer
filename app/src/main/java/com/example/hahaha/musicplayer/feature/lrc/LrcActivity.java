package com.example.hahaha.musicplayer.feature.lrc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseActivity;

public class LrcActivity extends BaseActivity {
  public static void start(Context context) {
    Intent intent = new Intent(context, LrcActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lrc);
  }
}
