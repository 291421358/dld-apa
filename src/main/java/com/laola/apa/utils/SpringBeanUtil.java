package com.laola.apa.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

public class SpringBeanUtil {
    /**
     * @param BeanId
     * @return
     */
    public static <O> O getBeanByTypeAndName(Class o, String BeanId) {

        ApplicationContext applicationContext = ApplicationContextGetBeanHelper.getApplicationContext();
        Map<String, Class<O>> map = applicationContext.getBeansOfType(o);
        O oClass = (O) map.get(BeanId);
        return oClass;
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
