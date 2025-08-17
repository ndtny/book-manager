package com.david.bookmanager.controller;

import com.david.bookmanager.dto.*;
import com.david.bookmanager.service.UserService;
import com.david.bookmanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    public ApiResponse<UserResponseDto> getUserProfile(HttpServletRequest request) {
        // 从JWT中获取用户ID（这里简化处理，实际应该从JWT中解析）
        Long userId = getUserIdFromRequest(request);
        return userService.getUserProfile(userId);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    public ApiResponse<UserResponseDto> updateUserProfile(@Valid @RequestBody UserUpdateDto updateDto, 
                                                         HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return userService.updateUserProfile(userId, updateDto);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody PasswordChangeDto passwordDto, 
                                           HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return userService.changePassword(userId, passwordDto);
    }

    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                // 检查token是否过期
                if (jwtUtil.isTokenExpired(token)) {
                    return null;
                }
                return jwtUtil.getUserIdFromToken(token);
            } catch (Exception e) {
                // Token无效或过期
                return null;
            }
        }
        return null;
    }
} 