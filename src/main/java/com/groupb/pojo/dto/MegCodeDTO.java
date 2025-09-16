package com.groupb.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 手机验证码传输对象
 * 用于封装传输手机验证码信息
 * */

@Data
public class MegCodeDTO {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "\\d{6}$", message = "验证码格式错误")
    private String code;
}
