package com.cookingBird.controller;

import com.cookingBird.annotations.Auth;
import com.cookingBird.dto.UserDTO;
import com.cookingBird.models.User;
import com.cookingBird.util.TokenUtil;
import com.cookingBird.vo.UserVO;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/token")
public class TestController {

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

    @SneakyThrows
    @PostMapping("/login")
    public String login(HttpServletResponse response, @RequestBody UserDTO userDTO) {
        String account = userDTO.getAccount();
        String password = userDTO.getPassword();
        User user = usersMap.get(account);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        if (!Objects.equals(user.getPassword(), password)) {
            throw new RuntimeException("密码错误");
        }
        return TokenUtil.create(user);
    }

    @GetMapping("/info")
    @Auth("admin")
    public UserVO getInfo(HttpServletRequest request) {
        String token = request.getHeader(TokenUtil.TOKEN_HEADER);
        if (Objects.isNull(token)) {
            throw new RuntimeException("无权限");
        }
        User user1 = TokenUtil.parse(token, User.class);
        User user2 = usersMap.get(user1.getAccount());

        if (Objects.isNull(user2)) {
            throw new RuntimeException("无权限");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user2, userVO);
        return userVO;
    }
}
