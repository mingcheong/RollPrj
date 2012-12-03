/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出资金来源增加操作类
 * </p>
 * <p>
 * Description:支出资金来源增加操作类
 * </p>

 */
public class PayOutFSAdd {
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
	public PayOutFSAdd(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		this.payOutFsObj = payOutFS.payOutFsObj;
		// 定义数据库接口
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * 增加支出资金来源操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {
		// 取得支出预算表共个多少个列
		int MaxFieldNum = sysIaeStruServ.getPayOutFsIValue(Integer
				.parseInt(Global.loginYear));
		// 取得支出预算表空的字段名称
		String sEname = PubInterfaceStub.getMethod().assignNewCol("PFS_ENAME",
				"F", MaxFieldNum, "fb_iae_payout_fundsource", Global.loginYear,
				"");
		if ("".equals(sEname)) {
			// 分配失败，提示错误信息
			JOptionPane.showMessageDialog(payOutFS, "支出资金来源数目已到最大设置范围。", "提示",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// 获得自动生成的编码
		String sPfsCode = PubInterfaceStub.getMethod().getMaxCode(
				"fb_iae_payout_fundsource", "PFS_CODE",
				"set_Year = " + Global.loginYear, ISysIaeStru.iCodeLen);
		// 获得自动生成的层次码
		String sLvlIdCode;
		if ("".equals(payOutFsObj.lvl_id)) {
			if (payOutFS.dsPayOutFS.isEmpty())
				sLvlIdCode = ISysIaeStru.ROOT_CODE;
			else
				sLvlIdCode = PubInterfaceStub.getMethod().getNodeID(
						"fb_iae_payout_fundsource",
						"LVL_ID",
						payOutFsObj.lvl_id,
						"set_Year = " + Global.loginYear
								+ " and par_id is null", payOutFS.lvlIdRule);
		} else {
			sLvlIdCode = PubInterfaceStub.getMethod().getNodeID(
					"fb_iae_payout_fundsource",
					"LVL_ID",
					payOutFsObj.lvl_id,
					"set_Year = " + Global.loginYear + " and par_id ='"
							+ payOutFsObj.lvl_id + "'", payOutFS.lvlIdRule);
		}
		// 判断编码是否获得成功
		if (sPfsCode == null) {
			JOptionPane.showMessageDialog(payOutFS, "无法增加下一层次代码，当前代码已是最大级！",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 判断编码长度填写是"9999"
		int iLevel = payOutFS.lvlIdRule.levelOf(sLvlIdCode); // 获得当前编码节次
		if (iLevel < 0) {
			JOptionPane.showMessageDialog(payOutFS, "无法增加，已到最大代码!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 判断增加，是不是增加第一个叶节子，如果增加第一个叶节点，将父节点的信息传给子节点
		if (payOutFsObj.end_flag == 1)
			payOutFS.sSaveType = "addfirstson";
		else
			payOutFS.sSaveType = "add";

		payOutFS.dsPayOutFS.append();
		// 判断树中选中的节点是否是叶节点,如是是叶节点，将他的信息传给第一个叶节点
		payOutFS.dsPayOutFS.maskDataChange(true);
		payOutFS.ftxtPriCode.setValue(sPfsCode);// 内码
		payOutFS.ftxtfPfsCode.setValue(sLvlIdCode);// 层次编码
		payOutFS.ftxtfPfsName.setValue("");// 名称
		if ("addfirstson".equals(payOutFS.sSaveType)) {// 增加第一个子节点
			// 英文名称
			payOutFS.dsPayOutFS.fieldByName("PFS_ENAME").setValue(
					payOutFsObj.pfs_ename);
		} else {// 增加子节点
			// 性质
			payOutFS.flstPfsKind.setSelectedIndex(0);
			// 参与控制数分配
			((JCheckBox) payOutFS.fchkPfsFlag.getEditor()).setSelected(false);
			// 默认隐藏
			((JCheckBox) payOutFS.fchkHide.getEditor()).setSelected(false);
			// 收支平衡
			((JCheckBox) payOutFS.fchkIsBalance.getEditor()).setSelected(true);
			// 支持项目情况
			payOutFS.frdoSupPrj.setValue("0");
			// 数据来源程序
			payOutFS.frdoIncColDts.setValue("0");
			// 显示格式
			payOutFS.fcbxSFormate.setValue("");
			// 编辑格式
			// payOutFS.fcbxEFormate.setValue("");
			// 计算公式
			payOutFS.ftxtaPfsFormula.setValue("");
			// 计算优先级
			payOutFS.jspPfsCalcPRI.setValue(new Integer(0));
			// 英文名称
			payOutFS.dsPayOutFS.fieldByName("PFS_ENAME").setValue(sEname);
		}
		payOutFS.dsPayOutFS.fieldByName("PFS_CODE").setValue(sPfsCode);
		payOutFS.dsPayOutFS.fieldByName("END_FLAG").setValue(new Integer(1));
		payOutFS.dsPayOutFS.fieldByName("set_Year").setValue(Global.loginYear);
		payOutFS.dsPayOutFS.fieldByName("RG_CODE").setValue(
				Global.getCurrRegion());
		payOutFS.dsPayOutFS.maskDataChange(false);
		payOutFS.ftxtfPfsName.setFocus();
		return true;
	}
}
