package com.common.funciton;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩文件操作
 */
public class Zip4jUtil {

	private static byte[] buff = new byte[1024];
	
	public static void main(String[] args) throws FileNotFoundException {
		String path = "E:\\personal files manage\\IMS\\";
		File file = new File(path);
		String targetFileName = file.getName() + ".zip";
		File targetFile = new File("E:\\personal files manage\\IMS\\" + targetFileName);
		/*FileOutputStream fos = new FileOutputStream(targetFile);
		zip(file, fos, "", targetFile);*/




	}

	/**
	 * 内容压缩
	 * 使用gzip进行压缩
	 */
	public static String gzipContent(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Base64.encodeBase64String(out.toByteArray());
	}

	/**
	 *
	 * <p>
	 *  内容解压缩
	 * Description:使用gzip进行解压缩
	 * </p>
	 * 
	 * @param compressedStr
	 * @return
	 */
	public static String gunzipContent(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = Base64.decodeBase64(compressedStr);
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}

	/**
	 * zip4j压缩文件 传入多个文件压缩成一个压缩包
	 * @param files 文件列表
	 * @param os 输出流  FileOutputStream或者response的输出流
	 * @param password 压缩密码
	 */
	public static void zip(ArrayList<File> files, OutputStream os, String password){
        ZipParameters zipParameters = getZipParameters(password);
		ZipOutputStream zos = new ZipOutputStream(os);
		try {
			for (File file : files) {
				outPut(file, zos, zipParameters);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

	/**
	 * 根据路径压缩该路径下的所有文件
	 * @param filePath 文件或文件夹路径
	 * @param os 输出流
	 * @param password 密码
	 */
    public static void zip(String filePath, OutputStream os, String password){
		File sourceFile = new File(filePath);
		String sourceFileName = sourceFile.getName();
		File targetFile = null;
		if (sourceFile.isDirectory()) targetFile = new File(sourceFileName + ".zip");
		else targetFile = new File(sourceFileName.substring(0, sourceFileName.lastIndexOf(".")) + ".zip");
		zip(sourceFile, os, password, targetFile);
	}

	/**
	 * 根据给定的路径文件压缩，压缩文件或文件夹
	 * @param sourceFile 源文件/文件夹
	 * @param os 输出流  FileOutputStream或者response的输出流
	 * @param password 压缩密码
	 * @param targetFile targetFile 目标压缩包 避免使用文件输出流时将先创建的压缩包重复压缩了
	 */
	public static void zip(File sourceFile, OutputStream os, String password, File targetFile){
		ZipParameters zipParameters = getZipParameters(password);
		ZipOutputStream zos = new ZipOutputStream(os);
		try {
			compress(sourceFile, zos, zipParameters, targetFile, sourceFile, "");
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * zip 压缩操作 按照原目录结构压缩 支持多级目录
	 * @param tempFile 当前操作文件或文件夹
	 * @param zos  输出流
	 * @param zipParameters 压缩参数
	 * @param targetFile 目标压缩包 避免使用文件输出流时将先创建的压缩包重复压缩了
	 * @param sourceFile 根文件或文件夹
	 * @throws ZipException
	 * @throws IOException
	 */
    public static void compress(File tempFile, ZipOutputStream zos, ZipParameters zipParameters, File targetFile, File sourceFile, String folder) throws ZipException, IOException {
    	if (tempFile.equals(sourceFile)){ //操作文件为根路径
			if (tempFile.isDirectory()){
				File[] files = tempFile.listFiles();
				if (files.length != 0){
					for (File file : files) {
						compress(file, zos, zipParameters, targetFile, sourceFile, folder);
					}
				}
			}else {
				if (!tempFile.equals(targetFile)) outPut(tempFile, zos, zipParameters);
			}
		}else {
			File parentFile = tempFile.getParentFile();
			if (parentFile.equals(sourceFile)){ //根路径下的一级文件或文件夹
				zipParameters.setRootFolderInZip("");//设置文件输出目录
			}else {
				folder = folder + File.separator + parentFile.getName();
				zipParameters.setRootFolderInZip(folder);
			}
			if (tempFile.isDirectory()){
				File[] files = tempFile.listFiles();
				zos.putNextEntry(tempFile, zipParameters);
				zos.closeEntry();
				if (files.length != 0){
					for (File file : files) {
						compress(file, zos, zipParameters, targetFile, sourceFile, folder);
					}
				}
			}else {
				if (!tempFile.equals(targetFile)) outPut(tempFile, zos, zipParameters);
			}
		}
	}


	/**
	 * 文件输出
	 * @param tempFile 源文件
	 * @param zos 压缩输出流
	 * @param zipParameters 压缩参数
	 */
	public static void outPut(File tempFile, ZipOutputStream zos,  ZipParameters zipParameters){
		FileInputStream fis = null;
		BufferedInputStream bis = null;
    	try {
			zos.putNextEntry(tempFile, zipParameters);
			fis = new FileInputStream(tempFile);
			bis = new BufferedInputStream(fis);
			int len = 0;
			while ((len = bis.read(buff)) != -1){
				zos.write(buff, 0, len);
			}
			zos.closeEntry();
		}catch (Exception e){
    		e.printStackTrace();
		}finally {
			try {
				fis.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * 获取压缩参数属性
	 * @param password
	 * @return
	 */
	public static ZipParameters getZipParameters(String password){
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); //压缩方式  默认
		zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); //压缩级别  1-9
		if (StringUtils.isNotBlank(password)){
			zipParameters.setEncryptFiles(true);//是否加密处理
			zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); //加密方式
			zipParameters.setPassword(password);//设置密码
		}
		return zipParameters;
	}
}