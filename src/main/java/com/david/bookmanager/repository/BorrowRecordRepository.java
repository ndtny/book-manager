package com.david.bookmanager.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.bookmanager.model.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 借阅记录Repository接口
 */
@Mapper
public interface BorrowRecordRepository extends BaseMapper<BorrowRecord> {

    /**
     * 分页查询借阅记录（带关联信息）
     */
    IPage<BorrowRecord> selectBorrowRecordPage(Page<BorrowRecord> page,
                                               @Param("userId") Long userId,
                                               @Param("bookId") Long bookId,
                                               @Param("status") Integer status,
                                               @Param("keyword") String keyword);

    /**
     * 查询用户的当前借阅记录
     */
    List<BorrowRecord> selectCurrentBorrowsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的借阅历史
     */
    List<BorrowRecord> selectBorrowHistoryByUserId(@Param("userId") Long userId);

    /**
     * 查询逾期记录
     */
    List<BorrowRecord> selectOverdueRecords(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询即将到期的记录（3天内到期）
     */
    List<BorrowRecord> selectExpiringRecords(@Param("currentTime") LocalDateTime currentTime,
                                            @Param("expireDays") Integer expireDays);

    /**
     * 统计用户借阅数量
     */
    Integer countBorrowsByUserId(@Param("userId") Long userId);

    /**
     * 统计图书借阅次数
     */
    Integer countBorrowsByBookId(@Param("bookId") Long bookId);

    /**
     * 查询热门图书统计
     */
    List<BorrowRecord> selectPopularBooks(@Param("limit") Integer limit);

    /**
     * 检查用户是否已借阅该图书
     */
    Integer checkUserBorrowedBook(@Param("userId") Long userId, @Param("bookId") Long bookId);

    /**
     * 分页查询借阅记录（带用户和图书信息）
     */
    IPage<BorrowRecord> selectBorrowRecordsWithDetails(Page<BorrowRecord> page,
                                                       @Param("username") String username,
                                                       @Param("bookTitle") String bookTitle,
                                                       @Param("status") Integer status,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate);

    /**
     * 根据ID列表批量查询借阅记录
     */
    List<BorrowRecord> selectBatchIds(@Param("ids") List<Long> ids);
} 