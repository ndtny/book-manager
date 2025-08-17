package com.david.bookmanager.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传工具类
 */
public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String BOOK_COVER_DIR = "book-covers/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};

    /**
     * 上传图书封面图片
     */
    public static String uploadBookCover(MultipartFile file) throws IOException {
        // 验证文件
        validateFile(file);
        
        // 创建上传目录
        String uploadPath = UPLOAD_DIR + BOOK_COVER_DIR;
        createDirectoryIfNotExists(uploadPath);
        
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String filename = UUID.randomUUID().toString() + extension;
        
        // 保存文件
        Path filePath = Paths.get(uploadPath + filename);
        Files.write(filePath, file.getBytes());
        
        return BOOK_COVER_DIR + filename;
    }

    /**
     * 验证文件
     */
    private static void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("文件不能为空");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("文件大小不能超过5MB");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("文件名不能为空");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean isValidExtension = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                isValidExtension = true;
                break;
            }
        }
        
        if (!isValidExtension) {
            throw new IOException("不支持的文件格式，只支持jpg、jpeg、png、gif格式");
        }
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }

    /**
     * 创建目录（如果不存在）
     */
    private static void createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(UPLOAD_DIR + filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }
} 