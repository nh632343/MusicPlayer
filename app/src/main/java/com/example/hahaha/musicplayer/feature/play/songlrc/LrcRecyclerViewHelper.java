package com.example.hahaha.musicplayer.feature.play.songlrc;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.feature.base.BaseRecyclerAdapter;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import java.util.List;

public class LrcRecyclerViewHelper {
  private class SmoothScroll implements Runnable {
    @Override public void run() {
      RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(mToPosition);
      if (viewHolder == null) return;
      View view = viewHolder.itemView;
      mRecyclerView.smoothScrollBy(0, view.getTop() + view.getHeight() / 2 - mRecyclerView.getHeight() / 2);
    }
  }

  private RecyclerView mRecyclerView;
  private BaseRecyclerAdapter mAdapter;
  private Handler mHandler;
  private boolean mWaitDataChange = false;
  private boolean mLighting = false;
  private boolean mTouching = false;
  private int mToPosition;

  public LrcRecyclerViewHelper(RecyclerView recyclerView, LrcRecyclerAdapter adapter) {
    this.mRecyclerView = recyclerView;
    mAdapter = adapter;
    mHandler = MusicApp.appHandler();
    initScrollListener();
    initTouchListener();
  }

  private void initScrollListener() {
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState != RecyclerView.SCROLL_STATE_IDLE) return;
        mLighting = false;
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }
    });
  }

  private void initTouchListener() {
    mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) mTouching = true;
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) mTouching = false;
        return false;
      }
    });
  }

  public void highLight(int pos) {
    if (mWaitDataChange || mLighting || mTouching
        || mRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) return;
    mToPosition = pos + 1;
    mLighting = true;
    mAdapter.notifyItemRangeChanged(1, mAdapter.getItemCount() - 2, Navigator.PAYLOAD_LRC_COLOR_CHANGE);
    mHandler.post(new Runnable() {
      @Override public void run() {
        RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(mToPosition);
        if (viewHolder != null) {
          View view = viewHolder.itemView;
          mRecyclerView.smoothScrollBy(0, view.getTop() + view.getHeight() / 2 - mRecyclerView.getHeight() / 2);
          return;
        }
        mRecyclerView.scrollToPosition(mToPosition);
        mHandler.post(new SmoothScroll());
      }
    });

  }

  public void setDataList(List<LrcLineInfo> lrcList) {
    mWaitDataChange = true;
    mLighting = false;
    mAdapter.addAll(lrcList);
    mHandler.post(() -> mWaitDataChange = false);
  }
}
