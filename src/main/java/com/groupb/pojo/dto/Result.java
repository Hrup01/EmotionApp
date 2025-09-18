package com.groupb.pojo.dto;

import lombok.Data;

/**
 * 统一响应结果类
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    private Integer code; // 响应码
    private String msg; // 响应信息
    private T data; // 数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 500;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> unauthorized(String msg) {
        Result<T> result = new Result<>();
        result.code = 401;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> forbidden(String msg) {
        Result<T> result = new Result<>();
        result.code = 403;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> notFound(String msg) {
        Result<T> result = new Result<>();
        result.code = 404;
        result.msg = msg;
        return result;
    }
}

