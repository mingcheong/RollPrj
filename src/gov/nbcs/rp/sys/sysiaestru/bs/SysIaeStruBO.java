package gov.nbcs.rp.sys.sysiaestru.bs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * ��֧�ṹ�ӿ�ʵ��
 * 

 */
public class SysIaeStruBO implements ISysIaeStru {
	/**
	 * �ύ����
	 * 
	 * @param dataSet
	 */
	public void postDataSet(DataSet dataSet) throws Exception {
		dataSet.post();
	}

	/**
	 * �����ʾ��ʽ
	 */
	public DataSet getSFormate(int setYear) throws Exception {
		String sFormateSQL = " select distinct '' as name from fb_s_pubcode"
				+ " union all"
				+ " select Name from fb_s_pubcode "
				+ " where typeID='DQRDISPLAY' and cvalue='��С������ֵ' and  set_year=?"
				+ " Order By Name ";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(sFormateSQL);
		ds.open();
		return ds;
	}

	/**
	 * ��ñ༭��ʽ
	 */
	public DataSet getEFormate(int setYear) throws Exception {
		String eFormateSQL = "select distinct '' as name from fb_s_pubcode"
				+ " union all" + " select Name from fb_s_pubcode"
				+ " where typeID='DQREDIT' and cvalue='��С������ֵ' and set_year=?"
				+ " Order By Name";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(eFormateSQL);
		ds.open();
		return ds;
	}

	/**
	 * �������
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getKind(String typeID, String setYear) throws Exception {
		String sSql = "select code,name from fb_s_pubcode where typeId=?  and set_Year = ?  order by code";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new String(typeID),
				new Integer(setYear) });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * ���������Ŀ��
	 */
	public DataSet getIncColTre(int setYear) throws Exception {
		String incColSQL = "select lvl_id||' '||inccol_name as name ,a.*  from fb_iae_inccolumn a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(incColSQL);
		ds.open();
		return ds;
	}

	/**
	 * ���������Ŀ��,��ѡ��Ŀ
	 * 
	 * @return��¼��
	 * @throws Exception
	 */
	public DataSet getIncColTreCalc(int setYear, String sIncColCode)
			throws Exception {
		String sSql = "select inccol_code,inccol_fname,'' as par_id,inccol_ename "
				+ " from fb_iae_inccolumn where set_year=? and end_flag=1 and inccol_code<>?"
				+ " order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear), sIncColCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * �õ�������Ŀ���շ���Ŀ
	 * 
	 * @param setYear
	 *            ���
	 * @param sIncColCode
	 *            ������Ŀ����
	 * @return ����������Ŀ���շ���ĿDataSet
	 * @throws Exception
	 */

	public DataSet getInccolumnToToll(String setYear, String sIncColCode)
			throws Exception {
		String sSql = "select toll_code "
				+ " from fb_iae_inccolumn_to_toll where set_year=? and  inccol_code=? ";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { setYear, sIncColCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * ȡ������Ԥ��������ٸ�������Ŀ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getIncColIValue(String setYear) throws Exception {
		String sSql = "select IValue from fb_s_pubcode where TypeID = 'MAXFIELDNUM' and code='002' and set_Year="
				+ setYear;
		return DBSqlExec.getIntValue(sSql);
	}

	/**
	 * �ж��ܷ�ɾ��������Ŀ��¼
	 * 
	 * @param sENameӢ������
	 * @param sIncColCode������Ŀ����
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncColEnableDel(String sEName, String sIncColCode,
			String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// �жϸ�������Ŀ����Ϊ������Ŀ���㹫ʽ������,����ǣ�������ɾ��
		String Epq = "%{" + sEName + "}%";
		if (DBSqlExec.getRecordCount("fb_iae_inccolumn", "formula_Det like '"
				+ Epq + "' and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("��������Ŀ����Ϊ������Ŀ���㹫ʽ������,����ɾ��������Ŀ!");
			return infoPackage;
		}
		// �жϴ˼�¼�Ƿ��ѱ�����Ԥ���ʹ�ã�����ѱ�ʹ�ã�������ɾ��
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming", sEName
				+ ">0 and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ�ѱ�����Ԥ���ʹ��,����ɾ��!");
			return infoPackage;
		}
		// �ж�������Ŀ�����������Ŀ�Ƿ����˶�Ӧ��ϵ
		if (DBSqlExec.getRecordCount("fb_iae_inctype_to_incolumn",
				"IncCol_code ='" + sIncColCode + "' and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ�ѱ�������Ŀ������ö�Ӧ��ϵ,����ɾ��!");
			return infoPackage;
		}

		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж��ܷ��޸�������Ŀ��¼
	 * 
	 * @param sENameӢ������
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncColEnableModify(String sEName, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming", sEName
				+ ">0 and set_Year =" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ�ѱ�ʹ��,ֻ���޸���ʾ��ʽ!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �жϱ����Ƿ����,�Ҳ��ǲ�Ҷ�ӽᣬ�����Ҷ�ӽڵ�
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return��true��������Ҳ���Ҷ�ڵ�
	 */
	public InfoPackage judgeIncColParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_inccolumn", setYear);
	}

	/**
	 * �ж�������Ŀ��д�ı����Ƿ��ظ�
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :���ظ�,false �ظ�
	 * @throws Exception
	 */
	public InfoPackage judgeIncColCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilterA;
		if (!bModidy) { // ����
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// �޸�
			sFilterA = "lvl_id ='" + sLvlId + "' and inccol_code <> '" + sCode
					+ "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_inccolumn", sFilterA) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("�����Ѿ���ʹ��!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж�������Ŀ��д�������Ƿ��ظ�
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :���ظ�,false �ظ�
	 */
	public InfoPackage judgeIncColNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and INCCOL_NAME = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and INCCOL_NAME = '" + sName
					+ "' and set_year=" + setYear;
		}
		if (bModidy) {// �Ƿ����޸�,�ж��������ϲ������Լ�
			sFilter = sFilter + " and inccol_code != '" + sCode + "'";
		}

		if (DBSqlExec.getRecordCount("fb_iae_inccolumn", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ�����Ѿ���ʹ��!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * ����������Ŀ
	 * 
	 * @param dsIncCol������ĿDataSet
	 * @param sIncOldColCodeԭ������Ŀ����
	 * @param sIncColCode������Ŀ����
	 * @paramb AddFirstNode�Ƿ����ӵĵ�һ���ڵ�
	 * @param lstTollCode�շ���Ŀ�б�
	 * @param setYear���
	 * @throws Exception
	 */
	public void saveIncCol(DataSet dsIncCol, String sIncOldColCode,
			String sIncColCode, boolean bAddFirstNode, ArrayList lstTollCode,
			String setYear) throws Exception {
		// �õ�optMenuֵ
		String optMenu = UpdateStruPub.getOptMenu(dsIncCol, sIncColCode,
				IIncColumn.INCCOL_CODE, IIncColumn.INCCOL_NAME,
				IIncColumn.INCCOL_ROOT);

		// �õ�������Ŀ��OptSql,����postǰִ��
		String optSql = UpdateStruPub.getIncColFormulaOptSql(dsIncCol,
				sIncColCode);

		// ����������Ŀ����Ϣ
		dsIncCol.post();

		ArrayList lstSql = new ArrayList();
		String sSql = null;
		// ɾ��ԭ��Ӧ��ϵ
		if (!"".equals(sIncOldColCode) && sIncOldColCode != null) {
			sSql = "delete fb_iae_inccolumn_to_toll where incCol_code ='"
					+ sIncOldColCode + "' and set_year=" + setYear;
		}
		lstSql.add(sSql);
		// �¶�Ӧ��ϵ
		if (lstTollCode != null) {
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			for (int i = 0; i < lstTollCode.size(); i++) {
				sSql = "insert into fb_iae_inccolumn_to_toll(inccol_code,toll_code,set_year,rg_code) values('"
						+ sIncColCode
						+ "','"
						+ lstTollCode.get(i).toString()
						+ "'," + setYear + ",'" + sRgCode + "')";
				lstSql.add(sSql);
			}
		}
		// �Ƿ����ӵĵ�һ���ڵ㣬�����ӽ���������������Ŀ���Ķ�Ӧ��ϵ����ӽڵ�
		if (bAddFirstNode) {
			sSql = "update fb_iae_inctype_to_incolumn  set incCol_code ='"
					+ sIncColCode + "' where incCol_code ='" + sIncOldColCode
					+ "' and set_year=" + setYear;
			lstSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);

		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, optSql);
	}

	/**
	 * ɾ��������Ŀ
	 * 
	 * @param dsIncCol
	 *            ������ĿDataSet
	 * @param sIncColParCode
	 *            ���ڵ����
	 * @param sIncColCode
	 *            ������Ŀ����
	 * @param setYear���
	 * @param sIncColName������Ŀ����
	 * @param sIncColEname������Ŀename
	 * @throws Exception
	 */
	public void delIncCol(DataSet dsIncCol, String sIncColParCode,
			String sIncColCode, String setYear, String sIncColName,
			String sIncColEname) throws Exception {
		dsIncCol.post();
		String OptSql = "";
		String sSql = null;
		// sIncColParCode == null˵��ɾ���ڵ�ĸ��ڵ㲻����һ���ӽڵ�
		if (sIncColParCode == null) {
			// ɾ��ԭ��Ӧ��ϵ
			sSql = "delete fb_iae_inccolumn_to_toll where incCol_code ='"
					+ sIncColCode + "' and set_year=" + setYear;
			// ����fb_u_Div_Incoming��sIncColEname�ֶ�Ϊ��ֵ
			OptSql = UpdateStruPub.getUpdateToZeroSql("fb_u_Div_Incoming",
					sIncColEname);
		} else {// ����������Ŀ���룬���ӽڵ����շ���Ŀ�Ķ�Ӧ��ϵ����Ϊ���ڵ����շ���Ŀ
			sSql = "update fb_iae_inccolumn_to_toll set incCol_code ='"
					+ sIncColParCode + "' where incCol_code ='" + sIncColCode
					+ "' and set_year=" + setYear;
		}
		QueryStub.getQueryTool().executeUpdate(sSql);
		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IIncColumn.INCCOL_ROOT + "-" + sIncColName, OptSql);
	}

	/**
	 * ���֧���ʽ���Դ
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSTre(int setYear) throws Exception {
		String payOutFSSQL = "select lvl_id||' '||PFS_NAME as name ,a.*  from Fb_Iae_Payout_Fundsource a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(payOutFSSQL);
		ds.open();
		return ds;
	}

	/**
	 * ���֧���ʽ���Դ,��ѡ��Ŀ
	 * 
	 * @return��¼��
	 * @throws Exception
	 */
	public DataSet getPayOutFSCalc(int setYear, String sPfsCode)
			throws Exception {
		String sSql = "select pfs_code,pfs_fname,'' as par_id"
				+ " from Fb_Iae_Payout_Fundsource a where set_year=? and end_flag=1 and pfs_code <>?"
				+ " order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear), sPfsCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * ȡ��֧���ʽ���Դ�������ٸ���Ŀ��
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public int getPayOutFsIValue(int setYear) throws Exception {
		String sSql = "select IValue from fb_s_pubcode where TypeID = 'MAXFIELDNUM' and code='001' and set_Year="
				+ setYear;
		return DBSqlExec.getIntValue(sSql);
	}

	/**
	 * ����ʽ���Դ�Ƿ�ʹ�� 0δ��ʹ��1�����ã�����ṹ��2��ʹ�ã�¼�����ݣ�3�ȱ������ֱ�ʹ�á�
	 * 
	 * @param iSetYear
	 * @param sPfsCode
	 * @param sPfsField
	 * @return
	 */
	public InfoPackage chkFundSourceByRef(String setYear, String sPfsCode,
			String sPfsField) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		infoPackage.setSuccess(true);
		int iCiteFlag = 0;// ���ñ�־
		int iUseFlag = 0; // ʹ�ñ�־
		// �ж��Ƿ���"֧����Ŀ��Ԫ��ʽ�������Ԫ�ر�","�ʽ���Դ��������Ŀ��","֧����Ŀ��Ԫ��ʽ��"�Ƿ�����
		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_Formula",
				"FFieldName='" + sPfsField + "' and set_year=" + setYear) > 0) {
			iCiteFlag = 1;
			infoPackage.setsMessage(infoPackage.getsMessage() + "�ѱ�\""
					+ getTableChinaName("FB_IAE_PAYOUT_CELL_FORMULA")
					+ "\"ʹ�ã�\n");
			infoPackage.setSuccess(false);
		}

		if (DBSqlExec.getRecordCount("fb_Iae_Pfs_To_IncItem", "PFS_Code='"
				+ sPfsField + "' and set_year=" + setYear) > 0) {
			iCiteFlag = 1;
			infoPackage.setsMessage(infoPackage.getsMessage() + "�ѱ�\""
					+ getTableChinaName("FB_IAE_PFS_TO_INCITEM") + "\"ʹ�ã�\n");
			infoPackage.setSuccess(false);
		}
		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_RefElet",
				"FFieldName='" + sPfsField + "' and set_year=" + setYear) > 0) {
			iCiteFlag = 1;
			infoPackage.setsMessage(infoPackage.getsMessage() + "�ѱ�\""
					+ getTableChinaName("FB_IAE_PAYOUT_CELL_REFELET")
					+ "\"ʹ�ã�\n");
			infoPackage.setSuccess(false);
		}
		// ���ʽ���Դ�йصı�
		DataSet ds = getFundSouceDataTable(setYear);
		ds.beforeFirst();
		while (ds.next()) {
			if (DBSqlExec.getRecordCount(ds.fieldByName("name").getString(),
					sPfsField + ">0 and set_year=" + setYear) > 0) {
				iUseFlag = 2;
				infoPackage.setsMessage(infoPackage.getsMessage()
						+ "�ѱ�\""
						+ getTableChinaName(ds.fieldByName("name").getString()
								.toUpperCase()) + "\"���ã�\n");
				infoPackage.setSuccess(false);
				break;
			}
		}
		infoPackage.setObject(new Integer(iCiteFlag + iUseFlag));
		return infoPackage;
	}

	/**
	 * ���ݱ��Ӣ�����ƻ�ñ����������
	 * 
	 * @param sTableName
	 * @return
	 * @throws Exception
	 */
	private String getTableChinaName(String sTableName) throws Exception {
		String sSql = " select comments from all_tab_comments where table_name = '"
				+ sTableName.toUpperCase() + "'";
		return DBSqlExec.getStringValue(sSql);
	}

	/**
	 * �����֧���ʽ���Դ�йصı���Ϣ
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private DataSet getFundSouceDataTable(String setYear) throws Exception {
		// ���ʽ���Դ�йصı�
		String sSql = "Select Name From fb_s_PubCode Where set_Year=" + setYear
				+ " And TypeID = 'PFSDATATABLE' Order By lvl_id";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * ���֧����Ŀ���
	 * 
	 * @param settYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutKind(int setYear) throws Exception {
		String sSQL = "select lvl_id||' '||PAYOUT_KIND_NAME as name ,a.*  from fb_iae_payout_kind a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(sSQL);
		ds.open();
		return ds;
	}

	/**
	 * ����ɾ�����޸��ʽ���Դ���Ӧ��������Ϣ
	 * 
	 * @param setYear
	 * @param ASrcPfsCodeԴ�ʽ���Դ����
	 * @param ATagPfsCodeĿ���ʽ���Դ���룬�����޸��ʽ���Դ����ʱ,ɾ��ʱ�ò���Ϊ��.
	 * @param APfsField�ʽ���Դ��Ӧ���ֶΡ�
	 * @param AOptType�������ͣ�1ɾ��2�޸ġ�
	 * @throws Exception
	 */
	private void ModFundSourceRefInfo(String setYear, String ASrcPfsCode,
			String ATagPfsCode, String APfsField, int AOptType)
			throws Exception {
		if (AOptType == 1) { // ɾ��
			// ֧����Ŀ��ʽ�Ե�λ��
			String sSql = "Select distinct Formula_ID,Payout_Item_Code "
					+ " From fb_Iae_PayOut_Formula_To_Div Where set_Year=? and FFieldName =?";
			DataSet ds = DataSet.create();
			ds.setQueryParams(new Object[] { new Integer(setYear), APfsField });
			ds.setSqlClause(sSql);
			ds.open();

			InfoPackage infoPackage = new InfoPackage();
			ds.beforeFirst();
			while (ds.next()) {
				if (!PubUntSys.unReg_Div_Formula(setYear, "", ds.fieldByName(
						"Formula_ID").getString(), APfsField, ds.fieldByName(
						"Payout_Item_Code").getString(), -1, -1, infoPackage))
					return;
			}

			ArrayList lstSql = new ArrayList();
			// ֧����Ŀ��Ԫ��ʽ��
			sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
					+ String.valueOf(setYear) + " and FFieldName ='"
					+ APfsField + "'";
			lstSql.add(sSql);
			// �ʽ���Դ��������Ŀ��
			sSql = "Delete From fb_Iae_Pfs_To_IncItem  Where set_Year="
					+ String.valueOf(setYear) + " and Pfs_Code='" + ASrcPfsCode
					+ "'";
			lstSql.add(sSql);
			// ֧����Ŀ��Ԫ��ʽ��
			sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
					+ String.valueOf(setYear) + " and FFieldName ='"
					+ APfsField + "'";
			lstSql.add(sSql);

			QueryStub.getQueryTool().executeBatch(lstSql);
		} else if (AOptType == 2) { // �޸�
			// �ʽ���Դ��������Ŀ��
			String sSql = "Update fb_Iae_Pfs_To_IncItem  Set Pfs_Code='"
					+ ATagPfsCode + "' Where set_Year="
					+ String.valueOf(setYear) + " and Pfs_Code ='"
					+ ASrcPfsCode + "'";
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

	}

	/**
	 * ɾ���ʽ���Դ������һ���������ύ����������
	 * 
	 * @param setYear
	 * @param sSrcPfsCodeԴ�ʽ���Դ����
	 * @param sTagPfsCodeĿ���ʽ���Դ���룬�����޸��ʽ���Դ����ʱ,ɾ��ʱ�ò���Ϊ��.
	 * @param sPfsField�ʽ���Դ��Ӧ���ֶΡ�
	 * @param sOptType�������ͣ�1ɾ��2�޸ġ�
	 * @param dataSet,�ύ�����ݼ�
	 * @param iClearFlag,true:������
	 *            ClearFundSourceData����������ʽ���Դ��Ϣ;false������
	 * @param sPfsName����
	 * @return
	 */
	public void delFundSource(String setYear, String sSrcPfsCode,
			String sTagPfsCode, String sPfsField, int iOptType,
			DataSet dataSet, boolean iClearFlag, String sPfsName)
			throws Exception {
		// �������ҵ����Ӧ�ֶε�ֵΪ��
		List lstOptSql = null;
		if (iOptType == 1) {
			// �õ����ʽ���Դ�ṹ�йصı�
			DataSet ds = getFundSouceDataTable(setYear);
			lstOptSql = UpdateStruPub.getUpdateToZeroSql(ds, sPfsField);
		}

		if (sTagPfsCode == null || "".equals(sTagPfsCode)) {// ɾ���ڵ�
			String sSql = "delete from fb_iae_pfs_to_acctcode where pfs_code = '"
					+ sSrcPfsCode + "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		} else {// ɾ���Ľڵ���Ϣת�������ڵ�
			String sSql = "update fb_iae_pfs_to_acctcode set pfs_code ='"
					+ sTagPfsCode + "' where pfs_code = '" + sSrcPfsCode
					+ "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		}
		ModFundSourceRefInfo(setYear, sSrcPfsCode, sTagPfsCode, sPfsField,
				iOptType);
		dataSet.post();

		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IPayOutFS.PFS_ROOT + "-" + sPfsName, lstOptSql);
	}

	/**
	 * ���������Ŀ���
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getIncTypeTre(int setYear) throws Exception {
		String sSQL = "select lvl_id||' '||INCTYPE_NAME as name ,a.*  from fb_iae_inctype a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(sSQL);
		ds.open();
		return ds;
	}

	/**
	 * �ж�������Ŀ����Ƿ���ɾ��
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableDel(String sCode, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// // �õ�isInc�ֶ�ֵ
		// int isInc = DBSqlExec
		// .getIntValue(
		// "select is_inc from fb_iae_inctype where inctype_code =? and
		// set_year=?",
		// new Object[] { sCode, setYear });

		// �ж��Ƿ��֧��Ԥ���ȡ��
		// if (isInc != 2) {
		// �ж��Ƿ��ʽ���Դ��������Ŀ��ʹ�ò���
		if (DBSqlExec.getRecordCount("fb_Iae_Pfs_To_IncItem", "IncType_Code='"
				+ sCode + "' and set_Year = " + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ����ѱ��ʽ���Դ��������Ŀ��ʹ��");
			return infoPackage;
		}
		// }
		// �ж��Ƿ�����Ԥ���ʹ��
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming_budget",
				"IncType_Code='" + sCode + "' and INC_MONEY<>0 and set_Year = "
						+ setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ����ѱ�����Ԥ���ʹ��");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж�������Ŀ����Ƿ����޸�
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableModify(String sCode, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// �ж��Ƿ�����Ԥ���ʹ��
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming_budget",
				"IncType_Code='" + sCode + "' and INC_MONEY<>0 and set_Year = "
						+ setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ����ѱ�����Ԥ���ʹ�ã�ֻ���޸ı���!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж�������Ŀ�����д�ı����Ƿ��ظ�
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :���ظ�,false �ظ�
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilterA;
		if (!bModidy) { // ����
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// �޸�
			sFilterA = "lvl_id ='" + sLvlId + "' and inctype_code <> '" + sCode
					+ "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_incType", sFilterA) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("�����Ѿ���ʹ��!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж�������Ŀ�����д�������Ƿ��ظ�
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :���ظ�,false �ظ�
	 */
	public InfoPackage judgeIncTypeNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and inctype_name = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and inctype_name = '" + sName
					+ "' and set_year=" + setYear;
		}
		if (bModidy) {// �Ƿ����޸�,�ж��������ϲ������Լ�
			sFilter = sFilter + " and inctype_code != '" + sCode + "'";
		}

		if (DBSqlExec.getRecordCount("fb_iae_inctype", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("������Ŀ��������Ѿ���ʹ��!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж�������Ŀ�������Ƿ����,�Ҳ��ǲ�Ҷ�ӽᣬ�����Ҷ�ӽڵ�
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return��true��������Ҳ���Ҷ�ڵ�
	 */
	public InfoPackage judgeIncTypeParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_inctype", setYear);
	}

	/**
	 * ɾ��������Ŀ���
	 * 
	 * @param dataSet
	 * @param sCode,��ǰ��lvl���� *
	 * @param sParCode,��ǰ�ڵ㸸�ڵ����
	 * @param setYear
	 * @param sIncTypeName
	 * 
	 */
	public void delIncType(DataSet dataSet, String sCode, String sParCode,
			String setYear, String sIncTypeName) throws Exception {
		// ɾ��fb_u_Div_Incoming_budget����PAYOUT_KIND_CODE=sCode��¼
		String OptSql = UpdateStruPub.getClearDataSql(
				"fb_u_Div_Incoming_budget", sCode, IIncType.INCTYPE_CODE);

		// �����ڵ���������Ŀ�Ķ�Ӧ��ϵ���ø����ڵ�
		if (!"".equals(sParCode) && sParCode != null) {
			String sSql = "update fb_iae_inctype_to_incolumn set inctype_code ='"
					+ sParCode
					+ "' where inctype_code = '"
					+ sCode
					+ "' and set_year=" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
			sSql = "update fb_iae_pfs_to_incitem set inctype_code ='"
					+ sParCode + "' where inctype_code = '" + sCode
					+ "' and set_year=" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		} else {
			// DataSet dsPfsToInc = this.getPfsToIncCode(sCode, setYear);
			// if (dsPfsToInc != null && !dsPfsToInc.isEmpty()) {
			// dsPfsToInc.beforeFirst();
			// dsPfsToInc.next();
			// String sPfsCode = dsPfsToInc.fieldByName(IPayOutFS.PFS_CODE)
			// .getString();
			// // ��ĳ����֧����������ĳ��֧���ʽ���Դ�����Ϊ��
			// if (!Common.isNullStr(sPfsCode))
			// updateFundsourceIncZero(sPfsCode, setYear);
			// }

			// ɾ����Ӧ��ϵ
			String sSql = "delete fb_iae_inctype_to_incolumn where inctype_code ='"
					+ sCode + "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
			// ɾ����Ӧ��ϵ
			sSql = "delete fb_iae_pfs_to_incitem where inctype_code ='" + sCode
					+ "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);

		}
		// �ύ����
		dataSet.post();
		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IIncType.INCTYPE_ROOT + "-" + sIncTypeName, OptSql);
	}

	/**
	 * ��ĳ����֧����������ĳ��֧���ʽ���Դ�����Ϊ�㡣
	 * 
	 * @param sPfsCode
	 * @param setYear
	 * @throws Exception
	 */
	// private void updateFundsourceIncZero(String sPfsCode, String setYear)
	// throws Exception {
	// String sSql = "UPDATE fb_u_Div_FundSource_Inc SET " + sPfsCode
	// + "=0.00 where set_year=" + setYear;
	// QueryStub.getQueryTool().execute(sSql);
	// }
	/**
	 * ����������Ŀ���
	 * 
	 * @param dsIncType
	 *            ������Ŀ������ݼ�
	 * @param sOldIncTypeCode
	 *            ������Ŀ���ԭ����
	 * @param sIncTypeCode
	 *            ������Ŀ����±���
	 * @param dsInccolumnToInc
	 *            ѡ�е�������Ŀ�б�����Ԥ���Ŀ���շ���Ŀ
	 * @param setYear���
	 * @param sInctypeName
	 *            ������Ŀ������ƣ��޸����ƴ�ֵ��������null
	 * @param lstPfsCode
	 *            ֧���ʽ���Դ
	 * @throws Exception
	 */
	public void saveIncType(DataSet dsIncType, String sOldIncTypeCode,
			String sIncTypeCode, DataSet dsInccolumnToInc, String setYear,
			String sInctypeName, List lstPfsCode) throws Exception {
		DataSet dsOldPfsToInc = null;
		// ��Ӧ��֧���ʽ���Դ���룬��һһ��Ӧ
		String sPfsCode = null;
		if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
			sPfsCode = lstPfsCode.get(0).toString();
		}
		// �õ�optMenuֵ
		String optMenu = UpdateStruPub.getOptMenu(dsIncType, sIncTypeCode,
				IIncType.INCTYPE_CODE, IIncType.INCTYPE_NAME,
				IIncType.INCTYPE_ROOT);

		// ������Ŀ������ݼ��ύ����
		dsIncType.post();

		String sSql = null;
		// ɾ��ԭ��Ӧ��ϵ
		if (!"".equals(sOldIncTypeCode) && sOldIncTypeCode != null) {
			if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
				// �õ�ԭ������Ŀ��֧���ʽ���Դԭ��Ӧ��ϵ
				dsOldPfsToInc = getPfsToIncCode(sOldIncTypeCode, setYear);

				// ɾ��������Ŀ���ʽ���Դ�Ķ�Ӧ��ϵ����������Ŀ����,�������ʽ���Դ������
				sSql = "delete fb_iae_pfs_to_incitem where inctype_code ='"
						+ sOldIncTypeCode
						+ "' and pfs_code in ( select pfs_code from fb_iae_payout_fundsource where data_source=0 and set_year="
						+ setYear + ") and set_year=" + setYear;
				QueryStub.getQueryTool().execute(sSql);

				// ɾ��������Ŀ���ʽ���Դ�Ķ�Ӧ��ϵ�����ʽ���Դ����
				sSql = "delete fb_iae_pfs_to_incitem where pfs_code ='"
						+ sPfsCode + "' and set_year=" + setYear;
				QueryStub.getQueryTool().execute(sSql);
			}

			sSql = "delete fb_iae_inctype_to_incolumn where inctype_code ='"
					+ sOldIncTypeCode + "' and set_year=" + setYear;
			QueryStub.getQueryTool().execute(sSql);

		}

		// ������Ŀ�¶�Ӧ��ϵ
		if (dsInccolumnToInc != null && !dsInccolumnToInc.isEmpty()) {
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			dsInccolumnToInc.beforeFirst();
			while (dsInccolumnToInc.next()) {
				sSql = "insert into fb_iae_inctype_to_incolumn(INCTYPE_CODE,INCCOL_CODE,TOLL_FILTER,set_year,rg_code) values(?,?,?,?,?)";
				QueryStub.getQueryTool().executeUpdate2(
						sSql,
						new Object[] {
								sIncTypeCode,
								dsInccolumnToInc.fieldByName(
										IIncColumn.INCCOL_CODE).getString(),
								dsInccolumnToInc.fieldByName(
										IIncType.TOLL_FILTER).getString(),
								setYear, sRgCode });
			}
		}

		// ֧���ʽ���Դ�¶�Ӧ��ϵ
		if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
			List lstSql = new ArrayList();
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			for (Iterator it = lstPfsCode.iterator(); it.hasNext();) {
				String pfsCode = it.next().toString();
				// ɾ��ѡ�е��ʽ���Դ��������Ŀ�Ķ�Ӧ��ϵ
				sSql = "delete from  fb_iae_pfs_to_incitem where pfs_code='"
						+ pfsCode + "' and set_year=" + setYear;
				lstSql.add(sSql);

				sSql = "insert into fb_iae_pfs_to_incitem(PFS_CODE,INCTYPE_CODE,set_year,rg_code) values('"
						+ pfsCode
						+ "','"
						+ sIncTypeCode
						+ "',"
						+ setYear
						+ ",'" + sRgCode + "')";
				lstSql.add(sSql);
			}
			QueryStub.getQueryTool().executeBatch(lstSql);
		}

		// ԭ��Ӧ�ʽ���Դ
		if (!"".equals(sOldIncTypeCode) && sOldIncTypeCode != null) {
			// ���¼���ԭ֧����Դ
			if (dsOldPfsToInc != null && !dsOldPfsToInc.isEmpty()) {
				int iBatchNo = 0;
				dsOldPfsToInc.beforeFirst();
				while (dsOldPfsToInc.next()) {
					String oldPfsCode = dsOldPfsToInc.fieldByName(
							IPayOutFS.PFS_CODE).getString();
					// ԭ��Ӧ��ϵ������ѡ���֧���ʽ���Դ�����ظ�����
					if (oldPfsCode.equals(sPfsCode)) {
						continue;
					}
					String sSqlDet = this.getSqlDetValue(oldPfsCode, setYear);
					// ����ԭ֧���ʽ���ԴSql_DET�ֶ�ֵ
					sSql = "update fb_iae_payout_fundsource set sql_det=? where pfs_code = ? and set_year=?";
					QueryStub.getQueryTool().executeUpdate2(sSql,
							new Object[] { sSqlDet, oldPfsCode, setYear });
					// ���¼���
					calc_Div_FundSource_Inc(setYear, null, iBatchNo, -1,
							oldPfsCode);
				}
			}
		}
		// �¶�Ӧ�ʽ���Դ
		if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
			// ��д֧���ʽ���ԴSQL_DET�ֶ�ֵ
			updateSqlDet(sIncTypeCode, lstPfsCode, setYear);
			// ���¼���
			int iBatchNo = 0;
			calc_Div_FundSource_Inc(setYear, null, iBatchNo, -1, sPfsCode);
		}

		// ����fb_u_Div_Incoming_budget����
		String optSql = null;
		if (sInctypeName != null) {
			optSql = "update fb_u_Div_Incoming_budget set inctype_name = ''"
					+ sInctypeName + "'' where inctype_code = ''"
					+ sIncTypeCode + "'' and set_year = " + setYear;
		}

		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, optSql);
	}

	/**
	 * ����fb_iae_payout_fundsource����sql_detֵ
	 * 
	 * @param sIncTypeCode
	 * @param lstPfsCode
	 * @param setYear
	 * @throws Exception
	 */
	private void updateSqlDet(String sIncTypeCode, List lstPfsCode,
			String setYear) throws Exception {
		// ��Ӧ��֧���ʽ���Դ���룬��һһ��Ӧ
		String pfsCode = lstPfsCode.get(0).toString();
		String sqlDet = "(INCTYPE_CODE = '" + sIncTypeCode + "') ";
		String sSql = "update fb_iae_payout_fundsource set sql_det=? where pfs_code = ? and set_year=?";
		QueryStub.getQueryTool().executeUpdate2(sSql,
				new Object[] { sqlDet, pfsCode, setYear });
	}

	/**
	 * ����֧���ʽ���Դ���룬�õ���Ӧ��������Ŀ����֯fb_iae_payout_fundsource����sql_det�ֶ�ֵ
	 * 
	 * @param sPfsCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private String getSqlDetValue(String sPfsCode, String setYear)
			throws Exception {
		DataSet dsPfsCodeToInc = this.getIncWithPfsCode(sPfsCode, setYear);
		if (dsPfsCodeToInc == null && dsPfsCodeToInc.isEmpty()) {
			return "";
		}
		String sSqlIncTypeCode = "";
		dsPfsCodeToInc.beforeFirst();
		while (dsPfsCodeToInc.next()) {
			String sInctypeCode = dsPfsCodeToInc.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			if ("".equals(sSqlIncTypeCode) || sSqlIncTypeCode == null)
				sSqlIncTypeCode = "INCTYPE_CODE = '" + sInctypeCode + "'";
			else
				sSqlIncTypeCode = sSqlIncTypeCode + " OR INCTYPE_CODE = '"
						+ sInctypeCode + "'";
		}
		if (!"".equals(sSqlIncTypeCode)) {
			sSqlIncTypeCode = "(" + sSqlIncTypeCode + ")";
		}
		return sSqlIncTypeCode;
	}

	/**
	 * ����������Ŀ�����룬�õ�������Ŀ�����������Ŀ�Ķ�Ӧ��ϵ
	 * 
	 * @param setYear���
	 * @param sIncTypeCode������Ŀ������
	 * @return
	 * @throws Exception
	 */
	public DataSet getInctypeToIncolumn(String setYear, String sIncTypeCode)
			throws Exception {
		String sSql = "select INCTYPE_CODE,INCCOL_CODE,TOLL_FILTER"
				+ " from fb_iae_inctype_to_incolumn  "
				+ " where inctype_code = ? and set_year=? order by inccol_code";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { sIncTypeCode, setYear });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;

	}

	/**
	 * ɾ��֧����Ŀ���
	 * 
	 * @param dataSet
	 * @param sCode
	 * @param sParCode
	 * @param setYear
	 * @throws Exception
	 */
	public void delPayOutKind(DataSet dataSet, String sCode, String sParCode,
			String sParName, String sParParId, String setYear,
			String sPayoutKindName) throws Exception {
		// ɾ������PAYOUT_KIND_CODE=sCode��¼
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		List lstOptSql = UpdateStruPub.getClearDataSql(dsTable, sCode,
				IPayOutType.PAYOUT_KIND_CODE);

		String sSql;
		// �ύ����
		dataSet.post();
		if ("".equals(sParCode)) {// ���ڵ��������������������϶��ӣ�ɾ���ڵ���Ϣ
			// ֧����Ŀ��Ԫ��ʽ��
			sSql = " select distinct Formula_ID,FFieldName,payout_Item_code From fb_Iae_PayOut_Formula_To_Div where payout_Item_code='"
					+ sCode
					+ "' and payout_item_kind=0 and set_Year = "
					+ setYear
					+ " union all "
					+ " select distinct Formula_ID,FFieldName,payout_Item_code From  fb_Iae_PayOut_Formula_To_Div "
					+ " where payout_Item_code in (select Acct_code_jj from fb_Iae_PayOut_Kind_To_JJ where  payout_kind_code='"
					+ sCode
					+ "' and Set_Year = "
					+ setYear
					+ " ) and payout_item_kind=1 and par_id ='"
					+ sCode
					+ "' and set_Year = " + setYear;
			DataSet ds = DataSet.create();
			ds.setSqlClause(sSql);
			ds.open();

			InfoPackage infoPackage = new InfoPackage();
			ds.beforeFirst();
			while (ds.next()) {
				if (!PubUntSys.unReg_Div_Formula(setYear, "", ds.fieldByName(
						"Formula_ID").getString(), ds.fieldByName("FFieldName")
						.getString(), ds.fieldByName("payout_Item_code")
						.getString(), -1, -1, infoPackage))
					return;
			}

			ArrayList lstSql = new ArrayList();
			// ֧����Ŀ��Ԫ��ʽ��
			sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
					+ String.valueOf(setYear) + " and payout_Item_code ='"
					+ sCode + "' and payout_item_kind=0";
			lstSql.add(sSql);
			sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
					+ String.valueOf(setYear) + " and par_id ='" + sCode
					+ "' and payout_item_kind=1";
			lstSql.add(sSql);
			// ֧����Ŀ��Ԫ��ʽ��
			sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
					+ String.valueOf(setYear) + " and payout_Item_code ='"
					+ sCode + "' and payout_item_kind=0";
			lstSql.add(sSql);
			sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
					+ String.valueOf(setYear) + " and par_id ='" + sCode
					+ "' and payout_item_kind=1";
			lstSql.add(sSql);
			QueryStub.getQueryTool().executeBatch(lstSql);

			// ֧����Ŀ���Ծ��ÿ�Ŀ
			sSql = "delete from  fb_Iae_PayOut_Kind_To_JJ where payout_Kind_code= '"
					+ sCode + "'  and Set_Year = " + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);

		} else {// ���ڵ�ֻ��һ�����ӣ���������Ϣ�������ڵ�
			// �޸���ر�Ķ�Ӧ��ϵ
			updateFormula(sParCode, sParParId, sCode, setYear);
			// ֧����Ŀ���Ծ��ÿ�Ŀ
			sSql = "update  fb_Iae_PayOut_Kind_To_JJ set Payout_kind_code='"
					+ sParCode + "',Payout_kind_name='" + sParName
					+ "' where PayOut_Kind_Code='" + sCode
					+ "' and set_Year = " + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IPayOutType.PAYOUTKIND_ROOT + "-" + sPayoutKindName,
				lstOptSql);
	}

	/**
	 * ����֧����Ŀ���
	 * 
	 * @param dataSet
	 * @throws Exception
	 */
	public void savePayOutKind(DataSet dataSet, DataSet dsPayoutKindToJj,
			String sSaveType, String sPayoutKindCode, String sPayOutKindName,
			String setYear, String sParCode) throws Exception {
		// �õ�optMenuֵ
		String optMenu = UpdateStruPub.getOptMenu(dataSet, sPayoutKindCode,
				IPayOutType.PAYOUT_KIND_CODE, IPayOutType.PAYOUT_KIND_NAME,
				IPayOutType.PAYOUTKIND_ROOT);

		List optSql = null;

		if (dataSet.fieldByName(IPayOutType.PAYOUT_KIND_NAME).getOldValue() != null) {
			String sOldPayoutKindName = dataSet.fieldByName(
					IPayOutType.PAYOUT_KIND_NAME).getOldValue().toString();
			if (!sPayOutKindName.equals(sOldPayoutKindName)) {
				if (optSql == null)
					optSql = new ArrayList();
				optSql.addAll(getUpdatePayouKindName(sPayoutKindCode,
						sPayOutKindName, setYear));
			}
		}

		// �ύ����
		dataSet.post();
		String sSql;

		// �����ӵ��ǵ�һ���ӽڵ㣬�޸ĸ��ڵ㲿����Ϣ
		if ("addfirstson".equals(sSaveType)) {
			// �޸���ر�Ķ�Ӧ��ϵ
			updateFormula(sPayoutKindCode, sParCode, sParCode, setYear);
		}

		String sCode = null;
		if ("addfirstson".equals(sSaveType)) {
			sCode = sParCode;
		} else if ("mod".equals(sSaveType) || "modformate".equals(sSaveType)) {
			sCode = sPayoutKindCode;
		}
		if ("addfirstson".equals(sSaveType) || "mod".equals(sSaveType)
				|| "modformate".equals(sSaveType)) {
			// ��ѯԭ��֧����Ŀ�Ծ��ÿ�Ŀ��Ӧ��ϵ
			DataSet dsPayOutKindToJJOld = DataSet.create();
			sSql = "select * from fb_iae_payout_kind_to_jj  where payout_kind_code='"
					+ sCode + "' and set_Year = " + setYear;
			DBSqlExec.getDataSet(sSql, dsPayOutKindToJJOld);
			// �ж�ԭ֧����Ŀ�Ծ��ÿ�Ŀ��Ӧ��ϵ����֧����Ŀ�Ծ��ÿ�Ŀ��Ӧ��ϵ�Ƿ���ڣ�
			// �粻���ڣ�ɾ��fb_Iae_PayOut_Formula_To_Div���еľ��ÿ�Ŀ��Ӧ��ϵ
			InfoPackage infoPackage = new InfoPackage();
			dsPayOutKindToJJOld.beforeFirst();
			// boolean bEquals = false;
			String sPayoutItemCode;
			while (dsPayOutKindToJJOld.next()) {
				String sBSI_ID = dsPayOutKindToJJOld.fieldByName(
						IPubInterface.BSI_ID).getString();
				// modify by qzc 2009,6,4
				if (dsPayoutKindToJj != null
						&& !dsPayoutKindToJj.isEmpty()
						&& !dsPayoutKindToJj.locate(IPubInterface.BSI_ID,
								sBSI_ID)) {
					sSql = " select distinct Formula_ID,FFieldName,payout_Item_code From  fb_Iae_PayOut_Formula_To_Div "
							+ " where bsi_id='"
							+ sBSI_ID
							+ "' and Set_Year = "
							+ setYear
							+ " and payout_item_kind=1 and set_Year = "
							+ setYear + " and par_id ='" + sCode + "'";
					DataSet ds = DataSet.create();
					DBSqlExec.getDataSet(sSql, ds);
					ds.beforeFirst();
					while (ds.next()) {
						if (!PubUntSys.unReg_Div_Formula(setYear, "", ds
								.fieldByName("Formula_ID").getString(), ds
								.fieldByName("FFieldName").getString(), ds
								.fieldByName("payout_Item_code").getString(),
								-1, -1, infoPackage))
							return;
					}

					sPayoutItemCode = dsPayOutKindToJJOld.fieldByName(
							"acct_code_jj").getString();
					ArrayList lstSql = new ArrayList();
					// ֧����Ŀ��Ԫ��ʽ��
					sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
							+ String.valueOf(setYear)
							+ " and par_id ='"
							+ sCode
							+ "' and payout_item_kind=1 and payout_Item_code='"
							+ sPayoutItemCode + "'";
					lstSql.add(sSql);
					// ֧����Ŀ��Ԫ��ʽ��
					sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
							+ String.valueOf(setYear)
							+ " and par_id ='"
							+ sCode
							+ "' and payout_item_kind=1 and payout_Item_code='"
							+ sPayoutItemCode + "'";
					lstSql.add(sSql);
					QueryStub.getQueryTool().executeBatch(lstSql);

					// ȡ�����ÿ�Ŀ��֧����Ŀ����Ӧ��ϵ��ɾ�����ҵ��������ݣ�ע��ֻɾ���������ε�ҵ������
					if (optSql == null)
						optSql = new ArrayList();
					List lstUpdateScript = clearPayOutKindAcctJjData(sCode,
							sPayoutItemCode, setYear, -1, -1);
					optSql.addAll(lstUpdateScript);

				}
			}
			// ɾ��֧����Ŀ����뾭�ÿ�Ŀ�Ķ�Ӧ��ϵ
			sSql = "delete from  fb_Iae_PayOut_Kind_To_JJ where payout_kind_code='"
					+ sCode + "' and set_Year = " + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

		// ����֧����Ŀ����뾭�ÿ�Ŀ��Ӧ��ϵ
		if (dsPayoutKindToJj != null) {
			dsPayoutKindToJj.setName("fb_Iae_PayOut_Kind_To_JJ");
			dsPayoutKindToJj.post();
		}

		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, optSql);
	}

	/**
	 * ȡ�����ÿ�Ŀ��֧����Ŀ����Ӧ��ϵ��ɾ�����ҵ���������
	 * 
	 * @param payoutKindCode֧����Ŀ���
	 * @param acctJjCode���ÿ�Ŀ
	 * @param setYear���
	 * @return
	 * @throws Exception
	 */
	private List clearPayOutKindAcctJjData(String payoutKindCode,
			String acctJjCode, String setYear, int iBatchNo, int iDataType)
			throws Exception {
		if (Common.isNullStr(payoutKindCode) || Common.isNullStr(acctJjCode)) {
			return null;
		}

		String filter;
		List lstSql = new ArrayList();
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		while (dsTable.next()) {
			filter = " PayOut_Kind_Code=''" + payoutKindCode
					+ "'' and ACCT_CODE_JJ = ''" + acctJjCode + "''";
			// ����
			String tableName = dsTable.fieldByName("name").getString();
			// �ж�����ֵ�����Ƿ���ڴ��ֶ�
			if (iBatchNo != -1
					&& QueryStub.getQueryTool().getColumnInfo(tableName)
							.contains("batch_no")) {
				filter = filter + " and Batch_No = " + iBatchNo;
			}
			// �ж���������ֵ�����Ƿ���ڴ��ֶ�
			if (iDataType != -1
					&& QueryStub.getQueryTool().getColumnInfo(tableName)
							.contains("data_type")) {
				filter = filter + " and Data_Type=" + iDataType;
			}
			filter = filter + " and set_Year = " + setYear;
			String sSql = "delete from " + tableName + " where " + filter;
			lstSql.add(sSql);
		}
		return lstSql;
	}

	/**
	 * �õ�֧����Ŀ����������Ӱ��ҵ�����ݱ��sql�������
	 * 
	 * @param payoutKindCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private List getUpdatePayouKindName(String payoutKindCode,
			String payoutKindName, String setYear) throws Exception {
		String filter;
		List lstSql = new ArrayList();
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		while (dsTable.next()) {
			filter = " PayOut_Kind_Code=''" + payoutKindCode + "''";
			// ����
			String tableName = dsTable.fieldByName("name").getString();
			filter = filter + " and set_Year = " + setYear;
			String sSql = "update  " + tableName + " set payout_kind_name =''"
					+ payoutKindName + "'' where " + filter;
			lstSql.add(sSql);
		}
		return lstSql;
	}

	/**
	 * �޸�֧����Ŀ������룬�޸��йر�����Ӧ�Ķ�Ӧ��ϵ
	 * 
	 * @param sNewPayoutKindCode�µı���
	 * @param sNewParCode�µı���ĸ�����
	 * @param sOldPayoutKindCodeԭ����
	 * @param setYear
	 * @throws Exception
	 */
	private void updateFormula(String sNewPayoutKindCode, String sNewParCode,
			String sOldPayoutKindCode, String setYear) throws Exception {
		ArrayList lstSql = new ArrayList();
		// ֧����Ŀ��ʽֵ��
		// String sSql = " update fb_u_Div_PayOut_Formula_Value set
		// Payout_Item_code='"
		// + sNewPayoutKindCode
		// + "',Par_ID='"
		// + sNewParCode
		// + "' where Payout_Item_code='"
		// + sOldPayoutKindCode
		// + "' and payout_item_kind=0 and set_Year = " + setYear;
		// lstSql.add(sSql);
		// sSql = " update fb_u_Div_PayOut_Formula_Value set par_id='"
		// + sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
		// + "' and payout_item_kind=1 and set_Year = " + setYear;
		// lstSql.add(sSql);

		// ֧����Ŀ��ʽ�Ե�λ��
		String sSql = " update  fb_iae_payout_formula_to_div set Payout_Item_code='"
				+ sNewPayoutKindCode
				+ "',Par_ID='"
				+ sNewParCode
				+ "',Calc_Flag=1 where Payout_Item_code='"
				+ sOldPayoutKindCode
				+ "' and payout_item_kind=0 and set_Year = " + setYear;
		lstSql.add(sSql);
		sSql = " update  fb_iae_payout_formula_to_div set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// ֧����Ŀ��Ԫ��ʽ�������Ԫ�ر�
		sSql = " update  fb_Iae_Payout_Cell_Refelet set Payout_Item_code='"
				+ sNewPayoutKindCode + "',Par_ID='" + sNewParCode
				+ "' where Payout_Item_code='" + sOldPayoutKindCode
				+ "' and payout_item_kind=0 and set_Year = " + setYear;
		lstSql.add(sSql);
		sSql = " update  fb_Iae_Payout_Cell_Refelet set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// ֧����Ŀ��Ԫ��ʽ��
		sSql = " update  fb_Iae_PayOut_Cell_Formula set Payout_Item_code='"
				+ sNewPayoutKindCode + "',Par_ID='" + sNewParCode
				+ "' where Payout_Item_code='" + sOldPayoutKindCode
				+ "' and payout_item_kind=0 and set_Year = " + setYear;
		lstSql.add(sSql);
		sSql = " update  fb_Iae_PayOut_Cell_Formula set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);
		QueryStub.getQueryTool().executeBatch(lstSql);

	}

	/**
	 * ���ã�����֧��Ԥ����֧���ʽ���Դ�ɹ�֧������롣 ˵��������Ԥ��౨������֧ƽ�⣬Ԥ�㵥λ¼���ÿһ��֧��������Ԥ����ж�Ӧ����
	 * ��Ӧ�����룬���û�������������Ŀ���ʽ���Դ�Ĺ�ϵ��ϵͳ��Ҫ׼����һ�������
	 * ֧���ʽ���Դ�Ŀ�֧�����������Ա���֧��Ԥ��¼�롢����Ԥ����֧���ʱ�������á�
	 * ��ˣ��������������¼�������±����ã�һ���û��ı�������Ԥ��󣻶��ǲ�������
	 * �ڸı���֧���ʽ���Դ��Ϣ�����ǲ��������ڸı����ʽ���Դ��������Ŀ��Ķ�Ӧ��ϵ��
	 * 
	 * @param setYearԤ�����
	 * @param sDivCode��λ����
	 * @param iBatchNo����
	 * @param iDataType��������
	 * @param sPFSCode�ʽ���Դ���룬Ĭ��Ϊ�գ���Ϊ��ʱ��ʾ����ָ��֧���ʽ���Դ�Ŀɹ�֧����������
	 * @throws Exception
	 * @returnTrue�ɹ���Falseʧ��
	 */
	public boolean calc_Div_FundSource_Inc(String setYear, String sDivCode,
			int iCurBatchNo, int iDataType, String sPFSCode) throws Exception {
		if ((iCurBatchNo == -1 && iDataType != -1))
			return false;
		String sSql = "SELECT PFS_Code,PFS_EName,IncCol_EName,FORMULA_DET,SQL_DET"
				+ "  FROM fb_Iae_PayOut_FundSource"
				+ " WHERE set_Year="
				+ setYear + " AND End_Flag=1";
		if (!"".equals(sPFSCode) && sPFSCode != null)
			sSql = sSql + " AND PFS_Code='" + sPFSCode + "'";
		DataSet dsPfs = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsPfs);
		if (dsPfs.isEmpty())
			return false;

		ArrayList lstSql = new ArrayList();
		if (iDataType == -1) {
			for (int i = 1; i <= iCurBatchNo; i++) {
				fCalc_Div_FundSource_Inc(setYear, sDivCode, i, 0, dsPfs, lstSql);
				fCalc_Div_FundSource_Inc(setYear, sDivCode, i, 1, dsPfs, lstSql);
			}
		} else
			fCalc_Div_FundSource_Inc(setYear, sDivCode, iCurBatchNo, iDataType,
					dsPfs, lstSql);

		QueryStub.getQueryTool().executeBatch(lstSql);
		return true;
	}

	/**
	 * ����֧��Ԥ����֧���ʽ���Դ�ɹ�֧�������
	 * 
	 * @param setYear
	 * @param iBatchNo
	 * @param iDataType
	 * @param dsPfs
	 * @param dsDiv
	 * @return
	 * @throws Exception
	 */
	private void fCalc_Div_FundSource_Inc(String setYear, String sDivCode,
			int iBatchNo, int iDataType, DataSet dsPfs, ArrayList lstSql)
			throws Exception {

		String sSql = "SELECT en_id,Div_Code,Div_Name" + " FROM vw_fb_division"
				+ " WHERE set_Year=" + setYear + " AND is_leaf=1"
				+ " and en_id in ( select en_id from fb_u_Div_FundSource_Inc"
				+ "  where set_Year=" + setYear + " AND Batch_No="
				+ String.valueOf(iBatchNo) + " AND Data_Type="
				+ String.valueOf(iDataType) + ")";
		if (!"".equals(sDivCode) && sDivCode != null)
			sSql = sSql + " AND Div_Code='" + sDivCode + "'";
		DataSet dsDiv = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsDiv);
		dsDiv.beforeFirst();
		while (dsDiv.next()) {
			incUpdate(setYear, iBatchNo, iDataType, dsPfs, dsDiv, lstSql);
		}

		sSql = "SELECT en_id,Div_Code,Div_Name"
				+ " FROM vw_fb_division"
				+ " WHERE set_Year="
				+ setYear
				+ " AND is_leaf=1"
				+ " and en_id not in ( select en_id from fb_u_Div_FundSource_Inc"
				+ "  where set_Year=" + setYear + " AND Batch_No="
				+ String.valueOf(iBatchNo) + " AND Data_Type="
				+ String.valueOf(iDataType) + ")";
		if (!"".equals(sDivCode) && sDivCode != null)
			sSql = sSql + " AND Div_Code='" + sDivCode + "'";
		DBSqlExec.getDataSet(sSql, dsDiv);
		dsDiv.beforeFirst();
		while (dsDiv.next()) {
			incInsert(setYear, iBatchNo, iDataType, dsPfs, dsDiv, lstSql);
		}
	}

	private double getValue(String setYear, int iBatchNo, int iDataType,
			DataSet dsPfs, DataSet dsDiv) throws Exception {
		if ("".equals(dsPfs.fieldByName("IncCol_EName").getString())
				|| dsPfs.fieldByName("IncCol_EName").getString() == null)
			return 0;

		Object oIncColEname = dsPfs.fieldByName("IncCol_EName").getValue();
		if ("".equals(oIncColEname) || oIncColEname == null)
			return 0;
		String sSql = "SELECT SUM(isnull(" + oIncColEname.toString()
				+ ",0)) AS " + oIncColEname.toString()
				+ "  FROM fb_u_Div_Incoming_budget" + " WHERE set_Year="
				+ setYear + "   AND Batch_No=" + String.valueOf(iBatchNo)
				+ "   AND Data_Type=" + String.valueOf(iDataType)
				+ "   AND Div_Code='"
				+ dsDiv.fieldByName("Div_Code").getString() + "'";
		Object oFormulaDet = dsPfs.fieldByName("SQL_DET").getString();
		if ("".equals(oFormulaDet) || oFormulaDet == null)
			return 0;
		sSql = sSql + " AND (" + dsPfs.fieldByName("SQL_DET").getString() + ")";
		return DBSqlExec.getFloatValue(sSql);
	}

	/**
	 * ��֧��������������
	 * 
	 * @param setYear
	 * @param iBatchNo
	 * @param iDataType
	 * @param dsPfs
	 * @param dsDiv
	 * @param lstSql
	 * @throws Exception
	 */
	private void incInsert(String setYear, int iBatchNo, int iDataType,
			DataSet dsPfs, DataSet dsDiv, ArrayList lstSql) throws Exception {
		double fValue;
		String sInsert = "";
		String sValues = "";
		dsPfs.beforeFirst();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");
		while (dsPfs.next()) {
			fValue = getValue(setYear, iBatchNo, iDataType, dsPfs, dsDiv);
			if ("".equals(sInsert)) {
				sInsert = "INSERT INTO fb_u_Div_FundSource_Inc(en_id,Div_Code,"
						+ "set_year,Batch_No,Data_Type,Data_Src,Data_Attr,Rg_Code,"
						+ dsPfs.fieldByName("PFS_EName").getString();
				sValues = " VALUES('" + dsDiv.fieldByName("en_id").getString()
						+ "','" + dsDiv.fieldByName("Div_Code").getString()
						+ "'," + setYear + "," + String.valueOf(iBatchNo) + ","
						+ String.valueOf(iDataType) + ",0,0,'" + sRgCode + "',"
						+ String.valueOf(fValue);
			} else {
				sInsert = sInsert + ","
						+ dsPfs.fieldByName("PFS_EName").getString();
				sValues = sValues + "," + String.valueOf(fValue);
			}
		}
		String sSql = sInsert + ")" + sValues + ")";
		lstSql.add(sSql);
	}

	/**
	 * ���¿�֧�����������
	 * 
	 * @param setYear
	 * @param iBatchNo
	 * @param iDataType
	 * @param dsPfs
	 * @param dsDiv
	 * @param lstSql
	 * @throws Exception
	 */
	private void incUpdate(String setYear, int iBatchNo, int iDataType,
			DataSet dsPfs, DataSet dsDiv, ArrayList lstSql) throws Exception {
		double fValue;
		String sUpdate = "";

		dsPfs.beforeFirst();
		while (dsPfs.next()) {
			fValue = getValue(setYear, iBatchNo, iDataType, dsPfs, dsDiv);
			if ("".equals(sUpdate))
				sUpdate = "UPDATE fb_u_Div_FundSource_Inc SET "
						+ dsPfs.fieldByName("PFS_EName").getString() + "="
						+ String.valueOf(fValue);
			else
				sUpdate = sUpdate + ","
						+ dsPfs.fieldByName("PFS_EName").getString() + "="
						+ String.valueOf(fValue);
		}
		String sSql = sUpdate + " WHERE set_Year=" + setYear + " AND Batch_No="
				+ String.valueOf(iBatchNo) + " AND Data_Type="
				+ String.valueOf(iDataType) + " AND Div_Code='"
				+ dsDiv.fieldByName("Div_Code").getString() + "'";
		lstSql.add(sSql);
	}

	/**
	 * ���ã������ʽ���Դ��Ӧ����
	 * 
	 * @param setYear���
	 * @param sDivCode
	 * @param iBatchNo
	 * @param iDataType
	 * @param sPFSCode
	 * @param dsPfsToIncitem,�ʽ���Դ��Ӧ������ĿDataSet
	 * @throws Exception
	 */
	public DataSet savePfsToItem(String setYear, String sDivCode,
			String sPFSCode, DataSet dsPayOutFS, DataSet dsPfsToIncitem)
			throws Exception {
		// �����������ݽӿ�
		IPubInterface iPubInterface = PubInterfaceStub.getServerMethod();
		dsPayOutFS.edit();
		dsPayOutFS.setName("fb_iae_payout_fundsource");
		dsPayOutFS.post();

		// �ж�ԭ��Ӧ��ϵ�Ƿ���һ��һ��ϵ��������Ŀ�Ӹ��ʽ���Դȡ��
		DataSet dsPfsToIncitemOld = getIncWithPfsCode(sPFSCode, setYear);
		if (dsPfsToIncitemOld != null && !dsPfsToIncitemOld.isEmpty()
				&& dsPfsToIncitemOld.getRecordCount() == 1) {
			dsPfsToIncitemOld.beforeFirst();
			dsPfsToIncitemOld.next();
			String incTypeCode = dsPfsToIncitemOld.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			int oldIsInc = DBSqlExec
					.getIntValue("select is_inc from fb_iae_inctype where inctype_code ='"
							+ incTypeCode + "' and set_year=" + setYear);
			if (oldIsInc == 2) {
				// �ж��Ƿ�����˶�Ӧ��ϵ
				if (dsPfsToIncitem.locate(IIncType.INCTYPE_CODE, incTypeCode)) {
					// �ж϶�Ӧ��ϵ���Ƿ����1�������1������������Ŀis_incֵ(��2��Ϊ0)
					if (dsPfsToIncitem.getRecordCount() != 1) {
						String sSql = "update fb_iae_inctype set is_inc =0 where inctype_code ='"
								+ incTypeCode + "' and set_year=" + setYear;
						QueryStub.getQueryTool().executeUpdate(sSql);
					}
				} else {// ������ԭ��Ӧ��ϵ
					// ��is_incֵ(��2��Ϊ0)
					String sSql = "update fb_iae_inctype set is_inc =0 where inctype_code ='"
							+ incTypeCode + "' and set_year=" + setYear;
					QueryStub.getQueryTool().executeUpdate(sSql);
				}
			}
		}

		// ɾ���ʽ���Դ��������Ŀ��Ӧ��ϵ
		String sSql = "delete from fb_iae_pfs_to_incitem where pfs_code ='"
				+ sPFSCode + "' and set_year =" + setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);
		// �����Ӧ��ϵ
		dsPfsToIncitem.post();
		// ���¼���
		int iBatchNo = 0;
		calc_Div_FundSource_Inc(setYear, sDivCode, iBatchNo, -1, sPFSCode);
		return dsPfsToIncitem;

	}

	/**
	 * ����֧�����ÿ�Ŀ��֧������Ŀ
	 * 
	 * @param setYear
	 * @param lstSubItem
	 * @param sBsiId
	 * @param sAcctCodeJj
	 * @param sSubType
	 * @throws Exception
	 */
	public void saveAcctJjToSubItem(String setYear, String sBsiId,
			String sSubType) throws Exception {
		// ���ĵ�ǰ��Ŀ�ӿ�Ŀ����
		String sSql = "update ele_budget_subject_item set SubItem_Type ="
				+ sSubType + " where chr_id = '" + sBsiId + "' and set_year = "
				+ setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);
	}

	/**
	 * ���������Ԥ���Ŀ���շ���Ŀ
	 * 
	 * @param setYear
	 * @param sSelectNodeId
	 * @param sIN_BS_ID
	 * @param sAcctCodeInc
	 * @param sSubType
	 * @param iDataSrc
	 * @throws Exception
	 */
	public void saveIncAcctToIncItem(String setYear, ArrayList sSelectNodeId,
			ArrayList sSelectNodeCode, String sIN_BS_ID, String sAcctCodeInc,
			String sSubType, int iDataSrc) throws Exception {
		String sSql = "";
		if (iDataSrc == 1) {
			sSql = " update ele_budget_subject_income set subItem_type="
					+ sSubType + " where chr_id = '" + sIN_BS_ID
					+ "' and set_year = " + setYear;
		} else if (iDataSrc == 2) {
			sSql = " update fb_e_acctitem_inc set subItem_type=" + sSubType
					+ " where chr_id = '" + sIN_BS_ID + "' and set_year = "
					+ setYear;
		}
		QueryStub.getQueryTool().executeUpdate(sSql);

		// �ж��Ƿ���ȡ�����ÿ�Ŀ���շ���Ŀ��Ӧ��ϵ
		sSql = "select INCITEM_CODE from fb_Iae_IncAcct_To_IncItem where IN_BS_ID = '"
				+ sIN_BS_ID + "' and set_Year = " + setYear;
		DataSet dsIncAcctToIncItem = DataSet.create();
		dsIncAcctToIncItem.setSqlClause(sSql);
		dsIncAcctToIncItem.open();
		String incItemCode;
		List lstOptSql = null;
		dsIncAcctToIncItem.beforeFirst();
		while (dsIncAcctToIncItem.next()) {
			incItemCode = dsIncAcctToIncItem.fieldByName("INCITEM_CODE")
					.getString();
			if (!sSelectNodeCode.contains(incItemCode)) {
				if (lstOptSql == null) {
					lstOptSql = new ArrayList();
				}
				sSql = "delete fb_u_div_incoming where ACCT_CODE_INC=''"
						+ sAcctCodeInc + "'' and TOLL_CODE=''" + incItemCode
						+ "'' and set_year = " + setYear;
				lstOptSql.add(sSql);
			}
		}

		// ɾ����Ӧ��ϵ
		sSql = "delete from fb_Iae_IncAcct_To_IncItem  where IN_BS_ID = '"
				+ sIN_BS_ID + "' and set_Year = " + setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);

		// �����Ӧ��ϵ
		ArrayList listSql = new ArrayList();
		for (int i = 0; i < sSelectNodeId.size(); i++) {
			String sCode = sSelectNodeCode.get(i).toString();
			sSql = "insert into fb_Iae_IncAcct_To_IncItem(IN_BS_ID, ACCT_CODE_INC, INCITEM_CODE, set_year,toll_id)"
					+ " values('"
					+ sIN_BS_ID
					+ "','"
					+ sAcctCodeInc
					+ "','"
					+ sCode
					+ "',"
					+ setYear
					+ ",'"
					+ sSelectNodeId.get(i).toString() + "')";
			listSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(listSql);

		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._MOD
				+ "����Ԥ���Ŀ���շ���-Ԥ���Ŀ����" + sAcctCodeInc, lstOptSql);
	}

	/**
	 * �жϱ����Ƿ����,�Ҳ��ǲ�Ҷ�ӽڵ�,��������
	 * 
	 * @param sCode
	 * @param sFieldName
	 * @param sTableName
	 * @param setYear
	 * @return true��������Ҳ���Ҷ�ڵ�
	 * @throws Exception
	 */
	private InfoPackage judgeParExist(String sParLvlId, String sFieldName,
			String sTableName, String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sSql = "select * from " + sTableName + " where " + sFieldName
				+ " ='" + sParLvlId + "' and set_year=" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		if (ds.isEmpty()) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("�����󲻴���,��������д!");
			return infoPackage;
		}
		ds.beforeFirst();
		ds.next();
		if (ds.fieldByName("end_flag").getInteger() == 1) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("��д�ĸ�������Ҷ�ӽڵ㣬�����޸�!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * ��ñ���ĳ���ֶ�,����������
	 * 
	 * @param sFieldName
	 * @param sTableName
	 * @param sFilter
	 * @return
	 * @throws Exception
	 */
	private String[] getArrayID(String sFieldName, String sTableName,
			String sFilter) throws Exception {
		DataSet ds = DataSet.create();
		String sSql = "select " + sFieldName + " from " + sTableName
				+ " where " + sFilter;
		DBSqlExec.getDataSet(sSql, ds);
		if (ds.isEmpty())
			return null;
		String sID[] = new String[ds.getRecordCount()];
		ds.beforeFirst();
		int i = 0;
		while (ds.next()) {
			sID[i] = ds.fieldByName(sFieldName).getString();
			i++;
		}
		return sID;
	}

	/**
	 * �õ��շ���Ŀcha_id,��������Ԥ���Ŀ����
	 * 
	 * @param sIncTypeCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getIncItemWithCode(String sIN_BS_ID, String setYear)
			throws Exception {
		String sFilter = " IN_BS_ID='" + sIN_BS_ID + "' and set_year="
				+ setYear;
		return getArrayID("toll_id", "fb_Iae_IncAcct_To_IncItem", sFilter);
	}

	/**
	 * ��֧��Ŀ�ҽ���ϸ�� �õ�֧������Ŀcha_id,����֧�����ÿ�Ŀ����
	 * 
	 * @param sIncTypeCode
	 * @param setYear
	 * @throws Exception
	 */

	public InfoPackage judgeJjEnableModify(String sBsiId, String sName,
			String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();

		// �ж��Ƿ���ʹ��
		String sFilter = " bsi_id='" + sBsiId + "' and set_year=" + setYear;
		if (DBSqlExec.getRecordCount("vw_fb_u_Payout_Budget", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("[" + sName + "]�Ѿ���ʹ��,�����޸�!");
			return infoPackage;
		}
		// �ж��Ƿ����˹�ʽ
		String sSql = "  select * from vw_fb_Iae_PayOut_Formula_Count where bsi_id='"
				+ sBsiId + "' and set_year =" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		ds.beforeFirst();
		while (ds.next()) {
			String sFormulaId = ds.fieldByName("formula_id").getString();
			if (!PubUntSys.isDefaultFormula(sFormulaId)) {
				infoPackage.setSuccess(false);
				infoPackage.setsMessage("[" + sName + "]�Ѿ������˹�ʽ,�����޸�!");
				return infoPackage;
			}
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public InfoPackage judgeIncEnableModify(String sActtCode, String sName,
			String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// �ж��Ƿ���ʹ��
		String sFilter = " ACCT_CODE_INC='" + sActtCode + "' and set_year="
				+ setYear;
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("[" + sName + "]�Ѿ���ʹ��,�����޸�!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж�֧����Ŀ����Ƿ��ѱ�ʹ��,�����޸ĺ�ɾ��ʱ�ж�
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePayOutTypeUse(String sPayoutKindCode, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// �ж��Ƿ�ʹ��
		String sFilter = "payout_kind_code='" + sPayoutKindCode
				+ "' and set_Year = " + setYear;
		if (DBSqlExec.getRecordCount("vw_fb_u_Payout_Budget", sFilter) > 0) {
			infoPackage.setsMessage("֧����Ŀ����ѱ�֧��Ԥ���ʹ��");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if (DBSqlExec.getRecordCount("fb_u_Payout_Budget_Rae", sFilter) > 0) {
			infoPackage.setsMessage("֧����Ŀ����ѱ�֧��������ʹ��");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind_to_div", sFilter) > 0) {
			infoPackage.setsMessage("֧����Ŀ����������뵥λ�Ķ�Ӧ��ϵ");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * ֧����Ŀ��𣬵õ����ÿ�Ŀid,����֧����Ŀ������
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getJjWithPayoutKindCode(String sPayoutKindCode,
			String setYear) throws Exception {
		String sFilter = " PAYOUT_KIND_CODE='" + sPayoutKindCode
				+ "' and set_year=" + setYear;
		return getArrayID("BSI_ID", "fb_iae_payout_kind_to_jj", sFilter);
	}

	/**
	 * ֧���ʽ���Դ�����룬�ж�Ӣ�������Ƿ��ѱ�ʹ��
	 * 
	 * @param sEname
	 * @param setYear
	 * @return true:û�б�ʹ��,false�ѱ�ʹ��
	 * @throws Exception
	 */
	public boolean judgePfsEnameUse(String sEname, String setYear)
			throws Exception {
		String aFilter = sEname + ">0 and set_year = " + setYear;
		if (DBSqlExec.getRecordCount("vw_fb_u_Payout_Budget", aFilter) > 0)
			return false;
		else
			return true;
	}

	/**
	 * ֧���ʽ���Դ�����룬����֧���ʽ���Դ����
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getIncWithPfsCode(String sPfsCode, String setYear)
			throws Exception {
		String sSql = "select * from  fb_iae_pfs_to_incitem where pfs_code='"
				+ sPfsCode + "' and set_year=" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * �ж��ܷ�ɾ��֧���ʽ���Դ
	 * 
	 * @param sENameӢ������
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePfsEnableDel(String sEName, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String Epq = "%{" + sEName + "}%";
		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource",
				"formula_Det like '" + Epq + "' and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("��֧���ʽ���Դ����Ϊ������Ŀ���㹫ʽ������,����ɾ��������Ŀ!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * �ж�֧����Ŀ�����д�ı����Ƿ��ظ�
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :���ظ�,false �ظ�
	 * @throws Exception
	 */
	public boolean judgePayOutTypeCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception {
		String sFilterA;
		if (!bModidy) { // ����
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// �޸�
			sFilterA = "lvl_id ='" + sLvlId + "' and payout_kind_code <> '"
					+ sCode + "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind", sFilterA) > 0) {
			return false;
		}
		return true;

	}

	/**
	 * ֧����Ŀ���,�жϱ����Ƿ����,�Ҳ��ǲ�Ҷ�ӽᣬ�����Ҷ�ӽڵ�
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return��true��������Ҳ���Ҷ�ڵ�
	 */
	public InfoPackage judgePayOutTypeParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_payout_kind", setYear);
	}

	/**
	 * �ж�֧����Ŀ�����д�������Ƿ��ظ�
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @throws Exception
	 * @returntrue :���ظ�,false �ظ�
	 */
	public boolean judgePayOutTypeNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception {
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and PAYOUT_KIND_NAME = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and PAYOUT_KIND_NAME = '"
					+ sName + "' and set_year=" + setYear;
		}
		if (bModidy) {// �Ƿ����޸�,�ж��������ϲ������Լ�
			sFilter = sFilter + " and PAYOUT_KIND_CODE != '" + sCode + "'";
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind", sFilter) > 0) {
			return false;
		}
		return true;
	}

	public InfoPackage judgePfsParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_payout_fundsource",
				setYear);
	}

	public InfoPackage judgePfsCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilterA;
		if (!bModidy) { // ����
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// �޸�
			sFilterA = "lvl_id ='" + sLvlId + "' and PFS_CODE <> '" + sCode
					+ "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource", sFilterA) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("�����Ѿ���ʹ��!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public InfoPackage judgePfsNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and PFS_Name = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and PFS_Name = '" + sName
					+ "' and set_year=" + setYear;
		}
		if (bModidy) {// �Ƿ����޸�,�ж��������ϲ������Լ�
			sFilter = sFilter + " and PFS_CODE != '" + sCode + "'";
		}

		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("֧���ʽ���Դ�����Ѿ���ʹ��!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public boolean judgePfsStdTypeCode(String sCode, String setYear,
			boolean bModify) throws Exception {
		String sFilter = " std_type_code='001'" + " and set_year=" + setYear;
		if (bModify) {
			sFilter = sFilter + " and pfs_code<>'" + sCode + "'";
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource", sFilter) > 0)
			return false;
		return true;
	}

	/**
	 * �ж��ʽ���Դ�����룬������Ŀ�Ƿ�ѡ��
	 * 
	 * @param lstId
	 * @param lstName
	 * @param setYear
	 * @param sPayOutTypeCode
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeIsCheck(ArrayList lstId, ArrayList lstName,
			String setYear, String sPfs) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		boolean bUseFlag = false;
		String sMsg = "";
		String sVaule;
		String sName;
		for (int i = 0; i < lstId.size(); i++) {
			String sIncTypeCode = lstId.get(i).toString();
			String sSql = "select pfs_code from fb_iae_pfs_to_incitem  where inctype_code = '"
					+ sIncTypeCode + "' and set_year=" + setYear;
			if (sPfs != null && !"".equals(sPfs))
				sSql = sSql + " and  pfs_code<>'" + sPfs + "'";
			sVaule = DBSqlExec.getStringValue(sSql);
			if (sVaule != null) {
				sSql = "select pfs_name from fb_iae_payout_fundsource  where pfs_code = '"
						+ sVaule + "' and set_year=" + setYear;
				sName = DBSqlExec.getStringValue(sSql);
				sMsg = sMsg + "[" + lstName.get(i).toString() + "]��[" + sName
						+ "]���Ѿ���ʹ�ã�\n";
				bUseFlag = true;
			}
		}

		if (bUseFlag) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage(sMsg);
		} else {
			infoPackage.setSuccess(true);
		}
		return infoPackage;
	}

	/**
	 * ����֧���ʽ���Դ
	 */
	public void savePayOutFS(DataSet dsPayOutFS, String sPfsCode,
			String sParPfsCode, String setYear, List lstAcctCode,
			boolean bAddFirstNode) throws Exception {
		// �õ�optMenuֵ
		String optMenu = UpdateStruPub.getOptMenu(dsPayOutFS, sPfsCode,
				IPayOutFS.PFS_CODE, IPayOutFS.PFS_NAME, IPayOutFS.PFS_ROOT);

		dsPayOutFS.post();
		// �ʽ���Դ��������Ŀ��
		if (bAddFirstNode) {
			String sSql = "Update fb_Iae_Pfs_To_IncItem  Set Pfs_Code='"
					+ sPfsCode + "' Where set_Year=" + setYear
					+ " and Pfs_Code ='" + sParPfsCode + "'";
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

		ArrayList lstSql = new ArrayList();
		String sSql = null;
		// ɾ��ԭ��Ӧ��ϵ
		if (!"".equals(sParPfsCode) && sParPfsCode != null) {
			sSql = "delete fb_iae_pfs_to_acctcode where pfs_code ='"
					+ sParPfsCode + "' and set_year=" + setYear;
		}
		lstSql.add(sSql);
		// �¶�Ӧ��ϵ
		if (lstAcctCode != null) {
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			for (int i = 0; i < lstAcctCode.size(); i++) {
				sSql = "insert into fb_iae_pfs_to_acctcode(pfs_code,acct_code,set_year,rg_code) values('"
						+ sPfsCode
						+ "','"
						+ lstAcctCode.get(i).toString()
						+ "'," + setYear + ",'" + sRgCode + "')";
				lstSql.add(sSql);
			}
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, "");

	}

	/**
	 * �õ��ʽ���Դ�Թ��ܿ�Ŀ
	 * 
	 * @param setYear
	 * @param sPfsCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsToAcct(String setYear, String sPfsCode)
			throws Exception {
		String sSql = "select acct_code "
				+ " from fb_iae_pfs_to_acctcode where set_year=? and  pfs_code=? ";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { setYear, sPfsCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * �ж��Ƿ������ù�ʽ�͹�ʽ�뵥λ��Ӧ��ϵ,
	 * 
	 * @param payOutKindId֧����Ŀ�������
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePayoutTypeFormulaUse(String payOutKindCode)
			throws Exception {
		InfoPackage info = new InfoPackage();
		String filter = " (PAYOUT_ITEM_CODE='" + payOutKindCode
				+ "' or PAR_ID = '" + payOutKindCode + "')"
				+ " and set_year = "
				+ SessionUtil.getUserInfoContext().getSetYear();

		if (DBSqlExec.getRecordCount("fb_iae_payout_formula_to_div", filter) > 0) {
			info.setSuccess(false);
			info.setsMessage("�����ù�ʽ����ʽ�뵥λ�Ķ�Ӧ��ϵ");
			return info;
		}

		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_Formula", filter) > 0) {
			info.setSuccess(false);
			info.setsMessage("�����ù�ʽ");
			return info;
		}
		info.setSuccess(true);
		return info;
	}

	/**
	 * �жϾ��ÿ�Ŀ���֧����Ŀ����Ƿ���ڶ�Ӧ��ϵ
	 * 
	 * @param sPayoutKindCode
	 *            ֧����Ŀ������
	 * @param acctJJCode
	 *            ���ÿ�Ŀ����
	 * @return ���ڶ�Ӧ��ϵ����true,���򷵻�false
	 * @throws Exception
	 */

	public boolean JudgeAcctJJExist(String sPayoutKindCode, String acctJJCode)
			throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String filter = " PAYOUT_KIND_CODE='" + sPayoutKindCode
				+ "' and Acct_code_jj ='" + acctJJCode + "' and set_year="
				+ setYear;
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind_to_jj", filter) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �ı�֧����Ŀ���
	 * 
	 * @param sNewPayoutKindCode��֧����Ŀ������
	 * @param sNewPayoutKindName��֧����Ŀ��𸸶�������
	 * @param sOldPayoutKindCodeԭ֧����Ŀ������
	 * @param acctJjCode���ÿ�Ŀ����
	 * @throws Exception
	 * 
	 */
	public void changePayOutKind(String sNewPayoutKindCode,
			String sNewPayoutKindName, String sOldPayoutKindCode,
			String acctJjCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		ArrayList lstSql = new ArrayList();
		// ֧����Ŀ��ʽֵ��
		// String sSql = " update fb_u_Div_PayOut_Formula_Value set par_id='"
		// + sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
		// + "' and Payout_Item_code='" + acctJjCode + "'"
		// + " and payout_item_kind=1 and set_Year = " + setYear;
		// lstSql.add(sSql);

		// ֧����Ŀ��ʽ�Ե�λ��
		String sSql = " update  fb_iae_payout_formula_to_div set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and Payout_Item_code='" + acctJjCode + "'"
				+ "  and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// ֧����Ŀ��Ԫ��ʽ�������Ԫ�ر�
		sSql = " update  fb_Iae_Payout_Cell_Refelet set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and Payout_Item_code='" + acctJjCode + "'"
				+ "  and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// ֧����Ŀ��Ԫ��ʽ��
		lstSql.add(sSql);
		sSql = " update  fb_Iae_PayOut_Cell_Formula set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and Payout_Item_code='" + acctJjCode + "'"
				+ " and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);
		// ֧����Ŀ���Ծ��ÿ�Ŀ
		sSql = "update  fb_Iae_PayOut_Kind_To_JJ set Payout_kind_code='"
				+ sNewPayoutKindCode + "',Payout_kind_name='"
				+ sNewPayoutKindName + "' where PayOut_Kind_Code='"
				+ sOldPayoutKindCode + "' and ACCT_CODE_JJ = '" + acctJjCode
				+ "' and set_Year = " + setYear;
		lstSql.add(sSql);
		QueryStub.getQueryTool().executeBatch(lstSql);

		// �����֧����Ŀ��𾭼ÿ�Ŀ��ص�ҵ�����ݱ�
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		List lstOptSql = new ArrayList();
		while (dsTable.next()) {
			String tableName = dsTable.fieldByName("name").getString();
			sSql = "update  " + tableName + " set Payout_kind_code=''"
					+ sNewPayoutKindCode + "'',Payout_kind_name=''"
					+ sNewPayoutKindName + "'' where PayOut_Kind_Code=''"
					+ sOldPayoutKindCode + "'' and ACCT_CODE_JJ = ''"
					+ acctJjCode + "'' and set_Year = " + setYear;
			lstOptSql.add(sSql);
			Set setCol = QueryStub.getQueryTool().getColumnInfo(tableName);
			if (setCol.contains("data_src")) {
				sSql = "update  " + tableName + " set PROJECT_NAME=''"
						+ sNewPayoutKindName + "'' where PayOut_Kind_Code=''"
						+ sOldPayoutKindCode + "'' and ACCT_CODE_JJ = ''"
						+ acctJjCode + "'' and data_src = 50 and set_Year = "
						+ setYear;
				lstOptSql.add(sSql);
			}
		}
		// ά���ṹ�䶯���
		UpdateStruPub.exeStruSqlToAllDiv("�ı�֧����Ŀ���-���ÿ�Ŀ����" + acctJjCode,
				lstOptSql);
	}

	/**
	 * �����֧����Ŀ��𾭼ÿ�Ŀ��ص�ҵ�����ݱ�
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private DataSet getPayOutKindAcctJjTable(String setYear) throws Exception {
		// ���ʽ���Դ�йصı�
		String sSql = "Select Name From fb_s_PubCode Where set_Year=" + setYear
				+ " And TypeID = 'PAYOUTKINDTABLE' Order By Lvl_id";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * �ж�֧����Ŀ����о��ÿ�Ŀ�Ƿ�ʹ��
	 * 
	 * @param payOutKindId֧����Ŀ�������
	 * @param acctJjCode���ÿ�Ŀ����
	 * @returnʹ�������Ϣ��δʹ�÷��ؿ�ֵ
	 * @throws Exception
	 */
	public String judgePayoutTypeAcctJjUse(String payOutKindCode,
			String acctJjCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sFilter = "payout_kind_code='" + payOutKindCode
				+ "' and acct_code_jj='" + acctJjCode + "' and set_Year = "
				+ setYear;

		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		while (dsTable.next()) {
			String tableName = dsTable.fieldByName("name").getString();
			if (DBSqlExec.getRecordCount(tableName, sFilter) > 0)
				return this.getTableChinaName(tableName) + "��������";
		}

		sFilter = "  PAYOUT_ITEM_CODE='" + acctJjCode + "' and PAR_ID = '"
				+ payOutKindCode + "' and  set_year = " + setYear;

		if (DBSqlExec.getRecordCount("fb_iae_payout_formula_to_div", sFilter) > 0) {
			return "�����ù�ʽ����ʽ�뵥λ�Ķ�Ӧ��ϵ";
		}

		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_Formula", sFilter) > 0) {
			return "�����ù�ʽ";
		}
		return "";
	}

	/**
	 * �õ�֧����Ŀ���-ר��֧����¼��
	 * 
	 * @param payouKindCode
	 * @return
	 * @throws Exception
	 */
	public String getPayoutKindStandPrj(String payouKindCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sFilter = IPayOutType.STD_TYPE_CODE + "='"
				+ IPayOutType.PAYOUTKINDSTAND_PRJ + "'";
		if (!Common.isNullStr(payouKindCode)) {
			sFilter = sFilter + " and " + IPayOutType.PAYOUT_KIND_CODE + "<>'"
					+ payouKindCode + "'";
		}

		sFilter = sFilter + " and set_year=" + setYear;
		return DBSqlExec
				.getStringValue(" select PAYOUT_KIND_NAME from fb_iae_payout_kind where "
						+ sFilter);
	}

	/**
	 * �����֧���ʽ���Դ���õ�֧���ʽ���Դ����,����������Ŀ����
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getPfsToIncCode(String sIncTypeCode, String setYear)
			throws Exception {
		String sSql = "select pfs_code,INCTYPE_CODE"
				+ " from  fb_iae_pfs_to_incitem "
				+ " where inctype_code='"
				+ sIncTypeCode
				+ "' and pfs_code in ( select pfs_code from fb_iae_payout_fundsource where data_source=0 and set_year="
				+ setYear + ") and set_year=" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * ���֧���ʽ���Դ(������������ԴΪ����ļ�¼��
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSNotCalcTre(int setYear) throws Exception {
		String payOutFSSQL = "select lvl_id||' '||PFS_NAME as name ,a.* from fb_iae_payout_fundsource a where end_flag = 1 and data_source=0 and set_year=?"
				+ " union all"
				+ " select lvl_id||' '||PFS_NAME as name ,a.* from fb_iae_payout_fundsource A  where a.end_flag <> 1 and a.set_year=?"
				+ " and exists (select 1 from fb_iae_payout_fundsource b where b.Lvl_Id like a.lvl_id||'%' and b.end_flag = 1 and b.set_year=?)";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear),
				new Integer(setYear), new Integer(setYear) });
		ds.setSqlClause(payOutFSSQL);
		ds.open();
		return ds;
	}

	/**
	 * �õ�������Ŀ���is_inc�ֶ�ֵ
	 * 
	 * @param incTypeCode
	 * @return
	 * @throws Exception
	 */
	public int getIncTypeIsInc(String incTypeCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		return DBSqlExec
				.getIntValue("select is_inc from fb_iae_inctype where inctype_code ='"
						+ incTypeCode + "' and set_year=" + setYear);
	}

	public DataSet judgeIncColYLBLExist(String setYear, String sCode)
			throws Exception {
		String sSql = "select * from fb_iae_inccolumn where INCCOL_CODE<>'"
				+ sCode + "' and n2=1 and set_year=" + setYear;
		return DBSqlExec.getDataSet(sSql);
	}

	/**
	 * �õ�֧���ʽ���Դ�ܼƼ�¼
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsHj(String setYear) throws Exception {
		String sSql = " select * from fb_iae_payout_fundsource"
				+ " where std_type_code='001'" + " and set_year=" + setYear;
		DataSet ds = DataSet.create();
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}
}