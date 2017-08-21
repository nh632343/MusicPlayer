package com.example.hahaha.musicplayer.feature.base.header;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * 辅助类, 为RecyclerView添加Header
 */
public class RecyclerHeaderAdapterHelper {
  /**
   * 添加header信息
   */
  public static class Config {
    private List<Class<? extends ViewHolder>> mHeaderHolderTypes;

    public Config() {
      mHeaderHolderTypes = new ArrayList<>();
    }

    /**
     * 添加顺序决定了header显示顺序
     * @param headerClass
     * @return
     */
    public Config bindHeader(Class<? extends ViewHolder> headerClass) {
      mHeaderHolderTypes.add(headerClass);
      return this;
    }

    public List<Class<? extends ViewHolder>> getHeaderHolderTypes() {
      return mHeaderHolderTypes;
    }
  }


  private RecyclerHeaderAdapter mHeaderAdapter;

  public RecyclerHeaderAdapterHelper(Context context, RecyclerView recyclerView,
      @Nullable Config headerConfig) {
    RecyclerView.Adapter wrappedAdapter = recyclerView.getAdapter();
    mHeaderAdapter = new RecyclerHeaderAdapter(context, wrappedAdapter);
    //确保被包裹的adapter能notifyItem变化
    wrappedAdapter.registerAdapterDataObserver(mDataObserver);
    if (headerConfig != null) {
      bindHeaders(headerConfig);
    }
    recyclerView.setAdapter(mHeaderAdapter);
  }

  private void bindHeaders(Config recyclerHeaderConfig) {
    List<Class<? extends ViewHolder>> viewHolderTypes = recyclerHeaderConfig.getHeaderHolderTypes();
    int headerCount = viewHolderTypes.size();
    //保证顺序
    for (int i = 0; i < headerCount; ++i) {
      mHeaderAdapter.bindHeader(viewHolderTypes.get(i));
    }
  }

  public RecyclerHeaderAdapter getHeaderAdapter() {
    return mHeaderAdapter;
  }

  private final RecyclerView.AdapterDataObserver mDataObserver =
      new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
          mHeaderAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
          mHeaderAdapter.notifyItemRangeInserted(
              mHeaderAdapter.getHeaderCount() + positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
          mHeaderAdapter.notifyItemRangeChanged(
              mHeaderAdapter.getHeaderCount() + positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
          mHeaderAdapter.notifyItemRangeChanged(
              mHeaderAdapter.getHeaderCount() + positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
          mHeaderAdapter.notifyItemRangeRemoved(
              mHeaderAdapter.getHeaderCount() + positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
          int realFromPosition = mHeaderAdapter.getHeaderCount() + fromPosition;
          int realToPosition   = mHeaderAdapter.getHeaderCount() + toPosition;
          for (int index = 0; index < itemCount - 1; ++index) {
            mHeaderAdapter.notifyItemMoved(realFromPosition + index, realToPosition + index);
          }
        }
      };
}
