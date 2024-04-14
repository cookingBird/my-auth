package session.controller;


import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import session.dto.UserDTO;
import session.models.User;
import session.vo.UserVO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/session")
public class TestController {
    public static final Map<String, User> usersMap = new HashMap<String, User>() {
        {
            put("123", User.builder()
                    .name("张三")
                    .account("123")
                    .password("456")
                    .permission(new String[]{"admin"})
                    .build());
            put("456", User.builder()
                    .name("李四")
                    .account("456")
                    .password("789")
                    .permission(new String[]{"admin"})
                    .build());
        }
    };


    @GetMapping("/hello-world")
    public String hello() {
        return "hello world";
    }

    @SneakyThrows
    @PostMapping("/login")
    public boolean login(HttpServletResponse response,@RequestBody UserDTO userDTO) {
        String account = userDTO.getAccount();
        String password = userDTO.getPassword();
        User user = usersMap.get(account);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        if (!Objects.equals(user.getPassword(), password)) {
            throw new RuntimeException("密码错误");
        }
        Cookie cookie = new Cookie("userId", URLEncoder.encode(user.getAccount(), "Utf-8"));
        cookie.setMaxAge(2 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return true;
    }

    @GetMapping("/info")
    public UserVO getInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<String> userId = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("userId"))
                .map(cookie -> {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        String s = userId.get(0);
        if (Objects.isNull(s)) {
            throw new RuntimeException("用户不存在");
        }
        User user = usersMap.get(s);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
