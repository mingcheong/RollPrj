package gov.nbcs.rp.common;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * ���ּ��㸨����
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */

public class Arith {
	//Ĭ�Ͼ���
	private static final int DEF_DIV_SCALE = 2;
	//У���ʽ��������ʽ
	//����,���ǿ�ѧ������
	public static final String NUMBER_PATTERN = "[+,-]?([0-9]+[.])?([0-9])+([E,e]([+,-])?([0-9])+)?" ;
	//�Ǹ�����
	public static final String NON_NEGATIVE_NUMBER_PATTERN = "([0-9]+[.])?([0-9])+([E,e]([+,-])?([0-9])+)?" ;
	
	
	/**
	 * ���췽��
	 */
	private Arith(){}
	
	/** 
	 * �ṩ��ȷ�ļӷ����㡣
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������ĺ�
	 */
	public static double add(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * �ṩ��ȷ�ļ������㡣
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������Ĳ�
	 */
	public static double sub(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();	
	}
	
	
	/**
	 * �ṩ��ȷ�ĳ˷����㡣
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������Ļ�
	 */
	public static double mul(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));	
		BigDecimal b2 = new BigDecimal(Double.toString(v2));	
		return b1.multiply(b2).doubleValue();	
	}
	
	
	/**
	 * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ��
	 * С�����Ժ�10λ���Ժ�������������롣
	 * @param v1 ������
	 * @param v2 ����
	 * @return ������������
	 */
	public static double div(double v1,double v2){	
		return div(v1,v2,DEF_DIV_SCALE);
	}
	
	
	
	/**
	 * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ
	 * �����ȣ��Ժ�������������롣
	 * @param v1 ������ 
	 * @param v2 ����
	 * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
	 * @return ������������
	 */
	public static double div(double v1,double v2,int scale){
		if(scale<0){	
			throw new IllegalArgumentException("The scale must be a positive integer or zero");	
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * �ṩ��ȷ��С��λ�������봦��
	 * @param v ��Ҫ�������������
	 * @param scale С���������λ
	 * @return ���������Ľ��
	 */
	public static double round(double v,int scale){
		if(scale<0){	
			throw new IllegalArgumentException("The scale must be a positive integer or zero");	
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	 public static void main(String []args){
	 }
	 
		/**
		 * �ж��ַ����Ƿ�Ϊ�ƶ���ʽ������
		 * @param str
		 * @return
		 * @author yangxuefeng 20080429
		 */
		public static boolean isNumeric(String numStr , String patStr){
			if((numStr == null) || (numStr.trim().length() == 0)){
				return false ;
			}
			Pattern pattern = Pattern.compile(patStr);
			Matcher isNum = pattern.matcher(numStr);
			if( !isNum.matches() ){
				return false;
			}
			return true;
		} 
}
