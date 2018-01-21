package com.lingxiaosuse.picture.tudimension.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

/**
 * Created by lingxiao on 2018/1/21.
 */

public class ZoomableDrawwView extends SimpleDraweeView{

    /**
     *用于处理缩放的工具类，用法与GestureDetector类似，
     * 都是通过onTouchEvent()关联相应的MotionEvent的。
     */
    private ScaleGestureDetector mScaleDetector;

    /**
     *用于识别一些特定的手势 我们只需要调用GestureDetector.onTouchEvent()，并把MotionEvent传递进去即可。
     * 对于各种手势的回调，可以通过GestureDetector中的接口OnGestureListener来完成。
     */
    private GestureDetector mGestureDetector;

    /** 和ViewPager交互相关，判断当前是否可以左移、右移  */
    boolean mLeftDragable;
    boolean mRightDragable;
    /**  是否第一次移动 */
    boolean mFirstMove=false;
    private PointF mStartPoint = new PointF();

    private float mCurrentScale = 1f;
    private Matrix mCurrentMatrix;
    private float mMidX;
    private float mMidY;
    private OnClickListener mClickListener;
    private OnLongClickListener mLongClickListener;


    private Uri mUri;
    private ViewGroup.LayoutParams layoutParams;
    public ZoomableDrawwView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public ZoomableDrawwView(Context context) {
        super(context);
        init();
    }

    public ZoomableDrawwView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomableDrawwView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){

        mCurrentMatrix = new Matrix();

        ScaleGestureDetector.OnScaleGestureListener scaleListener =
                new ScaleGestureDetector.SimpleOnScaleGestureListener(){
                    /**
                     * 缩放时。返回值代表本次缩放事件是否已被处理。如果已被处理，
                     * 那么detector就会重置缩放事件；
                     * 如果未被处理，detector会继续进行计算，修改getScaleFactor()的返回值，
                     * 直到被处理为止。因此，它常用在判断只有缩放值达到一定数值时才进行缩放。
                     */
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        float scaleFactor = detector.getScaleFactor();  //获得缩放因子

                        mCurrentScale *= scaleFactor;
                        if (mMidX == 0f){
                            mMidX = getWidth() / 2f;
                        }
                        if (mMidY == 0f){
                            mMidY = getHeight() / 2f;
                        }
                        mCurrentMatrix.postScale(scaleFactor,scaleFactor,mMidX,mMidY);
                        invalidate();

                        int width = getWidth();
                        int height = getHeight();
                        if (null != layoutParams){
                                layoutParams.width = (int) (width * scaleFactor);
                                layoutParams.height = (int) (height * scaleFactor);
                                setLayoutParams(layoutParams);
                        }
                        if (scaleFactor >1){
                            startDrag();
                        }else {
                            stopDrag();
                        }
                        return true;
                    }

                    /**
                     *缩放结束时。
                     */
                    @Override
                    public void onScaleEnd(ScaleGestureDetector detector) {
                        super.onScaleEnd(detector);

                        if (mCurrentScale <1f){
                            reset();
                        }
                        checkBorder();
                    }
                };
        mScaleDetector = new ScaleGestureDetector(getContext(),scaleListener);

        GestureDetector.SimpleOnGestureListener gestureListener =
                new GestureDetector.SimpleOnGestureListener(){

            /**
             *当确定该事件为短单击事件时执行该方法
             */
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mClickListener != null) {
                            mClickListener.onClick();
                        }
                        return true;
                    }

                    /**
                     *长按事件
                     */
                    @Override
                    public void onLongPress(MotionEvent e) {
                        if (mLongClickListener != null) {
                            mLongClickListener.onLongClick();
                        }

                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        if (mCurrentScale > 1f) {
                            mCurrentMatrix.postTranslate(-distanceX, -distanceY);
                            invalidate();
                            checkBorder();
                        }
                        return true;
                    }
                };
        mGestureDetector = new GestureDetector(getContext(),gestureListener);
    }


    /**
     *检查图片边界是否移到view以内
     * 目的是让图片边缘不要移动到view里面
     */
    private void checkBorder() {
        RectF rectF = getDisplayRect(mCurrentMatrix);
        boolean reset = false;
        float dx = 0;
        float dy = 0;

        if (rectF.left > 0) {
            dx = getLeft() - rectF.left;
            reset = true;
        }
        if (rectF.top > 0) {
            dy = getTop() - rectF.top;
            reset = true;
        }
        if (rectF.right < getRight()) {
            dx = getRight() - rectF.right;
            reset = true;
        }
        if (rectF.bottom < getHeight()) {
            dy = getHeight() - rectF.bottom;
            reset = true;
        }
        if (reset) {
            mCurrentMatrix.postTranslate(dx, dy);
            invalidate();
        }
    }

    private RectF getDisplayRect(Matrix matrix) {
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
        matrix.mapRect(rectF);
        return rectF;
    }

    private void reset() {
        mCurrentMatrix.reset();
        mCurrentScale = 1f;
        invalidate();
    }

    @Override
    public void setImageURI(Uri uri) {
        reset();
        super.setImageURI(uri);
        mUri = uri;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        reset();
        super.setImageBitmap(bm);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.concat(mCurrentMatrix);
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
        layoutParams = getLayoutParams();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        if (!mScaleDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener Listener) {
        mClickListener = Listener;
    }



    public interface OnLongClickListener {
        void onLongClick();
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mLongClickListener = listener;
    }

    /*
    DraweeHolder mDraweeHolder;
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDraweeHolder.onDetach();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        mDraweeHolder.onDetach();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDraweeHolder.onAttach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        mDraweeHolder.onAttach();
    }*/

    private OnMovingListener movingListener;
    public interface OnMovingListener{
        void  startDrag();
        void  stopDrag();
    }

    public void setOnMovingListener(OnMovingListener listener){
        movingListener = listener;
    }

    /**
     *   子控件开始进入移动状态，令ViewPager无法拦截对子控件的Touch事件
     */
    private void startDrag(){
        if(movingListener!=null) movingListener.startDrag();

    }
    /**
     *   子控件开始停止移动状态，ViewPager将拦截对子控件的Touch事件
     */
    private void stopDrag(){
        if(movingListener!=null) movingListener.stopDrag();
    }


}
