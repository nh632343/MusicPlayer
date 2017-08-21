package com.example.hahaha.musicplayer.feature.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import com.example.hahaha.musicplayer.feature.base.BaseRecyclerAdapter;
import com.example.hahaha.musicplayer.feature.base.header.RecyclerHeaderAdapterHelper;
import com.example.hahaha.musicplayer.feature.collect.detail.CollectDetailFragment;
import com.example.hahaha.musicplayer.feature.common.CollectViewHolder;
import com.example.hahaha.musicplayer.feature.main.allsong.AllSongFragment;
import com.example.hahaha.musicplayer.model.entity.internal.Collect;
import java.util.List;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(CollectMainPresenter.class)
public class CollectMainFragment extends BaseFragment<CollectMainPresenter> {
  @BindView(R.id.recycler_collect) RecyclerView mRecyclerCollect;

  private BaseRecyclerAdapter mAdapter;
  private RecyclerHeaderAdapterHelper mHelper;
  private CollectHeadViewHolder mCollectHeadViewHolder;
  private int mCollectNum = -1;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_collect_main;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mAdapter = new BaseRecyclerAdapter(getContext());
    mAdapter.bind(Collect.class, CollectViewHolder.class);
    mAdapter.setOnClickListener((position, view1) -> {
      long collectId = ((Collect)(mAdapter.get(position - 2))).getId();
      startFragment(CollectDetailFragment.newInstance(collectId));
    });
    mRecyclerCollect.setAdapter(mAdapter);
    mRecyclerCollect.setLayoutManager(new LinearLayoutManager(getContext()));

    RecyclerHeaderAdapterHelper.Config config = new RecyclerHeaderAdapterHelper.Config();
    config.bindHeader(OptionHeadViewHolder.class)
          .bindHeader(CollectHeadViewHolder.class);
    mHelper = new RecyclerHeaderAdapterHelper(getContext(), mRecyclerCollect, config);

    mHelper.getHeaderAdapter().setHeaderViewHolderListener(headerViewHolder -> {
      if (headerViewHolder instanceof CollectHeadViewHolder) {
        mCollectHeadViewHolder = (CollectHeadViewHolder) headerViewHolder;
        mCollectHeadViewHolder.setNum(mCollectNum);
      }
      if (headerViewHolder instanceof OptionHeadViewHolder) {
        ((OptionHeadViewHolder)headerViewHolder).setListener(() -> optionClick());
      }
    });
  }

  void setCollectNum(int num) {
    if (mCollectHeadViewHolder == null) {
      mCollectNum = num;
      return;
    }
    mCollectHeadViewHolder.setNum(num);
  }

  void setCollectList(List<Collect> collectList) {
    mAdapter.addAll(collectList);
  }

  void optionClick() {
    startFragment(AllSongFragment.newInstance());
  }
}
