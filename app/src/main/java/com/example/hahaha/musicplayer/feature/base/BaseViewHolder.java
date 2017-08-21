package com.example.hahaha.musicplayer.feature.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import java.util.List;
import support.ui.adapters.EasyViewHolder;

public class BaseViewHolder<T> extends EasyViewHolder<T> {
  public BaseViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public BaseViewHolder(Context context, ViewGroup parent, int layoutId) {
    super(context, parent, layoutId);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, T value) {}

  public void bindPayload(int position, T value, List<Object> payloads) {

  }
}
