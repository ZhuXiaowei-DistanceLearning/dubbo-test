package com.zxw.config;

import com.zxw.node.ZNodeName;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author zxw
 * @date 2020/7/3 22:49
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.createBean(ZNodeName.class);
        System.out.println("MyBeanFactoryPostProcessor执行");
    }
}
