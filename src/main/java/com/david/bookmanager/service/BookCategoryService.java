package com.david.bookmanager.service;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BookCategoryDto;

import java.util.List;

/**
 * 图书分类服务接口
 */
public interface BookCategoryService {

    /**
     * 获取所有分类
     */
    ApiResponse<List<BookCategoryDto>> getAllCategories();

    /**
     * 获取所有启用的分类
     */
    ApiResponse<List<BookCategoryDto>> getEnabledCategories();

    /**
     * 根据ID获取分类
     */
    ApiResponse<BookCategoryDto> getCategoryById(Long id);

    /**
     * 添加分类
     */
    ApiResponse<BookCategoryDto> addCategory(BookCategoryDto categoryDto);

    /**
     * 更新分类
     */
    ApiResponse<BookCategoryDto> updateCategory(Long id, BookCategoryDto categoryDto);

    /**
     * 删除分类
     */
    ApiResponse<Void> deleteCategory(Long id);
} 