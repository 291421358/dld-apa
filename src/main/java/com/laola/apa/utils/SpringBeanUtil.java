package com.laola.apa.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class SpringBeanUtil {
    /**
     * @param BeanId
     * @return
     */
    public static Object getBeanByName(String BeanId) {
        if (null == BeanId) {
            return null;
        }
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        assert wac != null;
        return wac.getBean(BeanId);
    }

    /**
     * @param clazz
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object getBeanByType(Class clazz) {
        if (null == clazz) {
            return null;
        }
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        assert wac != null;
        return wac.getBean(clazz);
    }
}
