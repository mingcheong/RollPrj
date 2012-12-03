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
 * Title:收入项目类别修改操作类
 * </p>
 * <p>
 * Description:收入项目类别修改操作类
 * </p>

 */
public class IncTypeModify {
	// 收入项目类别管理客户端主界面类
	private IncType incType = null;

	// 收入项目类别对象
	private IncTypeObj incTypeObj = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param incType
	 *            收入项目类别管理客户端主界面类
	 */
	public IncTypeModify(IncType incType) {
		this.incType = incType;
		this.incTypeObj = incType.incTypeObj;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	/**
	 * 修改收入项目类别操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void modify() throws HeadlessException, Exception {
		// 判断是否是叶节点
		if (incTypeObj.end_flag != 1) {
			incType.sSaveType = "modname";
		} else {
			InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeEnableModify(
					incTypeObj.inctype_code, String
							.valueOf(incTypeObj.set_year));
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(incType, infoPackage
						.getsMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
				incType.sSaveType = "modformate";
			} else {
				incType.sSaveType = "mod";
			}
		}
		incType.dsIncType.edit();
	}

}
