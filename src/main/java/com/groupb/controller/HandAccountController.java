package com.groupb.controller;

import com.groupb.pojo.HandAccount;
import com.groupb.pojo.dto.Result;
import com.groupb.service.HandAccountService;
import com.groupb.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/hand/account")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class HandAccountController {

    private final HandAccountService handAccountService;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.max-size:10485760}")
    private long maxFileSize;

    public HandAccountController(HandAccountService handAccountService) {
        this.handAccountService = handAccountService;
    }

    @PostMapping("/upload")
    public Result<Map<String, Object>> uploadHandAccount(@RequestParam("file") MultipartFile file,
                                                         @RequestParam(value = "title", required = false) String title,
                                                         @RequestParam(value = "remark", required = false) String remark) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }

            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            if (file.getSize() > maxFileSize) {
                return Result.error("文件大小不能超过10MB");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只能上传图片文件");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = generateFileName(extension);
            String relativePath = "handaccount/" + generateRelativePath(fileName);
            String fullPath = uploadPath + File.separator + relativePath;

            File directory = new File(fullPath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path path = Paths.get(fullPath);
            Files.copy(file.getInputStream(), path);

            String fileUrl = "/uploads/" + relativePath.replace("\\", "/");

            HandAccount entity = handAccountService.createHandAccount(userId, fileUrl, title, remark);

            Map<String, Object> result = new HashMap<>();
            result.put("id", entity.getId());
            result.put("imageUrl", entity.getImageUrl());
            result.put("title", entity.getTitle());
            result.put("remark", entity.getRemark());

            return Result.success(result, "手账上传成功");
        } catch (Exception e) {
            log.error("手账上传失败", e);
            return Result.error("手账上传失败：" + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    private String generateFileName(String extension) {
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
    }

    private String generateRelativePath(String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return datePath + "/" + fileName;
    }
}
