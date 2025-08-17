package com.david.bookmanager.dto;

import lombok.Data;

import java.util.List;

/**
 * 热门图书统计DTO
 */
@Data
public class PopularBookStatisticsDto {

    /**
     * 热门图书列表
     */
    private List<PopularBookDto> popularBooks;

    /**
     * 借阅排行
     */
    private List<BookRankingDto> bookRanking;
}