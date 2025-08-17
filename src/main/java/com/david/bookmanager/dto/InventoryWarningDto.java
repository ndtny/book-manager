package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 库存预警DTO
 */
@Data
public class InventoryWarningDto {

    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String isbn;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Integer borrowedQuantity;
    private String categoryName;
} 