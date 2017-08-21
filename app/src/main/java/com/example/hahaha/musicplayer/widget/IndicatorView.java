package com.example.hahaha.musicplayer.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.example.hahaha.musicplayer.R;

public class IndicatorView extends LinearLayout
  implements TwoSideSwipeLayout.PageChangeListener {
  private View[] viewArray;

  public IndicatorView(Context context) {
    super(context);
    init();
  }

  public IndicatorView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    viewArray = new View[3];
    LayoutInflater.from(getContext()).inflate(R.layout.indicator, this, true);
    viewArray[0] = findViewById(R.id.view_1);
    viewArray[1] = findViewById(R.id.view_2);
    viewArray[2] = findViewById(R.id.view_3);

    viewArray[1].setBackgroundResource(R.drawable.circle_white);
    viewArray[0].setBackgroundResource(R.drawable.circle_grey);
    viewArray[2].setBackgroundResource(R.drawable.circle_grey);
  }

  public void setup(TwoSideSwipeLayout twoSideSwipeLayout) {
    twoSideSwipeLayout.addPageChangeListener(this);
  }

  @Override
  public void onPageChange(@TwoSideSwipeLayout.PageType int currentPageType) {
    int whiteIndex = 0;
    if (currentPageType == TwoSideSwipeLayout.PageType.LEFT) whiteIndex = 0;
    if (currentPageType == TwoSideSwipeLayout.PageType.MIDDLE) whiteIndex = 1;
    if (currentPageType == TwoSideSwipeLayout.PageType.RIGHT) whiteIndex = 2;
    for (int i = 0; i < 3; ++i) {
      if (i == whiteIndex) viewArray[i].setBackgroundResource(R.drawable.circle_white);
      else viewArray[i].setBackgroundResource(R.drawable.circle_grey);
    }
  }
}
