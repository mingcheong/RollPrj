/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.pubinterface.bs;

import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.bs.PrjectManageBO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.SessionUtilEx;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.dictionary.interfaces.IControlDictionary;
import com.foundercy.pf.util.DTO;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.UUIDRandom;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.sessionmanager.SessionUtil;




public class PubInterfaceBO extends PrjectManageBO implements IPubInterface
{
	/**
	 * ��õ�λ��Ϣ���ݼ�
	 */
	public DataSet getDivData(String sYear) throws Exception
	{
		String sSql = " SELECT * FROM VW_rp_DIVISION WHERE set_year = " + sYear;
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	/**
	 * ��Ȩ�޵ĵ�λ���ݼ�
	 */
	public DataSet getDivDataPop(String sYear) throws Exception
	{
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
		String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
		String sSql = " select * from vw_rp_division ENTAB " + " where is_leaf = 1 and set_year=" + sYear + " " + swhere + " union all " + " select * from vw_rp_division tab "
				+ " where tab.is_leaf <> 1 and tab.set_year=" + sYear + " and " + " exists (select 1 from vw_rp_division ENTAB where ENTAB.div_code like tab.div_code||'%'"
				+ " and ENTAB.is_leaf = 1 and ENTAB.set_year=" + sYear + " " + swhere + ")";
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	/**
	 * �����ŵ�λ���ݼ�
	 */
	public DataSet getDepToDivData(String sYear, int levl, boolean isRight) throws Exception
	{
		String sSql = "";
		if (isRight)
		{
			IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
			String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
			sSql = " select distinct dep_id as EN_ID,dep_id as EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_rp_department a "
					+ " where  exists (select 1  from rp_u_deptodiv ENTAB where ENTAB.dep_code = a.Dep_code and a.set_year=ENTAB.set_year " + swhere + " )" + " and a.set_year =" + sYear
					+ " union all "
					+ " select distinct a.EN_ID,dep_ID||a.EN_ID as EID,case when a.Parent_id is null then dep_ID else dep_ID||a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_rp_division a,rp_u_deptodiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code = ENTAB.div_code and a.set_year=" + sYear + " and a.level_num <= "
					+ String.valueOf(levl) + " " + swhere;
		}
		else
		{
			sSql = " select distinct dep_id as EN_ID,dep_id as EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_rp_department a "
					+ " where  exists (select 1  from rp_u_deptodiv ENTAB where ENTAB.dep_code like a.Dep_code||'%' and a.set_year=ENTAB.set_year)" + " and a.set_year =" + sYear + " union all "
					+ " select distinct a.EN_ID,dep_ID||a.EN_ID as EID,case when a.Parent_id is null then dep_ID else dep_ID||a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_rp_division a,rp_u_deptodiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code = ENTAB.div_code and a.set_year=" + sYear;
		}
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	/**
	 * �����ŵ�λ���ݼ�
	 */
	public DataSet getDivDataByMyRight(String sYear, int levl, boolean isRight) throws Exception
	{
		String sSql = "";
		if (isRight)
		{
			IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
			String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
			sSql = " select distinct dep_id as EN_ID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_rp_department a "
					+ " where  exists (select 1  from rp_u_deptodiv ENTAB where ENTAB.dep_code like a.Dep_code||'%' and a.set_year=ENTAB.set_year " + swhere + " )" + " and a.set_year =" + sYear
					+ " union all " + " select distinct a.EN_ID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_rp_division a,rp_u_deptodiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code like ENTAB.div_code||'%' and a.set_year=" + sYear + " and a.level_num <= "
					+ String.valueOf(levl) + " " + swhere;
		}
		else
		{
			sSql = " select distinct dep_id as EN_ID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_rp_department a "
					+ " where  exists (select 1  from rp_u_deptodiv ENTAB where ENTAB.dep_code like a.Dep_code||'%' and a.set_year=ENTAB.set_year)" + " and a.set_year =" + sYear + " union all "
					+ " select distinct a.EN_ID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_rp_division a,rp_u_deptodiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code like ENTAB.div_code||'%' and a.set_year=" + sYear;
		}
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	/**
	 * ��þ��ÿ�Ŀ�����ݼ�
	 */
	public DataSet getAcctEconomy(String filter) throws Exception
	{
		String sSQL = "select * from VW_RP_ACCT_ECONOMY " + (Common.isNullStr(filter) ? "" : " WHERE " + filter);
		DataSet dsAcct = DataSet.create();
		DBSqlExec.getDataSet(sSQL, dsAcct);
		return dsAcct;
	}


	/**
	 * ��ù��ܿ�Ŀ�����ݼ�
	 */
	public DataSet getAcctFunc(String filter) throws Exception
	{
		String sSQL = "select * from VW_RP_ACCT_FUNC " + (Common.isNullStr(filter) ? "" : " WHERE " + filter);
		DataSet dsAcct = DataSet.create();
		DBSqlExec.getDataSet(sSQL, dsAcct);
		return dsAcct;
	}


	/**
	 * ��ñ������
	 */
	public SysCodeRule getCodeRule(int nYear, String ruleType) throws Exception
	{
		SysCodeRule codeRule = new SysCodeRule(nYear, ruleType.toUpperCase());
		return codeRule;
	}


	/**
	 * �������ݱ��滻�ַ��� sValue : �����ַ���������{001}+{002}�� intType �滻���� 0 ��ʾֻ�滻{}�е����ݣ�1
	 * ��ʾȫ���ỻ�� aTable ������nowField �������ݶ�Ӧ���ֶ� endField �����滻��Ӧ���ֶ� aFilter ����
	 */
	public String replaceTextEx(String sValue, int intType, String aTable, String nowField, String endField, String aFilter) throws Exception
	{
		String sArrayValue[] = Common.splitString(sValue);
		String sReturn = sValue;
		String sTemp = "";
		for (int i = 0; i < sArrayValue.length; i++)
		{
			sTemp = DBSqlExec.getStringValue("SELECT " + endField + " FROM " + aTable + " WHERE " + nowField + " = '" + sArrayValue[i] + (Common.isNullStr(aFilter) ? "'" : "' and " + aFilter));
			if (intType == 0)
			{
				sReturn = StringUtils.replace(sReturn, "{" + sArrayValue[i] + "}", "{" + (Common.isNullStr(sTemp) ? "0" : sTemp) + "}");
			}
			if (intType == 1)
			{
				sReturn = StringUtils.replace(sReturn, "{" + sArrayValue[i] + "}", (Common.isNullStr(sTemp) ? "0" : sTemp));
			}
		}
		return sReturn;
	}


	/**
	 * �������ݼ��滻�ַ��� sValue : �����ַ���������{001}+{002}�� intType �滻���� 0 ��ʾֻ�滻{}�е����ݣ�1
	 * ��ʾȫ���ỻ�� aTable ������nowField �������ݶ�Ӧ���ֶ� endField �����滻��Ӧ���ֶ� aFilter ����
	 */
	public String replaceTextExDs(String sValue, int intType, DataSet dsTable, String nowField, String endField) throws Exception
	{
		String sArrayValue[] = Common.splitString(sValue);
		String sReturn = sValue;
		String sTemp = "";
		for (int i = 0; i < sArrayValue.length; i++)
		{
			if (dsTable.locate(nowField, sArrayValue[i]))
			{
				sTemp = dsTable.fieldByName(endField).getString();
			}
			else
			{
				sTemp = "";
			}
			if (intType == 0)
			{
				sReturn = StringUtils.replace(sReturn, "{" + sArrayValue[i] + "}", "{" + (Common.isNullStr(sTemp) ? "0" : sTemp) + "}");
			}
			if (intType == 1)
			{
				sReturn = StringUtils.replace(sReturn, "{" + sArrayValue[i] + "}", (Common.isNullStr(sTemp) ? "0" : sTemp));
			}
		}
		return sReturn;
	}


	/**
	 * ���ã����ѷ���������ݼ��в��ҳ����к��ʵĶ�Ӧ�ֶΣ���ϵͳ�еĶ����������ֶζ�����F1��F2��F3����ʽ���ڵģ�
	 * ֱ���û�Ϊ�����ñ���ڸ��е�ʵ�ʺ��壬��Щ�ֶβű����裬�����û�����ɾ���Զ��������Ϣ������F[X]�ֻᱻ���ã�
	 * ������������ָ�����ݼ��б�����ֱ���ҵ���һ�����õ��ֶ�Ϊֹ�� ���� �� sFieldName���ֶ�����c
	 * sFieldPrefix:�ֶ�ǰ׺������F,C��N) iMaxFieldNum:�ֶ�����(��FB_B_PUBCODE���ñ��ж���Float�ֶ�)
	 * sTableName:������ nYear:��½��� sEname:���ز��ҳ����ֶ����� sErrorMessage���ش�����Ϣ
	 * ����ֵΪboolean�ͣ�����ֵ���Ϊtrue��ִ�гɹ��� false:ʧ��
	 */
	public String assignNewCol(String sFieldName, String sFieldPrefix, int iMaxFieldNum, String sTableName, String nYear, String sFilter) throws Exception
	{
		boolean bResult = false;
		String sFieldTemp = "";
		int iNum = 0;
		// ���ݱ������ֶ�������ݣ���ѯ��ǰʹ�õ��ֶΣ�ת��������
		String sSql = "SELECT " + sFieldName + ",cast(substr(" + sFieldName + ",2,length(" + sFieldName + ")-1) as int) as dd  FROM " + sTableName + " where SEt_Year = " + nYear + " and "
				+ sFieldName + " is not null " + (Common.isNullStr(sFilter) ? "" : " and " + sFilter) + " ORDER BY dd";
		DataSet dsTemp = DataSet.create();
		if (DBSqlExec.getDataSet(sSql, dsTemp))
		{
			if (dsTemp.isEmpty())
			{
				return sFieldPrefix + '1';
			}
			else
			{
				iNum = 1;
				while (!dsTemp.eof())
				{
					if ((iNum != dsTemp.fieldByName("dd").getInteger()) && (iNum <= iMaxFieldNum))
					{
						bResult = true;
						sFieldTemp = sFieldPrefix + String.valueOf(iNum);
						break;
					}
					else
					{
						iNum++;
						dsTemp.next();
					}
				}
				if (!bResult && (iNum <= iMaxFieldNum))
				{
					sFieldTemp = sFieldPrefix + String.valueOf(iNum);
				}
				else
				{
					if (iNum > iMaxFieldNum)
					{
						sFieldTemp = "";
					}
				}
			}
		}
		else
		{
			sFieldTemp = "";
		}
		return sFieldTemp;
	}


	/**
	 * �������ܣ����ݹ����ڶ�Ӧ�����ݱ��л�ȡ�µĽڵ�code����˵���� aTableName:���� aFieldName:ȡֵ�ֶ�
	 * aParID:���ڵ�ID aFilter:�������� aRule:�ڵ�����δ������
	 */
	public String getNodeID(String aTableName, String aFieldName, String aParID, String aFilter, SysCodeRule codeRule) throws Exception
	{
		String MaxCode = null, endCode = null; // ���������� ��003001����С���� 001
		int subLevelLen = 0, nCode = 0;
		aParID = Common.nonNullStr(aParID);
		subLevelLen = codeRule.nextLevelLength(aParID); // ���Ҫ�����¼��ĳ�;
		// �縸�գ��򷵻ص�һ���ĳ��ȣ��粻�����¼�����-1
		if (subLevelLen >= 0)
		{
			MaxCode = DBSqlExec.getMaxValueFromField(aTableName, aFieldName, aFilter);
			if ("0".equals(MaxCode))
			{
				endCode = Common.getStrID(new BigDecimal(1), subLevelLen - aParID.length());
			}
			else
			{
				endCode = codeRule.concurrent(MaxCode);
				nCode = Integer.parseInt(endCode) + 1;
				endCode = Common.getStrID(new BigDecimal(nCode), endCode.length());
			}
			return Common.nonNullStr(aParID) + endCode;
		}
		else
		{
			return null;
		}
	}


	/**
	 * �������ܣ����ݱ��볤�����ɱ�����󳤶ȵ�code ����˵���� aTableName:���� aFieldName:�ֶ� aFilter:��������
	 * intL:�ڵ�����δ������ 0 �����������ַ���
	 */
	public String getMaxCode(String aTableName, String aFieldName, String aFilter, int intL) throws Exception
	{
		String maxCode = DBSqlExec.getMaxValueFromField(aTableName, aFieldName, aFilter);
		return Common.getStrID(new BigDecimal(maxCode).add(new BigDecimal(1)), intL);
	}


	/**
	 * ��ȡ��ĳ���ֶ����ֵ
	 * 
	 * @param tableName
	 *            ����
	 * @param fieldName
	 *            �ֶ���
	 */
	public int getMaxValue(String tableName, String fieldName)
	{
		int maxId = 1;
		// ��ȡ���ֵ
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" SELECT CASE " + " WHEN MAX(" + fieldName + ") IS NULL ");
		strSQL.append(" then 1 ELSE MAX(" + fieldName + ")+1");
		strSQL.append(" END " + fieldName + " FROM " + tableName);
		try
		{
			List lstData = null;
			try
			{
				lstData = QueryStub.getQueryTool().executeQuery(strSQL.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if ((lstData != null) && (lstData.size() > 0))
			{
				MyMap data = (MyMap) lstData.get(0);
				maxId = Integer.parseInt(data.get(fieldName).toString());
			}
		}
		catch (Exception ew)
		{
			ew.printStackTrace();
		}
		finally
		{}
		return maxId;
	}


	/**
	 * ��ȡ��ĳ���ֶ���Сֵ
	 * 
	 * @param tableName
	 *            ����
	 * @param fieldName
	 *            �ֶ���
	 */
	public int getMinValue(String tableName, String fieldName)
	{
		int maxId = 1;
		// ��ȡ���ֵ
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" SELECT CASE " + " WHEN MIN(" + fieldName + ") IS NULL ");
		strSQL.append(" then 1 ELSE MIN(" + fieldName + ")-1");
		strSQL.append(" END " + fieldName + " FROM " + tableName);
		try
		{
			List lstData = null;
			try
			{
				lstData = QueryStub.getQueryTool().executeQuery(strSQL.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if ((lstData != null) && (lstData.size() > 0))
			{
				MyMap data = (MyMap) lstData.get(0);
				maxId = Integer.parseInt(data.get(fieldName).toString());
			}
		}
		catch (Exception ew)
		{
			ew.printStackTrace();
		}
		finally
		{}
		return maxId;
	}


	/**
	 * ���ݴ�����û�ID,��ȡ��Ӧ���û�������������
	 * 
	 * @param user_id
	 * @return
	 */
	public String getUserBelongType(String user_id) throws Exception
	{
		String result = "";
		String sql = "select belong_type from sys_usermanage" + Tools.addDbLink() + " where user_id=?";
		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		ds.setQueryParams(new Object[] { user_id == null ? "" : user_id });
		ds.open();
		if ((ds == null) || ds.isEmpty()) { return null; }
		ds.beforeFirst();
		ds.next();
		result = ds.fieldByName("belong_type").getString();
		return result;
	}


	public String getBranchCode(String userBeLong) throws Exception
	{
		String sql = " SELECT dep_code FROM vw_fb_department WHERE dep_id=? AND set_year=?";
		return DBSqlExec.getStringValue(sql, new Object[] { userBeLong, SessionUtil.getUserInfoContext().getSetYear() });
	}


	public String getEnDivCode(String en_id) throws Exception
	{
		String sql = " SELECT div_code FROM vw_rp_division WHERE en_id=? AND set_year=?";
		return DBSqlExec.getStringValue(sql, new Object[] { en_id, SessionUtil.getUserInfoContext().getSetYear() });
	}


	public int getEnDivLevelNum(String en_id) throws Exception
	{
		String sql = " SELECT level_num FROM vw_rp_division WHERE en_id=? AND set_year=?";
		return DBSqlExec.getIntValue(sql, new Object[] { en_id, SessionUtil.getUserInfoContext().getSetYear() });
	}


	// public InfoPackage getAuditCanEdit(String aPrjCode, String aUserCode,
	// String depID) throws Exception {
	// InfoPackage info = new InfoPackage();
	// info.setSuccess(true);
	// info.setsMessage("���и���Ŀ�������Ŀ");
	// DataSet dsStep1 = null;
	// DataSet dsCurState = getAuditCurStateDataSet(aPrjCode);
	// String stepID = "0";
	// String isEnd = "0";
	// String isUser = "1";
	// if ((dsCurState != null) && !dsCurState.isEmpty()) {
	// dsCurState.beforeFirst();
	// dsCurState.next();
	// stepID = dsCurState
	// .fieldByName(IPrjAudit.PrjAuditTable.audit_state)
	// .getString();
	// isEnd = dsCurState.fieldByName(IPrjAudit.PrjAuditTable.AUDIT_ISEND)
	// .getString();
	// if (!"0".equals(stepID)) {
	// dsStep1 =
	// getAuditStepDataSet(IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_ID
	// + "=" + stepID);
	// } else {
	// dsStep1 = getAuditStepDataSet("");
	// }
	// isUser = dsStep1.fieldByName(
	// IPrjAudit.PrjAuditTable.PrjAuditSetTable.IS_USER)
	// .getString();
	// } else {
	//
	// }
	// boolean auditIsEnd = "1".equals(isEnd);
	// int ms = getMaxStep();
	// if (SessionUtilEx.isFisVis()) {
	// // ����ǲ����û�
	// String depCode = getDepCode(depID);
	// if ("0".equals(isUser)) {
	// dsStep1 =
	// getAuditStepDataSet(IPrjAudit.PrjAuditTable.PrjAuditSetTable.AUDIT_OPER
	// + " like '%" + Common.nonNullStr(depCode) + "%'");
	// } else {
	// dsStep1 =
	// getAuditStepDataSet(IPrjAudit.PrjAuditTable.PrjAuditSetTable.AUDIT_OPER
	// + " like '%" + Common.nonNullStr(aUserCode) + "%'");
	// }
	// if (!((dsStep1 != null) && !dsStep1.isEmpty())) {
	// info.setSuccess(false);
	// info.setsMessage("��û�����Ȩ��");
	// return info;
	// }
	// if (auditIsEnd) {
	// // ��ǰ����Ŀ�����ɻ����ڵ�λ��
	// stepID = String.valueOf(Integer.parseInt(stepID) + 1);
	// }
	// if (Integer.parseInt(stepID) > ms) {
	// stepID = String.valueOf(ms);
	// }
	// // ��������һ�����
	// if (dsStep1.locate(
	// IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_ID, stepID)) {
	// String auditOper = dsStep1.fieldByName(
	// IPrjAudit.PrjAuditTable.PrjAuditSetTable.AUDIT_OPER)
	// .getString();
	// if ("1".equals(isUser)) {
	// // ������Ԫ�����û�
	// if (auditOper.indexOf(aUserCode) >= 0) {
	// return info;
	// } else {
	// info.setSuccess(false);
	// info.setsMessage("��û�����Ȩ��");
	// return info;
	// }
	// } else {
	// // ������Ԫ����ҵ����
	// if (auditOper.indexOf(depCode) >= 0) {
	// return info;
	// } else {
	// info.setSuccess(false);
	// info.setsMessage("��û�����Ȩ��");
	// return info;
	// }
	// }
	// } else {
	// info.setSuccess(false);
	// info.setsMessage("��Ŀ��˲��������̱���û���ҵ���ص�������Ϣ");
	// return info;
	// }
	//
	// } else {
	// // ����ǵ�λ�û�
	// if ((dsCurState != null) && !dsCurState.isEmpty()) {
	// // �����ǰ��Ŀ�����������
	// if ("0".equals(stepID) && !auditIsEnd) {
	// // ����ڵ�λ
	// info.setSuccess(true);
	// return info;
	// } else {
	// // ������ڵ�λ
	// // ��˵������һ��λ�����������ɣ�Ҳ�������
	// if ((Integer.parseInt(stepID) == ms) && auditIsEnd) {
	// info.setSuccess(true);
	// return info;
	// } else {
	// info.setSuccess(false);
	// info.setsMessage("��û�б༭�����Ȩ��");
	// return info;
	// }
	// }
	// } else {
	// info.setSuccess(true);
	// return info;
	// }
	// }
	// }
	public InfoPackage getAuditCanEdit(String aPrjCode, String aUserCode, String depID, String moduleId) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		info.setsMessage("���и���Ŀ�������Ŀ");

		String msg = "";
		String billId = "";
		try
		{
			// for (int i = 0; i < xmxhs.size(); i++) {
			// String xmxh = xmxhs.get(i).toString();
			billId = getbill(aPrjCode, "RP_SB_BILL");
			goFlow(null, "RP_SB_BILL", "BILL_ID", billId, "TONEXT", moduleId);

		}
		catch (Exception ex)
		{
			msg = "��˳���";
			ex.printStackTrace();
		}

		return info;

	}


	/**
	 * ��λ������Ϣ
	 * 
	 * @param prjCode
	 *            ��Ŀ����
	 * @return
	 * @throws Exception
	 */
	public InfoPackage BackAuditInfoByDiv(int type, String[] prjCodes, String aUserCode, String rgCode, String moduleid) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		String flowstate = "";
		if (type == 0)
			flowstate = "TONEXT";
		else if (type == 1)
			flowstate = "TORECALLNEXT";
		else if (type == 2)
			flowstate = "TOPREVIOUS";
		else if (type == 3)
			flowstate = "TORECALLPREVIOUS";

		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < prjCodes.length; i++)
			{
				String xmxh = prjCodes[i].toString();
				billId = getbill(xmxh, "RP_TB_BILL");
				goFlow(null, "RP_TB_BILL", "XMXH", billId, flowstate, moduleid);
			}
		}
		catch (Exception ex)
		{
			msg = "��˳���";
			ex.printStackTrace();
		}
		info.setsMessage("�����ɹ�  " + prjCodes.length + "   ����Ŀ");
		return info;
	}


	public InfoPackage BackAuditTZInfoByDiv(int type, String[] prjCodes, String aUserCode, String rgCode, String moduleid) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		String flowstate = "";
		if (type == 0)
			flowstate = "TONEXT";
		else if (type == 1)
			flowstate = "TORECALLNEXT";
		else if (type == 2)
			flowstate = "TOPREVIOUS";
		else if (type == 3)
			flowstate = "TORECALLPREVIOUS";

		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < prjCodes.length; i++)
			{
				String xmxh = prjCodes[i].toString();
				billId = getbill(xmxh, "RP_TZ_BILL");
				goFlow(null, "RP_TB_BILL", "BILL_ID", billId, flowstate, moduleid);
			}
		}
		catch (Exception ex)
		{
			msg = "��˳���";
			ex.printStackTrace();
		}
		info.setsMessage("�����ɹ�  " + prjCodes.length + "   ����Ŀ");
		return info;
	}


	public InfoPackage startflowInfoByDiv(List xmxhs, String aUserCode, String rgCode, String moduleid) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		List list = new ArrayList();
		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < xmxhs.size(); i++)
			{
				String xmxh = Common.nonNullStr(((Map) xmxhs.get(i)).get("xmxh"));
				if (!isworkflow(xmxh, "RP_TB_BILL"))
				{
					billId = UUIDRandom.generate();
					intoBill(Common.nonNullStr(((Map) xmxhs.get(i)).get("enid")), billId, xmxh, "RP_TB_BILL");
					inputFlow("RP_TB_BILL", "XMXH", billId, moduleid);
				}
			}
		}
		catch (Exception ex)
		{
			msg = "��˳���";
			ex.printStackTrace();
		}

		info.setsMessage("����ɹ�  " + list.size() + "   ����Ŀ");
		QueryStub.getQueryTool().executeBatch(list);
		return info;
	}


	public InfoPackage sendAuditInfoByDiv(List xmxhs, String aUserCode, String rgCode, String moduleid) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		List list = new ArrayList();
		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < xmxhs.size(); i++)
			{
				String xmxh = Common.nonNullStr(((Map) xmxhs.get(i)).get("xmxh"));
				if (!isworkflow(xmxh, "RP_TB_BILL"))
				{
					billId = UUIDRandom.generate();
					intoBill(Common.nonNullStr(((Map) xmxhs.get(i)).get("enid")), billId, xmxh, "RP_TB_BILL");

					inputFlow("RP_TB_BILL", "XMXH", billId, moduleid);

					savehistory(xmxh);

					goFlow(null, "RP_TB_BILL", "BILL_ID", billId, "TONEXT", moduleid);
				}
				else
				{
					savehistory(xmxh);
					billId = getbill(xmxh, "RP_TB_BILL");
					goFlow(null, "RP_TB_BILL", "XMXH", billId, "TONEXT", moduleid);
				}
			}

		}
		catch (Exception ex)
		{
			msg = "��˳���";
			ex.printStackTrace();
		}

		info.setsMessage("����ɹ�  " + list.size() + "   ����Ŀ");
		// QueryStub.getQueryTool().executeBatch(list);
		return info;
	}


	public InfoPackage sendTZAuditInfoByDiv(List xmxhs, String aUserCode, String rgCode, String moduleid) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		List list = new ArrayList();
		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < xmxhs.size(); i++)
			{
				String xmxh = Common.nonNullStr(((Map) xmxhs.get(i)).get("xmxh"));
				if (!isworkflow(xmxh, "RP_TZ_BILL"))
				{
					billId = UUIDRandom.generate();
					intoBill(Common.nonNullStr(((Map) xmxhs.get(i)).get("enid")), billId, xmxh, "RP_TZ_BILL");
					inputFlow("RP_TZ_BILL", "XMXH", billId, moduleid);
					goFlow(null, "RP_TZ_BILL", "BILL_ID", billId, "TONEXT", moduleid);
				}
				else
				{
					billId = getbill(xmxh, "RP_TZ_BILL");
					goFlow(null, "RP_TZ_BILL", "XMXH", billId, "TONEXT", moduleid);
				}
			}

		}
		catch (Exception ex)
		{
			msg = "��˳���";
			ex.printStackTrace();
		}

		info.setsMessage("����ɹ�  " + list.size() + "   ����Ŀ");
		QueryStub.getQueryTool().executeBatch(list);
		return info;
	}


	// public InfoPackage sendAjustInfoByDiv(String[] prjCodes, String
	// aUserCode,String rgCode,String moduleid )
	// throws Exception {
	// InfoPackage info = new InfoPackage();
	// info.setSuccess(true);
	// List list = new ArrayList();
	// String msg = "";
	// String billId="";
	// try {
	// for (int i = 0; i < prjCodes.length; i++) {
	// String xmxh = prjCodes[i].toString();
	// if(!isworkflow(xmxh,"RP_TB_BILL"))
	// {
	// billId=UUID.randomUUID().toString();
	// intoBill(billId,xmxh,"RP_TB_BILL");
	// inputFlow("RP_TB_BILL", "XMXH", xmxh, moduleid);
	// goFlow(null, "RP_TB_BILL", "XMXH", xmxh, "TONEXT",moduleid);
	// }
	// else{
	// goFlow(null, "RP_TB_BILL", "XMXH", xmxh, "TONEXT",moduleid);
	// }
	// }
	//				
	// } catch (Exception ex) {
	// msg = "��˳���";
	// ex.printStackTrace();
	// }
	//			
	// info.setsMessage("����ɹ� " + list.size() + " ����Ŀ");
	// QueryStub.getQueryTool().executeBatch(list);
	// return info;
	// }

	public boolean isworkflow(String xmxh, String tablename)
	{
		String sql = "SELECT 1 FROM  " + tablename + " where xmxh='" + xmxh + "'";
		if (dao.findBySql(sql).size() > 0)
			return true;
		else
			return false;
	}


	public String getbill(String xmxh, String tablename)
	{
		String bill = "select bill_id from " + tablename + " where xmxh='" + xmxh + "' and set_year=" + SessionUtil.getLoginYear();

		List list = dao.findBySql(bill);

		return ((Map) (list.get(0))).get("bill_id").toString();
	}


	public int intoBill(String enID, String bill_id, String xmxh, String tablename)
	{

		dao.executeBySql("delete from " + tablename + " where xmxh ='" + xmxh + "'");
		String sql = "insert into " + tablename + "(en_id,bill_id,xmxh,set_year)values('" + enID + "','" + bill_id + "','" + xmxh + "','" + SessionUtil.getLoginYear() + "')";
		return dao.executeBySql(sql);
	}


	// public InfoPackage sendAuditInfoByDiv(String[] prjCodes, String
	// aUserCode,String rgCode)
	// throws Exception {
	//
	// //int audid = DBSqlExec
	// //.getIntValue("SELECT N_VALUE FROM RP_S_CONFIG WHERE SUPTYPE =
	// 'CUR_STATE'");
	//		
	// InfoPackage info = new InfoPackage();
	// info.setSuccess(true);
	// List list = new ArrayList();
	// for (int i = 0; i < prjCodes.length; i++) {
	// int batchNo = DBSqlExec
	// .getIntValue("select nvl(max(batch_no),0) from RP_PRJBATCH where
	// set_year="+SessionUtilEx.getLoginYear()+" and prj_code = '"+prjCodes[i]
	// +"' ");
	//			
	// int batchNohis = DBSqlExec
	// .getIntValue("select nvl(max(batch_no),-1) from rp_audit_his t where
	// t.set_year="+SessionUtilEx.getLoginYear()+" and t.prj_code='"+
	// prjCodes[i] +"'");
	//			
	// String filter = IPrjAudit.PrjAuditTable.prj_code + "='"
	// + prjCodes[i] + "' and set_year = "
	// + SessionUtilEx.getLoginYear() + " and " + "("
	// + IPrjAudit.PrjAuditTable.audit_state + ">0" + " or "
	// + IPrjAudit.PrjAuditTable.AUDIT_ISEND + "=1)";
	// DataSet ds = DBSqlExec.getDataSet("select * From "
	// + IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR + " where "
	// + filter);
	// String xmxh = DBSqlExec.getStringValue("select xmxh From rp_xmjl where
	// set_year="+SessionUtilEx.getLoginYear()+" and xmbm ='"+prjCodes[i]+"'");
	//			
	// if (ds.getRecordCount() > 0) {
	// //
	// String stepID =
	// ds.fieldByName(IPrjAudit.PrjAuditTable.audit_state).getString();
	// String isEnd = IPrjAudit.PrjAuditTable.AUDIT_ISEND;
	// // if ((Integer.parseInt(stepID) == getMaxStep())
	// // && "1".equals(isEnd)) {
	// // batchNo++;
	// addsendSql(list, prjCodes[i], aUserCode,xmxh,
	// batchNo,rgCode,true,batchNohis);
	// // } else {
	// // continue;
	// // }
	// } else {
	// addsendSql(list, prjCodes[i], aUserCode,xmxh,
	// batchNo,rgCode,true,batchNohis);
	// }
	// }
	// info.setsMessage("����ɹ� " + list.size() + " ����Ŀ");
	// QueryStub.getQueryTool().executeBatch(list);
	// return info;
	// }
	//	

	private List addListSendDivAuditSql(List list, String prjCode, String aUserCode, int batchNo, String rgCode)
	{
		String filter1 = IPrjAudit.PrjAuditTable.prj_code + "='" + prjCode + "' and set_year = " + SessionUtilEx.getLoginYear();
		StringBuffer sqlD = new StringBuffer();
		sqlD.append("delete from ");
		sqlD.append(IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
		sqlD.append(" where " + filter1);
		StringBuffer sqlI = new StringBuffer();
		sqlI.append("insert into " + IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
		sqlI.append("(ROW_ID, DIV_CODE, DIV_NAME, PRJ_CODE,");
		sqlI.append("PRJ_NAME, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,");
		sqlI.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,AUDIT_ISEND,RG_CODE)");
		sqlI.append("select newid,div_code,div_name,xmbm,xmmc,");
		sqlI.append("'" + aUserCode + "'," + "(select user_name from sys_usermanage where user_code = '" + aUserCode + "')" + ",0,0,");
		sqlI.append("'" + Tools.getCurrDate() + "',");
		sqlI.append(SessionUtilEx.getLoginYear() + ",en_id,1,'" + rgCode + "'");
		sqlI.append(" from rp_xmjl where xmbm = '" + prjCode + "' and set_year = " + SessionUtilEx.getLoginYear());
		StringBuffer sqlIH = new StringBuffer();
		sqlIH.append("insert into " + IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_HIS);
		sqlIH.append("(ROW_ID, DIV_CODE, DIV_NAME, prj_code,");
		sqlIH.append("prj_name, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,");
		sqlIH.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,AUDIT_ISEND,BATCH_NO,DATA_TYPE,RG_CODE)");
		sqlIH.append("select newid,div_code,div_name,xmbm,xmmc,");
		sqlIH.append("'" + aUserCode + "'," + "(select user_name from sys_usermanage where user_code ='" + aUserCode + "')" + ",0,0,");
		sqlIH.append("'" + Tools.getCurrDate() + "',");
		sqlIH.append(SessionUtilEx.getLoginYear() + ",en_id,1,'" + rgCode + "',");
		sqlIH.append(batchNo + ",0");
		sqlIH.append(" from rp_xmjl where xmbm ='" + prjCode + "' and set_year = " + SessionUtilEx.getLoginYear());
		list.add(sqlD.toString());
		list.add(sqlI.toString());
		list.add(sqlIH.toString());
		return list;
	}


	private List addsendSql(List list, String prjCode, String aUserCode, String xmxh, int batchNo, String rgCode, boolean type, int batchNohis)
	{

		int AUDIT_STATE = 0;
		try
		{
			AUDIT_STATE = DBSqlExec.getIntValue("SELECT audit_state FROM rp_audit_cur WHERE   set_year='" + SessionUtilEx.getLoginYear() + "' and prj_code = '" + prjCode + "'");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String filter1 = IPrjAudit.PrjAuditTable.prj_code + "='" + prjCode + "' and set_year = " + SessionUtilEx.getLoginYear();
		batchNohis = batchNohis + 1;
		String sqljl_his_del = "delete  from rp_xmjl_history where step_id=0 and batch_no=" + batchNo + " and his_no=0 and xmxh='" + xmxh + "'";
		String sqlsb_his_del = "delete from rp_xmsb_history  where step_id=0 and batch_no=" + batchNo + " and his_no=0 and xmxh='" + xmxh + "'";
		String sqljl_his = "insert into rp_xmjl_history (select a.*,0," + batchNo + ",0 from rp_xmjl a where  a.xmxh='" + xmxh + "')";
		String sqlsb_his = "insert into rp_xmsb_history(select a.*,0," + batchNo + ",0 from rp_xmsb  a where a.xmxh ='" + xmxh + "')";

		StringBuffer sqlD = new StringBuffer();

		sqlD.append("delete from ");
		sqlD.append(IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
		sqlD.append(" where " + filter1);
		StringBuffer sqlI = new StringBuffer();
		sqlI.append("insert into " + IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
		sqlI.append("(ROW_ID, DIV_CODE, DIV_NAME, PRJ_CODE,");
		sqlI.append("PRJ_NAME, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,");
		sqlI.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,AUDIT_ISEND,RG_CODE)");
		sqlI.append("select newid,div_code,div_name,xmbm,xmmc,");
		sqlI.append("'" + aUserCode + "'," + "(select user_name from sys_usermanage where user_code = '" + aUserCode + "')");
		if (type)
		{
			sqlI.append("," + (AUDIT_STATE + 1) + ",0,");
		}
		else
		{
			sqlI.append("," + (AUDIT_STATE - 1) + ",0,");
		}
		sqlI.append("'" + Tools.getCurrDate() + "',");
		sqlI.append(SessionUtilEx.getLoginYear() + ",en_id,");

		sqlI.append("0");

		sqlI.append(",'" + rgCode + "'");
		sqlI.append(" from rp_xmjl where xmbm = '" + prjCode + "' and set_year = " + SessionUtilEx.getLoginYear());
		StringBuffer sqlIH = new StringBuffer();
		sqlIH.append("insert into " + IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_HIS);
		sqlIH.append("(ROW_ID, DIV_CODE, DIV_NAME, prj_code,");
		sqlIH.append("prj_name, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,");
		sqlIH.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,AUDIT_ISEND,RG_CODE,BATCH_NO,DATA_TYPE)");
		sqlIH.append("select newid,div_code,div_name,xmbm,xmmc,");
		sqlIH.append("'" + aUserCode + "'," + "(select user_name from sys_usermanage where user_code ='" + aUserCode + "')" + ",0,0,");
		sqlIH.append("'" + Tools.getCurrDate() + "',");
		sqlIH.append(SessionUtilEx.getLoginYear() + ",en_id,1,'" + rgCode + "',");
		sqlIH.append(batchNo);
		if (type)
		{
			sqlIH.append(",1");
		}
		else
		{
			sqlIH.append(",0");
		}

		sqlIH.append(" from rp_xmjl where xmbm ='" + prjCode + "' and set_year = " + SessionUtilEx.getLoginYear());
		if (type)
		{
			list.add(sqljl_his_del.toString());
			list.add(sqlsb_his_del.toString());
			list.add(sqljl_his.toString());
			list.add(sqlsb_his.toString());
		}

		list.add(sqlD.toString());
		list.add(sqlI.toString());
		list.add(sqlIH.toString());
		return list;
	}


	/**
	 * ��ȡ�������
	 * 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	private DataSet getAuditStepDataSet(String filter) throws Exception
	{
		String sql = "select * from " + IPrjAudit.PrjAuditTable.PrjAuditSetTable.TABLENAME_AUDIT_SET;
		if (!Common.isNullStr(filter))
		{
			sql = sql + " where " + filter;
		}
		return DBSqlExec.getDataSet(sql);
	}


	/**
	 * ��ȡ���ұ���
	 * 
	 * @return
	 */
	private String getDepCode(String depID) throws Exception
	{
		String loginDepCode = DBSqlExec.client().getStringValue("select dep_code from vw_fb_department where dep_id = '" + depID + "'");
		return loginDepCode;
	}


	private DataSet getAuditCurStateDataSet(String prjCode) throws Exception
	{
		if (Common.isNullStr(prjCode)) { return null; }
		String sql = "select * from " + IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR + " where " + IPrjAudit.PrjAuditTable.prj_code + "='" + prjCode + "' and set_year = "
				+ SessionUtilEx.getLoginYear();
		DataSet ds = DBSqlExec.getDataSet(sql);
		if ((ds != null) && !ds.isEmpty())
		{
			return ds;
		}
		else
		{
			return null;
		}
	}


	/**
	 * ��ȡ��˵��������
	 * 
	 * @return
	 */
	private int getMaxStep() throws Exception
	{
		return DBSqlExec.getIntValue("select max(step_id) from " + IPrjAudit.PrjAuditTable.PrjAuditSetTable.TABLENAME_AUDIT_SET);
	}


	public String getCurState(String setYear, String rgCode) throws Exception
	{
		return DBSqlExec.getStringValue("select n_value from rp_s_config where suptype = 'CUR_STATE'");
	}


	/**
	 * ��ȡ���ñ���ĳ���������ֵ
	 * 
	 * ע��: �����ֶ�δ���û�ó���δ����ʱ��Ҳ�᷵��""��
	 * 
	 * @throws Exception
	 */
	public String getSupportNValue(String supType) throws Exception
	{
		return DBSqlExec.getStringValue("SELECT N_VALUE FROM fb_s_support WHERE SUPTYPE='" + supType + "'");

	}


	public DataSet getOptDataTypeList() throws Exception
	{
		DataSet ds = DataSet.create();
		int iBatchNo = 0; // ��ǰ����
		int iStateKind = 0; // ��ǰ���ε�����״̬ 2 �˴�����
		// ��λ�Ƿ���ʾ������ݣ�1 ��ʾ 0 ����ʾ
		int iDivShow = 1;
		// int iDivShow = DBSqlExec
		// .getIntValue("SELECT N_VALUE FROM FB_S_SUPPORT WHERE
		// SUPTYPE='DIVSHOWSH'");
		// ������� �� ��λ��ʾ�������
		if ("007".equals((SessionUtilEx.getBelongType())) || iDivShow == 1)
		{
			int id = 1;
			if (iStateKind == 2)
			{
				dataSetAppend(ds, id, iBatchNo, 1, getCurStateName());
				iBatchNo = iBatchNo - 1;
			}
			for (int i = 1; i <= iBatchNo; i++)
			{ // BatchNo
				for (int j = 0; j <= 1; j++) // DataType
				{
					id++;
					dataSetAppend(ds, id, i, j, (j == 0 ? Common.GetNumberOfHz(i) + "���걨��" : Common.GetNumberOfHz(i) + "�������"));
				}
			}
		}
		else
		{
			// ��λ�ǲ�ʾ�������
			if (iStateKind == 2)
			{
				dataSetAppend(ds, iBatchNo, iBatchNo, 1, getCurStateName());
				iBatchNo = iBatchNo - 1;
			}
			for (int i = 1; i <= iBatchNo; i++)
			{ // BatchNo
				dataSetAppend(ds, i, i, 0, Common.GetNumberOfHz(i) + "���걨��");
			}
		}
		return ds;
	}


	private void dataSetAppend(DataSet dataSet, int sID, int iBatchNo, int iDataType, String sDataName) throws Exception
	{
		dataSet.append();
		dataSet.fieldByName("ID").setValue(new Integer(sID));
		dataSet.fieldByName("BatchNo").setValue(new Integer(iBatchNo));
		dataSet.fieldByName("DataType").setValue(new Integer(iDataType));
		dataSet.fieldByName("Name").setValue(sDataName);
	}


	public String getCurStateName() throws Exception
	{
		String sql = " SELECT STATE_NAME FROM FB_S_STATE WHERE STATE_NO =" + " (SELECT N_VALUE FROM FB_S_SUPPORT WHERE SUPTYPE='CUR_STATE')" + " AND Set_Year = "
				+ SessionUtil.getUserInfoContext().getSetYear();
		return DBSqlExec.getStringValue(sql);
	}


	public String savehistory(String[] prjCodes) throws Exception
	{
		String msg = "";
		try
		{
			List list = new ArrayList();
			for (int i = 0; i < prjCodes.length; i++)
			{
				String xmxh = prjCodes[i];
				String bill_id = getbill(xmxh, "RP_TB_BILL");
				if (getTasksNode(bill_id, "RP_TB_BILL") == null)
					break;
//				int j = getTasksNode(bill_id, "RP_TB_BILL").size() - 1;
//				XMLData node = ((DTO) getTasksNode(bill_id, "RP_TB_BILL").get(j)).toXMLData();
//				String node_name = node.get("node_name").toString();
				String node_name = ((Map)(getTasksNode(bill_id, "RP_TB_BILL").get(0))).get("node_name").toString();
				// ��ѯ��Ŀ���ǰ�������κ�
				String sql_xmjl = "select  nvl(max(a.p_no),0) as p_no from rp_xmjl_history a where a.set_year = ? and a.xmxh = ? ";
				int xmjl_no = DBSqlExec.getIntValue(sql_xmjl, new String[] { SessionUtil.getLoginYear(), xmxh });

				StringBuffer sql = new StringBuffer();


				if (xmjl_no > 0)
				{
					// ɾ����ʷ����
					sql.append("DELETE FROM RP_XMJL_HISTORY WHERE XMXH='").append(xmxh).append("'");
					sql.append(" and set_year='").append(SessionUtil.getLoginYear()).append("'");
					sql.append(" AND node_name='").append(node_name).append("'");
					sql.append(" and p_no = ").append(xmjl_no);
					list.add(sql.toString());
					sql.delete(0, sql.length());

					// ɾ����Ŀ���ϸ��ʷ����
					sql.append("DELETE FROM RP_XMSB_HISTORY WHERE XMXH='").append(xmxh).append("'");
					sql.append(" AND node_name='").append(node_name).append("' ");
					sql.append(" and p_no = ").append(xmjl_no);
					sql.append(" and set_year='").append(SessionUtil.getLoginYear()).append("'");
					list.add(sql.toString());
					sql.delete(0, sql.length());
				}

				// ������ʷ����
				sql.append("INSERT INTO RP_XMJL_HISTORY  SELECT A.*,'");
				sql.append(node_name);
				sql.append("',sysdate ");
				sql.append(",").append(xmjl_no);
				sql.append(",'").append(SessionUtilEx.getUserInfoContext().getAttribute("user_name"));
				sql.append("' FROM RP_XMJL  A WHERE XMXH='").append(xmxh).append("'");
				sql.append(" and set_year='").append(SessionUtil.getLoginYear()).append("'");
				list.add(sql.toString());
				sql.delete(0, sql.length());

				// ������Ŀ���ϸ��ʷ����
				sql.append("INSERT INTO RP_XMSB_HISTORY SELECT A.*,0,0,0,'");
				sql.append(node_name);
				sql.append("',sysdate ");
				sql.append(",").append(xmjl_no);
				sql.append(" FROM rp_xmsb A WHERE XMXH='").append(xmxh).append("'");
				sql.append(" and set_year='").append(SessionUtil.getLoginYear()).append("'");
				list.add(sql.toString());
				sql.delete(0, sql.length());
			}

			QueryStub.getQueryTool().executeBatch(list);

		}
		catch (Exception ex)
		{
			msg = "������ʷ����ʧ��";
			ex.printStackTrace();
		}
		return msg;
	}


	public void savehistory(String prjCodes) throws Exception
	{
		String msg = "";
		try
		{
			List list = new ArrayList();

			String bill_id = getbill(prjCodes, "RP_TB_BILL");
			if (getTasksNode(bill_id, "RP_TB_BILL") == null)
				return;
//			int j = getTasksNode(bill_id, "RP_TB_BILL").size() - 1;
//			XMLData node = ((DTO) getTasksNode(bill_id, "RP_TB_BILL").get(j)).toXMLData();
//			String node_name = node.get("node_name").toString();
			String	node_name = ((Map)(getTasksNode(bill_id, "RP_TB_BILL").get(0))).get("node_name").toString();

			// ��ѯ��Ŀ���ǰ�������κ�
			String sql_xmjl = "select  nvl(max(a.p_no),0) as p_no from rp_xmjl_history a where a.set_year = ? and a.xmxh = ? ";
			int xmjl_no = DBSqlExec.getIntValue(sql_xmjl, new String[] { SessionUtil.getLoginYear(), prjCodes });

			StringBuffer sql = new StringBuffer();
			xmjl_no=xmjl_no+1;
			
			
			if (xmjl_no > 0)
			{
				// ɾ����ʷ����
				sql.append("DELETE FROM RP_XMJL_HISTORY WHERE XMXH='").append(prjCodes).append("'");
				sql.append(" AND SET_YEAR='").append(SessionUtil.getLoginYear()).append("'");
				sql.append(" AND node_name='").append(node_name).append("'");
				sql.append(" and p_no = ").append(xmjl_no);
				
				list.add(sql.toString());
				sql.delete(0, sql.length());

				// ɾ����Ŀ���ϸ��ʷ����
				sql.append("DELETE FROM RP_XMSB_HISTORY WHERE XMXH='").append(prjCodes).append("'");
				sql.append(" AND node_name='").append(node_name).append("' ");
				sql.append(" and p_no = ").append(xmjl_no);
				sql.append(" and set_year='").append(SessionUtil.getLoginYear()).append("'");
				list.add(sql.toString());
				sql.delete(0, sql.length());
			}

			// ������ʷ����
			sql.append("INSERT INTO RP_XMJL_HISTORY  SELECT A.*,'");
			sql.append(node_name);
			sql.append("',sysdate ");
			sql.append(",").append(xmjl_no);
			sql.append(",'").append(SessionUtilEx.getUserInfoContext().getAttribute("user_name"));
			sql.append("' FROM RP_XMJL  A WHERE XMXH='").append(prjCodes).append("'");
			sql.append(" and set_year='").append(SessionUtil.getLoginYear()).append("'");
			list.add(sql.toString());
			sql.delete(0, sql.length());

			// ������Ŀ���ϸ��ʷ����
			sql.append("INSERT INTO RP_XMSB_HISTORY SELECT A.*,0,0,0,'");
			sql.append(node_name);
			sql.append("',sysdate ");
			sql.append(",").append(xmjl_no);
			sql.append(" FROM rp_xmsb A WHERE XMXH='").append(prjCodes).append("'");
			sql.append(" and set_year='").append(SessionUtil.getLoginYear()).append("'");
			list.add(sql.toString());
			sql.delete(0, sql.length());

			QueryStub.getQueryTool().executeBatch(list);

		}
		catch (Exception ex)
		{
			msg = "������ʷ����ʧ��";
			ErrorInfo.showErrorDialog(ex, msg);
		}
	}


	public String saveTZhistory(String[] prjCodes) throws Exception
	{
		// TODO Auto-generated method stub
		String msg = "";
		try
		{
			List list = new ArrayList();
			for (int i = 0; i < prjCodes.length; i++)
			{
				String xmxh = prjCodes[i];
				String bill_id = getbill(xmxh, "RP_TZ_BILL");
				if (getTasksNode(bill_id, "RP_TZ_BILL") == null)
					break;
//				int j = getTasksNode(bill_id, "RP_TZ_BILL").size() - 1;
//				XMLData node = ((DTO) getTasksNode(bill_id, "RP_TZ_BILL").get(j)).toXMLData();
//				String node_name = node.get("node_name").toString();
				String node_name = ((Map)(getTasksNode(bill_id, "RP_TB_BILL").get(0))).get("node_name").toString();
				

				StringBuffer sql = new StringBuffer();
				// ɾ����ʷ����

				sql.append("DELETE FROM RP_XMJL_HISTORY WHERE XMXH='").append(xmxh).append("'");
				sql.append("AND node_name='").append(node_name).append("'");
				String strSql = sql.toString();
				sql.delete(0, sql.length());
				list.add(strSql);
				// ������ʷ����
				sql.append("INSERT INTO RP_XMJL_HISTORY  SELECT A.*,'");
				sql.append(node_name);
				sql.append("',sysdate ");
				sql.append(" FROM RP_XMJL  A WHERE XMXH='").append(xmxh).append("'");
				strSql = sql.toString();
				sql.delete(0, sql.length());
				list.add(strSql);

				// ɾ����Ŀ���ϸ��ʷ����
				sql.append("DELETE FROM RP_XMSB_HISTORY WHERE XMXH='").append(xmxh).append("'");
				sql.append("AND node_name='").append(node_name);
				sql.append("' ");
				strSql = sql.toString();
				sql.delete(0, sql.length());
				list.add(strSql);
				// ������Ŀ���ϸ��ʷ����
				sql.append("INSERT INTO RP_XMSB_HISTORY SELECT A.*,0,0,0,'");
				sql.append(node_name);
				sql.append("',sysdate ");
				sql.append("FROM RP_XMSB A WHERE XMXH='").append(xmxh).append("'");
				strSql = sql.toString();
				list.add(strSql);
				sql.delete(0, sql.length());
			}
			QueryStub.getQueryTool().executeBatch(list);

		}
		catch (Exception ex)
		{
			msg = "������ʷ����ʧ��";
			ex.printStackTrace();
		}
		return msg;
	}


	public int getCurBatchNO() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
