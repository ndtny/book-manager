package com.david.bookmanager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存变动记录实体类
 */
@Data
@TableName("inventory_record")
public class InventoryRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 变动类型：1入库，2出库，3借出，4归还
     */
    private Integer changeType;

    /**
     * 变动数量
     */
    private Integer changeQuantity;

    /**
     * 变动前数量
     */
    private Integer beforeQuantity;

    /**
     * 变动后数量
     */
    private Integer afterQuantity;

    /**
     * 操作员ID
     */
    private Long operatorId;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // 关联查询字段
    /**
     * 图书标题
     */
    @TableField(exist = false)
    private String bookTitle;

    /**
     * 图书作者
     */
    @TableField(exist = false)
    private String bookAuthor;

    /**
     * 图书ISBN
     */
    @TableField(exist = false)
    private String bookIsbn;

    /**
     * 图书分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 操作员用户名
     */
    @TableField(exist = false)
    private String operatorName;

    /**
     * 操作员真实姓名
     */
    @TableField(exist = false)
    private String operatorRealName;
} 