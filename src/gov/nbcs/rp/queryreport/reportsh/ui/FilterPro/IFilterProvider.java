/**
 * @# IFilterProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import com.foundercy.pf.control.FPanel;

/**
 * 功能说明:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public interface IFilterProvider {

	public static String TYPE_ACCT = "ACCT";

	public static String TYPE_ACCT_TYPE = "ACCT_TYPE";

	public static String TYPE_ACCT_JJ = "ACCT_JJ";
//
//	public static String TYPE_FB_TYPE = "FB_TYPE";
//	
	/** 经费类别. */
//	public static String TYPE_FUND_CLASS = "FUND_CLASS";
	
//	public static String TYPE_FUND_CLASS_FILTER="FUND_CLASS_FILTER";//经费类别的过滤条件

	
	public static String TYPE_PRJSORT = "PRJSORT";

	public static String TYPE_PROJECT = "PROJECT";
	
	public static String TYPE_FUN = "FUN";
	
	public static String TYPE_PRJSTATUS = "PRJSTATUS";

	public static String TYPE_GENERAL = "GENERAL";

	public static String TYPE_DEP = "DEP";

	public static String TYPE_DIV = "DIV";

	public static String TYPE_DIV_KIND = "DIV_KIND";

	public static String TYPE_DIV_FMKIND = "DIV_FMKIND";

	public static String LVL_1 = "3";

	public static String LVL_2 = "5";

	public static String LVL_3 = "7";

	/**
	 * 取得显示的面板
	 * @return
	 */
	public FPanel getFilterPanel();

	/**
	 * 取得过滤条件
	 * @param align　前缀名
	 * @return
	 */
	public String getFilter(String align);

	/**
	 * 是否用户有选择
	 * @return
	 */
	public boolean isSelect();

	/**
	 * 清空所有的选择
	 */
	public void reset();

}
