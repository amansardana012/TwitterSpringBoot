package com.example.intuitTwitter.Interceptors;

import com.example.intuitTwitter.domain.valueObject.SystemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHeaderInterceptors extends HandlerInterceptorAdapter {

    private static final String CLIENT_ID= "clientId";
    private static final Logger logger =  LoggerFactory.getLogger(RequestHeaderInterceptors.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if(request != null){
            final String clientId= request.getHeader(CLIENT_ID);
            if(StringUtils.isEmpty(clientId)){
                return badRequestResponse(response, SystemEvent.CLIENT_ID_MISSING);
            }
        }
        return true;
    }

    private boolean badRequestResponse(final HttpServletResponse response, final SystemEvent systemEvent){
        final ResponseEntity responseEntity = ResponseEntity.badRequest().body(systemEvent.getDescription());
        try {
            response.setContentType("application/json");
            response.setStatus(responseEntity.getStatusCodeValue());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(systemEvent.getDescription());
        }
        catch (Exception ex){
            logger.error(SystemEvent.API_UNKNOWN_ERROR.getId(), SystemEvent.API_UNKNOWN_ERROR.getDescription(), ex);
        }
        return false;
    }

}
