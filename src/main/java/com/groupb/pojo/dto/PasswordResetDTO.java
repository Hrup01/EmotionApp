package com.groupb.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 密码重置数据传输对象
 * 用于封装用户密码重置所需的信息
 */

@Data
public class PasswordResetDTO {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "\\d{6}$", message = "验证码格式错误")
    private String code;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度必须在6-30位之间")
    private String password;
}
