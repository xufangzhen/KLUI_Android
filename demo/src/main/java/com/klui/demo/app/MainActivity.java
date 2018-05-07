package com.klui.demo.app;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.klui.demo.R;
import com.klui.demo.modules.nestedscroll.NestedScrollLayoutActivity;
import com.klui.demo.modules.smarttablayout.SmartTabLayoutActivity;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：主activity
 */
public class MainActivity extends BaseActivity {

    private ListView mListView;

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mListView = findViewById(R.id.main_list_view);
    }

    @Override
    public void initData() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, NestedScrollLayoutActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SmartTabLayoutActivity.class));
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
