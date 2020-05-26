package com.common.funciton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.common.algorithm.Apple;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * This class consists exclusively of static methods that operate on or return
 * json.which returns a new json、jsonp or object by a specified object or json.
 *
 *
 * <p>The methods of this class all throw a <tt>NullPointerException</tt>
 * if the json or object provided to them are null.
 *
 * 依赖fastjson
 *
 * @Since jdk1.7
 */
public class ItvJsonUtil {
    private final static String NULLSTR = "";
    private final static String JSONP_PREFIX = "callback(";
    private final static String JSONP_SUFFIX = ")";


    private final static SerializeConfig config = new SerializeConfig();
    {
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    private final static SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullStringAsEmpty
    };

    /**
     * 对象转json
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * 对象转jsonp
     * @param object
     * @return
     */
    public static String toJsonp(Object object) {
        return  JSONP_PREFIX + JSON.toJSONString(object, config, features) + JSONP_SUFFIX;
    }

    /**
     * json转对象
     * @param jsonValue
     * @param c 类型
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String jsonValue, Class<T> c) {
        return StringUtils.isBlank(jsonValue) ? null : JSON.parseObject(jsonValue, c);
    }


    /**
     * json转对象 适用与实体中包含集合
     * @param jsonValue
     * @param typeReference 被转对象引用类型  转集合 T=Collection<Clazz>   转obj  T=Clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String jsonValue, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(jsonValue) || typeReference == null)return null;
        return (T)(typeReference.getType().equals(String.class) ? jsonValue : JSON.parseObject(jsonValue, typeReference));
    }

    /**
     * json转List集合
     * @param json
     * @param t 集合内元素类型
     * @param <T>
     * @return
     */
    public static <T>List<T> jsonList(String json, T t){
        String decode="";
        if(json!=null&&!json.equals("")){
            try {
                decode = URLDecoder.decode(json, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            List<T> collection = JSON.parseObject(decode, new TypeReference<List<T>>() {});
            return collection;
        }else{
            return null;
        }
    }

    /**
     * jsonp 转对象
     * @param jsonpValue
     * @param c 对象类型
     * @param <T>
     * @return
     */
    public static <T> T jsonpToObj(String jsonpValue, Class<T> c) {
        String rex = "[()]+";
        if (StringUtils.isBlank(jsonpValue)) {
            return null;
        }
        String[] json = jsonpValue.split(rex);
        if (json.length < 1) {
            throw new IllegalArgumentException("The params of 'jsonpValue' is not invalid,pls check.");
        }
        return JSON.parseObject(json[1],c);
    }


    public static void main(String[] args) {
        /*String jsonp = "callback({'code':'200','curPage':0,'entity':[{'createTime':'2017/08/09','creator':1,'icon':'3','id':3,'isEnabled':1,'modifyTime':'2017/08/22','name':'test3','reviser':1,'sort':1},{'createTime':'2017/08/14','creator':1,'icon':'3','id':4,'isEnabled':1,'modifyTime':'2017/08/22','name':'test4','reviser':1,'sort':1},{'createTime':'2017/08/15','creator':1,'icon':'3','id':5,'isEnabled':1,'modifyTime':'2017/08/22','name':'test5','reviser':1,'sort':1},{'createTime':'2017/08/16','creator':1,'icon':'3','id':6,'isEnabled':1,'modifyTime':'2017/08/22','name':'test6','reviser':1,'sort':1},{'createTime':'2017/08/16','creator':1,'icon':'','id':7,'isEnabled':1,'modifyTime':'2017/08/16','name':'test add column one','reviser':1,'sort':1},{'createTime':'2017/08/16','creator':1,'icon':'','id':8,'isEnabled':1,'modifyTime':'2017/08/16','name':'abc','reviser':1,'sort':1},{'createTime':'2017/08/16','creator':0,'icon':'','id':10,'isEnabled':1,'modifyTime':'2017/08/16','name':'new00','reviser':0,'sort':1},{'createTime':'2017/08/16','creator':0,'icon':'','id':11,'isEnabled':1,'modifyTime':'2017/08/16','name':'xxynew1','reviser':0,'sort':1},{'createTime':'2017/08/17','creator':1,'icon':'','id':12,'isEnabled':1,'modifyTime':'2017/08/17','name':'columnfirst','reviser':1,'sort':1}],'msg':'OK','pageCount':0,'total':''})";
        Map<String,String> map = jsonpToObj(jsonp,new HashMap<String,String>().getClass());
        //String entity = map.get("entity").toString();
        System.out.println(map);*/

        ArrayList<Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        apples.add(new Apple());
        String s = ItvJsonUtil.toJson(new Apple());
        String s1 = ItvJsonUtil.toJson(apples);

        Apple apple2 = jsonToObj(s, new TypeReference<Apple>() {
        });
        System.out.println(apple2);


        List<Apple> apples1 = ItvJsonUtil.jsonToObj(s1, new TypeReference<List<Apple>>() {
        });
        System.out.println(apples1);


    }
    
}
