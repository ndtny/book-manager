package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 图书分类DTO
 */
@Data
public class BookCategoryDto {

    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "排序顺序不能为空")
    private Integer sortOrder;

    @NotNull(message = "状态不能为空")
    private Integer status;
} 