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

	private List lstTempTable = null;// ����

	private String sSqlBody; // ����sql

	private List lstSqlMidView;// �м���ͼ

	private List lstDeleteRecord;// ɾ����¼

	private List lstViewName;// �м���ͼ����

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
			return "û��ָ���м���ͼ";
		if (sSqlBody == null || sSqlBody.equals(""))
			return "û��ָ����ѯ���";
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
