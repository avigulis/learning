package com.tel.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Helper for accessing application context from static or instance methods
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException { context = applicationContext; }

    public static ApplicationContext get() { return context; }
}
