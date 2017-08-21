package com.example.hahaha.musicplayer.feature.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.example.hahaha.musicplayer.feature.base.header.FragmentStack;
import nucleus.presenter.Presenter;
import starter.kit.app.StarterFragment;

public abstract class BaseFragment<P extends Presenter> extends StarterFragment<P> {
  public void startFragment(Fragment fragment) {
    Activity activity = getActivity();
    if (activity ==  null) return;
    if (!(activity instanceof FragmentStack)) return;
    ((FragmentStack)activity).startFragment(fragment);
  }

  public void backPress() {
    Activity activity = getActivity();
    if (activity == null) return;
    activity.onBackPressed();
  }
}
