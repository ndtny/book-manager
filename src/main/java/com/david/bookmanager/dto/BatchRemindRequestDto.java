package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量催还通知请求DTO
 */
@Data
public class BatchRemindRequestDto {
    
    /**
     * 借阅记录ID列表
     */
    @NotEmpty(message = "借阅记录ID列表不能为空")
    private List<Long> borrowRecordIds;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 通知类型（催还/提醒）
     */
    private String type;
}
