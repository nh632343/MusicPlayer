package com.example.hahaha.musicplayer.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class TwoSideSwipeLayout extends ViewGroup {

  public interface SwipeListener {
    /**
     *
     * @param isLeftSwipe 是否左边的view在滑动
     * @param percent 在滑动的view露出的宽度 占viewGroup宽度的百分比
     */
    void onSwipe(boolean isLeftSwipe, float percent);
  }

  public interface PageChangeListener {
    void onPageChange(@PageType int currentPageType);
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({PageType.LEFT, PageType.MIDDLE, PageType.RIGHT})
  public @interface PageType {
    int LEFT = 0;
    int MIDDLE = 1;
    int RIGHT = 2;
  }

  private static class AnimInfo {
    View targetView;
    float targetTranslationX;
    @PageType int targetPageType;
  }

  private static final int INVALID_POINTER = -1;
  private static final int TOTAL_ANIMATION_TIME = 300;

  private boolean mIsFirstLayou = true;
  private int mTouchSlop;
  private @TwoSideSwipeLayout.PageType int mCurrentPageType;
  private int mWidth;

  private Fragment[] mFragmentList = new Fragment[3];
  private View mLeftView;
  private View mMiddleView;
  private View mRightView;
  private FragmentManager mFragmentManager;
  private ArrayList<SwipeListener> mSwipeListenerList;
  private ArrayList<PageChangeListener> mPageChangeListenerList;

  private VelocityTracker mVelocityTracker;
  private Scroller mScroller;
  private int mActivePointerId;
  private float mInitialX;
  private float mInitialY;
  private AnimInfo mAnimInfo;

  public TwoSideSwipeLayout(Context context) {
    super(context);
    init();
  }

  public TwoSideSwipeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TwoSideSwipeLayout(Context context, AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mTouchSlop = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
    mCurrentPageType = PageType.MIDDLE;
    mScroller = new Scroller(getContext());
  }

  public void addSwipeListener(SwipeListener listener) {
    if (mSwipeListenerList == null) {
      mSwipeListenerList = new ArrayList<>();
      mSwipeListenerList.add(listener);
      return;
    }
    if (mSwipeListenerList.contains(listener)) return;
    mSwipeListenerList.add(listener);
  }

  public void addPageChangeListener(PageChangeListener listener) {
    listener.onPageChange(mCurrentPageType);
    if (mPageChangeListenerList == null) {
      mPageChangeListenerList = new ArrayList<>();
      mPageChangeListenerList.add(listener);
      return;
    }
    if (mPageChangeListenerList.contains(listener)) return;
    mPageChangeListenerList.add(listener);
  }

  public void setFragmentManager(FragmentManager fragmentManager) {
    mFragmentManager = fragmentManager;
  }

  public void setLeftFragment(Fragment leftFragment) {
    addFragment(0, leftFragment);
  }

  public void setMiddleFragment(Fragment middleFragment) {
    addFragment(1, middleFragment);
  }

  public void setRightFragment(Fragment rightFragment) {
    addFragment(2, rightFragment);
  }

  private void addFragment(int position, Fragment fragment) {
    mFragmentList[position] = fragment;
    if (fragment instanceof SwipeListener) {
      addSwipeListener((SwipeListener) fragment);
    }
    if (fragment instanceof PageChangeListener) {
      addPageChangeListener((PageChangeListener) fragment);
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int contentWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    int contentHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
    mWidth = contentWidth;

    if (mFragmentManager == null) {
      throw new NullPointerException("you must call setFragmentManager to set FragmentManager");
    }
    if (mFragmentList[0] == null ||
        mFragmentList[1] == null ||
        mFragmentList[2] == null) {
      throw new NullPointerException("you must set 3 fragments");
    }

    if (mIsFirstLayou) {
      mIsFirstLayou = false;
      //添加3个fragment
      mFragmentManager.beginTransaction()
          .replace(getId(), mFragmentList[1], "1") //middleView放最底下
          .add(getId(), mFragmentList[0], "0")
          .add(getId(), mFragmentList[2], "2")
          .commitNowAllowingStateLoss();

      if (getChildCount() != 3) {
        throw new IllegalStateException("child view number must be 3");
      }

      mMiddleView = getChildAt(0);
      mLeftView = getChildAt(1);
      mRightView = getChildAt(2);
    }

    //每个view尺寸都是viewGroup尺寸
    for (int index = 0 ; index < 3; ++index) {
      getChildAt(index).measure(
          MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.EXACTLY),
          MeasureSpec.makeMeasureSpec(contentHeight, MeasureSpec.EXACTLY));
    }
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int leftBound = getPaddingLeft();
    int rightBound = r - l - getPaddingRight();
    int topBound = getPaddingTop();

    mLeftView.layout(leftBound - mLeftView.getMeasuredWidth(), 0,
        0,  topBound + mLeftView.getMeasuredHeight());
    mMiddleView.layout(leftBound, topBound,
        leftBound + mMiddleView.getMeasuredWidth(), topBound + mMiddleView.getMeasuredHeight());
    mRightView.layout(rightBound, topBound,
        rightBound + mRightView.getMeasuredWidth(), topBound + mRightView.getMeasuredHeight());

    //Log.d("xyz", "left: left"+String.valueOf(mLeftView.getLeft())+ "right"+String.valueOf(mLeftView.getRight()));
    //Log.d("xyz", "middle: left"+String.valueOf(mMiddleView.getLeft())+ "right"+String.valueOf(mMiddleView.getRight()));
    //Log.d("xyz", "right: left"+String.valueOf(mRightView.getLeft())+ "right"+String.valueOf(mRightView.getRight()));

  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    super.onInterceptTouchEvent(ev);
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(ev);

    int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
    if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
      resetTouch();
      return false;
    }

    //保存起点坐标
    if (action == MotionEvent.ACTION_DOWN) {
      mScroller.abortAnimation();

      mActivePointerId = ev.getPointerId(0);
      mInitialX = ev.getX();
      mInitialY = ev.getY();
      return false;
    }

    if (action == MotionEvent.ACTION_MOVE) {
      float dx = ev.getX() - mInitialX;
      float dy = ev.getY() - mInitialY;

      if (Math.abs(dx) < mTouchSlop) return false;
      if (mCurrentPageType == PageType.LEFT && dx >= 0) return false;
      if (mCurrentPageType == PageType.RIGHT && dx <= 0) return false;

      if (Math.abs(dx) * 0.5 > Math.abs(dy)) return true;
      return false;
    }
    return false;
  }

  private void resetTouch() {
    mActivePointerId = INVALID_POINTER;
    if (mVelocityTracker != null) {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(event);

    int action = event.getAction() & MotionEvent.ACTION_MASK;
    if (action == MotionEvent.ACTION_DOWN) {
      mScroller.abortAnimation();
      mActivePointerId = event.getPointerId(0);
      mInitialX = event.getX();
      mInitialY = event.getY();
      return true;
    }

    float dx = event.getX() - mInitialX;
    float dy = event.getY() - mInitialY;
    if (Math.abs(dx) < mTouchSlop) return true;
    //在leftPage向右滑 或者 在rightPage向左滑。 返回false
    if (mCurrentPageType == PageType.LEFT && dx >= 0) return false;
    if (mCurrentPageType == PageType.RIGHT && dx <= 0) return false;

    switch (action) {
      case MotionEvent.ACTION_MOVE: {
        if (Math.abs(dx) * 0.5 < Math.abs(dy)) break;
        //真正处理 随手指滑动
        handleActionMove(dx);
        return true;
      }

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL: {
        if (mAnimInfo == null) {
          mAnimInfo = new AnimInfo();
        }
        calcuAnimInfo(mAnimInfo, dx);
        scrollToTargetPage(mAnimInfo);
      }
    }

    //如果来到这里，说明不再接收事件
    resetTouch();
    return false;
  }

  private void handleActionMove(float dx) {
    if (mCurrentPageType == PageType.LEFT) {
      mLeftView.setTranslationX(mWidth + dx);
      dispatchSwipeListener(true, (mWidth + dx) / mWidth);
      return;
    }
    if (mCurrentPageType == PageType.RIGHT) {
      mRightView.setTranslationX(-mWidth + dx);
      dispatchSwipeListener(false, (mWidth - dx) / mWidth);
      return;
    }
    //currentPageType == PostType.Middle
    if (dx > 0) {
      //Log.d("xyz", "left dx"+String.valueOf(dx));
      mLeftView.setTranslationX(dx);
      dispatchSwipeListener(true, dx / mWidth);
      return;
    }
    //Log.d("xyz", "right dx"+String.valueOf(dx));
    mRightView.setTranslationX(dx);
    dispatchSwipeListener(false, -dx / mWidth);

  }

  private void dispatchSwipeListener(boolean isLeftSwipe, float percent) {
    if (mSwipeListenerList == null) return;
    for(SwipeListener listener : mSwipeListenerList) {
      listener.onSwipe(isLeftSwipe, percent);
    }
  }

  private void dispatchPageChangeListener(@PageType int currentPageType) {
    if (mPageChangeListenerList == null) return;
    for(PageChangeListener listener : mPageChangeListenerList) {
      listener.onPageChange(currentPageType);
    }
  }

  private void calcuAnimInfo(AnimInfo animInfo, float dx) {
    animInfo.targetPageType = PageType.MIDDLE;
    if (mCurrentPageType == PageType.LEFT) {
      animInfo.targetView = mLeftView;
      if (Math.abs(mLeftView.getTranslationX()) > 3 * mWidth / 4) {
        animInfo.targetPageType = PageType.LEFT;
        animInfo.targetTranslationX = mWidth;
        return;
      }
      animInfo.targetTranslationX = 0;
      return;
    }

    if (mCurrentPageType == PageType.RIGHT) {
      animInfo.targetView = mRightView;
      if (Math.abs(mRightView.getTranslationX()) > 3 * mWidth / 4) {
        animInfo.targetPageType = PageType.RIGHT;
        animInfo.targetTranslationX = -mWidth;
        return;
      }
      animInfo.targetTranslationX = 0;
      return;
    }

    //currentPageType == PostType.Middle
    if (dx > 0) {
      animInfo.targetView = mLeftView;
      if (Math.abs(mLeftView.getTranslationX()) > mWidth / 4) {
        animInfo.targetPageType = PageType.LEFT;
        animInfo.targetTranslationX = mWidth;
        return;
      }
      animInfo.targetPageType = PageType.MIDDLE;
      animInfo.targetTranslationX = 0;
      return;
    }
    //dx < 0
    animInfo.targetView = mRightView;
    if (Math.abs(mRightView.getTranslationX()) > mWidth / 4) {
      animInfo.targetPageType = PageType.RIGHT;
      animInfo.targetTranslationX = -mWidth;
      return;
    }
    animInfo.targetPageType = PageType.MIDDLE;
    animInfo.targetTranslationX = 0;
  }

  private void scrollToTargetPage(AnimInfo animInfo) {
    float deltaX = animInfo.targetTranslationX - animInfo.targetView.getTranslationX();
    mScroller.startScroll((int)animInfo.targetView.getTranslationX(), 0,
        (int)deltaX, 0, (int) (Math.abs(deltaX) * TOTAL_ANIMATION_TIME /mWidth));
    postInvalidate();
  }

  @Override public void computeScroll() {
    super.computeScroll();
    if (mAnimInfo == null) return;
    if (! mScroller.computeScrollOffset()) {
      //scroll finish
      if (mCurrentPageType == mAnimInfo.targetPageType) return;
      mCurrentPageType = mAnimInfo.targetPageType;
      dispatchPageChangeListener(mCurrentPageType);
      return;
    }
    int currX = mScroller.getCurrX();
    mAnimInfo.targetView.setTranslationX(currX);
    dispatchSwipeListener(mAnimInfo.targetView == mLeftView,
        (float)Math.abs(currX) / mWidth);
    postInvalidate();
  }
}
