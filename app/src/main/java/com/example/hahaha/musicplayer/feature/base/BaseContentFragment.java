package com.example.hahaha.musicplayer.feature.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import nucleus.presenter.Presenter;
import starter.kit.app.StarterFragment;
import support.ui.content.ContentPresenter;
import support.ui.content.ReflectionContentPresenterFactory;

public abstract class BaseContentFragment<P extends Presenter> extends StarterFragment<P> {
  private ReflectionContentPresenterFactory factory =
      ReflectionContentPresenterFactory.fromViewClass(getClass());
  protected ContentPresenter mContentPresenter;

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    mContentPresenter = factory.createContentPresenter();
    mContentPresenter.onCreate(getContext());
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override public void onResume() {
    super.onResume();
    mContentPresenter.attachContainer(provideContainer());
    mContentPresenter.attachContentView(provideContentView());
  }

  @Override public void onPause() {
    super.onPause();
    mContentPresenter.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mContentPresenter.onDestroy();
    mContentPresenter = null;
  }

  public ViewGroup provideContainer() {
    return (ViewGroup) getView();
  }

  public abstract View provideContentView();

  public void showEmptyView() {
    mContentPresenter.displayEmptyView();
  }

  public void showLoadView() {
    mContentPresenter.displayLoadView();
  }

  public void showContent() {
    mContentPresenter.displayContentView();
  }
}
