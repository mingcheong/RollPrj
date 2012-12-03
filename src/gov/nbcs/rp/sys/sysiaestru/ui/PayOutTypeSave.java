/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出项目类别保存操作类
 * </p>
 * <p>
 * Description:支出项目类别保存操作类

 */
public class PayOutTypeSave {
	private PayOutType payOutType = null;

	private DataSet dsPayOutType = null;

	private DataSet dsAcctJJ = null;

	private PayOutTypeObj payOutTypeObj = null;

	private ISysIaeStru sysIaeStruServ = null;

	private String sSaveType = null;

	private MyTreeNode myTreeNode[] = null;

	private CustomTree ftreeAcctJJ = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutType
	 *            支出项目类别管理客户端主界面类
	 */
	public PayOutTypeSave(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.dsPayOutType = payOutType.dsPayOutType;
		this.payOutTypeObj = payOutType.payOutTypeObj;
		this.sSaveType = payOutType.sSaveType;
		this.ftreeAcctJJ = payOutType.ftreeAcctJJ;
		this.dsAcctJJ = payOutType.dsAcctJJ;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	/**
	 * 保存支出项目类别操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void save() throws Exception {
		boolean bRefresh = false;
		// modify by qzc 2009,6,4
		myTreeNode = ftreeAcctJJ.getSelectedNodes(true);
		// 判断信息填写是否完整
		if (!judgeFillInfo())
			return;
		String slvlId = payOutType.ftxtfPayOutTypeCode.getValue().toString();
		String sPayOutTypeName = payOutType.ftxtfPayOutTypeName.getValue()
				.toString();
		String sPayoutKindCode = dsPayOutType.fieldByName(
				IPayOutType.PAYOUT_KIND_CODE).getString();

		String sParCode = "";
		String sParId = null;
		dsPayOutType.maskDataChange(true);
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))// 增加
			sParCode = payOutTypeObj.payout_kind_code;
		else {// 修改
			sParId = payOutType.lvlIdRule.previous(slvlId);
			if (sParId == null) {
				sParId = "";
			}

			ReplaceUnt replaceUnt = new ReplaceUnt();
			List lstValue = replaceUnt.getParNodeInfo(sParId,
					new String[] { "PAYOUT_KIND_CODE" }, dsPayOutType);
			if (lstValue != null)
				sParCode = lstValue.get(0).toString();

			if (!slvlId.equals(payOutTypeObj.lvl_id)) {// 节点编码发生改变
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(payOutType.ftreePayOutType);
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						payOutType.ftreePayOutType, dsPayOutType);
				replaceUnt.ReplaceLvlPar(lstBookmark, dsPayOutType, slvlId,
						payOutTypeObj.lvl_id, payOutType.lvlIdRule);
				replaceUnt.delParNode(lstDelBookmark, dsPayOutType);
				bRefresh = true;
			}
		}

		if ("modformate".equals(sSaveType)) {
			// 控制数分配到明细
			dsPayOutType.fieldByName("N1").setValue(
					new Integer("false".equals(payOutType.fchkPayOutFlag
							.getValue().toString()) ? 0 : 1));
		}

		if ("modname".equals(sSaveType)) {
			// 名称
			dsPayOutType.fieldByName(IPayOutType.PAYOUT_KIND_NAME).setValue(
					payOutType.ftxtfPayOutTypeName.getValue().toString());
		}

		// add,addfirstson,mod,将值传给dataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)) {
			// 名称
			dsPayOutType.fieldByName(IPayOutType.PAYOUT_KIND_NAME).setValue(
					payOutType.ftxtfPayOutTypeName.getValue().toString());
			// 性质
			dsPayOutType.fieldByName(IPayOutType.STD_TYPE_CODE).setValue(
					payOutType.flstPayOutTypeKind.getSelectedElement().getId());
			// 控制数分配到明细
			dsPayOutType.fieldByName("N1").setValue(
					new Integer("false".equals(payOutType.fchkPayOutFlag
							.getValue().toString()) ? 0 : 1));
		}
		// 如增加的是第一个子节点，修改父节点部分信息
		if ("addfirstson".equals(sSaveType)) {
			String sBookmark = dsPayOutType.toogleBookmark();
			MyTreeNode node = payOutType.ftreePayOutType
					.getSelectedNode();
			MyTreeNode parentNode = (MyTreeNode) node.getParent();
			if (parentNode != null) {
				dsPayOutType.gotoBookmark(node.getBookmark());
				dsPayOutType.fieldByName(IIncType.STD_TYPE_CODE).setValue("");
				dsPayOutType.fieldByName("end_flag").setValue(new Integer(0));
			}
			// 定位回本节点
			dsPayOutType.gotoBookmark(sBookmark);
		}

		DataSet dsPayoutKindToJj = null;
		if ("addfirstson".equals(sSaveType) || "mod".equals(sSaveType)
				|| "add".equals(sSaveType) || "modformate".equals(sSaveType)) {
			dsPayoutKindToJj = DataSet.create();
			// 保存对应经济科目信息
			dsPayoutKindToJj.clearAll();
			if (myTreeNode != null) {
				for (int i = 0; i < myTreeNode.length; i++) {
					dsAcctJJ.gotoBookmark(myTreeNode[i].getBookmark());
					String sBSI_ID = dsAcctJJ.fieldByName(IPubInterface.BSI_ID)
							.getString();
					String sAcctCodeJj = dsAcctJJ.fieldByName(
							IPubInterface.ACCT_CODE_JJ).getString();
					String sAcctNameJj = dsAcctJJ.fieldByName(
							IPubInterface.ACCT_NAME_JJ).getString();
					dsPayoutKindToJj.append();
					dsPayoutKindToJj.fieldByName("PAYOUT_KIND_CODE").setValue(
							sPayoutKindCode);
					dsPayoutKindToJj.fieldByName("PAYOUT_KIND_NAME").setValue(
							sPayOutTypeName);
					dsPayoutKindToJj.fieldByName("BSI_ID").setValue(sBSI_ID);
					dsPayoutKindToJj.fieldByName("ACCT_CODE_JJ").setValue(
							sAcctCodeJj);
					dsPayoutKindToJj.fieldByName("ACCT_NAME_JJ").setValue(
							sAcctNameJj);
					dsPayoutKindToJj.fieldByName("SET_YEAR").setValue(
							Global.loginYear);
					dsPayoutKindToJj.fieldByName("RG_CODE").setValue(
							Global.getCurrRegion());
				}
			}
		}
		dsPayOutType.maskDataChange(false);
		dsPayOutType.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsPayOutType.fieldByName("par_id").setValue(payOutTypeObj.lvl_id);
		else
			dsPayOutType.fieldByName("par_id").setValue(sParId);
		dsPayOutType.fieldByName("name").setValue(
				slvlId + " " + sPayOutTypeName);
		// 提交数据
		sysIaeStruServ.savePayOutKind(dsPayOutType, dsPayoutKindToJj,
				sSaveType, sPayoutKindCode, sPayOutTypeName, Global.loginYear,
				sParCode);
		dsPayOutType.applyUpdate();
		payOutType.setChangeFlag(true);
		// add状态定位到增加的节点
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			payOutType.ftreePayOutType.expandTo("lvl_id", slvlId);
		if (bRefresh) { // 刷新树
			payOutType.ftreePayOutType.reset();
			payOutType.ftreePayOutType.expandTo("lvl_id", slvlId);
		}

	}

	/**
	 * 判断填写信息是否有问题
	 * 
	 * @throws Exception
	 * @throws HeadlessException
	 * 
	 * @return，true填写没有问题，false,填写有问题
	 */
	private boolean judgeFillInfo() throws HeadlessException, Exception {
		String sLvlId = payOutType.ftxtfPayOutTypeCode.getValue().toString();
		String sPayOutKindName = payOutType.ftxtfPayOutTypeName.getValue()
				.toString().trim();
		String sParId = payOutType.lvlIdRule.previous(sLvlId); // 获得上级编码
		if ("mod".equals(sSaveType.substring(0, 3))) {
			// 判断编码是否为空
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(payOutType, "编码不能为空!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				payOutType.ftxtfPayOutTypeCode.setFocus();
				return false;
			}

			// 编码被修改，要判断编码
			if (sLvlId != payOutTypeObj.lvl_id) {
				// 判断编码填写的是否是数字
				if (!sLvlId.matches("\\d+")) {
					JOptionPane.showMessageDialog(payOutType, "编码必须是数字，请重新填写！",
							"提示", JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
				// 判断编码长度填写是否正确,修改的情况要判断
				int iLevel = payOutType.lvlIdRule.levelOf(sLvlId); // 获得当前编码节次
				int iCount = payOutType.lvlIdRule.originRules().size();
				if (iLevel < 0) {
					JOptionPane.showMessageDialog(payOutType,
							"编码不正确，编码需四位一节且不超过节次长度" + String.valueOf(iCount)
									+ "节 ，请重新填写!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
				// 修改编码父编码是否存在,且不是不叶子节点，如果是叶子节点，不可修改
				String sPar = payOutType.lvlIdRule.previous(sLvlId);// 获得父对象编码
				if (!"".equals(sPar) && sPar != null) {
					InfoPackage infoPackage = sysIaeStruServ
							.judgePayOutTypeParExist(sPar, Global.loginYear);
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(payOutType, infoPackage
								.getsMessage(), "提示",
								JOptionPane.INFORMATION_MESSAGE);
						payOutType.ftxtfPayOutTypeCode.setFocus();
						return false;
					}
				}
				// 判断编码是否重复,修改情况判断，增加因为编码是自动生成且不可修改的
				if (!sysIaeStruServ.judgePayOutTypeCodeRepeat(sLvlId,
						Global.loginYear, payOutTypeObj.payout_kind_code, true)) {
					JOptionPane.showMessageDialog(payOutType, "编码已经被使用!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
				// 判断节点不能直接改为下级节点，造成数不完整
				ReplaceUnt replaceUnt = new ReplaceUnt();
				if (!replaceUnt.checkCode(sLvlId, dsPayOutType.fieldByName(
						"lvl_id").getOldValue().toString(),
						payOutType.lvlIdRule)) {
					JOptionPane.showMessageDialog(payOutType,
							"不能将节点修改成自己的下级节点,请重新填写编码!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
			}
		}
		// 判断名称是否填写
		if ("".equals(sPayOutKindName)) {
			JOptionPane.showMessageDialog(payOutType, "收入栏目名称不能为空!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			payOutType.ftxtfPayOutTypeCode.setFocus();
			return false;
		}
		// 判断同级名称是否重复
		boolean bFlag;
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
			bFlag = sysIaeStruServ.judgePayOutTypeNameRepeat(sPayOutKindName,
					sParId, Global.loginYear, null, false);
		} else {
			bFlag = sysIaeStruServ.judgePayOutTypeNameRepeat(sPayOutKindName,
					sParId, Global.loginYear, payOutTypeObj.payout_kind_code,
					true);
		}
		if (!bFlag) {
			JOptionPane.showMessageDialog(payOutType, "资金来源名称已经被使用!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			payOutType.ftxtfPayOutTypeName.setFocus();
			return false;
		}

		// 判断对应的经济科目,叶节点需要判断
		if (dsPayOutType.fieldByName("end_flag").getInteger() == 1
				&& !IPayOutType.PAYOUTKINDSTAND_PRJ
						.equals(payOutType.flstPayOutTypeKind
								.getSelectedElement().getId())) {
			// 判断对应的经济科目不能为空
			myTreeNode = ftreeAcctJJ.getSelectedNodes(true);
			if (myTreeNode.length == 0) {
				JOptionPane.showMessageDialog(payOutType, "对应的经济科目不能为空!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				payOutType.ftxtfPayOutTypeName.setFocus();
				return false;
			}
		}
		// 判断支出项目类别性质-专项支出只允许选择一次
		String stdTypeCode = payOutType.flstPayOutTypeKind.getSelectedElement()
				.getId();
		if (IPayOutType.PAYOUTKINDSTAND_PRJ.equals(stdTypeCode)) {
			String payoutKindName = sysIaeStruServ
					.getPayoutKindStandPrj(payOutType.ftxtPriCode.getValue()
							.toString());
			if (!Common.isNullStr(payoutKindName)) {
				JOptionPane.showMessageDialog(payOutType, "专项支出只可被\""
						+ payoutKindName
						+ "\"支出项目类别使用,专项支出只可以被一个支出项目类别使用，请选择其他性质!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}

		return true;
	}
}
