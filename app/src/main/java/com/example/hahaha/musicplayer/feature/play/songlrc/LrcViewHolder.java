package com.example.hahaha.musicplayer.feature.play.songlrc;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.feature.base.BaseViewHolder;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import java.util.List;

public class LrcViewHolder extends BaseViewHolder<LrcLineInfo> {
  @BindView(R.id.txt_lrc) TextView mTxtLrc;

  public LrcViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_lrc);
  }

  @Override public void bindTo(int position, LrcLineInfo value) {
    mTxtLrc.setText(value.getContent());
    mTxtLrc.setTextColor(value.getColor());
  }

  @Override public void bindPayload(int position, LrcLineInfo value, List<Object> payloads) {
    if (payloads.contains(Navigator.PAYLOAD_LRC_COLOR_CHANGE)) {
      mTxtLrc.setTextColor(value.getColor());
    }
  }
}
