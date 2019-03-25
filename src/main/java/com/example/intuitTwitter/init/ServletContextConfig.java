package com.example.intuitTwitter.init;

import com.example.intuitTwitter.Interceptors.AuthorizationInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletContext;

@Configuration
public class ServletContextConfig extends WebMvcConfigurerAdapter implements ServletContextAware {

    private static final String ROOT_URL="/";
    private static final String DOCS_API="/docs/index.html";
    private static final String SWAGGER_URL ="/swagger-ui.html";
    private static final String SWAGGER_UI_JARS = "/webjars/springfox-swagger-ui/**";
    private static final String SWAGGER_RESOURCES="/swagger-resources";
    private static final String SWAGGER_CONFIGURATION="/swagger-resources/configuration/**";
    private static final String API_DOCS="/v2/api-docs";

    private ServletContext servletContext;
    private  Environment environment;
    private AuthorizationInterceptors authorizationInterceptors;

    @Autowired
    public ServletContextConfig(Environment environment, AuthorizationInterceptors authorizationInterceptors){
        this.environment = environment;
        this.authorizationInterceptors= authorizationInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //addInterceptor(registry, new RequestHeaderInterceptors());
        addInterceptor(registry, authorizationInterceptors);
    }

    private void addInterceptor(final InterceptorRegistry registry, final HandlerInterceptorAdapter handlerInterceptorAdapter){
        InterceptorRegistration registration = registry.addInterceptor(handlerInterceptorAdapter)
                .excludePathPatterns(ROOT_URL)
                .excludePathPatterns(DOCS_API)
                .excludePathPatterns(SWAGGER_URL)
                .excludePathPatterns(SWAGGER_UI_JARS)
                .excludePathPatterns(SWAGGER_RESOURCES)
                .excludePathPatterns(SWAGGER_CONFIGURATION)
                .excludePathPatterns(API_DOCS);
    }


    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
