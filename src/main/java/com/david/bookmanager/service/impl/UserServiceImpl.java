package com.david.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.bookmanager.dto.*;
import com.david.bookmanager.model.User;
import com.david.bookmanager.repository.UserRepository;
import com.david.bookmanager.service.UserService;
import com.david.bookmanager.util.JwtUtil;
import com.david.bookmanager.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    /**
     * 用户注册
     */
    @Override
    public ApiResponse<UserResponseDto> register(UserRegisterDto registerDto) {
        // 验证密码一致性
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return ApiResponse.error("两次输入的密码不一致");
        }

        // 检查用户名唯一性
        if (userRepository.countByUsername(registerDto.getUsername()) > 0) {
            return ApiResponse.error("用户名已存在");
        }

        // 检查邮箱唯一性
        if (userRepository.countByEmail(registerDto.getEmail()) > 0) {
            return ApiResponse.error("邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordUtil.encodePassword(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        user.setRealName(registerDto.getRealName());
        user.setRole("user"); // 默认普通用户
        user.setStatus(1); // 默认启用

        userRepository.insert(user);

        // 返回用户信息（不包含密码）
        UserResponseDto responseDto = convertToResponseDto(user);
        return ApiResponse.success("注册成功", responseDto);
    }

    /**
     * 用户登录
     */
    @Override
    public ApiResponse<LoginResponseDto> login(UserLoginDto loginDto) {
        // 根据用户名查询用户
        User user = userRepository.findByUsername(loginDto.getUsername());
        if (user == null) {
            return ApiResponse.error("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            return ApiResponse.error("账户已被禁用，请联系管理员");
        }

        // 验证密码
        if (!passwordUtil.matches(loginDto.getPassword(), user.getPassword())) {
            return ApiResponse.error("用户名或密码错误");
        }

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());

        // 构建登录响应
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setToken(token);
        loginResponse.setUser(convertToResponseDto(user));
        loginResponse.setLoginTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return ApiResponse.success("登录成功", loginResponse);
    }

    /**
     * 用户登出
     */
    @Override
    public ApiResponse<Void> logout(String token) {
        // 在实际项目中，可以将token加入黑名单
        // 这里简单返回成功
        return ApiResponse.success("登出成功");
    }

    /**
     * 获取用户信息
     */
    @Override
    public ApiResponse<UserResponseDto> getUserProfile(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }

        UserResponseDto responseDto = convertToResponseDto(user);
        return ApiResponse.success(responseDto);
    }

    /**
     * 更新用户信息
     */
    @Override
    public ApiResponse<UserResponseDto> updateUserProfile(Long userId, UserUpdateDto updateDto) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }

        // 检查邮箱唯一性（排除当前用户）
        if (StringUtils.hasText(updateDto.getEmail()) && !updateDto.getEmail().equals(user.getEmail())) {
            User existingUser = userRepository.findByEmail(updateDto.getEmail());
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                return ApiResponse.error("邮箱已被其他用户使用");
            }
        }

        // 更新用户信息
        if (StringUtils.hasText(updateDto.getEmail())) {
            user.setEmail(updateDto.getEmail());
        }
        if (StringUtils.hasText(updateDto.getPhone())) {
            user.setPhone(updateDto.getPhone());
        }
        if (StringUtils.hasText(updateDto.getRealName())) {
            user.setRealName(updateDto.getRealName());
        }

        user.setUpdateTime(LocalDateTime.now());
        userRepository.updateById(user);

        UserResponseDto responseDto = convertToResponseDto(user);
        return ApiResponse.success("更新成功", responseDto);
    }

    /**
     * 修改密码
     */
    @Override
    public ApiResponse<Void> changePassword(Long userId, PasswordChangeDto passwordDto) {
        // 验证新密码一致性
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            return ApiResponse.error("两次输入的新密码不一致");
        }

        User user = userRepository.selectById(userId);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }

        // 验证原密码
        if (!passwordUtil.matches(passwordDto.getOldPassword(), user.getPassword())) {
            return ApiResponse.error("原密码错误");
        }

        // 更新密码
        user.setPassword(passwordUtil.encodePassword(passwordDto.getNewPassword()));
        userRepository.updateById(user);

        return ApiResponse.success("密码修改成功");
    }

    /**
     * 获取用户列表（管理员）
     */
    @Override
    public ApiResponse<Object> getUserList(Integer page, Integer size, String keyword) {
        Page<User> pageParam = new Page<>(page != null ? page : 1, size != null ? size : 10);
        
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("username", keyword)
                    .or()
                    .like("email", keyword)
                    .or()
                    .like("real_name", keyword);
        }
        queryWrapper.orderByDesc("create_time");

        Page<User> userPage = userRepository.selectPage(pageParam, queryWrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("records", userPage.getRecords().stream().map(this::convertToResponseDto).collect(Collectors.toList()));
        result.put("total", userPage.getTotal());
        result.put("current", userPage.getCurrent());
        result.put("size", userPage.getSize());
        result.put("pages", userPage.getPages());

        return ApiResponse.success(result);
    }

    /**
     * 更新用户状态（管理员）
     */
    @Override
    public ApiResponse<Void> updateUserStatus(Long userId, Integer status) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }

        if (status != 0 && status != 1) {
            return ApiResponse.error("状态值无效");
        }

        user.setStatus(status);
        userRepository.updateById(user);

        String message = status == 1 ? "用户已启用" : "用户已禁用";
        return ApiResponse.success(message);
    }

    /**
     * 转换为响应DTO
     */
    private UserResponseDto convertToResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        BeanUtils.copyProperties(user, responseDto);
        return responseDto;
    }
} 