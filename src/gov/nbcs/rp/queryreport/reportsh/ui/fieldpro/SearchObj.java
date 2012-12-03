/**
 * @# SearchObj.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * ����˵��:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class SearchObj {

	private DataSet dsHeader;

	private String sumExp;

	private String fieldExp;
	
	private String showFieldExp;

	private String subTotalExp;

	private String tableName;

	private int lastIndex;
	
	private String subTotalField;

	public DataSet getDsHeader() {
		return dsHeader;
	}

	public void setDsHeader(DataSet dsHeader) {
		this.dsHeader = dsHeader;
	}

	public String getSumExp() {
		return sumExp;
	}

	public void setSumExp(String sumExp) {
		this.sumExp = sumExp;
	}

	public String getFieldExp() {
		return fieldExp;
	}

	public void setFieldExp(String fieldExp) {
		this.fieldExp = fieldExp;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public String getSubTotalExp() {
		return subTotalExp;
	}

	public void setSubTotalExp(String subTotalExp) {
		this.subTotalExp = subTotalExp;
	}

	public void setShowFieldExp(String showFieldExp) {
		this.showFieldExp = showFieldExp;
	}

	public String getShowFieldExp() {
		return showFieldExp;
	}

	public String getSubTotalField() {
		return subTotalField;
	}

	public void setSubTotalField(String subTotalField) {
		this.subTotalField = subTotalField;
	} 

}
