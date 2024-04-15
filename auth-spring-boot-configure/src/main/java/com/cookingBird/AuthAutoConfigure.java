package com.cookingBird;

import com.cookingBird.interceptors.AuthInterceptor;
import com.cookingBird.service.SessionService;
import com.cookingBird.service.impl.DefaultSessionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public <T> SessionService<T> sessionService() {
        return new DefaultSessionService<>();
    }

    @Bean
    public <T> AuthInterceptor<T> authInterceptor(SessionService<T> sessionService) {
        AuthInterceptor<T> tAuthInterceptor = new AuthInterceptor<>(sessionService);
        return tAuthInterceptor;
    }
}
