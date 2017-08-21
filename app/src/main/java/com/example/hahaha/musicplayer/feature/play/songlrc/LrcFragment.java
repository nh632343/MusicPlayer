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
import java.util.List;
import nucleus.factory.RequiresPresenter;
import support.ui.content.RequiresContent;

@RequiresContent()
@RequiresPresenter(LrcPresenter.class)
public class LrcFragment extends BaseContentFragment<LrcPresenter> {
  public static LrcFragment newAutoInstance() {
    return new LrcFragment();
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
