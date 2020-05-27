package com.zxw.spi.service.impl;

import com.zxw.spi.service.Robot;

/**
 * @author zxw
 * @date 2020-05-26 16:16:43
 * @Description
 */
public class Bumblebee implements Robot {

    @Override
    public void sayHello() {
        System.out.println("Hello, i am Bumblebee.");
    }
}
