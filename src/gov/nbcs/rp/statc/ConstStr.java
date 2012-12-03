/**
 * @Copyright �㽭���Ű�Ȩ����
 * 
 * @ProjectName ��������������Ŀϵͳ
 * 
 * @aouthor ���ܱ�
 * 
 * @version 1.0
 */

package gov.nbcs.rp.statc;

import java.util.regex.Pattern;




/**
 * @author ���ܱ�
 * 
 * @version ����ʱ�䣺2012-3-7 ����06:44:39
 * 
 * @Description ��̬
 */
public class ConstStr
{
	private static String[] regexStrAry = { "[0-9]+" };

	public static String TREE_ROOT_NAME_DIV = "Ԥ�㵥λ";



	/**
	 * ��ȡ�ַ�������
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
	 * ��ȡ�ļ���׺�����ļ�����
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
	 * ������ʽ
	 */
	public static boolean isPattern(String value, int num)
	{
		Pattern pattern = Pattern.compile(regexStrAry[num], Pattern.CASE_INSENSITIVE);
		return pattern.matcher(value).matches();
	}


	/**
	 * �Ա�����
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
	 * �ж�һ�����ַ�����null����""��
	 * 
	 * @param s
	 *            ���жϵ��ַ���
	 * @return �жϽ��
	 */
	public static boolean isNullStr(String s)
	{
		return (s == null) || (s.length() <= 0);
	}


	/**
	 * ��һ�����󷵻طǿյ��ַ���
	 * 
	 * @param o
	 *            ����Ķ���
	 * @return ���ɵ��ַ���
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
