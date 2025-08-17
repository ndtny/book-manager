package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 催还通知请求DTO
 */
@Data
public class RemindRequestDto {
    
    /**
     * 借阅记录ID
     */
    @NotNull(message = "借阅记录ID不能为空")
    private Long borrowRecordId;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 通知类型（催还/提醒）
     */
    private String type;
}
