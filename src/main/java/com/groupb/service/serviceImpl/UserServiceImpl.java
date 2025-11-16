package com.groupb.service.serviceImpl;

import com.groupb.mapper.UserMapper;
import com.groupb.pojo.User;
import com.groupb.pojo.dto.LoginDTO;
import com.groupb.service.UserService;
import com.groupb.util.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
            log.warn("用户登录失败：用户不存在 - {}", user.getUsername());
            return null;
        }

        // 2. 检查用户状态
        if (u.getStatus() == User.UserStatus.DISABLED || u.getStatus() == User.UserStatus.DELETED) {
            log.warn("用户登录失败：用户状态异常 - {} (状态: {})", user.getUsername(), u.getStatus());
            return null;
        }

        // 3. 校验密码（支持BCrypt加密存储）
        boolean passwordMatches = passwordEncoder.matches(user.getPassword(), u.getPassword());
        if (!passwordMatches) {
            log.warn("用户登录失败：密码错误 - {}", user.getUsername());
            return null;
        }

        // 4. 更新最后登录时间
        u.setLastLoginTime(LocalDateTime.now());
        u.setStatus(User.UserStatus.ONLINE);
        userMapper.updateUser(u);

        // 5. 生成JWT令牌
        String token = jwtTokenProvider.generateToken(u.getId(), u.getUsername());

        // 6. 返回登录DTO（隐藏密码）
        log.info("用户登录成功: {}", u.getUsername());
        return new LoginDTO(u.getId(), u.getUsername(), null, token);
    }

    @Override
    public LoginDTO register(User user) {
        // 1. 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            log.warn("用户注册失败：用户名已存在 - {}", user.getUsername());
            return null;
        }

        // 2. 加密密码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 3. 设置默认值
        user.setStatus(User.UserStatus.ONLINE);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());

        // 4. 保存用户
        int result = userMapper.insertUser(user);
        if (result <= 0) {
            log.error("用户注册失败：数据库插入失败 - {}", user.getUsername());
            return null;
        }

        // 5. 生成JWT令牌
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        // 6. 返回登录DTO
        log.info("用户注册成功: {}", user.getUsername());
        return new LoginDTO(user.getId(), user.getUsername(), null, token);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findById(long id) {
        return userMapper.findById(id);
    }

    @Override
    public boolean updateAvatar(long userId, String avatarUrl) {
        try {
            int result = userMapper.updateAvatarUrl(userId, avatarUrl, LocalDateTime.now());
            if (result > 0) {
                log.info("更新用户头像成功: userId={}, avatarUrl={}", userId, avatarUrl);
                return true;
            } else {
                log.warn("更新用户头像失败: userId={}", userId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户头像异常: userId={}", userId, e);
            return false;
        }
    }

    @Override
    public boolean updateNickname(long userId, String nickname) {
        try {
            int result = userMapper.updateNickname(userId, nickname, LocalDateTime.now());
            if (result > 0) {
                log.info("更新用户昵称成功: userId={}, nickname={}", userId, nickname);
                return true;
            } else {
                log.warn("更新用户昵称失败: userId={}", userId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户昵称异常: userId={}", userId, e);
            return false;
        }
    }

    @Override
    public boolean updateGender(long userId, String gender) {
        try {
            int result = userMapper.updateGender(userId, gender, LocalDateTime.now());
            if (result > 0) {
                log.info("更新用户性别成功: userId={}, gender={}", userId, gender);
                return true;
            } else {
                log.warn("更新用户性别失败: userId={}", userId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户性别异常: userId={}", userId, e);
            return false;
        }
    }

    @Override
    public boolean updateBirthday(long userId, LocalDate birthday) {
        try {
            int result = userMapper.updateBirthday(userId, birthday, LocalDateTime.now());
            if (result > 0) {
                log.info("更新用户生日成功: userId={}, birthday={}", userId, birthday);
                return true;
            } else {
                log.warn("更新用户生日失败: userId={}", userId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户生日异常: userId={}", userId, e);
            return false;
        }
    }

    @Override
    public boolean updateProfile(long userId, String avatarUrl, String nickname, String gender, LocalDate birthday) {
        try {
            int result = userMapper.updateProfile(userId, avatarUrl, nickname, gender, birthday, LocalDateTime.now());
            if (result > 0) {
                log.info("更新用户个人资料成功: userId={}", userId);
                return true;
            } else {
                log.warn("更新用户个人资料失败: userId={}", userId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户个人资料异常: userId={}", userId, e);
            return false;
        }
    }

    @Override
    public boolean deactivateUser(long userId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            int result = userMapper.deactivateUser(userId, now);
            if (result > 0) {
                log.info("用户账号已注销(软删除): userId={}", userId);
                return true;
            } else {
                log.warn("用户账号注销失败: userId={}", userId);
                return false;
            }
        } catch (Exception e) {
            log.error("用户账号注销异常: userId={}", userId, e);
            return false;
        }
    }
}
