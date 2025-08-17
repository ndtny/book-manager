package com.david.bookmanager.service;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BookInfoDto;
import com.david.bookmanager.dto.BookSearchDto;

/**
 * 图书信息服务接口
 */
public interface BookInfoService {

    /**
     * 获取图书列表
     */
    ApiResponse<Object> getBookList(BookSearchDto searchDto);

    /**
     * 根据ID获取图书详情
     */
    ApiResponse<BookInfoDto> getBookById(Long id);

    /**
     * 添加图书
     */
    ApiResponse<BookInfoDto> addBook(BookInfoDto bookDto);

    /**
     * 更新图书信息
     */
    ApiResponse<BookInfoDto> updateBook(Long id, BookInfoDto bookDto);

    /**
     * 删除图书
     */
    ApiResponse<Void> deleteBook(Long id);

    /**
     * 搜索图书
     */
    ApiResponse<Object> searchBooks(String keyword);

    /**
     * 更新图书状态
     */
    ApiResponse<Void> updateBookStatus(Long id, Integer status);
} 