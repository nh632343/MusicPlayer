package com.example.hahaha.musicplayer.feature.base;

import android.os.Bundle;
import nucleus.presenter.RxPresenter;

public class BasePresenter<View> extends RxPresenter<View> {
  private boolean mIsFirst;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mIsFirst = true;
  }

  @Override public void takeView(View view) {
    super.takeView(view);
    if (!mIsFirst) return;
    mIsFirst = false;
    onFirstTakeView(view);
  }

  protected void onFirstTakeView(View view) {

  }
}
