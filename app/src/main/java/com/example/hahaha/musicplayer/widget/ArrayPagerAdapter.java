package com.example.hahaha.musicplayer.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
import rx.functions.Func1;

public class ArrayPagerAdapter<T> extends FragmentPagerAdapter {
  private List<T> mList;
  private Func1<T, Fragment> mGetFragment;

  public ArrayPagerAdapter(FragmentManager fm, List<T> list,
      Func1<T, Fragment> getFragment) {
    super(fm);
    if (list == null || list.isEmpty()) throw new NullPointerException("list is empty");
    this.mList = list;
    this.mGetFragment = getFragment;
  }

  @Override public Fragment getItem(int position) {
    return mGetFragment.call(mList.get(position));
  }

  @Override public int getCount() {
    return mList.size();
  }

  @Override public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  public void changeData(List<T> list) {
    mList = list;
    notifyDataSetChanged();
  }
}
