/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;

/**
 * <p>
 * Title:收入项目类别删除操作类
 * </p>
 * <p>
 * Description:收入项目类别删除操作类
 * </p>
 * <p>

 */

public class IncTypeDel {
	// 收入项目类别管理客户端主界面类
	private IncType incType = null;

	// 收入项目类别数据集
	private DataSet dsIncType = null;

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
	public IncTypeDel(IncType incType) {
		this.incType = incType;
		this.dsIncType = incType.dsIncType;
		this.incTypeObj = incType.incTypeObj;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	/**
	 * 删除收入项目类别操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean delete() throws HeadlessException, Exception {

		// 当删除节点的父节点只有一个子节点是，保存父节点编码
		String sParIncTypeCode = null;// 父节点编码

		// 判断是否是末节点，不是末节点，不允许删除
		if (dsIncType.fieldByName("End_flag").getValue() != null
				&& dsIncType.fieldByName("End_flag").getInteger() == 0) {
			JOptionPane.showMessageDialog(incType, "收入项目类别存在子栏目,请先删除子栏目!",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断能否删除
		InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeEnableDel(
				incTypeObj.inctype_code, String.valueOf(incTypeObj.set_year));
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incType, infoPackage.getsMessage()
					+ "，不能删除。", "提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 提示是否确定删除
		if (JOptionPane.showConfirmDialog(incType, "您是否确认要删除该条记录?", "提示",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return false;
		// 判断选中的节点的父节点是否存在
		MyTreeNode node = incType.ftreeIncType.getSelectedNode();
		MyTreeNode parentNode = (MyTreeNode) node.getParent();
		if (parentNode != (MyTreeNode) incType.ftreeIncType.getRoot()) {
			// 判断有几个兄弟，如果只有一个节点，相本节的部分信息传给父节点
			if (parentNode.getChildCount() == 1) {
				// 定位到父节点
				dsIncType.gotoBookmark(parentNode.getBookmark());
				sParIncTypeCode = dsIncType.fieldByName("inctype_code")
						.getString();
				// 将部分信息
				dsIncType.maskDataChange(true);
				dsIncType.edit();
				dsIncType.fieldByName("std_type_code").setValue(
						incTypeObj.std_type_code);
				dsIncType.fieldByName("end_flag").setValue(
						new Integer(incTypeObj.end_flag));
				dsIncType.fieldByName("is_inc").setValue(
						new Integer(incTypeObj.is_inc));
				dsIncType.fieldByName(IIncType.IS_SUM).setValue(
						new Integer(incTypeObj.is_sum));
				dsIncType.maskDataChange(false);
				// 定位回本节点
				dsIncType.gotoBookmark(node.getBookmark());
			}
		}
		// 得到对应的支出资金来源编码
		String sPfsCode = null;
		if (dsIncType.fieldByName(IIncType.IS_INC).getInteger() == 2) {
			List lstPfsCode = SysUntPub.getLeafNodeCode(
					incType.fpnlPfs.ftreePfs, incType.fpnlPfs.ftreePfs
							.getDataSet(), IPayOutFS.PFS_CODE);
			if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
				sPfsCode = lstPfsCode.get(0).toString();
			}
		}
		// 删除本节点
		dsIncType.delete();
		// 提交数据库
		sysIaeStruServ.delIncType(dsIncType, incTypeObj.inctype_code,
				sParIncTypeCode, String.valueOf(incTypeObj.set_year),
				incTypeObj.inctype_name);

		dsIncType.applyUpdate();
		return true;
	}
}
