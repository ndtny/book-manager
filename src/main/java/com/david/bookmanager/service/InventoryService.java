package com.david.bookmanager.service;

import com.david.bookmanager.dto.ApiResponse;

/**
 * 库存管理服务接口
 */
public interface InventoryService {

    /**
     * 获取图书库存信息
     */
    ApiResponse<Object> getBookInventory(Long bookId);

    /**
     * 获取库存变动记录
     */
    ApiResponse<Object> getInventoryRecords(Long bookId, Integer changeType, Integer page, Integer size);

    /**
     * 图书入库
     */
    ApiResponse<Object> bookInStock(Long bookId, Integer quantity, String remarks, Long operatorId);

    /**
     * 图书出库
     */
    ApiResponse<Object> bookOutStock(Long bookId, Integer quantity, String remarks, Long operatorId);

    /**
     * 获取库存预警信息
     */
    ApiResponse<Object> getInventoryWarnings(Integer threshold);

    /**
     * 获取库存统计信息
     */
    ApiResponse<Object> getInventoryStatistics();
} 