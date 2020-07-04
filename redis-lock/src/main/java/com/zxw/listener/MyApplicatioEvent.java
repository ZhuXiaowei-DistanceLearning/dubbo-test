package com.zxw.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author zxw
 * @date 2020/7/4 11:59
 */
public class MyApplicatioEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public MyApplicatioEvent(Object source) {
        super(source);
    }

}