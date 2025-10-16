package com.groupb.controller;

import com.groupb.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

/**
 * 文件上传控制器
 * 处理图片和其他文件的上传
 */
@Slf4j
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class FileUploadController {

    @Value("${file.upload.path:/uploads}")
    private String uploadPath;

    @Value("${file.upload.max-size:10485760}")
    private long maxFileSize; // 10MB

    /**
     * 上传单个图片文件
     * API接口：POST /api/upload/image
     * 
     * @param file 图片文件
     * @return 上传结果，包含文件URL
     */
    @PostMapping("/image")
    public Result<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 验证文件
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 2. 验证文件大小
            if (file.getSize() > maxFileSize) {
                return Result.error("文件大小不能超过10MB");
            }
            
            // 3. 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只能上传图片文件");
            }
            
            // 4. 生成文件名和路径
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = generateFileName(extension);
            String relativePath = generateRelativePath(fileName);
            // 使用File.separator确保跨平台兼容性
            String fullPath = uploadPath + File.separator + relativePath;
            
            // 5. 创建目录
            File directory = new File(fullPath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // 6. 保存文件
            Path path = Paths.get(fullPath);
            Files.copy(file.getInputStream(), path);
            
            // 7. 生成访问URL
            String fileUrl = "/uploads/" + relativePath;
            
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", fileName);
            result.put("originalName", originalFilename);
            result.put("fileSize", file.getSize());
            result.put("fileUrl", fileUrl);
            result.put("contentType", contentType);
            
            log.info("图片上传成功: fileName={}, size={}, url={}", fileName, file.getSize(), fileUrl);
            return Result.success(result, "图片上传成功");
            
        } catch (IOException e) {
            log.error("图片上传失败", e);
            return Result.error("图片上传失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("图片上传过程中发生错误", e);
            return Result.error("图片上传失败，请稍后重试");
        }
    }

    /**
     * 批量上传图片文件
     * API接口：POST /api/upload/images
     * 
     * @param files 图片文件数组
     * @return 上传结果，包含文件URL列表
     */
    @PostMapping("/images")
    public Result<List<Map<String, Object>>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        try {
            if (files == null || files.length == 0) {
                return Result.error("文件不能为空");
            }
            
            if (files.length > 9) {
                return Result.error("最多只能上传9张图片");
            }
            
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                try {
                    // 验证文件
                    if (file.isEmpty()) {
                        errors.add("第" + (i + 1) + "个文件为空");
                        continue;
                    }
                    
                    if (file.getSize() > maxFileSize) {
                        errors.add("第" + (i + 1) + "个文件超过10MB");
                        continue;
                    }
                    
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        errors.add("第" + (i + 1) + "个文件不是图片格式");
                        continue;
                    }
                    
                    // 生成文件名和路径
                    String originalFilename = file.getOriginalFilename();
                    String extension = getFileExtension(originalFilename);
                    String fileName = generateFileName(extension);
                    String relativePath = generateRelativePath(fileName);
                    // 使用File.separator确保跨平台兼容性
                    String fullPath = uploadPath + File.separator + relativePath;
                    
                    // 创建目录
                    File directory = new File(fullPath).getParentFile();
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    
                    // 保存文件
                    Path path = Paths.get(fullPath);
                    Files.copy(file.getInputStream(), path);
                    
                    // 生成访问URL
                    String fileUrl = "/uploads/" + relativePath;
                    
                    Map<String, Object> fileResult = new HashMap<>();
                    fileResult.put("fileName", fileName);
                    fileResult.put("originalName", originalFilename);
                    fileResult.put("fileSize", file.getSize());
                    fileResult.put("fileUrl", fileUrl);
                    fileResult.put("contentType", contentType);
                    
                    results.add(fileResult);
                    log.info("批量上传图片成功: fileName={}, size={}, url={}", fileName, file.getSize(), fileUrl);
                    
                } catch (IOException e) {
                    errors.add("第" + (i + 1) + "个文件上传失败：" + e.getMessage());
                    log.error("批量上传第{}个文件失败", i + 1, e);
                }
            }
            
            if (results.isEmpty()) {
                return Result.error("所有文件上传失败：" + String.join("; ", errors));
            }
            
            String message = "成功上传" + results.size() + "个文件";
            if (!errors.isEmpty()) {
                message += "，" + errors.size() + "个文件上传失败";
            }
            
            return Result.success(results, message);
            
        } catch (Exception e) {
            log.error("批量上传图片过程中发生错误", e);
            return Result.error("批量上传失败，请稍后重试");
        }
    }

    /**
     * 删除上传的文件
     * API接口：DELETE /api/upload/file
     * 
     * @param fileUrl 文件URL
     * @return 删除结果
     */
    @DeleteMapping("/file")
    public Result<Void> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.trim().isEmpty()) {
                return Result.error("文件URL不能为空");
            }
            
            // 移除URL前缀，获取相对路径
            String relativePath = fileUrl.replaceFirst("^/uploads/", "");
            // 使用File.separator确保跨平台兼容性
            String fullPath = uploadPath + File.separator + relativePath;
            
            File file = new File(fullPath);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    log.info("文件删除成功: {}", fileUrl);
                    return Result.success(null, "文件删除成功");
                } else {
                    return Result.error("文件删除失败");
                }
            } else {
                return Result.error("文件不存在");
            }
            
        } catch (Exception e) {
            log.error("删除文件失败", e);
            return Result.error("删除文件失败：" + e.getMessage());
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
