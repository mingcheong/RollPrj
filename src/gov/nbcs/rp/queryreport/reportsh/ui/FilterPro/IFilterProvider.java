/**
 * @# IFilterProvider.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import com.foundercy.pf.control.FPanel;

/**
 * ����˵��:
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
	/** �������. */
//	public static String TYPE_FUND_CLASS = "FUND_CLASS";
	
//	public static String TYPE_FUND_CLASS_FILTER="FUND_CLASS_FILTER";//�������Ĺ�������

	
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
	 * ȡ����ʾ�����
	 * @return
	 */
	public FPanel getFilterPanel();

	/**
	 * ȡ�ù�������
	 * @param align��ǰ׺��
	 * @return
	 */
	public String getFilter(String align);

	/**
	 * �Ƿ��û���ѡ��
	 * @return
	 */
	public boolean isSelect();

	/**
	 * ������е�ѡ��
	 */
	public void reset();

}
