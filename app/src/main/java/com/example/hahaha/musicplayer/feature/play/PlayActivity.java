package com.example.hahaha.musicplayer.feature.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseActivity;
import com.example.hahaha.musicplayer.feature.play.image.SongImageFragment;
import com.example.hahaha.musicplayer.feature.play.info.BriefInfoFragment;
import com.example.hahaha.musicplayer.feature.play.songlrc.LrcFragment;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSong;
import com.example.hahaha.musicplayer.widget.IndicatorView;
import com.example.hahaha.musicplayer.widget.TitleBar;
import com.example.hahaha.musicplayer.widget.TwoSideSwipeLayout;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(PlayPresenter.class)
public class PlayActivity extends BaseActivity<PlayPresenter> {
  public static void start(Context context) {
    Intent intent = new Intent(context, PlayActivity.class);
    context.startActivity(intent);
  }

  @BindView(R.id.titlebar) TitleBar mTitlebar;
  @BindView(R.id.swipeLayout) TwoSideSwipeLayout mSwipeLayout;
  @BindView(R.id.indicator) IndicatorView mIndicator;
  private BriefInfoFragment briefInfoFragment;
  private SongImageFragment songImageFragment;

  @OnClick(R.id.view_add_to_collect) void addToCollect() {
    getPresenter().addToCollect(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lrc);
    mTitlebar.setBackClickListener(v -> finish());

    mSwipeLayout.setFragmentManager(getSupportFragmentManager());
    briefInfoFragment = BriefInfoFragment.newInstance();
    mSwipeLayout.setLeftFragment(briefInfoFragment);
    songImageFragment = SongImageFragment.newInstance();
    mSwipeLayout.setMiddleFragment(songImageFragment);
    mSwipeLayout.setRightFragment(LrcFragment.newAutoInstance());
    mIndicator.setup(mSwipeLayout);
  }

  void setTitle(String title) {
    mTitlebar.setTitle(title);
  }

  void setDoubanInfo(DoubanSong doubanSong) {
    briefInfoFragment.setContent(doubanSong);
    songImageFragment.setContent(doubanSong);
  }
}
