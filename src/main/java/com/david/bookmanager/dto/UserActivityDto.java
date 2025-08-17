package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 用户活跃度DTO
 */
@Data
public class UserActivityDto {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 借阅次数
     */
    private Long borrowCount;

    /**
     * 活跃度评分
     */
    private Double activityScore;
}