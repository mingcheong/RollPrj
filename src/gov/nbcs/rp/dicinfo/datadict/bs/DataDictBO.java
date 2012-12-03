
package gov.nbcs.rp.dicinfo.datadict.bs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import gov.nbcs.rp.basinfo.common.BaseUtil;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.datactrl.SQLTool;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.UUIDRandom;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * <p>
 * Title:字典管理操作
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */

public class DataDictBO implements IDataDictBO {
	private static Map SQL_TYPE_MAPPING_STRING = new HashMap();
	static {
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.INTEGER), "整数");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.BIGINT), "货币");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.CHAR), "字符型");

		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.FLOAT), "货币");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.DOUBLE), "货币");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.SMALLINT), "整数");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.VARCHAR), "字符型");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.DECIMAL), "货币");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.NUMERIC), "货币");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.BIGINT), "整数");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.DATE), "货币");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.TIMESTAMP), "字符型");
	}

	public static final String TABLE_DEFINE_BASE = "FB_B_SET_REPSET";

	public static final String TABLE_DEFINE_BUDGET = "FB_S_IR_TABLE";

	public static final String TABLE_DEFINE_USER = "FB_U_IR_MASTER";

	public static final String FIELD_ACCT_CODE = "ACCT_CODE";// 功能科目字段名

	public static final String FIELD_DIV_CODE = "DIV_CODE";// 单位编码

	// 支出项目类别引用列ID
	private final String PAYOUTKIND_REFCOLID = "0007";

	// 收入项目类别引用列ID
	private final String INCTYPE_REFCOLID = "0006";

	GeneralDAO dao;

	private static XMLData xmlRefTable = new XMLData();
	static {
		xmlRefTable.put("fb_b_set_repset", new String[] { "fb_b_set_colset" });
		xmlRefTable.put("FB_B_SET_COLSET", new String[] { "FB_B_SET_REPSET" });
		xmlRefTable.put("FB_U_IR_COLINFO", new String[] { "FB_U_IR_MASTER" });
		xmlRefTable
				.put(
						"FB_S_IR_FIXCOLINFO",
						new String[] { "FB_U_DIV_INCOMING_BUDGET,FB_U_DIV_INCOMING,VW_FB_U_PAYOUT_BUDGET,FB_U_PAYOUT_GOV_PURCHASE" });
		xmlRefTable.put("FB_IAE_PAYOUT_FUNDSOURCE", new String[] {
				"VW_FB_U_PAYOUT_BUDGET", "FB_U_PAYOUT_GOV_PURCHASE",
				IDataDictBO.PAYOUT_RAE, IDataDictBO.PRJ_PFS });
		xmlRefTable.put("FB_IAE_INCCOLUMN",
				new String[] { "FB_U_DIV_INCOMING" });
	}

	/**
	 * @return the dao
	 */
	public GeneralDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/**
	 * 查询表类型
	 * 
	 * @return
	 */
	public List queryTableTypeList() {
		String sql = "select TYPE_CODE CHR_CODE,TYPE_CODE chr_id,TYPE_CODE,TYPE_NAME chr_name,TYPE_NAME,MEMO,NULL parent_id from fb_dict_type ORDER BY TYPE_CODE";
		return dao.findBySql(sql);
	}

	/**
	 * 根据表名称，设置所有有默认值的列的值
	 * 
	 * @param tableName
	 */
	// private void setDefaultValue(String tableName) {
	// String sql = "select table_name,column_name,data_default from
	// user_tab_columns where table_name=?";
	// List defaultValue = dao.findBySql(sql, new String[] { tableName });
	// for (int i = 0; i < defaultValue.size(); i++) {
	// XMLData tempData = (XMLData) defaultValue.get(i);
	// if (tempData.get("data_default") != null) {
	// String updateSql = "update fb_dict_info_detail set adefault_value=? where
	// table_ename=? and afield_ename=?";
	// dao.executeBySql(updateSql,
	// new Object[] { tempData.get("data_default"),
	// tempData.get("table_name"),
	// tempData.get("column_name") });
	// }
	// }
	// }
	/**
	 * 删除字典表信息
	 * 
	 * @return 表名
	 */
	public int deleteTableInfo(String dicID) {
		// 删除项目视图
		if (dicID.indexOf(IDataDictBO.PRJ) == 0) {
			String setYear = SessionUtil.getUserInfoContext().getSetYear();
			// 得到视图名称
			try {
				String veiwName = DBSqlExec
						.getStringValue(
								"select TABLEENAME from fb_dict_info where dicid =? ",
								new Object[] { dicID });
				dao.executeBySql("drop view " + veiwName);
				dao
						.executeBySql("delete fb_s_offline_update where OBJECT_TYPE='SYBASE_SCRIPT' and OBJECT_NAME='"
								+ veiwName + "'");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int retCode = -1;
		String sql = "delete from fb_dict_info where dicid='" + dicID + "'";
		retCode = dao.executeBySql(sql);
		if (retCode >= 0) {
			String sqlSub = "delete from fb_dict_info_detail where dicid='"
					+ dicID + "'";
			retCode = dao.executeBySql(sqlSub);
		}

		return retCode;
	}

	/**
	 * 判断表格类型的编号是否存在
	 * 
	 * @param typeCodeObje
	 * @return
	 */
	public boolean isCodeExist(String typeCodeObje) {
		String sql = "select * from fb_dict_type where Type_code='"
				+ typeCodeObje + "'";
		List tempList = dao.findBySql(sql);
		return tempList != null && tempList.size() > 0;
	}

	/**
	 * 增加类款项，为了不让用户在查询要类款项时不操作函数，在此将主动添加此字段， 只要表中存在ACCT_CODE字段，就进行此操作
	 * 
	 * @param reportID
	 * @param setYear
	 */
	public void addLKX(String tableName, String dicID, String setYear)
			throws Exception {
		String sSql = "select afield_sort from fb_dict_info_detail where table_ename='"
				+ tableName
				+ "' and upper(afield_ename)='"
				+ FIELD_ACCT_CODE
				+ "' and dicid='" + dicID + "'";
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return;
		String field_sort = BaseUtil.getAStringField(lstData, "afield_sort");
		if (Common.isNullStr(field_sort))
			throw new Exception("没有找到编码");
		// 添加
		List lstSql = new ArrayList();
		// 先将排序号后的序号更新
		sSql = "update fb_dict_info_detail set afield_sort=afield_sort+6 where table_ename='"
				+ tableName
				+ "' and afield_sort>"
				+ field_sort
				+ " and dicid='" + dicID + "'";
		lstSql.add(sSql);
		lstSql.add("insert into fb_dict_info_detail"

		+ "(dicid,TABLE_ENAME,"// 类
				+ "AFIELD_SORT," + "AFIELD_CNAME," + "AFIELD_ENAME,"
				+ "AFIELD_TYPE," + "AIS_VISIBLE," + "REFCOL_ID,"
				+ "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)" + "values('"
				+ dicID + "','" + tableName.toUpperCase() + "'," + field_sort
				+ "+1,'类','subStr(ACCT_CODE,1,3)','字符型'"
				+ ",'是',null,null,null,null," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "')");
		lstSql.add("insert into fb_dict_info_detail" + "(dicid,TABLE_ENAME,"// 款
				+ "AFIELD_SORT," + "AFIELD_CNAME," + "AFIELD_ENAME,"
				+ "AFIELD_TYPE," + "AIS_VISIBLE," + "REFCOL_ID,"
				+ "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)" + "values('"
				+ dicID + "','" + tableName.toUpperCase() + "'," + field_sort
				+ "+2,'款','subStr(ACCT_CODE,4,2)','字符型'"
				+ ",'是',null,null,null,null ," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "')");
		lstSql.add("insert into fb_dict_info_detail" + "(dicid,TABLE_ENAME,"// 项
				+ "AFIELD_SORT," + "AFIELD_CNAME," + "AFIELD_ENAME,"
				+ "AFIELD_TYPE," + "AIS_VISIBLE," + "REFCOL_ID,"
				+ "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)" + "values('"
				+ dicID + "','" + tableName.toUpperCase() + "'," + field_sort
				+ "+3,'项','subStr(ACCT_CODE,6,2)','字符型'"
				+ ",'是',null,null,null,null ," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "')");
		lstSql.add("insert into fb_dict_info_detail" + "(dicid,TABLE_ENAME,"// 类和款
				+ "AFIELD_SORT," + "AFIELD_CNAME," + "AFIELD_ENAME,"
				+ "AFIELD_TYPE," + "AIS_VISIBLE," + "REFCOL_ID,"
				+ "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)" + "values('"
				+ dicID + "','" + tableName.toUpperCase() + "'," + field_sort
				+ "+4,'类款','subStr(ACCT_CODE,1,5)','字符型'"
				+ ",'是',null,null,null,null ," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "')");
		lstSql.add("insert into fb_dict_info_detail" + "(dicid,TABLE_ENAME,"// 款和项
				+ "AFIELD_SORT," + "AFIELD_CNAME," + "AFIELD_ENAME,"
				+ "AFIELD_TYPE," + "AIS_VISIBLE," + "REFCOL_ID,"
				+ "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)" + "values('"
				+ dicID + "','" + tableName.toUpperCase() + "'," + field_sort
				+ "+5,'款项','subStr(ACCT_CODE,4,4)','字符型'"
				+ ",'是',null,null,null,null ," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "')");
		lstSql.add("insert into fb_dict_info_detail" + "(dicid,TABLE_ENAME,"// 编码加单位
				+ "AFIELD_SORT," + "AFIELD_CNAME," + "AFIELD_ENAME,"
				+ "AFIELD_TYPE," + "AIS_VISIBLE," + "REFCOL_ID,"
				+ "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)" + "values('"
				+ dicID + "','" + tableName.toUpperCase() + "'," + field_sort
				+ "+6,'科目编码和名称','ACCT_CODE||ACCT_NAME','字符型'"
				+ ",'是',null,null,null,null ," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "')");

		QueryStub.getQueryTool().executeBatch(lstSql);

	}

	/**
	 * 增加单位名称和编码字段
	 * 
	 */
	public void addDivCodeName(String tableName, String setYear, String dicID)
			throws Exception {
		String sSql = "select afield_sort from fb_dict_info_detail where table_ename='"
				+ tableName
				+ "' and upper(afield_ename)='"
				+ FIELD_DIV_CODE
				+ "' and dicid='" + dicID + "'";
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return;
		String field_sort = BaseUtil.getAStringField(lstData, "afield_sort");

		if (Common.isNullStr(field_sort))
			throw new Exception("没有找到编码");
		// 更新编号
		sSql = "update fb_dict_info_detail set afield_sort=afield_sort+1 where table_ename='"
				+ tableName
				+ "' and afield_sort>"
				+ field_sort
				+ " and dicid='" + dicID + "'";
		dao.executeBySql(sSql);
		// 添加

		sSql = "insert into fb_dict_info_detail" + "(dicid,TABLE_ENAME,"// 编码加单位
				+ "AFIELD_SORT," + "AFIELD_CNAME," + "AFIELD_ENAME,"
				+ "AFIELD_TYPE," + "AIS_VISIBLE," + "REFCOL_ID,"
				+ "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)" + "values('"
				+ dicID + "','" + tableName.toUpperCase() + "'," + field_sort
				+ "+1,'单位编码和名称','DIV_CODE||DIV_NAME','字符型'"
				+ ",'是',null,null,null,null," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "')";
		dao.executeBySql(sSql);
	}

	// // 查询一个数据源的字段类型信息，用于缓存
	// public XMLData getFieldAndType(String sObjectName, String setYear) {
	// String sSql = "select AFIELD_ENAME,AFIELD_TYPE from fb_dict_info_detail
	// where TABLE_ENAME='"
	// + sObjectName + "' ";
	// List lstData = dao.findBySql(sSql);
	// if (lstData == null || lstData.size() == 0)
	// return new XMLData();
	// XMLData xmlResult = new XMLData();
	// for (int i = 0; i < lstData.size(); i++) {
	// XMLData aData = (XMLData) lstData.get(i);
	// xmlResult.put(aData.get("afield_ename"), aData.get("afield_type"));
	//
	// }
	// return xmlResult;
	//
	// }

	/**
	 * 取得表类型选择模型
	 * 
	 * @return
	 */
	public String getTableTypeRefString() {
		String sql = "select TYPE_CODE CHR_CODE,TYPE_CODE chr_id,TYPE_CODE,TYPE_NAME chr_name,TYPE_NAME,MEMO,NULL parent_id from fb_dict_type ORDER BY TYPE_CODE";
		List tabTypeList = dao.findBySql(sql);

		StringBuffer tabType = new StringBuffer("#");
		XMLData tempData;
		for (int i = 0; i < tabTypeList.size(); i++) {
			tempData = (XMLData) tabTypeList.get(i);
			if (tempData.get("type_code") != null
					&& !tempData.get("type_code").toString().equals("")
					&& tempData.get("type_name") != null
					&& !tempData.get("type_name").toString().equals("")) {
				tabType.append("+")
						.append(tempData.get("type_code").toString()).append(
								"#").append(
								tempData.get("type_name").toString());
			}
		}
		return tabType.toString();
	}

	/**
	 * 取得数据字典中的明细字段信息
	 * 
	 * @param sTableID表的英文名
	 * @return
	 */
	public List getDicField(String sTableID, String setYear) {
		String sSql = "select TABLE_ENAME,AFIELD_CNAME,AFIELD_TYPE,AIS_VISIBLE,AFIELD_ENAME,"
				+ "AFIELD_SORT,REFCOL_ID,REFCOL_NAME,CON_FIELDCNAME,CON_FIELDENAME,dicid,amemo "
				+ "from fb_dict_info_detail where dicid='"
				+ sTableID
				+ "'  "
				+ " order by length(AFIELD_SORT),AFIELD_SORT";
		return dao.findBySql(sSql);
	}

	/**
	 * 取得所有的参考列选择项
	 * 
	 * @return
	 */
	public String getAllRefFields(String setYear) {
		List lstData = getRefData(setYear);
		if (lstData == null || lstData.size() == 0)
			return "#";
		int iCount = lstData.size();
		StringBuffer sb = new StringBuffer();
		sb.append("#");
		for (int i = 0; i < iCount; i++) {
			Map aData = (Map) lstData.get(i);
			sb.append("+" + BaseUtil.getAStringField(aData, "code_field") + "#"
					+ BaseUtil.getAStringField(aData, "code_field") + "+"
					+ BaseUtil.getAStringField(aData, "name_field") + "#"
					+ BaseUtil.getAStringField(aData, "name_field"));

		}
		return sb.toString();

	}

	/**
	 * 取得所有的支持数据源的表
	 * 
	 * @param logMode
	 * @return
	 */
	public List getTableList(int logMode) {

		String sSql = " select * from(select case  when tableCname is  null "
				+ "then ' ' else tableCname end as chr_name,"
				+ "' ' as chr_code,tablecname,"
				+ " DICID,tableEname,isinuse,isAudit,aMemo ,"
				+ "0 as isClass,tabprop as parent_id,dicid "
				+ "chr_id,lvl_id,tableEname_div,isSOURCE,ISSENT,ISBATCHNO,sup_ver,isdefault"
				+ " from fb_dict_info "
				+ " union all "
				+ " select type_name as Chr_name,' ' as chr_code,"
				+ "'','','',null,null,'',1 as isClass,null parent_id,type_code as chr_id,type_code,null,null,null,null,null,null "
				+ " from FB_DICT_TYPE ) aa order by aa.lvl_id";

		return dao.findBySql(sSql);

	}

	/**
	 * 保存数据，因为不可以添加和删除字段，所以只是更新某些字段
	 * 
	 * @param xmlMain
	 *            主表信息
	 * @param lstFields
	 *            明细表信息,主一定要排过序的
	 * @return
	 * @throws Exception
	 */
	public String saveDs(XMLData xmlMain, List lstFields) throws Exception {
		String dicid = BaseUtil.getAStringField(xmlMain, "dicid");
//		String dicid =xmlMain.get("dicid").toString();
		String tableName_Div = BaseUtil.getAStringField(xmlMain,
				"tableename_div");
		if (dicid == null)
			return "没有找到数据表信息";
		if (tableName_Div == null || tableName_Div.trim().equals(""))
			return "单位表名称不可以为空！";
		List lstSql = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("update fb_dict_info set tableename_div=").append(
				BaseUtil.getFieldQuoted(tableName_Div) + ",");
		sb.append("TABLECNAME=").append(
				BaseUtil.getFieldQuoted(BaseUtil.getAStringField(xmlMain,
						"tablecname"))).append(",");
		sb.append("ISINUSE=").append(
				BaseUtil.getAStringField(xmlMain, "isinuse")).append(",");
		sb.append("ISSOURCE=").append(
				BaseUtil.getAStringField(xmlMain, "issource")).append(",");
		sb.append("ISAUDIT=").append(
				BaseUtil.getAStringField(xmlMain, "isaudit")).append(",");
		sb.append("ISSENT=")
				.append(BaseUtil.getAStringField(xmlMain, "issent"))
				.append(",");
		sb.append("ISBATCHNO=").append(
				BaseUtil.getAStringField(xmlMain, "isbatchno")).append(",");
		sb.append("sup_ver=").append(
				BaseUtil.getAStringField(xmlMain, "sup_ver"));
		sb.append(" where dicid=").append(BaseUtil.getFieldQuoted(dicid));
		lstSql.add(sb.toString());
		int iCount = lstFields.size();
		DicFieldInfo aField;
		for (int i = 0; i < iCount; i++) {
			XMLData data = (XMLData) lstFields.get(i);

			int fieldSort = 0;
			if (data.get(IDataDictBO.AFIELD_SORT) != null
					&& !Common.isNullStr(data.get(IDataDictBO.AFIELD_SORT)
							.toString())) {
				fieldSort = Integer.parseInt(data.get(IDataDictBO.AFIELD_SORT)
						.toString());
			}
			aField = new DicFieldInfo((XMLData) lstFields.get(i), fieldSort);
			String sErr = aField.check();
			if (!sErr.equals(""))
				return sErr;
			lstSql.add(aField.getUpdateSql());
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
		return "";
	}

	/**
	 * 取得枚举源的原始数据
	 */
	public List getRefData(String setYear) {
		String sSql = "select refcol_id,refcol_name,CODE_FIELD,"
				+ "NAME_FIELD,PRIMARY_FIELD from fb_s_refcol ";
		return dao.findBySql(sSql);
	}

	/**
	 * 取得枚举源的主选择项模型字串
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRefDataString(String setYear) throws Exception {
		List lstData = getRefData(setYear);
		if (lstData == null || lstData.size() == 0)
			return "#";
		int iCount = lstData.size();
		StringBuffer sb = new StringBuffer();
		sb.append("#");
		for (int i = 0; i < iCount; i++) {
			Map aData = (Map) lstData.get(i);
			if (BaseUtil.getAStringField(aData, "refcol_id") == null
					|| BaseUtil.getAStringField(aData, "refcol_name") == null) {
				throw new Exception("引用列编码或名称为空，请检查。");
			}
			sb.append("+").append(BaseUtil.getAStringField(aData, "refcol_id"))
					.append("#").append(
							BaseUtil.getAStringField(aData, "refcol_name"));

		}
		return sb.toString();

	}

	/**
	 * 取得枚举源的字段选择模型数据,返回的数据类型为：key:REFCOL_ID,value:选择项String
	 * 
	 * @return
	 */
	public XMLData getRefDataFieldSelection(String setYear) {
		List lstData = getRefData(setYear);
		if (lstData == null || lstData.size() == 0)
			return new XMLData();
		int iCount = lstData.size();
		XMLData xmlResult = new XMLData();
		for (int i = 0; i < iCount; i++) {
			Map aData = (Map) lstData.get(i);
			String sRef = "#+" + BaseUtil.getAStringField(aData, "code_field")
					+ "#" + BaseUtil.getAStringField(aData, "code_field") + "+"
					+ BaseUtil.getAStringField(aData, "name_field") + "#"
					+ BaseUtil.getAStringField(aData, "name_field");
			xmlResult.put(BaseUtil.getAStringField(aData, "refcol_id"), sRef);
		}
		return xmlResult;
	}

	/**
	 * 取得数据库表中定义的表和视图
	 * 
	 * @param isALl,,iModel :
	 * @return
	 */
	public List getTableInfo(int iModel, boolean isAll, String setYear) {
		// 普通表和视图
		if (iModel == TABLE_MODEL) {

			String sSql = "select null as parent_ID,'001' chr_id, '表选择' chr_name,' ' chr_code,1 isclass,1 isinuse, null tableename,'' as type,'' as report_id "
					+ " from dual union all ";
			sSql = sSql
					+ "select '001' as parent_ID,t.table_name as chr_id,t.table_name as chr_name,' ' as chr_code,0 isclass,0 isinuse, t.table_name as Tableename "
					+ " ,case table_type when 'TABLE' then 'T' else 'V' end   ,'' as report_Id"
					+ " from user_tab_comments t "
					+ "where (table_name like 'RP_%' or table_name like 'VW_RP_%') ";
			sSql = sSql + " and   not exists (select tableename   "
					+ " from fb_dict_info a where  a.tableename=t.table_name) ";

			if (isAll) {
				sSql = sSql
						+ " union all select '001' as parent_ID,e.table_name as chr_id,e.table_name as chr_name,' ' "
						+ "as chr_code,0 isclass,1 isinuse, e.table_name as Tableename "
						+ " ,case table_type when 'TABLE' then 'T' else 'V' end ,'' as report_id "
						+ " from user_tab_comments e,fb_dict_info f "
						+ "where ( table_name like 'RP_%' or table_name like 'VW_RP_%') ";
				sSql = sSql + " and f.tableename=e.table_name ";

			}

			sSql = "select * from (" + sSql + ") order by chr_name";

			return dao.findBySql(sSql);

		} else// 是已定义表
		{
			StringBuffer sbSql = new StringBuffer();

			sbSql
					

					

					.append(
							"select null as parent_id,'100' as chr_id,' ' as chr_code, ")
					.append("   '滚动项目表' as chr_name, ")
					.append("  'fb_u_ir_master' as tableename, ")
					.append(" 1 as isclass, ")
					.append(" 1 as isinuse ,")
					.append(" 'fb_u_ir_master' as classname,'' as Report_id ")
					.append(" from dual ")
					.append("union ")
					.append(
							"select '100' as parent_id,report_id as chr_id,' ' as chr_code, ")

					.append("  report_sName as chr_name, ")
					.append(" report_vname as tableename, ")
					.append(" 0 as isclass, ")
					.append(" 0 as isinuse ,")
					.append(

					" 'fb_u_ir_master' as classname,report_id ")
					.append(" from fb_u_ir_master ")
//					.append(" where set_year=" + setYear)
					.append(
							" where report_id not in (select  dicid  "
									+ " from fb_dict_info ) ");
//									where set_year="
//									+ setYear + "" +
//											")");

				

					

			if (isAll)
				sbSql
				        .append("union ")
						.append(
								"select '100' as parent_id,report_id as chr_id,' ' as chr_code, ")

						.append("  report_sName as chr_name, ")
						.append(" report_vname as tableename, ")
						.append(" 0 as isclass, ")
						.append(" 1 as isinuse ,")
						.append(

						" 'fb_u_ir_master' as classname ,report_id ")
						.append("from fb_u_ir_master ")
//						.append(" where set_year=" + setYear)
						.append(
								" where report_id  in (select  dicid  "
										+ "from fb_dict_info  )");
						

			return dao.findBySql(sbSql.toString());
		}
	}

	/**
	 * 增加一个已定义的表信息
	 * 
	 * @param tableName
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String makeNewTableByDefine(String tableName, String type,
			String tabProp, String reportID, String setYear) throws Exception {
		// 检查是否已使用
		String sErr = checkTableDefine(tableName, reportID, setYear);
		if (!sErr.equals(""))
			return sErr;
		if (type.toUpperCase().equals(TABLE_DEFINE_BASE))
			addBaseTable(tableName, tabProp, reportID, setYear);
		if (type.toUpperCase().equals(TABLE_DEFINE_BUDGET))
			addBudgetTable(tableName, tabProp, reportID, setYear);
		if (type.toUpperCase().equals(TABLE_DEFINE_USER))
			addUserDefineTable(tableName, tabProp, reportID, setYear);
		updateSerNum(tableName, reportID, setYear);
		addDivCodeName(tableName.toUpperCase(), SessionUtil
				.getUserInfoContext().getSetYear(), reportID);
		addLKX(tableName.toUpperCase(), reportID, setYear);
		addRefField(reportID, setYear);
		return "";
	}

	public String checkTableDefine(String sTableName, String report_id,
			String setYear) {
		if (sTableName == null || sTableName.equals(""))
			return "未指定表名称";
		String sSql;
		if (report_id == null || report_id.equals(""))
			sSql = "select 1 from fb_dict_info where tableename='"
					+ sTableName.toUpperCase() + "'";
		else
			sSql = "select 1 from fb_dict_info where tableename='"
					+ sTableName.toUpperCase() + "' and dicid='" + report_id
					+ "' ";
		List lstData = dao.findBySql(sSql);
		if (lstData != null && lstData.size() > 0)
			return "此数据名已使用不可以再添加！";
		return "";
	}

	private void addBaseTable(String sTableName, String tabProp,
			String report_id, String setYear) throws Exception {
		// 添加主表
		List lstSql = new ArrayList();
		String sOrder = getNewOrder(tabProp, setYear);
		// 判断表中文名称是否重复
		String tableCname = DBSqlExec.getStringValue(
				"select report_name from fb_b_set_repset where report_id =?",
				new Object[] { report_id });
		tableCname = this.getTableCname(tableCname, report_id);
		lstSql
				.add("insert into fb_dict_info"
						+ "(DICID,"
						+ "TABLECNAME,"
						+ "TABLEENAME,"
						+ "ISINUSE,"
						+ "ISSOURCE,"
						+ "ISAUDIT,"
						+ "ISSENT,TABPROP,TABTYPE,ISBATCHNO,TABLEENAME_DIV,SUP_VER,LVL_ID,set_year,LAST_VER,rg_code)"
						+ "select report_id as  DICID,'"
						+ tableCname
						+ "',upper(table_name),case when is_active='是' then 1 else 0 end ,1,0,0,'"
						+ tabProp
						+ "','T'"
						+ ",1,upper(table_name),0,'"
						+ sOrder
						+ "',"
						+ setYear
						+ ",'"
						+ Tools.getCurrDate()
						+ "','"
						+ SessionUtil.getUserInfoContext().getAttribute(
								"cur_region")
						+ "'  from fb_b_set_repset where report_id='"
						+ report_id + "'");
		lstSql.add("insert into fb_dict_info_detail"

		+ "(dicID," + "TABLE_ENAME," + "AFIELD_SORT," + "AFIELD_CNAME,"
				+ "AFIELD_ENAME," + "AFIELD_TYPE," + "AIS_VISIBLE,"
				+ "REFCOL_ID," + "REFCOL_NAME," + "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code,amemo)"

				+ "select " + BaseUtil.getFieldQuoted(report_id) + ",'"
				+ sTableName.toUpperCase()
				+ "',field_id,field_fname,field_ename,field_type"

				+ ",'是',null,null,null,null ," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "',lvl_id from fb_b_set_colset where "
				+ "  report_id ='" + report_id + "' and  is_leaf='是'");
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	private void addBudgetTable(String sTableName, String tabProp,
			String report_id, String setYear) throws Exception {
		// 添加主表
		List lstSql = new ArrayList();
		String sOrder = getNewOrder(tabProp, setYear);
		// 判断表中文名称是否重复
		String tableCname = DBSqlExec.getStringValue(
				"select table_cname from fb_s_ir_table where report_id =?",
				new Object[] { report_id });
		tableCname = this.getTableCname(tableCname, report_id);

		lstSql
				.add("insert into fb_dict_info"
						+ "(DICID,"
						+ "TABLECNAME,"
						+ "TABLEENAME,"
						+ "ISINUSE,"
						+ "ISSOURCE,"
						+ "ISAUDIT,"
						+ "ISSENT,TABPROP,TABTYPE,ISBATCHNO,TABLEENAME_DIV,SUP_VER,LVL_ID,set_year,LAST_VER,rg_code)"
						+ "select report_id as  DICID,'"
						+ tableCname
						+ "',upper(table_ename),case when is_hide=1 then 0 else 1 end ,1,0,0,'"
						+ tabProp
						+ "','T'"
						+ ",sup_batch,table_ename,0,'"
						+ sOrder
						+ "',"
						+ setYear
						+ ",'"
						+ Tools.getCurrDate()
						+ "','"
						+ SessionUtil.getUserInfoContext().getAttribute(
								"cur_region")
						+ "' from fb_s_ir_table where report_id='" + report_id
						+ "'");
		// 将级次字段放入备注字段中
		if (sTableName.toUpperCase().equals("FB_U_DIV_INCOMING"))// 收入征收计划表
			lstSql
					.add("insert into fb_dict_info_detail"

							+ "(DICID,"
							+ "TABLE_ENAME,"
							+ "AFIELD_SORT,"
							+ "AFIELD_CNAME,"
							+ "AFIELD_ENAME,"
							+ "AFIELD_TYPE,"
							+ "AIS_VISIBLE,"
							+ "REFCOL_ID,"
							+ "REFCOL_NAME,"
							+ "CON_FIELDCNAME,"
							+ "CON_FIELDENAME,set_year,LAST_VER,rg_code,amemo)"

							+ "(select "

							+ "report_id,'FB_U_DIV_INCOMING' ,case when length(field_code)>4 then substr(field_code,length(field_code)-4,4)else field_code end as field_code,"
							+ "field_fname,field_ename,data_type,case when is_hide=1 then '否' else '是' end,null,null,null,null ,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region")
							+ "',lvl_id from fb_s_ir_fixcolinfo t where  report_id='400002' and end_flag=1 "
							+ " union "
							+ " select "
							+ BaseUtil.getFieldQuoted(report_id)
							+ " ,  'FB_U_DIV_INCOMING' , case when length(lvl_id)>4 then substr(lvl_id,length(lvl_id)-4,4)else lvl_id end as field_code,"
							+ "inccol_fname as field_fname,inccol_ename as field_ename,"
							+ "'浮点型' as data_type,case when is_hide=1 then '否' else '是' end,null,null,null,null,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region")
							+ "',lvl_id from fb_iae_inccolumn where end_flag=1 )");
		else if (sTableName.toUpperCase().equals("FB_U_DIV_INCOMING_BUDGET")) {// 收入预算表
			lstSql
					.add("insert into fb_dict_info_detail"
							+ "(DICID,"
							+ "TABLE_ENAME,"
							+ "AFIELD_SORT,"
							+ "AFIELD_CNAME,"
							+ "AFIELD_ENAME,"
							+ "AFIELD_TYPE,"
							+ "AIS_VISIBLE,"
							+ "REFCOL_ID,"
							+ "REFCOL_NAME,"
							+ "CON_FIELDCNAME,"
							+ "CON_FIELDENAME,set_year,LAST_VER,rg_code,amemo)"

							+ "(select report_id"

							+ ",'FB_U_DIV_INCOMING_BUDGET' as object_ename,case when length(field_code)>4 then substr(field_code,length(field_code)-4,4)else field_code end as field_code,"
							+ "field_fname,field_ename,data_type,case when is_hide=1 then '否' else '是' end,null,"
							+ "null,null,null ,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region")
							+ "',lvl_id from fb_s_ir_fixcolinfo t where  report_id='400001' and end_flag=1 )");
		}

		else if (sTableName.toUpperCase().equals("VW_FB_U_PAYOUT_BUDGET"))// 支出预算表,基本支出预算表
			lstSql
					.add("insert into fb_dict_info_detail"
							+ "(DICID,"
							+ "TABLE_ENAME,"
							+ "AFIELD_SORT,"
							+ "AFIELD_CNAME,"
							+ "AFIELD_ENAME,"
							+ "AFIELD_TYPE,"
							+ "AIS_VISIBLE,"
							+ "REFCOL_ID,"
							+ "REFCOL_NAME,"
							+ "CON_FIELDCNAME,"
							+ "CON_FIELDENAME,set_year,LAST_VER,rg_code,amemo)"

							+ "(select "

							+ BaseUtil.getFieldQuoted(report_id)
							+ ","
							+ BaseUtil.getFieldQuoted(sTableName.toUpperCase())
							+ " as object_ename,case when length(field_code)>4 then substr(field_code,length(field_code)-4,4)else field_code end as field_code,"
							+ "field_fname,field_ename,data_type,case when is_hide=1 then '否' else '是' end,null,null,null,null ,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region")
							+ "',lvl_id from fb_s_ir_fixcolinfo t where  report_id='400003' and end_flag=1 "
							+ " union "
							+ " select "
							+ BaseUtil.getFieldQuoted(report_id)
							+ ","
							+ BaseUtil.getFieldQuoted(sTableName.toUpperCase())
							+ " as object_ename,"
							+ " case when length(lvl_id)>4 then substr(lvl_id,length(lvl_id)-4,4)else lvl_id end as field_code,pfs_fname as field_cname,"
							+ "pfs_ename as field_ename,'浮点型' as data_type,"
							+ "'是',null,null,null,null,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region")
							+ "',lvl_id from fb_iae_payout_fundsource where end_flag=1"
							+ " union "
							+ " select "
							+ BaseUtil.getFieldQuoted(report_id)
							+ ","
							+ BaseUtil.getFieldQuoted(sTableName.toUpperCase())
							+ " as object_ename,"
							+ " '' as field_code,'基本项目支出标记' as field_cname,"
							+ "'DATA_SRC' as field_ename,'字符型' as data_type,"
							+ "'是',null,null,null,null,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region") + "','' from dual)");

		else if (sTableName.toUpperCase().equals("FB_U_PAYOUT_GOV_PURCHASE"))// 政府采购预算
			lstSql
					.add("insert into fb_dict_info_detail"
							+ "(DICID,"
							+ "TABLE_ENAME,"
							+ "AFIELD_SORT,"
							+ "AFIELD_CNAME,"
							+ "AFIELD_ENAME,"
							+ "AFIELD_TYPE,"
							+ "AIS_VISIBLE,"
							+ "REFCOL_ID,"
							+ "REFCOL_NAME,"
							+ "CON_FIELDCNAME,"
							+ "CON_FIELDENAME,set_year,LAST_VER,rg_code,amemo)"

							+ "(select "

							+ "report_id,'FB_U_PAYOUT_GOV_PURCHASE' as object_ename,case when length(field_code)>4 then substr(field_code,length(field_code)-4,4)else field_code end as field_code,"
							+ "field_fname,field_ename,data_type,case when is_hide=1 then '否' else '是' end,null,null,null,null,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region")
							+ "',lvl_id  from fb_s_ir_fixcolinfo t where  report_id='400004' and end_flag=1 "
							+ " union "
							+ " select "
							+ BaseUtil.getFieldQuoted(report_id)
							+ ",'FB_U_PAYOUT_GOV_PURCHASE' as object_ename,"
							+ " case when length(lvl_id)>4 then substr(lvl_id,length(lvl_id)-4,4)else lvl_id end as field_code,pfs_fname as field_cname,"
							+ "pfs_ename as field_ename,'浮点型' as data_type,"
							+ "'是',null,null,null,null,"
							+ setYear
							+ ",'"
							+ Tools.getCurrDate()
							+ "','"
							+ SessionUtil.getUserInfoContext().getAttribute(
									"cur_region")
							+ "',lvl_id from fb_iae_payout_fundsource where end_flag=1)");

		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	private void addUserDefineTable(String sTableName, String tabProp,
			String report_id, String setYear) throws Exception {
		List lstSql = new ArrayList();
		String sOrder = getNewOrder(tabProp, setYear);
		// 判断表中文名称是否重复
		String tableCname = DBSqlExec.getStringValue(
				"select report_fname from fb_u_ir_master where report_id =?",
				new Object[] { report_id });
		tableCname = this.getTableCname(tableCname, report_id);
		// 插入主表
		lstSql
				.add("insert into fb_dict_info"
						+ "(DICID,"
						+ "TABLECNAME,"
						+ "TABLEENAME,"
						+ "ISINUSE,"
						+ "ISSOURCE,"
						+ "ISAUDIT,"
						+ "ISSENT,TABPROP,TABTYPE,ISBATCHNO,TABLEENAME_DIV,SUP_VER,LVL_ID,set_year,LAST_VER,rg_code)"
						+ "select report_id as  DICID,'"
						+ tableCname
						+ "',upper(report_vname),case when is_hide=1 then 0 else 1 end ,1,0,0,'"
						+ tabProp
						+ "','V'"
						+ ",sup_batch,upper(report_vname),0,'"
						+ sOrder
						+ "',"
						+ setYear
						+ ",'"
						+ Tools.getCurrDate()
						+ "','"
						+ SessionUtil.getUserInfoContext().getAttribute(
								"cur_region")
						+ "' from fb_u_ir_master where report_id='" + report_id
						+ "'");

		lstSql
				.add("insert into fb_dict_info_detail"
						+ "(dicID,"
						+ "TABLE_ENAME,"
						+ "AFIELD_SORT,"
						+ "AFIELD_CNAME,"
						+ "AFIELD_ENAME,"
						+ "AFIELD_TYPE,"
						+ "AIS_VISIBLE,"
						+ "REFCOL_ID,"
						+ "REFCOL_NAME,"
						+ "CON_FIELDCNAME,"
						+ "CON_FIELDENAME,set_year,LAST_VER,rg_code,amemo)"
						+ "select "
						+ "report_id,'"
						+ sTableName.toUpperCase()
						+ "',field_id,field_fname,field_ename,data_type"

						+ ",case when is_hidecol=1 then 0 else 1 end ,null,null,null,null,"
						+ setYear
						+ ",'"
						+ Tools.getCurrDate()
						+ "','"
						+ SessionUtil.getUserInfoContext().getAttribute(
								"cur_region")
						+ "',COL_LABEL||field_id from fb_u_ir_colinfo where "
						+  " and report_id ='" + report_id
						+ "' and isleaf='1'");

		QueryStub.getQueryTool().executeBatch(lstSql);

	}

	/**
	 * 得到不重复的表中文名称
	 * 
	 * @param tableCname
	 * @param reportId
	 * @return
	 * @throws Exception
	 */
	private String getTableCname(String tableCname, String reportId)
			throws Exception {
		if (this.isExistTableCname(tableCname, reportId)) {
			int i = 1;
			while (true) {
				if (!this.isExistTableCname(tableCname + "[" + i + "]",
						reportId)) {
					return tableCname + "[" + i + "]";
				} else {
					i++;
				}
			}
		} else {
			return tableCname;
		}
	}

	public void makeListTable(List lstSelect, boolean isDefine, String tabProp,
			String setYear) throws Exception {
		int iCount = lstSelect.size();
		String sErr;
		if (isDefine) {

			for (int i = 0; i < iCount; i++) {
				XMLData aData = (XMLData) lstSelect.get(i);
				// if (aData.get("CHR_CODE").toString().length() == 3)
				// continue;
				// 判断增加是否是项目，项目特殊处理
				String type = ((String) aData.get("classname"));
				if (IDataDictBO.PRJ.equals(type)) {
					sErr = makeNewTableByPrj((String) aData.get("tableename"),
							(String) aData.get("chr_name"), tabProp,
							(String) aData.get("report_id"), setYear);
				} else {
					sErr = makeNewTableByDefine((String) aData
							.get("tableename"),
							(String) aData.get("classname"), tabProp,
							(String) aData.get("report_id"), setYear);
				}
				if (!sErr.equals(""))
					throw new Exception(sErr);
			}
		} else {
			for (int i = 0; i < iCount; i++) {
				XMLData aData = (XMLData) lstSelect.get(i);
				sErr = makeNewTable((String) aData.get("tableename"),
						(String) aData.get("type"), tabProp, setYear);
				if (!sErr.equals(""))
					throw new Exception(sErr);
			}
		}

	}

	public String makeNewTable(String sTableName, String type, String tabProp,
			String setYear) throws Exception {
		// 检查此表是否已经使用
		String sErr = checkTableDefine(sTableName, null, setYear);
		if (!sErr.equals(""))
			return sErr;
		String dicID = makeViewInfo(sTableName, type, tabProp, setYear);
		addDivCodeName(sTableName.toUpperCase(), SessionUtil
				.getUserInfoContext().getSetYear(), dicID);
		addLKX(sTableName.toUpperCase(), dicID, setYear);
		addRefField(dicID, setYear);
		return "";
	}

	private String[][] makeViewInfo_A(String sViewname, String type,
			String tabProp, String setYear, String uuid) throws Exception {
		String[][] cols = genColInfo(sViewname);
		if (cols == null || cols.length < 1)
			throw new Exception("没有找到相关表的信息");
		List lstSql = new ArrayList();

		String newOrder = getNewOrder(tabProp, setYear);
		// 先插入主表信息
		lstSql
				.add("insert into fb_dict_info"
						+ "(DICID,"
						+ "TABLECNAME,"
						+ "TABLEENAME,"
						+ "ISINUSE,"
						+ "ISSOURCE,"
						+ "ISAUDIT,"
						+ "ISSENT,TABPROP,TABTYPE,ISBATCHNO,TABLEENAME_DIV,SUP_VER,LVL_ID,set_year,LAST_VER,rg_code)"
						+ "values('"
						+ uuid
						+ "','未命名"
						+ sViewname
						+ "','"
						+ sViewname
						+ "',1,1,0,0,'"
						+ tabProp
						+ "','"
						+ type
						+ "'"
						+ ",'1','"
						+ sViewname
						+ "',0,'"
						+ newOrder
						+ "',"
						+ setYear
						+ ",'"
						+ Tools.getCurrDate()
						+ "','"
						+ SessionUtil.getUserInfoContext().getAttribute(
								"cur_region") + "')");
		int iCount = cols.length;// 不知道为什么除以2
		for (int i = 0; i < iCount; i++) {

			lstSql.add("insert into fb_dict_info_detail"
					+ "(dicid,TABLE_ENAME,"
					+ "AFIELD_SORT,"
					+ "AFIELD_CNAME,"
					+ "AFIELD_ENAME,"
					+ "AFIELD_TYPE,"
					+ "AIS_VISIBLE,"
					+ "REFCOL_ID,"
					+ "REFCOL_NAME,"
					+ "CON_FIELDCNAME,"
					+ "CON_FIELDENAME,set_year,LAST_VER,rg_code)"
					+ "values("

					+ "'"
					+ uuid
					+ "','"
					+ sViewname
					+ "',"
					+ i
					+ ",'未命名字段','"
					+ cols[i][0]
					+ "','"
					+ cols[i][1]
					+ "','是',null,null,null,null,"
					+ setYear
					+ ",'"
					+ Tools.getCurrDate()
					+ "','"
					+ SessionUtil.getUserInfoContext().getAttribute(
							"cur_region") + "')");
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
		return cols;
	}

	private String makeViewInfo(String sViewname, String type, String tabProp,
			String setYear) throws Exception {
		// 先取得结构，再分析
		String uuid = UUIDRandom.generate();
		makeViewInfo_A(sViewname, type, tabProp, setYear, uuid);
		return uuid;

	}

	/**
	 * 将Table的字段信息生成到指定的Set中去
	 * 
	 * @param tableName
	 * @param buffer
	 */
	protected synchronized String[][] genColInfo(String tableName)
			throws Exception {

		String sql = SQLTool.querySQL(tableName);
		Statement stmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().createStatement();
			rs = stmt.executeQuery(sql + " WHERE 1=2");
			ResultSetMetaData meta = rs.getMetaData();
			String[][] cols = new String[meta.getColumnCount()][2];
			for (int i = 0; i < meta.getColumnCount(); i++) {
				String columnName = meta.getColumnName(i + 1);
				cols[i][0] = columnName;
				cols[i][1] = getStringType(meta.getColumnType(i + 1));
				// 特殊处理整型字段
				if ("货币".equals(cols[i][1])
						&& (meta.getPrecision(i + 1) != 0 && meta
								.getScale(i + 1) == 0)) {
					cols[i][1] = "整型";
				}
			}
			return cols;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			dao.closeSession(session);
		}
	}

	/**
	 * 添加枚举字段的设置
	 * 
	 * @param dicID
	 */
	private void addRefField(String dicID, String setYear) throws Exception {
		// 使用更新语句
		String sSql = "select a.aField_ename,a.AFIELD_CNAME,"
				+ "b.refcol_id,b.name_field,b.code_field,b.refcol_name "
				+ " from fb_dict_info_detail a,FB_S_REFCOL b "
				+ " where (upper(a.AFIELD_ENAME)=upper(b.PRIMARY_FIELD) or upper(a.AFIELD_ENAME)=upper(b.name_field)) "
				+ " and a.DICID='" + dicID
				+ "' and b.Refcol_Kind=1 "
				+ " and b.refcol_id<>'" + this.PAYOUTKIND_REFCOLID + "'";

		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return;
		// 逐个更新
		int iCount = lstData.size();
		XMLData aData;
		String fieldEname, fieldCname, codeField, nameField, refcolID, refcolName;
		List lstSql = new ArrayList();
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstData.get(i);
			fieldEname = BaseUtil.getAStringField(aData, "afield_ename");
			fieldCname = BaseUtil.getAStringField(aData, "afield_cname");
			codeField = BaseUtil.getAStringField(aData, "code_field");
			nameField = BaseUtil.getAStringField(aData, "name_field");
			refcolID = BaseUtil.getAStringField(aData, "refcol_id");
			refcolName = BaseUtil.getAStringField(aData, "refcol_name");

			// 收入项目类别特殊处理
			if (IIncType.INCTYPE_CODE.equalsIgnoreCase(fieldEname)
					&& !this.INCTYPE_REFCOLID.equals(refcolID)) {
				continue;
			}
			if (fieldEname.toUpperCase().equals(codeField.toUpperCase())
					|| (!fieldEname.toUpperCase().equals(
							nameField.toUpperCase()))) {
				sSql = " update fb_dict_info_detail set REFCOL_ID='" + refcolID
						+ "',REFCOL_NAME='" + refcolName + "',CON_FIELDCNAME='"
						+ fieldCname + "',CON_FIELDENAME='" + codeField
						+ "' where dicid='" + dicID + "' and aField_ename='"
						+ fieldEname + "'  ";
			} else
				sSql = " update fb_dict_info_detail set REFCOL_ID='" + refcolID
						+ "',REFCOL_NAME='" + refcolName + "',CON_FIELDCNAME='"
						+ fieldCname + "',CON_FIELDENAME='" + nameField
						+ "' where dicid='" + dicID + "' and aField_ename='"
						+ fieldEname + "' " ;
			lstSql.add(sSql);
		}

		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	private String getNewOrder(String tabProp, String setYear) {
		String sSql = "select max(lvl_id)as chr_code from fb_dict_info where tabprop='"
				+ tabProp + "' ";
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.isEmpty())
			return "000001";
		String maxCode = BaseUtil.getAStringField(lstData, "chr_code");
		if (maxCode == null || maxCode.equals(""))
			return "000001";
		if (maxCode.length() > 6)
			maxCode = maxCode.substring(maxCode.length() - 6);
		Long lMax = null;
		try {
			lMax = new Long(maxCode);
		} catch (Exception e) {
			lMax = new Long("555555");
		}
		long max = lMax.longValue();
		max = max + 1;
		maxCode = String.valueOf(max);
		return ("000000" + maxCode)
				.substring(("000000" + maxCode).length() - 6);

	}

	public boolean sysRelTable(String sTableName, String setYear) {
		try {
			if (sTableName == null || sTableName.equals(""))
				return false;
			String[] arrRefTable = (String[]) xmlRefTable.get(sTableName
					.toUpperCase());
			if (arrRefTable == null)
				return false;
			int iCount = arrRefTable.length;
			String sSql;
			for (int i = 0; i < iCount; i++) {
				if (arrRefTable[i].equals(IDataDictBO.PAYOUT_RAE)) {// 控制数特殊处理
					sSql = "select dicid,tabprop,tableename from FB_DICT_INFO where dicid like '"
							+ arrRefTable[i].toUpperCase()
							+ "%' ";
					List lstData = dao.findBySql(sSql);
					if (lstData == null || lstData.isEmpty())
						continue;
					// 可能出现多个
					for (int j = 0; j < lstData.size(); j++) {
						XMLData aData = (XMLData) lstData.get(j);
						String dicID = BaseUtil.getAStringField(aData, "dicid");
						String tableEname = BaseUtil.getAStringField(aData,
								"tableename");
						// 先删除资金来源字段
						sSql = "delete FB_DICT_INFO_Detail where dicid='"
								+ dicID
								+ "' and AFIELD_ENAME like 'F%' and (AFIELD_TYPE='货币' or AFIELD_TYPE='浮点型') ";
						dao.executeBySql(sSql);
						this.addPfs(tableEname, dicID, setYear);
					}
				} else if (arrRefTable[i].equals(IDataDictBO.PRJ_PFS)) {// 项目明细
					sSql = "select dicid,tabprop,tableename,tablecname from FB_DICT_INFO where dicid like '"
							+ arrRefTable[i].toUpperCase()
							+ "%'";

					List lstData = dao.findBySql(sSql);
					if (lstData == null || lstData.isEmpty())
						continue;

					// 可能出现多个
					for (int j = 0; j < lstData.size(); j++) {
						XMLData aData = (XMLData) lstData.get(j);
						String dicID = BaseUtil.getAStringField(aData, "dicid");
						String tabProp = BaseUtil.getAStringField(aData,
								"tabprop");
						// 先删除
						sSql = "delete FB_DICT_INFO_Detail where dicid='"
								+ dicID + "' ";
						dao.executeBySql(sSql);
						sSql = "delete FB_DICT_INFO where dicid='" + dicID
								+ "'";
						dao.executeBySql(sSql);

						makeNewTableByPrj((String) aData.get("tableename"),
								(String) aData.get("tablecname"), tabProp,
								dicID, setYear);
					}

				} else {
					// 先检查相对应的表有没有添加到数据源中
					// 其特征是DICID为六位,表名一致
					sSql = "select dicid,tabprop from FB_DICT_INFO where length(dicid)=6 and tableename='"
							+ arrRefTable[i].toUpperCase()
							+ "' ";

					List lstData = dao.findBySql(sSql);
					if (lstData == null || lstData.isEmpty())
						continue;

					// 可能出现多个
					for (int j = 0; j < lstData.size(); j++) {
						XMLData aData = (XMLData) lstData.get(j);
						String dicID = BaseUtil.getAStringField(aData, "dicid");
						String tabProp = BaseUtil.getAStringField(aData,
								"tabprop");

						// 基本支出预算表要特处理
						String tableType = getTableType(arrRefTable[i], setYear);

						if (tableType == null || tableType.equals(""))
							continue;
						// 先删除
						sSql = "delete FB_DICT_INFO_Detail where dicid='"
								+ dicID + "' ";
						dao.executeBySql(sSql);
						sSql = "delete FB_DICT_INFO where dicid='" + dicID
								+ "'  ";
						dao.executeBySql(sSql);

						makeNewTableByDefine(arrRefTable[i], tableType,
								tabProp, dicID, setYear);

					}
				}

			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}

	}

	/**
	 * 查询一指定表所在的类别
	 * 
	 * @param tableName
	 * @return
	 */
	private String getTableType(String tableName, String setYear) {
		String sSql = "select distinct 'FB_B_SET_REPSET'as chr_name from FB_B_SET_REPSET where upper(table_name)='"
				+ tableName.toUpperCase()
				+ "' "
				+ " union all "
				+ " select distinct 'FB_S_IR_TABLE' from FB_S_IR_TABLE where upper(table_ename)='"
				+ tableName.toUpperCase()
				+ "'"
				+ " union all"
				+ " select distinct 'FB_U_IR_MASTER' from FB_U_IR_MASTER where upper(report_vname)='"
				+ tableName.toUpperCase() + "' ";

		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.isEmpty())
			return "";
		return BaseUtil.getAStringField(lstData, "chr_name");

	}

	/**
	 * 保存字典类型
	 * 
	 * @param typeCode
	 *            类型编号
	 * @param ypeName
	 *            类型名称
	 * @param memo
	 *            注释 *
	 * @return : <=0 插入失败；1：插入成功，2：已存在该编码
	 */
	public int addDictType(String typeCode, String typeName, String memo) {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");

		String countSQL = "select * from fb_dict_type where TYPE_CODE = '"
				+ typeCode + "'";
		List dataList = dao.findBySql(countSQL);
		if (dataList != null && dataList.size() > 0)
			return 2;

		StringBuffer sql = new StringBuffer(
				"insert into fb_dict_type (TYPE_CODE,TYPE_NAME,MEMO,SET_YEAR,RG_CODE) values ('");
		sql.append(typeCode.trim()).append("','");
		sql.append(typeName.trim()).append("','");
		sql.append(memo.trim()).append("',");
		sql.append(setYear).append(",'");
		sql.append(sRgCode).append("')");
		return dao.executeBySql(sql.toString());
	}

	/**
	 * 
	 * 删除字典类型
	 * 
	 * @param dictCode
	 *            编号
	 */
	public int deleteDictType(String dictCode) {
		String sql = "delete from fb_dict_type where TYPE_CODE ='" + dictCode
				+ "'";
		return dao.executeBySql(sql);
	}

	/**
	 * 更新字典类型
	 * 
	 * @param typeCode
	 *            类型编号
	 * @param ypeName
	 *            类型编号
	 * @param tableInfoData
	 *            注释
	 */
	public int updateDictType(String typeCode, String ypeName, String memo) {
		StringBuffer sql = new StringBuffer(
				"update fb_dict_type set TYPE_NAME='");
		sql.append(ypeName).append("', MEMO='");
		sql.append(memo).append("' where TYPE_CODE = '").append(typeCode)
				.append("'");
		return dao.executeBySql(sql.toString());
	}

	/**
	 * 交换两个表的顺序
	 * 
	 * @param xmlFirst
	 * @param xmlSecond
	 * @return
	 */
	public boolean changeOrder(XMLData xmlFirst, XMLData xmlSecond,
			String setYear) {
		if (xmlFirst == null || xmlSecond == null)
			return false;
		List lstSql = new ArrayList();
		String sSql = "update FB_DICT_INFO set lvl_id='"
				+ BaseUtil.getAStringField(xmlSecond, "lvl_id")
				+ "' where dicid='"
				+ BaseUtil.getAStringField(xmlFirst, "dicid")
				+ "' ";
		lstSql.add(sSql);

		sSql = "update FB_DICT_INFO set lvl_id='"
				+ BaseUtil.getAStringField(xmlFirst, "lvl_id")
				+ "' where dicid='"
				+ BaseUtil.getAStringField(xmlSecond, "dicid")
				+ "' ";
		lstSql.add(sSql);
		try {
			QueryStub.getQueryTool().executeBatch(lstSql);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 取得字典表的主表信息
	 * 
	 * @param tableID
	 *            表在字典表中的ID
	 * @return
	 */
	public XMLData getTableMainInfo(String tableID, String setYear) {
		if (tableID == null)
			return null;
		String sSql = "select * from FB_DICT_INFO where dicid ='" + tableID
				+ "' ";
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return null;
		return (XMLData) lstData.get(0);

	}

	/**
	 * 取得数据源，建树用，以指定父结点的方式建树。将显示英文字段 数据分为三级，一级是分类，二级是数据表名，三级是字段名
	 * 
	 * @return
	 */
	public List getDataSourceForTree(String setYear) {
		String sSql = null;

		String loginMode = SessionUtil.getUserInfoContext().getLoginMode();
		if ("0".equals(loginMode)) {// 在线
			sSql = "   select a.table_ename,aField_cname,afield_type,aField_ename,refcol_id,con_fieldEname,a.dicid||a.afield_ename as chr_id,afield_ename as chr_code,afield_cname as  chr_name,a.dicid as parent_id ,1 isfield ,lpad(AFIELD_SORT,4,0) as lvl_id,a.dicid from FB_DICT_INFO_DETAIL a,FB_DICT_INFO b where a.dicid=b.DICID and b.ISSOURCE=1 and b.isinuse=1 ";
		} else {
			sSql = "   select a.table_ename,aField_cname,afield_type,aField_ename,refcol_id,con_fieldEname,a.dicid||a.afield_ename as chr_id,afield_ename as chr_code,afield_cname as  chr_name,a.dicid as parent_id ,1 isfield ,replicate(0,4-length(afield_sort))||afield_sort as lvl_id,a.dicid from FB_DICT_INFO_DETAIL a,FB_DICT_INFO b where a.dicid=b.DICID and b.ISSOURCE=1 and b.isinuse=1 ";
		}

		sSql = sSql
				+ " union all "
				+ " select tableename,tablecname,null,null,null,null,dicid as chr_id,tableename as chr_code,tablecname as chr_name,tabprop as parent_id,0 as isfield,lvl_id ,dicid from FB_DICT_INFO where issource=1 and isinuse=1 "
				+ " union all "
				+ " select null,null,null,null,null,null,type_code,type_code,type_name,null,-1 as isfield,type_code as lvl_id ,null from fb_dict_type where type_code in (select distinct tabprop from FB_DICT_INFO where issource=1 and isinuse=1) ";

		sSql = "select * from (" + sSql + ") order by lvl_id";
		return dao.findBySql(sSql);
	}

	// 查询一个数据源的字段类型信息，用于缓存
	public XMLData getFieldAndType(String dicID, String setYear) {
		String sSql = "select AFIELD_ENAME,AFIELD_TYPE from fb_dict_info_detail where dicid='"
				+ dicID + "' ";
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return new XMLData();
		XMLData xmlResult = new XMLData();
		for (int i = 0; i < lstData.size(); i++) {
			XMLData aData = (XMLData) lstData.get(i);
			xmlResult.put(aData.get("afield_ename"), aData.get("afield_type"));

		}
		return xmlResult;

	}

	/**
	 * 更新序号
	 * 
	 */
	private void updateSerNum(String tableName, String dicID, String setYear)
			throws Exception {
		String sSql = "select afield_ename from FB_DICT_INFO_DETAIL where dicid='"
				+ dicID
				+ "' "
				+ " order by amemo ";
		List lstData = dao.findBySql(sSql);
		if (lstData != null && !lstData.isEmpty()) {
			int iCount = lstData.size();
			for (int i = 0; i < iCount; i++) {
				XMLData aData = (XMLData) lstData.get(i);
				sSql = "update FB_DICT_INFO_DETAIL set aField_sort=" + (i + 1)
						+ " where dicid='" + dicID + "' and afield_ename='" + aData.get("afield_ename")
						+ "'";
				dao.executeBySql(sSql);
			}
		}
	}

	public static String getStringType(int fromSqlType) {
		return (String) SQL_TYPE_MAPPING_STRING
				.get(String.valueOf(fromSqlType));
	}

	/**
	 * 在数据字典中增加支出资金来源
	 * 
	 * @param sTableName
	 * @param report_id
	 * @param setYear
	 * @throws Exception
	 */
	private void addPfs(String sTableName, String dicID, String setYear)
			throws Exception {
		String sSql = "insert into fb_dict_info_detail"
				+ "(DICID,"
				+ "TABLE_ENAME,"
				+ "AFIELD_SORT,"
				+ "AFIELD_CNAME,"
				+ "AFIELD_ENAME,"
				+ "AFIELD_TYPE,"
				+ "AIS_VISIBLE,"
				+ "REFCOL_ID,"
				+ "REFCOL_NAME,"
				+ "CON_FIELDCNAME,"
				+ "CON_FIELDENAME,set_year,LAST_VER,rg_code,amemo)"

				+ "( select "
				+ BaseUtil.getFieldQuoted(dicID)
				+ ","
				+ BaseUtil.getFieldQuoted(sTableName.toUpperCase())
				+ " as object_ename,"
				+ " case when length(lvl_id)>4 then substr(lvl_id,length(lvl_id)-4,4)else lvl_id end as field_code,pfs_fname as field_cname,"
				+ "pfs_ename as field_ename,'浮点型' as data_type,"
				+ "'是',null,null,null,null," + setYear + ",'"
				+ Tools.getCurrDate() + "','"
				+ SessionUtil.getUserInfoContext().getAttribute("cur_region")
				+ "',lvl_id from fb_iae_payout_fundsource where end_flag=1)";
		dao.executeBySql(sSql);
		updateSerNum(sTableName, dicID, setYear);
	}

	/**
	 * 增加一个项目表
	 * 
	 * @param tableName
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private String makeNewTableByPrj(String tableName, String chrName,
			String tabProp, String reportID, String setYear) throws Exception {
		// 创建视图
		String createViewSql;
		if (reportID.indexOf(IDataDictBO.PRJ_PFS + "-001-") == 0) {// 项目明细(本年预算)
			createViewSql = PrjData.getCreatePrjPfsView(tableName, reportID,
					setYear);
		} else if (reportID.indexOf(IDataDictBO.PRJ_PFS + "-002-") == 0) {// 项目明细(本年预算含所有项目状态)
			createViewSql = PrjData.getCreatePrjPfsAllStateView(tableName,
					reportID, setYear);
		} else if (reportID.indexOf(IDataDictBO.PRJ_PFS + "-003-") == 0) {// 项目预算(项目总预算)
			createViewSql = PrjData.getCreatePrjPfsTotalView(tableName,
					reportID, setYear);
		} else if (reportID.indexOf(IDataDictBO.PRJ_PFS + "-004-") == 0) {// 项目预算(项目总预算含所有项目状态)
			createViewSql = PrjData.getCreatePrjPfsTotalAllStateView(tableName,
					reportID, setYear);
		} else if (reportID.indexOf(IDataDictBO.PRJ_MX) == 0) {// 项目费用明细
			createViewSql = PrjData.getCreatePrjMxViewSql(tableName, reportID,
					setYear);
		} else {
			return "无法解析的项目数据源";
		}
		QueryStub.getQueryTool().execute(createViewSql);
		// dao.executeBySql(createViewSql);

		// 将项目视图插入fb_s_offline_update表
		String loginmode = SessionUtil.getUserInfoContext().getLoginMode();
		if ("0".equals(loginmode)) {// 在线
			Session session = null;
			PreparedStatement smt = null;
			try {
				// 在此也行删除一下,保险 add by xxl
				String sSql = "delete fb_s_offline_update where object_type='SYBASE_SCRIPT' and object_name='"
						+ tableName + "'";
				dao.executeBySql(sSql);
				session = dao.getSession();
				Connection con = session.connection();
				sSql = "insert into fb_s_offline_update(object_type, object_name,OBJECT_VER,BIG_SCRIPT)"
						+ " values (?,?,?,empty_clob())";
				smt = con.prepareStatement(sSql);
				smt.setString(1, "SYBASE_SCRIPT");
				smt.setString(2, tableName);
				smt.setInt(3, 1);
				smt.executeUpdate();
			} finally {
				if (smt != null)
					smt.close();
				dao.closeSession(session);
			}
			// 处理createViewSql
			createViewSql = createViewSql.replaceFirst("or replace", "");
			createViewSql = "IF exists(select * from sysobjects where name='"
					+ tableName + "' and type='V')" + " drop view " + tableName
					+ "\n--go\n" + createViewSql;
			QueryStub.getQueryTool().updateClob("fb_s_offline_update",
					"big_script", "OBJECT_TYPE = ? and  OBJECT_NAME = ? ",
					new Object[] { "SYBASE_SCRIPT", tableName }, createViewSql);
		}

		// 增加数据源主从表信息
		String[][] cols = this.makeViewInfo_A(tableName, "V", tabProp, setYear,
				reportID);

		List lstSql = new ArrayList();
		// 更新主表中文名称信息
		lstSql.add("update fb_dict_info set TABLECNAME = '" + chrName
				+ "' where DICID='" + reportID + "' ");

		int inputSetId;
		String detailCode = null;
		if (reportID.indexOf(IDataDictBO.PRJ_PFS) == 0) {// 项目明细
			inputSetId = Integer.parseInt(reportID.substring(11));
		} else {// 项目费用明细
			String prjsortId = reportID.substring(6, 9);
			detailCode = reportID.substring(10);
			// 根据项目类别id得到对应的面板ID
			inputSetId = PrjData.getInputSetIdWithPrjSortId(prjsortId, setYear);

		}

		// 更新数据字段明细表列中文名称
		lstSql.addAll(PrjData.getUpdateFieldCnameSql(reportID, cols,
				inputSetId, detailCode, setYear));

		QueryStub.getQueryTool().executeBatch(lstSql);

		addDivCodeName(tableName.toUpperCase(), setYear, reportID);
		addLKX(tableName.toUpperCase(), reportID, setYear);
		addRefField(reportID, setYear);

		return "";
	}

	public boolean isExistTableCname(String cname, String reportId)
			throws Exception {
		String filter = "TABLECNAME = '" + cname + "' and DICID<>'" + reportId
				+ "'";
		int count = DBSqlExec.getRecordCount("fb_dict_info", filter);
		if (count > 0)
			return true;
		else
			return false;
	}
}
