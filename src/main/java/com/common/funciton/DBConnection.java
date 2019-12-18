package com.common.funciton;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DBConnection<T> {
	private final static ThreadLocal<Connection> tl=new ThreadLocal();//创建线程局部变量
	public static ThreadLocal<Connection> getThreadLocal(){
		return tl;
	}
	private static String driver = "oracle.jdbc.OracleDriver";
	private static String url = "jdbc:oracle:thin:@10.6.17.50:1521:itunesdb";
	private static String user = "walldev";
	private static String password = "123456";
	/**
	 * 从线程局部变量中获取连接，未获取到则创建一个
	 *
	 * @return
	 */
	public static Connection getCon(){
		Connection con = tl.get();		
		if(con==null){
			try {
				Class.forName(driver);
				con=DriverManager.getConnection(url,user,password);
				tl.set(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return con;
	}

    /**
     * 处理结果集
     * @param rs
     * @param keys
     * @return
     * @throws SQLException
     */
	public static HashMap<String, Object> dealResultSet(ResultSet rs, ArrayList<String> keys) {
        HashMap<String, Object> resultMap = new HashMap();
		try {
			if (rs.next() && !CollectionUtils.isEmpty(keys)){
				for (String s : keys) {
					Object o = rs.getObject(s);
					if (o instanceof String)resultMap.put(s, rs.getString(s));
					if (o instanceof Boolean)resultMap.put(s, rs.getBoolean(s));
					if (o instanceof Date)resultMap.put(s, rs.getDate(s));
					if (o instanceof Byte)resultMap.put(s, rs.getByte(s));
					if (o instanceof Short)resultMap.put(s, rs.getShort(s));
					if (o instanceof Integer)resultMap.put(s, rs.getInt(s));
					if (o instanceof Long)resultMap.put(s, rs.getLong(s));
					if (o instanceof Float)resultMap.put(s, rs.getFloat(s));
					if (o instanceof Double)resultMap.put(s, rs.getDouble(s));
					if (o instanceof BigDecimal)resultMap.put(s, rs.getBigDecimal(s));
					if (o instanceof Clob)resultMap.put(s, rs.getClob(s));
					if (o instanceof Blob)resultMap.put(s, rs.getBlob(s));
					if (o instanceof Array)resultMap.put(s, rs.getArray(s));
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			return resultMap;
		}
    }
	public static void main(String[] args) throws SQLException {
        Connection con = getCon();
        PreparedStatement statement = con.prepareStatement("SQL ");//预执行sql
        //给占位符设值 第几个？和要设的值
        //statement.setString(1, "");
        statement.execute();//执行
        ResultSet resultSet = statement.executeQuery();//查询

        HashMap<String, Object> map = dealResultSet(resultSet, new ArrayList());

    }
}
