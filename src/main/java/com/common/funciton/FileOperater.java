package com.common.funciton;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:文件操作工具, 限定文本类型
 * 当前功能：
 *      获取缓冲输入流 getBufferedReader
 *      获取缓冲输出流 getBufferedOutputStream
 *      资源关闭 release
 *      读取文件内容 getFileContents （适用文本类型）
 *      文件内容写出 write2File   （适用文本类型）
 *      检查文件是否存在不存在则创建 checkAndCreateFile
 *      大文件切分（仅限文本文件） fileCut   （适用文本类型）
 *      项目内配置文件读取 propertiesRead
 *      根据路径获取properties对象 getProperties
 *      获取当前项目的绝对路径 getProjectPath
 *      获取Tomcat的绝对路径 getTomcatPath
 *      文件下载 downloadFile
 *      文件或文件夹比较 compareFile
 *      文件或文件夹遍历筛选 fileErgodic
 *      文件比较 compareProcess
 * @Author: wangqiang
 * @Date:2019/6/18 14:57
 * @创建时jdk版本 1.8
 */
public class FileOperater {
    private static String fileNameReg = ".(JPEG|jpeg|JPG|jpg|PNG|png|HTML|html|JSP|jsp|TXT|txt|XML|xml|PROPERTIES|properties|CLASS|class|JAVA|java)$";
    public final static String EN_UTF8 = "UTF-8";
    public final static String EN_GBK = "GBK";



    /**
     * 关闭资源
     * @param br
     * @param os
     */
    public static boolean release(BufferedReader br, OutputStream os, InputStream is){
        boolean flag = true;
        try {
            if (null != br)br.close();
            if (null != os)os.close();
            if (null != is)is.close();
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 读取文件内容
     * @param absolutely
     * @param encoding
     * @return
     * @throws IOException
     */
    public static List<String> getFileContents(String absolutely, String encoding){
        ArrayList<String> result = null;
        try {
            if (!StringUtils.isBlank(absolutely)){
                result = new ArrayList<>();
                BufferedReader br = getBufferedReader(absolutely, encoding);
                String tempLine = null;
                while ((tempLine = br.readLine())!= null){
                    result.add(tempLine);
                }
                release(br, null, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写文件 目标文件不存在则返回false
     * @param toAbsolutely
     * @param contents
     */
    public static boolean write2File(String toAbsolutely, List<String> contents){
        try {
            File file = checkFileExists(toAbsolutely);
            if (file == null)return false;
            return write2File(file, contents);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写文件
     * @param toAbsolutely
     * @param contents
     */
    public static void write2File(String toAbsolutely, List<String> contents, boolean isCreate) throws IOException {
        if (CollectionUtils.isEmpty(contents))throw new RuntimeException("contents is null");
        if (isCreate){
            File file = checkAndCreateFile(toAbsolutely);
            write2File(file, contents);
        }else {
            write2File(toAbsolutely, contents);
        }
    }

    /**
     * 写文件 文件不存在则返回false
     * @param toFile
     * @param contents
     */
    public static boolean write2File(File toFile, List<String> contents){
        boolean flag = true;
        try {
            if (CollectionUtils.isEmpty(contents))throw new RuntimeException("contents is null");
            if (null == toFile || !toFile.exists())throw new RuntimeException("target file is null or is not exists");
            BufferedOutputStream bos = getBufferedOutputStream(toFile.getAbsolutePath());
            String tempLine = null;
            for (int i = 0; i < contents.size(); i++) {
                if (i == contents.size() - 1) tempLine = contents.get(i);
                else tempLine = contents.get(i) + "\n";
                bos.write(tempLine.getBytes());
                bos.flush();
            }
            flag = release(null, bos, null);
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
    /**
     * 检查文件是否存在，不存在则创建文件
     * @param absolutely
     * @return
     * @throws IOException
     */
    public static File checkAndCreateFile(String absolutely){
        if (StringUtils.isBlank(absolutely)){
            System.out.println("param absolutely is null or ''");
            return null;
        }
        File file = new File(absolutely);
        try {
            if (!file.exists())file.createNewFile();
            return file;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 检查文件是否存在 不存在则返回null
     * @param absolutely
     * @return
     */
    private static File checkFileExists(String absolutely){
        if (StringUtils.isBlank(absolutely)){
            System.out.println("param absolutely is null or ''");
            return null;
        }
        File file = new File(absolutely);
        if (!file.exists()){
            System.out.println(absolutely + " is not exist");
            return null;
        }
        return file;
    }


    /**
     * 大文件切分
     * @param absolutely
     * @param size
     * @return
     */
    public static boolean fileCut(String absolutely, int size, String encoding){
        boolean flag = true;
        try {
            if (checkFileExists(absolutely) == null)
            if (StringUtils.isBlank(absolutely))throw new RuntimeException("absolutely is null");
            File oriFile = new File(absolutely);
            if (!oriFile.exists())throw new RuntimeException(absolutely + " is not find");
            if (size == 0) throw new RuntimeException("The size of child file could not be 0");
            List<String> contents = getFileContents(absolutely, encoding);
            if (CollectionUtils.isEmpty(contents))throw new RuntimeException("File contents is null");
            //处理文件名
            String fileName = oriFile.getName();
            String[] split = fileName.split("\\.");
            StringBuffer fileNamePri = new StringBuffer("");//子文件名前缀
            for (int i = 0; i < split.length-1; i++) {
                if (i ==0 )fileNamePri.append(split[i]);    //防止出现文件名中含有.的情况
                else fileNamePri.append(".").append(split[i]);
            }
            String fileNameSuf = split[split.length-1];//文件名后缀
            //处理文件内容
            Integer curRowNum = 1;  //当前行数
            int maxRowNum = contents.size();//最大行数
            Integer count = 1; //子文件行计数器
            int childFileSeq = 1;//子文件计数器
            String childFilePath = absolutely.replace(fileName, fileNamePri.toString());//子文件路径
            new File(childFilePath).mkdirs();
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < maxRowNum; i++) {
                list.add(contents.get(i));
                if (count.equals(size) || curRowNum.equals(maxRowNum)){
                    StringBuffer childFileName = new StringBuffer("");
                    childFileName.append(fileNamePri).append("_").append(childFileSeq).append(".").append(fileNameSuf);
                    File file = checkAndCreateFile(childFilePath + File.separator + childFileName.toString());
                    boolean b = write2File(file, list);
                    if (!b)throw new RuntimeException("Contents write to file error");
                    list.clear();
                    childFileSeq++;
                    count = 1;
                }else {
                    count++;
                }
                curRowNum++;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            flag = false;
        }
        return flag;
    }

    /**
     * 根据指定的路径获取properties文件中指定key对应的值
     * @param reference
     * @param key
     * @return
     */
    public static String propertiesRead(String reference, String key){
        String val = null;
        Properties properties = getProperties(reference);
        if (properties != null) val = (String)properties.get(key);
        return val;
    }

    /**
     * 根据指定的路径获取properties文件的内容
     * @param reference
     * @return
     */
    public static Properties getProperties(String reference){
        Properties properties = null;
        try {
            properties = new Properties();
            ClassLoader classLoader = FileOperater.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(reference);
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            properties.load(isr);
        }catch (IOException e){
            System.out.println("文件读取异常");
            e.printStackTrace();
        }
        return properties;
    }


    /**
     * 获取当前工程所在路径   结尾没有分隔符
     *如C:\Users\admin\Desktop\personal files manage\git repository\common-my
     * @return
     * @throws IOException
     */
    public static String getProjectPath() {
        File file = new File("");
        String path = null;
        try {
            path = file.getCanonicalPath();
        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }


    /**
     * 获取tomcat的绝对路径
     * @return
     */
    public static String getTomcatPath(){
        String property = System.getProperty("catalina.home");
        return property;
    }


    /**
     * 文件下载 可用于本地文件复制，也可用于浏览器文件下载
     * @param absolutely 要下载的文件的绝对路径
     * @param os 输出流 可用response获取（浏览器） 也可根据目标路径新建（指定存储路径）
     */
    public static void downloadFile(String absolutely, OutputStream os){
        BufferedInputStream bis = null;
        try {
            byte[] buff = new byte[1024];
            File realFile = new File(absolutely);
            FileInputStream fis = new FileInputStream(realFile);
            bis = new BufferedInputStream(fis);
            int i = 0;
            while ((i = bis.read(buff)) != -1){
                os.write(buff, 0, i);
                os.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                bis.close();
                os.close();
            }catch (Exception e){

            }
        }
    }


    /**
     * 比较两个文件或两个文件夹是否相同
     * @param resultDir 比较结果存储路径
     * @param compareDir1 参与比较文件或文件夹路径
     * @param compareDir2 参与比较文件或文件夹路径
     * @throws IOException
     */
    public static void compareFile(String resultDir, String compareDir1, String compareDir2) throws IOException {
        HashMap<String, String> map1 = new HashMap<>();
        HashMap<String, String> map2 = new HashMap<>();
        fileErgodic(compareDir1, map1);
        fileErgodic(compareDir2, map2);
        ArrayList<String> common = new ArrayList<>();
        ArrayList<String> diff = new ArrayList<>();
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            boolean b = compareProcess(entry.getValue(), map2.get(entry.getKey()));
            if (b) common.add(entry.getValue());
            else diff.add(entry.getValue());
        }
        if(!CollectionUtils.isEmpty(common))
            FileOperater.write2File(resultDir + File.separator + "common.txt", common, true);
        if(!CollectionUtils.isEmpty(diff))
            FileOperater.write2File(resultDir + File.separator + "diff.txt", diff, true);
    }

    /**
     * 遍历目录筛选文件
     * @param filePath 文件或文件夹路径
     * @param map
     */
    public static void fileErgodic(String filePath, Map<String, String> map){
        File file = checkFileExists(filePath);
        if (file != null){
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    if (!file1.isDirectory()){
                        Matcher matcher = Pattern.compile(fileNameReg).matcher(file1.getName());
                        if (matcher.find())map.put(file1.getName(), file1.getAbsolutePath());
                    }else {
                        fileErgodic(file1.getAbsolutePath(), map);
                    }
                }
            }else {
                Matcher matcher = Pattern.compile(fileNameReg).matcher(file.getName());
                if (matcher.find())map.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    /**
     * 文件比较程序 比较两个文件是否一样
     * 方法：验证两个文件的MD5签名
     * 适用范围 .txt .java l
     * @param filePath1 文件的绝对路径
     * @param filePath2 文件的绝对路径
     * @return 比较结果 相同返回true
     */
    public static boolean compareProcess(String filePath1, String filePath2){
        try {
            File file1 = checkFileExists(filePath1);
            File file2 = checkFileExists(filePath2);
            if (file1 == null || file2 == null) System.out.println("传入路径错误，请确认");
            FileInputStream fis1= new FileInputStream(filePath1);
            String md5Hex1 = DigestUtils.md5Hex(fis1);
            FileInputStream fis2= new FileInputStream(filePath2);
            String md5Hex2 = DigestUtils.md5Hex(fis2);
            return md5Hex1.equals(md5Hex2);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    /**
     *  获取缓冲输入流 文件如果不存在，会抛出异常
     * @param absolutely
     * @param encoding
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private static BufferedReader getBufferedReader(String absolutely, String encoding){
        BufferedReader br = null;
        File file = new File(absolutely);
        try {
            if (file.exists()){
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, encoding);
                br = new BufferedReader(isr);
            }else {
                throw new RuntimeException(absolutely + " is not find");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return br;
    }

    /**
     *  获取缓冲输出流 同一文件不会覆盖，只会拼接
     *  文件如果不存在会抛出异常
     * @param absolutely
     * @return
     * @throws FileNotFoundException
     */
    private static BufferedOutputStream getBufferedOutputStream(String absolutely){
        BufferedOutputStream bos = null;
        File file = new File(absolutely);
        try {
            if (file.exists()){
                FileOutputStream fos = new FileOutputStream(file, true);
                bos = new BufferedOutputStream(fos);
            }else {
                throw new RuntimeException(absolutely + " is not find");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bos;
    }



    public static void main(String[] args) throws IOException {
        //文件分割测试
        String filePath = "C:\\Users\\admin\\Desktop\\personal files manage\\git repository\\common\\card.txt";
        /*boolean b = fileCut(filePath, 1000, FileOperater.EN_UTF8);
        System.out.println(b);*/
        /*String s = propertiesRead("config.properties", "aliPayAppId");
        System.out.println(s);*/
        File file = new File(filePath);
        //String realPath = getProjectPath();
        //getTomcatPath();


        //文件比较测试
        /*String resultDir = "D:\\WORK SPACE\\YUM\\百胜交接\\日常处理\\线上代码比较\\20191022";
        String dir1 = "D:\\WORK SPACE\\YUM\\百胜交接\\日常处理\\线上代码比较\\20191022\\162\\CardPlatform";
        String dir2 = "D:\\WORK SPACE\\YUM\\百胜交接\\日常处理\\线上代码比较\\20191022\\local\\CardPlatform";
        compareFile(resultDir, dir1, dir2);*/
    }

}