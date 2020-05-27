package com.zxw.pojo;

import java.io.Serializable;

/**
 * @author zxw
 * @date 2020-05-26 14:14:59
 * @Description
 */
public class Text implements Serializable {
    private String name;
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
