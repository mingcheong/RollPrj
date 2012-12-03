/**
 * 
 */
package gov.nbcs.rp.dicinfo.datadict.bs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;

/**
 * <p>
 * Title:��Ŀ����Դ����
 * </p>
 * <p>
 * Description:��Ŀ����Դ����
 * </p>

 
 */
public class PrjData {
	/** ��ĿĬ�ϴ����ֶ� */
	private static String VIEW_SQL_PRJDEFAULT = "A.ROW_ID, A.EN_ID, A.Div_Code, A.Div_Name,A.Div_Kind,"
			+ "b.prjsort_code as payout_kind_code,c.payout_kind_name,c.LVL_ID as Payout_Kind_lvl,"
			+ "A.Prj_Code,A.Prj_Name,substring(c.LVL_ID||'00000000000000000000',1,20)||A.Prj_Code  as project_lvl,A.Stock_Flag,";

	/** ��ĿĬ�ϴ����ֶ� */
	private static String VIEW_SQL_PRJDEFAULT_A = "A.Set_Year,A.Batch_No,A.Data_Type,A.Data_Src,A.Opt_No,A.ver_no,A.rg_code,A.last_ver";

	/** ��Ŀ����Ĭ�ϴ����ֶ� */
	private static String VIEW_SQL_PRJBASEDEFAULT = "B.PRJSORT_CODE,B.BS_ID,B.Acct_Code,B.Acct_Name,B.Start_Date,B.End_Date,B.PRJSTD_CODE,B.PRJ_STATUS,B.PRJ_CONTENT,";

	/** ��Ŀ����Ĭ�ϴ����ֶ� (���������ܿ�Ŀ�� */
	private static String VIEW_SQL_PRJBASEDEFAULT_A = "B.PRJSORT_CODE,B.Start_Date,B.End_Date,B.PRJSTD_CODE,B.PRJ_STATUS,B.PRJ_CONTENT,";

	private static Map FIELD_CNAME = new HashMap();
	static {
		FIELD_CNAME.put("ROW_ID", "ID��");
		FIELD_CNAME.put("EN_ID", "��λID");
		FIELD_CNAME.put("DIV_CODE", "��λ����");
		FIELD_CNAME.put("DIV_NAME", "��λ����");
		FIELD_CNAME.put("DIV_KIND", "��λ����");
		FIELD_CNAME.put("PAYOUT_KIND_CODE", "֧����Ŀ������");
		FIELD_CNAME.put("PAYOUT_KIND_NAME", "֧����Ŀ�������");
		FIELD_CNAME.put("PRJ_CODE", "��Ŀ����");
		FIELD_CNAME.put("PRJ_NAME", "��Ŀ����");
		FIELD_CNAME.put("STOCK_FLAG", "�Ƿ������ɹ�");
		FIELD_CNAME.put("SET_YEAR", "���");
		FIELD_CNAME.put("BATCH_NO", "����");
		FIELD_CNAME.put("DATA_TYPE", "��������");
		FIELD_CNAME.put("DATA_SRC", "����Դ����");
		FIELD_CNAME.put("OPT_NO", "OPT_NO");
		FIELD_CNAME.put("VER_NO", "VER_NO");
		FIELD_CNAME.put("RG_CODE", "������");
		FIELD_CNAME.put("LAST_VER", "LAST_VER");
		FIELD_CNAME.put("PRJSORT_CODE", "��Ŀ������");
		FIELD_CNAME.put("BS_ID", "���ܿ�ĿID");
		FIELD_CNAME.put("ACCT_CODE", "���ܿ�Ŀ����");
		FIELD_CNAME.put("ACCT_NAME", "���ܿ�Ŀ����");
		FIELD_CNAME.put("BSI_ID", "���ÿ�ĿID");
		FIELD_CNAME.put("ACCT_CODE_JJ", "���ÿ�Ŀ����");
		FIELD_CNAME.put("ACCT_NAME_JJ", "���ÿ�Ŀ����");
		FIELD_CNAME.put("START_DATE", "��ʼ����");
		FIELD_CNAME.put("END_DATE", "��������");
		FIELD_CNAME.put("PRJSTD_CODE", "��Ŀ��׼Ŀ¼����");
		FIELD_CNAME.put("PRJ_STATUS", "��Ŀ״̬");
		FIELD_CNAME.put("DETAIL_TYPE", "��Ŀ������ϸ����");
		FIELD_CNAME.put("DETAIL_TYPE_NAME", "��Ŀ������ϸ����");
		FIELD_CNAME.put("PAYOUT_KIND_LVL", "֧����Ŀ�������");
		FIELD_CNAME.put("PROJECT_LVL", "��Ŀ�����");
		FIELD_CNAME.put("PRJ_CONTENT", "��Ŀ����");

	}

	private static int COMBO_BOX_FLAG = 4;

	private static String MC_POSTFIX = "_MC";

	// ��Ŀ��ϸ(����Ԥ��)
	public static String getCreatePrjPfsView(String viewName, String reportID,
			String setYear) throws Exception {
		int inputSetId = Integer.parseInt(reportID.substring(11));
		// ��Ŀ�����������Ϣ
		String prjBaseColInfo = getPrjBaseColStr(inputSetId, "B", setYear);
		// ֧���ʽ���Դ����Ϣ
		String pfsColInfo = getPfsColStr("A", setYear);
		String sSql = "create or replace view "
				+ viewName
				+ " as select "
				+ VIEW_SQL_PRJDEFAULT
				+ VIEW_SQL_PRJBASEDEFAULT_A
				+ "A.BS_ID,A.Acct_Code,A.Acct_Name,A.BSI_ID,A.Acct_Code_JJ,A.Acct_Name_JJ,"
				+ prjBaseColInfo
				+ pfsColInfo
				+ VIEW_SQL_PRJDEFAULT_A
				+ " from fb_p_detail_pfs A"
				+ " left join fb_p_base b on  b.prj_code = a.prj_code and a.batch_no = b.batch_no"
				+ " and a.data_type = b.data_type and a.set_year =b.set_year"
				+ " left join vw_fb_payout_kind c on b.prjsort_code =c.payout_kind_code and b.set_year=c.set_year"
				+ " where A.DETAIL_TYPE =11 and b.detail_degree in (3,4)"
				+ " and  b.prj_status in (select state_code from fb_p_state_info c where c.state_value > 3)"
				+ " union all"
				+ " select "
				+ VIEW_SQL_PRJDEFAULT
				+ VIEW_SQL_PRJBASEDEFAULT_A
				+ "A.BS_ID,A.Acct_Code,A.Acct_Name,A.BSI_ID,'39904' as Acct_Code_JJ,'δ���ֵ���Ŀ֧��' as Acct_Name_JJ,"
				+ prjBaseColInfo
				+ pfsColInfo
				+ VIEW_SQL_PRJDEFAULT_A
				+ " from fb_p_detail_pfs A"
				+ " left join fb_p_base b on  b.prj_code = a.prj_code and a.batch_no = b.batch_no"
				+ " and a.data_type = b.data_type and a.set_year =b.set_year"
				+ " left join vw_fb_payout_kind c on b.prjsort_code =c.payout_kind_code and b.set_year=c.set_year"
				+ " where A.DETAIL_TYPE =11 and b.detail_degree in (1,2)"
				+ " and  b.prj_status in (select state_code from fb_p_state_info c where c.state_value > 3)";
		return sSql;
	}

	// ��Ŀ��ϸ(����Ԥ�㺬������Ŀ״̬)
	public static String getCreatePrjPfsAllStateView(String viewName,
			String reportID, String setYear) throws Exception {
		int inputSetId = Integer.parseInt(reportID.substring(11));
		// ��Ŀ�����������Ϣ
		String prjBaseColInfo = getPrjBaseColStr(inputSetId, "B", setYear);
		// ֧���ʽ���Դ����Ϣ
		String pfsColInfo = getPfsColStr("A", setYear);
		String sSql = "create or replace view "
				+ viewName
				+ " as select "
				+ VIEW_SQL_PRJDEFAULT
				+ VIEW_SQL_PRJBASEDEFAULT_A
				+ "A.BS_ID,A.Acct_Code,A.Acct_Name,A.BSI_ID,A.Acct_Code_JJ,A.Acct_Name_JJ,"
				+ prjBaseColInfo
				+ pfsColInfo
				+ VIEW_SQL_PRJDEFAULT_A
				+ " from fb_p_detail_pfs A"
				+ " left join fb_p_base b on  b.prj_code = a.prj_code and a.batch_no = b.batch_no"
				+ " and a.data_type = b.data_type and a.set_year =b.set_year"
				+ " left join vw_fb_payout_kind c on b.prjsort_code =c.payout_kind_code and b.set_year=c.set_year"
				+ " where A.DETAIL_TYPE =11 and b.detail_degree in (3,4)"
				+ " union all"
				+ " select "
				+ VIEW_SQL_PRJDEFAULT
				+ VIEW_SQL_PRJBASEDEFAULT_A
				+ "A.BS_ID,A.Acct_Code,A.Acct_Name,A.BSI_ID,'39904' as Acct_Code_JJ,'δ���ֵ���Ŀ֧��' as Acct_Name_JJ,"
				+ prjBaseColInfo
				+ pfsColInfo
				+ VIEW_SQL_PRJDEFAULT_A
				+ " from fb_p_detail_pfs A"
				+ " left join fb_p_base b on  b.prj_code = a.prj_code and a.batch_no = b.batch_no"
				+ " and a.data_type = b.data_type and a.set_year =b.set_year"
				+ " left join vw_fb_payout_kind c on b.prjsort_code =c.payout_kind_code and b.set_year=c.set_year"
				+ " where A.DETAIL_TYPE =11 and b.detail_degree in (1,2)";
		return sSql;
	}

	// ��ĿԤ��(��Ŀ��Ԥ��)
	public static String getCreatePrjPfsTotalView(String viewName,
			String reportID, String setYear) throws Exception {
		String sSql = getCreatePrjPfsTotalAllStateView(viewName, reportID,
				setYear)
				+ " and  b.prj_status in (select state_code from fb_p_state_info c where c.state_value > 3)";
		return sSql;
	}

	// ��ĿԤ��(��Ŀ��Ԥ�㺬������Ŀ״̬)
	public static String getCreatePrjPfsTotalAllStateView(String viewName,
			String reportID, String setYear) throws Exception {
		int inputSetId = Integer.parseInt(reportID.substring(11));
		// ��Ŀ�����������Ϣ
		String prjBaseColInfo = getPrjBaseColStr(inputSetId, "B", setYear);
		// ֧���ʽ���Դ����Ϣ
		String pfsColInfo = getPfsColStr("A", setYear);
		String sSql = "create or replace view "
				+ viewName
				+ " as select "
				+ VIEW_SQL_PRJDEFAULT
				+ VIEW_SQL_PRJBASEDEFAULT_A
				+ "B.BS_ID,B.Acct_Code,B.Acct_Name,A.BSI_ID,A.Acct_Code_JJ,A.Acct_Name_JJ,"
				+ prjBaseColInfo
				+ pfsColInfo
				+ VIEW_SQL_PRJDEFAULT_A
				+ " from fb_p_detail_pfs A"
				+ " left join fb_p_base b on  b.prj_code = a.prj_code and a.batch_no = b.batch_no"
				+ " and a.data_type = b.data_type and a.set_year =b.set_year"
				+ " left join vw_fb_payout_kind c on b.prjsort_code =c.payout_kind_code and b.set_year=c.set_year"
				+ " where A.DETAIL_TYPE =1 ";
		return sSql;

	}

	// ��Ŀ������ϸ
	public static String getCreatePrjMxViewSql(String viewName,
			String reportID, String setYear) throws Exception {
		int onePos = reportID.indexOf("-");
		int twoPos = reportID.substring(onePos + 1).indexOf("-") + onePos + 1;
		// /��ȡprjsortId��sunstring�ĵڶ�������Ӧ����λ��onePos��twoPos֮��,������10
		String prjsortId = reportID.substring(onePos + 1, twoPos);
		String detailCode = reportID.substring(twoPos + 1);
		// ������Ŀ���id�õ���Ӧ�����ID
		int inputSetId = getInputSetIdWithPrjSortId(prjsortId, setYear);

		String PrjSortCode = getPrjSortCodeWithPrjSortId(prjsortId, setYear);

		// ��Ŀ������ϸ���������Ϣ
		String prjMxColInfo = getPrjMxColStr(detailCode, "A", setYear);
		// ��Ŀ�����������Ϣ
		String prjBaseColInfo = getPrjBaseColStr(inputSetId, "B", setYear);
		if (Common.isNullStr(prjMxColInfo))
			return "";

		String sSql = "create or replace view "
				+ viewName
				+ " as select "
				+ VIEW_SQL_PRJDEFAULT
				+ VIEW_SQL_PRJBASEDEFAULT
				+ "A.DETAIL_TYPE,A.DETAIL_TYPE_NAME,A.Acct_Code_JJ,A.Acct_Name_JJ,"
				+ prjBaseColInfo
				+ prjMxColInfo
				+ VIEW_SQL_PRJDEFAULT_A
				+ " from fb_p_detail_mx A"
				+ " left join fb_p_base b on "
				+ " b.prj_code = a.prj_code and a.batch_no = b.batch_no"
				+ " and a.data_type = b.data_type and a.set_year =b.set_year"
				+ " left join vw_fb_payout_kind c on b.prjsort_code =c.payout_kind_code and b.set_year=c.set_year"
				// /�޸�ԭ�ȵ�b.=����
				// �޸�--����--��A.detail_type ='"+detailCode+"'�����õ�where����
				+ " where A.detail_type ='" + detailCode
				+ "' and  b.prjsort_code='" + PrjSortCode + "'";
		// b.prj_status in (select state_code from fb_p_state_info c where
		// c.state_value > 3)
		return sSql;
	}

	/**
	 * �õ���Ŀ�������õ�����Ϣ��ɵ��ַ���
	 * 
	 * @param inputSetId���ID
	 * @param dataSourceAlias����
	 * @param setYear���
	 * @return
	 * @throws Exception
	 */
	private static String getPrjBaseColStr(int inputSetId,
			String dataSourceAlias, String setYear) throws Exception {
		List lstData = getPrjBaseColInfo(inputSetId, setYear);
		return buildBaseSqlWithList(lstData, "comp_ename", dataSourceAlias,
				IDataDictBO.PRJ_BASE_PREFIX, setYear);
		// return buildSqlWithList(lstData, "comp_ename", dataSourceAlias,
		// IDataDictBO.PRJ_BASE_PREFIX);
	}

	/**
	 * �õ���Ŀ�������õ�����Ϣ
	 * 
	 * @param inputSetIdd���ID
	 * @param setYear���
	 * @return
	 */
	private static List getPrjBaseColInfo(int inputSetId, String setYear) {
		String sSql = "select b.comp_ename,b.comp_name as field_cname,b.field_type"
				+ " from fb_p_input_setdetail a,fb_p_input_components b"
				+ " where a.input_set_id="
				+ inputSetId
				+ " and a.input_comp_id = b.comp_id and a.set_year =b.set_year and a.set_year="
				+ setYear;
		return QueryStub.getQueryTool().findBySql(sSql);
	}

	/**
	 * �õ���Ŀ�������õ�����Ϣ,����������
	 * 
	 * @param inputSetIdd���ID
	 * @param setYear���
	 * @return
	 */
	private static List getPrjBaseColInfoAndMc(int inputSetId, String setYear) {
		String sSql = "select b.comp_ename,b.comp_name as field_cname,b.field_type"
				+ " from fb_p_input_setdetail a,fb_p_input_components b"
				+ " where a.input_set_id="
				+ inputSetId
				+ " and a.input_comp_id = b.comp_id and a.set_year =b.set_year and a.set_year="
				+ setYear;
		List lstDataMC = null;
		List lstData = QueryStub.getQueryTool().findBySql(sSql);
		HashMap mapValue;
		String fieldEname;
		String fieldCname;
		for (Iterator iter = lstData.iterator(); iter.hasNext();) {
			mapValue = (HashMap) iter.next();
			int fieldtype = Integer.parseInt((String) mapValue
					.get("field_type"));
			if (fieldtype == COMBO_BOX_FLAG) {
				fieldEname = (String) mapValue.get("comp_ename");
				fieldCname = (String) mapValue.get("field_cname");
				mapValue.put("field_cname", fieldCname + "����");
				if (lstDataMC == null) {
					lstDataMC = new ArrayList();
				}
				HashMap mapMc = new HashMap();
				mapMc.put("comp_ename", fieldEname + MC_POSTFIX);
				mapMc.put("field_cname", fieldCname + "����");
				lstDataMC.add(mapMc);
			}
		}

		if (lstDataMC != null)
			lstData.addAll(lstDataMC);
		return lstData;
	}

	/**
	 * �õ���Ŀ������ϸ�м�¼��Ϣ��ɵ��ַ���
	 * 
	 * @param detailCode��Ŀ������ϸCode
	 * @param dataSourceAlias����
	 * @param setYear���
	 * @return
	 */
	private static String getPrjMxColStr(String detailCode,
			String dataSourceAlias, String setYear) {
		List lstData = getPrjMxColInfo(detailCode, setYear);
		return buildSqlWithList(lstData, "field_ename", dataSourceAlias, "");
	}

	/**
	 * �õ���Ŀ������ϸ�м�¼��Ϣ
	 * 
	 * @param detailCode��Ŀ������ϸCode
	 * @param dataSourceAlias����
	 * @param setYear���
	 * @return
	 */
	private static List getPrjMxColInfo(String detailCode, String setYear) {
		String sSql = "select field_ename,field_fname as field_cname from fb_p_simp_colinfo where DETAIL_CODE = '"
				+ detailCode
				+ "' and set_year= "
				+ setYear
				+ " order by lvl_Id";
		return QueryStub.getQueryTool().findBySql(sSql);
	}

	/**
	 * �õ�֧���ʽ���Դ��ɵ��ַ���
	 * 
	 * @param detailCode��Ŀ������ϸCode
	 * @param dataSourceAlias����
	 * @param setYear���
	 * @return
	 */
	private static String getPfsColStr(String dataSourceAlias, String setYear) {
		List lstData = getPfsColInfo(setYear);
		return buildSqlWithList(lstData, "pfs_ename", dataSourceAlias, "");
	}

	/**
	 * �õ�֧���ʽ���Դ
	 * 
	 * @param detailCode��Ŀ������ϸCode
	 * @param setYear���
	 * @return
	 */
	private static List getPfsColInfo(String setYear) {
		String sSql = "select pfs_ename,pfs_fname as field_cname from fb_iae_payout_fundsource"
				+ " where end_flag = 1 and set_year= "
				+ setYear
				+ " order by lvl_Id";
		return QueryStub.getQueryTool().findBySql(sSql);
	}

	/**
	 * ��������ϢList����ַ���(�ֶ������ֶ���,)
	 * 
	 * @param lstData����ϢList
	 * @param fieldEname�ֶ������г�Ա
	 * @param dataSourceAlias����
	 * @param aliasPrefix����ǰ׺
	 * @return
	 */
	private static String buildSqlWithList(List lstData, String fieldEname,
			String dataSourceAlias, String aliasPrefix) {
		StringBuffer sResult = new StringBuffer();
		if (lstData.isEmpty())
			return "";
		Map mapValue;
		for (Iterator iter = lstData.iterator(); iter.hasNext();) {
			mapValue = (HashMap) iter.next();
			String enameValue = (String) mapValue.get(fieldEname);
			if (!Common.isNullStr(enameValue)) {
				sResult.append(dataSourceAlias + "." + enameValue + " as "
						+ aliasPrefix + enameValue + ",");
			}
		}
		return sResult.toString();
	}

	/**
	 * ���������ֶ���ϸ������������
	 * 
	 * @param cols
	 * @param inputSetId
	 * @param detailCode
	 * @param setYear
	 * @return
	 */
	public static List getUpdateFieldCnameSql(String reportID, String[][] cols,
			int inputSetId, String detailCode, String setYear) {
		List lstSql = new ArrayList();
		String fieldEname;
		String fieldCname;
		int iCount = cols.length;
		List lstDataPrjBase = getPrjBaseColInfoAndMc(inputSetId, setYear);
		List lstDataPrjMx = getPrjMxColInfo(detailCode, setYear);
		List lstDataPfs = getPfsColInfo(setYear);
		for (int i = 0; i < iCount; i++) {
			fieldEname = cols[i][0];
			// ��FIELD_CNAME�õ���������
			fieldCname = (String) FIELD_CNAME.get(fieldEname);
			// ����Ŀ�����������Ϣ�õ���������
			if (Common.isNullStr(fieldCname)) {
				fieldCname = getFieldCname(lstDataPrjBase, "comp_ename",
						fieldEname, IDataDictBO.PRJ_BASE_PREFIX);
			}
			if (reportID.indexOf(IDataDictBO.PRJ_MX) == 0) {
				// ��Ŀ������ϸ���������Ϣ�õ���������
				if (Common.isNullStr(fieldCname)
						&& !Common.isNullStr(detailCode)) {
					fieldCname = getFieldCname(lstDataPrjMx, "field_ename",
							fieldEname, "");
				}
			} else {
				// ֧���ʽ���Դ����Ϣ�õ���������
				if (Common.isNullStr(fieldCname)) {
					fieldCname = getFieldCname(lstDataPfs, "pfs_ename",
							fieldEname, "");
				}
			}

			if (!Common.isNullStr(fieldCname)) {
				lstSql.add("update fb_dict_info_detail set AFIELD_CNAME = '"
						+ fieldCname + "' where dicid = '" + reportID
						+ "' and AFIELD_ENAME='" + fieldEname
						+ "' and set_year=" + setYear);
			}
		}
		return lstSql;
	}

	/**
	 * �õ���������
	 * 
	 * @param lstData���ݼ�
	 * @param fieldEnameӢ������
	 * @return
	 */
	private static String getFieldCname(List lstData, String fieldEname,
			String enameValue, String aliasPrefix) {
		Map mapValue;
		for (Iterator iter = lstData.iterator(); iter.hasNext();) {
			mapValue = (HashMap) iter.next();
			String enameValueTmp = (String) mapValue.get(fieldEname);
			if (enameValue.equalsIgnoreCase(aliasPrefix + enameValueTmp)) {
				return (String) mapValue.get("field_cname");
			}
		}
		return null;
	}

	/**
	 * �õ���Ŀ������ϸID������Ŀ���
	 * 
	 * @param prjsortId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public static int getInputSetIdWithPrjSortId(String prjsortId,
			String setYear) throws Exception {
		return DBSqlExec
				.getIntValue("select INPUT_SET_ID from fb_e_prjsort where PRJSORT_ID='"
						+ prjsortId + "' and set_year=" + setYear);
	}

	/**
	 * �õ���Ŀ�����������Ŀ���ID
	 * 
	 * @param prjsortId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public static String getPrjSortCodeWithPrjSortId(String prjsortId,
			String setYear) throws Exception {
		return DBSqlExec
				.getStringValue("select PRJSORT_Code from fb_e_prjsort where PRJSORT_ID='"
						+ prjsortId + "' and set_year=" + setYear);
	}

	/**
	 * ��������ϢList����ַ���(�ֶ������ֶ���,)
	 * 
	 * @param lstData����ϢList
	 * @param fieldEname�ֶ������г�Ա
	 * @param dataSourceAlias����
	 * @param aliasPrefix����ǰ׺
	 * @return
	 * @throws Exception
	 */
	private static String buildBaseSqlWithList(List lstData, String fieldEname,
			String dataSourceAlias, String aliasPrefix, String setYear)
			throws Exception {
		StringBuffer sResult = new StringBuffer();
		if (lstData.isEmpty())
			return "";
		Map mapValue;
		for (Iterator iter = lstData.iterator(); iter.hasNext();) {
			mapValue = (HashMap) iter.next();

			String enameValue = (String) mapValue.get(fieldEname);
			if (!Common.isNullStr(enameValue)) {
				sResult.append(dataSourceAlias + "." + enameValue + " as "
						+ aliasPrefix + enameValue + ",");
				int fieldType = Integer.parseInt((String) mapValue
						.get("field_type"));
				if (fieldType == COMBO_BOX_FLAG) {// ������
					int compId = getCompIDWithEname(enameValue, setYear);
					sResult
							.append("(select item_name from fb_p_input_combox_values  val where  val.item_code= b."
									+ enameValue
									+ " and val.comp_id="
									+ compId
									+ ") as "
									+ aliasPrefix
									+ enameValue
									+ MC_POSTFIX + ",");
				}
			}

		}
		return sResult.toString();
	}

	/**
	 * ����comp_enameֵ�õ�comp_idֵ
	 * 
	 * @param ename
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private static int getCompIDWithEname(String ename, String setYear)
			throws Exception {
		return DBSqlExec
				.getIntValue("select comp_id from fb_p_input_components where comp_ename='"
						+ ename + "' and set_year=" + setYear);
	}
}
