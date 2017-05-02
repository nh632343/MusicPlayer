package com.example.hahaha.musicplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.example.hahaha.musicplayer.R;

public class LrcView extends View {
  private static final int LIGHT_COLOR = 0xff99ff00;
  private static final int NORMAL_COLOR = 0x80ffffcc;
  private static final int DEFAULT_TEXT_SIZE = 30;
  private int mTextSize;
  private Paint mPaint;
  private Rect mRect;
  private LrcViewAdapter adapter;

  private void init() {
    mPaint = new Paint();
    mPaint.setColor(NORMAL_COLOR);
    mPaint.setTextSize(mTextSize);
    mPaint.setStrokeWidth(3);
    mPaint.setTextAlign(Paint.Align.CENTER);
    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mRect = new Rect();
    adapter = new LrcViewAdapter(this);
  }

  public LrcView(Context context) {
    super(context);
    mTextSize = DEFAULT_TEXT_SIZE;
    init();
  }

  public LrcView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
        R.styleable.ScrollLrcView, 0, 0);
    try {
      mTextSize = typedArray.getDimensionPixelSize(R.styleable.ScrollLrcView_LrcSize, DEFAULT_TEXT_SIZE);
    } finally {
      typedArray.recycle();
    }
    init();
  }

  public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
        R.styleable.ScrollLrcView, 0, 0);
    try {
      mTextSize = typedArray.getDimensionPixelSize(R.styleable.ScrollLrcView_LrcSize, DEFAULT_TEXT_SIZE);
    } finally {
      typedArray.recycle();
    }
    init();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(viewWidth, viewHeight);

    mPaint.getTextBounds("一二三", 0, 1, mRect);
    adapter.finishMeasure(
        viewHeight / 2, mRect.height() * 2, viewWidth / mRect.width() - 1, viewHeight / mRect.height());
  }

  @Override protected void onDraw(Canvas canvas) {


  }
}
