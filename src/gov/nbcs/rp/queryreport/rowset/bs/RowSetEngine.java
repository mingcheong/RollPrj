/**
 * @# RowSetEngine.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.bs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SessionUtilEx;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.dicinfo.datadict.bs.DataDictBO;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.qrbudget.bs.KitPub;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.rowset.ui.Condition;
import gov.nbcs.rp.queryreport.rowset.ui.RowInfo;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * ����˵��:�б����ѯ���� ��Ҫ�Ļ���:��������ѯһ��������������һ����ѯ��
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class RowSetEngine {
	private List lstRows;

	private XMLData xmlRows;// ����ΪITMEID ֵΪROWINFO

	private XMLData xmlDsRow;// ����Ϊ����Դ,ֵ��LIST list�д���ROWINFO

	private XMLData xmlRowFValue;// �ݴ�ֵ�ĵط�������ΪF+ITEMID;ֵΪROWINFO

	private List lstRowSqls;// ��SQLΪ��ѯ����ֵΪROWINFO

	private int rows = -1;

	private String reportID;

	private String setYear;

	private List depts;

	private List divs;

	private String divWhere = "1=1";// ��λ�Ĺ���

	private GeneralDAO dao;

	private RowSetBO rowSetBO;

	DataDictBO dataDicBO;

	String sVerNo;

	String sBatchNoFilter;

	int iLoginmode;

	/**
	 * ��ʼ��ʱ�ṩ
	 * 
	 * @param rows
	 *            һ����ʾ������
	 * @param reportID
	 *            ����ID
	 * @param depts
	 *            ��������
	 * @param divs
	 *            ��λ����
	 */
	public RowSetEngine(int rows, String reportID, List depts, List divs,
			String sVerNo, String sBatchNoFilter, int iLoginmode, GeneralDAO dao) {
		this.rows = rows;
		this.reportID = reportID;
		this.depts = depts;
		this.divs = divs;
		this.sVerNo = sVerNo;
		this.sBatchNoFilter = sBatchNoFilter;
		this.iLoginmode = iLoginmode;
		this.dao = dao;
		setYear = SessionUtil.getUserInfoContext().getSetYear();
		initData();
	}

	// ��ʼ�����ݣ���XMLData��ΪROWINFO���б�
	private void initData() {
		rowSetBO = new RowSetBO();
		rowSetBO.setDao(this.dao);
		List lstXMLRows = rowSetBO.getReportRows(reportID, setYear);
		List lstXMLCons = rowSetBO.getReportCons(reportID, setYear);

		dataDicBO = new DataDictBO();
		dataDicBO.setDao(dao);
		if (lstXMLRows != null && lstXMLRows.size() > 0) {
			int iCount = lstXMLRows.size();

			// ��ʼ����
			for (int i = 0; i < iCount; i++) {
				RowInfo aRow = new RowInfo((XMLData) lstXMLRows.get(i), null);
				addRow(aRow);
			}
			// ��ʼ������lstCons
			if (lstXMLCons != null && lstXMLCons.size() > 0) {
				iCount = lstXMLCons.size();
				for (int i = 0; i < iCount; i++) {
					Condition aCon = new Condition((XMLData) lstXMLCons.get(i));
					Object obj = xmlRows.get(aCon.getItemID());
					if (obj == null)
						continue;
					((RowInfo) obj).addCons(aCon);
				}

			}
		} else {
			lstRows = new ArrayList();
			xmlRows = new XMLData();
		}
	}

	/**
	 * ���һ��
	 * 
	 * @param aRow
	 */
	private void addRow(RowInfo aRow) {
		if (aRow == null)
			return;
		if (lstRows == null)
			lstRows = new ArrayList();
		if (xmlRows == null)
			xmlRows = new XMLData();
		lstRows.add(aRow);
		xmlRows.put(aRow.getItemID(), aRow);

	}

	/**
	 * ��ѯ����XMLDATA����֯��ʽΪ���У���
	 * 
	 */
	public XMLData doSearch() {
		xmlRowFValue = new XMLData();
		try {// ����
			divWhere = getDivWhereSql(depts, divs, setYear);
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		seperateSearch();// ����
		searchSeperate();
		searchSql();
		makeToCellValue();
		// ����Ҫ��ӵ�Ԫ��ļ���
		return xmlRowFValue;

	}

	private void searchSeperate() {
		if (xmlDsRow == null)
			return;
		Iterator it = xmlDsRow.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List lstRow = (List) xmlDsRow.get(key);
			searchDsGroup(key, lstRow);
		}

	}

	// ��ѯ���
	private void searchSql() {
		if (lstRowSqls == null || lstRowSqls.size() == 0)
			return;

		RowInfo rowInfo;
		String sSql = "";
		for (int i = 0; i < lstRowSqls.size(); i++) {
			rowInfo = (RowInfo) lstRowSqls.get(i);
			// if (Common.isNullStr(rowInfo.getTableName())) {// ���������Դ����Ҫ��Ӳ�ѯ����
			// sSql = rowInfo.getSql();
			// } else {
			// if (rowInfo.getSql().toLowerCase().indexOf("where") == -1)
			// sSql = rowInfo.getSql() + " where 1=1 " + divWhere
			// + getSqlWhere(rowInfo.getSourceID());
			// else
			// sSql = rowInfo.getSql() + divWhere
			// + getSqlWhere(rowInfo.getSourceID());
			// }
			sSql = rowInfo.getSql();
			sSql = replaceAllPamam(sSql);
			List lstData = null;
			try {
				if (rowInfo.getItemID().equals("5")) {
					System.out.println(sSql);
				}
				lstData = dao.findBySql(sSql);
			} catch (Exception e) {
				System.out.println(sSql);
				e.printStackTrace();
			}
			if (lstData == null || lstData.size() == 0)
				continue;
			// ��ȡ�õĵ�һ���еĵ�һ���ֶβ���
			XMLData aData = (XMLData) lstData.get(0);
			Iterator it = aData.values().iterator();
			xmlRowFValue.put("f" + rowInfo.getItemID(), it.next());

		}

	}

	// ����ѯ����,������Դ����
	private void seperateSearch() {
		if (lstRows == null || lstRows.size() == 0)
			return;
		xmlDsRow = new XMLData();
		lstRowSqls = new ArrayList();
		int iCount = lstRows.size();
		String sourceID;
		RowInfo rowInfo;
		for (int i = 0; i < iCount; i++) {
			rowInfo = (RowInfo) lstRows.get(i);
			if (rowInfo.getMeasureType() == RowInfo.MEASURE_SQL) {
				lstRowSqls.add(rowInfo);
				continue;
			}

			sourceID = rowInfo.getSourceID();
			Object obj = xmlDsRow.get(sourceID);
			if (obj == null) {
				List lstDs = new ArrayList();
				lstDs.add(rowInfo);
				xmlDsRow.put(sourceID, lstDs);
				continue;
			}
			((List) obj).add(rowInfo);

		}

	}

	// �Է���Ĳ�ѯ
	// ��ѯ����CASE when ����
	private void searchDsGroup(String sourceID, List lstRowInfo) {
		RowInfo rowInfo = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		for (int i = 0; i < lstRowInfo.size(); i++) {
			rowInfo = (RowInfo) lstRowInfo.get(i);
			sb.append(rowInfo.getSearchSql()).append(",");
		}
		// �������
		sb = sb.deleteCharAt(sb.length() - 1);

		sb.append(getSourceAndWhere(sourceID));
		List lstData = dao.findBySql(sb.toString());
		if (lstData == null || lstData.size() == 0)
			return;
		xmlRowFValue.putAll((XMLData) lstData.get(0));

	}

	private String getSourceAndWhere(String sourceID) {
		XMLData aData = dataDicBO.getTableMainInfo(sourceID, SessionUtil
				.getUserInfoContext().getSetYear());
		StringBuffer sb = new StringBuffer();
		String tableName = getDsName(sourceID, aData);

		// ��ӱ�
		sb.append(" from ").append(tableName);
		sb.append(" where 1=1 ");
		// ����Ƕ���õ���Ҫ��ӱ������
//		if (IAudit.MUTI_TABLE.indexOf(tableName.toLowerCase()) != -1)
//			sb.append(" and report_id='" + sourceID + "'");

		sb.append(divWhere);// ��λ����
		if ("1".equals(Common.getAStringField(aData,// ���ι���
				IDataDictBO.MAIN_FIELD_ISBATCHNO)))
			sb.append(sBatchNoFilter);
		if ("1".equals(Common.getAStringField(aData,// �汾����
				IDataDictBO.MAIN_FIELD_SUP_VER)))
			sb.append(" and  VER_NO=" + sVerNo);
		return sb.toString();
	}

	// ��ʽ�ļ���
	// private void searchFormula() {
	//
	// }

	/**
	 * �õ���ѯ���� // ��������Դ����Ϣȷ����Ҫ�Ĺ�������
	 */
	public String getDivWhereSql(List lstDept, List lstDiv, String setYear)
			throws Exception {
		String sFilter = "";
		// �õ�ҵ���Ҳ�ѯ����
		String sDeptWhere = getDeptFilter(lstDept, setYear);
		// �õ���λ��ѯ����
		String sDivWhere = getDivFilter(lstDiv);

		if ("".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDivWhere + ")";
		} else if (!"".equals(sDeptWhere) && "".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + ")";
		} else if (!"".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + " or" + sDivWhere + ")";
		}

		// �ܲ�ѯ����
		sFilter = sFilter + getUserToDiv(setYear);
		return sFilter;
	}

	/**
	 * �õ�ҵ���Ҳ�ѯ����
	 * 
	 * @param lstDept
	 * @param setYear
	 * @return
	 */
	private String getDeptFilter(List lstDept, String setYear) {
		// ҵ����
		String sDeptWhere = "";
		String sDept;
		for (int i = 0; i < lstDept.size(); i++) {
			sDept = "'" + lstDept.get(i).toString() + "'";
			if ("".equals(sDeptWhere))
				sDeptWhere = sDept;
			else
				sDeptWhere = sDeptWhere + "," + sDept;
		}
		if (lstDept.size() > 0) {
			sDeptWhere = "  div_code in (select div_code from fb_u_deptodiv where dep_id in("
					+ sDeptWhere + ") and set_year = " + setYear + ")";
		}
		return sDeptWhere;
	}

	/**
	 * �õ���λ��ѯ����
	 * 
	 * @param lstDiv
	 * @return
	 */
	private String getDivFilter(List lstDiv) {
		String sDivWhereisLeaf = "";
		String sDivWhereNoLeaf = "";
		IQrBudget.DivObject divObject;
		for (int i = 0; i < lstDiv.size(); i++) {
			divObject = ((IQrBudget.DivObject) lstDiv.get(i));
			if (divObject.isLeaf) { // Ҷ�ڵ�
				if ("".equals(sDivWhereisLeaf)) {
					sDivWhereisLeaf = "'" + divObject.sDivCode + "'";
				} else {
					sDivWhereisLeaf = sDivWhereisLeaf + ",'"
							+ divObject.sDivCode + "'";
				}
			} else { // ����Ҷ�ڵ�
				if ("".equals(sDivWhereNoLeaf)) {
					sDivWhereNoLeaf = " div_code like '" + divObject.sDivCode
							+ "%'";
				} else {
					sDivWhereNoLeaf = sDivWhereNoLeaf + " or div_code like '"
							+ divObject.sDivCode + "%'";
				}
			}
		}
		if (!"".equals(sDivWhereisLeaf)) {
			sDivWhereisLeaf = " div_code in (" + sDivWhereisLeaf + ")";
		}

		String sFilter = "";
		if ("".equals(sDivWhereisLeaf) && !"".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereNoLeaf;
		} else if (!"".equals(sDivWhereisLeaf) && "".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereisLeaf;
		} else if (!"".equals(sDivWhereisLeaf) && !"".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereisLeaf + " or " + sDivWhereNoLeaf;
		}
		return sFilter;
	}

	private String getUserToDiv(String setYear) throws Exception {
		String sFilterWhere = " and div_code in "
				+ KitPub.vw_fb_filterDiv(setYear, "chr_code");
		return sFilterWhere;
	}

	// ��F��ͷ�ļ�ֵת��Ϊ����Ϊ������ʽ
	private void makeToCellValue() {
		XMLData aData = new XMLData();
		Iterator it = xmlRowFValue.keySet().iterator();
		String key;
		while (it.hasNext()) {
			key = (String) it.next();
			// ȡ������
			int i = Integer.parseInt(key.substring(1));
			String newKey = getKeyValue(rows, i);
			aData.put(newKey, xmlRowFValue.get(key));
		}
		xmlRowFValue = aData;
	}

	// iRows һ��Ҫ��ʾ����
	// iCur ��ǰ���У���1��ʼ��
	private String getKeyValue(int iRows, int iCur) {
		iCur = iCur - 1;
		int col = iCur / iRows * 3 + 2;
		int row = iCur % iRows;

		return "" + col + "_" + row;
	}

	private String getDsName(String curDsName, XMLData xmlSource) {
		if (UntPub.FIS_VIS.equals(SessionUtilEx.getBelongType()))// ����
			return (String) xmlSource.get(IDataDictBO.MAIN_FIELD_TABLEENAME);
		else
			return (String) xmlSource
					.get(IDataDictBO.MAIN_FIELD_TABLEENAME_DIV);

	}

	private String replaceAllPamam(String sSql) {
		if (Common.isNullStr(sSql))
			return "";
		sSql = sSql.replaceAll(RowInfo.PARAM_DIV, " 1=1 " + divWhere);
		if (!Common.isNullStr(sBatchNoFilter)) {
			sBatchNoFilter = sBatchNoFilter.toLowerCase();
			String batchNum = "";
			String dataType = "";

			String arrPar[] = sBatchNoFilter.split("and");
			for (int i = 0; i < arrPar.length; i++) {
				String temp = arrPar[i];
				String arrTemp[] = temp.split("=");
				if (arrTemp[0].toString().trim().equals("batch_no"))
					batchNum = arrTemp[1];
				else if (arrTemp[0].toString().trim().equals("data_type")) {
					dataType = arrTemp[1];
				}
			}
			sSql = sSql.replaceAll(RowInfo.PARAM_BATCH, batchNum);
			sSql = sSql.replaceAll(RowInfo.PARAM_DATA_TYPE, dataType);
		}
		sSql = sSql.replaceAll(RowInfo.PARAM_DATA_VER, sVerNo == null ? ""
				: sVerNo);
		sSql = sSql.replaceAll(RowInfo.PARAM_REPORT_ID, reportID == null ? ""
				: reportID);
		sSql = sSql.replaceAll(RowInfo.PARAM_SET_YEAR, SessionUtil
				.getUserInfoContext().getSetYear());
		// �滻��
		Pattern p = Pattern.compile("\\$table-[\\da-zA-z\\_]{1,100}");
		Matcher m = p.matcher(sSql);
		while (m.find()) {
			String tableName = sSql.substring(m.start(), m.end());
			tableName = tableName.substring(7);
			// modify by zlx 2009-06-25 ԭ�򣺻�����Ϣ��ʹ��M��
			// if (UntPub.FIS_VIS.equals(SessionUtilEx.getBelongType()))// ����
			// tableName = "FB_B_" + BasPubAction.GetLastName(tableName);
			// else
			// tableName = "FB_B_M_" + BasPubAction.GetLastName(tableName);
			sSql = sSql.substring(0, m.start() - 1) + " " + tableName + " "
					+ sSql.substring(m.end());
			m = p.matcher(sSql);
		}

		return sSql;
	}
}
