package com.david.bookmanager.dto;

import lombok.Data;

import java.util.List;

/**
 * 库存统计DTO
 */
@Data
public class InventoryStatisticsDto {

    /**
     * 总图书种类
     */
    private Long totalBookTypes;

    /**
     * 总库存数量
     */
    private Long totalQuantity;

    /**
     * 可借阅数量
     */
    private Long availableQuantity;

    /**
     * 库存预警数量
     */
    private Long warningCount;

    /**
     * 库存周转率
     */
    private Double turnoverRate;

    /**
     * 分类库存统计
     */
    private List<CategoryInventoryDto> categoryInventory;
}