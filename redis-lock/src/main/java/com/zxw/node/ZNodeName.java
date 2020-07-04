package com.zxw.node;

import com.zxw.config.MyBeanPostProcessor;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zxw
 * @date 2020/7/2 21:23
 */
@Data
public class ZNodeName {
    private String name;
    private String prefix;
    private int sequence = -1;

    public ZNodeName() {
        this.name = "zNode";
        this.prefix = "zPrefix";
        this.sequence = 1;
    }

    public ZNodeName zNodeName() {
        ZNodeName zNodeName = new ZNodeName();
        zNodeName.name = "zNode";
        zNodeName.prefix = "zPrefix";
        return zNodeName;
    }

    public void initMethod(){
        System.out.println("初始化");
    }

    public void postConstruct(){
        System.out.println("postConstruct");
    }

}
