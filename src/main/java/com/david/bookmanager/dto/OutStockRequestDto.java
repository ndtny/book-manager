package com.david.bookmanager.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 出库请求DTO
 */
@Data
public class OutStockRequestDto {

    @NotNull(message = "图书ID不能为空")
    private Long bookId;

    @NotNull(message = "出库数量不能为空")
//    @Min(value = 1, message = "出库数量必须大于0")
    private Integer quantity;

    private String remarks;
}