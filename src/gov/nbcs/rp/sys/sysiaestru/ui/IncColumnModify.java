/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入栏目修改操作类
 * </p>
 * <p>
 * Description:收入栏目修改操作类
 * </p>

 */
public class IncColumnModify {
	// 收入栏目管理客户端主界面类
	private IncColumn incColumn = null;

	// 收入栏目DataSet
	private DataSet dsIncCol = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param incColumn
	 *            收入栏目管理客户端主界面类
	 */
	public IncColumnModify(IncColumn incColumn) {
		this.incColumn = incColumn;
		this.dsIncCol = incColumn.dsIncCol;
		// 定义数据库接口
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * 修改收入栏目操作方法
	 * 
	 * @return 返回修改操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean modify() throws HeadlessException, Exception {

		// 判断字段值是否完整
		if (dsIncCol.fieldByName("end_flag").getValue() == null) {
			JOptionPane.showConfirmDialog(incColumn, "数据不完整!", "提示",
					JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断是否是叶节点，不是叶节点，只能修改名称
		if (dsIncCol.fieldByName("end_flag").getInteger() != 1) {
			incColumn.sSaveType = "modname";
		} else { // 修改叶节点
			// 判断该节点是否被收入预算表使用,如使用只能修改格式
			String sEngName = dsIncCol.fieldByName("IncCol_Ename").getString();
			InfoPackage infoPackage = sysIaeStruServ.judgeIncColEnableModify(
					sEngName, Global.loginYear);

			// 该节点已被收入预算表使用,只能修改显示格式
			if (!infoPackage.getSuccess()) {
				incColumn.sSaveType = "modformate";
				JOptionPane.showMessageDialog(Global.mainFrame, infoPackage
						.getsMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
			} else { // 节点未被使用
				incColumn.sSaveType = "mod";
			}
		}
		// 设置收入栏目DataSet为编辑状态
		dsIncCol.edit();
		return true;
	}
}
