package com.example.hahaha.musicplayer.feature.base.header;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

/**
 * 通过包含原adapter, 来添加header
 */
public class RecyclerHeaderAdapter extends RecyclerView.Adapter {
  /**
   * 监听HeaderViewHolder变化的接口
   */
  public interface HeaderViewHolderListener {
    void onHeaderHolderCreate(ViewHolder headerViewHolder);
  }

  private RecyclerView.Adapter mWrappedAdapter;
  private RecyclerHeaderViewHolderFactory mHeaderFactory;
  private HeaderViewHolderListener mHeaderViewHolderListener;

  public RecyclerHeaderAdapter(Context context, RecyclerView.Adapter wrappedAdapter) {
    this.mWrappedAdapter = wrappedAdapter;
    mHeaderFactory = new RecyclerHeaderViewHolderFactory(context);
  }

  @Override public int getItemViewType(int position) {
    int headerCount = getHeaderCount();
    //不是header, 由上一级adapter处理
    if (position >= headerCount) return mWrappedAdapter.getItemViewType(position - headerCount);
    return mHeaderFactory.getHeaderViewType(position);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (!mHeaderFactory.isHeader(viewType))
      return mWrappedAdapter.onCreateViewHolder(parent, viewType);

    ViewHolder headerViewHolder = mHeaderFactory.create(viewType, parent);
    if (mHeaderViewHolderListener != null) {
      mHeaderViewHolderListener.onHeaderHolderCreate(headerViewHolder);
    }
    return headerViewHolder;
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    int headerCount = getHeaderCount();
    //这里不绑定数据
    if (position < headerCount) return;
    //不是header, 由上一级adapter处理
    mWrappedAdapter.onBindViewHolder(holder, position - headerCount);
  }

  @Override public int getItemCount() {
    return mWrappedAdapter.getItemCount() + getHeaderCount();
  }

  public int getHeaderCount() {
    return mHeaderFactory.getHeaderCount();
  }

  public void bindHeader(Class<? extends ViewHolder> viewHolder) {
    mHeaderFactory.bind(viewHolder);
  }

  public void setHeaderViewHolderListener(HeaderViewHolderListener listener) {
    mHeaderViewHolderListener = listener;
  }

  /**
   * @param layoutPosition viewHolder的显示位置
   * @return viewHolder对应的数据,在被包裹adapter里的位置
   */
  public int getWrappedDataPosition(int layoutPosition) {
    return layoutPosition - getHeaderCount();
  }
}
