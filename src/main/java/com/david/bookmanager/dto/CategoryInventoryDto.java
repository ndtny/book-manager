package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 分类库存统计DTO
 */
@Data
public class CategoryInventoryDto {

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 图书种类数
     */
    private Long bookTypeCount;

    /**
     * 总库存数量
     */
    private Long totalQuantity;

    /**
     * 可借阅数量
     */
    private Long availableQuantity;
}