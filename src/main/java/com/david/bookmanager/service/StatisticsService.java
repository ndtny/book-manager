package com.david.bookmanager.service;

import com.david.bookmanager.dto.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 数据统计服务接口
 */
public interface StatisticsService {

    /**
     * 获取借阅统计
     */
    ApiResponse<Object> getBorrowStatistics();

    /**
     * 获取用户活跃度统计
     */
    ApiResponse<Object> getUserActivityStatistics();

    /**
     * 获取库存统计
     */
    ApiResponse<Object> getInventoryStatistics();

    /**
     * 获取热门图书统计
     */
    ApiResponse<Object> getPopularBookStatistics(Integer limit);

    /**
     * 获取仪表板统计数据
     */
    ApiResponse<Object> getDashboardStatistics();

    /**
     * 导出统计数据
     */
    void exportStatistics(ExportRequestDto exportRequest, HttpServletResponse response);

    /**
     * 获取借阅趋势数据
     */
    ApiResponse<Object> getBorrowTrend(LocalDateTime startDate, LocalDateTime endDate, String dimension, Integer days);

    /**
     * 获取用户注册趋势数据
     */
    ApiResponse<Object> getUserRegistrationTrend(LocalDateTime startDate, LocalDateTime endDate, String dimension);

    /**
     * 获取分类借阅统计
     */
    ApiResponse<Object> getCategoryBorrowStatistics();

    /**
     * 获取分类库存统计
     */
    ApiResponse<Object> getCategoryInventoryStatistics();

    /**
     * 获取用户活跃度排行
     */
    ApiResponse<Object> getUserActivityRanking(Integer limit);

    /**
     * 获取图书借阅排行
     */
    ApiResponse<Object> getBookRanking(Integer limit);
} 