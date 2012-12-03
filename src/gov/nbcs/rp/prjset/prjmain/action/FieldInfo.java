/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.action;


/**
 * FieldInfo.java
 * <p>
 * Title:字段信息
 * </p>
 * <p>
 * Description:
 * C1,C2...C20...C
 * 主要用来存放字体的信息
 * 用此类的一目的是为了将(C+数字)信息按其中的数字来排序
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData: May 19, 2008
 * </p>
 * 
 * @author GeXinying
 * @version 1.0
 */
public class FieldInfo implements Comparable {
	//字体信息
	private String name;
	//字体信息中的 数字
	private int num;
	public FieldInfo(String name) {
		this.name = name;
		num = Integer.parseInt(name.substring(1,name.length()));
	}
	public String getName() {
		return name;
	}
	public int getNum() {
		return num;
	}
	/**
	 * 比较方法
	 */
	public int compareTo(Object arg0) {
		int second = ((FieldInfo)arg0).getNum();
		return num>second?1:-1;
	}

}
