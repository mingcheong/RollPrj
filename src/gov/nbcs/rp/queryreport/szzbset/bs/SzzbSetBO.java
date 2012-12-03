package gov.nbcs.rp.queryreport.szzbset.bs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import gov.nbcs.rp.basinfo.common.BaseUtil;
//import gov.nbcs.rp.basinfo.common.baseDataUtil.PubDataCache;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.qrbudget.bs.KitPub;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.reportcy.common.util.ReportTools;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.Session;
import com.foundercy.pf.util.sessionmanager.SessionUtil;
import com.fr.cell.CellSelection;

public class SzzbSetBO implements ISzzbSet {

	String MIN_REPORT_TYPE = "800001";

	/**
	 * 得到收支总表的信息
	 */

	private GeneralDAO dao;

	public GeneralDAO getDao() {
		return dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	public DataSet getRepset(String setYear, int iTypeFlag, String sReportID)
			throws Exception {
		String sSql = "select * from fb_u_qr_repset where type_flag = "
				+ iTypeFlag + " and  set_year = " + setYear
				+ " and report_ID='" + sReportID + "'";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 得到收支总表信息
	 */
	public DataSet getSzzb(String sReportId, String setYear) throws Exception {
		String sSql = "select distinct FIELD_COLUMN,FIELD_COLUMNSPAN,FIELD_ROW,FIELD_ROWSPAN,"
				+ "FIELD_COLWIDTH,FIELD_ROWHEIGHT,FIELD_FONT,FIELD_FONTSIZE,"
				+ "HEADERBODY_FLAG,FIELD_VALUE,HOR_ALIGNMENT,FIELD_PARA,FIELD_FONTSTYPE,VER_ALIGNMENT,report_id"
				+ " from fb_u_qr_szzb where report_id = '"
				+ sReportId
				+ "' and  set_year = " + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 保存收支总表信息
	 */
	public void saveReport(DataSet dsRepset, DataSet dsColSet, DataSet dsSzzb,
			List lstType) throws Exception {
		dsRepset.post();
		String sReportID = dsRepset.fieldByName(IQrBudget.REPORT_ID)
				.getString();
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");
		// 先删除
		String sSql = "delete from fb_u_qr_colset where report_id='"
				+ sReportID + "' and set_year="
				+ SessionUtil.getUserInfoContext().getSetYear();
		dao.executeBySql(sSql);
		dsColSet.edit();
		dsColSet.setName("fb_u_qr_colset");
		dsColSet.post();

		if (dsSzzb != null) {
			sSql = "delete from fb_u_qr_szzb where report_id='" + sReportID
					+ "' and set_year="
					+ SessionUtil.getUserInfoContext().getSetYear();
			dao.executeBySql(sSql);
			dsSzzb.setName("fb_u_qr_szzb");
			dsSzzb.post();
		}
	
		// 删除fb_u_qr_report_to_type当前报表记录信息
		dao
				.executeBySql("delete from fb_u_qr_report_to_type where REPORT_ID= '"
						+ sReportID + "' and set_year=" + setYear);
		
		dao.executeBySql("delete from fb_u_usertoreport where REPORT_ID='"+sReportID+"' and user_id='"+SessionUtil.getUserInfoContext().getUserID()+"' and set_year="+setYear+"");
		// 保存fb_u_qr_report_to_type
		int size = lstType.size();
		for (int i = 0; i < size; i++) {
			dao
					.executeBySql(
							"insert into fb_u_qr_report_to_type(report_id,type_code,set_year,rg_code) values (?,?,?,?)",
							new Object[] { sReportID, lstType.get(i), setYear,
									sRgCode });
			
			dao.executeBySql("insert into fb_u_usertoreport (LAST_VER,RG_CODE,USER_ID,REPORT_ID,SET_YEAR,REPORT_TYPE)values('','"+
					sRgCode+"','"+ SessionUtil.getUserInfoContext().getUserID()+"','"+sReportID+"','"+setYear+"','"+lstType.get(i)+"')");
		}
	}

	/**
	 * 得到报表类型
	 */
	public DataSet getReportType(String setYear) throws Exception {
		String sSql = "select code,name from FB_S_PUBCODE where typeid='QUERYTYPE' and set_year ="
				+ setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 保存封面
	 */
	public void saveConver(RepSetObject repSetObject, DataSet dsSzzb,
			String sReportID, List lstType) throws Exception {
		// 得到登陆年份
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");
		String reportID = repSetObject.getREPORT_ID();
		int reportType = repSetObject.getREPORT_TYPE();
		String reportCName = repSetObject.getREPORT_CNAME();
		String isActive = repSetObject.getIS_ACTIVE();
		String lvlID = repSetObject.getLVL_ID();
		int typeFlag = repSetObject.getTYPE_FLAG();
		int dataUser = repSetObject.getDATA_USER();
		String columnArea = repSetObject.getCOLUMN_AREA();

		dao.executeBySql(
				"delete fb_u_qr_repset where report_id =? and set_year = ? ",
				new Object[] { reportID, setYear });

		String sSqlTmp = "insert into fb_u_qr_repset"
				+ " (set_year, type_no, report_id, report_type, report_cname,title,  "
				+ "report_source, is_passverify, is_active, data_user, is_end,lvl_id, rg_code,  type_flag,COLUMN_AREA "
				+ ")" + " values" + " (" + setYear + ", 1, '" + reportID
				+ "', " + reportType + ", '" + reportCName + "','"
				+ reportCName + "','定制','是','" + isActive + "'," + dataUser
				+ ",1,'" + lvlID + "','" + sRgCode + "'," + typeFlag + ",'"
				+ columnArea + "')";
		dao.executeBySql(sSqlTmp);
		dao.executeBySql("delete from fb_u_usertoreport where REPORT_ID='"+reportID+"' and user_id='"+SessionUtil.getUserInfoContext().getUserID()+"' and set_year="+setYear+"");
	

		dao
				.executeBySql("delete from fb_u_qr_report_to_type where REPORT_ID= '"
						+ reportID + "' and set_year=" + setYear);
		// 保存fb_u_qr_report_to_type
		int size = lstType.size();
		for (int i = 0; i < size; i++) {
			dao
					.executeBySql(
							"insert into fb_u_qr_report_to_type(report_id,type_code,set_year,rg_code) values (?,?,?,?)",
							new Object[] { reportID, lstType.get(i), setYear,
									sRgCode });
			dao.executeBySql("insert into fb_u_usertoreport (LAST_VER,RG_CODE,USER_ID,REPORT_ID,SET_YEAR,REPORT_TYPE)values('','"+
					sRgCode+"','"+ SessionUtil.getUserInfoContext().getUserID()+"','"+reportID+"','"+setYear+"','"+lstType.get(i)+"')");
		}

		if (sReportID != null && !Common.isNullStr(sReportID)) {
			String sSql = "delete FB_U_QR_SZZB where report_id ='" + sReportID
					+ "' and set_year = " + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		}
		if (dsSzzb != null) {
			dsSzzb.setName("FB_U_QR_SZZB");
			dsSzzb.post();
		}
	}

	public String getMaxCode(String sFieldName) throws NumberFormatException,
			Exception {
		String sMaxCode = DBSqlExec.getMaxValueFromField("fb_u_qr_repset",
				sFieldName, " "
						 + sFieldName + "<>'null'");
		if (Common.isNullStr(sMaxCode) || "0".equals(sMaxCode)) {
			return MIN_REPORT_TYPE + "0001";
		}
		DecimalFormat df = new DecimalFormat("000000000");
		sMaxCode = df.format(Double.parseDouble(sMaxCode) + 1);
		return sMaxCode;
	}

	public DataSet getJsPositionSub(String setYear) throws Exception {
		String sSql = "select JBQK_ZW,ZZ_GZBT,ZZ_SHBT,LX_SHBT,TX_SHBT from FB_B_ZWBT where set_year="
				+ setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public void saveJsPositionSub(DataSet ds) throws Exception {
		ds.post();
	}

	/**
	 * 取得收支表的固定格信息
	 */
	public List getSzzbCons(String sReportId, String setYear) throws Exception {
		String sSql = "select FIELD_COLUMN,FIELD_COLUMNSPAN,FIELD_ROW,FIELD_ROWSPAN,"
				+ "FIELD_COLWIDTH,FIELD_ROWHEIGHT,FIELD_FONT,FIELD_FONTSIZE,"
				+ "HEADERBODY_FLAG,FIELD_VALUE,HOR_ALIGNMENT,FIELD_PARA,FIELD_FONTSTYPE,VER_ALIGNMENT"
				+ " from fb_u_qr_szzb where report_id = '"
				+ sReportId
				+ "' and  set_year = " + setYear;

//		return PubDataCache.convertToXMLList(dao.findBySqlByUpper(sSql));
		return null;
	}

	/**
	 * 取得收支表固定格信息(数据集)
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */

	public DataSet getSzzbConsDs(String sReportId, String setYear)
			throws Exception {
		String sSql = "select FIELD_COLUMN,FIELD_COLUMNSPAN,FIELD_ROW,FIELD_ROWSPAN,"
				+ "FIELD_COLWIDTH,FIELD_ROWHEIGHT,FIELD_FONT,FIELD_FONTSIZE,"
				+ "HEADERBODY_FLAG,FIELD_VALUE,HOR_ALIGNMENT,FIELD_PARA,FIELD_FONTSTYPE,VER_ALIGNMENT"
				+ " from fb_u_qr_szzb where report_id = '"
				+ sReportId
				+ "' and  set_year = " + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public List getSzzbFor(String sReportId, String setYear) throws Exception {
		String sSql = "select REPORT_ID,XH,FIELD_NAME,T_SOURCE,PFS,"
				+ "ACCT_CODE_JJ,INCTYPE_CODE,TOLL_CODE,SET_YEAR,"
				+ "FORMULA,CAL_FG,ACCT_CODE,ZC_TYPE,CELLCOL,CELLROW,SQL_STATEMENT,CAL_FG,PAYOUT_KIND_CODE"
				+ " from FB_U_QR_SZZBCELL where report_id = '" + sReportId
				+ "' and  set_year = " + setYear;
		return null;
//		return PubDataCache.convertToXMLList(dao.findBySqlByUpper(sSql));
	}

	/**
	 * 得到报表表头sql
	 */
	public String getReportHeaderSql(String sReportId, String setYear)
			throws Exception {
		String sSql = "select distinct a.set_year,a.Field_Id, a.FIELD_CODE ,a.FIELD_CNAME,a.FIELD_ENAME,a.FIELD_FNAME,a.FIELD_TYPE,a.FIELD_DISPWIDTH,a.FIELD_DISFORMAT,a.IS_LEAF"
				+ ",a.is_hidecol,a.FIELD_DISFORMAT,a.REPORT_ID from fb_u_qr_colset a , fb_u_qr_colset b"
				+ " where b.field_code like a.field_code||'%' and a.set_year = b.set_year and b.report_id = a.report_id"
				+ " and  b.report_id = '"
				+ sReportId
				+ "'  and b.set_year = "
				+ setYear + " order by a.FIELD_CODE ";
		return sSql;
	}

	public DataSet getReportHeader(String sReportId, String setYear)
			throws Exception {
		String sSql = getReportHeaderSql(sReportId, setYear);
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public boolean saveFillCols(List lstSql, String reportID) throws Exception {
		if (Common.isNullStr(reportID))
			throw new Exception("没有指定操作的报表ID");
		String sSql = "delete from FB_U_QR_SZZBCELL where  report_ID='"
				+ reportID + "' and set_year="
				+ SessionUtil.getUserInfoContext().getSetYear();
		QueryStub.getQueryTool().executeUpdate(sSql);
		if (lstSql == null || lstSql.size() < 1)
			return true;
		QueryStub.getQueryTool().executeBatch(lstSql);
		return true;

	}

	public XMLData getSzzbValues(XMLData xmlForCell, String sqlWhere,boolean isConvert) {
		SzzbEngine en = new SzzbEngine(xmlForCell, this.dao);
		return en.doQuery(sqlWhere,isConvert);
	}

	public XMLData getSzzbValuesByDetail(XMLData xmlForCell, String sVerNo,
			String sReportID, String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iTypeFlag,boolean isConvert)
			throws Exception {
		String sFilter = sBatchNoFilter + " and ver_no='" + sVerNo + "' ";
		// 得到单位查询条件
		sFilter = sFilter + getDivWhereSql(iTypeFlag, lstDept, lstDiv, setYear);
		return getSzzbValues(xmlForCell, sFilter,isConvert);
	}

	public String getDivWhereSql(int iTypeFlag, List lstDept, List lstDiv,
			String setYear) throws Exception {
		String sFilter = "";
		// 得到业务处室查询条件
		String sDeptWhere = getDeptFilter(lstDept, setYear);
		// 得到单位查询条件
		String sDivWhere = getDivFilter(lstDiv);

		if ("".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDivWhere + ")";
		} else if (!"".equals(sDeptWhere) && "".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + ")";
		} else if (!"".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + " or" + sDivWhere + ")";
		}

		// modify by zlx 20080406
		// // 总查询条件
		// if (!PubInterfaceStub.getServerMethod().isYSC()) {
		// if (iTypeFlag == 9)
		// sFilter = sFilter + getUserToDiv_ZHC(setYear);
		// else
		// sFilter = sFilter + getUserToDiv(setYear);
		// }

		sFilter = sFilter + getUserToDiv(setYear);

		return sFilter;
	}

	/**
	 * 得到业务处室查询条件
	 * 
	 * @param lstDept
	 * @param setYear
	 * @return
	 */
	private String getDeptFilter(List lstDept, String setYear) {
		// 业务处室
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

	// private String getUserToDiv_ZHC(String setYear) throws Exception {
	// String sFilterWhere = " and div_code in "
	// + KitPub.vw_fb_filterDiv_ZHC(setYear, "chr_code");
	// return sFilterWhere;
	// }

	private String getUserToDiv(String setYear) throws Exception {
		String sFilterWhere = " and div_code in "
				+ KitPub.vw_fb_filterDiv(setYear, "chr_code");
		return sFilterWhere;
	}

	/**
	 * 得到单位查询条件
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
			if (divObject.isLeaf) { // 叶节点
				if ("".equals(sDivWhereisLeaf)) {
					sDivWhereisLeaf = "'" + divObject.sDivCode + "'";
				} else {
					sDivWhereisLeaf = sDivWhereisLeaf + ",'"
							+ divObject.sDivCode + "'";
				}
			} else { // 不是叶节点
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

	/**
	 * 取得收支表固定格信息(数据集)不含标题
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */

	public DataSet getSzzbConsDsWithoutTitle(String sReportId, String setYear)
			throws Exception {
		int iRowStart = getHeadStartRow(sReportId, setYear);
		String sSql = "select FIELD_COLUMN,FIELD_COLUMNSPAN,FIELD_ROW,FIELD_ROWSPAN,"
				+ "FIELD_COLWIDTH,FIELD_ROWHEIGHT,FIELD_FONT,FIELD_FONTSIZE,"
				+ "HEADERBODY_FLAG,FIELD_VALUE,HOR_ALIGNMENT,FIELD_PARA,FIELD_FONTSTYPE,VER_ALIGNMENT"
				+ " from fb_u_qr_szzb where report_id = '"
				+ sReportId
				+ "' and  set_year = "
				+ setYear
				+ " and FIELD_ROW>"
				+ iRowStart;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;

	}

	// 取得收支表的表头起始行
	public int getHeadStartRow(String sReportId, String setYear)
			throws Exception {
		if (Common.isNullStr(sReportId))
			return -1;
		String sSql = "select column_area from FB_U_QR_REPSET where report_id='"
				+ sReportId + "' and set_year=" + setYear;
		String col = BaseUtil.getAStringField(dao.findBySql(sSql),
				"column_area");
		if (Common.isNullStr(col))
			return -1;
		CellSelection cells = ReportUtil.translateToNumber(col);
		return cells.getRow() - 1;
	}

	/**
	 * 取得格式化数据。
	 * 
	 * @return 键：类型 值：DATASET
	 * @throws Exception
	 */
	public XMLData getFormatList() throws Exception {
		XMLData aData = new XMLData();
		String sFormateSQL = " select Name from fb_s_pubcode "
				+ " where typeID='DQRDISPLAY' and cvalue='" + FORMAT_INT
				+ "' and  set_year=?" + " Order By Name ";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.setSqlClause(sFormateSQL);
		ds.open();
		ds.beforeFirst();

		Vector vector = new Vector();
		vector.add("");
		while (ds.next()) {
			String item = ds.fieldByName("name").getString();
			vector.add(item);
			// sb.append(item).append("#").append(item).append("+");
		}

		aData.put(FORMAT_INT, vector);

		sFormateSQL = " select Name from fb_s_pubcode "
				+ " where typeID='DQRDISPLAY' and cvalue='" + FORMAT_FLOAT
				+ "' and  set_year=?" + " Order By Name ";
		ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.setSqlClause(sFormateSQL);
		ds.open();
		ds.beforeFirst();
		vector = new Vector();
		vector.add("");
		while (ds.next()) {
			String item = ds.fieldByName("name").getString();
			vector.add(item);
		}

		aData.put(FORMAT_FLOAT, vector);
		vector = new Vector();
		vector.add("");
		aData.put(FORMAT_STRING, vector);
		return aData;

	}
	public DataSet getPayOutKind(int setYear) throws Exception {
		String sSQL = "select lvl_id||' '||PAYOUT_KIND_NAME as name ,a.*  from vw_fb_payout_kind a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(sSQL);
		ds.open();
		return ds;
	}
	
	/**
	 * 报表是否要转换到万元
	 * @param reportID
	 * @return
	 * @throws Exception
	 */
	public boolean isSZReportNeedConvert(String reportID)throws Exception{
		String sSql="select 1 as exp from Fb_u_Qr_Repset where report_id='"+reportID+"' and "+CONVERT_FIELD+"=1";
		return !Common.isNullStr(BaseUtil.getAStringField(dao.findBySql(sSql), "exp"));
	}
}
