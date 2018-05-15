package com.klui.demo.modules.nestedscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.klui.demo.R;

/**
 * @author 许方镇
 * @date 2018/5/14 0014
 * 模块功能：
 */

public class SeedingSearchBarLayout extends FrameLayout implements OnGradualChangeListener {

    private float mStartLeft;
    private float mStartTop;
    private float mStartRight;
    private float mStartBottom;

    private float mEndLeft;
    private float mEndTop;
    private float mEndRight;
    private float mEndBottom;

    private float mStartStrokeWidth;
    private float mEndStrokeWidth;

    private int mStartColor;
    private int mEndColor;

    private String mSearchKey;

    private Paint mPaint;
    private RectF mRectF;

    public SeedingSearchBarLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SeedingSearchBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeedingSearchBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DensityUtil.dp2px(2));

        mRectF = new RectF(0, 0, 0, 0);
        setBackgroundColor(0xffffd5d5);
        setWillNotDraw(false);
    }

    public void setStartLayout(int l, int t, int r, int b) {
        mStartLeft = l;
        mStartTop = t;
        mStartRight = r;
        mStartBottom = b;
        layout(l, t, r, b);
        onGradualChange(0, 0, false, true);
    }

    public void setEndLayout(int l, int t, int r, int b) {
        mEndLeft = l;
        mEndTop = t;
        mEndRight = r;
        mEndBottom = b;
    }

    public void setColorAndWidth(float startStrokeWidth, float endStrokeWidth, int startColor, int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        mStartStrokeWidth = startStrokeWidth;
        mEndStrokeWidth = endStrokeWidth;

        mPaint.setStrokeWidth(mStartStrokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private float getValue(float startValue, float endValue, float percent) {
        return startValue + (endValue - startValue) * percent;
    }

    private int getIntValue(float startValue, float endValue, float percent) {
        return (int) getValue(startValue, endValue, percent);
    }

    public void setSearchKey(String searchKey) {
        mSearchKey = searchKey;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, getHeight() / 2, getHeight() / 2, mPaint);
        super.dispatchDraw(canvas);
    }

    @Override
    public void onGradualChange(int scrollY, float percent, boolean isTop, boolean isBottom) {
        layout(getIntValue(mStartLeft, mEndLeft, percent), getIntValue(mStartTop, mEndTop, percent),
                getIntValue(mStartRight, mEndRight, percent), getIntValue(mStartBottom, mEndBottom, percent));

        float strokeWidth = getValue(mStartStrokeWidth, mEndStrokeWidth, percent);
        mPaint.setStrokeWidth(strokeWidth);
        mRectF.set(strokeWidth / 2, strokeWidth / 2, getWidth() - strokeWidth, getHeight() - strokeWidth);

        invalidate();
    }
}
