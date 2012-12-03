/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.HeadlessException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.rowset.ui.RowSetFrame;
import gov.nbcs.rp.queryreport.szzbset.ui.ConverSet;
import gov.nbcs.rp.queryreport.szzbset.ui.SzzbSetFrame;

import gov.nbcs.rp.sys.besqryreport.ui.BesUI;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:报表设计主界面
 * </p>
 * <p>
 * Description:报表设计主界面
 * </p>
 * <p>

 */
public class DefineReport extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	// 报表树
	private CustomTree ftreReport = null;

    //	 报表编码
	private FTextField reportNameCode = null;
	// 报表名称
	private FTextField reportNameTxt = null;

	// 报表分类
	private ReportTypeList reportTypeList;

	// 报表用户类型
	private FRadioGroup reportUserTypeGrp = null;

	// 是否进行四舍五入后再汇总
	// private FRadioGroup frdoIsMoneyOp = null;

	// // 是否跨年度
	// private FCheckBox fchkIsMulYear = null;
	//
	// // 是否是否跨区域
	// private FCheckBox fchkIsMulRgion = null;

	// 是否启用
	private FCheckBox fchkIsActice = null;

	// 报表种类
	private FComboBox cbxReoportSort = null;

	// 取得报表类型的数值
	private List lstReportSort = null;

	// 报表主表信息
	private RepSetObject repSetObject = null;

	// 得到报表信息
	private DataSet dsReport = null;

	public void initize() {
		// 定义数据库接口
		definReportServ = DefineReportI.getMethod();

		try {
			// 得到报表信息
			dsReport = definReportServ.getReport();
			// 定义设置的报表
			ftreReport = new CustomTree("定义的报表", dsReport,
					IDefineReport.SHOW_LVL, IQrBudget.REPORT_CNAME,
					IDefineReport.PAR_ID, null, IQrBudget.LVL_ID,false);

			// 定义树的选择事件
			ftreReport.addTreeSelectionListener(new TreeSelect());
			// 定义滚动面板
			FScrollPane spnlReport = new FScrollPane(ftreReport);

			ReportInfoPanel reportInfoPanel = new ReportInfoPanel();

			// 定义左右分隔组件
			FSplitPane slpnlBack = new FSplitPane();
			slpnlBack.setDividerLocation(300);
			slpnlBack.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			slpnlBack.add(spnlReport, JSplitPane.LEFT);
			slpnlBack.add(reportInfoPanel, JSplitPane.RIGHT);
			this.add(slpnlBack);

			// 创建工具栏
			this.createToolBar();
		} catch (Exception e) {
			new MessageBox("显示界面发生错误，错误信息:" + e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK + MessageBox.BUTTON_CANCEL).show();
			e.printStackTrace();
		}

	}

	private class ReportInfoPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public ReportInfoPanel() {
			jbInit();
		}

		private void jbInit() {

			// 定义报表名称文本框
//			 定义报表名称文本框
			reportNameCode = new FTextField("报表编码：");
			reportNameCode.setEditable(false);
			reportNameTxt = new FTextField("报表名称：");
			reportNameTxt.setEditable(false);

			// 报表分类信息
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("报表类型：");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));
			reportTypeList.setIsCheckBoxEnabled(false);

			// 取得报表种类的数值
			lstReportSort = definReportServ.getReportSort();

			// 定义报表种类
			Vector reportTypeVertor = new Vector();
			Map reportTypeMa = null;
			String report_type;
			for (int i = 0; i < lstReportSort.size(); i++) {
				reportTypeMa = (Map) lstReportSort.get(i);
				report_type = reportTypeMa.get("reporttype_name").toString();
				reportTypeVertor.add(report_type);
			}
			// 报表种类
			cbxReoportSort = new FComboBox("报表种类：");
			cbxReoportSort.setRefModel(reportTypeVertor);
			cbxReoportSort.setEnabled(false);

			// 定义报表用户类型
			reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
			reportUserTypeGrp.setRefModel("0#仅单位使用 +1#仅财政使用+2#财政单位共同使用");
			reportUserTypeGrp.setTitleVisible(false);
			reportUserTypeGrp.setValue("0");
			reportUserTypeGrp.setEditable(false);

			// 定义报表用户类型面板
			FPanel reportUserTypePnl = new FPanel();
			reportUserTypePnl.setTitle("用户类型");
			reportUserTypePnl.setLayout(new RowPreferedLayout(1));
			// 报表用户类型单选框加入报表用户类型面板
			reportUserTypePnl.addControl(reportUserTypeGrp,
					new TableConstraints(1, 1, true, true));

			// // 是否进行四舍五入后再汇总
			// frdoIsMoneyOp = new FRadioGroup("", FRadioGroup.HORIZON);
			// frdoIsMoneyOp.setRefModel("0#不四舍五入+1#四舍五入后再汇总+2#汇总后再四舍五入");
			// frdoIsMoneyOp.setTitleVisible(false);
			// frdoIsMoneyOp.setValue("0");
			//
			// FPanel isMoneyOpPnl = new FPanel();
			// isMoneyOpPnl.setTitle("四舍五入选择");
			// isMoneyOpPnl.setLayout(new RowPreferedLayout(1));
			// isMoneyOpPnl.addControl(frdoIsMoneyOp, new TableConstraints(1, 1,
			// true, true));
			//
			// // 是否跨年度
			// fchkIsMulYear = new FCheckBox("是否跨年度");
			// fchkIsMulYear.setTitlePosition("RIGHT");
			//
			// // 是否跨区域
			// fchkIsMulRgion = new FCheckBox("是否跨区域");
			// fchkIsMulRgion.setTitlePosition("RIGHT");

			// 是否启用
			fchkIsActice = new FCheckBox("是否启用");
			fchkIsActice.setTitlePosition("RIGHT");
			fchkIsActice.setEditable(false);

			// 设置左边显示面板
			FPanel leftPanel = new FPanel();
			leftPanel.setTitle("报表信息");
			RowPreferedLayout leftRlay = new RowPreferedLayout(1);
			leftRlay.setRowGap(8);
			leftPanel.setLayout(leftRlay);

			// 报表名称文本框加入左边显示面板
			leftPanel.addControl(reportNameCode, new TableConstraints(1, 1,
					false, true));
			leftPanel.addControl(reportNameTxt, new TableConstraints(1, 1,
					false, true));
			// 报表类型
			leftPanel.addControl(cbxReoportSort, new TableConstraints(1, 1,
					false, true));
			// 定义报表用户类型面板加入左边显示面板
			leftPanel.addControl(reportUserTypePnl, new TableConstraints(2, 1,
					false, true));

			// // 是否进行四舍五入后再汇总选择框加入左边显示面板
			// leftPanel.addControl(isMoneyOpPnl, new TableConstraints(2, 1,
			// false, true));
			// // 是否跨年度选择框加入左边显示面板
			// leftPanel.addControl(fchkIsMulYear, new TableConstraints(1, 1,
			// false, true));
			// // 是否是否跨区域选择框加入左边显示面板
			// leftPanel.addControl(fchkIsMulRgion, new TableConstraints(1, 1,
			// false, true));

			// 报表分类组合框加入左边显示面板
			leftPanel.addControl(fpnlReportType, new TableConstraints(5, 1,
					false, true));
			// 是否启用
			leftPanel.addControl(fchkIsActice, new TableConstraints(1, 1,
					false, true));

			// 设置布局
			this.setLayout(new RowPreferedLayout(2));
			this.addControl(leftPanel, new TableConstraints(1, 1, true, true));

		}
	}

	/**
	 * 定义树选中节点改变事件
	 */
	private class TreeSelect implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
			try {
				repSetObject = new RepSetObject();
				BeanUtil.mapping(repSetObject, dsReport.getOriginData());
				// 报表名称
				String sReportCode = repSetObject.getREPORT_ID();
				reportNameCode.setValue(sReportCode);
				String sReportName = repSetObject.getREPORT_CNAME();
				reportNameTxt.setValue(sReportName);

				// // 是否进行四舍五入后再汇总
				// int isMoneyOp = repSetObject.getIS_MONEYOP();
				// frdoIsMoneyOp.setValue(String.valueOf(isMoneyOp));
				// // 是否跨年度
				// int isMulYear = repSetObject.getIS_MONEYOP();
				// ((JCheckBox) fchkIsMulYear.getEditor()).setSelected(Common
				// .estimate(new Integer(isMulYear)));
				// // 是否跨区域
				// int isMulRgion = repSetObject.getIS_MULRGION();
				// ((JCheckBox) fchkIsMulRgion.getEditor()).setSelected(Common
				// .estimate(new Integer(isMulRgion)));

				// 定义报表分类
				reportTypeList.setSelected(repSetObject.getREPORT_ID());
				// 定义报表用户类型
				String sUserType = String.valueOf(repSetObject.getDATA_USER());
				reportUserTypeGrp.setValue(sUserType);

				int iTypeFalg = repSetObject.getTYPE_FLAG();
				String sValue = null;
				Map reportTypeMa;
				for (int i = 0; i < lstReportSort.size(); i++) {
					reportTypeMa = (Map) lstReportSort.get(i);
					if (Integer.parseInt(reportTypeMa.get("reporttype_id")
							.toString()) == iTypeFalg) {
						sValue = reportTypeMa.get("reporttype_name").toString();
					}
				}
				cbxReoportSort.setValue(sValue);

				// 是否跨区域
				String sIsActice = repSetObject.getIS_ACTIVE();
				if ("是".equals(sIsActice))
					((JCheckBox) fchkIsActice.getEditor()).setSelected(true);
				else
					((JCheckBox) fchkIsActice.getEditor()).setSelected(false);

			} catch (Exception e1) {
				new MessageBox("发生错误，错误信息:" + e1.getMessage(),
						MessageBox.ERROR, MessageBox.BUTTON_OK
								+ MessageBox.BUTTON_CANCEL).show();
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 增加报表
	 */
	public void doAdd() {
		// 定义报表类型选择类
		DefineReportTypeChoice reportTypeChoice = new DefineReportTypeChoice();
		// 定义选择的报表类型
		int reportType;
		try {
			Tools.centerWindow(reportTypeChoice);
			reportTypeChoice.setVisible(true);
			// 得到选中的报表类型
			reportType = reportTypeChoice.getReportType();
		} finally {
			reportTypeChoice.dispose();
		}
		// 判断是否选中报表
		if (reportType == -1)
			return;

		// 根据报表类型，调用不同的新增报表向导
		switch (reportType) {
		case IDefineReport.REPORTTYPE_COVER: {// 目录或封面
			new ConverSet(null, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_SZZB: {// 收支总表设置
			new SzzbSetFrame("", Global.loginYear, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_ROW: {// 单位综合情况表
			new RowSetFrame("", Global.loginYear, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_GROUP: {// 分组汇总表设置
			JFrame reportGuide = new ReportGuideUI(null, this);
			reportGuide.setVisible(true);
			break;
		}
		case IDefineReport.REPORTTYPE_OTHER: {// 其他报表设置
			new BesUI(null, this).show();
			break;
		}
		}
	}

	/**
	 * 删除报表
	 */
	public void doDelete() {
		try {
			// 删除前检查
			if (ftreReport.getSelectedNode() == null
					|| ftreReport.getSelectionPath().getLastPathComponent() == ftreReport
							.getRoot()
					|| ftreReport.getDataSet().fieldByName("end_flag")
							.getString().equals("0")) {
				new MessageBox("请选择要删除的报表!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			// 删除前提示
			String sReportName = ftreReport.getDataSet().fieldByName(
					"report_cname").getString();
			String sReportID = ftreReport.getDataSet().fieldByName("report_id")
					.getString();
			String isPrivate = ftreReport.getDataSet().fieldByName("c1")
			.getString();
			if(!GlobalEx.isFisVis()&&!"1".equals(isPrivate))
			{	
			
				JOptionPane.showMessageDialog(Global.mainFrame, "非个人报表，不能删除!"
						);
			return;
			}
				
				
			if (JOptionPane.showConfirmDialog(this, "确定要删除报表:[" + sReportID
					+ "]" + sReportName + "  吗?", "提示",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return;
			// 执行删除
			String sErr = definReportServ.deleteReport(sReportID, Global
					.getSetYear());
			if (!sErr.equals("")) {
				new MessageBox(sErr, MessageBox.MESSAGE, MessageBox.BUTTON_OK)
						.show();
				return;
			}

			// 刷新树，删除节点时
			refreshNodeDel();

			new MessageBox("删除成功!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox("删除报表失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return;
		}

	}

	/**
	 * 刷新树，删除节点时
	 * 
	 * @throws Exception
	 */
	private void refreshNodeDel() throws Exception {
		DataSet ds = ftreReport.getDataSet();
		String curNodeReportID = ds.fieldByName(IQrBudget.REPORT_ID)
				.getString();
		removeNode(curNodeReportID);
	}

	/**
	 * 根据报表ID移除树节点
	 * 
	 * @param sReportId报表ID
	 * @throws Exception
	 */
	private void removeNode(String sReportId) throws Exception {
		DataSet ds = ftreReport.getDataSet();
		MyTreeNode node;
		// 删除同一报表的其他节点
		DefaultMutableTreeNode root = ftreReport.getRoot();
		Enumeration enume = root.breadthFirstEnumeration();
		while (enume.hasMoreElements()) {
			node = (MyTreeNode) enume.nextElement();
			ds.gotoBookmark(node.getBookmark());
			if (sReportId.equals(ds.fieldByName(IQrBudget.REPORT_ID)
					.getString())) {
				node.removeFromParent();
				ds.delete();
			}
		}
		if (ds.getState() != StatefulData.DS_BROWSE)
			ds.applyUpdate();
		ftreReport.updateUI();
	}

	/**
	 * 修改或增加节点时，刷新树节点
	 * 
	 * @param repSetObjectA
	 * @param lstType
	 * @throws Exception
	 */
	public void refreshNodeEdit(RepSetObject repSetObjectA, List lstType,
			String sReportId) throws Exception {
		// 修改节点
		if (!Common.isNullStr(sReportId)) {
			// 根据报表ID移除树节点
			removeNode(sReportId);
		}

		DataSet ds = ftreReport.getDataSet();
		ds.maskDataChange(true);

		Map mapData;
		int size = lstType.size();
		for (int i = 0; i < size; i++) {
			mapData = new HashMap();
			repSetObjectA.setSHOW_LVL(repSetObjectA.getREPORT_ID()
					+ lstType.get(i));
			repSetObjectA.setPAR_ID(lstType.get(i).toString());
			BeanUtil.decodeObject(mapData, repSetObjectA);
			ds.append();
			ds.setOriginData(mapData);
		}
		ds.applyUpdate();
		ds.maskDataChange(false);
		ftreReport.reset();
//		ftreReport.reset();
		// 定位到新增加的节点
		ftreReport.expandTo(IDefineReport.SHOW_LVL, repSetObjectA
				.getREPORT_ID()
				+ lstType.get(0));
	}
	/**
	 * 修改或增加节点时，刷新树节点
	 * 
	 * @param ds
	 * @param lstType
	 * @throws Exception
	 */
	public void refreshNodeEdit(DataSet ds, List lstType, String sReportId)
			throws Exception {
		RepSetObject repSetObjectTmp = new RepSetObject();
		BeanUtil.mapping(repSetObjectTmp, ds.getOriginData());
		this.refreshNodeEdit(repSetObjectTmp, lstType, sReportId);
	}

	/**
	 * 修改或增加节点时，刷新树节点
	 * 
	 * @param ds
	 * @param lstType
	 * @throws Exception
	 */
	public void refreshNodeEdit(XMLData report, List lstType, String sReportId)
			throws Exception {
		RepSetObject repSetObjectTmp = new RepSetObject();
		BeanUtil.mapping(repSetObjectTmp, report);
		this.refreshNodeEdit(repSetObjectTmp, lstType, sReportId);
	}

	public void doCancel() {

	}

	public void doInsert() {

	}

	public void doModify() {
		// 删除前检查
		if (ftreReport.getSelectedNode() == null
				|| ftreReport.getSelectionPath().getLastPathComponent() == ftreReport
						.getRoot() || !ftreReport.getSelectedNode().isLeaf()) {
			new MessageBox("请选择要修改的报表!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
//		try {
//			if(!GlobalEx.isFisVis()&&!"1".equals(ftreReport.getDataSet().fieldByName("c1")
//					.getString().toString()))
//			{	
//			
//				JOptionPane.showMessageDialog(Global.mainFrame, "非个人报表，单位不修改!"
//						);
//			return;
//			}
//		} catch (HeadlessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// 定义选择的报表类型
		int typeFlag = repSetObject.getTYPE_FLAG();

		// 根据报表类型，调用不同的新增报表向导
		switch (typeFlag) {
		case IDefineReport.REPORTTYPE_COVER: {// 目录或封面
			new ConverSet(repSetObject, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_SZZB: {// 收支总表设置
			new SzzbSetFrame(repSetObject.getREPORT_ID(), Global.loginYear,
					this).show();

			break;
		}
		case IDefineReport.REPORTTYPE_ROW: {// 单位综合情况表
			new RowSetFrame(repSetObject.getREPORT_ID(), Global.loginYear, this)
					.show();
			break;
		}
		case IDefineReport.REPORTTYPE_GROUP: {// 分组汇总表设置
			new ReportGuideUI(repSetObject, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_OTHER: {// 其他报表设置
			new MessageBox("其他特殊报表不支持修改!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			// new BesUI(repSetObject.getREPORT_ID(), this).show();
			break;
		}
		}
	}

	public void doSave() {

	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	public CustomTree getFtreReport() {
		return ftreReport;
	}

	public RepSetObject getRepSetObject() {
		return repSetObject;
	}

	public DataSet getDsReport() {
		return dsReport;
	}

	public IDefineReport getDefinReportServ() {
		return definReportServ;
	}

	public ReportTypeList getReportTypeList() {
		return reportTypeList;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
