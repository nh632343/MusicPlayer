package com.example.hahaha.musicplayer.feature.play.songlrc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.OnClick;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import com.example.hahaha.musicplayer.feature.chooselrc.ChooseLrcActivity;
import com.example.hahaha.musicplayer.widget.TwoSideSwipeLayout;

public class SwipeLrcFragment extends BaseFragment
    implements TwoSideSwipeLayout.SwipeListener {

  public static SwipeLrcFragment newInstance() {
    return new SwipeLrcFragment();
  }

  @OnClick(R.id.view_search_lrc) void searchLrc() {
    ChooseLrcActivity.start(getContext());
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_swipe_lrc;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getChildFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, LrcFragment.newAutoInstance())
        .commit();
  }

  @Override public void onSwipe(boolean isLeftSwipe, float percent) {
    if (isLeftSwipe) return;

    float alpha = Math.min(Math.max(0, percent), 1);
    getView().setAlpha(alpha);
  }
}
