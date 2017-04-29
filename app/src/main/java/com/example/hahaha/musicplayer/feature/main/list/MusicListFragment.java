package com.example.hahaha.musicplayer.feature.main.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseContentFragment;
import com.example.hahaha.musicplayer.model.entity.Song;
import java.util.List;
import nucleus.factory.RequiresPresenter;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import support.ui.content.RequiresContent;

@RequiresPresenter(MusicListPresenter.class)
@RequiresContent(loadView = ListLoadView.class, emptyView = ListEmptyView.class)
public class MusicListFragment extends BaseContentFragment<MusicListPresenter>
  implements EasyViewHolder.OnItemClickListener{

  @BindView(R.id.recycler_music_list) RecyclerView mRecyclerMusicList;

  private MusicListPresenter mPresenter;
  private EasyRecyclerAdapter mAdapter;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_music_list;
  }

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    mPresenter = getPresenter();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecyclerView();
  }

  private void initRecyclerView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerMusicList.setLayoutManager(layoutManager);

    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.bind(Song.class, SongViewHolder.class);
    mAdapter.setOnClickListener(this);
    mRecyclerMusicList.setAdapter(mAdapter);
  }

  @Override public void onItemClick(int position, View view) {
    mPresenter.play(position);
  }

  @Override public View provideContentView() {
    return mRecyclerMusicList;
  }

  void showSongList(List<Song> songList) {
    mAdapter.addAll(songList);
    mContentPresenter.displayContentView();
  }

}
