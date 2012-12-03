/**
 * @# RepDisplayBO.java <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.bs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.queryreport.reportsh.ibs.IRepDisplay;

import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * ����˵��:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class RepDisplayBO implements IRepDisplay {

	private GeneralDAO dao;

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/**
	 * ȡ���ʽ���Դ
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getFunderSource(int level) throws Exception {
		int lvl = level * 4;

		String sSql = "select * from FB_IAE_PAYOUT_FUNDSOURCE where isnull(std_type_code,'000')<>'001' and length(lvl_id)<="
				+ lvl;
		DataSet ds = DataSet.create();
		ds.setSqlClause(sSql);
		ds.open();
		return ds;

	}

	/**
	 * ȡ������
	 * 
	 * @param sumfields
	 *            ���Ӻ͵��ֶα��ʽ
	 * @param tableName
	 *            ��ѯ�ı���
	 * @param fixCol
	 *            ǰ�����Ĺ̶���
	 * @param lstDiv
	 *            ѡ��ĵ�λ
	 * @return
	 * @throws Exception
	 */
	public List getSqlData(String sumfields, String fields, String tableName,
			String fixCol, List lstDiv, String filter) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		

		// ��һ��ƴд�ܼ�
		String sSqlTotal = "select '00' as xh, '�ܼ�'��as div_code,'' as div_name,"
				+ sumfields + " from " + tableName + " where  " + filter;
		// һ����λ
		String sSqlFirst = " select SUBSTR(Div_Code,1,3) as xh,SUBSTR(Div_Code,1,3) as div_code,"
				+ " '' as div_name,"
				+ sumfields
				+ " from "
				+ tableName
				+ " where " + filter + " group by SUBSTR(Div_Code,1,3) ";
		String firstInc = "select xh,a.div_Code,b.chr_name," + fields
				+ " from (" + sSqlFirst
				+ ") a,ELE_ENTERPRISE b where a.div_code=b.chr_code ";
		String sSqlBase = " select div_code as xh," + fixCol + "," + sumfields
				+ " from " + tableName + " where " + filter + " group by "
				+ fixCol + " order by div_code";
		String sSqlAll = "select * from (" + sSqlTotal + " union all "
				+ firstInc + " union all " + sSqlBase + ") order by xh";
		return dao.findBySql(sSqlAll);
	}

	/**
	 * ȡ����Ŀ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjSortDs(int lvl) throws Exception {
		String sSql = "";
		DataSet ds = DataSet.create();
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * ȡ���ʽ���Դ���ֶ�,����ÿ��Ϊ�ֶΣ�����Ԥ��֧�����ڶ���ΪCODEƴ����SQL������������Ŀ
	 * 
	 * @param lstSelectCode
	 * @return
	 * @throws Exception
	 */
	public List getFunfields(List lstSelectCode) throws Exception {
		if (lstSelectCode == null || lstSelectCode.isEmpty())
			return null;
		String sSql = "select pfs_ename  from FB_IAE_PAYOUT_FUNDSOURCE where ";
		StringBuffer sbField = new StringBuffer();
		int iCount = lstSelectCode.size();
		for (int i = 0; i < iCount; i++) {
			String lvl = (String) lstSelectCode.get(i);
			if (lvl.equals("1001")) {// ������ܼƣ������ﲻ�ٹ���
				List lstResult = new ArrayList();
				lstResult.add("f1");// �ܼ�
				lstResult.add("1=1");
				return lstResult;
			}
			sbField.append(" lvl_id like '").append(lstSelectCode.get(i))
					.append("%' or ");
		}
		sSql = sSql + "(" + sbField.substring(0, sbField.length() - 3)
				+ ") and pfs_ename is not null";
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.isEmpty())
			return null;
		iCount = lstData.size();
		StringBuffer sbResult = new StringBuffer();
		for (int i = 0; i < iCount; i++) {
			sbResult
					.append(
							"");
		}

		List lstResult = new ArrayList();
		lstResult.add(sbResult.substring(0, sbResult.length() - 1));
		lstResult.add(sbField.substring(0, sbField.length() - 3));
		return lstResult;

	}

	public void finalTreat(String userID) throws Exception {
		String sSql = "delete " + TEMP_TABLE + " where LOGIN_ID='" + userID
				+ "'";
		dao.executeBySql(sSql);

	}

	public void insertToTempTable(String fundCodeFilter, String filter,
			String fundSource, String userID) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql
				.append("insert into ")
				.append(TEMP_TABLE)
				.append(
						"(LOGIN_ID,DIV_CODE,DIV_NAME,DIV_KIND_CODE,DIV_KIND,div_fmkind,BATCH_NO,")
				.append("ACCT_CODE,ACCT_NAME,ACCT_CODE_JJ,ACCT_NAME_JJ,")
				.append("PRJ_CODE,PRJ_NAME,")
				.append(
						"DEP_CODE,DEP_NAME,PRJSORT_CODE,PRJSORT_NAME,SORT_CHARGE,ACCT_TYPE_CODE,ACCT_TYPE_NAME,PAYOUT")
				.append(
						"")
				.append(")��");
		// ��ӻ�����
		sbSql
				.append("select ")
				.append("'" + userID + "',")
				.append(
						"DIV_CODE,DIV_NAME,DIV_KIND_CODE,DIV_KIND,div_fmkind,BATCH_NO,")
				.append("ACCT_CODE,ACCT_NAME,ACCT_CODE_JJ,ACCT_NAME_JJ,")
				.append("PRJ_CODE,PRJ_NAME,")
				.append(
						"DEP_CODE,DEP_NAME,PRJSORT_CODE,PRJSORT_NAME,'����֧��' SORT_CHARGE,ACCT_TYPE_CODE,ACCT_TYPE_NAME,")
				.append(fundSource)
				.append(
						"")
				.append(" from ").append(VW_PAYOUT).append(" where ").append(
						filter);
//		sbSql.append(" union all ");
//		// �����Ŀ
//		sbSql
//				.append("select ")
//				.append("'" + userID + "',")
//				.append(
//						"DIV_CODE,DIV_NAME,DIV_KIND_CODE,DIV_KIND,div_fmkind,DIV_KIND,BATCH_NO,")
//				.append("ACCT_CODE,ACCT_NAME,ACCT_CODE_JJ,ACCT_NAME_JJ,")
//				.append("BUDGET_TYPE_CODE,BUDGET_TYPE_NAME,PRJ_CODE,PRJ_NAME,")
//				.append(
//						"DEP_CODE,DEP_NAME,PRJSORT_CODE,PRJSORT_NAME,SORT_CHARGE,ACCT_TYPE_CODE,ACCT_TYPE_NAME,PAYOUT")
//				.append(
//						",IS_LEGALINC,ENTER_MANA_TYPE,DETAIL_CLASS,DETAIL_CLASS_CODE,IS_SPECIALIZED,PRJ_STATUS")
//				.append(" from ").append(VW_PROJECT).append(" where ").append(
//						filter).append(" and ");
//		fundCodeFilter = fundCodeFilter.replaceAll("lvl_id", "PFS_LVL_ID");
//		sbSql.append("(").append(fundCodeFilter).append(")");
		dao.executeBySql(sbSql.toString());
	}

	/**
	 * ������ʱ��
	 * 
	 * @param fundCodeFilter
	 * @param filter
	 * @param fundSource
	 * @param userID
	 * @throws Exception
	 */
	public void insertToTempTableIncFun(String fundCodeFilter, String filter,
			String fundSource, String userID, String divFilter)
			throws Exception {
//		StringBuffer sbSql = new StringBuffer();
//		sbSql
//				.append("insert into ")
//				.append(TEMP_TABLE)
//				.append(
//						"(LOGIN_ID,DIV_CODE,DIV_NAME,DIV_KIND_CODE,DIV_KIND,div_fmkind,DIV_KIND,BATCH_NO,")
//				.append("ACCT_CODE,ACCT_NAME,ACCT_CODE_JJ,ACCT_NAME_JJ,")
//				.append("BUDGET_TYPE_CODE,BUDGET_TYPE_NAME,PRJ_CODE,PRJ_NAME,")
//				.append(
//						"DEP_CODE,DEP_NAME,PRJSORT_CODE,PRJSORT_NAME,PFS_LVL_ID,PFS_NAME,SORT_CHARGE,ACCT_TYPE_CODE,ACCT_TYPE_NAME,PAYOUT")
//
//				.append(
//						",IS_LEGALINC,ENTER_MANA_TYPE,DETAIL_CLASS,DETAIL_CLASS_CODE,IS_SPECIALIZED,PRJ_STATUS")
//				.append(")��");
//		// //��ӻ�����
//
//		// �����Ŀ
//		sbSql
//				.append("select ")
//				.append("'" + userID + "',")
//				.append(
//						"DIV_CODE,DIV_NAME,DIV_KIND_CODE,DIV_KIND,div_fmkind,DIV_KIND,BATCH_NO,")
//				.append("ACCT_CODE,ACCT_NAME,ACCT_CODE_JJ,ACCT_NAME_JJ,")
//				.append("BUDGET_TYPE_CODE,BUDGET_TYPE_NAME,PRJ_CODE,PRJ_NAME,")
//				.append(
//						"DEP_CODE,DEP_NAME,PRJSORT_CODE,PRJSORT_NAME,PFS_LVL_ID,PFS_NAME,SORT_CHARGE,ACCT_TYPE_CODE,ACCT_TYPE_NAME,PAYOUT")
//
//				.append(
//						",IS_LEGALINC,ENTER_MANA_TYPE,DETAIL_CLASS,DETAIL_CLASS_CODE,IS_SPECIALIZED,PRJ_STATUS")
//				.append(" from ").append(VW_PROJECT).append(" where ").append(
//						filter).append(" and ");

		// ���ɻ�����Ϣ
		// String sSql = "select LVL_ID,pfs_ename from fb_iae_payout_fundsource
		// where pfs_ename is not null and "
		// + fundCodeFilter;
		//�Ȳ�ѯ��û�е�λû�ж�Ӧ�ʽ���Դ������У����õ�ȫ��
		String sSql ="";
		/*= "select distinct lvl_id, pfs_ename from ("
				+
				// "select LVL_ID,pfs_ename from fb_iae_payout_fundsource pfs,
				// fb_iae_div_to_fundsource d2f where pfs_ename is not null and
				// ("
				// + fundCodeFilter
				// + ") and d2f.pfs_code=pfs.pfs_code and "
				// + divFilter
				// + " union all " +
				"select LVL_ID,pfs_ename from fb_iae_payout_fundsource pfs, fb_iae_div_to_fundsource d2f where pfs_ename is not null and ("
				+ fundCodeFilter
				+ ") and d2f.fs_code=pfs.pfs_code and "
				+ divFilter
				+ " union all "
				+ "select LVL_ID,pfs_ename from fb_iae_payout_fundsource pfs, fb_iae_div_to_fundsource d2f where pfs_ename is not null and ("
				+ fundCodeFilter + ") and d2f.fs_code=pfs.pfs_code and "
				+ divFilter + ")";*/
		
		sSql="select distinct lvl_id, pfs_ename from ( select  LVL_ID,pfs_ename from fb_iae_payout_fundsource pfs  where pfs_ename is not null and ("
				+ fundCodeFilter
				+ "))";

		// ������ʱ����
//		fundCodeFilter = fundCodeFilter.replaceAll("lvl_id", "PFS_LVL_ID");
//		sbSql.append("(").append(fundCodeFilter).append(")");
//		dao.executeBySql(sbSql.toString());

		List lstSql = new ArrayList();
		List lstFields = dao.findBySql(sSql);
		int iCount = lstFields.size();
		String strPre = "insert into "
				+ TEMP_TABLE
				+ "(LOGIN_ID,DIV_CODE,DIV_NAME,DIV_KIND_CODE,DIV_KIND,div_fmkind,BATCH_NO,"
				+ "ACCT_CODE,ACCT_NAME,ACCT_CODE_JJ,ACCT_NAME_JJ,"
				+ "PRJ_CODE,PRJ_NAME,"
				+ "DEP_CODE,DEP_NAME,PRJSORT_CODE,PRJSORT_NAME,SORT_CHARGE,ACCT_TYPE_CODE,ACCT_TYPE_NAME"
				+ ""
				+ ",PFS_LVL_ID,PAYOUT"
				+ ") select '"
				+ userID
				+ "',DIV_CODE,DIV_NAME,DIV_KIND_CODE,DIV_KIND,div_fmkind,BATCH_NO,"
				+ "ACCT_CODE,ACCT_NAME,ACCT_CODE_JJ,ACCT_NAME_JJ,"
				+ "PRJ_CODE,PRJ_NAME,"
				+ "DEP_CODE,DEP_NAME,PRJSORT_CODE,PRJSORT_NAME,'����֧��',ACCT_TYPE_CODE,ACCT_TYPE_NAME"
				+ ""
				+ ",";
		Map mapRow;
		for (int i = 0; i < iCount; i++) {
			mapRow = (Map) lstFields.get(i);
			String fieldName = (String) mapRow.get("pfs_ename");
			String lvlId = (String) mapRow.get("lvl_id");
			sSql = strPre + "'" + lvlId + "'," + fieldName + " from "
					+ VW_PAYOUT + " where isnull(" + fieldName + ",0)<>0 and  "
					+ filter;
			lstSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);

	}

	public void refreshCurUserQrData(List lstDiv) throws Exception {
		/*// ��ȡ��Ҫ�޸ĵĵ�λ����������
		int iCount = lstDiv.size();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iCount; i++) {
			sb.append("div_code like '").append(lstDiv.get(i)).append("%' or ");
		}
		String filter = "(" + sb.substring(0, sb.length() - 3) + ")";
		// ������κ��������͵Ĺ���,��ǰ���Σ���ǰ����
		FlowBO flowBO = new FlowBO();
		flowBO.setDao(dao);
		UserInfoEx userInfo = flowBO.getUserInfoExObj(SessionUtil
				.getUserInfoContext().getUserID());
		filter = filter + " and data_type=" + userInfo.getUserType()
				+ " and batch_no="
				+ PubInterfaceStub.getServerMethod().getCurBatchNO();

		iCount = upateView.length;
		List lstSql = new ArrayList();

		for (int i = 0; i < iCount; i++) {
			String viewName = upateView[i];

			//��λ�û�����ͬ������Ϊ�Ǹ�����ʹ�õ�
			if ("VW_FB_BUDGET_DETAIL".equalsIgnoreCase(viewName)) {
				if (userInfo.getUserType() == FlowNode.TYPE_DIV
						|| userInfo.getUserType() == FlowNode.TYPE_CHARGE
						|| userInfo.getUserType() == FlowNode.TYPE_SUP_DIV) {
					continue;
				}
			}

			String tableName = PRE_STRING + viewName;
			// ��ɾ��
			String sSql = "delete " + tableName + " where " + filter;
			lstSql.add(sSql);
			sSql = "insert into " + tableName + " select * from  " + viewName
					+ " where " + filter;
			lstSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);*/

	}

	/**
	 * ˢ�²�������
	 * 
	 * @param lstDiv
	 *            ��λ�б�
	 * @param batchNo
	 *            ����
	 * @param userType
	 *            �û�����
	 * @param lstView
	 *            Ҫˢ�µ���ͼ ��Ϊ��
	 * @param prjCode
	 *            ��Ŀ��ţ���Ϊ��
	 * @throws Exception
	 */
	public void refreshCurUserQrDataBySpec(List lstDiv, int batchNo,
			int userType, String[] lstView, String prjCode) throws Exception {
	/*	// �ȼ������ǲ���ִ��
		int inType = SupportValueStore.server().getSupportValue(
				"DATA_PREPARE_TYPE").getNValueAsInt();
		if (inType != 1)
			return;
		// ��ȡ��Ҫ�޸ĵĵ�λ����������
		String divFilter = " 1=1 ";
		if (lstDiv != null && !lstDiv.isEmpty()) {
			int iCount = lstDiv.size();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < iCount; i++) {
				sb.append("div_code = '").append(lstDiv.get(i)).append("' or ");
			}
			divFilter = "(" + sb.substring(0, sb.length() - 3) + ")";
		}
		FlowBO flowBO = new FlowBO();
		flowBO.setDao(dao);
		// ������κ��������͵Ĺ���,��ǰ���Σ���ǰ����
		if (batchNo <= 0)
			batchNo = PubInterfaceStub.getServerMethod().getCurBatchNO();
		String filter = " batch_no=" + batchNo
				+ (userType >= 0 ? (" and data_type=" + userType) : "");

		if (!Common.isNullStr(prjCode)) {
			filter = filter + " and prj_code='" + prjCode + "'";
		}

		if (lstView == null || lstView.length == 0)
			lstView = upateView;
		int iCount = lstView.length;
		List lstSql = new ArrayList();

		if (Common.isNullStr(prjCode)) {// �����Ŀ����Ϊ�գ��������������������Ϊ�գ���Ҫ��ɾ��ʱ���ӵ�λ����������ʱ�ӵ�λ����
			for (int i = 0; i < iCount; i++) {
				filter = filter + " and " + divFilter;
				String viewName = lstView[i];
				//��λ�û�����ͬ������Ϊ�Ǹ�����ʹ�õ�
				if ("VW_FB_BUDGET_DETAIL".equalsIgnoreCase(viewName)) {
					if (userType == FlowNode.TYPE_DIV
							|| userType == FlowNode.TYPE_CHARGE
							|| userType == FlowNode.TYPE_SUP_DIV) {
						continue;
					}
				}

				String tableName = PRE_STRING + viewName;
				// ��ɾ��
				String sSql = "delete " + tableName + " where " + filter;
				lstSql.add(sSql);
				sSql = "insert into " + tableName + " select * from  "
						+ viewName + " where " + filter;
				lstSql.add(sSql);
			}
		} else {
			for (int i = 0; i < iCount; i++) {
				String viewName = lstView[i];
				//��λ�û�����ͬ������Ϊ�Ǹ�����ʹ�õ�
				if ("VW_FB_BUDGET_DETAIL".equalsIgnoreCase(viewName)) {
					if (userType == FlowNode.TYPE_DIV
							|| userType == FlowNode.TYPE_CHARGE
							|| userType == FlowNode.TYPE_SUP_DIV) {
						continue;
					}
				}

				String tableName = PRE_STRING + viewName;
				// ��ɾ��
				String sSql = "delete " + tableName + " where " + filter;
				lstSql.add(sSql);
				sSql = "insert into " + tableName + " select * from  "
						+ viewName + " where " + divFilter + " and " + filter;
				lstSql.add(sSql);
			}
		}
		QueryStub.getQueryTool().executeBatch(lstSql);*/

	}

//	/* (non-Javadoc)
//	 * @see gov.nbcs.rp.queryreport.reportsh.ibs.IRepDisplay#getFundClass(int)
//	 */
//	public DataSet getFundClass(int lvl) throws Exception {
//		String sSql = "SELECT * FROM VW_FB_FUND_CLASS T"
//				+ " WHERE LENGTH(T.DETAIL_CLASS_CODE) <= " + lvl
//				+ " ORDER BY DETAIL_CLASS_CODE";
//		DataSet ds = DataSet.create();
//		ds.setSqlClause(sSql);
//		ds.open();
//		return ds;
//	}
//
//	/**
//	 * ȡ����Ŀ�������
//	 * @return
//	 * @throws Exception
//	 */
//	public DataSet getFunClassAll() throws Exception {
//		String sSql = "select detail_code as detail_code_lvl,detail_code as detail_class_code,detail_name from FB_P_SIMP_MASTER a where a.detail_code not like 'x%'"
//				+ " union all "
//				+ " select detail_code||subStr('000'||field_id,"
//				+ "LEN('000'||field_id)-2 )as detail_code_lvl,detail_code||'-'||field_id as detail_class_code,field_cname as detail_name from FB_P_SIMP_COLINFO where detail_code not like 'x%' ";
//		DataSet ds = DataSet.create();
//		ds.setSqlClause(sSql);
//		ds.open();
//		return ds;
//	}
	/**
	 * ȡ�����ѡ����
	 * 
	 * @return
	 */
	public String getYearRefString() {
		String sSql = "  select set_year,year_name from sys_year where enabled=1 ";
		StringBuffer sb = new StringBuffer();
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.isEmpty())
			return "";
		int iCount = lstData.size();
		for (int i = 0; i < iCount; i++) {
			Map map = (Map) lstData.get(i);
			sb.append(map.get("set_year")).append("#").append(
					map.get("year_name")).append("+");
		}
		return sb.substring(0, sb.length() - 1);

	}
	
	/**
	 * ������Ŀ���룬��ȡ��λ����
	 * @throws Exception 
	 */
	public String getDivCodeByPrjCode(String prjCode,int batchNo,int dataType,String setYear){
		String sql = 
			" select a.DIV_CODE " +
			" from FB_P_MAIN a" +
			" where a.PRJ_CODE='"+prjCode+"' and a.SET_YEAR="+setYear;
//			" and a.DATA_TYPE="+dataType;
		try {
			List list = QueryStub.getQueryTool().executeQuery(sql);
			if(!(list==null||list.size()<1)){
				Object value = ((Map)list.get(0)).get("DIV_CODE"); 
				return value.toString();
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return "";
	}
}
