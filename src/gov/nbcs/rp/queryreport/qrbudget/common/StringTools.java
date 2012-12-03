/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.qrbudget.common;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StringTools {

	/**
	 * 判断字符串不为空处理 如果是null or "" or "null" 返回false
	 * 
	 * @param str
	 * @return
	 * @author liutt
	 */
	public static boolean notNull(String str) {
		boolean result = false;
		if (str != null && !str.equals("") && !str.equals("null")) {
			result = true;
		}
		return result;

	}

	/**
	 * 判断字符串为空
	 * 
	 * @param str
	 * @return
	 * @author liutt
	 */
	public static boolean isNull(String str) {
		boolean result = notNull(str);
		return !result;
	}

	public static boolean isNull(List list) {
		if (list == null || list.isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * 将给定的字符串转换为UTF编码的字符串。
	 * 
	 * @param str
	 *            输入字符串
	 * @return 经UTF编码后的字符串，如果有异常，则返回原编码字符串
	 */
	public static String toUTF(String str) {
		if (isNull(str)) {
			return str;
		}
		String retVal = str;
		try {
			retVal = new String(str.getBytes("ISO-8859-1"), "Unicode");// ISO-8859-1//ISO8859_1
			// retVal = new String(str.getBytes("GBK"), "UTF-8");
			// retVal = new String(str.getBytes("ISO-8859-1"), "GBK");
			// retVal = new String(str.getBytes("UTF-8"), "GBK");
			// retVal = new String(str.getBytes("UTF-8"), "GB2312");
			// retVal = new String(str.getBytes("ISO-8859-1"), "GB2312");
			// retVal = new String(str.getBytes("GB2312"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 转换字符串：如（"aa_a","a_","A"）即将字符串"aa_a"中的"a_"转化为"A"
	 * 
	 * @param source
	 * @param oldString
	 * @param newString
	 * @return
	 * 
	 * @author
	 */
	public static String Replace(String source, String oldString,
			String newString) {
		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length();
		int lengthOfOld = oldString.length();
		int posStart;
		int pos;
		for (posStart = 0; (pos = source.indexOf(oldString, posStart)) >= 0; posStart = pos
				+ lengthOfOld) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
		}

		if (posStart < lengthOfSource)
			output.append(source.substring(posStart));
		return output.toString();
	}

	/**
	 * 字符串写到文件
	 * 
	 * @param code
	 * @param filePath
	 * @throws IOException
	 */
	public static void stringToFile(String code, String filePath)
			throws IOException {
		if (StringTools.notNull(code)) {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			out.write(code);
			out.close();
		}

	}

	/**
	 * 文件分行读转换为字符串
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String fileToString(String filePath) throws IOException {
		String code = "";
		File newFile = new File(filePath);
		if (newFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String strLine;
			StringBuffer strb = new StringBuffer();
			while ((strLine = br.readLine()) != null) {
				strb.append(strLine);
			}
			code = strb.toString();
		}

		return code;
	}
}
