package com.common.funciton;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
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
public class HttpUtil {
    private static final String IP_CHECK = "http://ip.taobao.com/service/getIpInfo.php";
    private static final String IP_GET = "http://pv.sohu.com/cityjson?ie=utf-8";

    private static String ENCODING = "UTF-8";
    private static String CONTENT_TYPE = "application/json";
    private static RequestConfig requestConfig = null;
    static {
         requestConfig = RequestConfig.custom()
                 .setConnectTimeout(100000)
                 .setConnectionRequestTimeout(50000)
                 .setSocketTimeout(50000)
                 .build();
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

        String s = "var returnCitySN = {\"cip\": \"222.129.19.10\", \"cid\": \"110000\", \"cname\": \"北京市\"};";
        System.out.println(getIPV4());

        //System.out.println(getIpInfo("222.129.19.10"));
    }

    /**
     * 发送get请求
     * @param url
     * @param params
     * @return
     */
    public static String httpGet(String url, HashMap<String, String> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        if (null != params && !params.isEmpty()){
            url = new StringBuffer(url).append("?")
                    .append(getEncodeParamStr(params))
                    .toString();
        }
        System.out.println(url);
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        get.addHeader("User-Agent", "ApiSdk Client v0.1");
        return execute(client, get);
    }

    /**
     * 默认参数格式post请求
     * @param url
     * @param params
     * @return
     */
    public static String httpPostDefault(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);
        post.addHeader("User-Agent", "ApiSdk Client v0.1");
        //装填参数
        List<NameValuePair> nvps = new ArrayList<>();
        if(params!=null && !params.isEmpty()){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, ENCODING);
        post.setEntity(entity);
        return execute(client, post);
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
        post.setEntity(stringEntity);
        return execute(client, post);
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
        post.setEntity(stringEntity);
        return execute(client, post);
    }

    /**
     * 执行post请求
     * @param client
     * @param request
     * @return
     */
    private static String execute(CloseableHttpClient client, HttpRequestBase request){
        String result = null;
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);//发送请求获取响应数据
            int statusCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                System.out.println("response status is " + statusCode);
            }else {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (response != null)response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }



    /**
     * 根据IP获取地址和运营商信息
     * @param ip
     * @return
     */
    public static IpInfo getIpInfo(String ip){
        HashMap<String, String> map = new HashMap<>();
        map.put("ip", ip);
        String s = HttpUtil.httpGet(IP_CHECK, map);
        if (s != null){
            IpCheckResult ipCheckResult = ItvJsonUtil.jsonToObj(s, IpCheckResult.class);
            return StringUtils.equals(ipCheckResult.getCode(), "0") ? ipCheckResult.data : null;
        }
        return null;
    }

    /**
     * 获取本机IP（内网）
     * @return
     */
    public static String getLocalIp() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本机公网IP
     * @return
     */
    public static String getIPV4() {
        String s = HttpUtil.httpGet(IP_GET, null);
        if (s != null){
            return s.substring(28, s.indexOf(",") -1);

        }
        return null;
    }


    /**
     * get请求拼装参数
     *
     * @param parames
     * @return
     */
    private static String getEncodeParamStr(HashMap<String, String> parames){
        String str = "";
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




    static class IpCheckResult{
        private String code;
        private IpInfo data;

        public IpCheckResult() {
        }

        public IpCheckResult(String code, IpInfo data) {
            this.code = code;
            this.data = data;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public IpInfo getData() {
            return data;
        }

        public void setData(IpInfo data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "IpCheckResult{" +
                    "code='" + code + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    static class IpInfo{
        private String ip;
        private String country;
        private String area;
        private String region;
        private String city;
        private String county;
        private String isp;
        private String country_id;
        private String area_id;
        private String region_id;
        private String city_id;
        private String county_id;
        private String isp_id;

        public IpInfo() {
        }

        public IpInfo(String ip, String country, String area, String region, String city, String county, String isp, String country_id, String area_id, String region_id, String city_id, String county_id, String isp_id) {
            this.ip = ip;
            this.country = country;
            this.area = area;
            this.region = region;
            this.city = city;
            this.county = county;
            this.isp = isp;
            this.country_id = country_id;
            this.area_id = area_id;
            this.region_id = region_id;
            this.city_id = city_id;
            this.county_id = county_id;
            this.isp_id = isp_id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCounty_id() {
            return county_id;
        }

        public void setCounty_id(String county_id) {
            this.county_id = county_id;
        }

        public String getIsp_id() {
            return isp_id;
        }

        public void setIsp_id(String isp_id) {
            this.isp_id = isp_id;
        }

        @Override
        public String toString() {
            return "IpInfo{" +
                    "ip='" + ip + '\'' +
                    ", country='" + country + '\'' +
                    ", area='" + area + '\'' +
                    ", region='" + region + '\'' +
                    ", city='" + city + '\'' +
                    ", county='" + county + '\'' +
                    ", isp='" + isp + '\'' +
                    ", country_id='" + country_id + '\'' +
                    ", area_id='" + area_id + '\'' +
                    ", region_id='" + region_id + '\'' +
                    ", city_id='" + city_id + '\'' +
                    ", county_id='" + county_id + '\'' +
                    ", isp_id='" + isp_id + '\'' +
                    '}';
        }
    }
}
