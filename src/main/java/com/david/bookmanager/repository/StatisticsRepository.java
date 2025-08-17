package com.david.bookmanager.repository;

import com.david.bookmanager.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据统计Repository接口
 */
@Mapper
public interface StatisticsRepository {

    /**
     * 获取借阅统计概览
     */
    BorrowStatisticsDto selectBorrowStatistics();

    /**
     * 获取今日借阅数量
     */
    Long selectTodayBorrowCount(@Param("today") LocalDateTime today);

    /**
     * 获取本月借阅数量
     */
    Long selectMonthBorrowCount(@Param("startOfMonth") LocalDateTime startOfMonth, 
                               @Param("endOfMonth") LocalDateTime endOfMonth);

    /**
     * 获取借阅趋势数据
     */
    List<TrendDataDto> selectBorrowTrend(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        @Param("dimension") String dimension);

    /**
     * 获取分类借阅统计
     */
    List<CategoryBorrowDto> selectCategoryBorrowStatistics();

    /**
     * 获取用户活跃度统计
     */
    UserActivityStatisticsDto selectUserActivityStatistics();

    /**
     * 获取用户活跃度排行
     */
    List<UserActivityDto> selectUserActivityRanking(@Param("limit") Integer limit);

    /**
     * 获取用户注册趋势
     */
    List<TrendDataDto> selectUserRegistrationTrend(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  @Param("dimension") String dimension);

    /**
     * 获取库存统计概览
     */
    InventoryStatisticsDto selectInventoryStatistics();

    /**
     * 获取分类库存统计
     */
    List<CategoryInventoryDto> selectCategoryInventoryStatistics();

    /**
     * 获取热门图书统计
     */
    List<PopularBookDto> selectPopularBooks(@Param("limit") Integer limit);

    /**
     * 获取图书借阅排行
     */
    List<BookRankingDto> selectBookRanking(@Param("limit") Integer limit);

    /**
     * 获取借阅统计详情（用于导出）
     */
    List<Object> selectBorrowStatisticsForExport(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    /**
     * 获取用户统计详情（用于导出）
     */
    List<Object> selectUserStatisticsForExport(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    /**
     * 获取库存统计详情（用于导出）
     */
    List<Object> selectInventoryStatisticsForExport();
} 