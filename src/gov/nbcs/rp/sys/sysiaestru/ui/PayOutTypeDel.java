/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出项目类别删除操作类
 * </p>
 * <p>
 * Description:支出项目类别删除操作类
 * </p>
 * <p>

 */
public class PayOutTypeDel {
	// 支出项目类别管理客户端主界面类
	private PayOutType payOutType = null;

	// 支出项目类别对象
	private PayOutTypeObj payOutTypeObj = null;

	// 支出项目类别数据集
	private DataSet dsPayOutType = null;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutType
	 *            支出项目类别管理客户端主界面类
	 */
	public PayOutTypeDel(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.dsPayOutType = payOutType.dsPayOutType;
		this.payOutTypeObj = payOutType.payOutTypeObj;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	/**
	 * 删除支出项目类别操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean delete() throws HeadlessException, Exception {
		String sParCode = ""; // 存放父节点编号
		String sParName = ""; // 存放父节点名称
		String sParParID = "";// 存放父节点ParID
		// 判断是否是末节点，不是末节点，不允许删除
		if (dsPayOutType.fieldByName("End_flag").getValue() != null
				&& dsPayOutType.fieldByName("End_flag").getInteger() == 0) {
			JOptionPane.showMessageDialog(payOutType, "支出项目类别存在子栏目,请先删除子栏目!",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断是否被使用
		InfoPackage infoPackage = sysIaeStruServ.judgePayOutTypeUse(
				payOutTypeObj.payout_kind_code, Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(payOutType, infoPackage.getsMessage()
					+ ",不能删除!", "提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断是否已设置公式和公式与单位对应关系
		infoPackage = sysIaeStruServ
				.judgePayoutTypeFormulaUse(payOutTypeObj.payout_kind_code);
		if (!infoPackage.getSuccess()) {
			if (JOptionPane.showConfirmDialog(payOutType, infoPackage
					.getsMessage()
					+ ",删除该条记录将同时删除"
					+ infoPackage.getsMessage()
					+ "，无法恢复!\n您是否确认要删除该条记录?", "提示", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return false;
		} else {
			// 提示是否确定删除
			if (JOptionPane.showConfirmDialog(payOutType, "您是否确认要删除该条记录?",
					"提示", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return false;
		}
		// 判断选中的节点的父节点是否存在
		MyTreeNode node = payOutType.ftreePayOutType
				.getSelectedNode();
		MyTreeNode parentNode = (MyTreeNode) node.getParent();
		if (parentNode != (MyTreeNode) payOutType.ftreePayOutType.getRoot()) {
			// 判断有几个兄弟，如果只有一个节点，将本节的部分信息传给父节点,做一些更改操作
			if (parentNode.getChildCount() == 1) {
				// 定位到父节点
				dsPayOutType.gotoBookmark(parentNode.getBookmark());
				sParCode = dsPayOutType.fieldByName(
						IPayOutType.PAYOUT_KIND_CODE).getString();
				sParName = dsPayOutType.fieldByName(
						IPayOutType.PAYOUT_KIND_NAME).getString();
				// 将部分信息
				dsPayOutType.maskDataChange(true);
				dsPayOutType.edit();
				dsPayOutType.fieldByName("std_type_code").setValue(
						payOutTypeObj.std_type_code);
				dsPayOutType.fieldByName("end_flag").setValue(
						new Integer(payOutTypeObj.end_flag));
				dsPayOutType.maskDataChange(false);
				if (parentNode.getParent() != payOutType.ftreePayOutType
						.getRoot()) {
					dsPayOutType.gotoBookmark(((MyTreeNode) parentNode
							.getParent()).getBookmark());
					sParParID = dsPayOutType.fieldByName(
							IPayOutType.PAYOUT_KIND_CODE).getString();
				} else
					sParParID = "";
				// 定位回本节点
				dsPayOutType.gotoBookmark(node.getBookmark());

			}
		}
		// 删除本节点
		dsPayOutType.delete();
		// 提交数据库
		sysIaeStruServ.delPayOutKind(dsPayOutType,
				payOutTypeObj.payout_kind_code, sParCode, sParName, sParParID,
				String.valueOf(payOutTypeObj.set_year),
				payOutTypeObj.payout_kind_name);
		dsPayOutType.applyUpdate();
		payOutType.setChangeFlag(true);

		return true;

	}
}
