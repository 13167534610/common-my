package com.common.funciton;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Hashtable;

/**
 * description:二维码生成工具类
 * 依赖jar
 <dependency>
 <groupId>com.google.zxing</groupId>
 <artifactId>core</artifactId>
 <version>3.3.0</version>
 </dependency>
 <dependency>
 <groupId>com.google.zxing</groupId>
 <artifactId>javase</artifactId>
 <version>3.3.0</version>
 </dependency>
 *
 * @author wangqiang
 */
public class QrCodeUtil {
    /**
     * 生成包含字符串信息的二维码图片
     * @param content 二维码携带信息
     * @param qrCodeSize 二维码图片大小
     * @throws WriterException
     * @throws IOException
     */
    public static BufferedImage createQrCode(File logoFile, String content, int qrCodeSize) throws WriterException, IOException {
        //设置二维码纠错级别ＭＡＰ
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  //矫错级别 H为最高级
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //创建比特矩阵(位矩阵)的QR码编码的字符串
        BitMatrix bm = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
        // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
        int width = bm.getWidth();
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.lightGray);
        graphics.fillRect(0, 0, width, width);
        // 使用比特矩阵画并保存图像
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (bm.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        if (logoFile != null)insertLogo(image, logoFile, qrCodeSize);
        return image;
    }

    /**
     * 插入logo
     * @param image
     * @param logoFile
     * @param qrCodeSize
     * @throws IOException
     */
    public static void insertLogo(BufferedImage image, File logoFile, int qrCodeSize) throws IOException {
        if (!logoFile.exists()){
            System.out.println("logo is not exist");
            return;
        }
        Image logoImg = ImageIO.read(logoFile);//读取logo图片
        int logoW = logoImg.getWidth(null);
        int logoH = logoImg.getHeight(null);
        //计算logo长宽的最大值，二维码长宽的0.2倍，取四舍五入
        int v = new BigDecimal(qrCodeSize).multiply(new BigDecimal(0.2)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        if (logoW > v)logoW = v;
        if (logoH > v)logoH = v;
        //logo图片按照新的长宽进行缩放
        Image logo = logoImg.getScaledInstance(logoW, logoH, Image.SCALE_SMOOTH);
        logoImg = logo;
        //织入logo
        Graphics2D graphics = image.createGraphics();
        int x = (qrCodeSize - logoW) / 2;
        int y = (qrCodeSize - logoH) / 2;
        graphics.drawImage(logoImg, x, y, logoW, logoH, null);
        RoundRectangle2D.Float shape = new RoundRectangle2D.Float(x, y, logoW, logoH, 1, 1);
        graphics.draw(shape);
        graphics.setStroke(new BasicStroke(3f));
        graphics.dispose();
    }

    /**
     * 读二维码并输出携带的信息
     */
    public static Result readQrCode(InputStream inputStream) throws IOException {
        //从输入流中获取字符串信息
        BufferedImage image = ImageIO.read(inputStream);
        //将图像转换为二进制位图源
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(bitmap);
        } catch (ReaderException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    /**
     * 测试代码
     *
     * @throws WriterException
     */
    public static void main(String[] args) throws IOException, WriterException {
        /*FileOutputStream outputStream = new FileOutputStream(new File("d:\\qrcode.jpg"));
        BufferedImage qrCode = createQrCode(new File("E:\\personal files manage\\git repository\\icon\\test.jpg"), "otpauth://totp/account1?secret=MHN2UZ5ZGWSLATUY", 300);
        ImageIO.write(qrCode, "JPEG", outputStream);
        outputStream.close();*/
        Result result = readQrCode(new FileInputStream(new File("E:\\personal files manage\\git repository\\imgs\\qrcode.png")));
        System.out.println(result.getText());
        //下方为浏览器请求方式
        /*ServletOutputStream outputStream = response.getOutputStream();
        String content = "www.baidu.com";
        Integer qrCodeSize = 200;
        String imageFormat = "JPEG";
        createQrCode(outputStream, content, qrCodeSize, imageFormat);
        * */
    }
}
