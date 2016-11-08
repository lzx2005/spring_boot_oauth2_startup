package com.lzx2005.format;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class StringTool {

	public static String IP;
	public static String getStrVal(Object obj){
		
		if(obj == null)
			return "";
		else
			return obj.toString().trim();
	}
	/**
	 * 获取json参数 避免报错
	 * @param obj
	 * @param key
	 * @return
	 */
	public   String getJsonValue(JSONObject obj,String key){
		if(obj==null||obj.isEmpty()){
			return "";
		}
		try {
			if(obj.containsKey(key)){
				return(obj.getString(key));
			}else{
				return "";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return key;
		
	}
	public static String getLocalIp(){
		if(IP == null || IP.equals("")){
			InetAddress addr = null;
			try {
				addr = InetAddress.getLocalHost();
				IP = addr.getHostAddress();
			} catch (UnknownHostException e) {
				System.err.println("获得地址失败");
				e.printStackTrace();
			}
			return IP;
		}
		return IP;
	}
	
	/**
	 * 转换字符集编码
	 * @param strKey
	 * @param strCharset
	 * @return
	 */
	public static String getUrlEncode(String strKey,String strCharset){
		try {
			strKey=URLEncoder.encode(strKey,strCharset);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return strKey;
		}
		return strKey;
	}
	
	/**
	 * 转换字符集解码
	 * @param strKey
	 * @param strCharset
	 * @return
	 */
	public static String getUrlDncode(String strKey,String strCharset){
		try {
			strKey=URLDecoder.decode(strKey, strCharset);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return strKey;
		}
		return strKey;
	}
	
	/**
	 * 格式化采集信息
	 * @param strInput
	 * @return
	 */
	public static String formatString(String strInput){
		try {
			strInput = strInput.replaceAll("\r\n", "");
			strInput = strInput.replaceAll("\\s{2,}", "");
			strInput = strInput.replaceAll("['   ']+", " ");
			strInput = strInput.replaceAll("&nbsp;", "");
			strInput = strInput.replaceAll("&quot;", "\"");
			strInput = strInput.replaceAll("&apos;", "'");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strInput;
	}
	
	
	public static Map<String, Object> parseJSON2Map(String jsonStr){  
        Map<String, Object> map = new HashMap<String, Object>();  
        JSONObject json = JSONObject.fromObject(jsonStr);  
        for(Object k : json.keySet()){  
            Object v = json.get(k);   
            if(v instanceof JSONArray){  
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
                Iterator<JSONObject> it = ((JSONArray)v).iterator();  
                while(it.hasNext()){  
                    JSONObject json2 = it.next();  
                    list.add(parseJSON2Map(json2.toString()));  
                }  
                map.put(k.toString(), list);  
            } else {  
                map.put(k.toString(), v);  
            }  
        }  
        return map;  
    }

	public static String stackTraceToStirng(Exception err)
	{
		String eStr = "";
		StringWriter sw = new StringWriter();
		err.printStackTrace(new PrintWriter(sw, true));
		eStr = sw.toString();
		try {
			sw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return eStr;
	}

	public static void main(String[] args){
		System.out.println(getUrlDncode(getUrlDncode("%25E6%259D%25AD%25E5%25B7%259E%25E6%259C%2589%25E6%2595%25B0%25E9%2587%2591%25E8%259E%258D%25E4%25BF%25A1%25E6%2581%25AF%25E6%259C%258D%25E5%258A%25A1%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8&jglx=%25E4%25BC%2581%25E4%25B8%259A%25E6%25B3%2595%25E4%25BA%25BA%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520%2520&jgdz=%25E6%25B5%2599%25E6%25B1%259F%25E7%259C%2581%25E6%259D%25AD%25E5%25B7%259E%25E5%25B8%2582%25E6%25B1%259F%25E5%25B9%25B2%25E5%258C%25BA%25E4%25B9%259D%25E7%258E%25AF%25E8%25B7%25AF%25E4%25B9%259D%25E5%258F%25B74%25E5%258F%25B7%25E6%25A5%25BC8%25E6%25A5%25BC808%25E5%25AE%25A4", "UTF-8"),"UTF-8"));
		
		System.out.println(getUrlDncode(getUrlDncode("%25E6%25B5%2599%25E6%25B1%259F%25E7%259C%2581%25E6%259D%25AD%25E5%25B7%259E%25E5%25B8%2582%25E8%25B4%25A8%25E9%2587%258F%25E6%258A%2580%25E6%259C%25AF%25E7%259B%2591%25E7%259D%25A3%25E5%25B1%2580", "UTF-8"),"UTF-8"));
	}
}
