package com.klui.demo.modules.nestedscroll;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.klui.demo.R;
import com.klui.demo.adapter.BaseRecyclerAdapter;
import com.klui.demo.adapter.SmartViewHolder;
import com.klui.demo.app.BaseActivity;
import com.klui.refresh.SmartRefreshLayout;
import com.klui.refresh.api.RefreshLayout;
import com.klui.refresh.listener.OnLoadMoreListener;
import com.klui.scroll.VerticalNestedScrollLayout;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：
 */
public class NestedScrollLayoutActivity extends BaseActivity {

    private BaseRecyclerAdapter<Model> mAdapter;

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private VerticalNestedScrollLayout mVerticalNestedScrollLayout;

    @Override
    public int getContentViewID() {
        return R.layout.activity_nested_scroll;
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    public void initData() {

        //加载数据
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(
                mAdapter = new BaseRecyclerAdapter<Model>(mockData(), R.layout.listitem_practice_repast) {
                    @Override
                    protected void onBindViewHolder(SmartViewHolder holder, Model model, int position) {
                        holder.text(R.id.name, model.name);
                        holder.text(R.id.nickname, model.nickname);
                        holder.image(R.id.image, model.imageId);
                        holder.image(R.id.avatar, model.avatarId);
                    }
                });

        //设置嵌套滚动
        mVerticalNestedScrollLayout = findViewById(R.id.nested_scroll_layout);
        mVerticalNestedScrollLayout.setOnScrollYListener((scrollY, percent, isTop, isBottom) -> {
            Log.e("xfz", "int scrollY, float percent, boolean isTop, boolean isBottom"
                    + scrollY
                    + "  "
                    + percent
                    + "  "
                    + isTop
                    + "  "
                    + isBottom);
            if (isBottom) {
                mRefreshLayout.setEnableRefresh(true);
            } else {
                mRefreshLayout.setEnableRefresh(false);
            }
        });

        mRefreshLayout = findViewById(R.id.smart_refresh_layout);
        //设置下拉刷新
        mRefreshLayout.setOnRefreshListener(refreshLayout1 -> refreshLayout1.getLayout().postDelayed(() -> {
            mAdapter.refresh(mockData());
            refreshLayout1.finishRefresh();
            refreshLayout1.setNoMoreData(false);
        }, 500));
        //设置上拉加载
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() > 30) {
                            Toast.makeText(getApplication(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            mAdapter.loadMore(mockData());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 2000);
            }
        });
    }

    private class Model {
        int imageId;
        int avatarId;
        String name;
        String nickname;
    }

    /**
     * 模拟数据
     */
    private Collection<Model> mockData() {
        return Arrays.asList(
                new Model() {{
                    this.name = "但家香酥鸭";
                    this.nickname = "爱过那张脸";
                    this.imageId = R.mipmap.image_practice_repast_1;
                    this.avatarId = R.mipmap.image_avatar_1;
                }}, new Model() {{
                    this.name = "香菇蒸鸟蛋";
                    this.nickname = "淑女算个鸟";
                    this.imageId = R.mipmap.image_practice_repast_2;
                    this.avatarId = R.mipmap.image_avatar_2;
                }}, new Model() {{
                    this.name = "花溪牛肉粉";
                    this.nickname = "性感妩媚";
                    this.imageId = R.mipmap.image_practice_repast_3;
                    this.avatarId = R.mipmap.image_avatar_3;
                }}, new Model() {{
                    this.name = "破酥包";
                    this.nickname = "一丝丝纯真";
                    this.imageId = R.mipmap.image_practice_repast_4;
                    this.avatarId = R.mipmap.image_avatar_4;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "米豆腐";
                    this.nickname = "宝宝树人";
                    this.imageId = R.mipmap.image_practice_repast_6;
                    this.avatarId = R.mipmap.image_avatar_6;
                }});
    }
}
