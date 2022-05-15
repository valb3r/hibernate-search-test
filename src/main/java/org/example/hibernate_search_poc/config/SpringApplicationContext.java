package org.example.hibernate_search_poc.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext implements ApplicationContextAware {
     
    private static ApplicationContext context;

    public static <T extends Object> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringApplicationContext.context = context;
    }
}