package com.klui.demo.modules.refresh;

import android.support.annotation.NonNull;
import android.widget.AbsListView;
import android.widget.Toast;

import com.klui.demo.R;
import com.klui.demo.adapter.BaseRecyclerAdapter;
import com.klui.demo.adapter.SmartViewHolder;
import com.klui.demo.app.BaseActivity;
import com.klui.refresh.api.RefreshLayout;
import com.klui.refresh.listener.OnLoadMoreListener;
import com.klui.refresh.listener.OnRefreshListener;

import java.util.Arrays;
import java.util.Collection;

import static android.R.layout.simple_list_item_2;

/**
 * @author 许方镇
 * @date 2018/5/7 0007
 * 模块功能：
 */
public class SmartRefreshActivity extends BaseActivity {

    private BaseRecyclerAdapter<Void> mAdapter;
    private AbsListView mListView;

    @Override
    public int getContentViewID() {
        return R.layout.activity_refresh_layout;
    }

    @Override
    public void initView() {
        mListView = findViewById(R.id.listView);
    }

    @Override
    public void initData() {

        mListView.setAdapter(mAdapter = new BaseRecyclerAdapter<Void>(simple_list_item_2) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, Void model, int position) {
                holder.text(android.R.id.text1, getString(R.string.item_example_number_title, position));
                holder.text(android.R.id.text2, getString(R.string.item_example_number_abstract, position));
                holder.textColorId(android.R.id.text2, R.color.colorPrimary);
            }
        });

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        //自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.autoRefresh();
        //设置下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.refresh(mockData());
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(false);
                    }
                }, 2000);
            }
        });
        //设置上拉加载
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
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

    private Collection<Void> mockData() {
        return Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);
    }
}
