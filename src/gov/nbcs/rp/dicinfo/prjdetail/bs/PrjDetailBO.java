/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title ��Ŀ��ϸ����-ʵ����
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */

package gov.nbcs.rp.dicinfo.prjdetail.bs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.dicinfo.prjdetail.ibs.IPrjDetail;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

// pubcode
// DQRDISPLAY ��ʾ���
// DQREDIT �༭���
// PRJDETAILSTDCOL ��Ŀ��ϸ��׼��
public class PrjDetailBO implements IPrjDetail {
	// private String detailSQL = "SELECT * FROM fb_p_simp_master WHERE set_year
	// = ?";

	private String DQRDISPLAYSQL = "SELECT * FROM fb_s_pubcode WHERE TYPEID = 'DQRDISPLAY' AND set_year = ?";

	private String DQREDITSQL = "SELECT * FROM fb_s_pubcode WHERE TYPEID = 'DQREDIT' AND set_year = ?";

	private String STDCOLSQL = "SELECT * FROM fb_s_pubcode WHERE TYPEID = 'PRJDETAILSTDCOL' AND set_year = ?";

	public DataSet getDataset(String aTableName, String aFilter)
			throws Exception {
		DataSet ds = DataSet.create();
		String sql = "SELECT * FROM " + aTableName + " WHERE set_year = ?";
		String sFilter = Common.isNullStr(aFilter) ? "" : " and " + aFilter;
		sql = sql + sFilter;
		ds.setSqlClause(sql);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}

	public DataSet getComboDPDataset() throws Exception {
		DataSet ds = DataSet.create();
		ds.setSqlClause(DQRDISPLAYSQL);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}

	public DataSet getComboREDataset() throws Exception {
		DataSet ds = DataSet.create();
		ds.setSqlClause(DQREDITSQL);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}

	public DataSet getComboSCDataset() throws Exception {
		DataSet ds = DataSet.create();
		ds.setSqlClause(STDCOLSQL);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}

	/**
	 * ��ȡ��ָ������ĳ���ֶε�ָ����ʽ�����ֵ
	 * 
	 * @param aFieldName
	 *            Ҫ��ȡ���ֵ���ֶ�����
	 * @param aTableName
	 *            ����
	 * @param aFilter
	 *            ��������
	 * @param aFormat
	 *            Ҫ�ѻ�ȡ��ֵformat�ɵĸ�ʽλ��
	 * @return ���
	 * @throws Exception
	 */
	public String getFormatMaxCode(String aFieldName, String aTableName,
			String aFilter, int aFormat) throws Exception {
		String sValue = null;
		sValue = DBSqlExec
				.getMaxValueFromField(aTableName, aFieldName, aFilter);
		if (Common.isNullStr(sValue)) {
			sValue = Common.getStrID(new BigDecimal(sValue).add(new BigDecimal(
					1)), 4);
		}
		return sValue;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @param aFilterCode
	 *            ��������,�ж��ǻ�ȡdetail�ı���������еı������
	 * @return ���
	 * @throws Exception
	 */
	public SysCodeRule getCodeRule(String aFilterCode) throws Exception {
		if (IPrjDetail.DETAILTAB_CODE.equals(aFilterCode)) {
			SysCodeRule sysCodeRule = new SysCodeRule(Integer
					.parseInt(SessionUtil.getUserInfoContext().getSetYear()),
					IPrjDetail.DETAILTAB_CODE);
			return sysCodeRule;
		}
		if (IPrjDetail.DETAILTABITEM_CODE.equals(aFilterCode)) {
			SysCodeRule sysCodeRule = new SysCodeRule(Integer
					.parseInt(SessionUtil.getUserInfoContext().getSetYear()),
					IPrjDetail.DETAILTABITEM_CODE);
			return sysCodeRule;
		} else
			return null;
	}

	/**
	 * ���ݹ����������dataset
	 * 
	 * @param aTableName
	 *            ����
	 * @param aFilter
	 *            ��������
	 * @return ���
	 * @throws Exception
	 */
	public DataSet getColInfoAccordDetailCode(String aTableName, String aFilter)
			throws Exception {
		DataSet ds = DataSet.create();
		String sSql = "select * from " + aTableName + " where set_year="
				+ SessionUtil.getUserInfoContext().getSetYear()
				+ (Common.isNullStr(aFilter) ? "" : " and " + aFilter);
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * ����dataset�����ݿ�
	 * 
	 * @param ds
	 *            Ҫ���ݵ�dataset
	 */
	public void dsPost(DataSet dsDetail,DataSet dscol,DataSet dsSour) throws Exception {
		dsDetail.post();
		dscol.post();
		dsSour.post();
	}

	/**
	 * ��������Ϣ����
	 * 
	 * @throws Exception
	 */
	public void saveColData(DataSet dsDetail,DataSet ds) throws Exception {
		dsDetail.post();
		// ɾ����¼
		DataSet dsDel = DataSetUtil.subDataSet(ds, DataSet.FOR_DELETE);
		dsDel.setName(ds.getName());
		dsDel.setPrimarykey(ds.getPrimarykey());
		dsDel.edit();
		dsDel.post();
		// �޸ļ�¼
		DataSet dsUpdate = DataSetUtil.subDataSet(ds, DataSet.FOR_UPDATE);
		List listAdd = new ArrayList();
		List listDel = new ArrayList();
		if (!dsUpdate.isEmpty()) {
			dsUpdate.beforeFirst();
			while (dsUpdate.next()) {
//				List list = new ArrayList();
				StringBuffer sqlDel = new StringBuffer();
				sqlDel.append("delete from FB_P_SIMP_COLINFO where FIELD_ID='");
				sqlDel.append(dsUpdate.fieldByName("FIELD_ID").getOldValue());
				sqlDel.append("' and DETAIL_CODE='");
				sqlDel.append(dsUpdate.fieldByName("DETAIL_CODE").getOldValue());
				sqlDel.append("' and set_year=");
				sqlDel.append(dsUpdate.fieldByName("set_year").getOldValue());
//				QueryStub.getQueryTool().executeUpdate(sqlDel.toString());
				listDel.add(sqlDel.toString());
				StringBuffer sqlUpdate = new StringBuffer();
				sqlUpdate
						.append("insert into FB_P_SIMP_COLINFO(PRIMARY_PROPFIELD,FIELD_INDEX,");
				sqlUpdate
						.append("PRIMARY_INDEX,PICK_VALUES,FIELD_ID,IS_HIDECOL,EDIT_FORMAT,");
				sqlUpdate
						.append("NOTNULL,LVL_ID,DETAIL_CODE,SET_YEAR,LAST_VER,FIELD_KIND,");
				sqlUpdate
						.append("FIELD_FNAME,DATA_TYPE,STD_TYPE,FIELD_COLUMN_WIDTH,DATASOURCE,");
				sqlUpdate
						.append("DISPLAY_FORMAT,FORMULA_DET,RG_CODE,FIELD_ENAME,");
				sqlUpdate
						.append("FIELD_CNAME,CALC_PRI,REPLACE_FIELD,IS_SUMCOL");
				sqlUpdate.append(") values('");
				sqlUpdate.append(dsUpdate.fieldByName("PRIMARY_PROPFIELD")
						.getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FIELD_INDEX").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("PRIMARY_INDEX").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("PICK_VALUES").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FIELD_ID").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("IS_HIDECOL").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("EDIT_FORMAT").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("NOTNULL").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("LVL_ID").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("DETAIL_CODE").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("SET_YEAR").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("LAST_VER").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FIELD_KIND").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FIELD_FNAME").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("DATA_TYPE").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("STD_TYPE").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FIELD_COLUMN_WIDTH")
						.getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("DATASOURCE").getString());
				sqlUpdate.append("','");
				//�жϣ�����С���������ʽû�����ã���Ҫǿ��һ��Ĭ�ϵģ�����ҳ��¼�뱨��
				String DISPLAY_FORMAT = dsUpdate.fieldByName("DISPLAY_FORMAT").getString();
				if("".equals(DISPLAY_FORMAT)){
					if("������".equals(dsUpdate.fieldByName("DATA_TYPE").getString()))
						DISPLAY_FORMAT = "#,##0.##";
				}
				sqlUpdate.append(DISPLAY_FORMAT);
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FORMULA_DET").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("RG_CODE").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FIELD_ENAME").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("FIELD_CNAME").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("CALC_PRI").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("REPLACE_FIELD").getString());
				sqlUpdate.append("','");
				sqlUpdate.append(dsUpdate.fieldByName("IS_SUMCOL").getString());
				sqlUpdate.append("')");
//				 QueryStub.getQueryTool().executeUpdate(sqlUpdate.toString());
				listAdd.add(sqlUpdate.toString());
//				QueryStub.getQueryTool().executeBatch(list);
			}
		}
		// ��Ӳ���
		DataSet dsAdd = DataSetUtil.subDataSet(ds,DataSet.FOR_APPEND
				| DataSet.FOR_INSERT);
		dsAdd.beforeFirst();
		while(dsAdd.next()){
			StringBuffer sqlAdd = new StringBuffer();
			sqlAdd
					.append("insert into FB_P_SIMP_COLINFO(PRIMARY_PROPFIELD,FIELD_INDEX,");
			sqlAdd
					.append("PRIMARY_INDEX,PICK_VALUES,FIELD_ID,IS_HIDECOL,EDIT_FORMAT,");
			sqlAdd
					.append("NOTNULL,LVL_ID,DETAIL_CODE,SET_YEAR,LAST_VER,FIELD_KIND,");
			sqlAdd
					.append("FIELD_FNAME,DATA_TYPE,STD_TYPE,FIELD_COLUMN_WIDTH,DATASOURCE,");
			sqlAdd
					.append("DISPLAY_FORMAT,FORMULA_DET,RG_CODE,FIELD_ENAME,");
			sqlAdd
					.append("FIELD_CNAME,CALC_PRI,REPLACE_FIELD,IS_SUMCOL");
			sqlAdd.append(") values('");
			sqlAdd.append(dsAdd.fieldByName("PRIMARY_PROPFIELD")
					.getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FIELD_INDEX").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("PRIMARY_INDEX").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("PICK_VALUES").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FIELD_ID").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("IS_HIDECOL").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("EDIT_FORMAT").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("NOTNULL").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("LVL_ID").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("DETAIL_CODE").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("SET_YEAR").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("LAST_VER").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FIELD_KIND").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FIELD_FNAME").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("DATA_TYPE").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("STD_TYPE").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FIELD_COLUMN_WIDTH")
					.getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("DATASOURCE").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("DISPLAY_FORMAT").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FORMULA_DET").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("RG_CODE").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FIELD_ENAME").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("FIELD_CNAME").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("CALC_PRI").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("REPLACE_FIELD").getString());
			sqlAdd.append("','");
			sqlAdd.append(dsAdd.fieldByName("IS_SUMCOL").getString());
			sqlAdd.append("')");
//			 QueryStub.getQueryTool().executeUpdate(sqlAdd.toString());
			listAdd.add(sqlAdd.toString());
		}
		QueryStub.getQueryTool().executeBatch(listDel);
		QueryStub.getQueryTool().executeBatch(listAdd);

//		dsAdd.setName(ds.getName());
//		dsAdd.setPrimarykey(ds.getPrimarykey());
//		dsAdd.edit();
//		dsAdd.post();
	}

	/**
	 * ��ȡfield_ename
	 */
	public DataSet getENameDataSet() throws Exception {
		DataSet ds = DataSet.create();
		String sSqlText = "SELECT COLUMN_NAME,substr(COLUMN_NAME,1,1) As COLUMN_TYPE"
				+ " FROM ALL_TAB_COLS"
				+ " WHERE 1=1 And TABLE_NAME='FB_P_DETAIL'"
				+ " AND substr(COLUMN_NAME,1,1) IN ('F','N','C')"
				+ " Order By Column_Type";
		ds.setSqlClause(sSqlText);
		ds.open();
		return ds;
	}

	/**
	 * ��ϸ�����Ʊ�ʹ��
	 */
	public boolean checkDetailName(String code,String Name) throws Exception {
		String sFilter = IPrjDetail.DETAIL_CODE + "<>'" + code + "' and "
		        + IPrjDetail.DETAIL_NAME + "='" + Name + "' and "
				+ IPrjDetail.SET_YEAR + "="
				+ SessionUtil.getUserInfoContext().getSetYear();
		int num = DBSqlExec.getRecordCount(IPrjDetail.Table_SIMP_MASTER,
				sFilter);
		if (num > 0) { // ����м�¼��ɾ��
			return true;
		}
		else return false;
	}

	/**
	 * ȡ�����������ݼ�
	 */
	public DataSet getAcctData() throws Exception {
		DataSet ds = DataSet.create();
		String sql = "select SET_YEAR,BSI_ID, ACCT_CODE_JJ, ACCT_NAME_JJ, substr('                             ',1,((length(ACCT_CODE_JJ)-3)/2)*2)||ACCT_NAME_JJ AS SHOWNAME,IS_LEAF from vw_fb_acct_economy_prj order by acct_code_jj";
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}

	/**
	 * 
	 */
	public int getRecNum(String aTableName, String aFilter) throws Exception {
		int num = 0;
		num = DBSqlExec.getRecordCount(aTableName, aFilter);
		return num;
	}
}
