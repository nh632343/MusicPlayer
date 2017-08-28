package com.example.hahaha.musicplayer.feature.play.songlrc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseContentFragment;
import com.example.hahaha.musicplayer.feature.base.BaseRecyclerAdapter;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import com.example.hahaha.musicplayer.widget.TwoSideSwipeLayout;
import java.util.ArrayList;
import java.util.List;
import nucleus.factory.RequiresPresenter;
import support.ui.content.RequiresContent;

@RequiresContent()
@RequiresPresenter(LrcPresenter.class)
public class LrcFragment extends BaseContentFragment<LrcPresenter> {
  private static final String EXTRA_IS_AUTO = "EXTRA_IS_AUTO";
  private static final String EXTRA_LRC_LIST = "EXTRA_LRC_LIST";

  public static LrcFragment newAutoInstance() {
    LrcFragment fragment = new LrcFragment();
    Bundle args = new Bundle();
    args.putBoolean(EXTRA_IS_AUTO, true);
    fragment.setArguments(args);
    return fragment;
  }

  public static LrcFragment newManualInstance(List<LrcLineInfo> lrcList) {
    LrcFragment fragment = new LrcFragment();
    Bundle args = new Bundle();
    args.putBoolean(EXTRA_IS_AUTO, false);
    ArrayList<LrcLineInfo> argList;
    if (lrcList instanceof ArrayList) {
      argList = (ArrayList<LrcLineInfo>) lrcList;
    } else {
      argList = new ArrayList<>(lrcList.size());
      for (int i = 0; i < lrcList.size(); ++i) {
        argList.add(lrcList.get(i));
      }
    }
    args.putParcelableArrayList(EXTRA_LRC_LIST, argList);
    fragment.setArguments(args);
    return fragment;
  }

  @BindView(R.id.lrc_view) RecyclerView mLrcView;
  private LrcRecyclerViewHelper mHelper;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_lrc;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LrcRecyclerAdapter adapter = new LrcRecyclerAdapter(getContext());
    adapter.bind(LrcLineInfo.class, LrcViewHolder.class);
    mLrcView.setAdapter(adapter);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    mLrcView.setLayoutManager(layoutManager);

    mHelper = new LrcRecyclerViewHelper(mLrcView, adapter);

    if (getArguments().getBoolean(EXTRA_IS_AUTO, true)) return;
    List<LrcLineInfo> lrcList = getArguments().getParcelableArrayList(EXTRA_LRC_LIST);
    setLrcLineInfoList(lrcList);
    getPresenter().setLrcList(lrcList);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mHelper = null;
  }

  @Override public View provideContentView() {
    return mLrcView;
  }

  void setLrcLineInfoList(List<LrcLineInfo> list) {
    mHelper.setDataList(list);
    mContentPresenter.displayContentView();
  }

  void highLight(int pos) {
    mHelper.highLight(pos);
  }


}
