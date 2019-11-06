package com.common.funciton;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2018/7/20 9:14
 * @创建时jdk版本 1.8
 */
public class HttpClient {

    private static String ENCODING = "UTF-8";
    private static String CONTENT_TYPE = "application/json";
    private static RequestConfig requestConfig = null;
    static {
         requestConfig = RequestConfig.custom()
                 .setConnectTimeout(20000)
                 .setConnectionRequestTimeout(10000)
                 .setSocketTimeout(10000)
                 .build();
    }

    /**
     * 发送get请求
     * @param url
     * @param params
     * @return
     */
    public static String httpGet(String url, HashMap<String, String> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        String completeUrl = new StringBuffer(url).append("?")
                .append(getEncodeParamStr(params))
                .toString();
        HttpGet get = new HttpGet(completeUrl);
        get.setConfig(requestConfig);
        get.addHeader("User-Agent", "ApiSdk Client v0.1");
        CloseableHttpResponse response = null;
        try {
            response = client.execute(get);
            return readResponseData(response);
        }catch (Exception e){
            throw new RuntimeException("请求发送失败");
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                throw new RuntimeException("响应关闭失败");
            }
        }
    }

    /**
     * 默认参数格式post请求
     * @param url
     * @param params
     * @return
     */
    public static String httpPostDefault(String url, HashMap<String, String> params){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);
        post.addHeader("User-Agent", "ApiSdk Client v0.1");
        //装填参数
        List<NameValuePair> nvps = new ArrayList<>();
        if(params!=null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, ENCODING);
            post.setEntity(entity);
            response = client.execute(post);//发送请求获取响应数据
            return readResponseData(response);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("请求发送失败");
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                throw new RuntimeException("响应关闭失败");
            }
        }
    }

    /**
     * 发送带有令牌请求头 参数格式为json的post请求
     * @param url
     * @param paramJson
     * @param token
     * @return
     */
    public static String httpPostJsonHeaderToken(String url, String paramJson, String token){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);
        post.addHeader("User-Agent", "ApiSdk Client v0.1");
        post.addHeader("token", token);
        //装填参数
        StringEntity stringEntity = new StringEntity(paramJson, ENCODING);
        stringEntity.setContentType(CONTENT_TYPE);
        stringEntity.setContentEncoding(ENCODING);
        CloseableHttpResponse response = null;
        String s = postExecute(client, post, stringEntity, response);
        return s;
    }

    /**
     * 发送json格式POST请求
     *
     * @param url
     * @param paramJson
     * @return
     */
    public static String httpPostJson(String url, String paramJson){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);
        post.addHeader("User-Agent", "ApiSdk Client v0.1");
        StringEntity stringEntity = new StringEntity(paramJson, ENCODING);
        stringEntity.setContentType(CONTENT_TYPE);
        stringEntity.setContentEncoding(ENCODING);
        CloseableHttpResponse response = null;
        String s = postExecute(client, post, stringEntity, response);
        return s;
    }

    /**
     * 获取响应数据
     *
     * @param response
     * @return
     * @throws IOException
     */
    private static String readResponseData(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
            System.out.println(response.getStatusLine().getStatusCode());
            throw new RuntimeException("访问失败");
        }
        InputStream inputStream = response.getEntity().getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer result = new StringBuffer();
        String line = "";
        if ((line = bufferedReader.readLine()) != null)result.append(line);
        return result.toString();
    }

    /**
     * get请求拼装参数
     *
     * @param parames
     * @return
     */
    public static String getEncodeParamStr(HashMap<String, String> parames){
        String str = "";
        if (!parames.isEmpty()){
            Object[] keyArray = parames.keySet().toArray();
            for(int i = 0; i < keyArray.length; i++){
                String key = (String)keyArray[i];
                if(0 == i){
                    str += (key + "=" + parames.get(key));
                }
                else{
                    str += ("&" + key + "=" + parames.get(key));
                }
            }
        }
        try {
            str = URLEncoder.encode(str, ENCODING)
                    .replace("%3A", ":")
                    .replace("%2F", "/")
                    .replace("%26", "&")
                    .replace("%3D", "=")
                    .replace("%3F", "?");
        } catch (Exception e) {
            throw new RuntimeException("参数字符串编码失败");
        }
        return str;
    }

    /**
     * 执行post请求
     * @param client
     * @param post
     * @param stringEntity
     * @param response
     * @return
     */
    private static String postExecute(CloseableHttpClient client, HttpPost post, StringEntity stringEntity, CloseableHttpResponse response){
        try {
            post.setEntity(stringEntity);
            response = client.execute(post);//发送请求获取响应数据
            return readResponseData(response);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("请求发送失败");
        }finally {
            try {
                if (response != null)response.close();
            } catch (IOException e) {
                throw new RuntimeException("响应关闭失败");
            }
        }
    }
    public static void main(String[] args) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", "111111");
        /*params.put("appId", "CSP001");
        params.put("requestId", "147258");
        params.put("commType", "61");
        params.put("orderId", "eea05db6-ac7b-41ee-b839-82e073f0");
        params.put("txnType", "5014");
        params.put("signature", "5a380eff89a062c40d2029a41c3ffd1d");*/
        /*String s1 = httpPostJson("http://10.7.13.231:8081/zhxApi/transaction/findOrderDetail", params);
        System.out.println(s1);*/

        /*String s1 = httpPostDefault("http://10.7.13.231:8081/zhxApi/transaction/findOrderDetail", params);
        System.out.println(s1);*/

        /*String s = httpPostDefault("http://10.7.13.231:8080/testParam", params);
        System.out.println(s);*/

        String s1 = httpPostJson("http://www.baidu.com", "");
        System.out.println(s1);
    }
}
