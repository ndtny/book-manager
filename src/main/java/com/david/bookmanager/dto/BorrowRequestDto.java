package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 借阅请求DTO
 */
@Data
public class BorrowRequestDto {

    @NotNull(message = "图书ID不能为空")
    private Long bookId;

    private String remarks;
} 