package com.klui.demo.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.klui.demo.R;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：
 */
public class DemoFragment extends BaseFragment {

    public static final String KEY = "key";

    private String mKey;
    private TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (null != args) {
            mKey = args.getString(KEY);
        }
    }

    @Override
    public int getContentViewID() {
        return R.layout.fragment_demo;
    }

    @Override
    public void initView(View v) {
        mTextView = v.findViewById(R.id.demo_fragment_text);
    }

    @Override
    public void initData() {
        mTextView.setText(mKey);
    }
}
