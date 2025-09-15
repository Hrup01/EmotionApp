package com.groupb.controller;


import com.groupb.pojo.User;
import com.groupb.pojo.dto.LoginDTO;
import com.groupb.pojo.dto.Result;
import com.groupb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录Controller
 */

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param user
     * @return
     */

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        log.info("用户登录:{}", user);
        LoginDTO loginDTO = userService.login(user);
        if (loginDTO != null){
            return Result.success(loginDTO);
        }else
            return Result.error("用户名或密码错误！");

    }
}
