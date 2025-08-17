package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponseDto {

    /**
     * JWT令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserResponseDto user;

    /**
     * 登录时间
     */
    private String loginTime;
} 