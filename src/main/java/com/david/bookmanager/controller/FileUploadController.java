package com.david.bookmanager.controller;

import com.david.bookmanager.dto.ApiResponse;
import com.david.bookmanager.util.FileUploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    /**
     * 上传图书封面图片
     */
    @PostMapping("/book-cover")
    public ApiResponse<Map<String, String>> uploadBookCover(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = FileUploadUtil.uploadBookCover(file);
            
            Map<String, String> result = new HashMap<>();
            result.put("filePath", filePath);
            result.put("fileName", file.getOriginalFilename());
            
            return ApiResponse.success(result);
        } catch (IOException e) {
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        }
    }
} 