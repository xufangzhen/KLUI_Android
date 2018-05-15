package com.klui.demo.modules.nestedscroll;

/**
 * @author 许方镇
 * @date 2018/5/14 0014
 * 模块功能：渐变触发的接口
 */

public interface OnGradualChangeListener {

    void onGradualChange(int scrollY, float percent, boolean isTop, boolean isBottom);
}
