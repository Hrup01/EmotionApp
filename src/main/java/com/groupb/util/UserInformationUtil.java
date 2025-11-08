package com.groupb.util;

import com.groupb.pojo.User;
import com.groupb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserInformationUtil {
    @Autowired
    private UserService userService;

    /**
     * 从SecurityContext获取当前用户ID
     * 从JWT token中解析用户信息
     */
    public Long getCurrentUserId(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }

        try {
            // 从SecurityContext中获取用户名
            String username = auth.getPrincipal().toString();

            // 从JWT token中解析用户ID
            // 这里需要从请求头中获取token并解析用户ID
            // 暂时通过用户名查询用户ID，实际项目中应该从JWT token中解析
            log.debug("当前用户: {}", username);

            // 通过用户名查询用户ID
            User user = userService.findByUsername(username);
            if (user != null) {
                return user.getId();
            }

            return null;
        } catch (Exception e) {
            log.error("解析用户ID失败", e);
            return null;
        }
    }
}
