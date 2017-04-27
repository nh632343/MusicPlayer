package com.example.hahaha.musicplayer.feature.main.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.example.hahaha.musicplayer.R;

public class EmptyView extends FrameLayout {
  public EmptyView(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.fragment_music_list_view_empty, this, true);
    setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
  }
}
