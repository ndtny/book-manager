package com.david.bookmanager.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * PDF导出工具类
 */
public class PdfExportUtil {

    /**
     * 导出数据到PDF
     */
    public static void exportToPdf(List<Object> data, String fileName, HttpServletResponse response) {
        try {
            // 创建文档
            Document document = new Document(PageSize.A4);
            
            // 设置响应头
            response.setContentType("application/pdf");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName + ".pdf", StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
            
            // 创建PDF写入器
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            
            // 设置中文字体
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font headerFont = new Font(baseFont, 12, Font.BOLD);
            Font dataFont = new Font(baseFont, 10, Font.NORMAL);
            
            // 添加标题
            Paragraph title = new Paragraph("统计数据报表", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // 创建表格
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            
            // 设置列宽
            float[] columnWidths = {1f, 2f, 1.5f, 1.5f, 2f};
            table.setWidths(columnWidths);
            
            // 添加表头
            String[] headers = {"序号", "名称", "数量", "占比", "备注"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }
            
            // 添加数据行
            if (data != null && !data.isEmpty()) {
                for (int i = 0; i < data.size(); i++) {
                    Object item = data.get(i);
                    
                    if (item instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) item;
                        
                        // 序号
                        PdfPCell cell0 = new PdfPCell(new Phrase(String.valueOf(i + 1), dataFont));
                        cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell0.setPadding(5);
                        table.addCell(cell0);
                        
                        // 名称
                        PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(map.getOrDefault("name", "")), dataFont));
                        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell1.setPadding(5);
                        table.addCell(cell1);
                        
                        // 数量
                        Object count = map.get("count");
                        String countStr = count instanceof Number ? String.valueOf(count) : String.valueOf(count);
                        PdfPCell cell2 = new PdfPCell(new Phrase(countStr, dataFont));
                        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell2.setPadding(5);
                        table.addCell(cell2);
                        
                        // 占比
                        Object percentage = map.get("percentage");
                        String percentageStr = percentage instanceof Number ? String.valueOf(percentage) + "%" : String.valueOf(percentage);
                        PdfPCell cell3 = new PdfPCell(new Phrase(percentageStr, dataFont));
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell3.setPadding(5);
                        table.addCell(cell3);
                        
                        // 备注
                        PdfPCell cell4 = new PdfPCell(new Phrase(String.valueOf(map.getOrDefault("remark", "")), dataFont));
                        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell4.setPadding(5);
                        table.addCell(cell4);
                    }
                }
            }
            
            // 添加表格到文档
            document.add(table);
            
            // 添加页脚信息
            document.add(new Paragraph(" ")); // 空行
            Paragraph footer = new Paragraph("生成时间：" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), dataFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);
            
            document.close();
            
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("导出PDF失败: " + e.getMessage());
        }
    }
} 