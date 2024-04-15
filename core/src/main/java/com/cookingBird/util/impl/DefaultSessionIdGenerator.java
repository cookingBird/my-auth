package com.cookingBird.util.impl;

import com.cookingBird.util.SessionIdGenerator;

import java.util.UUID;

public class DefaultSessionIdGenerator implements SessionIdGenerator {
    @Override
    public Object generate() {
        return UUID.randomUUID().toString();
    }
}
