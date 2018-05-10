package com.klui.refresh.header;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import com.klui.R;
import com.klui.refresh.api.RefreshHeader;
import com.klui.refresh.api.RefreshLayout;
import com.klui.refresh.constant.RefreshState;
import com.klui.refresh.constant.SpinnerStyle;
import com.klui.refresh.internal.InternalAbstract;
import com.klui.refresh.util.DensityUtil;

import java.util.logging.Handler;

/**
 * @author 许方镇
 * @date 2018/5/8 0008
 * 模块功能：无尾熊下拉刷新头部动画
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class KoalaBearHeader extends InternalAbstract implements RefreshHeader {

    //<editor-fold desc="属性字段">

    private static final long ANIM_TIME = 1500L;

    protected boolean mIsReleased;
    protected boolean mEnableHorizontalDrag = false;

    protected Path mPath;
    protected Paint mPaint;
    protected RectF mRectF = new RectF(0, 0, 0, 0);

    protected ValueAnimator mAnimatorRelease;

    protected int mMinimumHeight;

    protected int mEarRadius;
    protected float mReleaseRate;
    protected float mEarTransRate;

    protected int mHeaderRadius;
    protected int mHeaderTransRate;

    protected int mScaleRate;
    protected int mBottomInterval;
    //</editor-fold>

    //<editor-fold desc="FrameLayout">
    public KoalaBearHeader(Context context) {
        this(context, null);
    }

    public KoalaBearHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KoalaBearHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSpinnerStyle = SpinnerStyle.Scale;
        final View thisView = this;
        final DensityUtil density = new DensityUtil();

        mMinimumHeight = density.dip2px(130);
        thisView.setMinimumHeight(mMinimumHeight);
        mBottomInterval = mMinimumHeight / 2;
        mEarRadius = density.dip2px(10);
        mEarTransRate = 1.5f * mEarRadius / mBottomInterval;

        mScaleRate = mBottomInterval / mEarRadius;
        mHeaderRadius = density.dip2px(25);
        mHeaderTransRate = density.dip2px(50);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.KoalaBearHeader);

        mEnableHorizontalDrag =
                ta.getBoolean(R.styleable.KoalaBearHeader_srlEnableHorizontalDrag, mEnableHorizontalDrag);

        ta.recycle();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimatorRelease != null) {
            mAnimatorRelease.end();
            mAnimatorRelease = null;
        }
    }

    //</editor-fold>

    //<editor-fold desc="绘制方法 - draw">
    @Override
    protected void dispatchDraw(Canvas canvas) {
        final View thisView = this;
        final int width = thisView.getWidth();
        final int height = thisView.getHeight();
        final int y = !mIsReleased ? mBottomInterval : Math.min(height, mMinimumHeight) - mBottomInterval;
        //耳朵
        mPaint.setColor(getResources().getColor(R.color.red));
        float earRadius = Math.min(mEarRadius, y / mScaleRate) * (1 - mReleaseRate);
        float centerX = width * 0.5f;
        float leftEarCx = centerX - y * mEarTransRate - mReleaseRate * mEarRadius * 2;
        float rightEarCx = centerX + y * mEarTransRate + mReleaseRate * mEarRadius * 2;
        float earCy = height - mBottomInterval;
        canvas.drawCircle(leftEarCx, earCy, earRadius, mPaint);
        canvas.drawCircle(rightEarCx, earCy, earRadius, mPaint);
        //头部
        float headerRadius = earRadius * (1.4f - mReleaseRate * 0.4f);
        float headerCy = earCy + headerRadius * 0.3f * (1 - mReleaseRate);
        mPath.reset();
        float maxX = 1.1f;
        float maxXRate = Math.max(maxX - mReleaseRate * 0.8f, 1);
        mPath.moveTo(centerX, headerCy + headerRadius);
        mPath.cubicTo(centerX + headerRadius * maxX * 0.5f, headerCy + headerRadius,
                centerX + headerRadius * maxXRate, headerCy + headerRadius * 0.5f,
                centerX + headerRadius * maxXRate, headerCy);
        mPath.cubicTo(centerX + headerRadius * maxXRate, headerCy - headerRadius * 0.5f,
                centerX + headerRadius * maxX * 0.5f, headerCy - headerRadius,
                centerX, headerCy - headerRadius);
        mPath.cubicTo(centerX - headerRadius * maxX * 0.5f, headerCy - headerRadius,
                centerX - headerRadius * maxXRate, headerCy - headerRadius * 0.5f,
                centerX - headerRadius * maxXRate, headerCy);
        mPath.cubicTo(centerX - headerRadius * maxXRate, headerCy + headerRadius * 0.5f,
                centerX - headerRadius * maxX * 0.5f, headerCy + headerRadius,
                centerX, headerCy + headerRadius);
        canvas.drawPath(mPath, mPaint);
        canvas.save();
        //眼睛、鼻子
        if (mReleaseRate < 0.5f) {
            canvas.rotate(Math.max(0, mReleaseRate * 40), centerX - headerRadius, headerCy + headerRadius);
            float scale = 1 - mReleaseRate * 2;
            float k = earRadius * earRadius * scale;
            float noseWidthRadius = k * 0.3f / mEarRadius;
            float noseHeight = k * 1.1f / mEarRadius;
            float eyesRadius = k * 0.165f / mEarRadius;
            mPaint.setColor(getResources().getColor(R.color.black_171717));
            mRectF.set(centerX - noseWidthRadius, earCy, centerX + noseWidthRadius, earCy + noseHeight);
            canvas.drawRoundRect(mRectF, noseWidthRadius, noseWidthRadius, mPaint);
            canvas.drawCircle(centerX - k * 0.025f, earCy + noseHeight / 2, eyesRadius, mPaint);
            canvas.drawCircle(centerX + k * 0.025f, earCy + noseHeight / 2, eyesRadius, mPaint);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
    }

    @Override
    public void onReleased(@NonNull final RefreshLayout refreshLayout, int height, int maxDragHeight) {
        mIsReleased = false;
        mAnimatorRelease = ValueAnimator.ofFloat(0, 0.5f, 0.75f, 0.5f, 0);
        mAnimatorRelease.setRepeatCount(ValueAnimator.INFINITE);
        mAnimatorRelease.setDuration(ANIM_TIME);
        mAnimatorRelease.setInterpolator(new AnticipateInterpolator(0.5f));
        mAnimatorRelease.addUpdateListener(animation -> {
            mReleaseRate = (float) animation.getAnimatedValue();
            KoalaBearHeader.this.invalidate();
        });
        mAnimatorRelease.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        int delayTime = 0;
        if (mAnimatorRelease != null) {
            delayTime = (int) Math.max(ANIM_TIME - mAnimatorRelease.getCurrentPlayTime(), 0);
        }
        return delayTime;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState,
            @NonNull RefreshState newState) {
        switch (newState) {
            case None:
                if (mAnimatorRelease != null) {
                    mAnimatorRelease.end();
                    mAnimatorRelease = null;
                }
            case PullDownToRefresh:
                mReleaseRate = 0;
                mIsReleased = true;
                break;
            default:
                break;
        }
    }
}
