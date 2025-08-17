package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 分类借阅统计DTO
 */
@Data
public class CategoryBorrowDto {

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 借阅次数
     */
    private Long borrowCount;

    /**
     * 占比
     */
    private Double percentage;
}