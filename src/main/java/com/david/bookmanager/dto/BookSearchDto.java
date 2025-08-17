package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 图书搜索DTO
 */
@Data
public class BookSearchDto {

    private String keyword;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private Long categoryId;
    private Integer status;
    private Integer page = 1;
    private Integer size = 10;
    private String sortBy = "createTime";
    private String sortOrder = "desc";
} 