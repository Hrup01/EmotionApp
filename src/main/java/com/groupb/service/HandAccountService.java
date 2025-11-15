package com.groupb.service;

import com.groupb.pojo.HandAccount;

public interface HandAccountService {

    /**
     * 保存手账图片记录
     * @param userId 用户ID
     * @param imageUrl 图片URL
     * @param title 标题（可选）
     * @param remark 备注（可选）
     * @return 保存后的 HandAccount
     */
    HandAccount createHandAccount(Long userId, String imageUrl, String title, String remark);
}
