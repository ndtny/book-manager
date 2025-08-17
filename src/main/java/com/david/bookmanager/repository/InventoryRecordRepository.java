package com.david.bookmanager.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.bookmanager.model.InventoryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存变动记录Repository接口
 */
@Mapper
public interface InventoryRecordRepository extends BaseMapper<InventoryRecord> {

    /**
     * 分页查询库存变动记录（带关联信息）
     */
    IPage<InventoryRecord> selectInventoryRecordPage(Page<InventoryRecord> page,
                                                    @Param("bookId") Long bookId,
                                                    @Param("changeType") Integer changeType);

    /**
     * 查询图书的库存变动记录
     */
    List<InventoryRecord> selectByBookId(@Param("bookId") Long bookId);

    /**
     * 查询指定类型的库存变动记录
     */
    List<InventoryRecord> selectByChangeType(@Param("changeType") Integer changeType);

    /**
     * 统计图书库存变动次数
     */
    Integer countByBookId(@Param("bookId") Long bookId);

    /**
     * 统计指定类型的库存变动次数
     */
    Integer countByChangeType(@Param("changeType") Integer changeType);

    /**
     * 查询最近的库存变动记录
     */
    List<InventoryRecord> selectRecentRecords(@Param("limit") Integer limit);
} 