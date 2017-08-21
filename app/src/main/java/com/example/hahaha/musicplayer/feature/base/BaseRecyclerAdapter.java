package com.example.hahaha.musicplayer.feature.base;

import android.content.Context;
import android.view.ViewGroup;
import java.util.List;
import rx.functions.Action1;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;

public class BaseRecyclerAdapter extends EasyRecyclerAdapter {
  private Action1<EasyViewHolder> mViewHolderCreateListener;

  public BaseRecyclerAdapter(Context context) {
    super(context);
  }

  public void setmViewHolderCreateListener(Action1<EasyViewHolder> viewHolderCreateListener) {
    this.mViewHolderCreateListener = viewHolderCreateListener;
  }

  @Override public EasyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    EasyViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
    if (mViewHolderCreateListener != null) {
      mViewHolderCreateListener.call(viewHolder);
    }
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(EasyViewHolder holder, int position, List<Object> payloads) {
    if (payloads == null || payloads.isEmpty()) {
      onBindViewHolder(holder, position);
      return;
    }
    ((BaseViewHolder)holder).bindPayload(position, get(position), payloads);
  }
}
