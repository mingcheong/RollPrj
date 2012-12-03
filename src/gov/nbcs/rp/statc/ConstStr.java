/**
 * @Copyright 浙江易桥版权所有
 * 
 * @ProjectName 宁海财政扶持项目系统
 * 
 * @aouthor 陈宪标
 * 
 * @version 1.0
 */

package gov.nbcs.rp.statc;

import java.util.regex.Pattern;




/**
 * @author 陈宪标
 * 
 * @version 创建时间：2012-3-7 下午06:44:39
 * 
 * @Description 静态
 */
public class ConstStr
{
	private static String[] regexStrAry = { "[0-9]+" };

	public static String TREE_ROOT_NAME_DIV = "预算单位";



	/**
	 * 截取字符串长度
	 * 
	 * @param str
	 *            ,number_len
	 * @return String
	 */
	public static String getString(String str, final int number_len)
	{
		if (!str.equals("") && str != null)
		{
			str = str.trim();
			if (str.length() > number_len)
			{
				StringBuffer sb = new StringBuffer();
				str = sb.append(str.substring(0, number_len)).toString();
				sb.delete(0, sb.length());
			}
		}
		return str;
	}


	/**
	 * 获取文件后缀名和文件名称
	 */
	public static String getFile(String fileName, boolean f)
	{
		int pos = fileName.lastIndexOf(".");
		String fileType = fileName.substring(pos);
		if (f)
			return fileType.substring(1, fileType.length());
		else
			return fileName.substring(0, pos);
	}


	/**
	 * 正则表达式
	 */
	public static boolean isPattern(String value, int num)
	{
		Pattern pattern = Pattern.compile(regexStrAry[num], Pattern.CASE_INSENSITIVE);
		return pattern.matcher(value).matches();
	}


	/**
	 * 对比数据
	 * 
	 * @param isXt
	 *            ,v1,v2
	 * @return boolean
	 */
	public static boolean isStrBj(boolean isXt, String v1, String v2)
	{
		if (isXt)
			return isXt;
		else
		{
			if (!v1.equals(v2))
				return true;
			else
				return false;
		}
	}


	/**
	 * 判断一个空字符串（null或者""）
	 * 
	 * @param s
	 *            待判断的字符串
	 * @return 判断结果
	 */
	public static boolean isNullStr(String s)
	{
		return (s == null) || (s.length() <= 0);
	}


	/**
	 * 由一个对象返回非空的字符串
	 * 
	 * @param o
	 *            传入的对象
	 * @return 生成的字符串
	 */
	public static String nonNullStr(Object o)
	{
		return nonNullStr(o, "");
	}


	public static String nonNullStr(Object o, String def)
	{
		String s = o == null ? def : o.toString();
		if (s.equalsIgnoreCase("null"))
		{
			s = "";
		}
		return s;
	}
}
