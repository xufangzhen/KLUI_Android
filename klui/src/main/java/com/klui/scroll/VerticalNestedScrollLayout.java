package com.klui.scroll;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.klui.R;

/**
 * 通用垂直嵌套滚动壳
 * 注意：该View只允许有两个子View
 *
 * @author xufangzhen
 */
public class VerticalNestedScrollLayout extends LinearLayout implements NestedScrollingParent {
    /**
     * 设置true:主体View到顶了之后，头部View才能下移
     */
    private boolean mIsScrollDownWhenFirstItemIsTop;
    /**
     * 是否可以自动滚动，就头部View自动滚动到最大或者最小位置
     */
    private boolean mIsAutoScroll;
    /**
     * 上滑隐藏头部、头部保留的高度
     */
    private int mHeaderRetainHeight;
    /**
     * 最大可滚动距离
     */
    private int mMaxScrollHeight;
    /**
     * 是否正在flying
     */
    private boolean mIsFling;
    /**
     * 嵌套滚动助手类
     */
    private NestedScrollingParentHelper mParentHelper;
    /**
     * 自动滚动的动画
     */
    private ValueAnimator mScrollAnimator;
    /**
     * 头部View
     */
    private View mHeaderView;
    /**
     * 主体View
     */
    private View mBodyView;
    /**
     * 滚动助手
     */
    private Scroller mScroller;
    /**
     * 自动滚动阈值
     */
    private int mAutoScrollDistanceThreshold;

    public VerticalNestedScrollLayout(Context context) {
        this(context, null);
    }

    public VerticalNestedScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalNestedScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFromAttributes(context, attrs, defStyleAttr);
    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(LinearLayout.VERTICAL);
        mParentHelper = new NestedScrollingParentHelper(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalNestedScrollLayout,
                defStyleAttr, 0);
        mIsScrollDownWhenFirstItemIsTop =
                a.getBoolean(R.styleable.VerticalNestedScrollLayout_isScrollDownWhenFirstItemIsTop, false);
        mIsAutoScroll = a.getBoolean(R.styleable.VerticalNestedScrollLayout_isAutoScroll, false);
        mHeaderRetainHeight = (int) a.getDimension(R.styleable.VerticalNestedScrollLayout_headerRetainHeight, 0);
        mAutoScrollDistanceThreshold =
                (int) a.getDimension(R.styleable.VerticalNestedScrollLayout_autoScrollDistanceThreshold, dpToPx(50));
        a.recycle();

        mScroller = new Scroller(getContext());
        mScroller.setFriction(0.2f);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("VerticalNestedScrollLayout can host only two direct child");
        }
        super.addView(child, index, params);
    }

    public void setScrollDownWhenFirstItemIsTop(boolean scrollDownWhenFirstItemIsTop) {
        mIsScrollDownWhenFirstItemIsTop = scrollDownWhenFirstItemIsTop;
    }

    public void setAutoScroll(boolean autoScroll) {
        mIsAutoScroll = autoScroll;
    }

    public void setHeaderRetainHeight(int headerRetainHeight) {
        mHeaderRetainHeight = headerRetainHeight;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = getChildAt(0);
        mBodyView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeaderView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mMaxScrollHeight = mHeaderView.getMeasuredHeight() - mHeaderRetainHeight;
        if (mBodyView.getLayoutParams().height < getMeasuredHeight() - mHeaderRetainHeight) {
            mBodyView.getLayoutParams().height = getMeasuredHeight() - mHeaderRetainHeight;
        }
        setMeasuredDimension(getMeasuredWidth(), mBodyView.getLayoutParams().height + mHeaderView.getMeasuredHeight());
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (mScrollAnimator != null && mScrollAnimator.isStarted()) {
            mScrollAnimator.cancel();
        }
        mScroller.abortAnimation();
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (canScroll(target, dy)) {
            scrollBy(0, dy);
            consumed[1] = dy;
            if (mOnScrollToListener != null) {
                mOnScrollToListener.onScrolling(getScrollY(), mMaxScrollHeight == getScrollY(), getScrollY() == 0);
            }
        }
        if (getScrollY() > mMaxScrollHeight) {
            scrollTo(0, mMaxScrollHeight);
        } else if (getScrollY() < 0) {
            scrollTo(0, 0);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {

        if (mIsScrollDownWhenFirstItemIsTop && target.canScrollVertically(-1)) {
            return false;
        }

        if (mScrollAnimator != null && mScrollAnimator.isStarted()) {
            mScrollAnimator.cancel();
        }
        mIsFling = true;
        if (velocityX == 0 && velocityY != 0) {
            if (mIsAutoScroll) {
                if (velocityY < 0) {
                    autoDownScroll();
                } else {
                    autoUpScroll();
                }
            } else {
                mScroller.fling(0, getScrollY(), 0, (int) velocityY * 4, 0, 0, 0, mMaxScrollHeight);
            }
            if (mIsScrollDownWhenFirstItemIsTop) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            if (mOnScrollToListener != null) {
                mOnScrollToListener.onScrolling(getScrollY(), mMaxScrollHeight == getScrollY(), getScrollY() == 0);
            }
            invalidate();
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
        if (!mIsFling && getScrollY() > 0 && getScrollY() < mMaxScrollHeight && mIsAutoScroll) {
            if (getScrollY() > mAutoScrollDistanceThreshold) {
                autoUpScroll();
            } else {
                autoDownScroll();
            }
        } else {
            if (mOnScrollToListener != null) {
                mOnScrollToListener.onScrolling(getScrollY(), mMaxScrollHeight == getScrollY(), getScrollY() == 0);
            }
        }
        mIsFling = false;
    }

    public void autoUpScroll() {
        if (getScrollY() != mMaxScrollHeight) {
            smoothScrollTo(mMaxScrollHeight);
        }
    }

    public boolean isNoScroll() {
        return getScrollY() == 0;
    }

    public boolean isMaxScroll() {
        return getScrollY() == mMaxScrollHeight;
    }

    public void autoDownScroll() {
        if (getScrollY() != 0) {
            smoothScrollTo(0);
        }
    }

    private void smoothScrollTo(final int y) {
        if (getScrollY() == y) {
            return;
        }
        mScrollAnimator = ValueAnimator.ofInt(getScrollY(), y);
        mScrollAnimator.setInterpolator(new DecelerateInterpolator(2));

        int time = 400 * Math.abs(getScrollY() - y) / getScreenHeight();

        mScrollAnimator.setDuration(time);
        mScrollAnimator.addUpdateListener(valueAnimator -> {
            scrollTo(0, (Integer) valueAnimator.getAnimatedValue());
            if (mOnScrollToListener != null) {
                mOnScrollToListener.onScrolling(getScrollY(), mMaxScrollHeight == getScrollY(), getScrollY() == 0);
            }
        });
        mScrollAnimator.start();
    }

    private boolean canScroll(View target, int dy) {
        boolean hiddenTop = dy > 0 && getScrollY() < mMaxScrollHeight;
        boolean showTop = dy < 0 && getScrollY() > 0;
        if (mIsScrollDownWhenFirstItemIsTop) {
            showTop = showTop && !target.canScrollVertically(-1);
        }
        return hiddenTop || showTop;
    }

    OnScrollYListener mOnScrollToListener;

    public interface OnScrollYListener {
        void onScrolling(int scrollY, boolean isTop, boolean isBottom);
    }

    public void setOnScrollYListener(OnScrollYListener onScrollToListener) {
        this.mOnScrollToListener = onScrollToListener;
    }

    public int getMaxScrollHeight() {
        return mMaxScrollHeight;
    }

    private int dpToPx(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density);
    }

    private int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }
}
