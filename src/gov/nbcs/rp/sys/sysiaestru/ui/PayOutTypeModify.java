/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出项目类别修改操作类
 * </p>
 * <p>
 * Description:支出项目类别修改操作类
 * </p>
 * <p>

 */
public class PayOutTypeModify {
	private PayOutType payOutType;

	private PayOutTypeObj payOutTypeObj;

	public PayOutTypeModify(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.payOutTypeObj = payOutType.payOutTypeObj;
	}

	public void modify() throws HeadlessException, Exception {
		// 判断是否是叶节点
		if (payOutTypeObj.end_flag != 1) {
			payOutType.sSaveType = "modname";
		} else {
			ISysIaeStru sysIaeStruServ = SysIaeStruI.getMethod();
			InfoPackage infoPackage = sysIaeStruServ.judgePayOutTypeUse(
					payOutTypeObj.payout_kind_code, Global.loginYear);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(payOutType, infoPackage
						.getsMessage()
						+ ",只能修改编码!", "提示", JOptionPane.INFORMATION_MESSAGE);
				payOutType.sSaveType = "modformate";
			} else
				payOutType.sSaveType = "mod";
		}
		payOutType.dsPayOutType.edit();
	}
}
