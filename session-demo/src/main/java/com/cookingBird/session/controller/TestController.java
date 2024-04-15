package com.cookingBird.session.controller;

import com.cookingBird.annotations.Auth;
import com.cookingBird.model.Session;
import com.cookingBird.service.SessionService;
import com.cookingBird.session.dto.UserDTO;
import com.cookingBird.session.models.User;
import com.cookingBird.session.models.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/session")
public class TestController {
    private SessionService<UserVO> sessionService;
    public TestController(SessionService<UserVO> sessionService) {
        this.sessionService = sessionService;
    }

    public static final Map<String, User> usersMap = new HashMap<String, User>() {
        {
            put("123", new User()
                    .setName("张三")
                    .setAccount("123")
                    .setPassword("456")
                    .setPermission(new String[]{"admin"})
            );
            put("456", new User()
                    .setName("李四")
                    .setAccount("456")
                    .setPassword("789")
                    .setPermission(new String[]{"normal"})
            );
        }
    };


    @GetMapping("/hello-world")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/login")
    public void login(HttpServletResponse response, @RequestBody UserDTO userDTO) {
        String account = userDTO.getAccount();
        String password = userDTO.getPassword();
        User user = usersMap.get(account);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        if (!Objects.equals(user.getPassword(), password)) {
            throw new RuntimeException("密码错误");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        Session<UserVO> session = sessionService.login(userVO);
        session.setAttribute("permission", user.getPermission());
        sessionService.saveSession(session);
    }

    @Auth("admin")
    @GetMapping("/info")
    public User getInfo(HttpServletRequest request) {
        Session<UserVO> session = sessionService.getSession();
        UserVO userInfo = session.getUserInfo();
        if (Objects.isNull(userInfo)) {
            throw new RuntimeException("未登录");
        }
        String account = userInfo.getAccount();
        if (Objects.isNull(account)) {
            throw new RuntimeException("未登录");
        }
        List<User> users = usersMap.values()
                .stream()
                .filter(user -> user.getAccount().equals(account))
                .collect(Collectors.toList());
        if (Objects.isNull(users) || users.isEmpty()) {
            throw new RuntimeException("未登录");
        }
        return users.get(0);
    }
}
