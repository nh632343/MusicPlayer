package com.example.hahaha.musicplayer.feature.base.header;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class RecyclerHeaderViewHolderFactory {
  //支持多个Header
  //注意不要与原adapter的viewType重叠
  public static final int BASE_HEADER_VIEW_TYPE = 1000;
  public static final int MAX_HEADER_VIEW_TYPE = 1999;

  private Context mContext;
  //记录每个位置对应的ViewHolder类型
  private List<Class<? extends ViewHolder>> mHeaderViewHolderTypes;

  public RecyclerHeaderViewHolderFactory(Context context) {
    mContext = context;
    mHeaderViewHolderTypes = new ArrayList<>();
  }

  public ViewHolder create(int viewType, ViewGroup parent) {
    Class<? extends ViewHolder> headerViewHolderClass =
        mHeaderViewHolderTypes.get(viewType - BASE_HEADER_VIEW_TYPE);
    try {
      Constructor<? extends ViewHolder> constructor =
          headerViewHolderClass.getDeclaredConstructor(Context.class, ViewGroup.class);
      return constructor.newInstance(mContext, parent);
    } catch (Throwable e) {
      throw new RuntimeException(
          "Unable to create ViewHolder " + headerViewHolderClass + ". " + e.getCause().getMessage(), e);
    }
  }

  public void bind(Class<? extends ViewHolder> viewHolder) {
    mHeaderViewHolderTypes.add(viewHolder);
  }

  public int getHeaderViewType(int position) {
    return BASE_HEADER_VIEW_TYPE + position;
  }

  public boolean isHeader(int viewType) {
    return viewType >= BASE_HEADER_VIEW_TYPE && viewType <= MAX_HEADER_VIEW_TYPE;
  }

  public int getHeaderCount() {
    return mHeaderViewHolderTypes.size();
  }
}
