package com.example.hahaha.musicplayer.feature.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.example.hahaha.musicplayer.R;

public class LoadView extends FrameLayout {
  public LoadView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.load_view, this, true);
  }
}
