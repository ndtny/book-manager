package com.david.bookmanager.service.impl;

import com.david.bookmanager.dto.*;
import com.david.bookmanager.repository.StatisticsRepository;
import com.david.bookmanager.service.StatisticsService;
import com.david.bookmanager.util.ExcelExportUtil;
import com.david.bookmanager.util.PdfExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getBorrowStatistics() {
        try {
            BorrowStatisticsDto borrowStatistics = statisticsRepository.selectBorrowStatistics();
            
            // 获取今日和本月借阅数量
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
            
            borrowStatistics.setTodayBorrows(statisticsRepository.selectTodayBorrowCount(today));
            borrowStatistics.setMonthBorrows(statisticsRepository.selectMonthBorrowCount(startOfMonth, endOfMonth));
            
            // 获取借阅趋势数据（最近30天）
            LocalDateTime startDate = LocalDateTime.now().minusDays(30);
            LocalDateTime endDate = LocalDateTime.now();
            List<TrendDataDto> borrowTrend = statisticsRepository.selectBorrowTrend(startDate, endDate, "daily");
            borrowStatistics.setBorrowTrend(borrowTrend);
            
            // 获取分类借阅统计
            List<CategoryBorrowDto> categoryBorrows = statisticsRepository.selectCategoryBorrowStatistics();
            borrowStatistics.setCategoryBorrows(categoryBorrows);
            
            Map<String, Object> result = new HashMap<>();
            result.put("borrowStatistics", borrowStatistics);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取借阅统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getUserActivityStatistics() {
        try {
            UserActivityStatisticsDto userStatistics = statisticsRepository.selectUserActivityStatistics();
            
            // 获取用户注册趋势（最近30天）
            LocalDateTime startDate = LocalDateTime.now().minusDays(30);
            LocalDateTime endDate = LocalDateTime.now();
            List<TrendDataDto> registrationTrend = statisticsRepository.selectUserRegistrationTrend(startDate, endDate, "daily");
            userStatistics.setRegistrationTrend(registrationTrend);
            
            // 获取用户活跃度排行
            List<UserActivityDto> userRanking = statisticsRepository.selectUserActivityRanking(10);
            userStatistics.setUserRanking(userRanking);
            
            Map<String, Object> result = new HashMap<>();
            result.put("userStatistics", userStatistics);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取用户活跃度统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getInventoryStatistics() {
        try {
            InventoryStatisticsDto inventoryStatistics = statisticsRepository.selectInventoryStatistics();
            
            // 获取分类库存统计
            List<CategoryInventoryDto> categoryInventory = statisticsRepository.selectCategoryInventoryStatistics();
            inventoryStatistics.setCategoryInventory(categoryInventory);
            
            Map<String, Object> result = new HashMap<>();
            result.put("inventoryStatistics", inventoryStatistics);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取库存统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getPopularBookStatistics(Integer limit) {
        try {
            if (limit == null) {
                limit = 10;
            }
            
            PopularBookStatisticsDto popularBookStatistics = new PopularBookStatisticsDto();
            
            // 获取热门图书
            List<PopularBookDto> popularBooks = statisticsRepository.selectPopularBooks(limit);
            popularBookStatistics.setPopularBooks(popularBooks);
            
            // 获取图书借阅排行
            List<BookRankingDto> bookRanking = statisticsRepository.selectBookRanking(limit);
            popularBookStatistics.setBookRanking(bookRanking);
            
            Map<String, Object> result = new HashMap<>();
            result.put("popularBookStatistics", popularBookStatistics);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取热门图书统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getDashboardStatistics() {
        try {
            DashboardStatisticsDto dashboardStatistics = new DashboardStatisticsDto();
            
            // 获取借阅统计
            BorrowStatisticsDto borrowStatistics = statisticsRepository.selectBorrowStatistics();
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
            
            borrowStatistics.setTodayBorrows(statisticsRepository.selectTodayBorrowCount(today));
            borrowStatistics.setMonthBorrows(statisticsRepository.selectMonthBorrowCount(startOfMonth, endOfMonth));
            dashboardStatistics.setBorrowStatistics(borrowStatistics);
            
            // 获取用户统计
            UserActivityStatisticsDto userStatistics = statisticsRepository.selectUserActivityStatistics();
            List<UserActivityDto> userRanking = statisticsRepository.selectUserActivityRanking(5);
            userStatistics.setUserRanking(userRanking);
            dashboardStatistics.setUserStatistics(userStatistics);
            
            // 获取库存统计
            InventoryStatisticsDto inventoryStatistics = statisticsRepository.selectInventoryStatistics();
            dashboardStatistics.setInventoryStatistics(inventoryStatistics);
            
            // 获取热门图书统计
            PopularBookStatisticsDto popularBookStatistics = new PopularBookStatisticsDto();
            List<PopularBookDto> popularBooks = statisticsRepository.selectPopularBooks(5);
            popularBookStatistics.setPopularBooks(popularBooks);
            dashboardStatistics.setPopularBookStatistics(popularBookStatistics);
            
            Map<String, Object> result = new HashMap<>();
            result.put("dashboardStatistics", dashboardStatistics);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取仪表板统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public void exportStatistics(ExportRequestDto exportRequest, HttpServletResponse response) {
        try {
            String exportType = exportRequest.getExportType();
            String format = exportRequest.getFormat();
            LocalDateTime startDate = exportRequest.getStartDate();
            LocalDateTime endDate = exportRequest.getEndDate();
            
            List<Object> data = null;
            String fileName = "";
            
            switch (exportType) {
                case "borrow":
                    data = statisticsRepository.selectBorrowStatisticsForExport(startDate, endDate);
                    fileName = "借阅统计_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    break;
                case "user":
                    data = statisticsRepository.selectUserStatisticsForExport(startDate, endDate);
                    fileName = "用户统计_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    break;
                case "inventory":
                    data = statisticsRepository.selectInventoryStatisticsForExport();
                    fileName = "库存统计_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    break;
                default:
                    throw new IllegalArgumentException("不支持的导出类型: " + exportType);
            }
            
            if ("excel".equalsIgnoreCase(format)) {
                ExcelExportUtil.exportToExcel(data, fileName, response);
            } else if ("pdf".equalsIgnoreCase(format)) {
                PdfExportUtil.exportToPdf(data, fileName, response);
            } else {
                throw new IllegalArgumentException("不支持的导出格式: " + format);
            }
        } catch (Exception e) {
            throw new RuntimeException("导出统计数据失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getBorrowTrend(LocalDateTime startDate, LocalDateTime endDate, String dimension, Integer days) {
        try {
            if (days != null) {
                startDate = LocalDateTime.now().minusDays(days);
                endDate = LocalDateTime.now();
            } else {
                if (startDate == null) {
                    startDate = LocalDateTime.now().minusDays(30);
                }
                if (endDate == null) {
                    endDate = LocalDateTime.now();
                }
            }
            if (dimension == null) {
                dimension = "daily";
            }
            
            List<TrendDataDto> borrowTrend = statisticsRepository.selectBorrowTrend(startDate, endDate, dimension);
            
            Map<String, Object> result = new HashMap<>();
            result.put("borrowTrend", borrowTrend);
            result.put("startDate", startDate);
            result.put("endDate", endDate);
            result.put("dimension", dimension);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取借阅趋势数据失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getUserRegistrationTrend(LocalDateTime startDate, LocalDateTime endDate, String dimension) {
        try {
            if (startDate == null) {
                startDate = LocalDateTime.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDateTime.now();
            }
            if (dimension == null) {
                dimension = "daily";
            }
            
            List<TrendDataDto> registrationTrend = statisticsRepository.selectUserRegistrationTrend(startDate, endDate, dimension);
            
            Map<String, Object> result = new HashMap<>();
            result.put("registrationTrend", registrationTrend);
            result.put("startDate", startDate);
            result.put("endDate", endDate);
            result.put("dimension", dimension);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取用户注册趋势数据失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getCategoryBorrowStatistics() {
        try {
            List<CategoryBorrowDto> categoryBorrows = statisticsRepository.selectCategoryBorrowStatistics();
            
            Map<String, Object> result = new HashMap<>();
            result.put("categoryBorrows", categoryBorrows);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取分类借阅统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getCategoryInventoryStatistics() {
        try {
            List<CategoryInventoryDto> categoryInventory = statisticsRepository.selectCategoryInventoryStatistics();
            
            Map<String, Object> result = new HashMap<>();
            result.put("categoryInventory", categoryInventory);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取分类库存统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getUserActivityRanking(Integer limit) {
        try {
            if (limit == null) {
                limit = 10;
            }
            
            List<UserActivityDto> userRanking = statisticsRepository.selectUserActivityRanking(limit);
            
            Map<String, Object> result = new HashMap<>();
            result.put("userRanking", userRanking);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取用户活跃度排行失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getBookRanking(Integer limit) {
        try {
            if (limit == null) {
                limit = 10;
            }
            
            List<BookRankingDto> bookRanking = statisticsRepository.selectBookRanking(limit);
            
            Map<String, Object> result = new HashMap<>();
            result.put("bookRanking", bookRanking);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取图书借阅排行失败: " + e.getMessage());
        }
    }
} 