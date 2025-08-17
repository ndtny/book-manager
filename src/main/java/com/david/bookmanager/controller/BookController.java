package com.david.bookmanager.controller;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.dto.BookInfoDto;
import com.david.bookmanager.dto.BookSearchDto;
import com.david.bookmanager.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 图书信息控制器
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookInfoService bookInfoService;

    /**
     * 获取图书列表
     */
    @GetMapping
    public ApiResponse<Object> getBookList(BookSearchDto searchDto) {
        return bookInfoService.getBookList(searchDto);
    }

    /**
     * 根据ID获取图书详情
     */
    @GetMapping("/{id}")
    public ApiResponse<BookInfoDto> getBookById(@PathVariable Long id) {
        return bookInfoService.getBookById(id);
    }

    /**
     * 添加图书
     */
    @PostMapping
    public ApiResponse<BookInfoDto> addBook(@Valid @RequestBody BookInfoDto bookDto) {
        return bookInfoService.addBook(bookDto);
    }

    /**
     * 更新图书信息
     */
    @PutMapping("/{id}")
    public ApiResponse<BookInfoDto> updateBook(@PathVariable Long id, 
                                              @Valid @RequestBody BookInfoDto bookDto) {
        return bookInfoService.updateBook(id, bookDto);
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBook(@PathVariable Long id) {
        return bookInfoService.deleteBook(id);
    }

    /**
     * 搜索图书
     */
    @GetMapping("/search")
    public ApiResponse<Object> searchBooks(@RequestParam String keyword) {
        return bookInfoService.searchBooks(keyword);
    }

    /**
     * 更新图书状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateBookStatus(@PathVariable Long id, 
                                             @RequestParam Integer status) {
        return bookInfoService.updateBookStatus(id, status);
    }
} 