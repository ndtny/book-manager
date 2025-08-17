package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 仪表板数据DTO
 */
@Data
public class DashboardStatisticsDto {

    /**
     * 借阅统计
     */
    private BorrowStatisticsDto borrowStatistics;

    /**
     * 用户统计
     */
    private UserActivityStatisticsDto userStatistics;

    /**
     * 库存统计
     */
    private InventoryStatisticsDto inventoryStatistics;

    /**
     * 热门图书
     */
    private PopularBookStatisticsDto popularBookStatistics;
}