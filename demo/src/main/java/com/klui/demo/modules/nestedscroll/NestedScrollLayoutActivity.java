package com.klui.demo.modules.nestedscroll;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.klui.demo.R;
import com.klui.demo.adapter.BaseRecyclerAdapter;
import com.klui.demo.adapter.SmartViewHolder;
import com.klui.demo.app.BaseActivity;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：
 */
public class NestedScrollLayoutActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<Model> mAdapter;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(
                mAdapter = new BaseRecyclerAdapter<Model>(loadModels(), R.layout.listitem_practice_repast) {
                    @Override
                    protected void onBindViewHolder(SmartViewHolder holder, Model model, int position) {
                        holder.text(R.id.name, model.name);
                        holder.text(R.id.nickname, model.nickname);
                        holder.image(R.id.image, model.imageId);
                        holder.image(R.id.avatar, model.avatarId);
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
    private Collection<Model> loadModels() {
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
