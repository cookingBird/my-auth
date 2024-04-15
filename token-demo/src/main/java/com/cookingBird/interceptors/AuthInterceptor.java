package com.cookingBird.interceptors;

import com.cookingBird.annotations.Auth;
import com.cookingBird.controller.TestController;
import com.cookingBird.models.User;
import com.cookingBird.util.TokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public final String NAME = "com.cookingBird.interceptors.token.AuthInterceptor";

    private final TestController testController;

    public AuthInterceptor(TestController testController) {
        this.testController = testController;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth methodAnnotation = handlerMethod.getMethodAnnotation(Auth.class);
        if (Objects.isNull(methodAnnotation)) {
            methodAnnotation = handlerMethod.getBeanType().getAnnotation(Auth.class);
        }
        if (Objects.isNull(methodAnnotation)) return true;
        String permission = methodAnnotation.value();
        User user = TokenUtil.parse(request.getHeader(TokenUtil.TOKEN_HEADER), User.class);
        String[] permission1 = user.getPermission();
        return Arrays.asList(permission1).contains(permission);
    }
}
