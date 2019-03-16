package com.lingxiaosuse.picture.tudimension.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by lingxiao on 2018/1/21.
 */

public class ZoomableViewpager extends ViewPager{
    /**
     * 当前子控件是否处理拖动状态
     */
    private boolean mIsIntercepted = true;

    public ZoomableViewpager(Context context) {
        super(context);
    }

    public ZoomableViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.screenHeight = h;
    }

    private int screenHeight;
    private float mDownX, mDownY;
    private int currentStatus = -1;
    private static final int STATUS_MOVING = 0;
    private static final int STATUS_NORMAL = 1;
    private static final int STATUS_BACK = 2;
    public static int BACK_DURATION = 500;
    private static final float MIN_SCALE_WEIGHT = 0.25f;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();//记录按下的位置
                break;
            case MotionEvent.ACTION_MOVE:

                int deltaY = (int) (ev.getRawY() - mDownY);//计算手指竖直移动距离
                if (deltaY <= 0 && currentStatus != STATUS_MOVING)
                    return super.onInterceptTouchEvent(ev);
                if (deltaY > 0 ) {
                    //如果往下移动，或者目前状态是缩放移动状态，那么传入移动坐标，进行对ImageView的操作
                    //setupMoving(ev.getRawX(), ev.getRawY());
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float mUpX = ev.getRawX();
                float mUpY = ev.getRawY();//记录按下的位置
                if (currentStatus != STATUS_MOVING){
                    return super.onInterceptTouchEvent(ev);
                }else {
                    return true;
                }

                /*float vY = computeYVelocity();
                if (vY >= 1500 || Math.abs(mUpY - mDownY) > screenHeight / 4) {//速度有一定快，或者竖直方向位移超过屏幕1/4，那么释放
                    //这里可以通过设置接口回调，外部Activity可以finish()；
                } else {
                    setupBack(mUpX, mUpY);
                }*/
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();//记录按下的位置
                addIntoVelocity(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                addIntoVelocity(ev);
                int deltaX = (int) (ev.getRawX() - mDownX);//计算手指水平移动距离
                int deltaY = (int) (ev.getRawY() - mDownY);//计算手指移动距离，大于0表示手指往屏幕下方移动
                if (Math.abs(deltaX) > Math.abs(deltaY)){
                    return super.onTouchEvent(ev);
                }
                if (deltaY <= 0 && currentStatus != STATUS_MOVING)
                    return super.onTouchEvent(ev);
                if (deltaY > 0 || currentStatus == STATUS_MOVING) {
                    //如果往下移动，或者目前状态是缩放移动状态，那么传入移动坐标，进行对ImageView的操作
                    setupMoving(ev.getRawX(), ev.getRawY());
                    return super.onTouchEvent(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float mUpX = ev.getRawX();
                float mUpY = ev.getRawY();//记录按下的位置
                if (currentStatus != STATUS_MOVING)
                    return super.onTouchEvent(ev);
                float vY = computeYVelocity();
                if (vY >= 1500 || Math.abs(mUpY - mDownY) > screenHeight / 5) {//速度有一定快，或者竖直方向位移超过屏幕1/4，那么释放
                    //这里可以通过设置接口回调，外部Activity可以finish()；
                    if (screenListener != null){
                        screenListener.onFinish();
                    }
                } else {
                    setupBack(mUpX, mUpY);
                }
        }
        return super.onTouchEvent(ev);
    }

    private View currentShowView = null;
    private void setupMoving(float movingX, float movingY) {
        currentShowView = this.getRootView();
        if (currentShowView == null){
            currentShowView = this;
        }
        currentStatus = STATUS_MOVING;
        float deltaX = movingX - mDownX;
        float deltaY = movingY - mDownY;
        float scale = 1f;
        float alphaPercent = 1f;
        if (deltaY > 0) {
            scale = 1 - Math.abs(deltaY) / screenHeight;
            alphaPercent = 1 - Math.abs(deltaY) / (screenHeight / 5);//这里是设置背景的透明度，我这是设置移动屏幕一半高度的距离就全透明了。
        }

        ViewHelper.setTranslationX(currentShowView, deltaX);
        ViewHelper.setTranslationY(currentShowView, deltaY);
        setupScale(scale);

        setupBackground(alphaPercent);
    }


    private void setupScale(float scale) {
        scale = Math.min(Math.max(scale, MIN_SCALE_WEIGHT), 1);//MIN_SCALE_WEIGHT是最小可缩小倍数，我这里设置的0.25f
        ViewHelper.setScaleX(currentShowView, scale);
        ViewHelper.setScaleY(currentShowView, scale);
    }

    private void setupBackground(float percent) {
        setBackgroundColor(convertPercentToBlackAlphaColor(percent));
    }

    //把0~1这透明度转换成相应的黑色背景透明度，应该有更好的方式
    private int convertPercentToBlackAlphaColor(float percent) {
        percent = Math.min(1, Math.max(0, percent));
        int intAlpha = (int) (percent * 255);
        String stringAlpha = Integer.toHexString(intAlpha).toLowerCase();
        String color = "#" + (stringAlpha.length() < 2 ? "0" : "") + stringAlpha + "000000";
        return Color.parseColor(color);
    }

    private void setupBack(final float mUpX, final float mUpY) {
        currentStatus = STATUS_BACK;
        if (mUpY != mDownY) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpY, mDownY);
            valueAnimator.setDuration(BACK_DURATION);
            valueAnimator.addUpdateListener(animation -> {
                float mY = (float) animation.getAnimatedValue();
                float percent = (mY - mDownY) / (mUpY - mDownY);
                float mX = percent * (mUpX - mDownX) + mDownX;
                setupMoving(mX, mY);
                if (mY == mDownY) {
                    mDownY = 0;
                    mDownX = 0;
                    currentStatus = STATUS_NORMAL;
                }
            });
            valueAnimator.start();
        } else if (mUpX != mDownX) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpX, mDownX);
            valueAnimator.setDuration(BACK_DURATION);
            valueAnimator.addUpdateListener(animation -> {
                float mX = (float) animation.getAnimatedValue();
                float percent = (mX - mDownX) / (mUpX - mDownX);
                float mY = percent * (mUpY - mDownY) + mDownY;
                setupMoving(mX, mY);
                if (mX == mDownX) {
                    mDownY = 0;
                    mDownX = 0;
                    currentStatus = STATUS_NORMAL;
                }
            });
            valueAnimator.start();
        } else {
            //按下点的x，y值跟松开点的x,y值一样，可以说明是点击事件。
        }
    }

    /**
     * 速度追踪，追踪手指滑动过程中的速度
     */
    private VelocityTracker mVelocityTracker = null;
    private void addIntoVelocity(MotionEvent event) {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
    }

    /**
     * 计算1秒内，竖直方向的滑动速度
     * @return
     */
    private float computeYVelocity() {
        float result = 0;
        if (mVelocityTracker != null) {
            mVelocityTracker.computeCurrentVelocity(1000);
            result = mVelocityTracker.getYVelocity();
            releaseVelocity();
        }
        return result;
    }

    /**
     * 回收内存
     */
    private void releaseVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private FinishScreenScrollListener screenListener;
    public void setFinishScreenListener(FinishScreenScrollListener listener){
        this.screenListener = listener;
    }
    public interface FinishScreenScrollListener{
        void onFinish();
    }
}
