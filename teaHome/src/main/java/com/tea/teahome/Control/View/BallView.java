package com.tea.teahome.Control.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.Random;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-04-18 19:53
 */
public class BallView extends View {
    final RandomColor randomColor = new RandomColor(); // 随机生成好看的颜色，github开源库。
    private final Random mRandom = new Random();
    private final int mCount = 5;   // 小球个数
    private final int minSpeed = 5; // 小球最小移动速度
    private final int maxSpeed = 15; // 小球最大移动速度
    public Ball[] mBalls = new Ball[this.mCount];   // 用来保存所有小球的数组
    private int maxRadius;  // 小球最大半径
    private int minRadius; // 小球最小半径
    private int mWidth = 200;
    private int mHeight = 200;


    public BallView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = View.resolveSize(this.mWidth, widthMeasureSpec);
        this.mHeight = View.resolveSize(this.mHeight, heightMeasureSpec);
        this.setMeasuredDimension(this.mWidth, this.mHeight);
        this.maxRadius = this.mWidth / 12;
        this.minRadius = this.maxRadius / 2;

        // 初始化所有球(设置颜色和画笔, 初始化移动的角度)
        for (int i = 0; i < mBalls.length; i++) {
            this.mBalls[i] = getRandomBall();
        }
    }

    private Ball getRandomBall() {
        Ball mBall = new Ball();
        // 设置画笔
        setRandomBall(mBall);
        return mBall;
    }

    private void setRandomBall(Ball ball) {
        // 设置画笔
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(randomColor.randomColor());
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(180);
        paint.setStrokeWidth(0);
        ball.paint = paint;

        // 设置速度
        final float speedX = (this.mRandom.nextInt(this.maxSpeed - this.minSpeed + 1) + 5) / 10f;
        final float speedY = (this.mRandom.nextInt(this.maxSpeed - this.minSpeed + 1) + 5) / 10f;
        ball.vx = this.mRandom.nextBoolean() ? speedX : -speedX;
        ball.vy = -speedY;

        ball.radius = mRandom.nextInt(maxRadius + 1 - minRadius) + minRadius;
        ball.cx = mRandom.nextInt(mWidth - ball.radius) + ball.radius;
        ball.cy = mHeight - ball.radius;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        final long startTime = System.currentTimeMillis();
        // 先画出所有圆
        for (int i = 0; i < this.mCount; i++) {
            final Ball ball = this.mBalls[i];
            canvas.drawCircle(ball.cx, ball.cy, ball.radius, ball.paint);
        }

        // 球碰撞边界
        for (int i = 0; i < this.mCount; i++) {
            collisionDetectingAndChangeSpeed(mBalls[i]); // 碰撞边界的计算
            mBalls[i].move(); // 移动
        }

        final long stopTime = System.currentTimeMillis();
        final long runTime = stopTime - startTime;
        // 16毫秒执行一次
        this.postInvalidateDelayed(Math.abs(runTime - 16));
    }

    // 判断球是否碰撞碰撞边界
    public void collisionDetectingAndChangeSpeed(Ball ball) {
        final int left = 0;
        final int top = 0;
        final int right = this.mWidth;
        final int bottom = this.mHeight;

        final float speedX = ball.vx;
        final float speedY = ball.vy;

        // 碰撞左右，X的速度取反。 speed的判断是防止重复检测碰撞，然后黏在墙上了=。=
        if (ball.left() <= left && speedX < 0) {
            ball.vx = -ball.vx;
        } else if (ball.top() <= top && speedY < 0) {
            setRandomBall(ball);
        } else if (ball.right() >= right && speedX > 0) {
            ball.vx = -ball.vx;
        }
    }
}
