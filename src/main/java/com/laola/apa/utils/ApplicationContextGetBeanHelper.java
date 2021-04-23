package com.laola.apa.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextGetBeanHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBean(String className) throws BeansException,IllegalArgumentException {
        if(className==null || className.length()<=0) {
            throw new IllegalArgumentException("className为空");
        }

        return applicationContext != null ? applicationContext.getBean(className) : null;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

}