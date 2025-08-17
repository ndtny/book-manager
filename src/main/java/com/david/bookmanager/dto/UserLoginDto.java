package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录DTO
 */
@Data
public class UserLoginDto {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
} 