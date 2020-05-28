package com.common.funciton;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * 本工具类是一个通用的Excel导出工具类
 * 模板：一级标题（title0）  二级标题（title1）  数据表（dataRow）
 * 使用方法：二级标题汉化调用setMap<String,String>()方法
 *          调用exportExcel()方法设计导出Excel
 * @author wangqiang
 */
public class POIUtil {

    private static final short FONTSIZE_0=18; //大标题字号
    private static final short FONTSIZE_1=14; //一级标题字号
    private static final short FONTSIZE_2=10; //数据行字号
    private static final int COLUMN_START=0; //合并区域开始列
    private static final int COLUMN_TITLE0=0; //标题开始列
    private static final int COLUMN_WIDTH=12; //列宽
    private static final int ROW_START=0; //合并区域开始行
    private static final int ROW_END=1; //合并区域结束行
    private static final int ROWNUM_TITLE0=0; //标题开始行
    private static final int ROWNUM_TITLE1=2; //二级标题开始行
    private static final int FIELD_SUB_START=0; //属性截取开始
    private static final int FIELD_SUB_END=1; //属性截取结束
    private static final int DATAROW_ADD=3; //数据开始行
    public final static String RESPONSE_CONTENT_TYPE = "application/msexcel";
    public final static String SUFFix = ".xls";


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) throws IOException {


        List<People> peoples=new ArrayList<People>();
        peoples.add(new People("LF","男"));
        peoples.add(new People("WYH","女"));
        peoples.add(new People("WQ", "男"));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name","姓名");
        map.put("sex","性别");
        HSSFWorkbook workbook = new HSSFWorkbook();
        Workbook workbook1 = POIUtil.exportExcel(workbook, peoples, "非浏览器测试", People.class, map);
        //workbook1.write(getOutputStreamByAbsolutely("C:\\Users\\admin\\Documents\\WeChat Files\\A17839227646\\FileStorage\\File\\2019-06\\excetest.xls"));
        // TODO: 2019/6/18 下面为使用HttpServletResponse通过浏览器下载文件的设置方式

        ResponseFields fields = getResponseFields("nest");
        HeaderFields headerFields = fields.getHeaderFields();
        String contentType = fields.getContentType();
        String name = headerFields.getName();
        String value = headerFields.getValue();

        HttpServletResponse response = null;
        ServletOutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader(name, value);
        response.setContentType(contentType);
        workbook1.write(output);
    }



    /**
     *
     * @param workbook 工作簿，仅仅支持HSSFWorkBook
     * @param objs 倒数对象集合
     * @param title 文件名，一级标题
     * @param clazz 对象类型
     * @param map 字段汉化器
     * @return workbook  使用workbook.write(OutPutStream)写出
     */
    public static Workbook exportExcel(Workbook workbook, List objs, String title, Class clazz, Map<String, String> map){
        try {
            if (StringUtils.isEmpty(title))
                title = "newFile";
            Sheet sheet = workbook.createSheet(title);//建立新的sheet对象（excel的表单）
            Field[] fields = clazz.getDeclaredFields();//获取实体属性名
            //合并区域
            CellRangeAddress address = new CellRangeAddress(ROW_START, ROW_END, COLUMN_START, fields.length-1);
            sheet.addMergedRegion(address);//设置合并区域生效
            sheet.setDefaultColumnWidth(COLUMN_WIDTH);//设置默认列宽
            //创建一级标题
            Row title0 = sheet.createRow(ROWNUM_TITLE0);//开始行
            Cell cell = title0.createCell(COLUMN_TITLE0);//开始列

            CellStyle cellStyle = workbook.createCellStyle();
            cell.setCellStyle(getCellStyle(workbook, FONTSIZE_0, cellStyle));//设置样式
            cell.setCellValue(title);//设置一级标题名
            //创建二级标题
            Row title1 = sheet.createRow(ROWNUM_TITLE1);//从哪一行开始
            if (null == map || map.isEmpty()) {//不需要标题汉化
                for (int i = 0; i < fields.length; i++) {
                    Cell colCell = title1.createCell(i);//循环创建列
                    colCell.setCellStyle(getCellStyle(workbook,FONTSIZE_1, cellStyle));//设置样式
                    colCell.setCellValue(fields[i].getName());//设置值
                }
            }else {//需要标题汉化
                for (int i = 0; i < fields.length; i++) {
                    Cell colCell = title1.createCell(i);
                    colCell.setCellStyle(getCellStyle(workbook,FONTSIZE_1, cellStyle));
                    colCell.setCellValue(map.get(fields[i].getName()));
                }
            }
            if (null == objs || objs.isEmpty()){//没有需要导出的数据
              throw new RuntimeException("No datas what you want to export");
            }
            //创建数据行
            for (int i = 0; i < objs.size(); i++) {
                Row dataRow = sheet.createRow(i + DATAROW_ADD);
                for (int j = 0; j < fields.length; j++) {
                    Cell dataCell = dataRow.createCell(j);
                    //获取属性get方法名
                    String getMethodName = "get" +
                            fields[j].getName().substring(FIELD_SUB_START, FIELD_SUB_END).toUpperCase()
                            + fields[j].getName().substring(FIELD_SUB_END);
                    //返回方法对象 //参数一:方法的名字   //参数二:方法参数的类型（无参）
                    Method getMethod = clazz.getDeclaredMethod(getMethodName, new Class[]{});
                    //执行方法  参数一:执行那个对象中的方法    参数二:该方法的参数
                    Object value = getMethod.invoke(objs.get(i), new Object[]{});
                    //设置单元格显示格式控件
                    judgeAndSetValue(workbook, value, dataCell, cellStyle);
                }
            }
            return workbook;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置样式
     * @param workbook：工作簿
     * @param fontSize：字号
     * font.setFontName("");：设置字体类型，如：宋体
     */
    private static CellStyle getCellStyle(Workbook workbook,short fontSize, CellStyle cellStyle){
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中对齐
        Font font = workbook.createFont();
        if (fontSize == FONTSIZE_0 || fontSize == FONTSIZE_1)
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//设置粗体显示
        font.setFontHeightInPoints(fontSize);//设置字体大小
        cellStyle.setFont(font);//设置字体样式生效
        cellStyle.setWrapText(true);//设置自动换行
        return cellStyle;
    }

    /**
     * description：根据值得类型设置单元格格式并给单元格设值
     * @param workbook：工作簿
     * @param value：通过get方法获取的值
     * @param dataCell：数据单元格
     */
    private static void judgeAndSetValue(Workbook workbook, Object value, Cell dataCell, CellStyle cellStyle){
        DataFormat df = workbook.createDataFormat();//数据格式对象
        if (value instanceof Integer){//整数类型
            cellStyle = getCellStyle(workbook, FONTSIZE_2, cellStyle);
            dataCell.setCellStyle(cellStyle);
            dataCell.setCellValue((Integer)value);
        }
        if (value instanceof Long){//长整数类型
            cellStyle = getCellStyle(workbook, FONTSIZE_2, cellStyle);
            dataCell.setCellStyle(cellStyle);
            dataCell.setCellValue((Long)value);
        }
        if (value instanceof Double){//双精度小数类型
            cellStyle = getCellStyle(workbook, FONTSIZE_2, cellStyle);
            cellStyle.setDataFormat(df.getFormat("#,#0.00"));
            dataCell.setCellStyle(cellStyle);
            dataCell.setCellValue((Double)value);
        }
        if (value instanceof Float){//单精度小数类型
            cellStyle = getCellStyle(workbook, FONTSIZE_2, cellStyle);
            cellStyle.setDataFormat(df.getFormat("#,#0.0"));
            dataCell.setCellStyle(cellStyle);
            dataCell.setCellValue((Float)value);
        }
        if (value instanceof Date){//日期类型
            cellStyle = getCellStyle(workbook, FONTSIZE_2, cellStyle);
            cellStyle.setDataFormat(df.getFormat("yyyy/MM/dd"));
            dataCell.setCellStyle(cellStyle);
            dataCell.setCellValue((Date)value);
        }
        if(value instanceof String){//字符串类型
            cellStyle = getCellStyle(workbook, FONTSIZE_2, cellStyle);
            dataCell.setCellStyle(cellStyle);
            dataCell.setCellValue((String)value);
        }
    }


    /**
     * 根据指定的绝对路径获取输出流
     * @param absolutely
     * @return
     */
    public static OutputStream getOutputStreamByAbsolutely(String absolutely){
        FileOutputStream fos = null;
        try {
            if (StringUtils.isBlank(absolutely)){
                File file = new File(absolutely);
                fos = new FileOutputStream(file);
            }else {
                throw new RuntimeException("absolutely is null");
            }
        }catch (FileNotFoundException e){
            System.out.println("文件未找到，请确认文件路径是否正确: " + absolutely);
            throw new RuntimeException(e);
        }
        return fos;
    }

    /**
     * 获取响应属性
     * @param title
     * @return
     */
    public static ResponseFields getResponseFields(String title) {
        ResponseFields responseFields = null;
        try {
            HeaderFields headerFields = new HeaderFields(title, FileOperater.EN_UTF8);
            responseFields = new ResponseFields(headerFields);
        }catch (UnsupportedEncodingException e){
            System.out.println("不支持的编码类型:" + FileOperater.EN_UTF8);
        }
        return responseFields;
    }

    /**
     * response属性设置说明
     */
    public static class ResponseFields{
        private String contentType;

        private HeaderFields headerFields;

        public ResponseFields() {
        }

        public ResponseFields(HeaderFields headerFields) {
            this.contentType = RESPONSE_CONTENT_TYPE;
            this.headerFields = headerFields;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public HeaderFields getHeaderFields() {
            return headerFields;
        }

        public void setHeaderFields(HeaderFields headerFields) {
            this.headerFields = headerFields;
        }

        @Override
        public String toString() {
            return "ResponseFields{" +
                    "contentType='" + contentType + '\'' +
                    ", headerFields=" + headerFields +
                    '}';
        }
    }

    /**
     * 响应头信息
     */
    public static class HeaderFields{
        private String name;

        private String value;

        public HeaderFields() {
        }

        public HeaderFields(String title, String encoding) throws UnsupportedEncodingException {
            this.name = "Content-disposition";
            this.value = "attachment; filename=" + URLEncoder.encode(title + SUFFix, encoding);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "HeaderFields{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }



    /**
     * 测试内部类
     */
    public static class People{
        private String name;

        private String sex;

        public People(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}