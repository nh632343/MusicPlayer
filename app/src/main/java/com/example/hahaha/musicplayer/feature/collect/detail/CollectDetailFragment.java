package com.example.hahaha.musicplayer.feature.collect.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseRecyclerAdapter;
import com.example.hahaha.musicplayer.feature.common.SongViewHolder;
import com.example.hahaha.musicplayer.feature.common.TitleRecyclerFragment;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.widget.TitleBar;
import java.util.List;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(CollectDetailPresenter.class)
public class CollectDetailFragment extends TitleRecyclerFragment<CollectDetailPresenter> {
  private static final String KEY_COLLECT_ID = "KEY_COLLECT_ID";

  public static CollectDetailFragment newInstance(long collectId) {
    CollectDetailFragment fragment = new CollectDetailFragment();
    Bundle args = new Bundle();
    args.putLong(KEY_COLLECT_ID, collectId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    getPresenter().setCollectId(
        getArguments().getLong(KEY_COLLECT_ID));
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mAdapter.bind(Song.class, SongViewHolder.class);
    mAdapter.setOnClickListener((position, view1) -> {
      getPresenter().onSongClick((Song) mAdapter.get(position), getContext());

    });
  }

  void setSongList(List<Song> songList) {
    mAdapter.addAll(songList);
  }

  void setTitle(String title) {
    mTitlebar.setTitle(title);
  }
}
