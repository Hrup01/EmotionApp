package com.groupb.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户数据传输对象
 * 用于在系统中传输用户相关数据
 */

@Data
public class UserDTO {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    private String username;

    private String password;



    private String avatarUrl;
}
