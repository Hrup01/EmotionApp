package com.groupb.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HandAccount {
    private Long id;
    private Long userId;
    private String imageUrl;
    private String title;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer status;
}
