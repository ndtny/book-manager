package com.david.bookmanager.dto;

import lombok.Data;

import java.util.List;

/**
 * 借阅统计DTO
 */
@Data
public class BorrowStatisticsDto {

    /**
     * 总借阅次数
     */
    private Long totalBorrows;

    /**
     * 当前借阅数量
     */
    private Long currentBorrows;

    /**
     * 逾期数量
     */
    private Long overdueCount;

    /**
     * 今日借阅数量
     */
    private Long todayBorrows;

    /**
     * 本月借阅数量
     */
    private Long monthBorrows;

    /**
     * 借阅趋势数据
     */
    private List<TrendDataDto> borrowTrend;

    /**
     * 分类借阅统计
     */
    private List<CategoryBorrowDto> categoryBorrows;
}