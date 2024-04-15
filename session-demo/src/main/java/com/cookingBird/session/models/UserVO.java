package com.cookingBird.session.models;

import lombok.Data;

@Data
public class UserVO {
    private String account;

    private String name;

    private String[] permission;
}
