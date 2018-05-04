package com.kaola.klui.app;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：
 */
public abstract class BaseFragment extends Fragment {

    private View mRootView;

    /**
     * 返回布局id
     *
     * @return 返回布局id
     */
    public abstract @LayoutRes
    int getContentViewID();

    /**
     * 初始化组件
     *
     * @param v 根视图
     */
    public abstract void initView(View v);

    /**
     * 初始化数据
     */
    public abstract void initData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(getContentViewID(), container, false);
            initView(mRootView);
            initData();
        } else {
            ViewParent parent = mRootView.getParent();
            if (null != parent && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
