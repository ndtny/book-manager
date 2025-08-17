package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 库存信息DTO
 */
@Data
public class InventoryDto {

    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String isbn;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Integer borrowedQuantity;
    private Integer status;
}











