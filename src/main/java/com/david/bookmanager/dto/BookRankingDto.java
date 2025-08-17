package com.david.bookmanager.dto;

import lombok.Data;

/**
 * 图书排行DTO
 */
@Data
public class BookRankingDto {

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
     * 分类名称
     */
    private String categoryName;

    /**
     * 借阅次数
     */
    private Long borrowCount;

    /**
     * 排名
     */
    private Integer ranking;
}