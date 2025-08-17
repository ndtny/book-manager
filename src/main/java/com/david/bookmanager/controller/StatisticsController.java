package com.david.bookmanager.controller;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.ExportRequestDto;
import com.david.bookmanager.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 数据统计控制器
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取借阅统计
     */
    @GetMapping("/borrow")
    public ApiResponse<Object> getBorrowStatistics() {
        return statisticsService.getBorrowStatistics();
    }

    /**
     * 获取用户活跃度统计
     */
    @GetMapping("/user")
    public ApiResponse<Object> getUserActivityStatistics() {
        return statisticsService.getUserActivityStatistics();
    }

    /**
     * 获取库存统计
     */
    @GetMapping("/inventory")
    public ApiResponse<Object> getInventoryStatistics() {
        return statisticsService.getInventoryStatistics();
    }

    /**
     * 获取热门图书统计
     */
    @GetMapping("/popular")
    public ApiResponse<Object> getPopularBookStatistics(
            @RequestParam(required = false) Integer limit) {
        return statisticsService.getPopularBookStatistics(limit);
    }

    /**
     * 获取仪表板数据
     */
    @GetMapping("/dashboard")
    public ApiResponse<Object> getDashboardStatistics() {
        return statisticsService.getDashboardStatistics();
    }

    /**
     * 导出统计数据
     */
    @PostMapping("/export")
    public void exportStatistics(@Valid @RequestBody ExportRequestDto exportRequest,
                                HttpServletResponse response) {
        statisticsService.exportStatistics(exportRequest, response);
    }

    /**
     * 获取借阅趋势数据
     */
    @GetMapping("/borrow/trend")
    public ApiResponse<Object> getBorrowTrend(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(required = false) String dimension,
            @RequestParam(required = false) Integer days) {
        return statisticsService.getBorrowTrend(startDate, endDate, dimension, days);
    }

    /**
     * 获取用户注册趋势数据
     */
    @GetMapping("/user/trend")
    public ApiResponse<Object> getUserRegistrationTrend(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(required = false) String dimension) {
        return statisticsService.getUserRegistrationTrend(startDate, endDate, dimension);
    }

    /**
     * 获取分类借阅统计
     */
    @GetMapping("/category/borrow")
    public ApiResponse<Object> getCategoryBorrowStatistics() {
        return statisticsService.getCategoryBorrowStatistics();
    }

    /**
     * 获取分类库存统计
     */
    @GetMapping("/category/inventory")
    public ApiResponse<Object> getCategoryInventoryStatistics() {
        return statisticsService.getCategoryInventoryStatistics();
    }

    /**
     * 获取用户活跃度排行
     */
    @GetMapping("/user/ranking")
    public ApiResponse<Object> getUserActivityRanking(
            @RequestParam(required = false) Integer limit) {
        return statisticsService.getUserActivityRanking(limit);
    }

    /**
     * 获取图书借阅排行
     */
    @GetMapping("/book/ranking")
    public ApiResponse<Object> getBookRanking(
            @RequestParam(required = false) Integer limit) {
        return statisticsService.getBookRanking(limit);
    }
} 