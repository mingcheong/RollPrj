/**
 * title:  查询报表的数据操作
 * 
 * author : qzc
 * 
 * version :1.0
 */
package gov.nbcs.rp.sys.besqryreport.action;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.dicinfo.prjdetail.action.PrjDetailAction;

import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import gov.nbcs.rp.sys.besqryreport.ui.BesStub;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.PaperSize;
import com.fr.report.WorkSheet;
import com.fr.report.io.ExcelImporter;

public class BesAct {

	private IPubInterface pubserv;

	private int Type_NO;

	public static boolean afterSave; // 是否保存完毕

	public static int iTag = 0; // 标示：0为该界面重新打开，1：已经打开

	public static TableHeader tableHeader; // 导入获取的表头

	static IBesQryReport itserv; // 接口

	static Node node;

	PrjDetailAction PrjAct;

	public static String TableName_Mid1 = null;

	public static String TableName_Mid2 = null;

	public static SysCodeRule codeRule_Col = SysCodeRule
			.createClient(new int[] { 3, 6, 9, 12, 15, 18 });

	public static SysCodeRule codeRule_Main = SysCodeRule
			.createClient(new int[] { 6, 10, 14, 18 });

	/**
	 * 构造函数
	 */
	public BesAct() {
		itserv = BesStub.getMethod();
		PrjAct = new PrjDetailAction();
	}

	/**
	 * 获取业务编码
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getTypeNO() throws Exception {
		// pubserv = PubInterfaceStub.getMethod();
		// Type_NO = pubserv.getCurTypeNO();
		return 1;
	}

	// //////////////////////////////////////////////////////导入的相关函数///////////////////////////////////////////////////////////////////////////////
	/**
	 * 设置打开的excel对应的node
	 * 
	 * @param aNode
	 */
	public void setNode(Node aNode) {
		node = aNode;
	}

	/**
	 * 设置表头
	 * 
	 * @param aTableHeader
	 */
	public void setHeader(TableHeader aTableHeader) {
		tableHeader = aTableHeader;
	}

	/**
	 * 设置表头
	 * 
	 * @param aNode
	 */
	public void setHeader(Node aNode) {
		TableHeader TTableHeader = new TableHeader(aNode);
		TTableHeader.setFont(new Font("宋体", Font.PLAIN, 12));
		TTableHeader.setColor(new Color(250, 228, 184));
		setHeader(TTableHeader);
		setNode(aNode);
	}

	/**
	 * 获得表头
	 * 
	 * @return
	 */
	public static TableHeader getHeader() {
		tableHeader.setFont(new Font("宋体", Font.BOLD, 20));
		return tableHeader;
	}

	/**
	 * 初始化reportUI
	 * 
	 * @param ei
	 * @param aReportUI
	 * @return
	 * @throws Exception
	 */
	public ReportUI initReportUI(ExcelImporter ei, ReportUI aReportUI)
			throws Exception {
		aReportUI.clearAll();
		aReportUI.setWorkSheet((WorkSheet) ei.generateWorkBook().getReport(0));
		aReportUI.getReport().shrinkToFitRowHeight();
		aReportUI.getReport().getReportSettings().setPaperSize(
				new PaperSize(2000, 3000));
		return aReportUI;
	}

	/**
	 * 创建报表_根据节点
	 * 
	 * @param nodePost
	 * @param aReportUI
	 * @param aReport
	 * @throws Exception
	 */
	public void createReport(Node nodePost, ReportUI aReportUI, Report aReport)
			throws Exception {
		aReport.removeAllCellElements();
		TableHeader TTableHeader = new TableHeader(nodePost);
		TTableHeader.setFont(new Font("宋体", Font.PLAIN, 12));
		TTableHeader.setColor(new Color(250, 228, 184));
		aReport.setReportHeader(TTableHeader);
		aReportUI.setRowHeaderVisible(false);
		aReportUI.setColumnHeaderVisible(false);
		setHeader(TTableHeader);
		aReportUI.repaint();
	}

	/**
	 * 创建报表_根据表头
	 * 
	 * @param aTableHeader
	 * @param aReportUI
	 * @param aReport
	 */
	public void createReport(TableHeader aTableHeader, ReportUI aReportUI,
			Report aReport) {
		aReport.removeAllCellElements();
		aTableHeader.setFont(new Font("宋体", Font.PLAIN, 12));
		aTableHeader.setColor(new Color(250, 228, 184));
		aReport.setReportHeader(aTableHeader);
		aReportUI.setRowHeaderVisible(false);
		aReportUI.setColumnHeaderVisible(false);
		tableHeader = aTableHeader;
		aReportUI.repaint();
	}

	// //////////////////////////////////////////////////////基本属性的操作///////////////////////////////////////////////////////////////////////////////

	/**
	 * 设置源表的列信息dataSet
	 */
	public DataSet getSourceFieldDataSet(DataSet dsMain) {
		try {
			DataSet ds = itserv.getFieldInfo(dsMain.fieldByName(
					IBesQryReport.OBJECT_NAMES).getString());
			return ds;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "获取源表字段值失败");
			// e.printStackTrace();
			return null;
		}
	}

	// //////////////////////////////////////////////////////列属性的操作///////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取列信息的dataset
	 */
	public static DataSet getColDataset(DataSet dsMain) throws Exception {
		DataSet ds = DataSet.create();
		String sFilter = "";
		dsMain.beforeFirst();
		dsMain.next();
		String sSouceTalbe = dsMain.fieldByName(IBesQryReport.OBJECT_NAMES)
				.getString();
		if (Common.isNullStr(sSouceTalbe))
			return ds;
		ds = itserv.getDataSet(sSouceTalbe, sFilter);
		return ds;
	}

	// //////////////////////////////////////////////////功能函数/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获得数据类型dataset
	 */
	public DataSet getDataTypeData() throws Exception {
		DataSet ds = DataSet.create();
		String sFilter = IBesQryReport.DataType_TypeID + "='"
				+ IBesQryReport.DATATYPE + "'";
		ds = itserv.getDataSet(IBesQryReport.PUBCODETABLE, sFilter);
		return ds;
	}

	/**
	 * 获得报表类型得数据集
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportTypeData() throws Exception {
		String sFilter = IBesQryReport.DataType_TypeID
				+ "='QUERYTYPE' AND N1=1 AND " + IBesQryReport.SET_YEAR + "="
				+ Global.loginYear;
		DataSet ds = itserv.getDataSet(IBesQryReport.PUBCODETABLE, sFilter);
		return ds;
	}

	/**
	 * 获得主表reportid
	 * 
	 * @param aParentID
	 * @return
	 * @throws Exception


	/**
	 * 生成主表信息的sql执行语句
	 * 
	 * @param dsMain
	 * @return
	 * @throws Exception
	 */
	public String getBSql(DataSet dsMain) throws Exception {
		dsMain.maskDataChange(true);
		dsMain.beforeFirst();
		dsMain.next();
		String setyear = Global.loginYear;
		String typeno = dsMain.fieldByName(IBesQryReport.TYPENO).getString();
		String reportid = dsMain.fieldByName(IBesQryReport.REPORT_ID)
				.getString();
		String reportcname = dsMain.fieldByName(IBesQryReport.REPORT_CNAME)
				.getString();
		String currencyunit = dsMain.fieldByName(IBesQryReport.CURRENCYUNIT)
				.getString();
		String datauser = dsMain.fieldByName(IBesQryReport.DATA_USER)
				.getString();
		String objectnames = dsMain.fieldByName(IBesQryReport.OBJECT_NAMES)
				.getString();
		String objectename = dsMain.fieldByName(IBesQryReport.OBJECT_ENAMES)
				.getString();
		String sTitle = dsMain.fieldByName(IBesQryReport.TITLE).getString();
		// 这个地方的typeno 为"tyepno"
		String sql = "INSERT INTO " + IBesQryReport.TABLENAME_MAIN + "("
				+ IBesQryReport.SET_YEAR + "," + IBesQryReport.TYPENO + ","
				+ IBesQryReport.REPORT_ID + "," + IBesQryReport.REPORT_TYPE
				+ "," + IBesQryReport.REPORT_CNAME + ","
				+ IBesQryReport.CURRENCYUNIT + ","
				+ IBesQryReport.REPORT_SOURCE + ","
				+ IBesQryReport.IS_PASSVERIFY + "," + IBesQryReport.IS_ACTIVE
				+ "," + IBesQryReport.DATA_USER + ","
				+ IBesQryReport.OBJECT_NAMES + "," + IBesQryReport.OBJECT_ENAMES
				+ "," + IBesQryReport.IS_HASBATCH + ","
				+ IBesQryReport.IS_MULTICOND + "," + IBesQryReport.IS_END + ","
				+ IBesQryReport.LVL_ID + "," + IBesQryReport.TITLE + ") "
				+ "VALUES(" + setyear + ",'" + typeno + "','" + reportid + "',"
				+ IBesQryReport.REPORT_TYPE_VALUE + ",'" + reportcname + "','"
				+ currencyunit + "','" + IBesQryReport.REPORT_SOURCE_VALUE
				+ "','" + IBesQryReport.IS_PASSVERIFY_VALUE + "','"
				+ IBesQryReport.IS_ACTIVE_VALUE + "','" + datauser + "','"
				+ objectnames + "','" + objectename + "','"
				+ IBesQryReport.IS_HASBATCH_VALUE + "','"
				+ IBesQryReport.IS_MULTICOND_VALUE + "','"
				+ IBesQryReport.IS_END_VALUE + "','" + reportid + "','"
				+ sTitle + "'" + ")";
		dsMain.maskDataChange(false);
		return sql;
	}

	/**
	 * 生成sqllines表的SQL执行语句
	 * 
	 * @param dsSql
	 * @return
	 * @throws Exception
	 */
	public Map getSSql(DataSet dsSql) throws Exception {
		String setyear = Global.loginYear;
		String typeno = dsSql.fieldByName(IBesQryReport.TYPENO).getString();
		String reportid = dsSql.fieldByName(IBesQryReport.REPORT_ID)
				.getString();
		String sqltype = dsSql.fieldByName(IBesQryReport.SQLTYPE).getString();
		String viewlevel = dsSql.fieldByName(IBesQryReport.VIEWLEVEL)
				.getString();
		String viewname = dsSql.fieldByName(IBesQryReport.VIEWNAME).getString();
		// String sqllines = null;
		// if (!"LASTQUERY".equals(sqltype))
		// sqllines = getSql(viewname);
		// else
		// sqllines = "";
		String sql = "INSERT INTO " + IBesQryReport.TABLE_SQLLINES + "("
				+ IBesQryReport.SET_YEAR + "," + "TYPE_NO" + ","
				+ IBesQryReport.REPORT_ID + "," + IBesQryReport.SQLTYPE + ","
				+ IBesQryReport.VIEWLEVEL + "," + IBesQryReport.VIEWNAME + ","
				+ IBesQryReport.SQLLINES + ") " + " VALUES( " + setyear + ","
				+ typeno + ",'" + reportid + "','" + sqltype + "'," + viewlevel
				+ ",'" + viewname + "',?)";
		Map mapResult = new HashMap();
		mapResult.put(IBesQryReport.SQLTYPE, sqltype);
		mapResult.put(IBesQryReport.VIEWNAME, viewname);
		mapResult.put(IBesQryReport.SQLLINES, sql);
		return mapResult;
	}

	/**
	 * 生成列信息的SQL执行语句
	 */
	public String getCSql(DataSet dsCol) throws Exception {
		String setyear = Global.loginYear;
		String typeno = dsCol.fieldByName(IBesQryReport.TYPENO).getString();
		String reportid = dsCol.fieldByName(IBesQryReport.REPORT_ID)
				.getString();
		String fieldid = dsCol.fieldByName(IBesQryReport.FIELD_ID).getString();
		String fieldcode = dsCol.fieldByName(IBesQryReport.FIELD_CODE)
				.getString();
		String fieldcname = dsCol.fieldByName(IBesQryReport.FIELD_CNAME)
				.getString();
		String fieldename = dsCol.fieldByName(IBesQryReport.FIELD_ENAME)
				.getString();
		String fieldfname = dsCol.fieldByName(IBesQryReport.FIELD_FNAME)
				.getString();
		String isleaf = dsCol.fieldByName(IBesQryReport.IS_LEAF).getString();
		String fieldlevel = String.valueOf(codeRule_Col.levelOf(fieldcode) + 1);
		String fieldtype = dsCol.fieldByName(IBesQryReport.FIELD_TYPE)
				.getString();
		String fielddispwidth = dsCol
				.fieldByName(IBesQryReport.FIELD_DISPWIDTH).getString();
		String fieldishidecol = dsCol.fieldByName(IBesQryReport.IS_HIDECOL)
				.getString();
		String fielddisformat = dsCol
				.fieldByName(IBesQryReport.FIELD_DISFORMAT).getString();

		String sql = "INSERT INTO " + IBesQryReport.TABLENAME_COL + "("
				+ IBesQryReport.SET_YEAR + "," + "TYPE_NO" + ","
				+ IBesQryReport.REPORT_ID + "," + IBesQryReport.FIELD_ID + ","
				+ IBesQryReport.FIELD_CODE + "," + IBesQryReport.FIELD_CNAME
				+ "," + IBesQryReport.FIELD_ENAME + ","
				+ IBesQryReport.FIELD_FNAME + "," + IBesQryReport.IS_LEAF + ","
				+ IBesQryReport.FIELD_LEVEL + "," + IBesQryReport.FIELD_TYPE
				+ "," + IBesQryReport.FIELD_DISPWIDTH + ","
				+ IBesQryReport.IS_HIDECOL + ","
				+ IBesQryReport.FIELD_DISFORMAT + ") VALUES(" + setyear + ",'"
				+ typeno + "','" + reportid + "','" + fieldid + "','"
				+ fieldcode + "','" + fieldcname + "','" + fieldename + "','"
				+ fieldfname + "','" + isleaf + "'," + fieldlevel + ",'"
				+ fieldtype + "'," + fielddispwidth + ",'" + fieldishidecol
				+ "','" + fielddisformat + "')";
		return sql;
	}

	/**
	 * 获取表的类别
	 * 
	 * @return TABLE VIEW
	 * @throws Exception
	 */
	public String getTalbeType(String aTableName) throws Exception {
		String sType = "";
		String sFilter = IBesQryReport.TNAME + "='" + aTableName + "'";
		DataSet dsType = itserv.getDataSet("TAB", sFilter);
		if (!dsType.isEmpty()) {
			dsType.beforeFirst();
			dsType.next();
			sType = dsType.fieldByName(IBesQryReport.TNAME).getString();
		}
		return sType;
	}

	/**
	 * 把列信息node 中数据添入dataset中
	 */
	public InfoPackage FillIntoDataSet(DataSet dsCol, Node aNode,
			ReportUI reportUI, SysCodeRule codeRule, String ReportID)
			throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		Node[][] nodeArray = aNode.toArray();
		String nodeID = null; // node的唯一表示符
		int fieldID = 0;
		String sfieldID = "";
		// 循环添数据到dataSet
		for (int i = 1; i < nodeArray.length; i++) {
			for (int j = 0; j < nodeArray[i].length; j++) {
				Node nodeGet = nodeArray[i][j];
				nodeID = nodeGet.getIdentifier().toString();
				CellElement cell = (CellElement) nodeGet.getValue();
				Double columnWidth = new Double(reportUI.getReportContent()
						.getColumnWidth(cell.getColumn()));
				dsCol.append();
				dsCol.fieldByName(IBesQryReport.REPORT_ID).setValue(ReportID);
				dsCol.fieldByName(IBesQryReport.FIELD_DISPWIDTH).setValue(
						columnWidth);
				dsCol.fieldByName("nodeID").setValue(nodeID);
				// field_code
				String sFieldID ="";
				dsCol.fieldByName(IBesQryReport.FIELD_CODE).setValue(sFieldID);
				// field_id
				if (i == 1 && j == 0)
					sfieldID = Common.getStrID(new BigDecimal(fieldID)
							.add(new BigDecimal(1)), 4);
				else
					sfieldID = Common.getStrID(new BigDecimal(sfieldID)
							.add(new BigDecimal(1)), 4);

				dsCol.fieldByName("PARID").setValue(
						nodeGet.getParent().getIdentifier());
				dsCol.fieldByName(IBesQryReport.FIELD_ID).setValue(sfieldID);
				// field_cname
				dsCol.fieldByName(IBesQryReport.FIELD_CNAME).setValue(
						nodeGet.getText());
				// 生成Field_EName
				String sEName = "";
				dsCol.fieldByName(IBesQryReport.FIELD_ENAME).setValue(sEName);
				// 生成 FName
				String sParentFName = "";
				String sFName = nodeGet.getText();
				if (!Common.isNullStr(sParentFName))
					sFName = sParentFName + "." + sFName;
				dsCol.fieldByName(IBesQryReport.FIELD_FNAME).setValue(sFName);
				String sLeaf = null;
				if (nodeGet.getChildrenCount() > 0)
					sLeaf = "0";
				else
					sLeaf = "1";

				dsCol.fieldByName(IBesQryReport.IS_LEAF).setValue(sLeaf);
				String sLevel = String.valueOf(nodeGet.getLevel());
				dsCol.fieldByName(IBesQryReport.FIELD_LEVEL).setValue(sLevel);
				dsCol.fieldByName(IBesQryReport.FIELD_TYPE).setValue("");
				dsCol.fieldByName("FIELD_WIDTH").setValue("");
				dsCol.fieldByName(IBesQryReport.IS_HIDECOL).setValue("");
				dsCol.fieldByName(IBesQryReport.SET_YEAR).setValue(
						Global.loginYear);
				// 业务编号,从接口获取
				dsCol.fieldByName(IBesQryReport.TYPENO).setValue(
						String.valueOf(getTypeNO()));
				// String.valueOf(getTypeNO()) );
			}
		}
		CellSelection cells = new CellSelection(0, 0);
		reportUI.setCellSelection(cells);
		dsCol.beforeFirst();
		dsCol.next();
		dsCol.maskDataChange(false);
		return info;
	}

	/**
	 * 初始化基本属性的dataset
	 * 
	 * @return 两个 dataset此时的bookmark
	 */
	public String[] initAttrData(DataSet dsSouce, DataSet dsSql)
			throws Exception {
		String sTypeNO = String.valueOf(getTypeNO());
		// 主表信息
		initSourceData(dsSouce, sTypeNO);
		// sqllines表
		initSqlData(dsSql, sTypeNO);
		// 返回值
		String[] bmk = new String[2];
		bmk[0] = dsSouce.toogleBookmark();
		bmk[1] = dsSql.toogleBookmark();
		return bmk;
	}

	/**
	 * 初始化dsSource得数据
	 * 
	 * @param dsSouce
	 * @param aTypeNO
	 * @throws Exception
	 */
	private void initSourceData(DataSet dsSouce, String aTypeNO)
			throws Exception {
		if (dsSouce.getRecordCount() > 0)
			return;
		dsSouce.append();
		dsSouce.fieldByName(IBesQryReport.TYPENO).setValue(aTypeNO);
		dsSouce.fieldByName(IBesQryReport.REPORT_TYPE).setValue(
				IBesQryReport.REPORT_TYPE_VALUE);
		dsSouce.fieldByName(IBesQryReport.REPORT_CNAME).setValue("");
		dsSouce.fieldByName(IBesQryReport.CURRENCYUNIT).setValue("");
		dsSouce.fieldByName(IBesQryReport.REPORT_SOURCE).setValue(
				IBesQryReport.REPORT_SOURCE_VALUE);
		dsSouce.fieldByName(IBesQryReport.IS_PASSVERIFY).setValue(
				IBesQryReport.IS_PASSVERIFY_VALUE);
		dsSouce.fieldByName(IBesQryReport.IS_ACTIVE).setValue(
				IBesQryReport.IS_ACTIVE_VALUE);
		dsSouce.fieldByName(IBesQryReport.DATA_USER).setValue("");
		dsSouce.fieldByName(IBesQryReport.OBJECT_NAMES).setValue("");
		dsSouce.fieldByName(IBesQryReport.OBJECT_ENAMES).setValue("");
		dsSouce.fieldByName(IBesQryReport.IS_HASBATCH).setValue(
				IBesQryReport.IS_HASBATCH_VALUE);
		dsSouce.fieldByName(IBesQryReport.IS_MULTICOND).setValue(
				IBesQryReport.IS_MULTICOND_VALUE);
		dsSouce.fieldByName(IBesQryReport.IS_END).setValue(""); // 根据编码规则,编码判断
		dsSouce.fieldByName(IBesQryReport.SET_YEAR).setValue(Global.loginYear);
	}

	/**
	 * 初始化dsSql数据集
	 * 
	 * @param dsSql
	 * @param aTypeNO
	 * @throws Exception
	 */
	private void initSqlData(DataSet dsSql, String aTypeNO) throws Exception {
		if (dsSql.getRecordCount() > 0)
			return;
		dsSql.append();
		dsSql.fieldByName(IBesQryReport.SET_YEAR).setValue(Global.loginYear);
		dsSql.fieldByName(IBesQryReport.SQLTYPE).setValue("LASTQUERY");
		dsSql.fieldByName(IBesQryReport.VIEWLEVEL).setValue("1");
		dsSql.fieldByName(IBesQryReport.VIEWNAME).setValue("");
		dsSql.fieldByName(IBesQryReport.SQLLINES).setValue("");
		dsSql.fieldByName(IBesQryReport.TYPENO).setValue(aTypeNO);
	}

	/**
	 * 由本级节点信息修改父节点信息
	 * 
	 * @param dsPost
	 * @param aFieldID
	 * @throws Exception
	 */
	public void changeParentID(DataSet dsPost, String aFieldCode)
			throws Exception {
		dsPost.maskDataChange(true);
		String bmk = dsPost.toogleBookmark();
		String sParentID = codeRule_Main.previous(aFieldCode);
		if (dsPost.locate(IBesQryReport.FIELD_CODE, sParentID)) {
			dsPost.edit();
			dsPost.fieldByName(IBesQryReport.IS_END).setValue("0");
		}
		dsPost.gotoBookmark(bmk);
		dsPost.maskDataChange(false);
	}

	/**
	 * 检查数组是否为空
	 * 
	 * @param arrays
	 * @return 如果为空则返回false, 否则返回true
	 */
	public static boolean checkArrayIsNull(String[] arrays) {
		int iNum = arrays.length;
		int iNullNum = 0;
		for (int i = 0; i < iNum; i++) {
			if (Common.isNullStr(arrays[i]))
				iNullNum++;
		}
		if (iNullNum == iNum)
			return false;
		else
			return true;
	}

	/**
	 * 将数组中所有空值去掉
	 * 
	 * @param array
	 * @return
	 */
	public static String[] getNotNullArray(String[] array) {
		int iNotNullNum = 0;
		for (int i = 0; i < array.length; i++) {
			if (!Common.isNullStr(array[i]))
				iNotNullNum++;
		}
		String[] newArray = new String[iNotNullNum];
		for (int i = 0; i < array.length; i++) {
			if (!Common.isNullStr(array[i]))
				newArray[i] = array[i];
		}
		return newArray;
	}
}
