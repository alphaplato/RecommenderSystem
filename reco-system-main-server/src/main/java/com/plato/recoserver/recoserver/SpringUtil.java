package com.plato.recoserver.recoserver;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Kevin
 * @date 2022-04-07
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T getBean(String name, Class<T> type) {
        return applicationContext.getBean(name, type);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
