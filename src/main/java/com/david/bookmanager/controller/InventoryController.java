package com.david.bookmanager.controller;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.InStockRequestDto;
import com.david.bookmanager.dto.OutStockRequestDto;
import com.david.bookmanager.service.InventoryService;
import com.david.bookmanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 库存管理控制器
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取图书库存信息
     */
    @GetMapping("/book/{bookId}")
    public ApiResponse<Object> getBookInventory(@PathVariable Long bookId) {
        return inventoryService.getBookInventory(bookId);
    }

    /**
     * 获取库存变动记录
     */
    @GetMapping("/records")
    public ApiResponse<Object> getInventoryRecords(
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Integer changeType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return inventoryService.getInventoryRecords(bookId, changeType, page, size);
    }

    /**
     * 图书入库
     */
    @PostMapping("/in-stock")
    public ApiResponse<Object> bookInStock(@Valid @RequestBody InStockRequestDto request,
                                         HttpServletRequest httpRequest) {
        Long operatorId = getUserIdFromRequest(httpRequest);
        return inventoryService.bookInStock(request.getBookId(), request.getQuantity(), 
                                          request.getRemarks(), operatorId);
    }

    /**
     * 图书出库
     */
    @PostMapping("/out-stock")
    public ApiResponse<Object> bookOutStock(@Valid @RequestBody OutStockRequestDto request,
                                          HttpServletRequest httpRequest) {
        Long operatorId = getUserIdFromRequest(httpRequest);
        return inventoryService.bookOutStock(request.getBookId(), request.getQuantity(), 
                                           request.getRemarks(), operatorId);
    }

    /**
     * 获取库存预警信息
     */
    @GetMapping("/warnings")
    public ApiResponse<Object> getInventoryWarnings(
            @RequestParam(required = false) Integer threshold) {
        return inventoryService.getInventoryWarnings(threshold);
    }

    /**
     * 获取库存统计信息
     */
    @GetMapping("/statistics")
    public ApiResponse<Object> getInventoryStatistics() {
        return inventoryService.getInventoryStatistics();
    }

    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                // 检查token是否过期
                if (jwtUtil.isTokenExpired(token)) {
                    return null;
                }
                return jwtUtil.getUserIdFromToken(token);
            } catch (Exception e) {
                // Token无效或过期
                return null;
            }
        }
        return null;
    }
} 