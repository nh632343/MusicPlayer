package com.example.hahaha.musicplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.hahaha.musicplayer.R;

/**
 * Created by hahaha on 9/13/16.
 */
public class Pause_PlayView extends View {
    private int viewRadius;
    private boolean state;
    private int myColor;
    private Paint myPaint;
    private Path path;

    private void getAttrs(Context context,AttributeSet attrs){
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Pause_PlayView,0,0);
        try {
            myColor=typedArray.getColor(R.styleable.Pause_PlayView_ppColor, Color.GREEN);
        }finally {
            typedArray.recycle();
        }
    }

    private void setMyPaint(){
        myPaint=new Paint();
        myPaint.setStrokeWidth(10);
        myPaint.setColor(myColor);
        myPaint.setStyle(Paint.Style.STROKE);
    }

    public Pause_PlayView(Context context) {
        super(context);
        myColor=Color.GREEN;
        setMyPaint();
        path=new Path();
        setClickable(true);
    }

    public Pause_PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context,attrs);
        setMyPaint();
        path=new Path();
        setClickable(true);
    }

    public Pause_PlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context,attrs);
        setMyPaint();
        path=new Path();
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        //viewRadius=width>height? width/2: height/2;
        //setMeasuredDimension(2*viewRadius,2*viewRadius);
        setMeasuredDimension(width,height);
        viewRadius=width/2;

        //set the path
        path.moveTo((float) (1.5*viewRadius),viewRadius);
        path.lineTo((float)(0.75*viewRadius),(float) (0.5*viewRadius));
        path.lineTo((float)(0.75*viewRadius),(float) (1.5*viewRadius));
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(viewRadius,viewRadius, (float) (0.95*viewRadius),myPaint);
        if (state){
            //true for playing
            canvas.drawLine((float)(0.8*viewRadius),(float)(0.5*viewRadius),
                    (float)(0.8*viewRadius),(float)(1.5*viewRadius),myPaint);
            canvas.drawLine((float)(1.2*viewRadius),(float)(0.5*viewRadius),
                    (float)(1.2*viewRadius),(float)(1.5*viewRadius),myPaint);

        }
        else{
            //false for pausing
            canvas.drawPath(path,myPaint);
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
