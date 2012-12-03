package gov.nbcs.rp.sys.sysiaestru.bs;

import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.QueryStub;


public class PubUntSys {

	/**
	 * ȡ����ʽ�Ե�λ����Ȩ
	 * 
	 * @param setYear���
	 * @param sDivCode��λ
	 * @param sFormulaID��ʽ
	 * @param sFFieldName������Դ�ֶ�
	 * @param sPayOutItemCode֧����Ŀ����򾭼ÿ�Ŀ����
	 * @param iBatchNo����
	 * @param iDataType����
	 * @return
	 * @throws Exception
	 */
	public static boolean unReg_Div_Formula(String setYear, String sDivCode,
			String sFormulaID, String sFFieldName, String sPayOutItemCode,
			int iBatchNo, int iDataType, InfoPackage infoPackage)
			throws Exception {
		if ("".equals(sFormulaID) || sFormulaID == null) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("ע����ʽ����Ϊ��!");
			return false;
		}
		String sSqlFilter;
		// �ж��Ƿ�ϵͳ�Զ��幫ʽ������¼�룬��¼�룬�ÿգ�
		if (isDefaultFormula(sFormulaID.trim())) {
			if ("".equals(sFFieldName) || "".equals(sPayOutItemCode)) {
				infoPackage.setSuccess(false);
				infoPackage.setsMessage("ע����ʽ����λ��Ȩʱ��������ȷ��");
				return false;
			}
			sSqlFilter = " Formula_ID= '" + sFormulaID + "' and FFieldName= '"
					+ sFFieldName + "' and PayOut_Item_Code ='"
					+ sPayOutItemCode + "'";

		} else {
			sSqlFilter = " Formula_ID= '" + sFormulaID + "'";
		}
		if (!Common.isNullStr(sDivCode)) {
			sSqlFilter = sSqlFilter + " and div_code = '" + sDivCode + "'";
		}
		sSqlFilter = sSqlFilter + " and  set_Year=" + setYear;

		// String sSql = "select
		// Div_code,Formula_ID,FFieldName,Payout_Item_code"
		// + " from fb_Iae_PayOut_Formula_To_Div where " + sSqlFilter;
		// DataSet ds = DataSet.create();
		// DBSqlExec.getDataSet(sSql, ds);
		// ds.beforeFirst();
		// while (ds.next()) {
		// String sFFieldNameA = ds.fieldByName("FFieldName").getString();
		// String sDivCodeA = ds.fieldByName("Div_Code").getString();
		// String sPayOutItemCodeA = ds.fieldByName("PayOut_Item_Code")
		// .getString();
		// sSql = " update fb_u_Payout_Budget set " + sFFieldNameA
		// + " =0 where set_Year=" + setYear + " and Div_Code ='"
		// + sDivCodeA + "' and Acct_code_JJ ='" + sPayOutItemCodeA
		// + "'";
		// if (iBatchNo != -1)
		// sSql = sSql + " and Batch_No = " + iBatchNo;
		// if (iDataType != -1)
		// sSql = sSql + " and Data_Type=" + iDataType;
		// QueryStub.getQueryTool().executeUpdate(sSql);
		// }

		// ɾ����ʽ�Ե�λ
		String sSql = " delete from  fb_Iae_PayOut_Formula_To_Div where "
				+ sSqlFilter;
		QueryStub.getQueryTool().executeUpdate(sSql);

		// // ɾ����ʽֵ��
		// if (iBatchNo != -1)
		// sSqlFilter = sSqlFilter + " and Batch_No = " + iBatchNo;
		// if (iDataType != -1)
		// sSqlFilter = sSqlFilter + " and Data_Type=" + iDataType;
		//
		// sSql = " delete from fb_u_Div_PayOut_Formula_Value where "
		// + sSqlFilter;
		// QueryStub.getQueryTool().executeUpdate(sSql);

		return true;
	}

	public static List getUnReg_Div_FormulaSql(String setYear,
			String sFormulaID, String sFFieldName, String sPayOutItemCode,
			int iBatchNo, int iDataType, List optId, String parId) {
		List lstSql = new ArrayList();

		String sSqlFilter = "";
		// ɾ����ʽֵ��
		if (iBatchNo != -1)
			sSqlFilter = sSqlFilter + " and Batch_No = " + iBatchNo;
		if (iDataType != -1)
			sSqlFilter = sSqlFilter + " and Data_Type=" + iDataType;

		sSqlFilter += " and  set_Year=" + setYear;

		String sSql = " update fb_u_Payout_Budget set " + sFFieldName
				+ " =0 where PAYOUT_KIND_CODE =''" + parId
				+ "'' and  Acct_code_JJ =''" + sPayOutItemCode + "''";
		sSql += sSqlFilter;
		sSql += " and div_code in (select div_code from fb_s_struopt_to_div where OPT_ID=''"
				+ optId.get(0)
				+ "'' and set_year ="
				+ setYear
				+ ") and data_src=50";
		lstSql.add(sSql);

		sSql = " delete from fb_u_Div_PayOut_Formula_Value where formula_id=''"
				+ sFormulaID + "'' " + sSqlFilter;
		sSql += " and div_code in (select div_code from fb_s_struopt_to_div where OPT_ID=''"
				+ optId.get(1) + "'' and set_year =" + setYear + ")";
		lstSql.add(sSql);

		return lstSql;

	}

	/**
	 * �ж��Ƿ�ϵͳ�Զ��幫ʽ
	 * 
	 * @param sFormulaId
	 * @return
	 */
	public static boolean isDefaultFormula(String sFormulaId) {
//		if (IPayOutItem.DEAFAULT_FORMULA_FLAG
//				.equals(sFormulaId.substring(0, 1))) {
//			return true;
//		}
		return false;
	}
}
