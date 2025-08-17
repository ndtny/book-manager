package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 借阅记录DTO
 */
@Data
public class BorrowRecordDto {

    private Long id;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "图书ID不能为空")
    private Long bookId;

    private LocalDateTime borrowDate;

    private LocalDateTime dueDate;

    private LocalDateTime returnDate;

    private Integer status;

    private String remarks;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 关联信息
    private String username;
    private String realName;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;
    private String categoryName;
} 