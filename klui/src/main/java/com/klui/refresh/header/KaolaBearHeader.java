package com.klui.refresh.header;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import com.klui.R;
import com.klui.refresh.api.RefreshHeader;
import com.klui.refresh.api.RefreshLayout;
import com.klui.refresh.constant.RefreshState;
import com.klui.refresh.constant.SpinnerStyle;
import com.klui.refresh.internal.InternalAbstract;
import com.klui.refresh.util.DensityUtil;

/**
 * @author 许方镇
 * @date 2018/5/8 0008
 * 模块功能：无尾熊下拉刷新头部动画
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class KaolaBearHeader extends InternalAbstract implements RefreshHeader {

    //<editor-fold desc="属性字段">

    public static final long ANIM_CYCLE_TIME = 1500L;
    public static final int MINIMUM_HEIGHT = DensityUtil.dp2px(130);

    protected boolean mIsPulling;
    protected boolean mEnableHorizontalDrag = false;

    protected Path mPath;
    protected Paint mPaint;
    protected RectF mRectF = new RectF(0, 0, 0, 0);

    protected ValueAnimator mAnimatorRelease;

    private int mOffset;

    protected int mEarRadius;
    protected float mReleaseRate;
    protected float mEarTransRate;

    protected int mHeaderRadius;
    protected int mHeaderTransRate;

    protected int mScaleRate;
    protected int mBottomInterval;

    public KaolaBearHeader(Context context) {
        this(context, null);
    }

    public KaolaBearHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KaolaBearHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSpinnerStyle = SpinnerStyle.Scale;
        final View thisView = this;
        final DensityUtil density = new DensityUtil();

        thisView.setMinimumHeight(MINIMUM_HEIGHT);
        mBottomInterval = MINIMUM_HEIGHT / 2;
        mEarRadius = density.dip2px(10);
        mEarTransRate = 1.5f * mEarRadius / mBottomInterval;

        mScaleRate = mBottomInterval / mEarRadius;
        mHeaderRadius = density.dip2px(25);
        mHeaderTransRate = density.dip2px(50);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimatorRelease != null) {
            mAnimatorRelease.end();
            mAnimatorRelease = null;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mOffset >= mBottomInterval) {
            final int y = !mIsPulling ? mBottomInterval : Math.min(mOffset, MINIMUM_HEIGHT) - mBottomInterval;
            //耳朵
            mPaint.setColor(getResources().getColor(R.color.red));
            float earRadius = Math.min(mEarRadius, y / mScaleRate) * (1 - mReleaseRate);
            float centerX = getWidth() * 0.5f;
            float leftEarCx = centerX - y * mEarTransRate - mReleaseRate * mEarRadius * 2;
            float rightEarCx = centerX + y * mEarTransRate + mReleaseRate * mEarRadius * 2;
            float earCy = getHeight() - mBottomInterval;
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

            //眼睛、鼻子
            if (mReleaseRate < 0.5f) {
                canvas.save();
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
        }
        super.dispatchDraw(canvas);
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight);
        onMoving(offset);
    }

    @Override
    public void onReleased(@NonNull final RefreshLayout refreshLayout, int height, int maxDragHeight) {
        onReleased();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        return onFinish();
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState,
            @NonNull RefreshState newState) {
        onStateChanged(newState);
    }

    public void onMoving(int offset) {
        mOffset = offset;
    }

    public void onReleased() {
        mIsPulling = false;
        mAnimatorRelease = ValueAnimator.ofFloat(0, 0.5f, 0.75f, 0.5f, 0);
        mAnimatorRelease.setRepeatCount(ValueAnimator.INFINITE);
        mAnimatorRelease.setDuration(ANIM_CYCLE_TIME);
        mAnimatorRelease.setInterpolator(new AnticipateInterpolator(0.5f));
        mAnimatorRelease.addUpdateListener(animation -> {
            mReleaseRate = (float) animation.getAnimatedValue();
            KaolaBearHeader.this.invalidate();
        });
        mAnimatorRelease.start();
    }

    public int onFinish() {
        int delayTime = 0;
        if (mAnimatorRelease != null) {
            delayTime = (int) Math.max(ANIM_CYCLE_TIME - mAnimatorRelease.getCurrentPlayTime(), 0);
        }
        return delayTime;
    }

    public void onStateChanged(RefreshState newState) {
        switch (newState) {
            case None:
                if (mAnimatorRelease != null) {
                    mAnimatorRelease.end();
                    mAnimatorRelease = null;
                }
            case PullDownToRefresh:
                mReleaseRate = 0;
                mIsPulling = true;
                break;
            default:
                break;
        }
    }
}
