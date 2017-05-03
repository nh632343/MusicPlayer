package com.example.hahaha.musicplayer.feature.lrc.songlrc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseContentFragment;
import com.example.hahaha.musicplayer.model.entity.LrcLineInfo;
import com.example.hahaha.musicplayer.widget.ScrollLrcView;
import java.util.List;
import nucleus.factory.RequiresPresenter;
import support.ui.content.RequiresContent;

@RequiresContent()
@RequiresPresenter(LrcPresenter.class)
public class LrcFragment extends BaseContentFragment<LrcPresenter> {
  @BindView(R.id.lrc_view) ScrollLrcView mLrcView;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_lrc;
  }

  @Override public View provideContentView() {
    return mLrcView;
  }

  void setLrcLineInfoList(List<LrcLineInfo> list) {
    mContentPresenter.displayContentView();
    mLrcView.setMyLrcInfo(list);
  }

  void setTime(int time) {
    mLrcView.setTime(time);
  }
}
