package com.lingxiaosuse.picture.tudimension.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

public class ProgressDrawable extends Drawable {
    private static final String TAG = ProgressDrawable.class.getSimpleName();
    private Paint mPaint,mColorFullPaint;

    private Paint mTextPaint;
    private int mCircleColor;
    private int mWidth,mHeight;
    private float mRadius;
    private int mSpeed = 2;
    private int mProgress = 0;

    public ProgressDrawable() {
        init();
    }

    private void init() {
        mRadius =  UIUtils.dip2px(30);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        //setBackgroundColor(Color.BLUE);

        mColorFullPaint = new Paint();
        mColorFullPaint.setColor(UIUtils.getColor(R.color.pink_a200));
        mColorFullPaint.setStyle(Paint.Style.FILL);
        mColorFullPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(UIUtils.sp2px(2f));
        mTextPaint.setTextSize(UIUtils.sp2px(16f));
        mTextPaint.setTextAlign(Paint.Align.CENTER); //文字水平居中
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        Path defPath = new Path();
        defPath.addCircle(0,0,mRadius,Path.Direction.CW);
        canvas.drawPath(defPath,mPaint);
        drawInCiracle(canvas,defPath);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float destance = (fontMetrics.bottom - fontMetrics.top) /2 - fontMetrics.bottom;
        canvas.drawText(mProgress / 100 + "%",0,destance,mTextPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@NonNull ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mWidth = bounds.width();
        mHeight = bounds.height();
        invalidateSelf();
    }

    //进度
    @Override
    protected boolean onLevelChange(int level) {
        this.mProgress = level;
        Log.e(TAG, "progress: " +  mProgress/100 + "%");
        return super.onLevelChange(level);
    }


    private int progress = 0;
    private int style = 0;
    private void drawInCiracle(Canvas canvas, Path defPath) {
        Path path = new Path();
        if (progress < 2 * mRadius){
            if (style == 0){
                path.addCircle(0,mRadius,progress, Path.Direction.CW);
                mColorFullPaint.setColor(UIUtils.getColor(R.color.pink_a200));
            }else if (style == 1){
                path.addCircle(0,-mRadius,progress, Path.Direction.CW);
                mColorFullPaint.setColor(UIUtils.getColor(R.color.green_200));
            }else if (style == 2){
                path.addCircle(mRadius,0,progress, Path.Direction.CW);
                mColorFullPaint.setColor(UIUtils.getColor(R.color.teal_400));
            }else if (style == 3){
                path.addCircle(-mRadius,0,progress, Path.Direction.CW);
                mColorFullPaint.setColor(UIUtils.getColor(R.color.blue400));
                style = 0;
            }
            path.op(defPath, Path.Op.INTERSECT);
            canvas.drawPath(path,mColorFullPaint);
            //canvas.drawCircle(0,mRadius,progress,mColorFullPaint);
            progress += mSpeed;
            //Log.e(TAG, "progress: " +  progress);
        }else {
            mPaint.setColor(mColorFullPaint.getColor());
            style++;
            progress = 0;
        }
        invalidateSelf();
    }
}
