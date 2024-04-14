package com.cookingBird.session.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String account;

    private String password;

    private String name;

    private String[] permission;

}
