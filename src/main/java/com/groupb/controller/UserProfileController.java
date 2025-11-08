package com.groupb.controller;

import com.groupb.pojo.User;
import com.groupb.pojo.dto.Result;
import com.groupb.service.UserService;
import com.groupb.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户个人资料控制器
 * 提供修改头像、昵称、生日等功能
 */
@Slf4j
@RestController
@RequestMapping("/api/user/profile")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.max-size:10485760}")
    private long maxFileSize; // 10MB

    /**
     * 获取当前用户信息
     * API接口：GET /api/user/profile
     * 
     * @return 用户信息
     */
    @GetMapping
    public Result<User> getCurrentUserProfile() {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }

            User user = userService.findById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 隐藏敏感信息
            user.setPassword(null);
            return Result.success(user, "获取用户信息成功");
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户头像
     * API接口：POST /api/user/profile/avatar
     * 
     * @param file 头像图片文件
     * @return 更新结果，包含头像URL
     */
    @PostMapping("/avatar")
    public Result<Map<String, Object>> updateAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 获取当前用户ID
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }

            // 2. 验证文件
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            // 3. 验证文件大小
            if (file.getSize() > maxFileSize) {
                return Result.error("文件大小不能超过10MB");
            }

            // 4. 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只能上传图片文件");
            }

            // 5. 生成文件名和路径
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = generateFileName(extension);
            String relativePath = "avatar/" + generateRelativePath(fileName);
            String fullPath = uploadPath + File.separator + relativePath;

            // 6. 创建目录
            File directory = new File(fullPath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 7. 保存文件
            Path path = Paths.get(fullPath);
            Files.copy(file.getInputStream(), path);

            // 8. 生成访问URL
            String fileUrl = "/uploads/" + relativePath.replace("\\", "/");

            // 9. 更新数据库中的头像URL
            boolean success = userService.updateAvatar(userId, fileUrl);
            if (!success) {
                // 如果更新失败，删除已上传的文件
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.warn("删除文件失败: {}", fullPath, e);
                }
                return Result.error("更新头像失败");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", fileUrl);
            result.put("fileName", fileName);

            log.info("更新用户头像成功: userId={}, avatarUrl={}", userId, fileUrl);
            return Result.success(result, "头像更新成功");
        } catch (IOException e) {
            log.error("头像上传失败", e);
            return Result.error("头像上传失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("更新头像过程中发生错误", e);
            return Result.error("更新头像失败，请稍后重试");
        }
    }

    /**
     * 更新用户昵称
     * API接口：PUT /api/user/profile/nickname
     * 
     * @param request 包含nickname的请求体
     * @return 更新结果
     */
    @PutMapping("/nickname")
    public Result<Void> updateNickname(@RequestBody Map<String, String> request) {
        try {
            // 1. 获取当前用户ID
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }

            // 2. 获取昵称
            String nickname = request.get("nickname");
            if (nickname == null || nickname.trim().isEmpty()) {
                return Result.error("昵称不能为空");
            }

            // 3. 验证昵称长度
            nickname = nickname.trim();
            if (nickname.length() > 50) {
                return Result.error("昵称长度不能超过50个字符");
            }

            // 4. 更新昵称
            boolean success = userService.updateNickname(userId, nickname);
            if (!success) {
                return Result.error("更新昵称失败");
            }

            log.info("更新用户昵称成功: userId={}, nickname={}", userId, nickname);
            return Result.success(null, "昵称更新成功");
        } catch (Exception e) {
            log.error("更新昵称过程中发生错误", e);
            return Result.error("更新昵称失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户生日
     * API接口：PUT /api/user/profile/birthday
     * 
     * @param request 包含birthday的请求体（格式：yyyy-MM-dd）
     * @return 更新结果
     */
    @PutMapping("/birthday")
    public Result<Void> updateBirthday(@RequestBody Map<String, String> request) {
        try {
            // 1. 获取当前用户ID
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }

            // 2. 获取生日字符串
            String birthdayStr = request.get("birthday");
            if (birthdayStr == null || birthdayStr.trim().isEmpty()) {
                return Result.error("生日不能为空");
            }

            // 3. 解析生日日期
            LocalDate birthday;
            try {
                birthday = LocalDate.parse(birthdayStr.trim());
            } catch (Exception e) {
                return Result.error("生日格式不正确，请使用yyyy-MM-dd格式");
            }

            // 4. 验证生日日期（不能是未来日期）
            if (birthday.isAfter(LocalDate.now())) {
                return Result.error("生日不能是未来日期");
            }

            // 5. 更新生日
            boolean success = userService.updateBirthday(userId, birthday);
            if (!success) {
                return Result.error("更新生日失败");
            }

            log.info("更新用户生日成功: userId={}, birthday={}", userId, birthday);
            return Result.success(null, "生日更新成功");
        } catch (Exception e) {
            log.error("更新生日过程中发生错误", e);
            return Result.error("更新生日失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户性别
     * API接口：PUT /api/user/profile/gender
     *
     * @param request 包含gender的请求体
     * gender可选值：MALE、FEMALE、OTHER
     * @return 更新结果
     */
    @PutMapping("/gender")
    public Result<Void> updateGender(@RequestBody Map<String, String> request) {
        try {
            //1. 获取当前用户ID
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            //2. 获取性别,验证传输性别合法性
            String gender = request.get("gender");
            if (gender == null || gender.trim().isEmpty()) {
                return Result.error("性别不能为空");
            }
            gender = gender.trim();
            if (!gender.equals("MALE") && !gender.equals("FEMALE") && !gender.equals("OTHER")) {
                return Result.error("性别参数错误");
            }
            //3. 更新性别
            boolean success = userService.updateGender(userId, gender);
            if (!success) {
                return Result.error("更新性别失败");
            }
            log.info("更新用户性别成功: userId={}, gender={}", userId, gender);
            return Result.success(null, "性别更新成功");
        } catch (Exception e) {
            log.error("更新性别过程中发生错误", e);
            return Result.error("更新性别失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新用户个人资料
     * API接口：PUT /api/user/profile
     * 
     * @param request 包含avatarUrl、nickname、gender、birthday的请求体
     * @return 更新结果
     */
    @PutMapping
    public Result<Void> updateProfile(@RequestBody Map<String, Object> request) {
        try {
            // 1. 获取当前用户ID
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }

            // 2. 获取参数
            String avatarUrl = request.get("avatarUrl") != null ? request.get("avatarUrl").toString() : null;
            String nickname = request.get("nickname") != null ? request.get("nickname").toString() : null;
            String gender = request.get("gender") != null ? request.get("gender").toString() : null;
            LocalDate birthday = null;

            // 3. 解析生日
            if (request.get("birthday") != null) {
                try {
                    birthday = LocalDate.parse(request.get("birthday").toString());
                    // 验证生日日期
                    if (birthday.isAfter(LocalDate.now())) {
                        return Result.error("生日不能是未来日期");
                    }
                } catch (Exception e) {
                    return Result.error("生日格式不正确，请使用yyyy-MM-dd格式");
                }
            }

            // 4. 验证昵称
            if (nickname != null) {
                nickname = nickname.trim();
                if (nickname.isEmpty()) {
                    return Result.error("昵称不能为空");
                }
                if (nickname.length() > 50) {
                    return Result.error("昵称长度不能超过50个字符");
                }
            }

            // 5. 校验性别
            if (gender != null) {
                gender = gender.trim();
                if (gender.isEmpty()) {
                    return Result.error("性别不能为空");
                }
                if (gender.length() > 10) {
                    return Result.error("性别长度不能超过10个字符");
                }
            }

            // 6. 更新个人资料（仅更新非空字段，底层使用IFNULL避免覆盖为null）
            boolean success = userService.updateProfile(userId, avatarUrl, nickname, gender, birthday);
            if (!success) {
                return Result.error("更新个人资料失败");
            }

            log.info("更新用户个人资料成功: userId={}", userId);
            return Result.success(null, "个人资料更新成功");
        } catch (Exception e) {
            log.error("更新个人资料过程中发生错误", e);
            return Result.error("更新个人资料失败：" + e.getMessage());
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(String extension) {
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
    }

    /**
     * 生成相对路径（按日期分目录）
     */
    private String generateRelativePath(String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return datePath + "/" + fileName;
    }
}
