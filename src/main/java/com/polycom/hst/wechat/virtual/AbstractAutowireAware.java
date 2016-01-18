package com.polycom.hst.wechat.virtual;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class AbstractAutowireAware {
	protected WebApplicationContext springContext;
	
	protected void autowireSpringContext(ServletContext context) {
		springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        final AutowireCapableBeanFactory beanFactory = springContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
	}
}
