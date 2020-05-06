package com.common.funciton;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtils {
	
	public static void main(String[] args) {
		String message  = "{\"ChannelID\":\"20000\",\"ChannelRefNo\":\"201610111637224\",\"ResponseInfo\":{\"ResponseCode\":\"C00\",\"ResponseMsg\":\"激活成功\",\"ResponseRefNo\":\"MTQ3NjI2MzY4Njc0NTQ5ODY2ZjNiLWU4NmEtNGNmMC04MDQ1LWVkYzgwYzUxODcwNA==\",\"ResponseTimeStamp\":\"1476263686902\",\"ResponseTimeZone\":\"CCT\"},\"MainData\":{\"MsgInfo\":{\"CommType\":\"RES\",\"TxnType\":\"ActivateCertificate\",\"TxnVersion\":\"0.0.1\",\"Charset\":\"UTF-8\"},\"ChannelInfo\":{\"ChannelLang\":\"zh_CN\",\"ChannelHostIP\":\"10.3.0.77\",\"ChannelRefNo\":\"201610111637224\",\"ChannelTimeStamp\":\"1476263686902\",\"ChannelTimeZone\":\"CCT\",\"SAFFlag\":\"\",\"SAFTimeStamp\":\"\"},\"RetailerInfo\":{\"StoreID\":\"1000000001\",\"StoreName\":\"神州黑鹰\",\"StoreTimeZone\":\"CCT\",\"TerminalID\":\"100001\",\"TerminalRefNo\":\"144861154f6947238bd1dd94fc47a180\",\"TerminalTimeStamp\":\"1476175042\",\"TerminalLang\":\"zh_CN\"},\"ServiceData\":\"4urz+PnZy5/ZjqtRxtexEB3XcGQKlbK9rPGc/ZEVHpJ0bwaExLjN1A7dVWLSMseoTU1r4xVKfCZoMpI6RT2lnnz5N+URtl1E+ZIRfO8LHudCCrPZUElDvZIERsX/fB5Cd0T8B693t62+OTAFpNsfdQ==\"},\"Signature\":\"eb210333d5f2715edc1bef45dceceb3e\"}";
		String gzipRet = gzip(message);
		System.out.println(gzipRet);
		String gunzip = gunzip(gzipRet);
		System.out.println(gunzip);
	}

	/**
	 * 
	 * 使用gzip进行压缩
	 */
	public static String gzip(String primStr) {
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
	 * Description:使用gzip进行解压缩
	 * </p>
	 * 
	 * @param compressedStr
	 * @return
	 */
	public static String gunzip(String compressedStr) {
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

}