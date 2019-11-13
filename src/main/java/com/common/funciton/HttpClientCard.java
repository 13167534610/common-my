package com.common.funciton;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class HttpClientCard {

	public static String postHttp(String OutParam, String requrl)
			throws UnsupportedEncodingException {

		URL url = null;
		HttpURLConnection urlconn = null;
		StringBuffer sb = new StringBuffer();
		String param = "";

		try {
			param = "td=" + URLEncoder.encode(OutParam, "utf-8");
			url = new URL(requrl);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setDoOutput(true);
			urlconn.setDoInput(true);
			urlconn.setUseCaches(false);
			urlconn.setRequestMethod("POST");
			urlconn.setConnectTimeout(300000);// （单位：毫秒）
			urlconn.setReadTimeout(300000);// （单位：毫秒）
			urlconn.setRequestProperty("Content-type","application/json");
			urlconn.setRequestProperty("Accept-Charset", "UTF-8");
			urlconn.setRequestProperty("Content-Encoding", "UTF-8");
			urlconn.connect();
			DataOutputStream out = new DataOutputStream(
					urlconn.getOutputStream());
			out.writeBytes(param);
			out.flush();
			out.close();

			int respCode = urlconn.getResponseCode();
			if (respCode == 200) {
				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(urlconn.getInputStream()));
				String text;
				while ((text = buffer.readLine()) != null) {
					sb.append(text);
				}

				buffer.close();
			} else {
                System.out.println(respCode);
				/*BufferedReader buffer = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
				String text;
				while ((text = buffer.readLine()) != null) {
					sb.append(text);
				}
				buffer.close();*/
			}
		} catch (UnsupportedEncodingException uee) {
			System.out.println("Encoding error" + uee);
		} catch (MalformedURLException me) {
			System.out.println("New url error" + me);
		} catch (ConnectException ce) {
			System.out.println("Connect error" + ce);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.out.println("Io error" + ie);
		} finally {
			if (urlconn != null)
				urlconn.disconnect();
			url = null;
		}

		return URLDecoder.decode(sb.toString(), "UTF-8");

	}

	/**
	 * HttpPost请求，设置超时时间
	 * 2017-10-23 HY
	 * @param OutParam
	 * @param requrl
	 * @param timeout
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String postHttpTimeout(String OutParam, String requrl, int timeout)
			throws UnsupportedEncodingException {

		URL url = null;
		HttpURLConnection urlconn = null;
		StringBuffer sb = new StringBuffer();
		String param = "";

		try {
			param = "td=" + URLEncoder.encode(OutParam, "utf-8");
			url = new URL(requrl);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setDoOutput(true);
			urlconn.setDoInput(true);
			urlconn.setUseCaches(false);
			urlconn.setRequestMethod("POST");
			urlconn.setConnectTimeout(timeout);// （单位：毫秒）
			urlconn.setReadTimeout(timeout);// （单位：毫秒）
			urlconn.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			urlconn.setRequestProperty("Accept-Charset", "utf-8");
			urlconn.setRequestProperty("contentType", "utf-8");

			urlconn.connect();
			DataOutputStream out = new DataOutputStream(
					urlconn.getOutputStream());
			out.writeBytes(param);
			out.flush();
			out.close();

			int respCode = urlconn.getResponseCode();
			if (respCode == 200) {
				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(urlconn.getInputStream()));
				String text;
				while ((text = buffer.readLine()) != null) {
					sb.append(text);
				}

				buffer.close();
			} else {
				System.out.println("Response code:" + respCode);
			}
		} catch (UnsupportedEncodingException uee) {
			System.out.println("Encoding error" + uee);
		} catch (MalformedURLException me) {
			System.out.println("New url error" + me);
		} catch (ConnectException ce) {
			System.out.println("Connect error" + ce);
		} catch (IOException ie) {
			System.out.println("Io error" + ie);
		} finally {
			if (urlconn != null)
				urlconn.disconnect();
			url = null;
		}

		return URLDecoder.decode(sb.toString(), "UTF-8");

	}
	
	
	public static String postHttpPro(String OutParam, String requrl)
			throws UnsupportedEncodingException {

		URL url = null;
		HttpURLConnection urlconn = null;
		StringBuffer sb = new StringBuffer();
//		String param = "";

		try {
//			param = URLEncoder.encode(OutParam, "utf-8");
			url = new URL(requrl);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setDoOutput(true);
			urlconn.setDoInput(true);
//			urlconn.setUseCaches(false);
			urlconn.setRequestMethod("POST");
			urlconn.setConnectTimeout(30000);// （单位：毫秒）
			urlconn.setReadTimeout(30000);// （单位：毫秒）
			urlconn.setRequestProperty("Charset", "UTF-8");
			urlconn.setRequestProperty("Content-type", "application/json");
			urlconn.setRequestProperty("Accept-Charset", "utf-8");
//			urlconn.setRequestProperty("User-Agent", "ApiSdk Client v0.1");
		
			urlconn.connect();
			DataOutputStream out = new DataOutputStream(
					urlconn.getOutputStream());
			out.writeBytes(OutParam);
			out.flush();
			out.close();

			int respCode = urlconn.getResponseCode();
			if (respCode == 200) {
				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(urlconn.getInputStream()));
				String text;
				while ((text = buffer.readLine()) != null) {
					sb.append(text);
				}

				buffer.close();
			} else {
				System.out.println("Response code:" + respCode);
			}
		} catch (UnsupportedEncodingException uee) {
			System.out.println("Encoding error" + uee);
		} catch (MalformedURLException me) {
			System.out.println("New url error" + me);
		} catch (ConnectException ce) {
			System.out.println("Connect error" + ce);
		} catch (IOException ie) {
			System.out.println("Io error" + ie);
		} finally {
			if (urlconn != null)
				urlconn.disconnect();
			url = null;
		}

		return URLDecoder.decode(sb.toString(), "UTF-8");

	}

	// public static String postHttp(String OutParam, String requrl) {
	//
	// String responseMsg = "";
	// // 1.构造HttpClient的实例
	// HttpUtil httpClient = new HttpUtil();
	// httpClient.getParams().setContentCharset("UTF-8");
	// // 2.构造PostMethod的实例
	// PostMethod postMethod = new PostMethod(requrl);
	// // 3.把参数值放入到PostMethod对象中
	// postMethod.addParameter("td", OutParam);
	// // postMethod.addParameter("param2", param2);
	// try {
	// // 4.执行postMethod,调用http接口
	// httpClient.executeMethod(postMethod);// 200
	// // 5.读取内容
	// responseMsg = postMethod.getResponseBodyAsString().trim();
	// // 6.处理返回的内容
	// } catch (HttpException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// // 7.释放连接
	// postMethod.releaseConnection();
	// }
	// String returnMsg = "";
	// try {
	// returnMsg = URLDecoder.decode(responseMsg.toString(), "UTF-8");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return returnMsg;
	// }

}
