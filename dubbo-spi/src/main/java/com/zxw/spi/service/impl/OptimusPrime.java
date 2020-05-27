package com.zxw.spi.service.impl;

import com.zxw.spi.service.Robot;

/**
 * @author zxw
 * @date 2020-05-26 16:16:40
 * @Description
 */
public class OptimusPrime implements Robot {

    @Override
    public void sayHello() {
        System.out.println("Hello, Im am Optimus Prime");
    }
}
