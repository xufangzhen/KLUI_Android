package com.klui.demo.adapter;

import java.io.Serializable;

/**
 * @author 许方镇
 * @date 2018/5/4 0004
 * 模块功能：
 */

public abstract class DemoItem implements Serializable {

    private static final long serialVersionUID = 2393963241955276169L;

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
