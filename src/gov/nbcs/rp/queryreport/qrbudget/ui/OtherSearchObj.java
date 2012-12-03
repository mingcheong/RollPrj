/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.util.List;

/**
 * @author qzc
 * 
 */
public class OtherSearchObj {

	private List lstTempTable = null;// 表名

	private String sSqlBody; // 表体sql

	private List lstSqlMidView;// 中间视图

	private List lstDeleteRecord;// 删除记录

	private List lstViewName;// 中间视图名称

	public OtherSearchObj() {
	}

	public List getLstSqlMidView() {
		return lstSqlMidView;
	}

	public void setLstSqlMidView(List lstSqlMidView) {
		this.lstSqlMidView = lstSqlMidView;
	}

	public String getSSqlBody() {
		return sSqlBody;
	}

	public void setSSqlBody(String sqlBody) {
		sSqlBody = sqlBody;
	}

	public List getLstDeleteRecord() {
		return lstDeleteRecord;
	}

	public void setLstDeleteRecord(List lstDeleteRecord) {
		this.lstDeleteRecord = lstDeleteRecord;
	}

	public String check() {

		if (lstSqlMidView == null || lstSqlMidView.size() == 0)
			return "没有指定中间视图";
		if (sSqlBody == null || sSqlBody.equals(""))
			return "没有指定查询语句";
		return "";
	}

	public List getLstTempTable() {
		return lstTempTable;
	}

	public void setLstTempTable(List lstTempTable) {
		this.lstTempTable = lstTempTable;
	}

	public List getLstViewName() {
		return lstViewName;
	}

	public void setLstViewName(List lstViewName) {
		this.lstViewName = lstViewName;
	}

}
