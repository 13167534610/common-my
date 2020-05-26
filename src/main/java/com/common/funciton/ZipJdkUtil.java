package com.common.funciton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/25 15:47
 */
public class ZipJdkUtil {
    private String zipFileName;	//目的地Zip文件
    private String sourceFileName;	//源文件

    public static void main(String[] args) throws Exception {
        String path = "E:\\personal files manage\\IMS";
        ZipJdkUtil zipJdkUtil = new ZipJdkUtil("E:\\personal files manage\\IMS\\test1.zip", path);
        zipJdkUtil.zip();
    }

    public ZipJdkUtil(String zipFileName, String sourceFileName) {
        this.zipFileName = zipFileName;
        this.sourceFileName = sourceFileName;
    }

    public void zip() throws Exception {
        System.out.println("开始压缩...");
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        File sourceFile = new File(sourceFileName);
        //调用函数
        compress(out, sourceFile, sourceFile.getName());
        out.close();
        System.out.println("压缩完成！");
    }

    public void compress(ZipOutputStream out, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if(sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            if(flist.length==0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                out.putNextEntry(new ZipEntry(base + File.separator));
            } else {//如果文件夹不为空，则递归调用compress,文件夹中的每一个文件（或文件夹）进行压缩
                for(int i=0; i<flist.length; i++) {
                    compress(out, flist[i], base+File.separator+flist[i].getName());
                }
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len;
            byte[] buf = new byte[1024];
            System.out.println(base);
            while((len=bis.read(buf, 0, 1024)) != -1) {
                out.write(buf, 0, len);
            }
            bis.close();
            fos.close();
        }
    }
}
