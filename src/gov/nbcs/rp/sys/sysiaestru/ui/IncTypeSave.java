/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入项目类别保存操作类
 * </p>
 * <p>
 * Description:收入项目类别保存操作类
 * </p>

 */
public class IncTypeSave {
	// 收入项目类别管理客户端主界面类
	private IncType incType = null;

	// 收入项目类别对象
	private IncTypeObj incTypeObj = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	// 收入项目类别DataSet
	private DataSet dsIncType = null;

	// 保存类型
	private String sSaveType = null;

	// 选中的收入栏目节点
	private MyTreeNode incColNodes[] = null;

	// 选中的支出资金来源节点
	private MyTreeNode pfsNodes[] = null;

	/**
	 * 构造函数
	 * 
	 * @param incType
	 *            收入项目类别管理客户端主界面类
	 */
	public IncTypeSave(IncType incType) {
		this.incType = incType;
		this.dsIncType = incType.dsIncType;
		this.incTypeObj = incType.incTypeObj;
		this.sSaveType = incType.sSaveType;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	/**
	 * 保存收入项目类别操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void save() throws Exception {
		boolean bRefresh = false;
		// 判断信息填写是否完整
		if (!judgeFillInfo())
			return;
		String slvlId = incType.ftxtfIncTypeCode.getValue().toString();
		String sIncTypeName = incType.ftxtfIncTypeName.getValue().toString();

		String sParId = null;

		dsIncType.maskDataChange(true);
		// 判断编码是否修改,判断编号是否修改
		if ("mod".equals(incType.sSaveType.substring(0, 3))) {
			sParId = incType.lvlIdRule.previous(slvlId);
			if (sParId == null)
				sParId = "";
			// 节点编码发生改变
			if (!slvlId.equals(incTypeObj.lvl_id)) {
				ReplaceUnt replaceUnt = new ReplaceUnt();
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(incType.ftreeIncType);
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						incType.ftreeIncType, dsIncType);
				replaceUnt.ReplaceLvlPar(lstBookmark, dsIncType, slvlId,
						incTypeObj.lvl_id, incType.lvlIdRule);
				replaceUnt.delParNode(lstDelBookmark, dsIncType);
				bRefresh = true;
			}
		}

		// 修改的节点有叶节点，只能修改名称和编码
		if ("modname".equals(sSaveType)) {
			dsIncType.fieldByName(IIncType.INCTYPE_NAME).setValue(sIncTypeName);
		}
		// add,addfirstson,mod,将值传给dataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)
				|| "modformate".equals(sSaveType)) {
			// 名称
			dsIncType.fieldByName(IIncType.INCTYPE_NAME).setValue(
					incType.ftxtfIncTypeName.getValue().toString());
			// 性质
			dsIncType.fieldByName(IIncType.STD_TYPE_CODE).setValue(
					incType.flstIncTypeKind.getSelectedElement().getId());
			// 数据类型,录入或非税表取数
			dsIncType.fieldByName(IIncType.IS_INC).setValue(
					incType.frdoIsInc.getValue());
			// 是否其中数
			dsIncType.fieldByName(IIncType.IS_SUM).setValue(
					new Integer("false".equals(incType.fchkIsMid.getValue()
							.toString()) ? 1 : 0));

		}

		if ("addfirstson".equals(sSaveType)) {
			String sBookmark = dsIncType.toogleBookmark();
			MyTreeNode node = incType.ftreeIncType
					.getSelectedNode();
			MyTreeNode parentNode = (MyTreeNode) node.getParent();
			if (parentNode != null) {
				dsIncType.gotoBookmark(node.getBookmark());
				dsIncType.fieldByName(IIncType.STD_TYPE_CODE).setValue("");
				dsIncType.fieldByName("end_flag").setValue(new Integer(0));
				dsIncType.fieldByName("IS_INC").setValue(new Integer(0));
				dsIncType.fieldByName(IIncType.IS_SUM).setValue(new Integer(1));
				// 定位回本节点
				dsIncType.gotoBookmark(sBookmark);
			}
		}

		dsIncType.maskDataChange(false);
		dsIncType.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsIncType.fieldByName("par_id").setValue(incTypeObj.lvl_id);
		else
			dsIncType.fieldByName("par_id").setValue(sParId);
		dsIncType.fieldByName("name").setValue(slvlId + " " + sIncTypeName);

		// 得当前编辑节点的内码
		String sIncTypeCode = dsIncType.fieldByName(IIncType.INCTYPE_CODE)
				.getString();

		// 数据来源
		String isInc = incType.frdoIsInc.getValue().toString();

		// 支出资金来源
		List lstPfsCode = null;
		// 得到选中的收入栏节点内码
		if ("0".equals(isInc)) {// 录入
			incType.dsInccolumnToInc = null;
		} else if ("1".equals(isInc)) {// 非税表中取数
			// 组织收入项目类别与收入栏目的对应关系
			incType.dsInccolumnToInc = orgInctypeToIncolumn(
					incType.dsInccolumnToInc,
					incType.fpnlIncCol.ftreeIncColumn, sIncTypeCode);
		} else if ("2".equals(isInc)) {// 从支出预算表取数
			incType.dsInccolumnToInc = null;
			DataSet dsPayOutFS = incType.fpnlPfs.ftreePfs.getDataSet();
			lstPfsCode = SysUntPub.getLeafNodeCode(incType.fpnlPfs.ftreePfs,
					dsPayOutFS, IPayOutFS.PFS_CODE);
		}

		// 提交数据
		String sNewIncTypeName = null;
		if ("mod".equals(sSaveType)) {
			if (!sIncTypeName.equals(incTypeObj.inctype_name)) {
				sNewIncTypeName = sIncTypeName;
			}
		}
		sysIaeStruServ.saveIncType(dsIncType, incTypeObj.inctype_code,
				sIncTypeCode, incType.dsInccolumnToInc, Global.loginYear,
				sNewIncTypeName, lstPfsCode);

		dsIncType.applyUpdate();
		// add状态定位到增加的节点
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			incType.ftreeIncType.expandTo("lvl_id", slvlId);
		if (bRefresh) { // 刷新树
			incType.ftreeIncType.reset();
			incType.ftreeIncType.expandTo("lvl_id", slvlId);
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
		String sLvlId = incType.ftxtfIncTypeCode.getValue().toString();
		String sIncTypeName = incType.ftxtfIncTypeName.getValue().toString()
				.trim();
		String sParId = incType.lvlIdRule.previous(sLvlId);// 获得父对象编码
		// 判断编码是否修改,判断编号是否修改
		if ("mod".equals(incType.sSaveType.substring(0, 3))) {
			// 判断编码是否填写正确
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(incType, "编码不能为空!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}
			// 判断编码填写的是否是数字
			if (!sLvlId.matches("\\d+")) {
				JOptionPane.showMessageDialog(incType, "编码必须是数字，请重新填写！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}

			// 编码被修改，要判断编码
			if (sLvlId != incTypeObj.lvl_id) {
				// 判断编码长度填写是否正确,修改的情况要判断
				int iLevel = incType.lvlIdRule.levelOf(sLvlId); // 获得当前编码节次
				int iCount = incType.lvlIdRule.originRules().size();
				if (iLevel < 0) {
					JOptionPane.showMessageDialog(incType,
							"编码不正确，编码需四位一节且不超过节次长度" + String.valueOf(iCount)
									+ "节 ，请重新填写!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					incType.ftxtfIncTypeCode.setFocus();
					return false;
				}
				// 修改编码父编码是否存在,且不是不叶子节点，如果是叶子节点，不可修改
				if (!"".equals(sParId) && sParId != null) {
					InfoPackage infoPackage = sysIaeStruServ
							.judgeIncTypeParExist(sParId, Global.loginYear);
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(incType, infoPackage
								.getsMessage(), "提示",
								JOptionPane.INFORMATION_MESSAGE);
						incType.ftxtfIncTypeCode.setFocus();
						return false;
					}
				}
			}
			// 判断编码是否重复,分增加和修改两种情况
			InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeCodeRepeat(
					sLvlId, Global.loginYear, incTypeObj.inctype_code, true);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(incType, infoPackage
						.getsMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}
			// 判断节点不能直接改为下级节点，造成数不完整
			ReplaceUnt replaceUnt = new ReplaceUnt();
			if (!replaceUnt.checkCode(sLvlId, dsIncType.fieldByName("lvl_id")
					.getOldValue().toString(), incType.lvlIdRule)) {
				JOptionPane.showMessageDialog(incType,
						"不能将节点修改成自己的下级节点,请重新填写编码!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}
		}
		// 判断名称是否填写
		if ("".equals(sIncTypeName)) {
			JOptionPane.showMessageDialog(incType, "收入项目类别名称不能为空!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			incType.ftxtfIncTypeCode.setFocus();
			return false;
		}
		// 判断同级名称是否重复
		InfoPackage infoPackage;
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
			infoPackage = sysIaeStruServ.judgeIncTypeNameRepeat(sIncTypeName,
					sParId, Global.loginYear, null, false);
		} else {
			infoPackage = sysIaeStruServ.judgeIncTypeNameRepeat(sIncTypeName,
					sParId, Global.loginYear, incTypeObj.inctype_code, true);
		}
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incType, infoPackage.getsMessage(),
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		String isInc = incType.frdoIsInc.getValue().toString();
		// 判断数据来源为从非税表取值时，是否设置了与收入项目的对应关系
		if ("1".equals(isInc)) {
			incColNodes = incType.fpnlIncCol.ftreeIncColumn
					.getSelectedNodes(true);
			if (incColNodes.length == 0) {
				JOptionPane.showMessageDialog(incType, "请选择对应的收入栏目!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		} else if ("2".equals(isInc)) { // 从支出预算表取数
			pfsNodes = incType.fpnlPfs.ftreePfs.getSelectedNodes(true);
			if (pfsNodes.length == 0) {
				JOptionPane.showMessageDialog(incType, "请选择对应的支出资金来源!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			if (pfsNodes.length > 1) {
				JOptionPane.showMessageDialog(incType,
						"只能与支出资金来源一一对应关系,请选择一个支出资金来源!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			// 判断选中的资金来源是否与其他收入项目设置了对应关系
			String bookmark = pfsNodes[0].getBookmark();
			DataSet dsPfs = incType.fpnlPfs.ftreePfs.getDataSet();
			if (dsPfs.gotoBookmark(bookmark)) {
				// 支出资金来源编码
				DataSet dsPayOutFS = incType.fpnlPfs.ftreePfs.getDataSet();
				List lstPfsCode = SysUntPub.getLeafNodeCode(
						incType.fpnlPfs.ftreePfs, dsPayOutFS,
						IPayOutFS.PFS_CODE);
				// 根据支出资金来源编码得到对应的收入栏目编码
				DataSet dsIncTypeToPfs = sysIaeStruServ.getIncWithPfsCode(
						lstPfsCode.get(0).toString(), Global.loginYear);
				// 得到typeCode值
				String sIncTypeCode = dsIncType.fieldByName(
						IIncType.INCTYPE_CODE).getString();

				dsIncType.maskDataChange(true);
				String curBookmark = dsIncType.toogleBookmark();
				dsIncTypeToPfs.beforeFirst();
				while (dsIncTypeToPfs.next()) {
					String incTypeCodeTmp = dsIncTypeToPfs.fieldByName(
							IIncType.INCTYPE_CODE).getString();
					// 判断是否是本条记录
					if (sIncTypeCode.equals(incTypeCodeTmp)) {
						continue;
					}
					if (dsIncType.locate(IIncType.INCTYPE_CODE, incTypeCodeTmp)) {
						String incTypeCode = incType.dsIncType.fieldByName(
								IIncType.INCTYPE_CODE).getString();
						int isIncTmp = sysIaeStruServ
								.getIncTypeIsInc(incTypeCode);
						// 判断是否从非税表中取数
						if (isIncTmp == 2) {
							JOptionPane.showMessageDialog(incType, "\""
									+ dsIncType.fieldByName(ISysIaeStru.NAME)
											.getString()
									+ "\"收入项目已设置从选中的支出资金来源中取数，请选择其他资金来源!",
									"提示", JOptionPane.INFORMATION_MESSAGE);
							dsIncType.gotoBookmark(curBookmark);
							dsIncType.maskDataChange(false);
							return false;
						}
					}
				}
				dsIncType.gotoBookmark(curBookmark);
				dsIncType.maskDataChange(false);

			}

		}

		return true;
	}

	/**
	 * 组织收入项目类别与收入栏目的对应关系
	 * 
	 * @param dsInccolumnToInc
	 * @param ftreeIncColumn
	 * @param inctypeCode
	 * @throws Exception
	 */
	private DataSet orgInctypeToIncolumn(DataSet dsInccolumnToInc,
			CustomTree ftreeIncColumn, String inctypeCode) throws Exception {
		List lstCode = SysUntPub.getLeafNodeCode(ftreeIncColumn, ftreeIncColumn
				.getDataSet(), IIncColumn.INCCOL_CODE);
		if (lstCode == null || lstCode.size() == 0)
			dsInccolumnToInc = null;
		// 对应关系DataSet补充未保存的收入栏目对应关系
		for (Iterator it = lstCode.iterator(); it.hasNext();) {
			String incColCode = it.next().toString();
			if (dsInccolumnToInc != null
					&& !dsInccolumnToInc.isEmpty()
					&& dsInccolumnToInc.locate(IIncColumn.INCCOL_CODE,
							incColCode)) {
				continue;
			}
			if (dsInccolumnToInc == null) {
				dsInccolumnToInc = DataSet.create();
			}
			dsInccolumnToInc.append();
			dsInccolumnToInc.fieldByName(IIncType.INCTYPE_CODE).setValue(
					inctypeCode);
			dsInccolumnToInc.fieldByName(IIncColumn.INCCOL_CODE).setValue(
					incColCode);
			dsInccolumnToInc.fieldByName(IIncType.TOLL_FILTER).setValue("");
		}
		// 对应关系DataSet中去掉未选中树节点的对应关系
		if (dsInccolumnToInc == null || dsInccolumnToInc.isEmpty()) {
			return null;
		}
		List lstBookmark = null;
		dsInccolumnToInc.beforeFirst();
		while (dsInccolumnToInc.next()) {
			String incColCode = dsInccolumnToInc.fieldByName(
					IIncColumn.INCCOL_CODE).getString();
			if (lstCode.indexOf(incColCode) == -1) {
				if (lstBookmark == null)
					lstBookmark = new ArrayList();
				lstBookmark.add(dsInccolumnToInc.toogleBookmark());
			}
		}
		if (lstBookmark != null) {
			for (Iterator it = lstBookmark.iterator(); it.hasNext();) {
				String bookmark = it.next().toString();
				if (dsInccolumnToInc.gotoBookmark(bookmark)) {
					dsInccolumnToInc.delete();
				}
			}
		}
		dsInccolumnToInc.applyUpdate();
		return dsInccolumnToInc;
	}
}
