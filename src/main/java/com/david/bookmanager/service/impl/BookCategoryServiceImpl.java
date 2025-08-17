package com.david.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BookCategoryDto;
import com.david.bookmanager.model.BookCategory;
import com.david.bookmanager.repository.BookCategoryRepository;
import com.david.bookmanager.service.BookCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 图书分类服务实现类
 */
@Service
public class BookCategoryServiceImpl implements BookCategoryService {

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Override
    public ApiResponse<List<BookCategoryDto>> getAllCategories() {
        try {
            List<BookCategory> categories = bookCategoryRepository.selectList(
                new QueryWrapper<BookCategory>().orderByAsc("sort_order")
            );
            
            List<BookCategoryDto> categoryDtos = categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ApiResponse.success(categoryDtos);
        } catch (Exception e) {
            return ApiResponse.error("获取分类列表失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<BookCategoryDto>> getEnabledCategories() {
        try {
            List<BookCategory> categories = bookCategoryRepository.findAllEnabled();
            
            List<BookCategoryDto> categoryDtos = categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            return ApiResponse.success(categoryDtos);
        } catch (Exception e) {
            return ApiResponse.error("获取启用分类列表失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<BookCategoryDto> getCategoryById(Long id) {
        try {
            BookCategory category = bookCategoryRepository.selectById(id);
            if (category == null) {
                return ApiResponse.error("分类不存在");
            }
            
            return ApiResponse.success(convertToDto(category));
        } catch (Exception e) {
            return ApiResponse.error("获取分类详情失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<BookCategoryDto> addCategory(BookCategoryDto categoryDto) {
        try {
            // 检查分类名称是否已存在
            BookCategory existingCategory = bookCategoryRepository.findByName(categoryDto.getName());
            if (existingCategory != null) {
                return ApiResponse.error("分类名称已存在");
            }
            
            BookCategory category = convertToEntity(categoryDto);
            bookCategoryRepository.insert(category);
            
            return ApiResponse.success(convertToDto(category));
        } catch (Exception e) {
            return ApiResponse.error("添加分类失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<BookCategoryDto> updateCategory(Long id, BookCategoryDto categoryDto) {
        try {
            BookCategory existingCategory = bookCategoryRepository.selectById(id);
            if (existingCategory == null) {
                return ApiResponse.error("分类不存在");
            }
            
            // 检查分类名称是否已存在（排除当前分类）
            BookCategory duplicateCategory = bookCategoryRepository.findByName(categoryDto.getName());
            if (duplicateCategory != null && !duplicateCategory.getId().equals(id)) {
                return ApiResponse.error("分类名称已存在");
            }
            
            BookCategory category = convertToEntity(categoryDto);
            category.setId(id);
            bookCategoryRepository.updateById(category);
            
            return ApiResponse.success(convertToDto(category));
        } catch (Exception e) {
            return ApiResponse.error("更新分类失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Void> deleteCategory(Long id) {
        try {
            BookCategory category = bookCategoryRepository.selectById(id);
            if (category == null) {
                return ApiResponse.error("分类不存在");
            }
            
            // 检查分类是否被使用
            int usedCount = bookCategoryRepository.countByCategoryId(id);
            if (usedCount > 0) {
                return ApiResponse.error("该分类下还有图书，无法删除");
            }
            
            bookCategoryRepository.deleteById(id);
            return ApiResponse.success("删除分类成功");
        } catch (Exception e) {
            return ApiResponse.error("删除分类失败: " + e.getMessage());
        }
    }

    /**
     * 转换为DTO
     */
    private BookCategoryDto convertToDto(BookCategory category) {
        BookCategoryDto dto = new BookCategoryDto();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }

    /**
     * 转换为实体
     */
    private BookCategory convertToEntity(BookCategoryDto dto) {
        BookCategory category = new BookCategory();
        BeanUtils.copyProperties(dto, category);
        return category;
    }
} 