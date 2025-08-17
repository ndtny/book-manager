package com.david.bookmanager.util;

/**
 * 借阅相关常量
 */
public class BorrowConstants {

    /**
     * 借阅状态：1-借阅中
     */
    public static final int STATUS_BORROWING = 1;

    /**
     * 借阅状态：2-已归还
     */
    public static final int STATUS_RETURNED = 2;

    /**
     * 借阅状态：3-逾期
     */
    public static final int STATUS_OVERDUE = 3;

    /**
     * 库存变动类型：1-入库
     */
    public static final int INVENTORY_CHANGE_IN = 1;

    /**
     * 库存变动类型：2-出库
     */
    public static final int INVENTORY_CHANGE_OUT = 2;

    /**
     * 库存变动类型：3-借出
     */
    public static final int INVENTORY_CHANGE_BORROW = 3;

    /**
     * 库存变动类型：4-归还
     */
    public static final int INVENTORY_CHANGE_RETURN = 4;

    /**
     * 默认借阅期限（天）
     */
    public static final int DEFAULT_BORROW_DAYS = 30;

    /**
     * 最大借阅数量
     */
    public static final int MAX_BORROW_COUNT = 5;

    /**
     * 默认即将到期提醒天数
     */
    public static final int DEFAULT_EXPIRE_DAYS = 3;
} 