package com.david.bookmanager.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 借阅相关工具类
 */
public class BorrowUtil {

    /**
     * 计算借阅天数
     */
    public static long calculateBorrowDays(LocalDateTime borrowDate, LocalDateTime returnDate) {
        return ChronoUnit.DAYS.between(borrowDate, returnDate);
    }

    /**
     * 计算是否逾期
     */
    public static boolean isOverdue(LocalDateTime dueDate) {
        return LocalDateTime.now().isAfter(dueDate);
    }

    /**
     * 计算逾期天数
     */
    public static long calculateOverdueDays(LocalDateTime dueDate) {
        if (isOverdue(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, LocalDateTime.now());
        }
        return 0;
    }

    /**
     * 计算剩余天数
     */
    public static long calculateRemainingDays(LocalDateTime dueDate) {
        if (isOverdue(dueDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), dueDate);
    }

    /**
     * 格式化借阅状态
     */
    public static String formatBorrowStatus(Integer status) {
        switch (status) {
            case BorrowConstants.STATUS_BORROWING:
                return "借阅中";
            case BorrowConstants.STATUS_RETURNED:
                return "已归还";
            case BorrowConstants.STATUS_OVERDUE:
                return "逾期";
            default:
                return "未知状态";
        }
    }

    /**
     * 格式化库存变动类型
     */
    public static String formatInventoryChangeType(Integer changeType) {
        switch (changeType) {
            case BorrowConstants.INVENTORY_CHANGE_IN:
                return "入库";
            case BorrowConstants.INVENTORY_CHANGE_OUT:
                return "出库";
            case BorrowConstants.INVENTORY_CHANGE_BORROW:
                return "借出";
            case BorrowConstants.INVENTORY_CHANGE_RETURN:
                return "归还";
            default:
                return "未知类型";
        }
    }
} 