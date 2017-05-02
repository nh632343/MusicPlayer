package com.example.hahaha.musicplayer.widget;

import android.text.TextUtils;
import com.example.hahaha.musicplayer.model.entity.LrcLineInfo;
import com.example.hahaha.musicplayer.tools.LrcTools;
import java.util.List;

public class LrcViewAdapter {
  private List<LrcLineInfo> mLineInfoList;
  private int[] mLineNumList;
  private int mCurrentIndex= -1;
  private int mCurrentVertical;
  private LrcView mLrcView;

  private int mInitVertical;
  private int mSingleHeight;
  private int mLineMaxNum;
  private int mMaxLines;
  private int mMaxVertical;

  private boolean mIsSliping = false;

  LrcViewAdapter(LrcView lrcView) {
    mLrcView = lrcView;
  }

  void finishMeasure(int initVertical, int singleHeight, int lineMaxNum, int maxLines) {
    mInitVertical = initVertical;
    mSingleHeight = singleHeight;
    mLineMaxNum = lineMaxNum;
    mMaxLines = maxLines;
  }

  public void setLineInfoList(List<LrcLineInfo> list) {
    if (list == null) {
      return;
    }
    mLineInfoList = list;
    int length = list.size();
    mLineNumList = new int[length];
    int totalLineNum = 0;
    for (int i = 0; i < length; ++i) {
      //计算每一句歌词战的行数
      String content = mLineInfoList.get(i).content;
      if (TextUtils.isEmpty(content)) {
        mLineNumList[i] = 1;
      } else if (content.length() % mLineMaxNum == 0) {
        mLineNumList[i] = content.length() / mLineMaxNum;
      } else {
        mLineNumList[i] = content.length() / mLineMaxNum + 1;
      }
      totalLineNum += mLineNumList[i];
    }
    //计算最大上升高度
    mMaxVertical = mInitVertical - totalLineNum * mSingleHeight;
  }

  public void setTime(long time) {
    //如果正在滑动或没有lrcInfo,不进行任何操作
    if (mIsSliping || mLineInfoList == null) {
      return;
    }
    int newIndex = LrcTools.getNoFromTime(mLineInfoList, mCurrentIndex, time);
    //如果currentNo没有变化,不重绘
    if (newIndex == mCurrentIndex) return;
    mCurrentIndex = newIndex;
    calcuNewVertical();

  }

  private void calcuNewVertical() {
    int offset = 0;
    for (int i = 0; i < mCurrentIndex; ++i) {
      offset += mLineNumList[i] * mSingleHeight;
    }
    mCurrentVertical = mInitVertical - offset;
  }

}
