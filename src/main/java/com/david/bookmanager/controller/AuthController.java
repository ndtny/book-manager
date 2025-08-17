package com.david.bookmanager.controller;

import com.david.bookmanager.dto.*;
import com.david.bookmanager.service.UserService;
import com.david.bookmanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<UserResponseDto> register(@Valid @RequestBody UserRegisterDto registerDto) {
        return userService.register(registerDto);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody UserLoginDto loginDto) {
        return userService.login(loginDto);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return userService.logout(token);
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    public ApiResponse<LoginResponseDto> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // 验证当前token是否有效（即使过期也可以刷新）
            String username = jwtUtil.getUsernameFromToken(token);
            Long userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            
            // 生成新的token
            String newToken = jwtUtil.generateToken(username, userId, role);
            
            LoginResponseDto response = new LoginResponseDto();
            response.setToken(newToken);
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("令牌刷新失败: " + e.getMessage());
        }
    }

    /**
     * 检查令牌状态
     */
    @GetMapping("/token/status")
    public ApiResponse<Object> checkTokenStatus(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            String username = jwtUtil.getUsernameFromToken(token);
            Long userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            Date expiration = jwtUtil.getExpirationDateFromToken(token);
            
            Map<String, Object> result = new HashMap<>();
            result.put("valid", !jwtUtil.isTokenExpired(token));
            result.put("expiringSoon", jwtUtil.isTokenExpiringSoon(token));
            result.put("username", username);
            result.put("userId", userId);
            result.put("role", role);
            result.put("expiration", expiration);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("valid", false);
            result.put("expiringSoon", false);
            result.put("error", e.getMessage());
            
            return ApiResponse.success(result);
        }
    }
} 