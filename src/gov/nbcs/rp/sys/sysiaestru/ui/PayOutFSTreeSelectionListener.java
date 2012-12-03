/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出资金来源树SelectionListerner
 * </p>
 * <p>
 * Description:支出资金来源树SelectionListerner
 * </p>

 */

public class PayOutFSTreeSelectionListener implements TreeSelectionListener {
	// 支出资金来源管理客户端主界面类
	private PayOutFS payOutFS = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutFS
	 *            支出资金来源管理客户端主界面类
	 */
	public PayOutFSTreeSelectionListener(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		// 定义数据库接口
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * 调用方法
	 */
	public void valueChanged(TreeSelectionEvent e) {
		try {
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(
					payOutFS.dsPayOutFS, payOutFS, payOutFS.ftreePayOutFS);
			setActionStatus.setState(true, true);
			if (payOutFS.dsPayOutFS.isEmpty() || payOutFS.dsPayOutFS.bof()
					|| payOutFS.dsPayOutFS.eof())
				return;
			// 内码
			if (payOutFS.dsPayOutFS.fieldByName("Pfs_code").getValue() != null)
				payOutFS.ftxtPriCode.setValue(payOutFS.dsPayOutFS.fieldByName(
						"Pfs_code").getString());
			else
				payOutFS.ftxtPriCode.setValue("");
			// 编码
			if (payOutFS.dsPayOutFS.fieldByName("LVL_ID").getValue() != null)
				payOutFS.ftxtfPfsCode.setValue(payOutFS.dsPayOutFS.fieldByName(
						"LVL_ID").getString());
			else
				payOutFS.ftxtfPfsCode.setValue("");
			// 名称
			payOutFS.ftxtfPfsName.setValue(payOutFS.dsPayOutFS.fieldByName(
					"PFS_NAME").getString());
			// 性质
			payOutFS.flstPfsKind.setMaskValueChange(true);
			if ("".equals(payOutFS.dsPayOutFS.fieldByName("STD_TYPE_CODE")
					.getString())) {
				payOutFS.flstPfsKind.setSelectedIndex(0);
			} else
				payOutFS.flstPfsKind.setSelectedValue(payOutFS.dsPayOutFS
						.fieldByName("STD_TYPE_CODE").getString(), true);
			payOutFS.flstPfsKind.setMaskValueChange(false);
			// 参与控制数分配
			if (payOutFS.dsPayOutFS.fieldByName("CF_PFS_FLAG").getValue() != null) {
				((JCheckBox) payOutFS.fchkPfsFlag.getEditor())
						.setSelected(Common.estimate(new Integer(
								payOutFS.dsPayOutFS.fieldByName("CF_PFS_FLAG")
										.getInteger())));
			} else {
				((JCheckBox) payOutFS.fchkPfsFlag.getEditor())
						.setSelected(false);
			}
			// 默认隐藏
			if (payOutFS.dsPayOutFS.fieldByName(IPayOutFS.IN_COMMON_USE)
					.getValue() != null) {
				((JCheckBox) payOutFS.fchkHide.getEditor()).setSelected(Common
						.estimate(new Integer(payOutFS.dsPayOutFS.fieldByName(
								IPayOutFS.IN_COMMON_USE).getInteger())));
			} else {
				((JCheckBox) payOutFS.fchkHide.getEditor()).setSelected(false);
			}
			// 支持平衡 mod by Cl 09-09-04
			if (payOutFS.dsPayOutFS.fieldByName(IPayOutFS.IS_BALANCE)
					.getValue() != null) {
				((JCheckBox) payOutFS.fchkIsBalance.getEditor()).setSelected(Common
						.estimate(new Integer(payOutFS.dsPayOutFS.fieldByName(
								IPayOutFS.IS_BALANCE).getInteger())));
			} else {
				((JCheckBox) payOutFS.fchkIsBalance.getEditor()).setSelected(false);
			}
			// 支持项目情况
			if (payOutFS.dsPayOutFS.fieldByName("SUP_PRJ").getValue() != null) {
				payOutFS.frdoSupPrj.setValue(payOutFS.dsPayOutFS.fieldByName(
						"SUP_PRJ").getString());
			} else {
				payOutFS.frdoSupPrj.setValue("0");
			}
			// 数据来源
			if (payOutFS.dsPayOutFS.fieldByName("DATA_SOURCE").getValue() != null) {
				payOutFS.frdoIncColDts.setValue(payOutFS.dsPayOutFS
						.fieldByName("DATA_SOURCE").getString());
			} else {
				payOutFS.frdoIncColDts.setValue("0");
			}
			// 显示格式
			payOutFS.fcbxSFormate.setValue(payOutFS.dsPayOutFS.fieldByName(
					"DISPLAY_FORMAT").getString());
			// 计算公式
			if (payOutFS.dsPayOutFS.fieldByName("FORMULA_DET").getString() != "") {
				payOutFS.ftxtaPfsFormula.setValue(PubInterfaceStub.getMethod()
						.replaceTextEx(
								payOutFS.dsPayOutFS.fieldByName("FORMULA_DET")
										.getString(), 0,
								"fb_iae_payout_fundsource", "PFS_ENAME",
								"PFS_FNAME", "set_year =" + Global.loginYear));
			} else {
				payOutFS.ftxtaPfsFormula.setValue("");
			}
			// 计算优先级
			if (payOutFS.dsPayOutFS.fieldByName("CALC_PRI").getValue() != null) {
				payOutFS.jspPfsCalcPRI.setValue(new Integer(payOutFS.dsPayOutFS
						.fieldByName("CALC_PRI").getInteger()));
			}
			payOutFS.ftxtfPfsCode.setFocus();

			// 根据数据来源，显示信息
			if (payOutFS.dsPayOutFS.fieldByName("DATA_SOURCE").getInteger() == 0) {// 录入
				// 得到收入栏目对应收费项目信息
				DataSet dsPfsToAcct = sysIaeStruServ.getPfsToAcct(
						Global.loginYear, payOutFS.dsPayOutFS.fieldByName(
								"PFS_CODE").getValue().toString());
				// 根据支出资金来源与功能科目对应关系设置功能科目节点选中状态
				SetSelectTree.setIsCheck(payOutFS.fpnlAcct.getFtreAcct(),
						dsPfsToAcct, "acct_code");
			} else {
				SetSelectTree.setIsNoCheck(payOutFS.fpnlAcct.getFtreAcct());
			}
			// 查询报表名称
			payOutFS.ftxtReportPfsName.setValue(payOutFS.dsPayOutFS
					.fieldByName("C1").getString());

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(payOutFS, "显示支出资金来源明细信息发生错误，错误信息："
					+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}
}
