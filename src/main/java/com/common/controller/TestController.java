package com.common.controller;

import com.common.funciton.*;
import com.google.zxing.WriterException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/26 14:13
 */
@Controller
public class TestController {

    @RequestMapping(value = "/zip")
    public void zip(String sourcePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        Zip4jUtil.zip(sourcePath, os, "123456");
    }

    @RequestMapping("/getImgValidCode")
    public void getImgValidCode(String random, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        //String s = VerifyCodeUtil.outputVerifyImage(260, 50, os, 6);
        BufferedImage image = VerifyCodeUtil.getImage(260, 50, random);
        ImageIO.write(image, "jpg", os);
        System.out.println("验证码：" + random);
    }

    @RequestMapping(value = "/exportExcel")
    public void exportExcel(String title, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("excel导出测试");
        List<POIUtil.People> peoples=new ArrayList<>();
        peoples.add(new POIUtil.People("LF","男"));
        peoples.add(new POIUtil.People("WYH","女"));
        peoples.add(new POIUtil.People("WQ", "男"));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name","姓名");
        map.put("sex","性别");
        HSSFWorkbook workbook = new HSSFWorkbook();


        Workbook excel = POIUtil.exportExcel(workbook, peoples, title, POIUtil.People.class, map);
        POIUtil.ResponseFields fields = POIUtil.getResponseFields(title);
        POIUtil.HeaderFields headerFields = fields.getHeaderFields();
        String contentType = fields.getContentType();
        response.setHeader(headerFields.getName(), headerFields.getValue());
        response.setContentType(contentType);
        ServletOutputStream os = response.getOutputStream();
        excel.write(os);
    }

    @RequestMapping(value = "/getQrCode")
    public void getQrCode(HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        String qrBarcode = TotpAuthUtil.getQRBarcode("account1", "MHN2UZ5ZGWSLATUY");
        BufferedImage qrCode = QrCodeUtil.createQrCode(null, qrBarcode, 150);
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(qrCode, "jpg", os);
    }

    @RequestMapping("/getTotpAuth")
    public void getTotpAuth(String random, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("获取动态密码: " + random);
        String pwd = "MHN2UZ5ZGWSLATUY";
        long code = TotpAuthUtil.getCode(pwd, System.currentTimeMillis());
        ServletOutputStream os = response.getOutputStream();
        VerifyCodeUtil.outputImage(200, 50, os, "" + code);
    }
}
