package com.cookingBird.session.service;

import com.cookingBird.model.Session;
import com.cookingBird.service.impl.DefaultSessionService;
import com.cookingBird.session.models.UserVO;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CustomSessionService extends DefaultSessionService<UserVO> {

    @Override
    public boolean check(String permission) {
        Session<UserVO> session = this.getSession();
        String[] permission1 = (String[]) session.getAttribute("permission");
        return Arrays.asList(permission1).contains(permission);
    }
}
