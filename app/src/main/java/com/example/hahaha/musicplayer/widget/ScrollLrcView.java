package com.example.hahaha.musicplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.hahaha.musicplayer.model.entity.LrcInfo;
import com.example.hahaha.musicplayer.model.entity.LrcLineInfo;
import com.example.hahaha.musicplayer.tools.MyLog;
import com.example.hahaha.musicplayer.tools.LrcTools;
import com.example.hahaha.musicplayer.R;
import java.lang.ref.WeakReference;

/**
 * Created by hahaha on 9/16/16.
 */
public class ScrollLrcView extends View {

  private static class DisplayInfo {
    final String string = "博客";
    int initVerticalLine;   //时间为0时的vertial高度
    int maxVerticalLine;    //vertical最大高度
    int singleHeight;   //一行字的高度,包括内容和空隙
    int singleWidth;    //一个字的宽度
    int maxNum;         //一行最多的字数
  }

  //此线程用于当前歌词变化时,滑动过渡
  //300ms内滑动到下一句,每隔60ms刷新一次
  private class SlipThread extends Thread {
    int space;            //需要滑动总距离
    int intervalSpace;    //单次滑动距离
    int intervalTime = 60;  //ms

    Thread runThread;
    LrcTools.BooleanHolder stopFlag = new LrcTools.BooleanHolder(true);

    SlipThread(int space) {
      this.space = space;
      intervalSpace = space / 5;
    }

    void stopRequest() {
      stopFlag.value = true;
      if (runThread != null) {
        runThread.interrupt();
      }
    }

    @Override
    public void run() {
      runThread = Thread.currentThread();
      stopFlag.value = false;
      int i = 0;
      while (!stopFlag.value && i < 5) {
        try {
          Thread.sleep(intervalTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        ++i;
        if (i != 5) {  //未完成
          Message message = new Message();
          message.what = MyHandler.SLIP;
          message.obj = intervalSpace;
          myHandler.sendMessage(message);
        } else {  //完成
          Message message = new Message();
          message.what = MyHandler.COMPLETE;
          message.obj = intervalSpace;
          myHandler.sendMessage(message);
        }
      }
    }
  }

  private static class MyHandler extends Handler {
    static final int SLIP = 1;
    static final int COMPLETE = 2;

    WeakReference<ScrollLrcView> lrcViewWeakReference;

    MyHandler(ScrollLrcView scrollLrcView){
      lrcViewWeakReference=new WeakReference<ScrollLrcView>(scrollLrcView);
    }

    @Override
    public void handleMessage(Message msg) {
      ScrollLrcView scrollLrcView=lrcViewWeakReference.get();
      //判断scrollLrcView是否为null
      if (scrollLrcView==null)
        return;

      switch (msg.what) {
        case COMPLETE:
          //滑动完成,停止线程
          if (scrollLrcView.slipThread != null) {
            scrollLrcView.slipThread.stopRequest();
            scrollLrcView.slipThread = null;
          }
          //会继续执行下面的滑动代码
        case SLIP:
          scrollLrcView.vertical = scrollLrcView.vertical + (int) msg.obj;
          scrollLrcView.invalidate();
          break;
      }
    }
  }

  private int lightColor = 0xff99ff00;
  private int normalColor = 0x80ffffcc;
  private int size;
  private Paint myPaint;

  private int viewHeight;
  private int viewWidth;
  private int currentNo = -1;
  private int vertical;

  private DisplayInfo displayInfo;
  private LrcInfo myLrcInfo;

  private boolean isFlying = false;
  private float initY;

  private MyHandler myHandler;
  private SlipThread slipThread;

  int mumu = 20;

  private void init() {
    displayInfo = new DisplayInfo();
    myHandler=new MyHandler(this);

    myPaint = new Paint();
    myPaint.setColor(normalColor);
    myPaint.setTextSize(size);
    myPaint.setStrokeWidth(3);
    myPaint.setTextAlign(Paint.Align.CENTER);
    myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
  }

  public ScrollLrcView(Context context) {
    super(context);
    size = 30;

    init();
  }

  public ScrollLrcView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
        R.styleable.ScrollLrcView, 0, 0);
    try {
      size = typedArray.getDimensionPixelSize(R.styleable.ScrollLrcView_LrcSize, 30);
    } finally {
      typedArray.recycle();
    }

    init();
  }

  public ScrollLrcView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
        R.styleable.ScrollLrcView, 0, 0);
    try {
      size = typedArray.getDimensionPixelSize(R.styleable.ScrollLrcView_LrcSize, 30);
    } finally {
      typedArray.recycle();
    }

    init();
  }

  //should call in OnMeasure
  private void setDisplayInfo() {
    Rect rect = new Rect();
    myPaint.getTextBounds(displayInfo.string, 0, 1, rect);
    displayInfo.singleHeight = (int) (rect.height() * 2.2);
    displayInfo.singleWidth = rect.width();
    displayInfo.maxNum = viewWidth / displayInfo.singleWidth - 1;
    displayInfo.initVerticalLine = viewHeight / 2 - displayInfo.singleHeight;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(viewWidth, viewHeight);

    setDisplayInfo();
  }

  private void setLightPaint() {
    myPaint.setColor(lightColor);
  }

  private void setNormalPaint() {
    myPaint.setColor(normalColor);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (myLrcInfo == null) {
      return;
    }

    int halfWidth = viewWidth / 2;
    int i = 0;
    int temp = vertical;
    //outside the canvas don't need to draw
    int add = myLrcInfo.lrcLineInfoList.get(i).lineNum * displayInfo.singleHeight;
    while (temp + add < 0) {
      temp += add;
      ++i;
      if (i == myLrcInfo.lrcLineInfoList.size()) {
        return;
      }
      add = myLrcInfo.lrcLineInfoList.get(i).lineNum * displayInfo.singleHeight;
    }
    //设置normalPaint
    setNormalPaint();
    //start to draw
    while (temp < viewHeight) {
      int lineNum = myLrcInfo.lrcLineInfoList.get(i).lineNum;
      String content = myLrcInfo.lrcLineInfoList.get(i).content;
      //遇到当前的歌词
      if (i == currentNo) {
        setLightPaint();
      }
      for (int k = 0; k < lineNum - 1; ++k) {
        canvas.drawText(content, k * displayInfo.maxNum, (k + 1) * displayInfo.maxNum,
            halfWidth, temp + (k + 1) * displayInfo.singleHeight, myPaint);
      }
      canvas.drawText(content, (lineNum - 1) * displayInfo.maxNum, content.length(),
          halfWidth, temp + lineNum * displayInfo.singleHeight, myPaint);
      temp += lineNum * displayInfo.singleHeight;
      //改回normalPaint
      if (i == currentNo) {
        setNormalPaint();
      }
      ++i;
      if (i == myLrcInfo.lrcLineInfoList.size()) {
        return;
      }
    }
  }

  //设置LrcInfo后,要更新lrcLineInfo中lineNum值
  //传入null表示清空
  public void setMyLrcInfo(LrcInfo myLrcInfo) {
    if (myLrcInfo == null) {
      invalidate();
      return;
    }
    this.myLrcInfo = myLrcInfo;
    int size = myLrcInfo.lrcLineInfoList.size();
    int totalLineNum = 0;
    for (int i = 0; i < size; ++i) {
      //确定num
      LrcLineInfo lrcLineInfo = myLrcInfo.lrcLineInfoList.get(i);
      if (lrcLineInfo.content.equals("")) {
        lrcLineInfo.lineNum = 1;
      } else if (lrcLineInfo.content.length() % displayInfo.maxNum == 0) {
        lrcLineInfo.lineNum = lrcLineInfo.content.length() / displayInfo.maxNum;
      } else {
        lrcLineInfo.lineNum = lrcLineInfo.content.length() / displayInfo.maxNum + 1;
      }

      //累加总行数
      totalLineNum += lrcLineInfo.lineNum;
    }

    //设置maxVerticalLine
    displayInfo.maxVerticalLine =
        displayInfo.initVerticalLine - totalLineNum * displayInfo.singleHeight;
  }

  //根据currentNo计算vertical的值
  private int calcuVerticalLine() {
    int offset = 0;
    for (int i = 0; i < currentNo; ++i) {
      offset += myLrcInfo.lrcLineInfoList.get(i).lineNum * displayInfo.singleHeight;
    }
    return displayInfo.initVerticalLine - offset;
  }

  public void setTime(long time) {
    //如果正在滑动或没有lrcInfo,不进行任何操作
    if (isFlying || myLrcInfo == null) {
      return;
    }
    int newNo = LrcTools.getNoFromTime(myLrcInfo.lrcLineInfoList, currentNo, time);
    //如果currentNo没有变化,不重绘
    if (newNo == currentNo) {
      return;
    }

    //重绘
    currentNo = newNo;
    int newVertical = calcuVerticalLine();
    //刚开始不需要滑动
    if (newNo == 0) {
      vertical = newVertical;
      invalidate();
      return;
    }

    if (slipThread != null) {
      slipThread.stopRequest();
    }
    slipThread = new SlipThread(newVertical - vertical);
    slipThread.start();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        isFlying = true;
        initY = event.getY();
        MyLog.d("xyz","init_y: "+String.valueOf(initY));
        break;
      case MotionEvent.ACTION_MOVE:
        float y_get=event.getY();
        int resultVertical=vertical + ((int) (y_get - initY)) / mumu;
        MyLog.d("xyz","y_get: "+String.valueOf(y_get)+"  minus: "+String.valueOf(y_get-initY));

        //判断vertical是否超出限度
        if (resultVertical < displayInfo.maxVerticalLine || resultVertical > displayInfo.initVerticalLine) {
          break;
        }

        vertical=resultVertical;
        invalidate();
        break;
      case MotionEvent.ACTION_UP:
        isFlying = false;
        break;
    }
    return true;
  }
}