package com.david.bookmanager.controller;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BorrowRequestDto;
import com.david.bookmanager.dto.ReturnRequestDto;
import com.david.bookmanager.dto.RemindRequestDto;
import com.david.bookmanager.dto.BatchRemindRequestDto;
import com.david.bookmanager.service.BorrowService;
import com.david.bookmanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 借阅管理控制器
 */
@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 借阅图书
     */
    @PostMapping
    public ApiResponse<Object> borrowBook(@Valid @RequestBody BorrowRequestDto borrowRequest, 
                                        HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return borrowService.borrowBook(borrowRequest, userId);
    }

    /**
     * 归还图书
     */
    @PostMapping("/return")
    public ApiResponse<Object> returnBook(@Valid @RequestBody ReturnRequestDto returnRequest, 
                                        HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return borrowService.returnBook(returnRequest, userId);
    }

    /**
     * 获取逾期记录（管理员）
     */
    @GetMapping("/overdue")
    public ApiResponse<Object> getOverdueRecords() {
        return borrowService.getOverdueRecords();
    }

    /**
     * 获取即将到期的记录
     */
    @GetMapping("/expiring")
    public ApiResponse<Object> getExpiringRecords(@RequestParam(required = false) Integer expireDays) {
        return borrowService.getExpiringRecords(expireDays);
    }

    /**
     * 获取借阅统计
     */
    @GetMapping("/statistics")
    public ApiResponse<Object> getBorrowStatistics() {
        return borrowService.getBorrowStatistics();
    }

    /**
     * 获取热门图书统计
     */
    @GetMapping("/popular")
    public ApiResponse<Object> getPopularBooks(@RequestParam(required = false) Integer limit) {
        return borrowService.getPopularBooks(limit);
    }

    /**
     * 获取借阅记录列表（分页查询）
     */
    @GetMapping("/records")
    public ApiResponse<Object> getBorrowRecords(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return borrowService.getBorrowRecords(page, size, username, bookTitle, status, startDate, endDate);
    }

    /**
     * 发送催还通知
     */
    @PostMapping("/remind")
    public ApiResponse<Object> sendReminder(@Valid @RequestBody RemindRequestDto remindRequest) {
        return borrowService.sendReminder(remindRequest);
    }

    /**
     * 批量发送催还通知
     */
    @PostMapping("/remind/batch")
    public ApiResponse<Object> sendBatchReminder(@Valid @RequestBody BatchRemindRequestDto batchRemindRequest) {
        return borrowService.sendBatchReminder(batchRemindRequest);
    }

    /**
     * 获取借阅记录详情
     */
    @GetMapping("/record/{id}")
    public ApiResponse<Object> getBorrowRecordDetail(@PathVariable Long id) {
        return borrowService.getBorrowRecordDetail(id);
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