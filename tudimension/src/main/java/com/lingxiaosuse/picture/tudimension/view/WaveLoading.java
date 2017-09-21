package com.lingxiaosuse.picture.tudimension.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

/**
 * Created by lingxiao on 2017/8/30.
 */

public class WaveLoading extends View{

    private int mColor;
    private String mText;

    /**
     *图形画笔
     */
    private Paint mPaint;
    /**
     *文字画笔
     */
    private Paint textPaint;
    private Path path;
    private int mWidth = UIUtils.dip2px(50);
    private int mHeight = UIUtils.dip2px(50);
    private float currentPersent;
    private int textSize = UIUtils.sp2px(getContext(), 25);

    public WaveLoading(Context context) {
        this(context,null);
    }

    public WaveLoading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //获取自定义参数
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveLoading);
        //获取颜色和文字
        mColor = typedArray.getColor(R.styleable.WaveLoading_mainColor,
                Color.rgb(41, 163, 254));
        mText = typedArray.getString(R.styleable.WaveLoading_text);
        if (TextUtils.isEmpty(mText)){
            mText = "刷";
        }
        typedArray.recycle();
        //初始化图形画笔 抗锯齿，填充，抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPaint.setDither(true);
        //初始化文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); //粗体
        //闭合波浪路径
        path = new Path();

        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.setDuration(1000);
        //添加值插器
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentPersent = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);
        textSize = mWidth / 2;
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //底部的字
        textPaint.setColor(mColor);
        drawCenterText(canvas,textPaint,mText);
        //上层的字
        textPaint.setColor(Color.WHITE);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        //裁剪成圆形
        Path o = new Path();
        o.addCircle(mWidth/2,mHeight/2,mWidth/2, Path.Direction.CCW);
        canvas.clipPath(o);
        //生成闭合曲线
        path = getActionPath(currentPersent);
        //画波浪
        canvas.drawPath(path,mPaint);
        //裁剪文字
        canvas.clipPath(path);
        drawCenterText(canvas,textPaint,mText);
        canvas.restore();
    }

    private Path getActionPath(float persent){
        Path path = new Path();
        int x = -mWidth;
        //当前x点坐标（根据动画进度水平推移，一个动画周期推移的距离为一个周期的波长）
        x+=mWidth*persent;
        //波形的起点
        path.moveTo(x,mHeight/2);
        //控制点的相对宽度
        int quadWidth = mWidth / 4;
        //控制点的相对高度
        int quadHeight = mHeight / 20*3;
        //第一个周期波形
        path.rQuadTo(quadWidth,quadHeight,quadWidth*2,0);
        path.rQuadTo(quadWidth,-quadHeight,quadWidth*2,0);
        //第二个周期波形
        path.rQuadTo(quadWidth,quadHeight,quadWidth*2,0);
        path.rQuadTo(quadWidth,-quadHeight,quadWidth*2,0);
        //右侧的直线
        path.lineTo(x+mWidth*2,mHeight);
        //下边的直线
        path.lineTo(x,mHeight);
        path.close();
        return path;
    }
    private void drawCenterText(Canvas canvas,Paint textPaint,String text){
        Rect rect = new Rect(0,0,mWidth,mHeight);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float top = metrics.top;
        float bottom = metrics.bottom;
        int centerY = (int) (rect.centerY() - top/2 - bottom/2);
        Log.i("code", "text:"+text+"  rect.centerX():"+rect.centerX()+"  centerY:"+centerY
                +"  textPaint:"+textPaint
        );
        canvas.drawText(text,rect.centerX(),centerY,textPaint);
    }
}
