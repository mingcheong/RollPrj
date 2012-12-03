/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入栏目保存操作类
 * </p>
 * <p>
 * Description:收入栏目保存操作类
 * </p>
 * <p>

 */
public class IncColumnSave {
	// 收入栏目管理客户端主界面类
	private IncColumn incColumn = null;

	// 收入栏目DataSet
	private DataSet dsIncCol = null;

	// 收费项目树
	private CustomTree ftreIncomeSubItem = null;

	// 收入栏目对象
	private IncColumnObj incColumnObj = null;

	// 保存类型
	private String sSaveType = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	public IncColumnSave(IncColumn incColumn) {

		this.incColumn = incColumn;
		this.dsIncCol = incColumn.dsIncCol;
		this.ftreIncomeSubItem = incColumn.ftreIncomeSubItem;
		this.incColumnObj = incColumn.incColumnObj;
		this.sSaveType = incColumn.sSaveType;
		// 定义数据库接口
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * 构造函数
	 * 
	 * @param incColumn
	 *            收入栏目管理客户端主界面类
	 */
	public void save() throws Exception {
		// 得到收费项目选中的节点编码
		ArrayList lstTollCode = null;

		// 是否增加的当前节点的第一个节点
		boolean bAddFirstNode = false;

		boolean bRefresh = false;
		// 判断信息填写是否完整
		if (!judgeFillInfo())
			return;

		// 层次码
		String slvlId = incColumn.ftxtfIncColCode.getValue().toString();
		// 收入栏目名称
		String sIncColName = incColumn.ftxtfIncColName.getValue().toString();
		// 判断编码是否修改,判断编号是否修改
		String sParId = null;

		dsIncCol.maskDataChange(true);
		// 判断保存类型是否是修改
		if ("mod".equals(sSaveType.substring(0, 3))) {
			// 得到当前填写层次码的父节编码
			sParId = incColumn.lvlIdRule.previous(slvlId);
			if (sParId == null) {
				sParId = "";
			}

			// 定义编码和全名操作类
			ReplaceUnt replaceUnt = new ReplaceUnt();
			String sNewFName = "";
			// 判断父编码是否为空
			if (!"".equals(sParId)) {// 父编码不为空
				// 根据父编码值得到父节点的信息
				List lstValue = replaceUnt.getParNodeInfo(sParId,
						new String[] { IIncColumn.INCCOL_FNAME }, dsIncCol);
				if (lstValue != null)
					sNewFName = lstValue.get(0).toString();
			}
			// 组织收入栏目全名的值
			if (sNewFName != null && !"".equals(sNewFName)) {
				sNewFName = sNewFName + UntPub.PARTITION_TAG + sIncColName;
			} else {
				sNewFName = sIncColName;
			}

			// 判断层次码是否发生改变
			if (!slvlId.equals(incColumnObj.LVL_ID)) {// 层次码发生改变
				// 得到选中树节点及其子节点的bookmark列表
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(incColumn.ftreeIncColumn);
				// 得到需删除的节点列表
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						incColumn.ftreeIncColumn, dsIncCol);

				// 层次码发生改变，收入栏目全名重新获得
				replaceUnt.ReplaceFname(lstBookmark, dsIncCol, sNewFName,
						dsIncCol.fieldByName("inccol_fname").getOldValue()
								.toString(), IIncColumn.INCCOL_FNAME);
				// 替换lvl_id,par_id值,改变子节点的lvl_id
				replaceUnt.ReplaceLvlPar(lstBookmark, dsIncCol, slvlId,
						incColumnObj.LVL_ID, incColumn.lvlIdRule);
				// 删除节点列表中节点
				replaceUnt.delParNode(lstDelBookmark, dsIncCol);
				bRefresh = true;
			} else if (!sIncColName.equals(incColumnObj.INCCOL_NAME)) {// 编码未改变判断名称是否改变
				// 得到选中树节点及其子节点的bookmark列表
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(incColumn.ftreeIncColumn);
				// 收入栏目全名重新获得
				replaceUnt.ReplaceFname(lstBookmark, dsIncCol, sNewFName,
						dsIncCol.fieldByName(IIncColumn.INCCOL_FNAME)
								.getOldValue().toString(),
						IIncColumn.INCCOL_FNAME);
			}
		}

		// 修改的节点有叶节点，只能修改名称和编码,更改只节点的fname值
		if ("modname".equals(sSaveType)) {
			dsIncCol.fieldByName("INCCOL_NAME").setValue(sIncColName);
		}
		// 修改的节点为叶节点，只能修改格式和编码
		if ("modformate".equals(sSaveType)) {
			dsIncCol.fieldByName("DISPLAY_FORMAT").setValue(
					incColumn.fcbxSFormate.getValue().toString());
		}
		// 保存类型为add,addfirstson,mod,将值传给收入栏目DataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)) {
			// 生成fname
			String sIncColNameNew = null;
			if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
				if ("".equals(incColumnObj.INCCOL_FNAME)) // 没上级，fname是其本身
				{
					sIncColNameNew = sIncColName;
				} else { // 有上级，fname= 上级fname+填写的name
					sIncColNameNew = incColumnObj.INCCOL_FNAME
							+ UntPub.PARTITION_TAG + sIncColName;
				}
				dsIncCol.fieldByName("inccol_fname").setValue(sIncColNameNew);
			} else {// mod,在上面已判断

			}
			// 名称
			dsIncCol.fieldByName("inccol_name").setValue(
					incColumn.ftxtfIncColName.getValue().toString());
			// 该栏目纵向求和
			dsIncCol.fieldByName("SUM_FLAG").setValue(
					new Integer("false".equals(incColumn.fchkSumFlag.getValue()
							.toString()) ? 0 : 1));
			// 该栏目隐藏
			dsIncCol.fieldByName("IS_HIDE").setValue(
					new Integer("false".equals(incColumn.fchkHideFlag
							.getValue().toString()) ? 0 : 1));
			// 该栏目与预留比例,mod by CL ,09,08,24
			dsIncCol.fieldByName("N2").setValue(
					new Integer("false".equals(incColumn.fchkRPFlag.getValue()
							.toString()) ? 0 : 1));

			dsIncCol.fieldByName("DISPLAY_FORMAT").setValue(
					incColumn.fcbxSFormate.getValue().toString());

			// 根据数据来源保存不同信息
			if ("1".equals(incColumn.frdoIncColDts.getValue().toString())) {// 计算
				// 计算公式
				String sForumla = incColumn.ftxtaIncColFormula.getValue()
						.toString();
				if (!"".equals(sForumla)) {
					dsIncCol.fieldByName("FORMULA_DET").setValue(
							PubInterfaceStub.getMethod().replaceTextEx(
									sForumla, 0, IIncColumn.INCCOL_TABLE,
									IIncColumn.INCCOL_FNAME,
									IIncColumn.INCCOL_ENAME,
									"set_year =" + Global.loginYear));
				} else {
					dsIncCol.fieldByName("FORMULA_DET").setValue("");
				}
				// 计算优先级
				dsIncCol.fieldByName("CALC_PRI").setValue(
						incColumn.jspIncColCalcPRI.getValue().toString());
			}

			if ("addfirstson".equals(sSaveType)) { // 增加第一个子节点，更改父节点的部分信息
				bAddFirstNode = true;
				String sBookmark = dsIncCol.toogleBookmark();
				MyTreeNode node = (MyTreeNode) incColumn.ftreeIncColumn
						.getSelectedNode();
				if (node != null) {
					dsIncCol.gotoBookmark(node.getBookmark());
					dsIncCol.fieldByName("inccol_ename").setValue("");
					dsIncCol.fieldByName("data_source")
							.setValue(new Integer(0));
					dsIncCol.fieldByName("formula_det").setValue("");
					dsIncCol.fieldByName("calc_pri").setValue(new Integer(0));
					dsIncCol.fieldByName("sum_flag").setValue(new Integer(0));
					dsIncCol.fieldByName("is_hide").setValue(new Integer(0));
					dsIncCol.fieldByName("end_flag").setValue(new Integer(0));
					dsIncCol.fieldByName("display_format").setValue("");
					dsIncCol.fieldByName("edit_format").setValue("");
					// 定位回本节点
					dsIncCol.gotoBookmark(sBookmark);
				}
			}
		}

		// 数据来源 mod by Cl,09,08,24
		lstTollCode = SysUntPub.getCodeList(ftreIncomeSubItem);
		if ("0".equals(incColumn.frdoIncColDts.getValue().toString())) {
			dsIncCol.fieldByName("DATA_SOURCE").setValue(
					incColumn.frdoIncColDts.getValue().toString());
		} else {
			if (lstTollCode != null) {
				dsIncCol.fieldByName("DATA_SOURCE").setValue(
						new String("2").toString());
			} else {
				dsIncCol.fieldByName("DATA_SOURCE").setValue(
						incColumn.frdoIncColDts.getValue().toString());
			}
		}
		if ("0".equals(incColumn.frdoIncColDts.getValue().toString())) { // 数据来源:录入
			// 计算公式
			dsIncCol.fieldByName("FORMULA_DET").setValue("");
			// 计算优先级
			dsIncCol.fieldByName("CALC_PRI").setValue("0");
			// 得到收费项目选中的节点编码
			// lstTollCode = SysUntPub.getCodeList(ftreIncomeSubItem);
		}
		// mod by CL,09,08,24

		dsIncCol.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsIncCol.fieldByName("par_id").setValue(incColumnObj.LVL_ID);
		dsIncCol.fieldByName("name").setValue(slvlId + " " + sIncColName);

		dsIncCol.maskDataChange(false);

		// 得当前编辑节点的内码
		String sIncColCode = dsIncCol.fieldByName("INCCOL_CODE").getString();
		// 提交数据
		sysIaeStruServ.saveIncCol(dsIncCol, incColumnObj.INCCOL_CODE,
				sIncColCode, bAddFirstNode, lstTollCode, Global.loginYear);
		dsIncCol.applyUpdate();
		incColumn.ftreeIncColumn.reset();
		// add状态定位到增加的节点
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			incColumn.ftreeIncColumn.expandTo("lvl_id", slvlId);
		if (bRefresh) { // 刷新树
			incColumn.ftreeIncColumn.reset();
			incColumn.ftreeIncColumn.expandTo("lvl_id", slvlId);
		}
		// 刷新系统数据字典信息
		if (!SysUntPub.synDict(IIncColumn.INCCOL_TABLE)) {
			JOptionPane.showMessageDialog(incColumn, "刷新系统数据字典信息发生错误!", "提示",
					JOptionPane.ERROR_MESSAGE);
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
		InfoPackage infoPackage;
		String sLvlId = incColumn.ftxtfIncColCode.getValue().toString();
		String sIncColName = incColumn.ftxtfIncColName.getValue().toString()
				.trim();
		String sParId = incColumn.lvlIdRule.previous(sLvlId); // 获得上级编码
		// 判断编码是否填写正确
		if ("mod".equals(sSaveType.substring(0, 3))
				&& sLvlId != incColumnObj.LVL_ID) {
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(incColumn, "编码不能为空!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				incColumn.ftxtfIncColCode.setFocus();
				return false;
			}
			// 判断编码填写的是否是数字
			if (!sLvlId.matches("\\d+")) {
				JOptionPane.showMessageDialog(incColumn, "编码必须是数字，请重新填写！",
						"提示", JOptionPane.INFORMATION_MESSAGE);
				incColumn.ftxtfIncColCode.setFocus();
				return false;
			}

			// 编码被修改，要判断编码
			if (sLvlId != incColumnObj.LVL_ID) {
				// 判断编码长度填写是否正确,修改的情况要判断
				int iLevel = incColumn.lvlIdRule.levelOf(sLvlId); // 获得当前编码节次
				int iCount = incColumn.lvlIdRule.originRules().size();
				if (iLevel < 0) {
					JOptionPane.showMessageDialog(incColumn,
							"编码不正确，编码需四位一节且不超过节次长度" + String.valueOf(iCount)
									+ "节 ，请重新填写!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}
				// 判断最顶级编码需要>=1001
				String sRootCode = incColumn.lvlIdRule.rootCode(sLvlId); // 根据当前编码获得最顶级的编码值
				if (sRootCode.compareTo(ISysIaeStru.ROOT_CODE) < 0) {
					JOptionPane.showMessageDialog(incColumn,
							"编码必须大于1000,请重新填写!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}
				// 修改编码父编码是否存在,且不是不叶子节点，如果是叶子节点，不可修改
				if (!"".equals(sParId) && sParId != null) {
					infoPackage = sysIaeStruServ.judgeIncColParExist(sParId,
							Global.loginYear);
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(incColumn, infoPackage
								.getsMessage(), "提示",
								JOptionPane.INFORMATION_MESSAGE);
						incColumn.ftxtfIncColCode.setFocus();
						return false;
					}
				}
				// 判断编码是否重复,分增加和修改两种情况,增加编码自动生成
				infoPackage = sysIaeStruServ.judgeIncColCodeRepeat(sLvlId,
						Global.loginYear, incColumnObj.INCCOL_CODE, true);
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(incColumn, infoPackage
							.getsMessage(), "提示",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}

				// 判断节点不能直接改为下级节点，造成数不完整
				ReplaceUnt replaceUnt = new ReplaceUnt();
				if (!replaceUnt.checkCode(sLvlId, dsIncCol
						.fieldByName("lvl_id").getOldValue().toString(),
						incColumn.lvlIdRule)) {
					JOptionPane.showMessageDialog(incColumn,
							"不能将节点修改成自己的下级节点,请重新填写编码!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}
			}
		}

		// 判断名称是否填写
		if ("".equals(sIncColName)) {
			JOptionPane.showMessageDialog(incColumn, "收入栏目名称不能为空!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			incColumn.ftxtfIncColName.setFocus();
			return false;
		}
		// 判断同级名称是否重复
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
			infoPackage = sysIaeStruServ.judgeIncColNameRepeat(sIncColName,
					sParId, Global.loginYear, null, false);
		} else {
			infoPackage = sysIaeStruServ.judgeIncColNameRepeat(sIncColName,
					sParId, Global.loginYear, incColumnObj.INCCOL_CODE, true);
		}
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incColumn, infoPackage.getsMessage(),
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断数据来源是计算的必须设置计算公式
		if ("1".equals(incColumn.frdoIncColDts.getValue().toString())
				&& ""
						.equals(incColumn.ftxtaIncColFormula.getValue()
								.toString())) {
			JOptionPane.showMessageDialog(incColumn, "数据来源是计算的必须设置计算公式!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断该栏目为预留比较只能设一个
		if (Common.estimate(incColumn.fchkRPFlag.getValue())) {
			String incColCode = incColumn.ftxtPriCode.getValue().toString();
			DataSet dsIncColTmp = sysIaeStruServ.judgeIncColYLBLExist(
					Global.loginYear, incColCode);
			if (dsIncColTmp.getRecordCount() > 0) {
				String info = "";
				dsIncColTmp.beforeFirst();
				while (dsIncColTmp.next()) {
					if (!Common.isNullStr(info)) {
						info += ",";
					}
					info += dsIncColTmp.fieldByName(IIncColumn.INCCOL_CODE)
							.getString()
							+ dsIncColTmp.fieldByName(IIncColumn.INCCOL_NAME)
									.getString();
				}
				JOptionPane.showMessageDialog(incColumn, "预留比例只能有一个栏目设置,"
						+ info + "已设置!", "提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		return true;
	}
}
