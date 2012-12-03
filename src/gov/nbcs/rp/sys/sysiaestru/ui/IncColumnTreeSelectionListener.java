/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入栏目树SelectionListerner
 * </p>
 * <p>
 * Description:收入栏目树SelectionListerner
 * </p>
 *
 */
public class IncColumnTreeSelectionListener implements TreeSelectionListener {
	// 收入栏目管理客户端主界面类
	private IncColumn incColumn = null;

	// 收入栏目DataSet
	private DataSet dsIncCol = null;

	// 收入栏目对应收费项目DataSet
	private DataSet dsInccolumnToToll = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param incColumn
	 *            收入栏目管理客户端主界面类
	 */
	public IncColumnTreeSelectionListener(IncColumn incColumn) {
		this.incColumn = incColumn;
		this.dsIncCol = incColumn.dsIncCol;
		// 定义数据库接口
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * 调用方法
	 */
	public void valueChanged(TreeSelectionEvent e) {
		try {
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(
					incColumn.dsIncCol, incColumn, incColumn.ftreeIncColumn);
			setActionStatus.setState(true, true);
			if (dsIncCol.isEmpty() || dsIncCol.eof() || dsIncCol.bof())
				return;

			if (dsIncCol.fieldByName("INCCOL_CODE").getValue() != null)
				incColumn.ftxtPriCode.setValue(dsIncCol.fieldByName(
						"INCCOL_CODE").getString());
			else
				incColumn.ftxtPriCode.setValue("");
			if (dsIncCol.fieldByName("LVL_ID").getValue() != null)
				incColumn.ftxtfIncColCode.setValue(dsIncCol.fieldByName(
						"LVL_ID").getString());
			else
				incColumn.ftxtfIncColCode.setValue("");
			if (dsIncCol.fieldByName("INCCOL_NAME").getValue() != null)
				incColumn.ftxtfIncColName.setValue(dsIncCol.fieldByName(
						"INCCOL_NAME").getString());
			else
				incColumn.ftxtfIncColName.setValue("");
			// 该栏目需纵向求和
			if (dsIncCol.fieldByName("SUM_FLAG").getValue() != null) {
				((JCheckBox) incColumn.fchkSumFlag.getEditor())
						.setSelected(Common.estimate(new Integer(dsIncCol
								.fieldByName("SUM_FLAG").getInteger())));
			}
			// 该栏目隐藏
			if (dsIncCol.fieldByName("IS_HIDE").getValue() != null) {
				((JCheckBox) incColumn.fchkHideFlag.getEditor())
						.setSelected(Common.estimate(new Integer(dsIncCol
								.fieldByName("IS_HIDE").getInteger())));
			}
			//该栏目为预留比例
			if (dsIncCol.fieldByName("N2").getValue() != null) {
				((JCheckBox) incColumn.fchkRPFlag.getEditor())
						.setSelected(Common.estimate(new Integer(dsIncCol
								.fieldByName("N2").getInteger())));
			}
			// 数据来源mod by CL ,09,08,24
			if (dsIncCol.fieldByName("DATA_SOURCE").getValue() != null) {
				if(dsIncCol.fieldByName("DATA_SOURCE").getInteger() == 0)
				incColumn.frdoIncColDts.setValue(new String(dsIncCol
						.fieldByName("DATA_SOURCE").getString()));
				else
				{incColumn.frdoIncColDts.setValue(new String("1").toString());
				// incColumn.DSFlag = true;
				}
			}
			// 显示格式
			incColumn.fcbxSFormate.setValue(dsIncCol.fieldByName(
					"DISPLAY_FORMAT").getString());

			// 计算公式
			if (dsIncCol.fieldByName("FORMULA_DET").getString() != "") {
				incColumn.ftxtaIncColFormula
						.setValue(PubInterfaceStub.getMethod()
								.replaceTextEx(
										dsIncCol.fieldByName("FORMULA_DET")
												.getString(), 0,
										"fb_iae_inccolumn", "IncCol_Ename",
										"IncCol_Fname",
										"set_year =" + Global.loginYear));
			} else {
				incColumn.ftxtaIncColFormula.setValue("");
			}
			// 计算优先级
			if (dsIncCol.fieldByName("CALC_PRI").getValue() != null)
				incColumn.jspIncColCalcPRI.setValue(new Integer(dsIncCol
						.fieldByName("CALC_PRI").getInteger()));

			// 根据数据来源，显示信息   
			if (dsIncCol.fieldByName("DATA_SOURCE") != null) {// 录入
			//if (dsIncCol.fieldByName("DATA_SOURCE").getInteger() == 0) {// 录入
				// 得到收入栏目对应收费项目信息
				dsInccolumnToToll = sysIaeStruServ.getInccolumnToToll(
						Global.loginYear, dsIncCol.fieldByName("INCCOL_CODE")
								.getValue().toString());
				// 根据收入栏目与收费项目对应关系设置收费项目节点选中状态
				SetSelectTree.setIsCheck(incColumn.ftreIncomeSubItem,
						dsInccolumnToToll, "toll_code");
			} else {
				SetSelectTree.setIsNoCheck(incColumn.ftreIncomeSubItem);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(incColumn, "显示收入栏目明细信息发生错误，错误信息："
					+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

}
