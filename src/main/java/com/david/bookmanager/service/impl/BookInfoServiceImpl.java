package com.david.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BookInfoDto;
import com.david.bookmanager.dto.BookSearchDto;
import com.david.bookmanager.model.BookInfo;
import com.david.bookmanager.repository.BookInfoRepository;
import com.david.bookmanager.service.BookInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图书信息服务实现类
 */
@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Override
    public ApiResponse<Object> getBookList(BookSearchDto searchDto) {
        try {
            Page<BookInfo> page = new Page<>(searchDto.getPage(), searchDto.getSize());
            
            IPage<BookInfo> bookPage = bookInfoRepository.selectBookPage(
                page,
                searchDto.getTitle(),
                searchDto.getAuthor(),
                searchDto.getIsbn(),
                searchDto.getPublisher(),
                searchDto.getCategoryId(),
                searchDto.getStatus()
            );
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", bookPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
            result.put("total", bookPage.getTotal());
            result.put("current", bookPage.getCurrent());
            result.put("size", bookPage.getSize());
            result.put("pages", bookPage.getPages());
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取图书列表失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<BookInfoDto> getBookById(Long id) {
        try {
            BookInfo book = bookInfoRepository.selectById(id);
            if (book == null) {
                return ApiResponse.error("图书不存在");
            }
            
            return ApiResponse.success(convertToDto(book));
        } catch (Exception e) {
            return ApiResponse.error("获取图书详情失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<BookInfoDto> addBook(BookInfoDto bookDto) {
        try {
            // 检查ISBN是否已存在
            int isbnCount = bookInfoRepository.countByIsbn(bookDto.getIsbn());
            if (isbnCount > 0) {
                return ApiResponse.error("ISBN已存在");
            }
            
            // 验证库存数量
            if (bookDto.getTotalQuantity() < bookDto.getAvailableQuantity()) {
                return ApiResponse.error("可借阅数量不能大于总库存数量");
            }
            
            BookInfo book = convertToEntity(bookDto);
            bookInfoRepository.insert(book);
            
            return ApiResponse.success(convertToDto(book));
        } catch (Exception e) {
            return ApiResponse.error("添加图书失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<BookInfoDto> updateBook(Long id, BookInfoDto bookDto) {
        try {
            BookInfo existingBook = bookInfoRepository.selectById(id);
            if (existingBook == null) {
                return ApiResponse.error("图书不存在");
            }
            
            // 检查ISBN是否已存在（排除当前图书）
            int isbnCount = bookInfoRepository.countByIsbnExcludeId(bookDto.getIsbn(), id);
            if (isbnCount > 0) {
                return ApiResponse.error("ISBN已存在");
            }
            
            // 验证库存数量
            if (bookDto.getTotalQuantity() < bookDto.getAvailableQuantity()) {
                return ApiResponse.error("可借阅数量不能大于总库存数量");
            }
            
            BookInfo book = convertToEntity(bookDto);
            book.setId(id);
            bookInfoRepository.updateById(book);
            
            return ApiResponse.success(convertToDto(book));
        } catch (Exception e) {
            return ApiResponse.error("更新图书失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Void> deleteBook(Long id) {
        try {
            BookInfo book = bookInfoRepository.selectById(id);
            if (book == null) {
                return ApiResponse.error("图书不存在");
            }
            
            // 检查图书是否有借阅记录
            int borrowCount = bookInfoRepository.countBorrowRecordsByBookId(id);
            if (borrowCount > 0) {
                return ApiResponse.error("该图书有借阅记录，无法删除");
            }
            
            bookInfoRepository.deleteById(id);
            return ApiResponse.success("删除图书成功");
        } catch (Exception e) {
            return ApiResponse.error("删除图书失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Object> searchBooks(String keyword) {
        try {
            List<BookInfo> books = bookInfoRepository.searchByKeyword(keyword);
            
            List<BookInfoDto> bookDtos = books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("books", bookDtos);
            result.put("total", bookDtos.size());
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("搜索图书失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Void> updateBookStatus(Long id, Integer status) {
        try {
            BookInfo book = bookInfoRepository.selectById(id);
            if (book == null) {
                return ApiResponse.error("图书不存在");
            }
            
            book.setStatus(status);
            bookInfoRepository.updateById(book);
            
            return ApiResponse.success("更新图书状态成功");
        } catch (Exception e) {
            return ApiResponse.error("更新图书状态失败: " + e.getMessage());
        }
    }

    /**
     * 转换为DTO
     */
    private BookInfoDto convertToDto(BookInfo book) {
        BookInfoDto dto = new BookInfoDto();
        BeanUtils.copyProperties(book, dto);
        return dto;
    }

    /**
     * 转换为实体
     */
    private BookInfo convertToEntity(BookInfoDto dto) {
        BookInfo book = new BookInfo();
        BeanUtils.copyProperties(dto, book);
        return book;
    }
} 