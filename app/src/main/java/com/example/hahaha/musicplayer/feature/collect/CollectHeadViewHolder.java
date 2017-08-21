package com.example.hahaha.musicplayer.feature.collect;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.base.BaseViewHolder;

public class CollectHeadViewHolder extends BaseViewHolder {
  @BindView(R.id.txt_collect_num) TextView mTxtCollectNum;

  public CollectHeadViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.header_collect);
  }

  void setNum(int num) {
    mTxtCollectNum.setText(
        MusicApp.appResources().getString(R.string.collect_head, num));
  }
}
