/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.image.ImageObserver;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;
import gov.nbcs.rp.common.ui.input.IntegerTextField;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysiaestru.ui.SetActionStatus;

import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.exception.AppException;

/**
 * <p>
 * Title:报表类型维护客户端界面
 * </p>
 * <p>
 * Description:报表类型维护客户端界面
 * </p>
 * <p>

 */
public class ReportTypeUI extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	// 报表类型树
	private CustomTree treReportType;

	private DataSet dsReportType;

	// 右边编辑区
	private FPanel fpnlRight;

	// 编码
	private IntegerTextField ftxtCode = null;

	// 名称
	private FTextField ftxtName = null;
	
	private  FCheckBox ftxtIsdiv = null;

	private String sSaveType;

	/**
	 * 构造函数
	 * 
	 */
	public ReportTypeUI() {

	}

	/**
	 * 界面初始化
	 */
	public void initize() {
		// 定义数据库接口
		definReportServ = DefineReportI.getMethod();

		FSplitPane fSplitPane = new FSplitPane();
		fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		fSplitPane.setDividerLocation(300);
		this.add(fSplitPane);

		// 得到报表分类数据
		try {
			dsReportType = definReportServ.getReportType();
			// DataSet状态改变事件
			dsReportType
					.addStateChangeListener(new ReportTypeStateChangeListener());
			// 报表类型树
			treReportType = new CustomTree("报表类型", dsReportType,
					IDefineReport.LVL_ID, IDefineReport.CODE_NAME, null,
					SysCodeRule.createClient("6"), IDefineReport.LVL_ID);

		} catch (Exception e1) {
			e1.printStackTrace();
			new MessageBox(Global.mainFrame, "错误信息：" + e1.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}

		// TreeSelectionListener事件
		treReportType
				.addTreeSelectionListener(new ReportTypeTreeSelectionListener());

		ftxtCode = new IntegerTextField("编码：");
		ftxtCode.setMaxLength(6);
		ftxtName = new FTextField("名称：");
		
		ftxtIsdiv = new FCheckBox("个人报表");
		
		fpnlRight = new FPanel();
		fpnlRight.setTopInset(20);
		fpnlRight.setLeftInset(20);
		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setColumnWidth(300);
		fpnlRight.setLayout(rLay);
		fpnlRight
				.addControl(ftxtCode, new TableConstraints(1, 1, false, false));
		fpnlRight
				.addControl(ftxtName, new TableConstraints(1, 1, false, false));
		fpnlRight
		        .addControl(ftxtIsdiv, new TableConstraints(1, 1, false, false));

		fSplitPane.addControl(treReportType);
		fSplitPane.addControl(fpnlRight);

		try {
			this.createToolBar();
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsReportType,
					this, treReportType);
			setActionStatus.setState(true, true);
		} catch (ControlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 设编辑区控件不可编辑
		Common.changeChildControlsEditMode(fpnlRight, false);

	}

	/**
	 * 报表类型树TreeSelectionListener事件
	 * 
	 */
	private class ReportTypeTreeSelectionListener implements
			TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent arg0) {

			if (dsReportType.isEmpty() || dsReportType.bof()
					|| dsReportType.eof())
				return;

			try {
				ftxtCode.setValue(dsReportType
						.fieldByName(IDefineReport.LVL_ID).getString());
				ftxtName.setValue(dsReportType.fieldByName(IDefineReport.NAME)
						.getString());
				if("1".equals(dsReportType.fieldByName("c1").getString().toString()))
				ftxtIsdiv.setValue(new Boolean(true));
				else
				ftxtIsdiv.setValue(new Boolean(false));
				
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox(Global.mainFrame, "显示信息出错，错误信息："
						+ e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

		}
	}

	/**
	 * DataSet状态改变事件
	 * 
	 */
	private class ReportTypeStateChangeListener implements StateChangeListener {

		private static final long serialVersionUID = 1L;

		public void onStateChange(DataSetEvent e) throws Exception {

			if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				ReportTypeUI.this.setAllControlEnabledFalse();
				treReportType.setEnabled(true);
			} else {
				treReportType.setEnabled(false);
				ReportTypeUI.this.setAllControlEnabledTrue();

			}
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsReportType,
					ReportTypeUI.this, treReportType);
			setActionStatus.setState(true, true);
		}
	}

	/**
	 * 增加
	 */
	public void doAdd() {
		try {
			dsReportType.append();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(Global.mainFrame, "增加出错，错误信息：" + e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}
		clearShowInfo();
		ftxtCode.setFocus();
		sSaveType = "add";
	}

	/**
	 * 删除
	 */
	public void doDelete() {
		if (treReportType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "请选择一个报表类型！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			String code = dsReportType.fieldByName(IDefineReport.CODE)
					.getString();
			String sErr = definReportServ.deleteReportType(code);
			if (!Common.isNullStr(sErr)) {
				JOptionPane.showMessageDialog(this, sErr, "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			dsReportType.delete();
			dsReportType.applyUpdate();
			JOptionPane.showMessageDialog(this, "删除成功！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * 取消
	 */
	public void doCancel() {
		try {
			String lvl_id = dsReportType.fieldByName(IDefineReport.LVL_ID)
					.getString();

			dsReportType.cancel();
			if (Common.isNullStr(lvl_id))
				clearShowInfo();
			else
				treReportType.expandTo(IDefineReport.LVL_ID, lvl_id);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(Global.mainFrame, "取消出错，错误信息：" + e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}
	}

	public void doInsert() {

	}

	/**
	 * 修改
	 */
	public void doModify() {
		if (treReportType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "请选择一个报表类型！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {

			dsReportType.edit();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(Global.mainFrame, "修改出错，错误信息：" + e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}
		sSaveType = "mod";
	}

	/**
	 * 保存
	 * 
	 * @throws Exception
	 */
	public void doSave() {
		String txtIsdiv;
		boolean c1= ((Boolean) ftxtIsdiv.getValue()).booleanValue();
	    if(c1)
		{
	    	txtIsdiv= "1";	
		} else {
			txtIsdiv="0";
		}
		String sLvl = ftxtCode.getValue().toString();
		if (Common.isNullStr(sLvl)) {
			JOptionPane.showMessageDialog(this, "请填写编码！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			ftxtCode.setFocus();
			return;
		}
		if (sLvl.length() != 6) {
			JOptionPane.showMessageDialog(this, "编码长度为6，请修改！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			ftxtCode.setFocus();
			return;
		}

		String sName = ftxtName.getValue().toString();
		if (Common.isNullStr(sName)) {
			JOptionPane.showMessageDialog(this, "请填写名称！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			ftxtName.setFocus();
			return;
		}

		try {
			String sCode;
			if (sSaveType.equals("add")) {// 增加
				sCode = definReportServ.saveReportType("", sLvl, sName,txtIsdiv);

			} else { // 修改
				sCode = dsReportType.fieldByName(IDefineReport.CODE)
						.getString();
				definReportServ.saveReportType(sCode, sLvl, sName,txtIsdiv);
			}
			dsReportType.maskDataChange(true);
			dsReportType.fieldByName(IDefineReport.CODE).setValue(sCode);
			dsReportType.fieldByName(IDefineReport.LVL_ID).setValue(sLvl);
			dsReportType.fieldByName(IDefineReport.NAME).setValue(sName);
			dsReportType.fieldByName(IDefineReport.CODE_NAME).setValue(
					sLvl + " " + sName);
			
			dsReportType.fieldByName("c1").setValue(txtIsdiv);
	
			dsReportType.applyUpdate();
			dsReportType.maskDataChange(false);

			// 刷新树
			treReportType.reset();

			// 定位到节点
			this.treReportType.expandTo(IDefineReport.LVL_ID, sLvl);

			JOptionPane.showMessageDialog(this, "保存成功！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"保存发生错误，错误信息：" + e.getMessage(), "提示", ImageObserver.ERROR);
			e.printStackTrace();
		}

	}

	/**
	 * 关闭
	 */
	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	/**
	 * 
	 * 设编辑区控件不可编辑
	 */
	public void setAllControlEnabledFalse() {
		Common.changeChildControlsEditMode(fpnlRight, false);
	}

	/**
	 * 
	 * 设编辑区控件可编辑
	 */
	public void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(fpnlRight, true);
	}

	private void clearShowInfo() {
		// 层次码
		ftxtCode.setValue("");
		// 名称
		ftxtName.setValue("");
		
		ftxtIsdiv.setValue("");
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
