package com.david.bookmanager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 借阅记录实体类
 */
@Data
@TableName("borrow_record")
public class BorrowRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 借阅时间
     */
    private LocalDateTime borrowDate;

    /**
     * 应还时间
     */
    private LocalDateTime dueDate;

    /**
     * 实际归还时间
     */
    private LocalDateTime returnDate;

    /**
     * 状态：1借阅中，2已归还，3逾期
     */
    private Integer status;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 关联查询字段
    /**
     * 用户名
     */
    @TableField(exist = false)
    private String username;

    /**
     * 真实姓名
     */
    @TableField(exist = false)
    private String realName;

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
     * 借阅次数（用于统计）
     */
    @TableField(exist = false)
    private Integer borrowCount;
} 