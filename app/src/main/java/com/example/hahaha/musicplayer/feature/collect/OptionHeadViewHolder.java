package com.example.hahaha.musicplayer.feature.collect;

import android.content.Context;
import android.view.ViewGroup;
import butterknife.OnClick;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseViewHolder;
import rx.functions.Action0;

public class OptionHeadViewHolder extends BaseViewHolder {
  private Action0 mClickAction;
  @OnClick(R.id.txt_local_song) void localSong() {
    mClickAction.call();
  }

  public OptionHeadViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.header_option);
  }

  public void setListener(Action0 action0) {
    mClickAction = action0;
  }
}
