package com.example.hahaha.musicplayer.SelfMadeVIew;

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

import com.example.hahaha.musicplayer.Info.LrcInfo;
import com.example.hahaha.musicplayer.Info.LrcLineInfo;
import com.example.hahaha.musicplayer.MyLog;
import com.example.hahaha.musicplayer.MyTools;
import com.example.hahaha.musicplayer.R;

/**
 * Created by hahaha on 9/16/16.
 */
public class ScrollLrcView extends View {

    private static class DisplayInfo{
        final String string="博客";
        int initVerticalLine;   //时间为0时的vertial高度
        int maxVerticalLine;    //vertical最大高度
        int singleHeight;   //一行字的高度,包括内容和空隙
        int singleWidth;    //一个字的宽度
        int maxNum;         //一行最多的字数
    }

    //此线程用于当前歌词变化时,滑动过渡
    //300ms内滑动到下一句,每隔60ms刷新一次
    private class SlipThread extends Thread{
        int space;            //需要滑动总距离
        int intervalSpace;    //单次滑动距离
        int intervalTime=60;  //ms

        Thread runThread;
        MyTools.BooleanHolder stopFlag=new MyTools.BooleanHolder(true);

        public SlipThread(int space){
            this.space=space;
            intervalSpace=space/5;
        }

        public void stopRequest(){
            stopFlag.value=true;
            if (runThread!=null)
                runThread.interrupt();
        }

        @Override
        public void run() {
            runThread=Thread.currentThread();
            stopFlag.value=false;
            int i=0;
            while(!stopFlag.value && i<5){
                try {
                    Thread.sleep(intervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ++i;
                if (i!=5){  //未完成
                    Message message=new Message();
                    message.what=MyHandler.SLIP;
                    message.obj=intervalSpace;
                    myHandler.sendMessage(message);
                }
                else {  //完成
                    Message message=new Message();
                    message.what=MyHandler.COMPLETE;
                    message.obj=intervalSpace;
                    myHandler.sendMessage(message);
                }
            }
        }
    }

    private class MyHandler extends Handler{
        static final int SLIP=1;
        static final int COMPLETE=2;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case COMPLETE:
                    if (slipThread!=null){
                        slipThread.stopRequest();
                        slipThread=null;
                    }
                case SLIP:
                    vertical=vertical+(int)msg.obj;
                    invalidate();
            }

        }
    }

    private int lightColor=0xff99ff00;
    private int normalColor=0x80ffffcc;
    private int size;
    private Paint myPaint;

    private int viewHeight;
    private int viewWidth;
    private int currentNo=-1;
    private int vertical;

    private DisplayInfo displayInfo;
    private LrcInfo myLrcInfo;

    private boolean isFlying=false;
    private float initY;

    private MyHandler myHandler=new MyHandler();
    private SlipThread slipThread;


    int mumu=10;

    private void init(){
        displayInfo=new DisplayInfo();

        myPaint=new Paint();
        myPaint.setColor(normalColor);
        myPaint.setTextSize(size);
        myPaint.setStrokeWidth(3);
        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    public ScrollLrcView(Context context) {
        super(context);
        size=30;

        init();
    }

    public ScrollLrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ScrollLrcView,0,0);
        try {
           size=typedArray.getDimensionPixelSize(R.styleable.ScrollLrcView_LrcSize,30);

        }finally {
            typedArray.recycle();
        }

        init();
    }

    public ScrollLrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ScrollLrcView,0,0);
        try {
            size=typedArray.getDimensionPixelSize(R.styleable.ScrollLrcView_LrcSize,30);

        }finally {
            typedArray.recycle();
        }

        init();
    }

    //should call in OnMeasure
    private void setDisplayInfo(){
        Rect rect=new Rect();
        myPaint.getTextBounds(displayInfo.string,0,1,rect);
        displayInfo.singleHeight= (int) (rect.height()*2.2);
        displayInfo.singleWidth=rect.width();
        displayInfo.maxNum=viewWidth/displayInfo.singleWidth-1;
        displayInfo.initVerticalLine=viewHeight/2-displayInfo.singleHeight;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewHeight=MeasureSpec.getSize(heightMeasureSpec);
        viewWidth=MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(viewWidth,viewHeight);

        setDisplayInfo();
    }

    private void setLightPaint(){
        myPaint.setColor(lightColor);
    }

    private void setNormalPaint(){
        myPaint.setColor(normalColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (myLrcInfo==null)
            return;

        int halfWidth=viewWidth/2;
        int i=0;
        int temp=vertical;
        //outside the canvas don't need to draw
        int add=myLrcInfo.lrcLineInfoList.get(i).lineNum*displayInfo.singleHeight;
        while(temp+add<0){
            temp+=add;
            ++i;
            if (i==myLrcInfo.lrcLineInfoList.size())
                return;
            add=myLrcInfo.lrcLineInfoList.get(i).lineNum*displayInfo.singleHeight;
        }
        //设置normalPaint
        setNormalPaint();
        //start to draw
        while(temp<viewHeight){
            int lineNum=myLrcInfo.lrcLineInfoList.get(i).lineNum;
            String content=myLrcInfo.lrcLineInfoList.get(i).content;
            //遇到当前的歌词
            if (i==currentNo){
                setLightPaint();
            }
            for (int k=0;k<lineNum-1;++k){
                canvas.drawText(content,k*displayInfo.maxNum,(k+1)*displayInfo.maxNum,
                             halfWidth,temp+(k+1)*displayInfo.singleHeight,myPaint);
            }
            canvas.drawText(content,(lineNum-1)*displayInfo.maxNum,content.length(),
                              halfWidth,temp+lineNum*displayInfo.singleHeight,myPaint);
            temp+=lineNum*displayInfo.singleHeight;
            //改回normalPaint
            if (i==currentNo){
                setNormalPaint();
            }
            ++i;
            if (i==myLrcInfo.lrcLineInfoList.size())
                return;

        }

    }


    //设置LrcInfo后,要更新lrcLineInfo中lineNum值
    //传入null表示清空
    public void setMyLrcInfo(LrcInfo myLrcInfo) {
        if (myLrcInfo==null) {
            invalidate();
            return;

        }
        this.myLrcInfo = myLrcInfo;
        int size=myLrcInfo.lrcLineInfoList.size();
        int totalLineNum=0;
        for(int i=0;i<size;++i){
            //确定num
            LrcLineInfo lrcLineInfo=myLrcInfo.lrcLineInfoList.get(i);
            if (lrcLineInfo.content.equals(""))
                lrcLineInfo.lineNum=1;
            else if (lrcLineInfo.content.length()%displayInfo.maxNum==0){
                lrcLineInfo.lineNum=lrcLineInfo.content.length()/displayInfo.maxNum;
            }
            else{
                lrcLineInfo.lineNum=lrcLineInfo.content.length()/displayInfo.maxNum+1;
            }

            //累加总行数
            totalLineNum+=lrcLineInfo.lineNum;
        }

        //设置maxVerticalLine
        displayInfo.maxVerticalLine=displayInfo.initVerticalLine-totalLineNum*displayInfo.singleHeight;
    }

    //根据currentNo计算vertical的值
    private int calcuVerticalLine(){
        int offset=0;
        for (int i=0;i<currentNo;++i){
            offset+=myLrcInfo.lrcLineInfoList.get(i).lineNum*displayInfo.singleHeight;
        }
        return displayInfo.initVerticalLine-offset;
    }

    public void setTime(long time){
        //如果正在滑动或没有lrcInfo,不进行任何操作
        if (isFlying || myLrcInfo==null)
            return;
        int newNo= MyTools.getNoFromTime(myLrcInfo.lrcLineInfoList,currentNo,time);
        //如果currentNo没有变化,不重绘
        if (newNo==currentNo)
            return;

        //重绘
        currentNo=newNo;
        int newVertical=calcuVerticalLine();
        //刚开始不需要滑动
        if (newNo==0){
            vertical=newVertical;
            invalidate();
            return;
        }

        if (slipThread!=null){
            slipThread.stopRequest();
        }
        slipThread=new SlipThread(newVertical-vertical);
        slipThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isFlying=true;
                initY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                vertical=vertical+((int)(event.getY()-initY))/mumu;
                //判断vertical是否超出限度
                if (vertical<displayInfo.maxVerticalLine || vertical>displayInfo.initVerticalLine)
                    break;

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isFlying=false;
                break;
        }
        return true;
    }

}
