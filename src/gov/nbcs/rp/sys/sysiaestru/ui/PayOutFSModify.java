/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;

/**
 * <p>
 * Title:支出资金来源修改操作类
 * </p>
 * <p>
 * Description:支出资金来源修改操作类
 * </p>

 */
public class PayOutFSModify {
	// 支出资金来源管理客户端主界面类
	private PayOutFS payOutFS = null;

	// 支出资金来源对象
	private PayOutFsObj payOutFsObj = null;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutFS
	 *            支出资金来源管理客户端主界面类
	 */
	public PayOutFSModify(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		this.payOutFsObj = payOutFS.payOutFsObj;
		// 定义数据库接口
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * 修改支出资金来源操作方法
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	public void modify() throws HeadlessException, Exception {
		// 判断是否是叶节点
		if (payOutFsObj.end_flag != 1) {
			payOutFS.sSaveType = "modname";
		} else {
			InfoPackage infoPackage = sysIaeStruServ.chkFundSourceByRef(String
					.valueOf(payOutFsObj.set_year), payOutFsObj.PFS_CODE,
					payOutFsObj.pfs_ename);
			int iReUse = Integer.parseInt(infoPackage.getObject().toString());
			if (iReUse != 0) {
				payOutFS.sSaveType = "modformate";
				JOptionPane.showMessageDialog(payOutFS, "该资金来源\n"
						+ infoPackage.getsMessage() + "只能修改显示格式!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				payOutFS.sSaveType = "mod";
			}
		}
		payOutFS.dsPayOutFS.edit();
	}

}
