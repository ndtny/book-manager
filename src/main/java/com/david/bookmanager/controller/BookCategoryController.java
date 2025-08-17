package com.david.bookmanager.controller;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BookCategoryDto;
import com.david.bookmanager.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 图书分类控制器
 */
@RestController
@RequestMapping("/api/categories")
public class BookCategoryController {

    @Autowired
    private BookCategoryService bookCategoryService;

    /**
     * 获取所有分类
     */
    @GetMapping
    public ApiResponse<List<BookCategoryDto>> getAllCategories() {
        return bookCategoryService.getAllCategories();
    }

    /**
     * 获取所有启用的分类
     */
    @GetMapping("/enabled")
    public ApiResponse<List<BookCategoryDto>> getEnabledCategories() {
        return bookCategoryService.getEnabledCategories();
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public ApiResponse<BookCategoryDto> getCategoryById(@PathVariable Long id) {
        return bookCategoryService.getCategoryById(id);
    }

    /**
     * 添加分类
     */
    @PostMapping
    public ApiResponse<BookCategoryDto> addCategory(@Valid @RequestBody BookCategoryDto categoryDto) {
        return bookCategoryService.addCategory(categoryDto);
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public ApiResponse<BookCategoryDto> updateCategory(@PathVariable Long id, 
                                                      @Valid @RequestBody BookCategoryDto categoryDto) {
        return bookCategoryService.updateCategory(id, categoryDto);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        return bookCategoryService.deleteCategory(id);
    }
} 