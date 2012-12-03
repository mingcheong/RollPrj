/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入栏目删除操作类
 * </p>
 * <p>
 * Description:收入栏目删除操作类
 * </p>

 */
public class IncColumnDel {
	// 收入栏目管理客户端主界面类
	private IncColumn incColumn = null;

	// 收入栏目DataSet
	private DataSet dsIncCol = null;

	// 收入栏目对象
	private IncColumnObj incColumnObj = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param incColumn
	 *            收入栏目管理客户端主界面类
	 */
	public IncColumnDel(IncColumn incColumn) {
		// 定义入栏目管理客户端主界面类
		this.incColumn = incColumn;
		// 定义收入栏目DataSet
		this.dsIncCol = incColumn.dsIncCol;
		// 定义收入栏目对象
		this.incColumnObj = incColumn.incColumnObj;
		// 定义数据库接口
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * 删除收入栏目操作方法
	 * 
	 * @throws Exception
	 */
	public boolean delete() throws Exception {
		// 当删除节点的父节点只有一个子节点是，保存父节点编码
		String sIncColParCode = null;

		// 判断是否是末节点，不是末节点，不允许删除
		if (dsIncCol.fieldByName("End_flag").getValue() != null
				&& dsIncCol.fieldByName("End_flag").getInteger() == 0) {
			JOptionPane.showMessageDialog(incColumn, "收入栏目存在子栏目,请先删除子栏目!",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 取得收入栏目英文名,判断是否可能删除
		String sEngName = dsIncCol.fieldByName("IncCol_Ename").getString();
		String sIncColCode = dsIncCol.fieldByName("IncCol_Code").getString();
		String sIncColName = dsIncCol.fieldByName(IIncColumn.INCCOL_NAME)
				.getString();
		String sIncColEname = dsIncCol.fieldByName(IIncColumn.INCCOL_ENAME)
				.getString();
		InfoPackage infoPackage = sysIaeStruServ.judgeIncColEnableDel(sEngName,
				sIncColCode, Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incColumn, infoPackage.getsMessage(),
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 提示是否确定删除
		if (JOptionPane.showConfirmDialog(incColumn, "您是否确认要删除该条记录?", "提示",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
			return false;
		}

		// 判断选中的节点的父节点是否存在
		MyTreeNode node = incColumn.ftreeIncColumn
				.getSelectedNode();
		MyTreeNode parentNode = (MyTreeNode) node.getParent();
		// 判断选中的节点是不是根节点
		if (parentNode != (MyTreeNode) incColumn.ftreeIncColumn.getRoot()) {
			// 判断有几个兄弟，如果只有一个节点，相本节的部分信息传给父节点
			if (parentNode.getChildCount() == 1) {
				// 定位到父节点
				dsIncCol.gotoBookmark(parentNode.getBookmark());
				// 将部分信息设置给根节点
				dsIncCol.maskDataChange(true);
				// 设置收入栏目DataSet为编辑状态
				dsIncCol.edit();

				sIncColParCode = dsIncCol.fieldByName("INCCOL_CODE")
						.getString();

				// 设置收入栏目对应字段
				dsIncCol.fieldByName("INCCOL_ENAME").setValue(
						incColumnObj.INCCOL_ENAME);
				// 设置数据来源
				dsIncCol.fieldByName("DATA_SOURCE").setValue(
						new Integer(incColumnObj.DATA_SOURCE));
				// 设置计算公式内容
				dsIncCol.fieldByName("FORMULA_DET").setValue(
						incColumnObj.FORMULA_DET);
				// 设置计算优先级
				dsIncCol.fieldByName("CALC_PRI").setValue(
						new Integer(incColumnObj.CALC_PRI));
				// 设置栏目是否需要纵向求和
				dsIncCol.fieldByName("SUM_FLAG").setValue(
						new Integer(incColumnObj.SUM_FLAG));
				// 设置 末级标志
				dsIncCol.fieldByName("end_flag").setValue(
						new Integer(incColumnObj.END_FLAG));
				// 设置 显示格式
				dsIncCol.fieldByName("DISPLAY_FORMAT").setValue(
						incColumnObj.DISPLAY_FORMAT);
				// 设置编辑格式
				dsIncCol.fieldByName("EDIT_FORMAT").setValue(
						incColumnObj.EDIT_FORMAT);
				// 设置是否隐藏是否隐藏
				dsIncCol.fieldByName("IS_HIDE").setValue(
						new Integer(incColumnObj.IS_HIDE));
				dsIncCol.maskDataChange(false);
				// 定位回本节点
				dsIncCol.gotoBookmark(node.getBookmark());
			}
		}
		// 删除本节点
		dsIncCol.delete();
		// 提交数据库
		sysIaeStruServ.delIncCol(dsIncCol, sIncColParCode,
				incColumnObj.INCCOL_CODE, Global.loginYear, sIncColName,
				sIncColEname);
		dsIncCol.applyUpdate();
		// 刷新系统数据字典信息
		if (!SysUntPub.synDict(IIncColumn.INCCOL_TABLE)) {
			JOptionPane.showMessageDialog(incColumn, "刷新系统数据字典信息发生错误!", "提示",
					JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}
}
