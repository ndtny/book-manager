package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 库存搜索DTO
 */
@Data
public class InventorySearchDto {

    private Long bookId;
    private Integer changeType;
    private Integer page = 1;
    private Integer size = 10;
}