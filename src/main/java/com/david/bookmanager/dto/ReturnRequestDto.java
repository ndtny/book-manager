package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 归还请求DTO
 */
@Data
public class ReturnRequestDto {

    @NotNull(message = "借阅记录ID不能为空")
    private Long borrowRecordId;

    private String remarks;
} 