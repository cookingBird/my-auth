package com.cookingBird.service.impl;

import com.cookingBird.model.Session;
import com.cookingBird.service.SessionService;

import java.util.concurrent.ConcurrentHashMap;

public class DefaultSessionService<T> extends SessionService<T> {

    private final ConcurrentHashMap<Object, Session<T>> concurrentHashMap = new ConcurrentHashMap<>();


    @Override
    public void saveSession(Session<T> tSession) {
        this.concurrentHashMap.put(tSession.getSessionId(), tSession);
    }

    @Override
    protected Session<T> findSession(String jsessionid) {
        return this.concurrentHashMap.get(jsessionid);
    }

    @Override
    protected void removeSession(Object sessionId) {
        this.concurrentHashMap.remove(sessionId);
    }

    @Override
    public boolean check(String permission) {
        return true;
    }
}
