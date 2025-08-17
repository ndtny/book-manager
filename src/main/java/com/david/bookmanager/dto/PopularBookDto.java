package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 热门图书DTO
 */
@Data
public class PopularBookDto {

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 图书标题
     */
    private String bookTitle;

    /**
     * 作者
     */
    private String author;

    /**
     * 借阅次数
     */
    private Long borrowCount;

    /**
     * 排名
     */
    private Integer ranking;
}