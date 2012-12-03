/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入项目类别树SelectionListerner
 * </p>
 * <p>
 * Description:收入项目类别树SelectionListerner
 * </p>
 * <p>
 *
 */
public class IncTypeTreeSelectionListener implements TreeSelectionListener {
	// 收入项目类别管理客户端主界面类
	private IncType incType = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 */
	public IncTypeTreeSelectionListener(IncType incType) {
		this.incType = incType;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(
					incType.dsIncType, incType, incType.ftreeIncType);
			setActionStatus.setState(true, true);
			if (incType.dsIncType.isEmpty() || incType.dsIncType.bof()
					|| incType.dsIncType.eof())
				return;
			// 内码
			if (incType.dsIncType.fieldByName(IIncType.INCTYPE_CODE).getValue() != null)
				incType.ftxtPriCode.setValue(incType.dsIncType.fieldByName(
						IIncType.INCTYPE_CODE).getString());
			else
				incType.ftxtPriCode.setValue("");
			// 编码
			if (incType.dsIncType.fieldByName(IIncType.LVL_ID).getString() != "")
				incType.ftxtfIncTypeCode.setValue(incType.dsIncType
						.fieldByName(IIncType.LVL_ID).getString());
			// 名称
			incType.ftxtfIncTypeName.setValue(incType.dsIncType.fieldByName(
					IIncType.INCTYPE_NAME).getString());

			// 性质
			incType.flstIncTypeKind.setMaskValueChange(true);
			if ("".equals(incType.dsIncType.fieldByName(IIncType.STD_TYPE_CODE)
					.getString())) {
				incType.flstIncTypeKind.setSelectedIndex(0);
			} else
				incType.flstIncTypeKind.setSelectedValue(incType.dsIncType
						.fieldByName(IIncType.STD_TYPE_CODE).getString(), true);
			incType.flstIncTypeKind.setMaskValueChange(false);

			// 是否是其中数
			if (incType.dsIncType.fieldByName(IIncType.IS_SUM).getValue() == null) {
				((JCheckBox) incType.fchkIsMid.getEditor()).setSelected(false);
			} else {
				if (incType.dsIncType.fieldByName(IIncType.IS_SUM).getValue() != null) {
					((JCheckBox) incType.fchkIsMid.getEditor())
							.setSelected(!Common.estimate(new Integer(
									incType.dsIncType.fieldByName(
											IIncType.IS_SUM).getInteger())));
				}
			}

			// 数据来源
			// Object isInc = incType.dsIncType.fieldByName(IIncType.IS_INC)
			// .getValue();
			// if (isInc == null) {
			// isInc = "0";
			// }
			// 必须重新从库中获取，资金来源对收入可能修改，造成库与界面显示不一致
			String incTypeCode = incType.dsIncType.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			int isInc = sysIaeStruServ.getIncTypeIsInc(incTypeCode);
			incType.dsIncType.fieldByName(IIncType.IS_INC).setValue(
					String.valueOf(isInc));

			incType.frdoIsInc.setValue(String.valueOf(isInc));

			switch (isInc) {
			case 1: // 从非税表取数
				// 得到收入项目类别编码
				String sIncTypeCode = incType.dsIncType.fieldByName(
						IIncType.INCTYPE_CODE).getString();

				// 获得收入项目类别与收入栏目对应关系
				incType.dsInccolumnToInc = sysIaeStruServ.getInctypeToIncolumn(
						Global.loginYear, sIncTypeCode);
				// 组织成List
				List lstIncColCode = SysUntPub.getFieldValueOrgList(
						incType.dsInccolumnToInc, IIncColumn.INCCOL_CODE);
				// 根据INCCOL_CODE值得到相应的lvl_id值
				DataSet dsIncCol = incType.fpnlIncCol.ftreeIncColumn
						.getDataSet();
				String[] slvlArr = SysUntPub.getIdWithCode(lstIncColCode,
						dsIncCol, IIncColumn.INCCOL_CODE, IIncColumn.LVL_ID);

				// 显示收入栏目数节点选中状态
				SetSelectTree.setIsCheck(incType.fpnlIncCol.ftreeIncColumn,
						slvlArr);
				// 定位到第一个勾选节点
				if (slvlArr != null && slvlArr.length > 0) {
					TreePath path = new TreePath(
							incType.fpnlIncCol.ftreeIncColumn.getRoot());
					incType.fpnlIncCol.ftreeIncColumn.expandPath(path);
					incType.fpnlIncCol.ftreeIncColumn.getSelectionModel()
							.setSelectionPath(path);
					incType.fpnlIncCol.ftreeIncColumn.scrollPathToVisible(path);
					incType.fpnlIncCol.ftreeIncColumn.expandTo(
							IIncColumn.LVL_ID, slvlArr[0]);

				}
				break;
			case 2: // 从支出预算表取数
				showIncTypeToPfs(incType);
				break;
			default: // 录入
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(incType, "显示明细信息发生错误，错误信息："
					+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 显示收入项目与支出资金来源的对应关系
	 * 
	 * @param incType
	 * @throws Exception
	 */
	public static void showIncTypeToPfs(IncType incType) throws Exception {
		if (incType.dsIncType == null || incType.dsIncType.isEmpty()
				|| incType.dsIncType.bof() || incType.dsIncType.eof()) {// 显示支出资金来源节点选中状态
			SetSelectTree.setIsNoCheck(incType.fpnlPfs.ftreePfs);
		} else {
			// 得到收入项目类别编码
			String sIncTypeCode = incType.dsIncType.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			// 获得收入项目类别与支出资金来源对应关系
			DataSet dsPfsToIncitem = incType.sysIaeStruServ.getPfsToIncCode(
					sIncTypeCode, Global.loginYear);
			// 组织成List
			List lstPfsCode = SysUntPub.getFieldValueOrgList(dsPfsToIncitem,
					IPayOutFS.PFS_CODE);
			// 根据pfs_code值得到相应的lvl_id值
			DataSet dsPayOutFS = incType.fpnlPfs.ftreePfs.getDataSet();
			String[] slvlArr = SysUntPub.getIdWithCode(lstPfsCode, dsPayOutFS,
					IPayOutFS.PFS_CODE, IPayOutFS.LVL_ID);
			// 显示支出资金来源节点选中状态
			SetSelectTree.setIsCheck(incType.fpnlPfs.ftreePfs, slvlArr);
		}
	}

}
