package com.example.hahaha.musicplayer.feature.main.allsong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseContentFragment;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import com.example.hahaha.musicplayer.feature.base.BaseRecyclerAdapter;
import com.example.hahaha.musicplayer.feature.common.SongViewHolder;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.widget.TitleBar;
import java.util.List;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(AllSongPresenter.class)
public class AllSongFragment extends BaseContentFragment<AllSongPresenter> {
  public static AllSongFragment newInstance() {
    return new AllSongFragment();
  }

  @BindView(R.id.titlebar) TitleBar mTitleBar;
  @BindView(R.id.container) ViewGroup mContainer;
  @BindView(R.id.recycler_all_song) RecyclerView mRecyclerView;

  private BaseRecyclerAdapter mAdapter;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_all_song;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mTitleBar.setBackClickListener(v -> getFragmentManager().popBackStack());

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mAdapter = new BaseRecyclerAdapter(getContext());
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.bind(Song.class, SongViewHolder.class);
    mAdapter.setOnClickListener(((position, view1) -> {
      getPresenter().onSongClick((Song) mAdapter.get(position), getContext());
    }));
  }

  @Override public ViewGroup provideContainer() {
    return mContainer;
  }

  @Override public View provideContentView() {
    return mRecyclerView;
  }

  void setSongList(List<Song> songList) {
    showContent();
    mAdapter.addAll(songList);
  }
}
