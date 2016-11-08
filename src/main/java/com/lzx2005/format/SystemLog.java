package com.lzx2005.format;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类介绍：此类为测试日志输出控制类，可以输出到后台，也可写入指定的测试日志文件
 * 
 */
public class SystemLog {

	public static SystemLog log = null;
	private StringBuffer stringBuffer;

	/**
	 * 功能：私有构造函数，负责加载配置文件类，获取相关配置信息
	 */
	public SystemLog() {
	}

	public static SystemLog getInstance() {
		if (log == null) {
			log = new SystemLog();
		}
		return log;
	}

	// ****************************************************************************************************************
	// 此部分为错误日志输出和错误日志输出控制方法
	// ****************************************************************************************************************

	/**
	 * 功能：测试日志输出方法
	 * 
	 * @param msg
	 *            字符串，输出的信息内容
	 */
	public void showMsg(String msg) {
		writeLogFile(msg);
	}

	/**
	 * 功能：测试日志输出方法
	 * 
	 * @param msg
	 *            字符串，输出的信息内容
	 */
	public void writeToFile(String msg) {

		// 组合日志信息
		StringBuffer buf = new StringBuffer();
		buf.append(msg + "\r\n");
		// 写入日志文件
		writeLogFile(buf.toString());
	}

	/**
	 * 功能：测试日志输出方法
	 * 
	 * @param msg
	 *            字符串，输出的信息内容
	 */
	public void writeToFile(String msg, String type,String year) {

		// 组合日志信息
		StringBuffer buf = new StringBuffer();
		buf.append(msg + "\r\n");
		// 写入日志文件
		writeLogFile(buf.toString(),type, year);
	}
	
	/**
	 * 功能：测试日志输出方法
	 * 
	 * @param msg
	 *            字符串，输出的信息内容
	 */
	public  void write2File(String msg) {
		// 写入日志文件
		writeLogFile(msg + "\r\n");
	}

	/**
	 * 功能：测试日志文件写入方法
	 * 
	 * @param msg
	 */
	private synchronized void writeLogFile(String msg) {
		// 利用月份创建文件夹，利用日期获得日志文件，采用追加的方式写入日志文件
		Date date_now=new Date();
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		// 组合日志信息
		StringBuffer buf =  new StringBuffer();
		buf.append(dateformat.format(date_now) + " " + msg);
		System.out.println(dateformat.format(date_now) + " " + msg);
		BufferedWriter bw = null;
		try {
			// 连接年、月文件夹路径`
			DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss",Locale.US);
			String date = format.format(date_now);
			// 判断主路径是否存在，如果不存在创建路径
			File path = new File("logs/" + date.substring(0, 4) + "/"
					+ date.substring(4, 6) + "/");
			if (!path.exists()) {
				path.mkdirs();
			}
			bw = new BufferedWriter(new FileWriter("logs/"
					+ date.substring(0, 4) + "/" + date.substring(4, 6) + "/"
					+ date.substring(0, 8) + ".txt", true));
			bw.write(buf.toString()+"\r\n");
			bw.flush();
		} catch (Exception e) {
			System.out.println("测试日志文件写入失败！");
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (Exception e) {
			}
		}
	}

	public synchronized String readFile(String name,String year) {
		
		File file = new File("RecordInfo/"+name+"/"+year+".txt");
		try {
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				lineTxt = bufferedReader.readLine();
				read.close();
				return lineTxt;
			} else {
				return "";
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 功能：测试日志文件写入方法
	 * 
	 * @param msg
	 */
	private synchronized void writeLogFile(String msg,String name, String year) {
		// 利用月份创建文件夹，利用日期获得日志文件，采用追加的方式写入日志文件
		BufferedWriter bw = null;
		try {
			// 判断主路径是否存在，如果不存在创建路径
			File path = new File("RecordInfo/"+name);
			if (!path.exists()) {
				path.mkdirs();
			}
			bw = new BufferedWriter(new FileWriter("RecordInfo/"+name+"/"+ year + ".txt", false));
			bw.write(msg);
			bw.flush();
		} catch (Exception e) {
			System.out.println("测试日志文件写入失败！");
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 功能：测试日志文件写入方法
	 * 
	 * @param msg
	 */
	public synchronized void writePdfLogFile(String msg) {
		// 利用月份创建文件夹，利用日期获得日志文件，采用追加的方式写入日志文件
		BufferedWriter bw = null;
		try {
			// 判断主路径是否存在，如果不存在创建路径
			File path = new File("PdfRecord/");
			if (!path.exists()) {
				path.mkdirs();
			}
			bw = new BufferedWriter(new FileWriter("PdfRecord/PdfBreakDown.txt", true));
			bw.write(msg+"\r");
			bw.flush();
		} catch (Exception e) {
			System.out.println("测试日志文件写入失败！");
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
		System.out.println(format.format(new Date()));*/
		SystemLog sl = new SystemLog();
		sl.writeLogFile("sadafcdsada", "cbw", "2016");
	}

}
