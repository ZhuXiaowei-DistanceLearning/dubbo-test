package com.zxw.config;

import com.zxw.listener.MyApplicatioEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zxw
 * @date 2020/7/4 12:07
 */
@Component
public class MyApplicationContextAware implements ApplicationContextAware {
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        applicationContext.publishEvent(new MyApplicatioEvent(this));
    }
}
