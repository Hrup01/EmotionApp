package com.groupb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 私信实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateMessage {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private Integer status;
    private LocalDateTime createdAt;
}


