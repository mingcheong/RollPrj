/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出项目类别增加操作类
 * </p>
 * <p>
 * Description:支出项目类别增加操作类
 * </p>
 * <p>

 */
public class PayOutTypeAdd {
	// 支出项目类别管理客户端主界面类
	private PayOutType payOutType = null;

	// 支出项目类别对象
	private PayOutTypeObj payOutTypeObj = null;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	// 公共方法数据接口
	private IPubInterface iPubInterface = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutType
	 *            支出项目类别管理客户端主界面类
	 */
	public PayOutTypeAdd(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.payOutTypeObj = payOutType.payOutTypeObj;
		this.iPubInterface = payOutType.iPubInterface;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	/**
	 * 增加支出项目类别操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {
		// 判断增加，是不是增加第一个叶节子，如果增加第一个叶节点，将父节点的部分信息传给子节点
		if (payOutTypeObj.end_flag == 1)
			payOutType.sSaveType = "addfirstson";
		else
			payOutType.sSaveType = "add";

		// 判断在节点下增加第一个子节点，如果节点已被使用，不能增加子节点
		if ("addfirstson".equals(payOutType.sSaveType)) {
			InfoPackage infoPackage = sysIaeStruServ.judgePayOutTypeUse(
					payOutTypeObj.payout_kind_code, Global.loginYear);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(payOutType, infoPackage
						.getsMessage()
						+ ",不能增加子节点!", "提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}

		// 获得自动生成的编码
		String sPayOutCode = iPubInterface.getMaxCode("fb_iae_payout_kind",
				"PAYOUT_KIND_CODE", "set_Year = " + Global.loginYear,
				ISysIaeStru.iCodeLen);

		// 判断编码是否获得成功
		if (sPayOutCode == null) {
			JOptionPane.showMessageDialog(payOutType, "获得编码有误，增加失败！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 获得自动生成的层次码
		String sLvlIdCode;
		if ("".equals(payOutTypeObj.lvl_id)) {
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_payout_kind",
					"LVL_ID", payOutTypeObj.lvl_id, "set_Year = "
							+ Global.loginYear + " and par_id is null",
					payOutType.lvlIdRule);
		} else {
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_payout_kind",
					"LVL_ID", payOutTypeObj.lvl_id, "set_Year = "
							+ Global.loginYear + " and par_id ='"
							+ payOutTypeObj.lvl_id + "'", payOutType.lvlIdRule);
		}
		// 判断编码是否获得成功
		if (sLvlIdCode == null) {
			JOptionPane.showMessageDialog(payOutType, "无法增加下一层次代码，已到最大可增加级次！",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		payOutType.dsPayOutType.append();
		// 判断树中选中的节点是否是叶节点,如是是叶节点，将他的信息传给第一个叶节点
		payOutType.dsPayOutType.maskDataChange(true);
		payOutType.ftxtPriCode.setValue(sPayOutCode);// 内码
		payOutType.ftxtfPayOutTypeCode.setValue(sLvlIdCode);// 层次编码
		payOutType.ftxtfPayOutTypeName.setValue("");// 名称
		if ("addfirstson".equals(payOutType.sSaveType)) {// 增加第一个子节点

		} else {// 增加子节点
			// 性质
			payOutType.flstPayOutTypeKind.setSelectedIndex(0);
			SetSelectTree.setIsNoCheck(payOutType.ftreeAcctJJ);
		}
		payOutType.dsPayOutType.fieldByName(IPayOutType.PAYOUT_KIND_CODE)
				.setValue(sPayOutCode);
		payOutType.dsPayOutType.fieldByName("END_FLAG")
				.setValue(new Integer(1));
		payOutType.dsPayOutType.fieldByName("set_Year").setValue(
				Global.loginYear);
		payOutType.dsPayOutType.fieldByName("RG_CODE").setValue(
				Global.getCurrRegion());
		payOutType.dsPayOutType.maskDataChange(false);
		payOutType.ftxtfPayOutTypeCode.setFocus();
		return true;
	}
}
