package com.example.hahaha.musicplayer.feature.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import com.example.hahaha.musicplayer.feature.base.BaseRecyclerAdapter;
import com.example.hahaha.musicplayer.widget.TitleBar;
import nucleus.presenter.Presenter;

public class TitleRecyclerFragment<P extends Presenter> extends BaseFragment<P> {
  @BindView(R.id.linear) protected LinearLayout mLinearContainer;
  @BindView(R.id.titlebar) protected TitleBar mTitlebar;
  @BindView(R.id.recycler) protected RecyclerView mRecyclerView;
  protected BaseRecyclerAdapter mAdapter;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_title_recycler;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mTitlebar.setBackClickListener(v -> {
      backPress();
    });

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mAdapter = new BaseRecyclerAdapter(getContext());
    mRecyclerView.setAdapter(mAdapter);
  }
}
