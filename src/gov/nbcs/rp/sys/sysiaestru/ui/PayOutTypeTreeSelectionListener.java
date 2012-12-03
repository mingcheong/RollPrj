/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出项目类别树SelectionListerner
 * </p>
 * <p>
 * Description:支出项目类别树SelectionListerner
 * </p>
 * <p>
 
 * @version 6.2.40
 */
public class PayOutTypeTreeSelectionListener implements TreeSelectionListener {
	// 支出项目类别管理客户端主界面类
	private PayOutType payOutType = null;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	public PayOutTypeTreeSelectionListener(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(
					payOutType.dsPayOutType, payOutType.getToolbarPanel(),
					payOutType.ftreePayOutType);
			setActionStatus.setState(true, true);
			if (payOutType.dsPayOutType.isEmpty()
					|| payOutType.dsPayOutType.bof()
					|| payOutType.dsPayOutType.eof())
				return;
			// 内码
			if (payOutType.dsPayOutType.fieldByName("PAYOUT_KIND_CODE")
					.getValue() != null)
				payOutType.ftxtPriCode.setValue(payOutType.dsPayOutType
						.fieldByName("PAYOUT_KIND_CODE").getString());
			else
				payOutType.ftxtPriCode.setValue("");
			// 编码
			if (payOutType.dsPayOutType.fieldByName("LVL_ID").getString() != "")
				payOutType.ftxtfPayOutTypeCode.setValue(payOutType.dsPayOutType
						.fieldByName("LVL_ID").getString());
			// 名称
			payOutType.ftxtfPayOutTypeName.setValue(payOutType.dsPayOutType
					.fieldByName("PAYOUT_KIND_NAME").getString());

			// 性质
			payOutType.flstPayOutTypeKind.setMaskValueChange(true);
			if ("".equals(payOutType.dsPayOutType.fieldByName(
					IPayOutType.STD_TYPE_CODE).getString())) {
				payOutType.flstPayOutTypeKind.setSelectedIndex(0);
			} else
				payOutType.flstPayOutTypeKind.setSelectedValue(
						payOutType.dsPayOutType.fieldByName(
								IPayOutType.STD_TYPE_CODE).getString(), true);
			payOutType.flstPayOutTypeKind.setMaskValueChange(false);
			// 控制数分配到明细
			if (payOutType.dsPayOutType.fieldByName("N1").getValue() != null) {
				((JCheckBox) payOutType.fchkPayOutFlag.getEditor())
						.setSelected(Common.estimate(new Integer(
								payOutType.dsPayOutType.fieldByName("N1")
										.getInteger())));
			} else {
				((JCheckBox) payOutType.fchkPayOutFlag.getEditor())
						.setSelected(false);
			}

			// 经济科目
			String[] sID = sysIaeStruServ.getJjWithPayoutKindCode(payOutType.dsPayOutType
					.fieldByName("payout_kind_code").getString(),
					Global.loginYear);
			SetSelectTree.setIsCheck(payOutType.ftreeAcctJJ, sID);

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(payOutType, "支出项目类别显示明细信息发生错误，错误信息："
					+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}
}
