package com.david.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.model.BookInfo;
import com.david.bookmanager.model.InventoryRecord;
import com.david.bookmanager.repository.BookInfoRepository;
import com.david.bookmanager.repository.InventoryRecordRepository;
import com.david.bookmanager.service.InventoryService;
import com.david.bookmanager.util.BorrowConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    private InventoryRecordRepository inventoryRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getBookInventory(Long bookId) {
        try {
            BookInfo book = bookInfoRepository.selectById(bookId);
            if (book == null) {
                return ApiResponse.error("图书不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("bookId", book.getId());
            result.put("bookTitle", book.getTitle());
            result.put("bookAuthor", book.getAuthor());
            result.put("isbn", book.getIsbn());
            result.put("totalQuantity", book.getTotalQuantity());
            result.put("availableQuantity", book.getAvailableQuantity());
            result.put("borrowedQuantity", book.getTotalQuantity() - book.getAvailableQuantity());
            result.put("status", book.getStatus());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取库存信息失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getInventoryRecords(Long bookId, Integer changeType, Integer page, Integer size) {
        try {
            Page<InventoryRecord> pageParam = new Page<>(page != null ? page : 1, size != null ? size : 10);

            IPage<InventoryRecord> records = inventoryRecordRepository.selectInventoryRecordPage(
                pageParam, bookId, changeType);

            Map<String, Object> result = new HashMap<>();
            result.put("records", records.getRecords());
            result.put("total", records.getTotal());
            result.put("current", records.getCurrent());
            result.put("size", records.getSize());
            result.put("pages", records.getPages());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取库存变动记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<Object> bookInStock(Long bookId, Integer quantity, String remarks, Long operatorId) {
        try {
            if (quantity <= 0) {
                return ApiResponse.error("入库数量必须大于0");
            }

            BookInfo book = bookInfoRepository.selectById(bookId);
            if (book == null) {
                return ApiResponse.error("图书不存在");
            }

            if (book.getStatus() == 0) {
                return ApiResponse.error("图书已下架，无法入库");
            }

            Integer beforeQuantity = book.getTotalQuantity();
            Integer afterQuantity = beforeQuantity + quantity;

            // 更新图书库存
            book.setTotalQuantity(afterQuantity);
            book.setAvailableQuantity(book.getAvailableQuantity() + quantity);
            book.setUpdateTime(LocalDateTime.now());
            bookInfoRepository.updateById(book);

            // 记录库存变动
            InventoryRecord record = new InventoryRecord();
            record.setBookId(bookId);
            record.setChangeType(BorrowConstants.INVENTORY_CHANGE_IN);
            record.setChangeQuantity(quantity);
            record.setBeforeQuantity(beforeQuantity);
            record.setAfterQuantity(afterQuantity);
            record.setOperatorId(operatorId);
            record.setRemarks(remarks);
            record.setCreateTime(LocalDateTime.now());

            inventoryRecordRepository.insert(record);

            Map<String, Object> result = new HashMap<>();
            result.put("bookId", bookId);
            result.put("bookTitle", book.getTitle());
            result.put("quantity", quantity);
            result.put("beforeQuantity", beforeQuantity);
            result.put("afterQuantity", afterQuantity);
            result.put("record", record);

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("图书入库失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<Object> bookOutStock(Long bookId, Integer quantity, String remarks, Long operatorId) {
        try {
            if (quantity <= 0) {
                return ApiResponse.error("出库数量必须大于0");
            }

            BookInfo book = bookInfoRepository.selectById(bookId);
            if (book == null) {
                return ApiResponse.error("图书不存在");
            }

            if (book.getAvailableQuantity() < quantity) {
                return ApiResponse.error("可出库数量不足");
            }

            Integer beforeQuantity = book.getTotalQuantity();
            Integer afterQuantity = beforeQuantity - quantity;

            // 更新图书库存
            book.setTotalQuantity(afterQuantity);
            book.setAvailableQuantity(book.getAvailableQuantity() - quantity);
            book.setUpdateTime(LocalDateTime.now());
            bookInfoRepository.updateById(book);

            // 记录库存变动
            InventoryRecord record = new InventoryRecord();
            record.setBookId(bookId);
            record.setChangeType(BorrowConstants.INVENTORY_CHANGE_OUT);
            record.setChangeQuantity(quantity);
            record.setBeforeQuantity(beforeQuantity);
            record.setAfterQuantity(afterQuantity);
            record.setOperatorId(operatorId);
            record.setRemarks(remarks);
            record.setCreateTime(LocalDateTime.now());

            inventoryRecordRepository.insert(record);

            Map<String, Object> result = new HashMap<>();
            result.put("bookId", bookId);
            result.put("bookTitle", book.getTitle());
            result.put("quantity", quantity);
            result.put("beforeQuantity", beforeQuantity);
            result.put("afterQuantity", afterQuantity);
            result.put("record", record);

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("图书出库失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getInventoryWarnings(Integer threshold) {
        try {
            if (threshold == null) {
                threshold = 5; // 默认预警阈值
            }

            // 使用关联查询获取预警图书的详细信息
            List<BookInfo> warningBooks = bookInfoRepository.selectWarningBooksWithDetails(threshold);

            Map<String, Object> result = new HashMap<>();
            result.put("warningBooks", warningBooks);
            result.put("threshold", threshold);
            result.put("total", warningBooks.size());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取库存预警失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Object> getInventoryStatistics() {
        try {
            // 统计总库存
            QueryWrapper<BookInfo> totalWrapper = new QueryWrapper<>();
            totalWrapper.eq("status", 1);
            List<BookInfo> allBooks = bookInfoRepository.selectList(totalWrapper);

            Integer totalQuantity = 0;
            Integer totalAvailable = 0;
            Integer totalBorrowed = 0;
            Integer lowStockCount = 0;

            for (BookInfo book : allBooks) {
                totalQuantity += book.getTotalQuantity();
                totalAvailable += book.getAvailableQuantity();
                totalBorrowed += (book.getTotalQuantity() - book.getAvailableQuantity());
                
                if (book.getAvailableQuantity() <= 5) {
                    lowStockCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalBooks", allBooks.size());
            result.put("totalQuantity", totalQuantity);
            result.put("totalAvailable", totalAvailable);
            result.put("totalBorrowed", totalBorrowed);
            result.put("lowStockCount", lowStockCount);
            result.put("utilizationRate", totalQuantity > 0 ? 
                String.format("%.2f", (double) totalBorrowed / totalQuantity * 100) + "%" : "0%");

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取库存统计失败: " + e.getMessage());
        }
    }
} 