package com.klui.demo.app;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：
 */

public class BaseApplication extends Application {

    private static BaseApplication mApplication;
    private static List<Activity> mRunningActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //前后台切换
        BaseTaskSwitch.init(this).setOnTaskSwitchListener(new BaseTaskSwitch.OnTaskSwitchListener() {
            @Override
            public void onTaskSwitchToForeground() {
                //切换到前台
            }

            @Override
            public void onTaskSwitchToBackground() {
                //切换到后台
            }
        });
    }

    public static BaseApplication getInstance() {
        if (mApplication == null) {
            throw new NullPointerException("app not create or be terminated!");
        }
        return mApplication;
    }

    public static List<Activity> getRunningActivityList() {
        if (mRunningActivity == null) {
            mRunningActivity = new ArrayList<>();
        }
        return mRunningActivity;
    }
}
