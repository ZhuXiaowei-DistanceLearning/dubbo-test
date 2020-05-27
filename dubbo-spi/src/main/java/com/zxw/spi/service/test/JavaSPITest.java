package com.zxw.spi.service.test;

import com.zxw.spi.service.Robot;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author zxw
 * @date 2020-05-26 16:16:47
 * @Description
 */
public class JavaSPITest {

    @Test
    public void sayHello() {
        ServiceLoader<Robot> loader = ServiceLoader.load(Robot.class);
        Iterator<Robot> iterator = loader.iterator();
        while (iterator.hasNext()){
            Robot next = iterator.next();
            next.sayHello();
        }
        System.out.println("JAVA SPI");
        loader.forEach(Robot::sayHello);
    }

    @Test
    public void dubboHello() {
        ExtensionLoader<Robot> extensionLoader = ExtensionLoader.getExtensionLoader(Robot.class);
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        System.out.println("------");
        Robot bumblebee = extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
    }
}
