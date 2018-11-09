package cn.itcast.hadoop.analylog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志解析类
 * Title: LogParser.java
 * Description: 
 * @author chengge
 * @date 2018年10月18日
 *
 */
public class LogParser {

	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"d/MMM/yyyy:HH:mm:ss",Locale.ENGLISH); //日志文件中的日期格式
	
	public static final SimpleDateFormat dateFormat1 = new SimpleDateFormat(
			"yyyyMMddHHmmss"); //格式后的日期格式
	
	//测试
	public static void main(String[] args) {
		final String S1 = "27.19.74.143 - - [30/May/2013:17:38:20 +0800] \"GET /static/image/common/faq.gif HTTP/1.1\" 200 1127";
		LogParser parser = new LogParser();
		String[] array = parser.parse(S1);
		
		System.out.println("样例数据： " + S1);
		
		 System.out.format(
                 "解析结果：  ip=%s, time=%s, url=%s, status=%s, traffic=%s",
                 array[0], array[1], array[2], array[3], array[4]);
	}
	

	/**
	 * 解析日志的行记录
	 * 
	 * 
	 * @param line
	 * @return 数组 {ip, dateStr, url, status, traffic}
	 */
	public String[] parse(String line) {
		String ip = parseIp(line);
		String time = parseDateStr(line);
		String url = parseURL(line);
		String status = parseStatus(line);
		String traffic = parseTraffic(line);
		
		String[] textArr = new String[] {ip,time,url,status,traffic};
		return textArr;
	}
	
	/**
	 * 解析时间
	 * 把时间字符串转化为  --- 时间
	 */
	private Date parseDateFormat(String string) {
		Date parse = null;
		try {
			parse = FORMAT.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parse;
	}
	
	/**
	 * 从行文本中解析出时间字符串
	 * 解析时间 
	 */
	private String parseDateStr(String line) {
		int start = line.indexOf("[");
		int end = line.lastIndexOf("+0800]");
		String dateStr = line.substring(start + 1, end).trim();
		Date date = parseDateFormat(dateStr);
		return dateFormat1.format(date);
	}
	
	/**
	 * 解析IP
	 */
	private String parseIp(String line) {
		String ip = line.split("- -")[0].trim();
		return ip;
	}
	
	/**
	 * 解析URl
	 */
	private String parseURL(String line) {
		int start = line.indexOf("\"");
		int end = line.lastIndexOf("\"");
		String url = line.substring(start + 1, end);
		return url;
	}
	
	/**
	 * 解析状态
	 */
	private String parseStatus(String line) {
		String code_traffic = line.substring(line.lastIndexOf("\"") + 1).trim();
		String status = code_traffic.split(" ")[0];
		return status;
	}
	/**
	 * 解析流量
	 */
	private String parseTraffic(String line) {
		String code_traffic = line.substring(line.lastIndexOf("\"") + 1).trim();
		String traffic = code_traffic.split(" ")[1];
		return traffic;
	}
	
}
