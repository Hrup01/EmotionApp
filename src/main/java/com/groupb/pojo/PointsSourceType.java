package com.groupb.pojo;

/**
 * 积分来源类型枚举，用于标识不同的业务场景。
 * 可根据实际业务扩展，商城、活动等场景统一使用该枚举。
 */
public enum PointsSourceType {

    /**
     * 完成游戏任务
     */
    GAME_COMPLETE,

    /**
     * 连续签到奖励
     */
    CONTINUOUS_CHECK_IN,

    /**
     * 单次签到奖励
     */
    DAILY_CHECK_IN,

    /**
     * 手动运营活动发放
     */
    OPERATION_EVENT,

    /**
     * 商城消费退款或系统补偿
     */
    COMPENSATION,

    /**
     * 其它自定义来源，须额外校验
     */
    CUSTOM;

    /**
     * 尝试根据字符串获得枚举，忽略大小写并兼容空值。
     *
     * @param value 前端传入的来源类型
     * @return 匹配的枚举，若无法识别则返回null
     */
    public static PointsSourceType safeValueOf(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return PointsSourceType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}


