package com.example.hahaha.musicplayer.feature.main.bar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.example.hahaha.musicplayer.R;

public class ControlBarLoadView extends FrameLayout {
  public ControlBarLoadView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
  }
}
