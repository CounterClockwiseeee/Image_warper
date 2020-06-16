package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class RippleButton extends androidx.appcompat.widget.AppCompatImageButton {

    private static final int INVALIDATE_DURATION = 15; //每次刷新的時間間隔
    private static int DIFFUSE_GAP = 200;    //擴散半徑增量
    private static int TAP_TIMEOUT;   //判斷點擊和長按的時間

    private int viewWidth, viewHeight; //控件寬高
    private int pointX, pointY; //控件原點坐標（左上角）
    private int maxRadio; //擴散的最大半徑
    private int shaderRadio;   //擴散的半徑

    private Paint bottomPaint, colorPaint;  //畫筆:背景和水波紋
    private boolean isPushButton;  //記錄是否按鈕被按下

    private int eventX, eventY;  //觸摸位置的X,Y坐標
    private long downTime = 0;  //按下的時間

    public RippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    }

    private void initPaint() {//初始化畫筆
        colorPaint = new Paint();
        bottomPaint = new Paint();
        colorPaint.setColor(getResources().getColor(R.color.reveal_color));
        bottomPaint.setColor(getResources().getColor(R.color.bottom_color));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (downTime == 0) downTime = SystemClock.elapsedRealtime();
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                //計算最大半徑：
                countMaxRadio();
                isPushButton = true;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(SystemClock.elapsedRealtime() - downTime < TAP_TIMEOUT){
                    //DIFFUSE_GAP = 100;
                    postInvalidate();
                }else{
                    clearData();
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(!isPushButton) return; //如果按鈕沒有被按下則返回
        //繪制按下後的整個背景
        canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight, bottomPaint);
        canvas.save();
        //繪制擴散圓形背景
        canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight);
        canvas.drawCircle(eventX, eventY, shaderRadio, colorPaint);
        canvas.restore();
        if(shaderRadio < maxRadio) {
            postInvalidateDelayed(INVALIDATE_DURATION,
                    pointX, pointY, pointX + viewWidth, pointY + viewHeight);
            shaderRadio += DIFFUSE_GAP;
        }else{
            clearData();
        }
    }

    private void countMaxRadio() {//計算最大半徑
        if (viewWidth > viewHeight) {
            if (eventX < viewWidth / 2) {
                maxRadio = viewWidth - eventX;
            } else {
                maxRadio = viewWidth / 2 + eventX;
            }
        } else {
            if (eventY < viewHeight / 2) {
                maxRadio = viewHeight - eventY;
            } else {
                maxRadio = viewHeight / 2 + eventY;
            }
        }
    }

    private void clearData(){//reset
        downTime = 0;
        DIFFUSE_GAP = 200;
        isPushButton = false;
        shaderRadio = 0;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewWidth = w;
        this.viewHeight = h;
    }
}