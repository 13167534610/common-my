package com.common.funciton;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Writer;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 条形码工具
 * @Author: wangqiang
 * @Date:2020/5/11 10:09
 */
public class BarCodeUtil {

    /**
     * 设置 条形码参数
     */
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;
        {
            // 设置编码方式
            put(EncodeHintType.CHARACTER_SET, "utf-8");
        }
    };

    /**
     * 生成 图片缓冲
     * @author fxbin
     * @param barCode  VA 码
     * @param width 条码宽
     * @param height 条码高
     * @return 返回BufferedImage
     */
    public static BufferedImage getBarCodeImg(String barCode, int width, int height){
        try {
            Code128Writer writer = new Code128Writer();
            // 编码内容, 编码类型, 宽度, 高度, 设置参数
            BitMatrix bitMatrix = writer.encode(barCode, BarcodeFormat.CODE_128, width, height, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把带logo的二维码下面加上文字
     * @author fxbin
     * @param barCode  条码串
     * @param desc  条码说明
     * @param width 条码宽
     * @param height 条码高
     * @param wordHeight 条码文字高度
     * @return 返回BufferedImage
     */
    public static BufferedImage insertWords(String barCode, String desc, int width, int height, int wordHeight){
        BufferedImage barCodeImg = getBarCodeImg(barCode, width, height);
        // 新的图片，把带logo的二维码下面加上文字
        if (StringUtils.isNotEmpty(desc)) {
            BufferedImage outImage = new BufferedImage(width, wordHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImage.createGraphics();
            // 抗锯齿
            setGraphics2D(g2d);
            // 设置白色
            setColorWhite(g2d);
            // 画条形码到新的面板
            g2d.drawImage(barCodeImg, 0, 0, barCodeImg.getWidth(), barCodeImg.getHeight(), null);
            // 画文字到新的面板
            Color color=new Color(0, 0, 0);
            g2d.setColor(color);
            // 字体、字型、字号
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            //文字长度
            int strWidth = g2d.getFontMetrics().stringWidth(desc);
            //总长度减去文字长度的一半  （居中显示）
            int wordStartX=(width - strWidth) / 2;
            //height + (outImage.getHeight() - height) / 2 + 12
            int wordStartY=height + 20;
            // 画文字
            g2d.drawString(desc, wordStartX, wordStartY);
            g2d.dispose();
            outImage.flush();
            return outImage;
        }
        return null;
    }


    /**
     * 解析读取条形码
     * @param barCodeImg 条形码文件
     * @return
     */
    public static String readBarcode(File barCodeImg){
        BufferedImage image;
        Result result = null;
        try {
            image = ImageIO.read(barCodeImg);
            if (image != null) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                result = new MultiFormatReader().decode(bitmap, null);
            }
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置 Graphics2D 属性  （抗锯齿）
     * @param g2d  Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setGraphics2D(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(s);
    }

    /**
     * 设置背景为白色
     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setColorWhite(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        //填充整个屏幕
        g2d.fillRect(0,0,600,600);
        //设置笔刷
        g2d.setColor(Color.BLACK);
    }
    public static void main(String[] args) throws IOException {
//        BufferedImage image = insertWords(getBarCode("123456789"), "123456789");
//        A80/90R8A(8A侧通孔)
        BufferedImage image = insertWords("A80/90R8A8A", "A80/90R8A(8A侧通孔)", 300, 50, 75);
        File file = new File("E:\\personal files manage\\git repository\\imgs\\barcode.jpg");
        ImageIO.write(image, "jpg", file);

        String s = readBarcode(file);
        System.out.println(s);
    }
}
