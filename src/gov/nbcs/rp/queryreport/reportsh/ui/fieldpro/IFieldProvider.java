/**
 * @# IFieldProvider.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import com.foundercy.pf.control.FPanel;

/**
 * ����˵��:ȡ���ֶ������Ϣ�Ľӿ�
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public interface IFieldProvider {


	/** The Constant tableName. */
	public static final String tableName = "VW_FB_U_PAYOUT_BUDGET_SH";

	/**
	 * ȡ�ò�ѯ�ĺ�����䣬���ɱ�ͷ�����ɴ�SUM�����.
	 * 
	 * @param dsHeader
	 *            the ds header
	 * @param colBeginIndex
	 *            the col begin index
	 * @param sumFields
	 *            the sum fields
	 * @return the searches the obj
	 * @throws Exception
	 *             the exception
	 */
	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception;

	/**
	 * ������е�����.
	 */
	public void reset();

	/**
	 * ȡ��չʾ�õ����.
	 * 
	 * @return the disp panel
	 */
	public FPanel getDispPanel();

	/**
	 * �Ƿ�����.
	 * 
	 * @return true, if checks if is actived
	 */
	public boolean isActived();

	/**
	 * Checks for.
	 * 
	 * @return the string
	 */
	public String check();

	/**
	 * �����ͷ��SQL.
	 * 
	 * @return the h header sql
	 */
	public String getHHeaderSql();
	
	/**
	 * Gets the h header sql alias.
	 * 
	 * @return the h header sql alias
	 */
	public String getHHeaderSqlAlias();
	
	/**
	 * �����ͷ��SQL.
	 * 
	 * @return the empty h header sql
	 */
	public String getEmptyHHeaderSql();

	/**
	 * ȡ�÷��鲢���ϻ��ܵ����.
	 * 
	 * @param fields
	 *            TODO
	 * @param lstV
	 *            TODO
	 * @param idx
	 *            TODO
	 * @param topCount
	 *            TODO
	 * @param sumFields
	 *            the sum fields
	 * @param tableName
	 *            the table name
	 * @return the sql group
	 */
	public String getSqlGroup(String fields, String sumFields, String tableName, List lstV, int idx, int topCount);
	
	/**
	 * Gets the filter sql.
	 * 
	 * @return the filter sql
	 */
	public String getFilterSql();
	
	/**
	 * Gets the group by sql.
	 * 
	 * @return the group by sql
	 */
	public String getGroupBySql() ;
	
	/**
	 * Gets the order by sql.
	 * 
	 * @return the order by sql
	 */
	public String getOrderBySql();
	
	/**
	 * �Ƿ�ȡ�ý���.
	 * 
	 * @param isFocus
	 *            the value is true if it is focus
	 */
	public void setFocus(boolean isFocus);

	/**
	 * Sets the fouce listener.
	 * 
	 * @param list
	 *            the new fouce listener
	 */
	public void setFouceListener(IFocusChangeListener list);

	/**
	 * Checks if is focused.
	 * 
	 * @return true, if is focused
	 */
	public boolean isFocused();

	/**
	 * Checks if is hor field pro.
	 * 
	 * @return true, if is hor field pro
	 */
	public boolean isHorFieldPro();
	
	/**
	 * Gets the code field c name.
	 * 
	 * @return the code field c name
	 */
	public String getCodeFieldCName();
	
	/**
	 * Gets the code field e name.
	 * 
	 * @return the code field e name
	 */
	public String getCodeFieldEName();
	
	/**
	 * Gets the name field c name.
	 * 
	 * @return the name field c name
	 */
	public String getNameFieldCName();
	
	/**
	 * Gets the name field e name.
	 * 
	 * @return the name field e name
	 */
	public String getNameFieldEName();

	/**
	 * Gets the max header sql.
	 * 
	 * @return the max header sql
	 */
	public String getMaxHeaderSql();

	/**
	 * Gets the indent max header sql.
	 * 
	 * @return the indent max header sql
	 */
	public String getIndentMaxHeaderSql();

	/**
	 * Checks if is group up.
	 * 
	 * @return true, if is group up
	 */
	public boolean isGroupUp();

	/**
	 * Gets the curr lvl length.
	 * 
	 * @return the curr lvl length
	 */
	public int getCurrLvlLength();
	
	/**
	 * Gets the max lvl length.
	 * 
	 * @return the max lvl length
	 */
	public int getMaxLvlLength();

	/**
	 * Gets the total h header sql.
	 * 
	 * @return the total h header sql
	 */
	public String getTotalHHeaderSql();
	
	/**
	 * Gets the ext fields.
	 * 
	 * @return the ext fields
	 */
	public String[][] getExtFields();
	
	/**
	 * Checks if is order by money.
	 * 
	 * @return true, if is order by money
	 */
	public boolean isOrderByMoney();

	/**
	 * Checks for specified order.
	 * 
	 * @return true, if successful
	 */
	public boolean hasSpecifiedOrder();
}
