package com.example.hahaha.musicplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.hahaha.musicplayer.R;

/**
 * Created by hahaha on 9/13/16.
 */
public class LoopView extends View {
    private int viewWidth;
    private int viewHeight;
    private boolean state;
    private RectF rectF;
    private int myColor;
    private Paint myPaint;

    private void getAttrs(Context context,AttributeSet attrs){
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LoopView,0,0);
        try {
            myColor=typedArray.getColor(R.styleable.LoopView_LoopColor, Color.GREEN);
        }finally {
            typedArray.recycle();
        }
    }

    private void setMyPaint(){
        myPaint=new Paint();
        myPaint.setStrokeWidth(5);
        myPaint.setColor(myColor);
        myPaint.setStyle(Paint.Style.STROKE);
    }

    public LoopView(Context context) {
        super(context);
        myColor=Color.GREEN;
        setMyPaint();
        rectF=new RectF();
        setClickable(true);
    }

    public LoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context,attrs);
        setMyPaint();
        rectF=new RectF();
        setClickable(true);
    }

    public LoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context,attrs);
        setMyPaint();
        rectF=new RectF();
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth=MeasureSpec.getSize(widthMeasureSpec);
        viewHeight=MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth,viewHeight);

        //init rectF
        rectF.left= (float) (0.1*viewWidth);
        rectF.right= (float) (0.9*viewWidth);
        rectF.top= (float) (0.3*viewHeight);
        rectF.bottom= (float) (0.9*viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(rectF,rectF.height()/3,rectF.height()/3,myPaint);
        canvas.drawLine(viewWidth/2,rectF.top,viewWidth/2-rectF.top,0,myPaint);
        canvas.drawLine(viewWidth/2,rectF.top,viewWidth/2-rectF.top,  rectF.top*2,myPaint);
        //if no loop ,draw cross line
        if (!state){
            canvas.drawLine(0,viewHeight,viewWidth,0,myPaint);
        }
    }

    @Override
    public boolean performClick() {
        state = !state;
        invalidate();
        return super.performClick();
    }

   public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
        invalidate();
    }
}
