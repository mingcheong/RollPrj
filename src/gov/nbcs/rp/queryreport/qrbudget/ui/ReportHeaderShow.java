package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

//import gov.nbcs.rp.basinfo.common.filtertable.FilterTableHeaderRender;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.qrbudget.action.SaveColWidthAction;
import gov.nbcs.rp.queryreport.qrbudget.action.SetWhereReadWrite;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.ColDispInf;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget.ToolBarPanel;

import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import gov.nbcs.rp.sys.sysiaestru.ui.SetActionStatus;
import com.foundercy.pf.control.table.ColumnGroup;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.report.cellElement.CellGUIAttr;

/**
 * 查询表表头显示
 * 
 * @author qzc
 * 
 */
public class ReportHeaderShow {
	private QrBudget qrBudget;

	public static String PAPER_SIZE = "PAPER_SIZE"; // 纸张大小

	public static String PRINT_ORIENT = "PRINT_ORIENT"; // 纸张方向

	public static String MARGIN_LEFT = "MARGIN_LEFT"; // 左边距

	public static String MARGIN_RIGHT = "MARGIN_RIGHT"; // 右边距

	public static String MARGIN_TOP = "MARGIN_TOP"; // 上边距

	public static String MARGIN_BOTTOM = "MARGIN_BOTTOM"; // 下边距

	public ReportHeaderShow(QrBudget qrBudget) {
		this.qrBudget = qrBudget;

	}

	public void show(ReportInfo reportInfo) {
		try {
			Global.mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			if (reportInfo == null)
				return;

			// 设置ToolBar组件状态
			setTooBarState(reportInfo);

			// 显示报表信息
			showHeader(reportInfo);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(qrBudget, "部门预算查询表显示报表表头发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		} finally {
			Global.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}

	/**
	 * 显示表头
	 * 
	 * @param dsRepset
	 * @throws Exception
	 */
	public void showHeader(ReportInfo reportInfo) throws Exception {

		if (reportInfo.getBussType() == QrBudget.TYPE_COVER) {// 封面
			// 设置固定行
			qrBudget.getReportUI().setFrozenRow(0);
			// 显示封面
			showCover(reportInfo);
			// 保存原报表列宽
			SaveColWidthAction.saveOldColWidth((Report) qrBudget.getReportUI()
					.getReport(), reportInfo);
		} else if (reportInfo.getBussType() == QrBudget.TYPE_ROWSET) {// 行报表
			Report curReport = (Report) qrBudget.getReportUI().getReport();
			qrBudget
					.setCurReport((gov.nbcs.rp.common.ui.report.Report) ReportFactory
							.getRowSetHeaderByExists(curReport, reportInfo));
			qrBudget.getReportUI().setReport(curReport);

		} else if (reportInfo.getBussType() == QrBudget.TYPE_SZZB) {// 收支总表达式
			Report curReport = (Report) qrBudget.getReportUI().getReport();
	
		
			qrBudget.getReportUI().setReport(curReport);
		SaveColWidthAction.saveOldColWidth((Report) qrBudget.getReportUI()
					.getReport(), reportInfo);
		} else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP
				|| reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {// 分组报表,特殊报表

			showNormalHeader(reportInfo.getReportID());

		}

		// 固定行
		int iFreezeCol = Integer.parseInt(qrBudget.getFpnlToolBar()
				.getJspFrozenColumn().getValue().toString());
		qrBudget.getReportUI().freezeColumn(iFreezeCol);

		qrBudget.getReportUI().updateUI();
		qrBudget.getReportUI().repaint();

		// if (reportInfo.getBussType() == QrBudget.TYPE_COVER
		// || reportInfo.getBussType() == QrBudget.TYPE_ROWSET
		// || reportInfo.getBussType() == QrBudget.TYPE_SZZB) {
		// qrBudget.getReportUI().freezeColumn(iFreezeCol);
		//
		// qrBudget.getReportUI().updateUI();
		// qrBudget.getReportUI().repaint();
		// } else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP
		// || reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {
		// qrBudget.getFtabReport().lockColumns(iFreezeCol);
		// }

	}

	/**
	 * 显示封面
	 */
	private void showCover(ReportInfo reportInfo) throws Exception {
		Report curReport = (Report) qrBudget.getReportUI().getReport();


	}

	public static void setCover(Report report, DataSet dsSzzb) throws Exception {
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;
		Object oValue;
		Cell cellElement;
		CellGUIAttr cellGUIAttr = new CellGUIAttr();
		cellGUIAttr.setEditableState(Constants.EDITABLE_STATE_FALSE);

		dsSzzb.beforeFirst();
	
	}

	/**
	 * 根据表头DdataSet得到表体查询字段语句
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getFieldSelect(DataSet dsReportHeader) throws Exception {
		String sFieldSelect = "";
		dsReportHeader.beforeFirst();
		while (dsReportHeader.next()) {
			if (dsReportHeader.fieldByName(IQrBudget.IS_LEAF).getInteger() == 0)
				continue;
			if ("".equals(sFieldSelect))
				sFieldSelect = dsReportHeader
						.fieldByName(IQrBudget.FIELD_ENAME).getString();
			else
				sFieldSelect = sFieldSelect
						+ ","
						+ dsReportHeader.fieldByName(IQrBudget.FIELD_ENAME)
								.getString();
		}
		return sFieldSelect;
	}

	/**
	 * 显示报表表头(分组表和特殊报表）
	 * 
	 */
	public static void showNormalHeader(DataSet dsReportHeader,
			QrBudget qrBudget) throws Exception {

		// 子节点,显示表头
		qrBudget.setDsReportHeader(dsReportHeader);

		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		qrBudget.setNodeHeader(hg.generate(qrBudget.getDsReportHeader(),
				IQrBudget.FIELD_CODE, IQrBudget.FIELD_CNAME,
				BesAct.codeRule_Col, IQrBudget.FIELD_CODE));

		// 报表表头的相关设置
		TableHeader tableHeader = new TableHeader(qrBudget.getNodeHeader());
		tableHeader.setColor(UntPub.HEADER_COLOR);
		tableHeader.setFont(qrBudget.getFont());
		Report curReport = (Report) qrBudget.getReportUI().getReport();
		int iEndRepeatRow = curReport.getBodyRowCount()
				+ curReport.getReportHeader().getRows();
		curReport.removeAllCellElements();
		for (int i = 0; i < iEndRepeatRow; i++)
			curReport.setRowHeight(i, 20);
		curReport.setReportHeader(tableHeader);

		// 设置报表列宽，根据库中保存的列宽的值,保存显示类型和显示格式
		List lstFields = tableHeader.getFields();

		for (int i = 0; i < lstFields.size(); i++) {
			if (!qrBudget.getDsReportHeader().locate(IQrBudget.FIELD_CODE,
					lstFields.get(i).toString())) {
				JOptionPane.showMessageDialog(qrBudget, "部门预算查询表显示报表表头列宽发生错误!",
						"提示", JOptionPane.ERROR_MESSAGE);
				break;
			}

			if (qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_DISPWIDTH).getValue() != null) {
				curReport.setColumnWidth(i, qrBudget.getDsReportHeader()
						.fieldByName(IQrBudget.FIELD_DISPWIDTH).getDouble());
			}
		}

		curReport.shrinkToFitRowHeight();
		int iSZZBTitleRows = curReport.getReportHeader().getRows();
		qrBudget.getReportUI().setFrozenRow(iSZZBTitleRows);

	}

	/**
	 * 显示报表表头(分组表和特殊报表）
	 * 
	 */
	private void showNormalHeader(String sReportId) throws Exception {
		List lstCols = new ArrayList();// 记录列信息

		DataSet dsReportHeader = qrBudget.getQrBudgetServ().getReportHeader(
				sReportId, Global.loginYear);

		// 表头内容，更改资金来源和对比分析时，读本地参数信息
		SetWhereReadWrite setWhereReadWrite = new SetWhereReadWrite(sReportId);
		if (setWhereReadWrite.isReportExists()) {
			if (setWhereReadWrite.getPfsFname() != null)
				// 改变表头资金来源显示
				dsReportHeader = ReportHeaderOpe.changeDataSource(
						dsReportHeader, setWhereReadWrite.getPfsFname());
		}

		// 子节点,显示表头
		qrBudget.setDsReportHeader(dsReportHeader);

		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		qrBudget.setNodeHeader(hg.generate(qrBudget.getDsReportHeader(),
				IQrBudget.FIELD_CODE, IQrBudget.FIELD_CNAME,
				BesAct.codeRule_Col, IQrBudget.FIELD_CODE));

		// 报表表头的相关设置
		TableHeader tableHeader = new TableHeader(qrBudget.getNodeHeader());
		tableHeader.setColor(UntPub.HEADER_COLOR);
		tableHeader.setFont(qrBudget.getFont());
		Report curReport = (Report) qrBudget.getReportUI().getReport();
		int iEndRepeatRow = curReport.getBodyRowCount()
				+ curReport.getReportHeader().getRows();
		curReport.removeAllCellElements();
		for (int i = 0; i < iEndRepeatRow; i++)
			curReport.setRowHeight(i, 20);
		curReport.setReportHeader(tableHeader);

		// 设置报表列宽，根据库中保存的列宽的值,保存显示类型和显示格式
		List lstFields = tableHeader.getFields();

		// add by xxl 有这样的问题：当些报表有隐藏列时，报表在调整适合高度会出现过大的行高
		// 这是由于隐藏列造成的，所以这里要添加处理，先将隐藏列设置成一个较大的宽度，并记录这此隐藏列
		// 待调整好高度后，再恢复其0的宽度 20090823

		int iFieldDispwidth[] = new int[lstFields.size()]; // 显示列宽
		Map mapZeroCol = new HashMap();

		for (int i = 0; i < lstFields.size(); i++) {
			if (!qrBudget.getDsReportHeader().locate(IQrBudget.FIELD_CODE,
					lstFields.get(i).toString())) {
				JOptionPane.showMessageDialog(qrBudget, "部门预算查询表显示报表表头列宽发生错误!",
						"提示", JOptionPane.ERROR_MESSAGE);
				break;
			}
			// xxl搬了个家------
			// if (qrBudget.getDsReportHeader().fieldByName(
			// IQrBudget.FIELD_DISPWIDTH).getValue() != null) {
			// curReport.setColumnWidth(i, qrBudget.getDsReportHeader()
			// .fieldByName(IQrBudget.FIELD_DISPWIDTH).getDouble());
			// }
			iFieldDispwidth[i] = qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_DISPWIDTH).getInteger();
			// xxl---------------
			int colWidth = iFieldDispwidth[i];
			if (colWidth <= 3) {// XXL 为什么这里是3
				// 呢，在苏州有人将列宽设置成了1，2等，目的也是想隐藏，这里放宽了条件
				colWidth = 600;
				mapZeroCol.put(new Integer(i), null);
			}// -----------------
			curReport.setColumnWidth(i, colWidth);

			ColDispInf colDispInf = new ColDispInf();
			colDispInf.sFieldType = qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_TYPE).getString();
			colDispInf.sFieldDisformat = qrBudget.getDsReportHeader()
					.fieldByName(IQrBudget.FIELD_DISFORMAT).getString();
			if ("整型".equals(colDispInf.sFieldType)
					|| "浮点型".equals(colDispInf.sFieldType)
					|| "货币".equals(colDispInf.sFieldType)) {
				colDispInf.style = colDispInf.style
						.deriveFormat(new DecimalFormat(
								colDispInf.sFieldDisformat));
				colDispInf.style = colDispInf.style
						.deriveHorizontalAlignment(Constants.RIGHT);
				colDispInf.style = colDispInf.style
						.deriveTextStyle(Style.TextStyle_WrapText);
			} else if ("日期型".equals(colDispInf.sFieldType)) {
				colDispInf.style = colDispInf.style
						.deriveFormat(new SimpleDateFormat(
								colDispInf.sFieldDisformat));
				colDispInf.style = colDispInf.style
						.deriveTextStyle(Style.TextStyle_SingleLine);
			} else {
				colDispInf.style = colDispInf.style
						.deriveTextStyle(Style.TextStyle_SingleLine);
			}
			colDispInf.fieldEName = qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_ENAME).getString();

			colDispInf.style = colDispInf.style.deriveBorder(1, Color.BLACK, 1,
					Color.BLACK, 1, Color.BLACK, 1, Color.BLACK);

			lstCols.add(colDispInf);
		}

		String sFieldSelect = getFieldSelect(qrBudget.getDsReportHeader());
		qrBudget.setSFieldSelect(sFieldSelect);
		qrBudget.setLstColInfo(lstCols);
		if ("".equals(sFieldSelect)) {
			JOptionPane.showMessageDialog(qrBudget, "未获得表列信息！", "提示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		curReport.shrinkToFitRowHeight();
		// 再恢复零宽度行
		if (!mapZeroCol.isEmpty()) {
			java.util.Iterator it = mapZeroCol.keySet().iterator();
			while (it.hasNext()) {
				curReport.setColumnWidth(((Integer) it.next()).intValue(), 0);
			}
		}
		int iSZZBTitleRows = curReport.getReportHeader().getRows();
		qrBudget.getReportUI().setFrozenRow(iSZZBTitleRows);

	}

	/**
	 * 设置ToolBar按钮状态
	 * 
	 * 
	 */
	private void setTooBarState(ReportInfo reportInfo) throws Exception {

		// 设置查询版本是否显示, 财政版显示版本信息,单位版不显示版本信息
		// chcx add 2007。09。10,modify:zlx
		ToolBarPanel fpnlToolBar = qrBudget.getFpnlToolBar();
		if (qrBudget.getIUserType() == 1) {
			DataSet dsDataVer = QrBudgetI.getMethod().getVerInfoWithReport(
					reportInfo.getReportID());
			fpnlToolBar.lstVer.clear();
			fpnlToolBar.cbxDataVer.removeAllItems();
			fpnlToolBar.cbxDataVer.addItem("实时");
			fpnlToolBar.lstVer.add(new Integer(0));
			if (dsDataVer != null && !dsDataVer.isEmpty())
				dsDataVer.beforeFirst();
			while (dsDataVer.next()) {
				fpnlToolBar.lstVer.add(new Integer(dsDataVer.fieldByName(
						IQrBudget.VER_NO).getInteger()));
				fpnlToolBar.cbxDataVer.addItem(dsDataVer.fieldByName(
						IQrBudget.FULLNAME).getString());
			}
			fpnlToolBar.cbxDataVer.setSelectedIndex(0);
		}

		// 设置数据类型是否显示
		if (reportInfo.isHasBatch()) {
			fpnlToolBar.flblEmpty1.setVisible(true);
//			fpnlToolBar.flblDataType.setVisible(true);
//			fpnlToolBar.cbxDataType.setVisible(true);
		} else {
			fpnlToolBar.flblEmpty1.setVisible(false);
//			fpnlToolBar.flblDataType.setVisible(false);
//			fpnlToolBar.cbxDataType.setVisible(false);
		}

		// 设置每列显示多少行
		fpnlToolBar.flblRown.setVisible(false);
		fpnlToolBar.jspRow.setVisible(false);
		fpnlToolBar.flblRown1.setVisible(false);
		fpnlToolBar.flblEmpty2.setVisible(false);

		int iReprot_type = qrBudget.getFtreReportName().getDataSet()
				.fieldByName(IQrBudget.TYPE_FLAG).getInteger();
		if (iReprot_type == 2) {
			fpnlToolBar.flblEmpty2.setVisible(true);
			fpnlToolBar.flblRown.setVisible(true);
			fpnlToolBar.jspRow.setVisible(true);
			fpnlToolBar.flblRown1.setVisible(true);
		}
		// 变换数据源按钮是否可用
		SetActionStatus setActionStatus = new SetActionStatus(qrBudget);
		if (QrBudget.TYPE_GROUP == reportInfo.getBussType()) {
			setActionStatus.setOneBtnState("条件选择", true);
		} else {
			setActionStatus.setOneBtnState("条件选择", false);
		}
	}

	/**
	 * 根据列表创建一个TABLE
	 * 
	 * @param lstCols
	 * @return
	 */
	private FTable createTable(DataSet dsHeader, String lvlField,
			String fieldName, String fieldTitle, String fieldLeaf,
			String fieldColwidth, String fieldFormat, SysCodeRule codeRole,
			FTable aTable) throws Exception {
		if (dsHeader == null || dsHeader.isEmpty())
			return null;
		if (aTable == null)
			aTable = new FTable();
		List lstBasCols = new ArrayList();

		XMLData xmlCols = new XMLData();
		XMLData xmlFieldToLvl = new XMLData();// 字段名对就LVLCODE，只记录字段

		List lstLastCol = new ArrayList();// 最终列
		// 组合父子关系
		dsHeader.beforeFirst();
		while (dsHeader.next()) {
			String sLeaf = dsHeader.fieldByName(fieldLeaf).getString();
			String sTitle = dsHeader.fieldByName(fieldTitle).getString();
			String sLvl = dsHeader.fieldByName(lvlField).getString();
			String sFieldName = dsHeader.fieldByName(fieldName).getString();
			if (sFieldName != null)
				sFieldName = sFieldName.toLowerCase();
			String sWidth = dsHeader.fieldByName(fieldColwidth).getString();
			String sFormat = dsHeader.fieldByName(fieldFormat).getString();
			boolean isTopLevel = (codeRole.previous(sLvl) == null);
			int width = 0;
			if (sWidth == null || "".equals(sWidth) || "0".equals(sWidth)) {
				width = 120;
			} else
				width = Integer.parseInt(sWidth);

			if (Common.estimate(sLeaf)) {
				// 创建表列
				FBaseTableColumn aCol = createTableCol(sFieldName, sTitle,
						sFormat, width);
//				aCol.setHeaderRenderer(new FilterTableHeaderRender(null, null,
//						null));
				if (isTopLevel)
					lstLastCol.add(aCol);
				else
					lstBasCols.add(aCol);

				xmlCols.put(sLvl, aCol);
				xmlFieldToLvl.put(sFieldName, sLvl);

			} else {
				// 创建分组
				ColumnGroup aGroup = new ColumnGroup();
				aGroup.setId(sLvl);
				aGroup.setTitle(sTitle);
//				aGroup.setHeaderRenderer(new FilterTableHeaderRender(null,
//						null, null));
				if (isTopLevel)
					lstLastCol.add(aGroup);
				else
					lstBasCols.add(aGroup);
				xmlCols.put(sLvl, aGroup);
			}
		}

		// 2.加列
		int iCount = lstBasCols.size();
		for (int i = 0; i < iCount; i++) {
			Object obj = lstBasCols.get(i);
			if (obj instanceof FBaseTableColumn) {
				FBaseTableColumn aCol = (FBaseTableColumn) obj;
				String sParentID = codeRole.previous((String) xmlFieldToLvl
						.get(aCol.getId()));
				ColumnGroup parGroup = (ColumnGroup) xmlCols.get(sParentID);
				if (parGroup == null)
					throw new Exception("报表的级次关系没有设置正确![" + aCol.getId() + "]");
				parGroup.addControl(aCol);
			} else {
				ColumnGroup aGroup = (ColumnGroup) obj;
				String sParentID = codeRole.previous(aGroup.getId());
				ColumnGroup parGroup = (ColumnGroup) xmlCols.get(sParentID);
				if (parGroup == null)
					throw new Exception("报表的级次关系没有设置正确![" + aGroup.getId()
							+ "]");
				parGroup.addControl(aGroup);
			}
		}
		iCount = lstLastCol.size();
		for (int i = 0; i < iCount; i++) {
			Object obj = lstLastCol.get(i);
			if (obj instanceof ColumnGroup)
				aTable.addColumnGroup((ColumnGroup) obj);
			else
				aTable.addColumn((FBaseTableColumn) obj);
		}

		return aTable;
	}

	private FBaseTableColumn createTableCol(String fieldName, String title,
			String sFormat, int width) {
		FBaseTableColumn aCol = new FBaseTableColumn();
		aCol.setId(fieldName);
		aCol.setTitle(title);
		aCol.setWidth(width);
		if (sFormat != null && !sFormat.equals("")) {
			// 增加格式化信息
			aCol.setFormat(sFormat);
		}
		return aCol;
	}
}
