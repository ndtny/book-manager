package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 图书信息DTO
 */
@Data
public class BookInfoDto {

    private Long id;

    @NotBlank(message = "ISBN不能为空")
    private String isbn;

    @NotBlank(message = "图书标题不能为空")
    private String title;

    @NotBlank(message = "作者不能为空")
    private String author;

    @NotBlank(message = "出版社不能为空")
    private String publisher;

    private LocalDate publishDate;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private String description;

    private String coverImage;

//    @DecimalMin(value = "0.0", message = "价格不能为负数")
    private BigDecimal price;

    @NotNull(message = "总库存数量不能为空")
//    @Min(value = 0, message = "总库存数量不能为负数")
    private Integer totalQuantity;

    @NotNull(message = "可借阅数量不能为空")
    @Min(value = 0, message = "可借阅数量不能为负数")
    private Integer availableQuantity;

    @NotNull(message = "状态不能为空")
    private Integer status;
} 