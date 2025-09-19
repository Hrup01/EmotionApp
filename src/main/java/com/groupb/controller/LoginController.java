package com.groupb.controller;

import com.groupb.pojo.User;
import com.groupb.pojo.dto.LoginDTO;
import com.groupb.pojo.dto.Result;
import com.groupb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录Controller
 * 提供简单的用户名密码登录功能
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * POST /api/auth/login
     * @param user 用户信息（用户名和密码）
     * @return 登录结果，包含JWT token
     */
    @PostMapping("/login")
    public Result<LoginDTO> login(@RequestBody User user) {
        try {
            log.info("用户登录请求: {}", user.getUsername());
            
            // 验证输入
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            
            LoginDTO loginDTO = userService.login(user);
            if (loginDTO != null) {
                log.info("用户 {} 登录成功", user.getUsername());
                return Result.success(loginDTO, "登录成功");
            } else {
                log.warn("用户 {} 登录失败：用户名或密码错误", user.getUsername());
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("登录过程中发生错误", e);
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 用户注册
     * POST /api/auth/register
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<LoginDTO> register(@RequestBody User user) {
        try {
            log.info("用户注册请求: {}", user.getUsername());
            
            // 验证输入
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (user.getPassword().length() < 6) {
                return Result.error("密码长度不能少于6位");
            }
            
            LoginDTO loginDTO = userService.register(user);
            if (loginDTO != null) {
                log.info("用户 {} 注册成功", user.getUsername());
                return Result.success(loginDTO, "注册成功");
            } else {
                return Result.error("用户名已存在");
            }
        } catch (Exception e) {
            log.error("注册过程中发生错误", e);
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 健康检查(测试接口 测试结束后删除)
     * GET /api/auth/health
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("认证服务运行正常", "服务正常");
    }
}
