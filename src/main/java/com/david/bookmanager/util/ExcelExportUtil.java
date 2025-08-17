package com.david.bookmanager.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Excel导出工具类
 */
public class ExcelExportUtil {

    /**
     * 导出数据到Excel
     */
    public static void exportToExcel(List<Object> data, String fileName, HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("统计数据");
            
            // 设置列宽
            sheet.setColumnWidth(0, 15 * 256);
            sheet.setColumnWidth(1, 20 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 20 * 256);
            
            // 创建标题样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // 创建数据样式
            CellStyle dataStyle = createDataStyle(workbook);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"序号", "名称", "数量", "占比", "备注"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            if (data != null && !data.isEmpty()) {
                for (int i = 0; i < data.size(); i++) {
                    Row dataRow = sheet.createRow(i + 1);
                    Object item = data.get(i);
                    
                    if (item instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) item;
                        
                        // 序号
                        Cell cell0 = dataRow.createCell(0);
                        cell0.setCellValue(i + 1);
                        cell0.setCellStyle(dataStyle);
                        
                        // 名称
                        Cell cell1 = dataRow.createCell(1);
                        cell1.setCellValue(String.valueOf(map.getOrDefault("name", "")));
                        cell1.setCellStyle(dataStyle);
                        
                        // 数量
                        Cell cell2 = dataRow.createCell(2);
                        Object count = map.get("count");
                        if (count instanceof Number) {
                            cell2.setCellValue(((Number) count).doubleValue());
                        } else {
                            cell2.setCellValue(String.valueOf(count));
                        }
                        cell2.setCellStyle(dataStyle);
                        
                        // 占比
                        Cell cell3 = dataRow.createCell(3);
                        Object percentage = map.get("percentage");
                        if (percentage instanceof Number) {
                            cell3.setCellValue(((Number) percentage).doubleValue());
                        } else {
                            cell3.setCellValue(String.valueOf(percentage));
                        }
                        cell3.setCellStyle(dataStyle);
                        
                        // 备注
                        Cell cell4 = dataRow.createCell(4);
                        cell4.setCellValue(String.valueOf(map.getOrDefault("remark", "")));
                        cell4.setCellStyle(dataStyle);
                    }
                }
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
            
            // 写入响应流
            workbook.write(response.getOutputStream());
            
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建标题样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // 设置字体
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        
        // 设置对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // 设置背景色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        return style;
    }
    
    /**
     * 创建数据样式
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // 设置字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        
        // 设置对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        return style;
    }
} 