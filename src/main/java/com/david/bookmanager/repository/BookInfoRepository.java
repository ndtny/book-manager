package com.david.bookmanager.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.bookmanager.model.BookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 图书信息数据访问层
 */
@Mapper
public interface BookInfoRepository extends BaseMapper<BookInfo> {

    /**
     * 根据ISBN查询图书
     */
    @Select("SELECT * FROM book_info WHERE isbn = #{isbn}")
    BookInfo findByIsbn(String isbn);

    /**
     * 检查ISBN是否存在
     */
    @Select("SELECT COUNT(*) FROM book_info WHERE isbn = #{isbn}")
    int countByIsbn(String isbn);

    /**
     * 检查ISBN是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM book_info WHERE isbn = #{isbn} AND id != #{id}")
    int countByIsbnExcludeId(@Param("isbn") String isbn, @Param("id") Long id);

    /**
     * 分页查询图书信息
     */
    IPage<BookInfo> selectBookPage(Page<BookInfo> page, @Param("title") String title,
                                   @Param("author") String author, @Param("isbn") String isbn,
                                   @Param("publisher") String publisher, @Param("categoryId") Long categoryId,
                                   @Param("status") Integer status);

    /**
     * 关键词搜索图书
     */
    @Select("SELECT * FROM book_info WHERE (title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR author LIKE CONCAT('%', #{keyword}, '%') " +
            "OR isbn LIKE CONCAT('%', #{keyword}, '%') " +
            "OR publisher LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND status = 1 ORDER BY create_time DESC")
    List<BookInfo> searchByKeyword(String keyword);

    /**
     * 检查图书是否有借阅记录
     */
    @Select("SELECT COUNT(*) FROM borrow_record WHERE book_id = #{bookId}")
    int countBorrowRecordsByBookId(Long bookId);

    /**
     * 查询库存预警图书（带关联信息）
     */
    List<BookInfo> selectWarningBooksWithDetails(@Param("threshold") Integer threshold);
} 