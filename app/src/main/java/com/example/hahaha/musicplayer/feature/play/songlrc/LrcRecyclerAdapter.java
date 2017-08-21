package com.example.hahaha.musicplayer.feature.play.songlrc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.example.hahaha.musicplayer.feature.base.BaseRecyclerAdapter;
import com.example.hahaha.musicplayer.feature.base.BaseViewHolder;
import java.util.List;
import support.ui.adapters.EasyViewHolder;

public class LrcRecyclerAdapter extends BaseRecyclerAdapter {
  private static final int VIEWTYPE_HEAD = 1000;
  private static final int VIEWTYPE_TAIL = 1001;

  public LrcRecyclerAdapter(Context context) {
    super(context);
  }

  @Override public int getItemCount() {
    return super.getItemCount() + 2;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) return VIEWTYPE_HEAD;
    if (position == getItemCount() - 1) return VIEWTYPE_TAIL;
    return super.getItemViewType(position - 1);
  }

  @Override public EasyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == VIEWTYPE_HEAD || viewType == VIEWTYPE_TAIL) {
      View view = new View(parent.getContext());
      RecyclerView.LayoutParams params =
          new RecyclerView.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT, (int) (parent.getMeasuredHeight() * 0.6));
      view.setLayoutParams(params);
      return new BaseViewHolder(view);
    }
    return super.onCreateViewHolder(parent, viewType);
  }

  @Override public void onBindViewHolder(EasyViewHolder holder, int position, List payloads) {
    if (position == 0 || position == getItemCount() - 1) return;
    super.onBindViewHolder(holder, position - 1, payloads);
  }


}
