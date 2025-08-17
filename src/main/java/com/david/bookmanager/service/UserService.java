package com.david.bookmanager.service;

import com.david.bookmanager.dto.*;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    ApiResponse<UserResponseDto> register(UserRegisterDto registerDto);

    /**
     * 用户登录
     */
    ApiResponse<LoginResponseDto> login(UserLoginDto loginDto);

    /**
     * 用户登出
     */
    ApiResponse<Void> logout(String token);

    /**
     * 获取用户信息
     */
    ApiResponse<UserResponseDto> getUserProfile(Long userId);

    /**
     * 更新用户信息
     */
    ApiResponse<UserResponseDto> updateUserProfile(Long userId, UserUpdateDto updateDto);

    /**
     * 修改密码
     */
    ApiResponse<Void> changePassword(Long userId, PasswordChangeDto passwordDto);

    /**
     * 获取用户列表（管理员）
     */
    ApiResponse<Object> getUserList(Integer page, Integer size, String keyword);

    /**
     * 更新用户状态（管理员）
     */
    ApiResponse<Void> updateUserStatus(Long userId, Integer status);
} 