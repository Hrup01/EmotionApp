package com.groupb.service.serviceImpl;

import com.groupb.mapper.UserMapper;
import com.groupb.pojo.User;
import com.groupb.pojo.dto.LoginDTO;
import com.groupb.service.UserService;
import com.groupb.util.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public LoginDTO login(User user) {
        // 1. 通过用户名查询用户
        User u = userMapper.findByUsername(user.getUsername());
        if (u == null) {
            return null;
        }

        // 2. 校验密码（支持BCrypt加密存储）
        boolean passwordMatches = passwordEncoder.matches(user.getPassword(), u.getPassword());

        if (!passwordMatches) {
            return null;
        }

        // 3. 生成JWT令牌
        String token = jwtTokenProvider.generateToken(u.getId(), u.getPhone());

        // 4. 返回登录DTO（隐藏密码）
        log.info("用户登录成功,用户信息:{}", u.getUsername());
        return new LoginDTO(u.getId(), u.getUsername(), null, token);
    }
}
