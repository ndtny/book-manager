package com.david.bookmanager.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 图书信息实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("book_info")
public class BookInfo {

    /**
     * 图书ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * ISBN号
     */
    @TableField("isbn")
    private String isbn;

    /**
     * 图书标题
     */
    @TableField("title")
    private String title;

    /**
     * 作者
     */
    @TableField("author")
    private String author;

    /**
     * 出版社
     */
    @TableField("publisher")
    private String publisher;

    /**
     * 出版日期
     */
    @TableField("publish_date")
    private LocalDate publishDate;

    /**
     * 分类ID，外键
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 图书描述
     */
    @TableField("description")
    private String description;

    /**
     * 封面图片路径
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 图书价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 总库存数量
     */
    @TableField("total_quantity")
    private Integer totalQuantity;

    /**
     * 可借阅数量
     */
    @TableField("available_quantity")
    private Integer availableQuantity;

    /**
     * 状态，1正常，0下架
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 关联查询字段
    /**
     * 分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 已借出数量
     */
    @TableField(exist = false)
    private Integer borrowedQuantity;
} 