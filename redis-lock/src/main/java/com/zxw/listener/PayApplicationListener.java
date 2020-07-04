package com.zxw.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zxw
 * @date 2020/7/4 12:04
 */
@Component
public class PayApplicationListener implements ApplicationListener<MyApplicatioEvent> {
    public void onApplicationEvent(MyApplicatioEvent event) {
        Object source = event.getSource();
        System.out.println("PayApplicationListener[" + source + "]");
    }
}
