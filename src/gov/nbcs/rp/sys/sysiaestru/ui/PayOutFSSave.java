/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出资金来源保存操作类
 * </p>
 * <p>
 * Description:支出资金来源保存操作类
 * </p>

 */
public class PayOutFSSave {
	private PayOutFS payOutFS = null;

	private String sSaveType = null;

	private PayOutFsObj payOutFsObj = null;

	private DataSet dsPayOutFS = null;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutFS
	 *            支出资金来源管理客户端主界面类
	 */
	public PayOutFSSave(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		this.sSaveType = payOutFS.sSaveType;
		this.payOutFsObj = payOutFS.payOutFsObj;
		this.dsPayOutFS = payOutFS.dsPayOutFS;
		// 定义数据库接口
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * 保存支出资金来源操作方法
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	public void save() throws HeadlessException, Exception {
		// 得到收费项目选中的节点编码
		ArrayList lstAcctCode = null;

		// boolean bRefresh = false;
		// 判断信息填写是否完整
		if (!judgeFillInfo())
			return;
		String slvlId = payOutFS.ftxtfPfsCode.getValue().toString();
		String sPfsName = payOutFS.ftxtfPfsName.getValue().toString();
		// 报表名称
		dsPayOutFS.fieldByName("C1").setValue(
				payOutFS.ftxtReportPfsName.getValue().toString());

		payOutFS.dsPayOutFS.maskDataChange(true);
		// 判断编码是否修改,判断编号是否修改
		if ("mod".equals(sSaveType.substring(0, 3))) {
			String sParId = payOutFS.lvlIdRule.previous(slvlId);
			if (sParId == null)
				sParId = "";

			ReplaceUnt replaceUnt = new ReplaceUnt();
			String sNewFName = "";
			if (!"".equals(sParId)) {
				List lstValue = replaceUnt.getParNodeInfo(sParId,
						new String[] { IPayOutFS.PFS_FNAME }, dsPayOutFS);
				if (lstValue != null)
					sNewFName = lstValue.get(0).toString();
			}
			if (sNewFName != null && !"".equals(sNewFName))
				sNewFName = sNewFName + UntPub.PARTITION_TAG + sPfsName;
			else
				sNewFName = sPfsName;

			if (!slvlId.equals(payOutFsObj.lvl_id)) {// 节点编码发生改变
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(payOutFS.ftreePayOutFS);
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						payOutFS.ftreePayOutFS, dsPayOutFS);

				replaceUnt.ReplaceFname(lstBookmark, dsPayOutFS, sNewFName,
						dsPayOutFS.fieldByName("pfs_fname").getOldValue()
								.toString(), IPayOutFS.PFS_FNAME);
				replaceUnt.ReplaceLvlPar(lstBookmark, dsPayOutFS, slvlId,
						payOutFsObj.lvl_id, payOutFS.lvlIdRule);
				replaceUnt.delParNode(lstDelBookmark, dsPayOutFS);
				// bRefresh = true;
			} else if (!sPfsName.equals(payOutFsObj.PFS_NAME)) {// 编码未改变判断名称是否改变
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(payOutFS.ftreePayOutFS);
				replaceUnt.ReplaceFname(lstBookmark, dsPayOutFS, sNewFName,
						dsPayOutFS.fieldByName(IPayOutFS.PFS_FNAME)
								.getOldValue().toString(), IPayOutFS.PFS_FNAME);
				// add by xxl 因为设置标题提前没有触发刷新，所以在标题改变时，也刷新树
				// bRefresh = true;
			}
		}

		// 修改的节点有叶节点，只能修改名称和编码,更改只节点的fname值
		if ("modname".equals(sSaveType)) {
			dsPayOutFS.fieldByName("pfs_name").setValue(sPfsName);
		}
		// 修改的节点为叶节点，只能修改格式和编码
		if ("modformate".equals(sSaveType)) {
			dsPayOutFS.fieldByName("DISPLAY_FORMAT").setValue(
					payOutFS.fcbxSFormate.getValue().toString());
		}
		// add,addfirstson,mod,将值传给dataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)) {
			// 生成fname
			if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
				String sPfsfNameNew;
				if ("".equals(payOutFsObj.pfs_fname)) // 没上级，fname是其本身
				{
					sPfsfNameNew = sPfsName;
				} else { // 有上级，fname= 上级fname+填写的name
					sPfsfNameNew = payOutFsObj.pfs_fname + UntPub.PARTITION_TAG
							+ sPfsName;
				}
				dsPayOutFS.fieldByName("pfs_fname").setValue(sPfsfNameNew);
			} else {// mod,在上面已判断

			}

			// 名称
			dsPayOutFS.fieldByName("pfs_name").setValue(
					payOutFS.ftxtfPfsName.getValue().toString());
			// 性质
			dsPayOutFS.fieldByName("STD_TYPE_CODE").setValue(
					payOutFS.flstPfsKind.getSelectedElement().getId());
			// 参与控制数分配
			dsPayOutFS.fieldByName("CF_PFS_FLAG").setValue(
					new Integer("false".equals(payOutFS.fchkPfsFlag.getValue()
							.toString()) ? 0 : 1));
			// 默认隐藏
			dsPayOutFS.fieldByName(IPayOutFS.IN_COMMON_USE).setValue(
					new Integer("false".equals(payOutFS.fchkHide.getValue()
							.toString()) ? 0 : 1));
			// 收支平衡
			dsPayOutFS.fieldByName(IPayOutFS.IS_BALANCE).setValue(
					new Integer("false".equals(payOutFS.fchkIsBalance
							.getValue().toString()) ? 0 : 1));
			// 支持项目情况
			dsPayOutFS.fieldByName("SUP_PRJ").setValue(
					payOutFS.frdoSupPrj.getValue().toString());
			// 数据来源
			dsPayOutFS.fieldByName("DATA_SOURCE").setValue(
					payOutFS.frdoIncColDts.getValue().toString());
			dsPayOutFS.fieldByName("DISPLAY_FORMAT").setValue(
					payOutFS.fcbxSFormate.getValue().toString());
			// 计算公式
			String sForumla = payOutFS.ftxtaPfsFormula.getValue().toString();
			if (!"".equals(sForumla)) {
				dsPayOutFS.fieldByName("FORMULA_DET").setValue(
						PubInterfaceStub.getMethod().replaceTextEx(sForumla, 0,
								IPayOutFS.PFS_TABLE, IPayOutFS.PFS_FNAME,
								IPayOutFS.PFS_ENAME,
								"set_year =" + Global.loginYear));
			} else {
				dsPayOutFS.fieldByName("FORMULA_DET").setValue("");
			}
			// 计算优先级
			dsPayOutFS.fieldByName("CALC_PRI").setValue(
					payOutFS.jspPfsCalcPRI.getValue().toString());

			if ("addfirstson".equals(sSaveType)) { // 增加第一个子节点，更改父节点的部分信息
				String sBookmark = dsPayOutFS.toogleBookmark();
				MyTreeNode node = (MyTreeNode) payOutFS.ftreePayOutFS
						.getSelectedNode();
				if (node != null) {
					dsPayOutFS.gotoBookmark(node.getBookmark());
					dsPayOutFS.fieldByName("PFS_EName").setValue("");
					dsPayOutFS.fieldByName("std_type_code").setValue("");
					dsPayOutFS.fieldByName("data_source").setValue(
							new Integer(0));
					dsPayOutFS.fieldByName("formula_det").setValue("");
					dsPayOutFS.fieldByName("calc_pri").setValue(new Integer(0));
					dsPayOutFS.fieldByName("sup_prj").setValue(new Integer(0));
					dsPayOutFS.fieldByName("CF_PFS_flag").setValue(
							new Integer(0));
					dsPayOutFS.fieldByName(IPayOutFS.IN_COMMON_USE).setValue(
							new Integer(0));
					dsPayOutFS.fieldByName("end_flag").setValue(new Integer(0));
					dsPayOutFS.fieldByName("display_format").setValue("");
					dsPayOutFS.fieldByName("edit_format").setValue("");
					// 定位回本节点
					dsPayOutFS.gotoBookmark(sBookmark);
				}
			}
		}

		if ("0".equals(payOutFS.frdoIncColDts.getValue().toString())) { // 数据来源:录入
			// 计算公式
			dsPayOutFS.fieldByName("FORMULA_DET").setValue("");
			// 计算优先级
			dsPayOutFS.fieldByName("CALC_PRI").setValue("0");
			// 得到收费项目选中的节点编码
			lstAcctCode = SysUntPub
					.getCodeList(payOutFS.fpnlAcct.getFtreAcct());
		}
		// modified by xxl 20090915原因是：当同是修改来源的编码和名称时，会报NULL错
		dsPayOutFS.fieldByName("name").setValue(slvlId + " " + sPfsName);
		dsPayOutFS.maskDataChange(false);

		dsPayOutFS.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsPayOutFS.fieldByName("par_id").setValue(payOutFsObj.lvl_id);

		// 计算列对项目支持的自动判断
		// 得到原sup_prj字段值
		Object oldValue = dsPayOutFS.fieldByName(IPayOutFS.SUP_PRJ)
				.getOldValue();
		// 得到新sup_prj字段值
		Object newValue = dsPayOutFS.fieldByName(IPayOutFS.SUP_PRJ).getString();
		String sBookmark = dsPayOutFS.toogleBookmark();
		// 判断sup_prj字段值有没有发生改变
		if (oldValue != null
				&& !oldValue.toString().equals(newValue.toString())) {
			String pfsEname = dsPayOutFS.fieldByName(IPayOutFS.PFS_ENAME)
					.getString();
			// 使用递归，更改使用该资金来源作为公式元素的记录的sup_prj字段值
			changeSupPrj(pfsEname, dsPayOutFS);
		}
		dsPayOutFS.gotoBookmark(sBookmark);

		// 提交数据
		if ("addfirstson".equals(sSaveType)) {
			sysIaeStruServ.savePayOutFS(dsPayOutFS, dsPayOutFS.fieldByName(
					IPayOutFS.PFS_CODE).getString(), payOutFsObj.PFS_CODE,
					Global.loginYear, lstAcctCode, true);
		} else {
			sysIaeStruServ.savePayOutFS(dsPayOutFS, dsPayOutFS.fieldByName(
					IPayOutFS.PFS_CODE).getString(), payOutFsObj.PFS_CODE,
					Global.loginYear, lstAcctCode, false);
		}
		dsPayOutFS.applyUpdate();
		// add状态定位到增加的节点
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			payOutFS.ftreePayOutFS.expandTo("lvl_id", slvlId);
		// 刷新树
		dsPayOutFS.maskDataChange(true);
		payOutFS.ftreePayOutFS.reset();
		dsPayOutFS.maskDataChange(false);
		payOutFS.ftreePayOutFS.expandTo("lvl_id", slvlId);

		// 刷新系统数据字典信息
		if (!SysUntPub.synDict(IPayOutFS.PFS_TABLE)) {
			JOptionPane.showMessageDialog(payOutFS, "刷新系统数据字典信息发生错误!", "提示",
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
		String sLvlId = payOutFS.ftxtfPfsCode.getValue().toString();
		String sParId = payOutFS.lvlIdRule.previous(sLvlId);// 获得父对象编码
		String sPfsName = payOutFS.ftxtfPfsName.getValue().toString().trim();
		String sPfsCode = dsPayOutFS.fieldByName("pfs_code").getString();
		// 编码被修改，要判断编码
		if ("mod".equals(payOutFS.sSaveType.substring(0, 3))
				&& sLvlId != payOutFsObj.lvl_id) {
			// 判断编码是否填写正确
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(payOutFS, "编码不能为空!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// 判断编码填写的是否是数字
			if (!sLvlId.matches("\\d+")) {
				JOptionPane.showMessageDialog(payOutFS, "编码必须是数字，请重新填写！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// 判断编码长度填写是否正确,修改的情况要判断
			int iLevel = payOutFS.lvlIdRule.levelOf(sLvlId); // 获得当前编码节次
			int iCount = payOutFS.lvlIdRule.originRules().size();
			if (iLevel < 0) {
				JOptionPane.showMessageDialog(payOutFS, "编码不正确，编码需四位一节且不超过节次长度"
						+ String.valueOf(iCount) + "节 ，请重新填写!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// 判断最顶级编码需要>=1001
			String sRootCode = payOutFS.lvlIdRule.rootCode(sLvlId); // 根据当前编码获得最顶级的编码值
			if (sRootCode.compareTo(ISysIaeStru.ROOT_CODE) < 0) {
				JOptionPane.showMessageDialog(payOutFS, "编码必须大于1000,请重新填写!",
						"提示", JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// 修改编码父编码是否存在,且不是不叶子节点，如果是叶子节点，不可修改
			if (!"".equals(sParId) && sParId != null) {
				InfoPackage infoPackage = sysIaeStruServ.judgePfsParExist(
						sParId, Global.loginYear);
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(payOutFS, infoPackage
							.getsMessage(), "提示",
							JOptionPane.INFORMATION_MESSAGE);
					payOutFS.ftxtfPfsCode.setFocus();
					return false;
				}
			}
			// 判断编码是否重复
			InfoPackage infoPackage = sysIaeStruServ.judgePfsCodeRepeat(sLvlId,
					Global.loginYear, payOutFsObj.PFS_CODE, true);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(payOutFS, infoPackage
						.getsMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}

			// 判断节点不能直接改为下级节点，造成数不完整
			ReplaceUnt replaceUnt = new ReplaceUnt();
			if (!replaceUnt.checkCode(sLvlId, dsPayOutFS.fieldByName("lvl_id")
					.getOldValue().toString(), payOutFS.lvlIdRule)) {
				JOptionPane.showMessageDialog(payOutFS,
						"不能将节点修改成自己的下级节点,请重新填写编码!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
		}

		// 判断资金名称是否填写
		if ("".equals(sPfsName)) {
			JOptionPane.showMessageDialog(payOutFS, "资金来源名称不能为空!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			payOutFS.ftxtfPfsName.setFocus();
			return false;
		}
		// 判断同级名称是否重复
		InfoPackage infoPackage;
		if ("mod".equals(sSaveType.substring(0, 3))) // 是否是修改,判断条件加上不等于自己
			infoPackage = sysIaeStruServ.judgePfsNameRepeat(sPfsName, sParId,
					Global.loginYear, sPfsCode, true);
		else
			infoPackage = sysIaeStruServ.judgePfsNameRepeat(sPfsName, sParId,
					Global.loginYear, null, false);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(payOutFS, infoPackage.getsMessage(),
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 判断数据来源是计算的必须设置计算公式
		if ("1".equals(payOutFS.frdoIncColDts.getValue().toString())
				&& "".equals(payOutFS.ftxtaPfsFormula.getValue().toString())) {
			JOptionPane.showMessageDialog(payOutFS, "数据来源是计算的必须设置计算公式!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 判断资金来源总合计是否已被使用，表中只能有一个字段是总合计
		if ("001".equals(payOutFS.flstPfsKind.getSelectedElement().getId())) {
			if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)
					|| "mod".equals(sSaveType)) {
				boolean bFlag;
				if ("mod".equals(sSaveType)) {
					bFlag = sysIaeStruServ.judgePfsStdTypeCode(
							payOutFsObj.PFS_CODE, Global.loginYear, true);
				} else {
					bFlag = sysIaeStruServ.judgePfsStdTypeCode(null,
							Global.loginYear, false);
				}
				if (!bFlag) {
					JOptionPane.showMessageDialog(payOutFS,
							"类型[资金来源总合计]已经被使用!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if (dsPayOutFS.fieldByName("end_flag").getInteger() != 1) {
					JOptionPane.showMessageDialog(payOutFS,
							"类型[资金来源总合计]只能使用在末级资金来源!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if (!"1".equals(payOutFS.frdoIncColDts.getValue().toString())) {
					JOptionPane.showMessageDialog(payOutFS,
							"类型[资金来源总合计]数据来源只能是计算!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * /使用递归，更改使用该资金来源作为公式元素的记录的sup_prj字段值
	 * 
	 * @param pfsEname
	 * @param dsPfs
	 * @throws Exception
	 */
	private void changeSupPrj(String pfsEname, DataSet dsPfs) throws Exception {
		// 查询使用该资金来源的记录
		// DatadsPfs.locateByFilter()
		// String sSql = "select PFS_CODE ,FORMULA_DET from
		// fb_iae_payout_fundsource where FORMULA_DET like '%{"
		// + pfsEname + "}%'";
		// String sFilter = "FORMULA_DET like '%{" + pfsEname + "}%'";
		// DataSet ds = DataSetUtil.filterBy(dsPfs, sFilter);
		// ds.setSqlClause(sSql);
		// ds.open();
		// 刷新计算公式使用到该资金来源的sup_prj值
		DataSet ds = getRecordWithEname(dsPfs, pfsEname);
		if (ds == null)
			return;

		ds.beforeFirst();
		while (ds.next()) {
			// 得到公式值内容
			String sForumla = ds.fieldByName(IPayOutFS.FORMULA_DET).getString();
			int supPrj = payOutFS.getsupPrjValue(sForumla, dsPayOutFS);
			// 得到公式值内容
			String sPfsCode = ds.fieldByName(IPayOutFS.PFS_CODE).getString();
			if (dsPfs.locate(IPayOutFS.PFS_CODE, sPfsCode)) {
				dsPfs.fieldByName(IPayOutFS.SUP_PRJ).setValue(
						String.valueOf(supPrj));
				// 得到Ename值
				String pfsEnameSearch = dsPfs.fieldByName(IPayOutFS.PFS_ENAME)
						.getString();
				changeSupPrj(pfsEnameSearch, dsPfs);
			}

		}
	}

	/**
	 * 得到公式内容包含pfsEname值的记录
	 * 
	 * @param dsPfs
	 * @param pfsEname
	 * @return
	 * @throws Exception
	 */
	private DataSet getRecordWithEname(DataSet dsPfs, String pfsEname)
			throws Exception {
		if (dsPfs == null && dsPfs.isEmpty()) {
			return null;
		}
		DataSet ds = null;
		dsPfs.beforeFirst();
		while (dsPfs.next()) {
			String sForumla = dsPfs.fieldByName(IPayOutFS.FORMULA_DET)
					.getString();
			if (sForumla.indexOf("{" + pfsEname + "}") != -1) {
				if (ds == null) {
					ds = DataSet.create();
				}
				ds.append();
				ds.setOriginData(dsPfs.getOriginData());
			}
		}
		if (ds != null) {
			ds.applyUpdate();
		}
		return ds;
	}

}
