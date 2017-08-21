package com.example.hahaha.musicplayer.feature.common;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.base.BaseViewHolder;
import com.example.hahaha.musicplayer.model.entity.internal.Collect;

public class CollectViewHolder extends BaseViewHolder<Collect> {
  @BindView(R.id.txt_collect_name) TextView mTxtCollectName;
  @BindView(R.id.txt_num) TextView mTxtNum;

  public CollectViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_collect);
  }

  @Override public void bindTo(int position, Collect value) {
    mTxtCollectName.setText(value.getName());
    mTxtNum.setText(
        MusicApp.appResources().getString(R.string.collect_song_num, value.getSongNum())
    );
  }
}
