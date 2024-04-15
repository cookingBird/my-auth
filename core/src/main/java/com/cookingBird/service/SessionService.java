package com.cookingBird.service;

import com.cookingBird.model.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SessionService<T> {

    public final static String COOKIE_NAME = "JSESSIONID";
    @Autowired(required = true)
    private HttpServletRequest request;
    @Autowired(required = true)
    private HttpServletResponse response;

    public final Session<T> login(T userInfo) {
        Session<T> tSession = new Session<>(userInfo);
        this.saveSession(tSession);
        Cookie cookie = new Cookie(COOKIE_NAME, tSession.getSessionId().toString());
        cookie.setPath("/");
        response.addCookie(cookie);
        return tSession;
    }

    public abstract void saveSession(Session<T> tSession);

    public final Session<T> getSession() {
        Cookie[] cookies = request.getCookies();
        String jsessionid = Stream.of(cookies)
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue))
                .get(COOKIE_NAME);
        return findSession(jsessionid);
    }

    protected abstract Session<T> findSession(String jsessionid);

    public final void logout(Object sessionId) {
        this.removeSession(sessionId);
    }

    protected abstract void removeSession(Object sessionId);

    public boolean check(String permission) {
        return false;
    }

}
