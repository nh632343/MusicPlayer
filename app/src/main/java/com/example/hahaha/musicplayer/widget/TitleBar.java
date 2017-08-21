package com.example.hahaha.musicplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hahaha.musicplayer.R;

public class TitleBar extends RelativeLayout {
  private View mBackView;
  private TextView mTxtTitle;

  public TitleBar(Context context) {
    super(context);
    init();
  }

  public TitleBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.titlebar, this, true);
    mBackView = view.findViewById(R.id.view_back);
    mTxtTitle = (TextView) view.findViewById(R.id.txt_title);
  }

  private void init(AttributeSet attrs) {
    init();
    TypedArray typedArray=getContext().getTheme().obtainStyledAttributes(
        attrs, R.styleable.TitleBar ,0, 0);
    try {
      String title = typedArray.getString(R.styleable.TitleBar_title);
      mTxtTitle.setText(title);
    }finally {
      typedArray.recycle();
    }
  }

  public void setTitle(@StringRes int id) {
    mTxtTitle.setText(id);
  }

  public void setTitle(String s) {
    mTxtTitle.setText(s);
  }

  public void setBackClickListener(OnClickListener listener) {
    mBackView.setOnClickListener(listener);
  }
}
