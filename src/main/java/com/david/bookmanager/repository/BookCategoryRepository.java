package com.david.bookmanager.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.bookmanager.model.BookCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 图书分类数据访问层
 */
@Mapper
public interface BookCategoryRepository extends BaseMapper<BookCategory> {

    /**
     * 根据名称查询分类
     */
    @Select("SELECT * FROM book_category WHERE category_name = #{name}")
    BookCategory findByName(String name);

    /**
     * 查询所有启用的分类
     */
    @Select("SELECT * FROM book_category WHERE status = 1 ORDER BY sort_order ASC")
    List<BookCategory> findAllEnabled();

    /**
     * 检查分类是否被使用
     */
    @Select("SELECT COUNT(*) FROM book_info WHERE category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);
} 