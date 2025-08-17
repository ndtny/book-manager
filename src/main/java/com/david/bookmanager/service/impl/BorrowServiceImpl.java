package com.david.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BorrowRequestDto;
import com.david.bookmanager.dto.ReturnRequestDto;
import com.david.bookmanager.dto.RemindRequestDto;
import com.david.bookmanager.dto.BatchRemindRequestDto;
import com.david.bookmanager.model.BookInfo;
import com.david.bookmanager.model.BorrowRecord;
import com.david.bookmanager.model.InventoryRecord;
import com.david.bookmanager.model.User;
import com.david.bookmanager.repository.BookInfoRepository;
import com.david.bookmanager.repository.BorrowRecordRepository;
import com.david.bookmanager.repository.InventoryRecordRepository;
import com.david.bookmanager.repository.UserRepository;
import com.david.bookmanager.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRecordRepository inventoryRecordRepository;

    private static final int BORROW_DAYS = 30;
    private static final int MAX_BORROW_COUNT = 5;

    @Override
    @Transactional
    public ApiResponse<Object> borrowBook(BorrowRequestDto borrowRequest, Long userId) {
        try {
            User user = userRepository.selectById(userId);
            if (user == null || user.getStatus() == 0) {
                return ApiResponse.error("用户不存在或已被禁用");
            }

            BookInfo book = bookInfoRepository.selectById(borrowRequest.getBookId());
            if (book == null || book.getStatus() == 0 || book.getAvailableQuantity() <= 0) {
                return ApiResponse.error("图书不存在或无法借阅");
            }

            Integer borrowedCount = borrowRecordRepository.checkUserBorrowedBook(userId, borrowRequest.getBookId());
            if (borrowedCount > 0) {
                return ApiResponse.error("您已借阅该图书");
            }

            Integer userBorrowCount = borrowRecordRepository.countBorrowsByUserId(userId);
            if (userBorrowCount >= MAX_BORROW_COUNT) {
                return ApiResponse.error("已达到最大借阅数量限制");
            }

            BorrowRecord borrowRecord = new BorrowRecord();
            borrowRecord.setUserId(userId);
            borrowRecord.setBookId(borrowRequest.getBookId());
            borrowRecord.setBorrowDate(LocalDateTime.now());
            borrowRecord.setDueDate(LocalDateTime.now().plusDays(BORROW_DAYS));
            borrowRecord.setStatus(1);
            borrowRecord.setRemarks(borrowRequest.getRemarks());
            borrowRecord.setCreateTime(LocalDateTime.now());
            borrowRecord.setUpdateTime(LocalDateTime.now());

            borrowRecordRepository.insert(borrowRecord);

            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            book.setUpdateTime(LocalDateTime.now());
            bookInfoRepository.updateById(book);

            recordInventoryChange(book.getId(), 3, 1, book.getAvailableQuantity() + 1, 
                                book.getAvailableQuantity(), userId, "图书借阅");

            Map<String, Object> result = new HashMap<>();
            result.put("borrowRecord", borrowRecord);
            result.put("dueDate", borrowRecord.getDueDate());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("借阅失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<Object> returnBook(ReturnRequestDto returnRequest, Long userId) {
        try {
            BorrowRecord borrowRecord = borrowRecordRepository.selectById(returnRequest.getBorrowRecordId());
            if (borrowRecord == null) {
                return ApiResponse.error("借阅记录不存在");
            }

            User user = userRepository.selectById(userId);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            if (!user.getRole().equals("admin") && !borrowRecord.getUserId().equals(userId)) {
                return ApiResponse.error("无权限归还该图书");
            }

            if (borrowRecord.getStatus() == 2) {
                return ApiResponse.error("该图书已归还");
            }

            borrowRecord.setReturnDate(LocalDateTime.now());
            borrowRecord.setStatus(2);
            borrowRecord.setRemarks(returnRequest.getRemarks());
            borrowRecord.setUpdateTime(LocalDateTime.now());
            borrowRecordRepository.updateById(borrowRecord);

            BookInfo book = bookInfoRepository.selectById(borrowRecord.getBookId());
            if (book != null) {
                book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                book.setUpdateTime(LocalDateTime.now());
                bookInfoRepository.updateById(book);

                recordInventoryChange(book.getId(), 4, 1, book.getAvailableQuantity() - 1, 
                                    book.getAvailableQuantity(), userId, "图书归还");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("borrowRecord", borrowRecord);
            result.put("returnDate", borrowRecord.getReturnDate());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("归还失败: " + e.getMessage());
        }
    }







    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getOverdueRecords() {
        try {
            List<BorrowRecord> overdueRecords = borrowRecordRepository.selectOverdueRecords(LocalDateTime.now());

            Map<String, Object> result = new HashMap<>();
            result.put("overdueRecords", overdueRecords);
            result.put("total", overdueRecords.size());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取逾期记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getExpiringRecords(Integer expireDays) {
        try {
            if (expireDays == null) {
                expireDays = 3;
            }
            List<BorrowRecord> expiringRecords = borrowRecordRepository.selectExpiringRecords(LocalDateTime.now(), expireDays);

            Map<String, Object> result = new HashMap<>();
            result.put("expiringRecords", expiringRecords);
            result.put("total", expiringRecords.size());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取即将到期记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getBorrowStatistics() {
        try {
            QueryWrapper<BorrowRecord> totalWrapper = new QueryWrapper<>();
            Long totalBorrows = borrowRecordRepository.selectCount(totalWrapper);

            QueryWrapper<BorrowRecord> currentWrapper = new QueryWrapper<>();
            currentWrapper.eq("status", 1);
            Long currentBorrows = borrowRecordRepository.selectCount(currentWrapper);

            QueryWrapper<BorrowRecord> overdueWrapper = new QueryWrapper<>();
            overdueWrapper.eq("status", 3);
            Long overdueCount = borrowRecordRepository.selectCount(overdueWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("totalBorrows", totalBorrows);
            result.put("currentBorrows", currentBorrows);
            result.put("overdueCount", overdueCount);

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取借阅统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getPopularBooks(Integer limit) {
        try {
            if (limit == null) {
                limit = 10;
            }
            List<BorrowRecord> popularBooks = borrowRecordRepository.selectPopularBooks(limit);

            Map<String, Object> result = new HashMap<>();
            result.put("popularBooks", popularBooks);
            result.put("total", popularBooks.size());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取热门图书失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getBorrowRecords(Integer page, Integer size, String username, 
                                               String bookTitle, Integer status, 
                                               String startDate, String endDate) {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (size == null || size < 1) {
                size = 10;
            }

            Page<BorrowRecord> pageParam = new Page<>(page, size);
            IPage<BorrowRecord> result = borrowRecordRepository.selectBorrowRecordsWithDetails(
                pageParam, username, bookTitle, status, startDate, endDate);

            Map<String, Object> response = new HashMap<>();
            response.put("borrowRecords", result.getRecords());
            response.put("total", result.getTotal());
            response.put("current", result.getCurrent());
            response.put("size", result.getSize());

            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("获取借阅记录失败: " + e.getMessage());
        }
    }

    @Override
    public void updateBorrowStatus() {
        try {
            QueryWrapper<BorrowRecord> overdueWrapper = new QueryWrapper<>();
            overdueWrapper.eq("status", 1)
                         .lt("due_date", LocalDateTime.now());
            
            BorrowRecord updateRecord = new BorrowRecord();
            updateRecord.setStatus(3);
            updateRecord.setUpdateTime(LocalDateTime.now());
            
            borrowRecordRepository.update(updateRecord, overdueWrapper);
        } catch (Exception e) {
            System.err.println("更新借阅状态失败: " + e.getMessage());
        }
    }

    private void recordInventoryChange(Long bookId, Integer changeType, Integer changeQuantity, 
                                     Integer beforeQuantity, Integer afterQuantity, 
                                     Long operatorId, String remarks) {
        InventoryRecord record = new InventoryRecord();
        record.setBookId(bookId);
        record.setChangeType(changeType);
        record.setChangeQuantity(changeQuantity);
        record.setBeforeQuantity(beforeQuantity);
        record.setAfterQuantity(afterQuantity);
        record.setOperatorId(operatorId);
        record.setRemarks(remarks);
        record.setCreateTime(LocalDateTime.now());
        
        inventoryRecordRepository.insert(record);
    }

    @Override
    public ApiResponse<Object> sendReminder(RemindRequestDto remindRequest) {
        try {
            BorrowRecord borrowRecord = borrowRecordRepository.selectById(remindRequest.getBorrowRecordId());
            if (borrowRecord == null) {
                return ApiResponse.error("借阅记录不存在");
            }

            // 这里可以集成邮件、短信或其他通知服务
            // 目前只是记录日志
            System.out.println("发送" + remindRequest.getType() + "通知给用户ID: " + borrowRecord.getUserId());
            System.out.println("通知内容: " + remindRequest.getContent());
            System.out.println("图书ID: " + borrowRecord.getBookId());

            Map<String, Object> result = new HashMap<>();
            result.put("message", "通知发送成功");
            result.put("borrowRecordId", borrowRecord.getId());
            result.put("userId", borrowRecord.getUserId());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("发送通知失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Object> sendBatchReminder(BatchRemindRequestDto batchRemindRequest) {
        try {
            List<BorrowRecord> borrowRecords = borrowRecordRepository.selectBatchIds(batchRemindRequest.getBorrowRecordIds());
            if (borrowRecords.isEmpty()) {
                return ApiResponse.error("未找到指定的借阅记录");
            }

            // 这里可以集成邮件、短信或其他通知服务
            // 目前只是记录日志
            System.out.println("批量发送" + batchRemindRequest.getType() + "通知");
            System.out.println("通知内容: " + batchRemindRequest.getContent());
            System.out.println("涉及借阅记录数量: " + borrowRecords.size());

            for (BorrowRecord record : borrowRecords) {
                System.out.println("发送通知给用户ID: " + record.getUserId() + ", 图书ID: " + record.getBookId());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("message", "批量通知发送成功");
            result.put("totalCount", borrowRecords.size());
            result.put("successCount", borrowRecords.size());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("批量发送通知失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getBorrowRecordDetail(Long id) {
        try {
            BorrowRecord borrowRecord = borrowRecordRepository.selectById(id);
            if (borrowRecord == null) {
                return ApiResponse.error("借阅记录不存在");
            }

            // 获取关联的用户信息
            User user = userRepository.selectById(borrowRecord.getUserId());
            if (user != null) {
                borrowRecord.setUsername(user.getUsername());
                borrowRecord.setRealName(user.getRealName());
            }

            // 获取关联的图书信息
            BookInfo book = bookInfoRepository.selectById(borrowRecord.getBookId());
            if (book != null) {
                borrowRecord.setBookTitle(book.getTitle());
                borrowRecord.setBookAuthor(book.getAuthor());
                borrowRecord.setBookIsbn(book.getIsbn());
            }

            return ApiResponse.success(borrowRecord);
        } catch (Exception e) {
            return ApiResponse.error("获取借阅记录详情失败: " + e.getMessage());
        }
    }
} 