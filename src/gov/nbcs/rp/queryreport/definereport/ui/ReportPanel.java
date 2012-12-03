/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import gov.nbcs.rp.queryreport.definereport.action.SetHeadFundAction;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.common.constants.ReportTypeConstants;
import com.foundercy.pf.reportcy.common.gui.actions.report.DisplayCellElementAction;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.common.ifun.io.IStyleCacheAble;
import com.foundercy.pf.reportcy.common.ifun.ui.ICreateReportPaneRightMenu;
import com.foundercy.pf.reportcy.common.util.Log;
import com.foundercy.pf.reportcy.common.util.ReportTools;
import com.foundercy.pf.reportcy.design.actions.edit.CopyAction;
import com.foundercy.pf.reportcy.design.actions.edit.CutAction;
import com.foundercy.pf.reportcy.design.actions.edit.PasteAction;
import com.foundercy.pf.reportcy.design.actions.edit.clear.ClearAllAction;
import com.foundercy.pf.reportcy.design.actions.other.DeleteColumnAction;
import com.foundercy.pf.reportcy.report.ui.ReportToolBarLineComponentImpl;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.IReportQuerySource;
import com.foundercy.pf.reportcy.summary.iface.ui.IGetColumnTypeAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryReportUiAttr;
import com.foundercy.pf.reportcy.summary.object.cellvalue.CalculateValueImpl;
import com.foundercy.pf.reportcy.summary.object.cellvalue.GroupValueImpl;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.cell.DefaultGridColumnRenderer;
import com.fr.cell.ReportPane;
import com.fr.report.GroupReport;
import com.fr.report.GroupReportAttr;

/**
 * <p>
 * Title:报表面板
 * </p>
 * <p>
 * Description:
 * </p>

 */
public class ReportPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	// 定义报表Pane
	SummaryReportPane designGroupReportPane = null;

	// 定义报表
	GroupReport groupReport = null;

	// 自定义查询表定义客服端界面类
	ReportGuideUI reportGuideUI = null;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	public ReportPanel(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.definReportServ = reportGuideUI.definReportServ;
		init();

	}

	private void init() {
		// 定义报表表格
		designGroupReportPane = getDesignGroupReportPane();
		designGroupReportPane.setColumnEndless(false);
		designGroupReportPane.setRowEndless(false);
		// 给格子的value为Formula的类型注册编辑器
		designGroupReportPane.getGrid().setDefaultCellEditor(
				GroupValueImpl.class, new GroupCellEditor());

		// 给格子的value为Formula的类型注册编辑器
		designGroupReportPane.getGrid().setDefaultCellEditor(
				CalculateValueImpl.class, new CaluCellEditor());

		designGroupReportPane.getGrid().setDefaultCellEditor(
				FundSourceImpl.class, new FundSourceCellEditor());

		FButton fbtnInput = new FButton("", "导入表头");
		fbtnInput.setIcon("images/fbudget/excel.gif");
		JComponent reportToolBar = createReportToolBarPanel(designGroupReportPane);
		reportToolBar.add(fbtnInput);
		fbtnInput.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				new ImportTitleDlg(reportGuideUI, designGroupReportPane).show();

			}
		});

		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setRowHeight(33);
		this.setLayout(rLay);
		this.add(reportToolBar, new TableConstraints(1, 1, false, true));
		this.add(designGroupReportPane, new TableConstraints(1, 1, true, true));
		new ReportPaneDropTarget(reportGuideUI, designGroupReportPane);

	}

	public SummaryReportPane getDesignGroupReportPane() {

		if (designGroupReportPane == null) {
			designGroupReportPane = new SummaryReportPane() {
				private static final long serialVersionUID = 1L;

				public JPopupMenu createColumnPopupMenu(MouseEvent evt,
						int columnIndex) {
					JPopupMenu columnPopupMenu = super.createColumnPopupMenu(
							evt, columnIndex);
					Component[] component = columnPopupMenu.getComponents();
					int count = component.length;
					for (int i = 0; i < count; i++) {
						if (!(component[i] instanceof JMenuItem)) {
							continue;
						}
						JMenuItem menuItem = (JMenuItem) component[i];
						if (menuItem.getAction() instanceof DeleteColumnAction) {
							columnPopupMenu.remove(i);

							CustomDeleteColumnAction deleteColumnAction = new CustomDeleteColumnAction(
									reportGuideUI, this);
							deleteColumnAction.setIndex(columnIndex);
							deleteColumnAction.setLocation(new Point(
									evt.getX() + 1, evt.getY() + 1));
							columnPopupMenu.add(deleteColumnAction
									.getMenuItem(), i);
							break;
						}

					}
					return columnPopupMenu;
				}

				// 表头区调用右击的方法
				public JPopupMenu createHeaderPopupMenu(MouseEvent arg0) {
					return this.createPopupMenu(arg0);
				}

				// 操作运算区为空时调用右键的方法
				public JPopupMenu createPopupMenuForGroupAndCalc(MouseEvent arg0) {
					return this.createPopupMenu(arg0);
				}

				// 操作运算区不为空时调用右键的方法
				public JPopupMenu createPopupMenu(MouseEvent evt) {

					JPopupMenu popupMenu = new JPopupMenu();
					popupMenu.add((new CutAction(this)).getMenuItem());
					popupMenu.add((new CopyAction(this)).getMenuItem());
					popupMenu.add((new PasteAction(this)).getMenuItem());
					popupMenu.addSeparator();
					ClearAllAction clearAllAction = new ClearAllAction(this) {
						private static final long serialVersionUID = 1L;

						public boolean executeAction(EventObject arg0,
								ReportPane arg1) {
							boolean result = super.executeAction(arg0, arg1);
							// 刷新显示排序
							reportGuideUI.reportOrderSet
									.showOrderSet(groupReport);
							// 刷新向中汇总显示顺序
							reportGuideUI.reportGroupSet
									.showGroupSet(groupReport);
							return result;
						}
					};

					clearAllAction.setName("清除");

					popupMenu.add(clearAllAction.getMenuItem());
					popupMenu.addSeparator();
					popupMenu.add((new DisplayCellElementAction(this))
							.getMenuItem());
					ICreateReportPaneRightMenu createReportPaneRightMenu = getCreateReportPaneRightMenu();
					if (createReportPaneRightMenu != null)
						createReportPaneRightMenu.createGridPopupMenu(evt,
								popupMenu);

					// 判断是不是在操作区，不在操作区，分组列、计算列和分栏菜单显示灰色
					boolean bEnable = false;
					CellSelection cell = designGroupReportPane
							.getCellSelection();
					int iRow = cell.getRow();
					int iCol = cell.getColumn();

					if (CreateGroupReport.isInGroupRow(iRow,
							RowConstants.UIAREA_HEADER, groupReport)) {
						SetHeadFundAction setHeadFundAction = new SetHeadFundAction(
								this, reportGuideUI, true);
						popupMenu.add(setHeadFundAction.getMenuItem());
						setHeadFundAction.setEnabled(true);

						CompareSetAction compareSetAction1 = new CompareSetAction(
								this, true, "分对分析1", ReportHeader.COMPARE_1);
						popupMenu.add(compareSetAction1);
						compareSetAction1.setEnabled(true);
						CompareSetAction compareSetAction2 = new CompareSetAction(
								this, true, "分对分析2", ReportHeader.COMPARE_2);
						popupMenu.add(compareSetAction2);
						compareSetAction2.setEnabled(true);
					}

					if (CreateGroupReport.isInGroupRow(iRow,
							RowConstants.UIAREA_OPERATION, groupReport)) {
						bEnable = true;
					} else {
						bEnable = false;
					}
					// 判断是否计算列
					boolean isCalcCol = DefinePub.isCalcCol(
							designGroupReportPane, iCol, iRow);

					// 判断是否计算列
					boolean isCalcGroup = DefinePub.isGroupCol(
							designGroupReportPane, iCol, iRow);

					SetGroupAction setGroupAction = new SetGroupAction(this,
							reportGuideUI, bEnable && isCalcGroup);
					popupMenu.add(setGroupAction.getMenuItem());
					setGroupAction.setEnabled(bEnable && isCalcGroup);

					SetCalcAction setCalcAction = new SetCalcAction(this,
							reportGuideUI, bEnable);
					popupMenu.add(setCalcAction.getMenuItem());
					setCalcAction.setEnabled(bEnable);

					SetSubFieldAction setSubFieldAction = new SetSubFieldAction(
							this, reportGuideUI, bEnable && isCalcCol);
					popupMenu.add(setSubFieldAction.getMenuItem());
					setSubFieldAction.setEnabled(bEnable && isCalcCol);

					// 标题区单位名称显示参数
					// if (CreateGroupReport.isInGroupRow(iRow,
					// RowConstants.UIAREA_TITLE, groupReport)) {
					// bEnable = true;
					// } else {
					// bEnable = false;
					// }
					// popupMenu.add(new SetParaAction(this, bEnable)
					// .getMenuItem());

					return popupMenu;
				}

			};
		}

		if (reportGuideUI.sReportId == null) {// 增加
			groupReport = new GroupReport();
			// 增加条件行
			groupReport.addPageFooterRow();
			// 界面属性对象
			SummaryReportUiAttr attr = new SummaryReportUiAttr();
			attr.setColumnTypeArray(new int[] { 0, 0, 0, 1, 1, 1 });
			attr.setColumnCount(10);
			attr.setTitleAreaRowCount(2);
			attr.setHeaderAreaRowCount(1);
			// // 设置列类型

			CreateGroupReport cg = new CreateGroupReport();
			int columnCount = attr.getColumnCount();
			try {
				// cg.makeReportNoGroupColumn(groupReport);
				int rowIndex = 0;

				/* titleArea */
				rowIndex += cg.addEmptyReportHeader(groupReport, rowIndex, attr
						.getTitleAreaRowCount(), columnCount);

				/* headerArea 头区域 */
				rowIndex += cg.addEmptyPageHeader(groupReport, rowIndex, attr
						.getHeaderAreaRowCount(), columnCount);

				/* OperationArea操作区 */
				rowIndex += cg.addEmptyDetail(groupReport, rowIndex, attr
						.getOperationAreaRowCount(), columnCount);

			} catch (Exception ex) {
				Log.logger.error("", ex);
			}
			// groupReport
			IReportQuerySource reportQuerySource = new ReportQuerySource();

			// reportQuerySource.setDataSourceManager();
			// reportQuerySource.setEnumSourceManager();
			// reportQuerySource.setParameterArray();
			reportQuerySource
					.setReportBasicAttr(new MySummaryReportBasicAttr());
			groupReport.setXMLable(reportQuerySource);
			reportQuerySource.getReportBasicAttr().setReportType(
					ReportTypeConstants.REPORT_TYPE_8_RUNTIME_STATIC_REPORT);

		} else {// 修改
			File file = getFileFromDB(Global.getSetYear(),
					reportGuideUI.sReportId, definReportServ);
			try {
				groupReport = (GroupReport) ReportTools.readReportByPath(file);
				// 检验枚举summaryIndex值
				ReportQuerySource querySource = (ReportQuerySource) ReportConver
						.getReportQuerySource(this.groupReport);
				CheckOutEnumName checkOutEnumName = new CheckOutEnumName(
						querySource);
				checkOutEnumName.checkEnumSummaryIndex(this.groupReport);
				// 校验原定义报表summaryIndex没有设置值问题
				SummaryIndexUnt.checkGroupDetail(groupReport);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 设置每面显示行数,默认为一万行
		int rowOfeveryPage = 10000;
		GroupReportAttr groupReportAttr = groupReport.getGroupReportAttr();
		if (groupReportAttr.getPageBreakAfterDetailRepeatTimes() <= -1) {
			groupReportAttr.setPageBreakAfterDetailRepeatTimes(rowOfeveryPage);
		}
		// 设置不自动补空行.
		groupReportAttr.setMakeUpBlankDetail(false);
		groupReport.setUpdateContentAble(new SummaryUpdateContent());
		// 行高不自适应
		groupReport.getReportSettings().setShrinkRowHeightToFitContent(false);
		designGroupReportPane.setReport(groupReport);
		designGroupReportPane.fireReportDataChanged();

		return this.designGroupReportPane;
	}

	public static GroupReport getGroupReport(String sReportId,
			IDefineReport definReportServ) {
		File file = getFileFromDB(Global.getSetYear(), sReportId,
				definReportServ);
		try {
			return (GroupReport) ReportTools.readReportByPath(file);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox("获得报表定义信息出错,请与管理员联系!", e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
			return null;
		}
	}

	/**
	 * @param reportPane
	 * @return 创建快速修改的 工具类按钮toolbar
	 * @see Jul 18, 2007 日 4:51:36 PM by mawenming(马文明)
	 */
	public static JComponent createReportToolBarPanel(ReportPane reportPane) {
		if (reportPane == null)
			return null;
		// 界面演示缓存对象,允许为null
		IStyleCacheAble styleCache = null;

		ReportToolBarLineComponentImpl barPanel = new ReportToolBarLineComponentImpl(
				styleCache);

		/* 单元格背景色,默认为false */
		barPanel.setShowUnmergeCell(true);
		/* 合并单元格,默认为false */
		barPanel.setShowMergeCell(true);

		/* 页面设置,默认为false */
		// barPanel.setShowPageSetup(true);
		/* 单元属性,默认为false */
		// barPanel.setShowCellAttribute(true);
		barPanel.setShowUndo(false);
		barPanel.setShowRedo(false);
		// barPanel.populate(reportPane);
		// 可以修改
		boolean editAble = true;
		return barPanel.getToolsBar(reportPane, editAble);
		// return barPanel.getPluginComponent();
	}

	class MyColumnRenderer extends DefaultGridColumnRenderer {

		/**
		 * @since mawenming at Mar 19, 2008日12:40:03 PM
		 */
		protected IGetColumnTypeAble columnTypeAble;

		/**
		 * @param columnTypeAble
		 */
		public MyColumnRenderer(IGetColumnTypeAble columnTypeAble) {
			super();
			this.columnTypeAble = columnTypeAble;
		}

		/**
		 * 
		 * @param index
		 * @return [列类型]A 的形式
		 * @see com.fr.cell.DefaultGridColumnRenderer#getDisplay(int)
		 * @since mawenming at Mar 18, 2008日2:50:29 PM
		 */
		public Object getDisplay(int index) {
			int type = columnTypeAble.getColumnType(index);

			StringBuffer buffer = new StringBuffer();
			buffer.append(super.getDisplay(index)).append("[").append(
					columnTypeAble.getColumnTypeName(type)).append("]");
			return buffer.toString();
		}
	}

	public static File getFileFromDB(String setYear, String reportID,
			IDefineReport definReportServ) {

		byte[] obj = null;
		try {
			obj = definReportServ.getOBByID(Global.getSetYear(), reportID,
					Global.loginmode);
		} catch (Exception e1) {

			e1.printStackTrace();
			new MessageBox("查询出错!", e1.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return null;
		}
		String fileName = IDefineReport.PATH_ + reportID + ".xml";
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		file.deleteOnExit();

		FileOutputStream out = null;

		try {
			out = new FileOutputStream(fileName);

			out.write(obj);
		} catch (Exception e1) {
			new MessageBox("取出文件出错!", e1.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e1.printStackTrace();
			file = null;
			try {
				out.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			return null;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			out = null;
		}
		return file;
	}

}
