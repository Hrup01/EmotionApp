package com.groupb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼图游戏图片资源控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/puzzle/image")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class PuzzleImageController {

    /**
     * 根据难度等级获取完整拼图图片
     * GET /api/puzzle/image/rank/{rank}/full
     * 例如: /api/puzzle/image/rank/rank1/full (获取静态资源 /puzzle/rank1/allrank1.jpg)
     */
    @GetMapping("/rank/{rank}/full")
    public ResponseEntity<Resource> getFullPuzzleImageByRank(
            @PathVariable String rank) {
        try {
            // 构建图片路径: image/Rank1/allrank1.jpg
            String rankCapitalized = rank.substring(0, 1).toUpperCase() + rank.substring(1).toLowerCase();
            String filename = "all" + rank.toLowerCase() + ".jpg";
            String imagePath = "image/" + rankCapitalized + "/" + filename;
            Resource resource = new ClassPathResource(imagePath);
            
            if (resource.exists()) {
                MediaType mediaType = getMediaType(filename);
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                log.warn("完整拼图图片不存在: {}", imagePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("获取完整拼图图片失败: rank={}", rank, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据难度等级和编号获取拼图部分图片
     * GET /api/puzzle/image/rank/{rank}/part/{number}
     * 例如: /api/puzzle/image/rank/rank1/part/01 (获取静态资源 /puzzle/rank1/part_01.png)
     */
    @GetMapping("/rank/{rank}/part/{number}")
    public ResponseEntity<Resource> getPuzzlePartImage(
            @PathVariable String rank,
            @PathVariable String number) {
        try {
            // 确保编号格式为两位数字，如 01, 02, ..., 16
            String formattedNumber = String.format("%02d", Integer.parseInt(number));
            String filename = "part_" + formattedNumber + ".png";
            String rankCapitalized = rank.substring(0, 1).toUpperCase() + rank.substring(1).toLowerCase();
            // 构建图片路径: image/Rank1/part_01.png
            String imagePath = "image/" + rankCapitalized + "/" + filename;
            Resource resource = new ClassPathResource(imagePath);
            
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                log.warn("拼图部分图片不存在: {}", imagePath);
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            log.error("拼图编号格式错误: number={}", number);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("获取拼图部分图片失败: rank={}, number={}", rank, number, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据难度等级直接获取图片（支持完整图片和部分图片）
     * GET /api/puzzle/image/rank/{rank}/{filename}
     * 例如: /api/puzzle/image/rank/rank1/allrank1.jpg (获取静态资源 /puzzle/rank1/allrank1.jpg)
     *      /api/puzzle/image/rank/rank1/part_01.png (获取静态资源 /puzzle/rank1/part_01.png)
     */
    @GetMapping("/rank/{rank}/{filename}")
    public ResponseEntity<Resource> getPuzzleImageByRank(
            @PathVariable String rank,
            @PathVariable String filename) {
        try {
            String rankCapitalized = rank.substring(0, 1).toUpperCase() + rank.substring(1).toLowerCase();
            // 构建图片路径: image/Rank1/{filename}
            String imagePath = "image/" + rankCapitalized + "/" + filename;
            Resource resource = new ClassPathResource(imagePath);
            
            if (resource.exists()) {
                MediaType mediaType = getMediaType(filename);
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                log.warn("拼图图片不存在: {}", imagePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("获取拼图图片失败: rank={}, filename={}", rank, filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取可用的难度等级列表（rank1, rank2等）
     * GET /api/puzzle/image/ranks
     * 返回resources/image目录下可用的难度等级文件夹
     */
    @GetMapping("/ranks")
    public ResponseEntity<List<String>> getAvailableRanks() {
        try {
            List<String> ranks = new ArrayList<>();
            // 检查resources/image目录下的Rank文件夹
            // 目前已知有Rank1，后续可以动态扫描
            String[] knownRanks = {"rank1"}; // 可以根据实际目录动态扫描
            
            for (String rank : knownRanks) {
                // 验证该rank的完整图片是否存在
                String rankCapitalized = rank.substring(0, 1).toUpperCase() + rank.substring(1).toLowerCase();
                String imagePath = "image/" + rankCapitalized + "/all" + rank.toLowerCase() + ".jpg";
                ClassPathResource resource = new ClassPathResource(imagePath);
                if (resource.exists()) {
                    ranks.add(rank);
                }
            }
            
            return ResponseEntity.ok(ranks);
        } catch (Exception e) {
            log.error("获取难度等级列表失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取静态资源URL（用于前端直接访问）
     * 返回静态资源的API访问路径
     * 
     * 完整图片: /api/puzzle/image/rank/rank1/full
     * 部分图片: /api/puzzle/image/rank/rank1/part/01
     */
    @GetMapping("/rank/{rank}/url")
    public ResponseEntity<Map<String, Object>> getImageUrls(@PathVariable String rank) {
        try {
            String rankLower = rank.toLowerCase();
            Map<String, Object> urls = new HashMap<>();
            
            // 完整图片URL（通过API）
            urls.put("full", "/api/puzzle/image/rank/" + rankLower + "/full");
            
            // 部分图片URL列表（通过API）
            Map<String, String> parts = new HashMap<>();
            // rank1是4x4拼图，共16块
            int totalParts = 16; // 可以根据rank动态确定
            for (int i = 1; i <= totalParts; i++) {
                String number = String.format("%02d", i);
                parts.put(number, "/api/puzzle/image/rank/" + rankLower + "/part/" + i);
            }
            urls.put("parts", parts);
            
            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            log.error("获取图片URL失败: rank={}", rank, e);
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
