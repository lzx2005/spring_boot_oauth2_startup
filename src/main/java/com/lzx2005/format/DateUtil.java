package com.lzx2005.format;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	
	public static boolean isDate(String str_input) {

		if (str_input == null || "".equals(str_input))
			return true;

		String rDateFormat = "";
		int length = str_input.length();
		if (length == 8) {
			rDateFormat = "yyyyMMdd";
		} else if (length == 14) {
			rDateFormat = "yyyyMMddHHmmss";
		} else
			return false;
		SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
		formatter.setLenient(false);
		try {
			Date now= formatter.parse(formatter.format(new Date()));
			Date d1 = formatter.parse(str_input);
			if(now.equals(d1)){
				return true;
			}else{
				return now.after(d1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	 public static int compareDate(String strdate1,String strdate2 ){
		 String rDateFormat ="yyyy-MM-dd HH:mm:ss";
		 SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
			formatter.setLenient(false);
			try {
				Date d1= formatter.parse(strdate1);
				Date d2 = formatter.parse(strdate2);
				if(d1.after(d2))
					return -1;
				else 
					return 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 1;
	 }
	
	 public static final ArrayList monthMap = new ArrayList();
		
		static {
			monthMap.add("January_01");
			monthMap.add("Jan[.]_01");
			monthMap.add("Jan_01");
			
			monthMap.add("February_02");
			monthMap.add("Feb[.]_02");
			monthMap.add("Feb_02");
			
			monthMap.add("March_03");
			monthMap.add("MAR[.]_03");
			monthMap.add("MAR_03");
			
			monthMap.add("April_04");
			monthMap.add("Apr[.]_04");
			monthMap.add("APR_04");
			monthMap.add("May_05");
			
			monthMap.add("June_06");
			monthMap.add("Jun[.]_06");
			monthMap.add("Jun_06");
			
			monthMap.add("July_07");
			monthMap.add("Jul[.]_07");
			monthMap.add("JUL_07");
			
			monthMap.add("August_08");
			monthMap.add("AUG[.]_08");
			monthMap.add("AUG_08");
			
			monthMap.add("September_09");
			monthMap.add("Sep[.]_09");
			monthMap.add("Sep_09");
			
			monthMap.add("October_10");
			monthMap.add("Oct[.]_10");
			monthMap.add("Oct_10");
			
			monthMap.add("November_11");
			monthMap.add("Nov[.]_11");
			monthMap.add("Nov_11");
			
			monthMap.add("December_12");
			monthMap.add("Dec[.]_12");
			monthMap.add("Dec_12");
			
			
		}
		
		public static String replaceStringP(String source, String oldstring,
				String newstring) {
			Matcher m = Pattern.compile(oldstring, Pattern.CASE_INSENSITIVE).matcher(source);
			return m.replaceAll(newstring);
		}

		public static String formatMonth(String strTemp) {
			for (int i = 0; i < monthMap.size(); ++i) {
				String value = (String)monthMap.get(i);
				String[] values = value.split("_");
				strTemp = replaceStringP(strTemp, values[0], values[1]);
			}
			
			return strTemp.replaceAll(" ", "");
		}
		
		public static String get14DateType(String strDate){
			
			if(strDate.length()>14){
				return strDate.substring(0, 14);
			}else if(strDate.length()<14){
				for(int i=strDate.length(); i<14; i++){
					strDate+="0";
				}
			}
			return strDate;
		}
		
		public static String get14Date() {
			Date now = new Date();
			DateFormat formatter = null;
			formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
			String strDte = formatter.format(now);
			return strDte;
		}
		
		public static String getCurrDate() {
			Date now = new Date();
			DateFormat formatter = null;
			formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String strDte = formatter.format(now);
			return strDte;
		}
		
		public static String getCurrDate14() {
			Date now = new Date();
			DateFormat formatter = null;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			String strDte = formatter.format(now);
			return strDte;
		}

		/**
		 * 获得几小时前的时间
		 * @param iHours
		 * @return
		 */
		public static String getAgoTime14(int iHours) {

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR,-iHours);
			String strDte = new SimpleDateFormat("yyyyMMddHHmmss").format(cal
					.getTime());
			return strDte;

		}

		
		public static int getYear(String strDate){
			
			if(strDate.length() < 4)
				return -1;
			
			return Integer.valueOf(strDate.substring(0,4));
		}
		
		public static int getMonth(String strDate){
			
			if(strDate.length() < 6)
				return -1;
			
			return Integer.valueOf(strDate.substring(4,6));
		}
		
		public static int getDay(String strDate){
			
			if(strDate.length() < 8)
				return -1;
			
			return Integer.valueOf(strDate.substring(6,8));
		}
		
		public static String formatDate14(String strDate){
			
			if(strDate.length() < 12)
				return strDate;
			
			String strYear = strDate.substring(0,4);
			String strMonth = strDate.substring(4,6);
			String strDay = strDate.substring(6,8);
			
			String strHour = strDate.substring(8,10);
			String strMin = strDate.substring(10,12);
			String strSec = strDate.substring(12);
			
			return strYear+"-"+strMonth+"-"+strDay+" "+strHour+":"+strMin+":"+strSec;
			
		}
		public static String replaceTag(String inputDate) {
			if (inputDate == null||inputDate.length()==0)
				return "";
			if (!"".equals(inputDate)) {
				if (inputDate.matches("(Jan[.]?(uary)?|Feb[.]?(ruary)?|May[.]?|March[.]?|Apr[.]?(il)?|Ju(ly|ne)[.]?|Aug[.]?(ust)?|Oct[.]?(ober)?|(Sept|Nov|Dec)[.]?(ember)?)[ |.]?\\d{1,2}, 20\\d{2}")) {
					try {
						inputDate=inputDate.replace(".", " ");
						inputDate=inputDate.replaceAll("[\\s]{2,}", " ");
						if(inputDate.matches("Sept[.]?(ember)? \\d{1,2}, 20\\d{2}")){
							long d = Date.parse(inputDate);
							Date dat = new Date(d);
							inputDate = new SimpleDateFormat("yyyyMMdd").format(dat);
						}else{
							Date dat =DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.ENGLISH).parse(inputDate);
							inputDate = new SimpleDateFormat("yyyyMMdd").format(dat);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					inputDate = inputDate.replace("-", "/");
					inputDate = inputDate.replace("年", "/");
					inputDate = inputDate.replace("月", "/");
					inputDate = inputDate.replace("日", "");
					long d = Date.parse(inputDate);
					Date dat = new Date(d);
					inputDate = new SimpleDateFormat("yyyyMMdd").format(dat);
				}
			}
			
			return inputDate;
		}
		public static String[] DeleteOneSeconds(Date currentDate){
			String[] mytime=new String[2];
			Calendar calender = Calendar.getInstance();
		    calender.setTime(currentDate);
			 calender.add(Calendar.SECOND, -1);
			 Date d=calender.getTime();
			 String dueTime="";
			 dueTime = new SimpleDateFormat("yyyyMMddHHmmss").format(calender.getTime());
			 mytime[0]=d.toString();
			 mytime[1]=dueTime;
			 return mytime;
		}
		/**
		 * 比较两个时间先后
		 * date1早于date2->true
		 * @param date1
		 * @param date2
		 * @return
		 */
		public static boolean compareDateBefore(String date1, String date2) {
			date1 = date1.replace("-", "");
			date2 = date2.replace("-", "");
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d1;
			Date d2;
			try {
				d1 = df.parse(date1);
				d2 = df.parse(date2);
				if (d1.equals(d2))
					return true;
				boolean flag = d1.before(d2);
				if (flag)
					return true;
				else
					return false;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		public static String formatDate(String inputDate){
			if(inputDate==null||inputDate.length()==0){
				return "";
			}
			try {
				inputDate = inputDate.replace("-", "/");
				inputDate = inputDate.replace("年", "/");
				inputDate = inputDate.replace("月", "/");
				inputDate = inputDate.replace("日", "");
				long d = Date.parse(inputDate);
				Date dat = new Date(d);
				inputDate = new SimpleDateFormat("yyyy-MM-dd").format(dat);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return inputDate;
		}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long sd=1437118593582L;  
        Date dat=new Date(sd);  
        GregorianCalendar gc = new GregorianCalendar();   
        gc.setTime(dat);  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sb=format.format(gc.getTime());  
        System.out.println(sb);
	}
}
