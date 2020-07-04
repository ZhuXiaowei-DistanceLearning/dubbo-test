package com.zxw.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author zxw
 * @date 2020/7/4 11:59
 */
@Component
public class MyApplicationListener implements ApplicationListener<MyApplicatioEvent> {

    public void onApplicationEvent(MyApplicatioEvent event) {
        Object source = event.getSource();
        System.out.println("这是MyApplicationListener[" + source + "]");
    }

}
