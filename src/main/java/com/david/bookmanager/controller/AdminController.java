package com.david.bookmanager.controller;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     */
    @GetMapping("/users")
    public ApiResponse<Object> getUserList(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String keyword) {
        return userService.getUserList(page, size, keyword);
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/users/{id}")
    public ApiResponse<Void> updateUserStatus(@PathVariable("id") Long userId,
                                            @RequestParam Integer status) {
        return userService.updateUserStatus(userId, status);
    }
} 