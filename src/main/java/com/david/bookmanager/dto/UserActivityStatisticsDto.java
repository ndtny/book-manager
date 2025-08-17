package com.david.bookmanager.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户活跃度统计DTO
 */
@Data
public class UserActivityStatisticsDto {

    /**
     * 总用户数
     */
    private Long totalUsers;

    /**
     * 活跃用户数
     */
    private Long activeUsers;

    /**
     * 新增用户数
     */
    private Long newUsers;

    /**
     * 用户活跃度排行
     */
    private List<UserActivityDto> userRanking;

    /**
     * 用户注册趋势
     */
    private List<TrendDataDto> registrationTrend;
}