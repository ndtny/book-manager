package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 趋势数据DTO
 */
@Data
public class TrendDataDto {

    /**
     * 日期
     */
    private String date;

    /**
     * 数值
     */
    private Long value;

    /**
     * 标签
     */
    private String label;
}