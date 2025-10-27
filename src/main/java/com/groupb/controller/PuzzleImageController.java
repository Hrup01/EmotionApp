package com.groupb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 拼图游戏图片资源控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/puzzle/image")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class PuzzleImageController {
    
    /**
     * 获取拼图游戏图片
     * GET /api/puzzle/image/{theme}/{filename}
     */
    @GetMapping("/{theme}/{filename}")
    public ResponseEntity<Resource> getPuzzleImage(
            @PathVariable String theme,
            @PathVariable String filename) {
        try {
            String imagePath = "static/puzzle/" + theme + "/" + filename;
            Resource resource = new ClassPathResource(imagePath);
            
            if (resource.exists()) {
                // 根据文件扩展名确定媒体类型
                MediaType mediaType = getMediaType(filename);
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                log.warn("拼图图片不存在: {}", imagePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("获取拼图图片失败: theme={}, filename={}", theme, filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取自定义分割图片
     * GET /api/puzzle/image/custom/part_{number}.png
     */
    @GetMapping("/custom/part_{number}.png")
    public ResponseEntity<Resource> getCustomPuzzleImage(
            @PathVariable String number) {
        try {
            String imagePath = "static/puzzle/custom/part_" + number + ".png";
            Resource resource = new ClassPathResource(imagePath);
            
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                log.warn("自定义拼图图片不存在: {}", imagePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("获取自定义拼图图片失败: number={}", number, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据文件扩展名获取媒体类型
     */
    private MediaType getMediaType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "webp":
                return MediaType.parseMediaType("image/webp");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
