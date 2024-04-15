package com.cookingBird.interceptors;

import com.cookingBird.annotations.Auth;
import com.cookingBird.service.SessionService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthInterceptor<T> implements HandlerInterceptor {
    public final String NAME = "com.cookingBird.interceptors.core.AuthInterceptor";
    private final SessionService<T> sessionService;

    public AuthInterceptor(SessionService<T> sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth methodAnnotation = handlerMethod.getMethodAnnotation(Auth.class);
        if (Objects.isNull(methodAnnotation)) {
            methodAnnotation = handlerMethod.getBeanType().getAnnotation(Auth.class);
        }
        if (Objects.isNull(methodAnnotation)) return true;
        String permission = methodAnnotation.value();

        return sessionService.check(permission);
    }
}
