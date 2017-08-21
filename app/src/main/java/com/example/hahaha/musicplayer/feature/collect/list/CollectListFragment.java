package com.example.hahaha.musicplayer.feature.collect.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.feature.collect.add.AddCollectFragment;
import com.example.hahaha.musicplayer.feature.common.CollectViewHolder;
import com.example.hahaha.musicplayer.feature.common.TitleRecyclerFragment;
import com.example.hahaha.musicplayer.model.entity.internal.Collect;
import java.util.List;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(CollectListPresenter.class)
public class CollectListFragment extends TitleRecyclerFragment<CollectListPresenter> {

  public static CollectListFragment newViewInstance() {
    CollectListFragment fragment = new CollectListFragment();
    Bundle args = new Bundle();
    args.putInt(Navigator.EXTRA_COLLECT_LIST_OPTION, Navigator.COLLECT_LIST_OPTION_VIEW);
    fragment.setArguments(args);
    return fragment;
  }

  public static CollectListFragment newAddInstance(long[] songIds) {
    CollectListFragment fragment = new CollectListFragment();
    Bundle args = new Bundle();
    args.putInt(Navigator.EXTRA_COLLECT_LIST_OPTION, Navigator.COLLECT_LIST_OPTION_ADD);
    args.putLongArray(Navigator.EXTRA_COLLECT_LIST_SONG_ID_ARRAY, songIds);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mAdapter.bind(Collect.class, CollectViewHolder.class);
    int option = getArguments().getInt(Navigator.EXTRA_COLLECT_LIST_OPTION);
    if (option == Navigator.COLLECT_LIST_OPTION_ADD) {
      initAdd(getArguments().getLongArray(Navigator.EXTRA_COLLECT_LIST_SONG_ID_ARRAY));
      return;
    }

  }

  private void initAdd(long[] songIds) {
    getPresenter().setSongIdArray(songIds);
    LayoutInflater inflater = LayoutInflater.from(getContext());
    View addView = inflater.inflate(R.layout.view_add, mTitlebar, false);
    addView.setOnClickListener(v -> {
      startFragment(AddCollectFragment.newInstance());
    });
    mTitlebar.addView(addView);
    View hintView = inflater.inflate(R.layout.view_add_to_collect_hint, mLinearContainer, false);
    mLinearContainer.addView(hintView, 1);

    mAdapter.setOnClickListener((position, view) -> {
      Collect collect = (Collect) mAdapter.get(position);
      getPresenter().addSongToCollect(collect, CollectListFragment.this);
    });

  }

  void setCollectList(List<Collect> collectList) {
    mAdapter.addAll(collectList);
  }
}
