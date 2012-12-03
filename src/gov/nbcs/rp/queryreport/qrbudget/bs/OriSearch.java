/**
 * @# OraSearch.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.qrbudget.bs;

import java.util.HashMap;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.dicinfo.datadict.bs.DataDictBO;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.DivObject;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OriSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import gov.nbcs.rp.sys.sysrefcol.ui.SysRefColI;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;


public class OriSearch {
	private ReportInfo reportInfo = null;// ������Ϣ

	private GeneralDAO dao = null;// ��ѯͨ��

	private DataDictBO dataDictBO;// ����Դ����

	public OriSearch(ReportInfo reportInfo, GeneralDAO dao) {
		this.reportInfo = reportInfo;
		this.dao = dao;

		dataDictBO = new DataDictBO();
		dataDictBO.setDao(this.dao);
	}

	/**
	 * �����List�����������֣���һ�����Ǹ�String ����ʱ�����ƣ���ΪCREATE TABLE��� ��ΪLIST
	 * ����INSERT��䣬�����DROP���
	 * 
	 * @return
	 * @throws Exception
	 */
	public OriSearchObj getReportExecuteSql(String sVerNo,
			String sBatchNoFilter, List lstDept, List lstDiv, String setYear,
			int iUserType, int iLoginmode, int iTypeFlag,
			ConditionObj conditionObj) throws Exception {

		QrBudgetBO qrBudgetBO = new QrBudgetBO();
		qrBudgetBO.setDao(dao);

		// ȡ��SQLLINE�е����ݲ���ӵ���ѯ����
		List lstSqlLines = qrBudgetBO.getSqlLines(reportInfo.getReportID(),
				setYear, iLoginmode);

		return getReportExecuteSql_A(sVerNo, sBatchNoFilter, lstDept, lstDiv,
				setYear, iUserType, iLoginmode, iTypeFlag, lstSqlLines,
				conditionObj);
	}

	/**
	 * ������֯lstSqlLines����
	 * 
	 * �����List�����������֣���һ�����Ǹ�String ����ʱ�����ƣ���ΪCREATE TABLE��� ��ΪLIST
	 * ����INSERT��䣬�����DROP���
	 * 
	 * @return
	 * @throws Exception
	 */
	public OriSearchObj getReportExecuteSql_A(String sVerNo,
			String sBatchNoFilter, List lstDept, List lstDiv, String setYear,
			int iUserType, int iLoginmode, int iTypeFlag, List lstSqlLines,
			ConditionObj conditionObj) throws Exception {
		if (reportInfo == null || dao == null)
			return null;
		boolean isNeedChangeTable = iUserType == QrBudget.USER_DW;// �Ƿ�Ҫ�޸�����Դ

		// ��������ʱ������
		String sTempTable = null;
		try {
			sTempTable = OriSearchObj.generateTempTableName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (sTempTable == null)
			return null;

		OriSearchObj oraSearchObj = new OriSearchObj(sTempTable);
		QrBudgetBO qrBudgetBO = new QrBudgetBO();
		qrBudgetBO.setDao(dao);

		if (lstSqlLines == null || lstSqlLines.size() == 0)
			return null;
		// ȡ�õ�λ�Ĺ�������
		String sDivSqlWhere = null;
		try {
			sDivSqlWhere = getDivWhereSql(lstDept, lstDiv, setYear);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		int iCount = lstSqlLines.size();
		HashMap aData = null;
		String sSqlTream;
		for (int i = 0; i < iCount; i++) {// ѭ��ȡ�������
			aData = (HashMap) lstSqlLines.get(i);
			if (aData.get(IBesQryReport.SQLTYPE).equals(
					IDefineReport.CREATE_QUERY)) {
				sSqlTream = ((String) aData.get(IBesQryReport.SQLTREAM))
						.toUpperCase();
				// �������滻�ֶ�����
				if (iLoginmode == 1) {
					sSqlTream = sSqlTream.replaceAll("VARCHAR2", "VARCHAR");
					sSqlTream = sSqlTream.replaceAll("NUMBER", "NUMERIC");
				}

				oraSearchObj.setCreateTableSql(sSqlTream);
			} else if (aData.get(IBesQryReport.SQLTYPE).equals(
					IDefineReport.MID_QUERY)) {
				XMLData xmlSource = null;
				if (aData.get(IBesQryReport.VIEWNAME) != null
						&& !Common.isNullStr(aData.get(IBesQryReport.VIEWNAME)
								.toString())) {
					xmlSource = dataDictBO.getTableMainInfo((String) aData
							.get(IBesQryReport.VIEWNAME), SessionUtil
							.getUserInfoContext().getSetYear());
				}

				String sOther = "";
				if (Common.estimate(reportInfo.getCompareFlag())) {// �Աȷ���������
					// �滻����
					XMLData xmlCompareSource = replaceCompareSource(xmlSource,
							conditionObj, aData);
					if (xmlCompareSource != null)
						xmlSource = xmlCompareSource;
					sOther = getOtherWhereCompare(xmlSource, sVerNo, iTypeFlag,
							setYear, conditionObj, aData);
				} else {
					sOther = getOtherWhere(xmlSource, sVerNo, sBatchNoFilter,
							iTypeFlag, setYear);
				}

				String sInsert = (String) aData.get(IBesQryReport.SQLTREAM);
				sInsert = SysRefColI.getServerMethod().replaceRefColFixFlag(
						sInsert);

				if (isNeedChangeTable
						&& !Common.estimate(reportInfo.getCompareFlag()))
					sInsert = convertDataSource(sInsert, xmlSource);
				// �ж�������Ƿ���DIV_CODE�ֶ�
				if (aData.get(IBesQryReport.VIEWNAME) != null) {
					oraSearchObj.addInsertSql(sInsert + sDivSqlWhere + sOther);
				} else {
					if (sInsert.indexOf(IPubInterface.DIV_CODE) != -1) {
						oraSearchObj.addInsertSql(sInsert + sDivSqlWhere
								+ sOther);
					} else {
						oraSearchObj.addInsertSql(sInsert + sOther);
					}
				}

			} else if (aData.get(IBesQryReport.SQLTYPE).equals(
					IDefineReport.LAST_QUERY)) {
				oraSearchObj.setSearchSql("select * from ("
						+ (String) aData.get(IBesQryReport.SQLTREAM)
						+ ") TMPT order by xh");
			}

		}
		return oraSearchObj;
	}

	/**
	 * �õ���ѯ����
	 */
	private String getDivWhereSql(List lstDept, List lstDiv, String setYear)
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
		DivObject divObject;
		for (int i = 0; i < lstDiv.size(); i++) {
			divObject = ((DivObject) lstDiv.get(i));
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

	/**
	 * �Աȷ��������ѯ����Դ
	 * 
	 * @param xmlSource
	 * @param conditionObj
	 * @param aData
	 * @throws Exception
	 */
	private XMLData replaceCompareSource(XMLData xmlSource,
			ConditionObj conditionObj, HashMap aData) throws Exception {

		if (xmlSource.get(IDataDictBO.YEAR_FLAG) == null) {
			throw new Exception("����Դδ��ʶ�����������!");
		}
		// �ж��Ƿ�����������
		if (ConditionObj.PRE_YEAR.equals(xmlSource.get(IDataDictBO.YEAR_FLAG)
				.toString())) {
			// �ж��Ƿ�Ҫ�ı�����Դ
			if (!ConditionObj.PRE_YEAR.equals(conditionObj.getOneYear())) {
				return replaceSource(xmlSource, conditionObj.getOneDataType(),
						aData);
			} else {
				if ("0".equals(conditionObj.getOneDataType())) {
					String sInsert = (String) aData.get(IBesQryReport.SQLTREAM);
					sInsert = convertDataSource(sInsert, xmlSource);
					aData.put(IBesQryReport.SQLTREAM, sInsert);
				}
			}
		} else if (ConditionObj.CUR_YEAR.equals(xmlSource.get(
				IDataDictBO.YEAR_FLAG).toString())) {

			// �ж��Ƿ�Ҫ�ı�����Դ
			if (!ConditionObj.CUR_YEAR.equals(conditionObj.getTwoYear())) {
				return replaceSource(xmlSource, conditionObj.getTwoDataType(),
						aData);
			} else {
				if ("0".equals(conditionObj.getTwoDataType())) {
					String sInsert = (String) aData.get(IBesQryReport.SQLTREAM);
					sInsert = convertDataSource(sInsert, xmlSource);
					aData.put(IBesQryReport.SQLTREAM, sInsert);
				}
			}
		} else {
			throw new Exception("����Դ��ʶ�޷�ʶ��!");
		}
		return null;
	}

	// �滻����Դ����
	private XMLData replaceSource(XMLData xmlSource, String dataType,
			HashMap aData) throws Exception {

		if (xmlSource.get(IDataDictBO.COMPARE_DICID) == null)
			throw new Exception("����Դδ��ʶ�Ա�����Դ!");
		// ���»������Դ��Ϣ���ݱ�Dcid
		XMLData xmlCompareSource = dataDictBO.getTableMainInfo(
				(String) xmlSource.get(IDataDictBO.COMPARE_DICID), SessionUtil
						.getUserInfoContext().getSetYear());
		if (xmlSource == null)
			throw new Exception("�Ա�����Դ��Ϣ������!");
		// �ж��Ƿ�֧������
		if (Common.estimate(xmlCompareSource.get("isbatchno"))) {
			convertCompareSource(aData, xmlSource, xmlCompareSource, false);
		} else {
			if ("0".equals(dataType)) {
				convertCompareSource(aData, xmlSource, xmlCompareSource, true);
			} else {
				convertCompareSource(aData, xmlSource, xmlCompareSource, false);
			}
		}
		// ��ʶ���
		xmlCompareSource.put(IDataDictBO.YEAR_FLAG, xmlSource
				.get(IDataDictBO.YEAR_FLAG));
		return xmlCompareSource;
	}

	/**
	 * �õ��Աȷ���������Դ����
	 * 
	 * @param xmlSource
	 * @param sVerNo
	 * @param iTypeFlag
	 * @param setYear
	 * @param conditionObj
	 * @param aData
	 * @return
	 * @throws Exception
	 */
	private String getOtherWhereCompare(XMLData xmlSource, String sVerNo,
			int iTypeFlag, String setYear, ConditionObj conditionObj,
			HashMap aData) throws Exception {
		if (xmlSource == null)
			return "";
		StringBuffer sb = new StringBuffer();
		if (Common.estimate(xmlSource.get("sup_ver")))
			sb.append(" and VER_NO=" + sVerNo);

		if (xmlSource.get(IDataDictBO.YEAR_FLAG) == null) {
			throw new Exception("����Դδ������������ʶ!");
		}

		// �ж��Ƿ�֧������
		if (Common.estimate(xmlSource.get("isbatchno"))) {
			if (ConditionObj.PRE_YEAR.equals(xmlSource.get(
					IDataDictBO.YEAR_FLAG).toString())) {
				return " and batch_no=" + conditionObj.getOneBatchNo()
						+ " and data_type =" + conditionObj.getOneDataType();
			} else if (ConditionObj.CUR_YEAR.equals(xmlSource.get(
					IDataDictBO.YEAR_FLAG).toString())) {
				return " and batch_no=" + conditionObj.getTwoBatchNo()
						+ " and data_type =" + conditionObj.getTwoDataType();
			} else {
				throw new Exception("����Դ��ʶ�޷�ʶ��!");
			}
		}

		sb.append(" and set_year=" + setYear);
		return sb.toString();
	}

	// ��������Դ�������ȡ������������
	private String getOtherWhere(XMLData xmlSource, String sVerNo,
			String sBatchNoFilter, int iTypeFlag, String setYear) {
		if (xmlSource == null)
			return "";
		StringBuffer sb = new StringBuffer();
		if (Common.estimate(xmlSource.get("sup_ver")))
			sb.append(" and VER_NO=" + sVerNo);

		if (Common.estimate(xmlSource.get("isbatchno")))
			sb.append(sBatchNoFilter);
		sb.append(" and set_year=" + setYear);

		return sb.toString();

	}

	// ����ѯ�����ݸ�Ϊ��λ��
	private String convertDataSource(String sSql, XMLData xmlDataSource) {
		if (xmlDataSource == null)
			return sSql;
		String divSourceName = (String) xmlDataSource
				.get(IDataDictBO.MAIN_FIELD_TABLEENAME_DIV);
		if (divSourceName == null || divSourceName.equals(""))
			return sSql;
		sSql = sSql.toUpperCase();
		divSourceName = divSourceName.toUpperCase();
		String oriTable = (String) xmlDataSource
				.get(IDataDictBO.MAIN_FIELD_TABLEENAME);

		return sSql.replaceAll(oriTable, divSourceName);
	}

	// ����ѯ�����ݸ�Ϊ�Ա����ݱ���
	private void convertCompareSource(HashMap aData, XMLData xmlDataSource,
			XMLData xmlCompareSource, boolean divTable) throws Exception {
		String sSql = (String) aData.get(IBesQryReport.SQLTREAM);
		if (xmlDataSource == null)
			return;

		sSql = sSql.toUpperCase();

		String compareSourceName;
		if (divTable) {
			compareSourceName = xmlCompareSource.get(
					IDataDictBO.MAIN_FIELD_TABLEENAME_DIV).toString()
					.toUpperCase();
		} else {
			compareSourceName = xmlCompareSource.get(
					IDataDictBO.MAIN_FIELD_TABLEENAME).toString().toUpperCase();
		}

		String oriTable = (String) xmlDataSource
				.get(IDataDictBO.MAIN_FIELD_TABLEENAME);

		aData.put(IBesQryReport.SQLTREAM, sSql.replaceAll(oriTable,
				compareSourceName));
		aData.put(IBesQryReport.VIEWNAME, xmlCompareSource
				.get(IDataDictBO.DICID));
	}
}
