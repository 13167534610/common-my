package com.common.funciton.pdf;

import com.common.funciton.SignUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2018/6/20 17:36
 */
public class PDFReport {
    private static Integer WORD_OVERSTRICKING = 1;
    private static int WORD_NUMBER = 8; //8号字
    public static void reportPDF() throws Exception {
        Document document = new Document(PageSize.A4, 36.0F, 36.0F, 36.0F, 36.0F);
        PdfWriter.getInstance(document, new FileOutputStream("template/测试1.pdf"));
        document.open();
        PdfPTable table = new PdfPTable(4);
        setTableStyle(table);

        Paragraph title = new Paragraph("进货单详情", getFont(WORD_NUMBER, WORD_OVERSTRICKING));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        //获取行填充器
        ArrayList<PdfPRow> rows = table.getRows();
        //第一行
        PdfPCell[] row1Cell = new PdfPCell[4];
        PdfPRow row1 = new PdfPRow(row1Cell);
        row1Cell[0] = new PdfPCell(new Paragraph("货单信息", getFont(WORD_NUMBER, WORD_OVERSTRICKING)));//单元格内容
        row1Cell[0].setColspan(4);
        setCellStytle(row1Cell[0], 2);
        rows.add(row1);

        //第二行
        PdfPCell[] row2Cell = new PdfPCell[4];
        PdfPRow row2 = new PdfPRow(row2Cell);
        row2Cell[0] = new PdfPCell(new Paragraph("进货单号", getFont(WORD_NUMBER, null)));//单元格内容
        setCellStytle(row2Cell[0], 2);
        row2Cell[1] = new PdfPCell(new Paragraph("1474536646461", getFont(WORD_NUMBER, null)));//单元格内容
        row2Cell[1].setColspan(3);
        rows.add(row2);

        setCell1(rows, "生成时间", "11", "确认时间", "22", getFont(WORD_NUMBER, null));
        setCell1(rows, "生成用户ID", "11", "确认用户ID", "22", getFont(WORD_NUMBER, null));
        setCell1(rows, "进货机构号", "11", "进货机构名称", "22", getFont(WORD_NUMBER, null));
        setCell1(rows, "出货机构号", "11", "出货机构名称", "22", getFont(WORD_NUMBER, null));

        //第七行
        PdfPCell[] row7Cell = new PdfPCell[4];
        PdfPRow row = new PdfPRow(row7Cell);
        row7Cell[0] = new PdfPCell(new Paragraph("货物明细", getFont(WORD_NUMBER, WORD_OVERSTRICKING)));//单元格内容
        row7Cell[0].setColspan(4);
        setCellStytle(row7Cell[0], 2);
        rows.add(row);

        //数据标题行
        setCell2(rows, "品名", "数量", "备注", getFont(WORD_NUMBER, null));

        //数据行

        document.add(table);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PdfPTable table1 = new PdfPTable(2);
        setTableStyle(table1);
        PdfPCell pdfPCell1 = new PdfPCell(new Phrase("文件名称：" + "1111", getFont(WORD_NUMBER, null)));
        PdfPCell pdfPCell2 = new PdfPCell(new Phrase("文件生成时间：" + format.format(new Date()), getFont(WORD_NUMBER, null)));
        setCellStytle(pdfPCell1, 4);
        setCellStytle(pdfPCell2, 3);
        table1.addCell(pdfPCell1);
        table1.addCell(pdfPCell2);
        document.add(table1);
        String s = SignUtil.MD5encrypt(String.valueOf(document));
        Paragraph paragraph = new Paragraph("SIGN：" + s, getFont(WORD_NUMBER, null));
        document.add(paragraph);
        document.close();
    }

    /**
     * 设置字体
     * @param wordNumber 字号
     * @param overStricking 是否加粗
     * @return
     */
    private static Font getFont(int wordNumber,Integer overStricking) throws Exception {
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = null;
        if (overStricking == null)font = new Font(bfChinese, wordNumber, Font.NORMAL);
        else font = new Font(bfChinese, wordNumber, Font.BOLD);
        return font;
    }

    private static void setTableStyle(PdfPTable table){
        table.setWidthPercentage(100); // 宽度100%填充
        table.setSpacingBefore(10f); // 前间距
        table.setSpacingAfter(10f); // 后间距
    }

    private static void setCell1(ArrayList<PdfPRow> rows, String key1, String value1, String key2, String value2,Font font){
        PdfPCell[] rowCell = new PdfPCell[4];
        PdfPRow row = new PdfPRow(rowCell);
        rowCell[0] = new PdfPCell(new Paragraph(key1, font));//单元格内容
        rowCell[2] = new PdfPCell(new Paragraph(key2, font));//单元格内容
        rowCell[1] = new PdfPCell(new Paragraph(value1, font));//单元格内容
        rowCell[3] = new PdfPCell(new Paragraph(value2, font));//单元格内容
        setCellStytle(rowCell[0], 1);
        setCellStytle(rowCell[2], 1);
        setCellStytle(rowCell[1], 1);
        setCellStytle(rowCell[3], 1);
        rows.add(row);
    }

    private static void setCell2(ArrayList<PdfPRow> rows, String v1, String v2, String v3, Font font){
        PdfPCell[] rowCell = new PdfPCell[4];
        PdfPRow row = new PdfPRow(rowCell);
        rowCell[0] = new PdfPCell(new Paragraph(v1, font));//单元格内容
        rowCell[0].setColspan(2);
        setCellStytle(rowCell[0], 1);
        rowCell[2] = new PdfPCell(new Paragraph(v2, font));//单元格内容
        setCellStytle(rowCell[2], 1);
        rowCell[3] = new PdfPCell(new Paragraph(v2, font));//单元格内容
        setCellStytle(rowCell[3], 1);
        rows.add(row);
    }

    private static void setCellStytle(PdfPCell cell, Integer type){
        if (type.equals(3) || type.equals(4))
            cell.setBorderColor(BaseColor.WHITE);
        else
            cell.setBorderColor(BaseColor.BLUE);//边框验证
        cell.setPaddingLeft(20);//左填充20
        if (type.equals(1))cell.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        else if (type.equals(2) || type.equals(4))cell.setHorizontalAlignment(Element.ALIGN_LEFT);//水平居左
        else if (type.equals(3))cell.setHorizontalAlignment(Element.ALIGN_RIGHT);//水平居右
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
    }

    public static void main(String[] args) throws Exception {
        reportPDF();
    }
}
