package com.common.funciton;

import com.common.algorithm.Apple;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON工具类
 * 依赖 jackjson
 * @author
 */
public class JsonUtility {
	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		// 对象字段全部列入

		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		// 取消默认转换timestamps形式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
		// 忽略空bean转json的错误
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		// 统一日期格式yyyy-MM-dd HH:mm:ss
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// 忽略在json字符串中存在,但是在java对象中不存在对应属性的情况
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
	}

	/**
	 * Object转json字符串
	 * @param obj
	 * @param <T>
	 * @return*/

	public static <T> String obj2Json(T obj){
		if (obj == null){
			return null;
		}
		try {
			return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			System.out.println("Parse object to String error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Object转json字符串并格式化美化
	 * @param obj
	 * @param <T>
	 * @return*/

	public static <T> String obj2JsonPretty(T obj){
		if (obj == null){
			return null;
		}
		try {
			return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			System.out.println("Parse object to String error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * string转object
	 * @param str json字符串
	 * @param clazz 被转对象class
	 * @param <T>
	 * @return*/

	public static <T> T json2Obj(String str, Class<T> clazz){
		if (StringUtils.isEmpty(str) || clazz == null){
			return null;
		}
		try {
			return clazz.equals(String.class)? (T) str :objectMapper.readValue(str,clazz);
		} catch (IOException e) {
			System.out.println("Parse String to Object error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * string转object
	 * @param str json字符串
	 * @param typeReference 被转对象引用类型  转集合 T=Collection<Clazz>   转obj  T=Clazz
	 * @param <T>
	 * @return*/

	public static <T> T json2Obj(String str, TypeReference<T> typeReference){
		if (StringUtils.isEmpty(str) || typeReference == null){
			return null;
		}
		try {
			return (T)(typeReference.getType().equals(String.class) ? str :objectMapper.readValue(str,typeReference));
		} catch (IOException e) {
			System.out.println("Parse String to Object error");
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * string转object 用于转为集合对象
	 * @param str json字符串
	 * @param collectionClass 被转集合class
	 * @param elementClasses 被转集合中对象类型class
	 * @param <T>
	 * @return*/

	public static <T> T json2Collections(String str, Class<?> collectionClass, Class<?>... elementClasses){
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
		try {
			return objectMapper.readValue(str,javaType);
		} catch (IOException e) {
			System.out.println("Parse String to Object error");
			e.printStackTrace();
			return null;
		}
	}


	public static void main(String[] args) {
		ArrayList<Apple> apples = new ArrayList<>();
		Apple apple = new Apple();
		Apple apple1 = new Apple();
		apples.add(apple);
		apples.add(apple1);
		String s1 = obj2Json(apple);
		String s = JsonUtility.obj2Json(apples);
		//System.out.println(s);

		List<Apple> list = json2Obj(s, List.class);
		//List<Apple> apples1 = json2Collections(s, List.class, Apple.class);
		Apple apple2 = json2Obj(s1, new TypeReference<Apple>() {
		});
		System.out.println(apple2);
	}
}
