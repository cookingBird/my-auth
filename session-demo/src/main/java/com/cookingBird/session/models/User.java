package com.cookingBird.session.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class User {

    private String account;

    private String password;

    private String name;

    private String[] permission;

    public User User(User user) {
        return user;
    }
}
