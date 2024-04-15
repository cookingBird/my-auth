package com.cookingBird.model;

import com.cookingBird.util.SessionIdGenerator;
import com.cookingBird.util.impl.DefaultSessionIdGenerator;
import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class Session<T> {
    private final Object sessionId;
    private Long cTime;
    private final Map<String, Object> data;
    private final T userInfo;
    private final SessionIdGenerator generator = new DefaultSessionIdGenerator();

    public Session(T userInfo) {
        this.sessionId = generator.generate();
        this.cTime = System.currentTimeMillis();
        this.data = new HashMap<>();
        this.userInfo = userInfo;
    }

    public void refresh() {
        this.cTime = System.currentTimeMillis();
    }

    public synchronized void setAttribute(String key, Object value) {
        this.data.put(key, value);
    }

    public Object getAttribute(String key) {
        return this.data.get(key);
    }

    public Collection<Object> getAttributes() {
        return this.data.values();
    }

    public void clearAttributes() {
        this.data.clear();
    }

}
