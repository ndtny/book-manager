package com.david.bookmanager.service;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BorrowRequestDto;
import com.david.bookmanager.dto.ReturnRequestDto;
import com.david.bookmanager.dto.RemindRequestDto;
import com.david.bookmanager.dto.BatchRemindRequestDto;

/**
 * 借阅服务接口
 */
public interface BorrowService {

    /**
     * 借阅图书
     */
    ApiResponse<Object> borrowBook(BorrowRequestDto borrowRequest, Long userId);

    /**
     * 归还图书
     */
    ApiResponse<Object> returnBook(ReturnRequestDto returnRequest, Long userId);



    /**
     * 获取逾期记录（管理员）
     */
    ApiResponse<Object> getOverdueRecords();

    /**
     * 获取即将到期的记录
     */
    ApiResponse<Object> getExpiringRecords(Integer expireDays);

    /**
     * 获取借阅统计
     */
    ApiResponse<Object> getBorrowStatistics();

    /**
     * 获取热门图书统计
     */
    ApiResponse<Object> getPopularBooks(Integer limit);

    /**
     * 获取借阅记录列表（分页查询）
     */
    ApiResponse<Object> getBorrowRecords(Integer page, Integer size, String username, 
                                       String bookTitle, Integer status, 
                                       String startDate, String endDate);

    /**
     * 更新借阅状态（定时任务）
     */
    void updateBorrowStatus();

    /**
     * 发送催还通知
     */
    ApiResponse<Object> sendReminder(RemindRequestDto remindRequest);

    /**
     * 批量发送催还通知
     */
    ApiResponse<Object> sendBatchReminder(BatchRemindRequestDto batchRemindRequest);

    /**
     * 获取借阅记录详情
     */
    ApiResponse<Object> getBorrowRecordDetail(Long id);
} 