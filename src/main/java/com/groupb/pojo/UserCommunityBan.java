package com.groupb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户社区封禁信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommunityBan {
    private Long userId;//用户id
    private String reason;//封禁原因
    private Integer strikeCount;//触发次数
    private LocalDateTime bannedUntil;//封禁到期时间
    private LocalDateTime updatedAt;//更新时间
}


