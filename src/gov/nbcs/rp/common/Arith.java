package gov.nbcs.rp.common;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * 数字计算辅助类
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

public class Arith {
	//默认精度
	private static final int DEF_DIV_SCALE = 2;
	//校验格式的正则表达式
	//数字,考虑科学计数法
	public static final String NUMBER_PATTERN = "[+,-]?([0-9]+[.])?([0-9])+([E,e]([+,-])?([0-9])+)?" ;
	//非负数字
	public static final String NON_NEGATIVE_NUMBER_PATTERN = "([0-9]+[.])?([0-9])+([E,e]([+,-])?([0-9])+)?" ;
	
	
	/**
	 * 构造方法
	 */
	private Arith(){}
	
	/** 
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();	
	}
	
	
	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));	
		BigDecimal b2 = new BigDecimal(Double.toString(v2));	
		return b1.multiply(b2).doubleValue();	
	}
	
	
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 小数点以后10位，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1,double v2){	
		return div(v1,v2,DEF_DIV_SCALE);
	}
	
	
	
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。
	 * @param v1 被除数 
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
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
	 * 提供精确的小数位四舍五入处理。
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
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
		 * 判断字符串是否为制定格式的数字
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
