package com.klui.demo.modules.smarttablayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.klui.smarttablayout.SmartTabLayout;
import com.klui.smarttablayout.v4.FragmentPagerItems;
import com.klui.smarttablayout.v4.FragmentStatePagerItemAdapter;
import com.klui.demo.R;
import com.klui.demo.app.BaseActivity;
import com.klui.demo.app.DemoFragment;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：
 */
public class SmartTabLayoutActivity extends BaseActivity {

    private SmartTabLayout mSmartTabLayout;
    private ViewPager mViewPager;

    private FragmentStatePagerItemAdapter mTabAdapter;

    private Class<?>[] mFragmentClass = { DemoFragment.class, DemoFragment.class, DemoFragment.class };

    private String[] mKey = { "Key1", "Key2", "Key3" };
    private String[] mTab = { "Tab1", "Tab2", "Tab3" };

    @Override
    public int getContentViewID() {
        return R.layout.tab_layout_activity;
    }

    @Override
    public void initView() {
        mSmartTabLayout = findViewById(R.id.stl_demo_smart_tab_layout);
        mViewPager = findViewById(R.id.stl_demo_view_pager);
    }

    @Override
    public void initData() {

        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for (int i = 0; i < mFragmentClass.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString(DemoFragment.KEY, mKey[i]);
            creator.add(mTab[i], (Class<? extends Fragment>) mFragmentClass[i], bundle);
        }
        mTabAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), creator.create());
        mViewPager.setAdapter(mTabAdapter);

        mSmartTabLayout.setCustomTabView(R.layout.stl_demo_tab_item, R.id.tab_tv);
        mSmartTabLayout.setNeedBold(true, R.id.tab_tv);
        mSmartTabLayout.setViewPager(mViewPager);
    }
}
