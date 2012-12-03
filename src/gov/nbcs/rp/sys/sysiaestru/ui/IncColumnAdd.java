/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入栏目增加操作类
 * </p>
 * <p>
 * Description:收入栏目增加操作类
 * </p>

 */
public class IncColumnAdd {
	// 收入栏目管理客户端主界面类
	private IncColumn incColumn = null;

	// 收入栏目对象
	private IncColumnObj incColumnObj = null;

	// 收入栏目DataSet
	private DataSet dsIncCol = null;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	// 公共方法数据接口
	private IPubInterface iPubInterface = null;

	/**
	 * 构造函数
	 * 
	 * @param incColumn
	 *            收入栏目管理客户端主界面类
	 */
	public IncColumnAdd(IncColumn incColumn) {
		// 定义入栏目管理客户端主界面类
		this.incColumn = incColumn;
		// 定义收入栏目对象
		this.incColumnObj = incColumn.incColumnObj;
		// 定义收入栏目DataSet
		this.dsIncCol = incColumn.dsIncCol;
		// 定义数据库接口
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
		// 公共方法数据接口
		this.iPubInterface = incColumn.iPubInterface;
	}

	/**
	 * 增加收入栏目操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {

		// 取得收入预算表共个多少个收入栏目列
		int MaxFieldNum = sysIaeStruServ.getIncColIValue(Global.loginYear);

		// 取得收入预算表空的字段名称
		String sEname = iPubInterface.assignNewCol("IncCol_EName", "F",
				MaxFieldNum, "fb_iae_inccolumn", Global.loginYear, "");
		if ("".equals(sEname)) {
			// 分配失败，提示错误信息
			JOptionPane.showMessageDialog(incColumn, "收入栏目数目超出设置范围。", "提示",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// 获得自动生成的编码
		String sInccolCode = iPubInterface.getMaxCode("fb_iae_inccolumn",
				"IncCol_CODE", "set_Year = " + Global.loginYear,
				ISysIaeStru.iCodeLen);
		// 判断编码是否获得成功
		if (sInccolCode == null) {
			JOptionPane.showMessageDialog(incColumn, "获得内码有误，增加失败！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 获得自动生成的层次码
		String sLvlIdCode;
		// 判断父点的层次码是否为空
		if ("".equals(incColumnObj.LVL_ID)) {// 父点的层次码为空
			// 判断收入栏目DataSet是否为空(即是否增加的第一个收入栏目节点）
			if (dsIncCol.isEmpty()) {// 收入栏目DataSet为空
				// 设置层次码
				sLvlIdCode = ISysIaeStru.ROOT_CODE;
			} else {// 收入栏目DataSet为空,设置层次码
				// 根据规则在对应的数据表中获取新的节点code
				sLvlIdCode = iPubInterface.getNodeID("fb_iae_inccolumn",
						"LVL_ID", incColumnObj.LVL_ID, "set_Year = "
								+ Global.loginYear + " and par_id is null",
						incColumn.lvlIdRule);
			}
		} else {// 父点的层次码不为空
			// 根据规则在对应的数据表中获取新的节点code
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_inccolumn", "LVL_ID",
					incColumnObj.LVL_ID, "set_Year = " + Global.loginYear
							+ " and par_id ='" + incColumnObj.LVL_ID + "'",
					incColumn.lvlIdRule);
		}
		// 判断层次码是否获得成功
		if (sLvlIdCode == null) {
			JOptionPane.showMessageDialog(incColumn, "无法增加下一级代码，已到最大可增加级次！",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 判断编码长度填写是"9999"
		int iLevel = incColumn.lvlIdRule.levelOf(sLvlIdCode); // 获得当前编码节次
		if (iLevel < 0) {
			JOptionPane.showMessageDialog(incColumn, "无法增加，已到最大代码!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 判断增加，是不是增加第一个叶节子，如果增加第一个叶节点，将父节点的信息传给子节点
		if (incColumnObj.END_FLAG == 1)
			incColumn.sSaveType = "addfirstson";
		else
			incColumn.sSaveType = "add";
		dsIncCol.append();
		// 判断树中选中的节点是否是叶节点,如是是叶节点，将他的信息传给第一个叶节点
		dsIncCol.maskDataChange(true);
		incColumn.ftxtPriCode.setValue(sInccolCode); // 内码
		incColumn.ftxtfIncColCode.setValue(sLvlIdCode);// 层次编码
		incColumn.ftxtfIncColName.setValue("");// 名称
		if ("addfirstson".equals(incColumn.sSaveType)) {// 增加第一个子节点
			// 英文名称
			dsIncCol.fieldByName("inccol_ename").setValue(
					incColumnObj.INCCOL_ENAME);
		} else {// 增加子节点
			((JCheckBox) incColumn.fchkSumFlag.getEditor()).setSelected(false);
			((JCheckBox) incColumn.fchkHideFlag.getEditor()).setSelected(false);
			//mod by CL ,09,08,24
			((JCheckBox) incColumn.fchkRPFlag.getEditor()).setSelected(false);
			incColumn.frdoIncColDts.setValue("0");
			// 显示格式
			incColumn.fcbxSFormate.setValue("");
			// 计算公式
			incColumn.ftxtaIncColFormula.setValue("");
			// 计算优先级
			incColumn.jspIncColCalcPRI.setValue(new Integer(0));
			// 英文名称
			dsIncCol.fieldByName("INCCOL_ENAME").setValue(sEname);
			SetSelectTree.setIsNoCheck(incColumn.ftreIncomeSubItem);
		}

		dsIncCol.fieldByName("INCCOL_CODE").setValue(sInccolCode);
		dsIncCol.fieldByName("END_FLAG").setValue(new Integer(1));
		dsIncCol.fieldByName("set_Year").setValue(Global.loginYear);
		dsIncCol.fieldByName("RG_CODE").setValue(Global.getCurrRegion());
		dsIncCol.maskDataChange(false);
		incColumn.ftxtfIncColName.setFocus();
		return true;
	}
}
