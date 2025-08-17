package com.david.bookmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存变动记录DTO
 */
@Data
public class InventoryRecordDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private Integer changeType;
    private String changeTypeText;
    private Integer changeQuantity;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private Long operatorId;
    private String operatorName;
    private String remarks;
    private LocalDateTime createTime;
}