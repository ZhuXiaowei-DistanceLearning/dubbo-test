package com.zxw.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

/**
 * @author zxw
 * @date 2020/7/3 22:49
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor, InitializingBean, BeanDefinitionRegistryPostProcessor, BeanFactoryAware {
    public ConfigurableListableBeanFactory configurableListableBeanFactory;
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("---before---");
        System.out.println(bean.getClass().getName());
        System.out.println(beanName);
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("---after---");
        System.out.println(bean.getClass().getName());
        System.out.println(beanName);
        return bean;
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("执行时机");
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("执行实际1");
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("执行实际2");
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        configurableListableBeanFactory = (ConfigurableListableBeanFactory) beanFactory;
        configurableListableBeanFactory.addBeanPostProcessor(new MyBeanPostProcessor());
    }
}
