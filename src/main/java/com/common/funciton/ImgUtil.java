package com.common.funciton;


import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 计算两张图片的相似度
 * Created by wangqiang on 2019/10/24.
 */
public class ImgUtil {

    public static void main(String[] args) throws IOException {
        File file1 = new File("D:\\mycode\\test\\test1.jpg");
        File file2 = new File("D:\\mycode\\test\\test2.jpg");
        File file3 = new File("D:\\mycode\\test\\test3.jpeg");
        System.out.println(compareImage(file1, file2));
        //System.out.println(compareImage(file2, file3));
        //System.out.println(compareImage(file1, file3));
    }

    /**
     * 将文件转换成二进制码
     * @param imageFile
     * @return
     * @throws IOException
     */
    public static String[][] getPX(File imageFile) throws IOException {
        FileImageInputStream fis = new FileImageInputStream(imageFile);
        BufferedImage image = ImageIO.read(fis);
        int[] rgb = new int[3];
        int width = image.getWidth();
        int height = image.getHeight();
        int minx = image.getMinX();
        int miny = image.getMinY();
        String[][] list = new String[width][height];
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = image.getRGB(i, j);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];
            }
        }
        return list;
    }


    /**
     * 计算相似和不似的二进制码
     * @param list1
     * @param list2
     * @param result
     * @return
     */
    private static Result culate(String[][] list1, String[][] list2, Result result){
        int i = 0, j = 0;
        Integer xiangsi = result.getXiansi();
        Integer busi = result.getBusi();
        for (String[] strings : list1) {
            if ((i + 1) == list1.length) {
                continue;
            }
            for (int m=0; m<strings.length; m++) {
                try {
                    String[] value1 = list1[i][j].toString().split(",");
                    String[] value2 = list2[i][j].toString().split(",");
                    int k = 0;
                    for (int n=0; n<value2.length; n++)
                        if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {
                            xiangsi++;
                        } else {
                            busi++;
                        }
                } catch (RuntimeException e) {
                    continue;
                }
                j++;
            }
            i++;
        }
        result.setXiansi(xiangsi);
        result.setBusi(busi);
        return result;
    }

    /**
     * 比较两个文件的相似度
     * @param imgPath1
     * @param imgPath2
     * @return
     * @throws IOException
     */
    public static Result compareImage(File imgPath1, File imgPath2) throws IOException {
        // 分析图片相似度 begin
        String[][] list1 = getPX(imgPath1);
        String[][] list2 = getPX(imgPath2);
        Result result = new Result();
        result = culate(list1, list2, result);
        list1 = getPX(imgPath2);
        list2 = getPX(imgPath1);
        result = culate(list1, list2, result);
        try {
            if(result.getBusi() == 0){
                result.setPercentage(NumberUtil.format("1", NumberUtil.DF_PERCENTAGE));
            }else {
                String format = NumberUtil.format(NumberUtil.initBigDecimal(result.getXiansi()).divide(NumberUtil.initBigDecimal(result.getSum()), 5, NumberUtil.ROUND_HALF_DOWN).toString(), NumberUtil.DF_PERCENTAGE);
                result.setPercentage(format);
            }
        } catch (Exception e) {
            result.setPercentage(NumberUtil.format("0", NumberUtil.DF_PERCENTAGE));
            e.printStackTrace();
        }
        return result;
    }




    static class Result{
        private Integer xiansi;

        private Integer busi;

        private String percentage;

        private Integer sum;

        public Result() {
            this.xiansi = 0;
            this.busi = 0;
        }

        public Result(Integer xiansi, Integer busi) {
            this.xiansi = xiansi;
            this.busi = busi;
        }

        @Override
        public String toString() {
            return "result{" +
                    "xiansi=" + xiansi +
                    ", busi=" + busi +
                    ", percentage=" + percentage +
                    '}';
        }

        public Integer getXiansi() {
            return xiansi;
        }

        public void setXiansi(Integer xiansi) {
            this.xiansi = xiansi;
            this.sum = this.xiansi + this.busi;
        }

        public Integer getBusi() {
            return busi;
        }

        public void setBusi(Integer busi) {
            this.busi = busi;
            this.sum = this.xiansi + this.busi;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        public Integer getSum() {
            return sum;
        }

        public void setSum(Integer sum) {
            this.sum = sum;
        }
    }
}
