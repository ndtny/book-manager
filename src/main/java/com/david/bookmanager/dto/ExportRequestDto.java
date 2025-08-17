package com.david.bookmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导出请求DTO
 */
@Data
public class ExportRequestDto {

    /**
     * 导出类型：borrow-借阅统计，user-用户统计，inventory-库存统计
     */
    private String exportType;

    /**
     * 导出格式：excel，pdf
     */
    private String format;

    /**
     * 开始日期
     */
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    private LocalDateTime endDate;

    /**
     * 统计维度：daily-按日，weekly-按周，monthly-按月
     */
    private String dimension;
} 