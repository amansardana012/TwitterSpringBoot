package com.example.intuitTwitter.Interceptors;

import com.example.intuitTwitter.domain.valueObject.SystemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@EnableConfigurationProperties
public class AuthorizationInterceptors extends HandlerInterceptorAdapter implements InitializingBean {

    @Value("#{${app.whitelistedClients}}")
    private Map<String, String> whitelistedClients= new HashMap<>();
    private Map<String, String> clientMap;
    private Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptors.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String CLIENT_ID = "clientId";

    @Autowired
    public AuthorizationInterceptors(Environment environment){
        this.environment = environment;
    }

    public Map<String, String> getWhitelistedClients() {
        return this.whitelistedClients;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        try {
            if (request != null) {
                String authToken = request.getHeader(AUTHORIZATION);
                final String requestPath = request.getServletPath();

                if (authToken != null) {
                    if (!clientMap.containsKey(authToken)) {
                        logger.error(SystemEvent.API_AUTHORIZATION_DENIED.getId(), SystemEvent.API_AUTHORIZATION_DENIED.getDescription() + " Auth token= " + request.getHeader(AUTHORIZATION) + " ClientId= " + request.getHeader(CLIENT_ID));
                        return errorResponse(response, SystemEvent.API_AUTHORIZATION_DENIED, 400);
                    } else {
                        Pattern urlMatcher = Pattern.compile(environment.getProperty("auth." + clientMap.get(authToken)));
                        if (!urlMatcher.matcher(requestPath).matches()) {
                            logger.error(SystemEvent.INSUFFICIENT_PERMISSIONS.getId(), SystemEvent.INSUFFICIENT_PERMISSIONS.getDescription() + " Path=" + requestPath + " Auth token= " + request.getHeader(AUTHORIZATION) + " ClientId= " + request.getHeader(CLIENT_ID));
                            return errorResponse(response, SystemEvent.INSUFFICIENT_PERMISSIONS, 401);
                        }
                    }
                } else {
                    logger.error(SystemEvent.API_AUTHORIZATION_DENIED.getId(), SystemEvent.API_AUTHORIZATION_DENIED.getDescription() + " Auth token NOT passed");
                    return errorResponse(response, SystemEvent.API_AUTHORIZATION_DENIED, 401);
                }
            }
        }
        catch (Exception ex){
            logger.error(SystemEvent.API_AUTHORIZATION_DENIED.getId(), SystemEvent.API_AUTHORIZATION_DENIED.getDescription() + " Auth token NOT passed");
            return errorResponse(response, SystemEvent.API_AUTHORIZATION_DENIED, 401);
        }
        return  true;
    }


    private  boolean errorResponse(final HttpServletResponse response, final SystemEvent systemEvent, final int httpStatusCode){
        try {
            response.setContentType("application/json");
            response.setStatus(httpStatusCode);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(systemEvent.getDescription());
        }
        catch (Exception ex){
            logger.error(SystemEvent.API_UNKNOWN_ERROR.getId(), SystemEvent.API_UNKNOWN_ERROR.getDescription(), ex);
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        clientMap= new HashMap<>();
        clientMap= whitelistedClients;
    }
}
