package com.quange.jhds;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.TextView;

public class DateConverter {
	private static final String TAG = "DateConverter";

	/**
	 * 是否显示时间
	 */
	public static boolean isDisplayTime(Date lastTime, Date currentTime) {

		long minuteBetween = (currentTime.getTime() - lastTime.getTime())
				/ (60 * 1000);

		System.out.println("minuteBetween=========" + minuteBetween);

		if (minuteBetween >= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 消息列表时间（分段）是否显示时间
	 */
	public static boolean isDisplayTimeInfo(Date lastTime, Date currentTime) {

		long minuteBetween = (currentTime.getTime() - lastTime.getTime())
				/ (60 * 1000);

		System.out.println("minuteBetween=========" + minuteBetween);

		if (minuteBetween >= 24 * 60) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isDisplayTimeInfo2(Date currentTime) {
		if (currentTime == null)
			return false;
		final Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTime(currentTime); // 设置时间为当前时间
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH) + 1;
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		int hour = mCalendar.get(Calendar.HOUR);
		int minute = mCalendar.get(Calendar.MINUTE);

		final Calendar mCalendar2 = Calendar.getInstance();
		mCalendar2.setTime(new Date()); // 设置时间为当前时间
		int curYear = mCalendar2.get(Calendar.YEAR);
		int curMonth = mCalendar2.get(Calendar.MONTH) + 1;
		int curDay = mCalendar2.get(Calendar.DAY_OF_MONTH);

		if (year < curYear) {
			return false;
		} else if (year == curYear) {
			if (month < curMonth) {
				return false;
			} else if (month == curMonth) {
				if (day < curDay) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * 格式化时间戳为 日期格式：yyyy-MM-dd HH:mm:ss
	 * @param timeStr
	 * @return
	 */
	public static String timeStrToString(long timeStr) {
		if (timeStr > 5000000000l) {
			timeStr = timeStr / 1000;
		}
		timeStr = timeStr * 1000;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		return df.format(new Date(timeStr));
	}
	
	/**
	 * 显示的时间格式
	 */
	public static String displayTime(Date currentTime) {
		if (currentTime == null) {
			
			return "";
		}
		try {
			String headStr = null;
			final Calendar mCalendar = Calendar.getInstance();
			mCalendar.setTime(currentTime); // 设置时间为当前时间
			int year = mCalendar.get(Calendar.YEAR);
			int month = mCalendar.get(Calendar.MONTH) + 1;
			int day = mCalendar.get(Calendar.DAY_OF_MONTH);
			int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
			int minute = mCalendar.get(Calendar.MINUTE);

			final Calendar mCalendar2 = Calendar.getInstance();
			mCalendar2.setTime(new Date()); // 设置时间为当前时间
			int curYear = mCalendar2.get(Calendar.YEAR);
			int curMonth = mCalendar2.get(Calendar.MONTH) + 1;
			int curDay = mCalendar2.get(Calendar.DAY_OF_MONTH);

			if (year == curYear) {
				if (month == curMonth) {
					if (day == curDay) {
						headStr = "今天";
					} else if (day + 1 == curDay) {
						headStr = "昨天";
					} else if (day + 2 == curDay) {
						headStr = "前天";
					} else {
						headStr = month + "/" + day;
					}
				} else {
					headStr = month + "/" + day;
				}
			} else {
				String years = Integer.toString(year);
				headStr = years.substring(2, years.length()) + "/" + month
						+ "/" + day;
			}

			return headStr + " " + hour + ":" + displayMinute(minute);

			// int apm = mCalendar.get(Calendar.AM_PM);
			// switch(apm){
			// case 0:
			// if(hour >= 0 && hour < 6){
			// // return headStr +" " + "凌晨" + hour + ":" +
			// displayMinute(minute);
			// return headStr +" " + hour + ":" + displayMinute(minute);
			// }else{
			// // return headStr +" " + "上午" + hour + ":" +
			// displayMinute(minute);
			// return headStr +" " + hour + ":" + displayMinute(minute);
			// }
			// case 1:
			// if(hour >= 0 && hour < 6){
			// hour = hour + 12;
			// // return headStr +" " + "下午" + hour + ":" +
			// displayMinute(minute);
			// return headStr +" " + hour + ":" + displayMinute(minute);
			// }else{
			// hour = hour + 12;
			// // return headStr +" " + "晚上" + hour + ":" +
			// displayMinute(minute);
			// return headStr +" " + hour + ":" + displayMinute(minute);
			// }
			// default:
			// break;
			// }
			// return headStr;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";

	}

	/**
	 * 显示通知栏时间
	 */
	public static String displayNotifiTime(Date currentTime) {
		try {
			String headStr = null;

			final Calendar mCalendar = Calendar.getInstance();
			mCalendar.setTime(currentTime); // 设置时间为当前时间
			int year = mCalendar.get(Calendar.YEAR);
			int month = mCalendar.get(Calendar.MONTH) + 1;
			int day = mCalendar.get(Calendar.DAY_OF_MONTH);
			int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
			int minute = mCalendar.get(Calendar.MINUTE);

			String years = Integer.toString(year);
			headStr = years.substring(2, years.length()) + "/" + month + "/"
					+ day;
			return headStr + " " + hour + ":" + displayMinute(minute);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";

	}

	private static String displayMinute(int minute) {
		String countStr = Integer.valueOf(minute).toString();
		if (countStr.length() > 1) {
			return countStr;
		} else {
			return "0" + countStr;
		}

	}

	/**
	 * 时间转换成Date "2015-02-05 11:21:45"---> Date
	 * 
	 * @param timeStr
	 * @return
	 */
	public static Date String2Date(String timeStr) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINESE);
		try {
			if (timeStr != null && timeStr.length() < 19) {
				if ("null".equals(timeStr)) {
					
					return null;
				}else if(timeStr.length() == 10){
					timeStr = timeStr + " 00:00:00";
				} else if(timeStr.length() == 16){
					timeStr = timeStr + ":00";
				}
			}
			Date date = df.parse(timeStr);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 10位时间戳转Date
	 * 
	 * @param time
	 * @return
	 */
	public static Date TimestampToDate(Long time) {
		if (time.toString().length()> 10)
			time = time / 1000;

		long temp = time * 1000;
		Timestamp ts = new Timestamp(temp);
		Date date = new Date();
		try {
			date = ts;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 时间间隔1分钟 大于10秒 true 小于10秒 false
	 */
	public static boolean is1Minute(Date beforeDate, Date nowDate) {
		long nowTime = nowDate.getTime();
		long lastTime = beforeDate.getTime();
		long time = nowTime - lastTime;
		if (time > (long) 10000) {
			return true;
		}
		return false;
	}

	/**
	 * 获取开始时间
	 * 
	 * @param tv
	 */
	public static String[] getTime(TextView tv) {
		String[] timeArr = { "", "", "", "" };
		try {
			String str = tv.getText().toString().trim();
			if (str == null || str.equals("")) {
				str = tv.getHint().toString().trim();
			}

			timeArr[0] = str.substring(0, 4);
			timeArr[1] = str.substring(5, str.lastIndexOf("-") - 1);

			if (!str.contains("现在")) {
				timeArr[2] = str.substring(str.lastIndexOf("-") + 1,
						str.lastIndexOf("-") + 5);
				timeArr[3] = str.substring(str.lastIndexOf("-") + 6,
						str.length() - 1);
			} else {
				timeArr[2] = getCurrentYear();
				timeArr[3] = getCurrentMonth();
			}
			return timeArr;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	/**
	 * 获取当前年份
	 * 
	 * @return
	 */
	private static String getCurrentYear() {
		Calendar now = Calendar.getInstance();
		return Integer.toString(now.get(Calendar.YEAR));
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	private static String getCurrentMonth() {
		Calendar now = Calendar.getInstance();
		return Integer.toString(now.get(Calendar.MONTH) + 1);
	}

	/**
	 * 2015-1-2------》2015年1月
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String getStartEndTime(String startTime, String endTime) {
		String[] startArr = startTime.split("-");
		String[] endArr = endTime.split("-");

		StringBuffer time = new StringBuffer();
		if (startTime != null && !startTime.equals("")) {
			time.append(startArr[0]).append("年").append(startArr[1])
					.append("月");
		}

		if (endTime.equals("现在")) {
			time.append("-").append("现在");
		} else if (endTime != null && !endTime.equals("")) {
			time.append("-").append(endArr[0]).append("年").append(endArr[1])
					.append("月");
		}
		return time.toString();

	}
	
	/**
	 * 时间格式转换
	 * "postTime": "/Date(1418355260000)/"————————》項目中常用格式
	 */
	public static String getDateFormat(String postTime){
		try{
			if(postTime.contains("(")){
				String stringPostTime = postTime.substring(postTime.lastIndexOf("(")+1, postTime.lastIndexOf(")"));
				Long longPostTime = Long.valueOf(stringPostTime);
				Date datePostTime = TimestampToDate(longPostTime);
				String formatTime = displayTime(datePostTime);
				return "分享于   " + formatTime; 
			}else{
				Long longPostTime = Long.valueOf(postTime);
				Date datePostTime = TimestampToDate(longPostTime);
				String formatTime = displayTime(datePostTime);
				return formatTime;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ""; 
	}

	/**
	 * 时间转换成Date "2015-02-05 11:21:45"---》 Date----》項目中常用格式
	 * @param timeStr
	 * @return
	 */
	public static String getDateFormat2(String postTime){
		try{
			Date datePostTime = String2Date(postTime);
			String formatTime = displayTime(datePostTime);
			return "分享于   " + formatTime; 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ""; 
	}
	
}
