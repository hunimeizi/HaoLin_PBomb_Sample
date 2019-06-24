package com.haolin.pbomb.sample;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：haoLin_Lee on 2019/06/24 11:36
 * 邮箱：Lhaolin0304@sina.com
 * class: 粒子爆炸 View
 */
public class PBombView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;
    private float d = 3; //粒子直径
    private List<Ball> mBallList = new ArrayList<>();
    private ValueAnimator mValueAnimator;

    public PBombView(Context context) {
        this(context, null);
    }

    public PBombView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PBombView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic);
        for (int i = 0; i < mBitmap.getWidth(); i++) {
            for (int j = 0; j < mBitmap.getHeight(); j++) {
                Ball mBall = new Ball();
                mBall.color = mBitmap.getPixel(i, j);
                mBall.x = i * d + d / 2;
                mBall.y = j * d + d / 2;
                mBall.r = d / 2;

                //速度(-20,20)
                mBall.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * 20 * Math.random());
                mBall.vY = rangInt(-15, 35);
                mBall.aX = 0;
                mBall.aY = 0.98f;
                mBallList.add(mBall);
            }
        }
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateBall();
                invalidate();
            }
        });
    }

    private int rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        //在0到(max - min)范围内变化，取大于x的最小整数 再随机
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }


    private void updateBall() {
        for (Ball ball : mBallList) {
            ball.x += ball.vX;
            ball.y += ball.vY;

            ball.vX += ball.aX;
            ball.vY += ball.aY;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(300, 300);

        for (Ball ball : mBallList) {
            mPaint.setColor(ball.color);
            canvas.drawCircle(ball.x, ball.y, ball.r, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mValueAnimator.start();
        }
        return super.onTouchEvent(event);
    }
}
