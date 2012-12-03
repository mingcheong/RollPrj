/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出资金来源删除操作类
 * </p>
 * <p>
 * Description:支出资金来源删除操作类
 * </p>

 */

public class PayOutFSDel {
	// 支出资金来源管理客户端主界面类
	private PayOutFS payOutFs = null;

	// 支出资金来源对象
	private PayOutFsObj payOutFsObj = null;

	// 支出资金来源DataSet
	private DataSet dsPayOutFS = null;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutFS
	 *            支出资金来源管理客户端主界面类
	 */
	public PayOutFSDel(PayOutFS payOutFs) {
		this.payOutFs = payOutFs;
		this.payOutFsObj = payOutFs.payOutFsObj;
		this.dsPayOutFS = payOutFs.dsPayOutFS;
		// 定义数据库接口
		this.sysIaeStruServ = payOutFs.sysIaeStruServ;
	}

	/**
	 * 删除支出资金来源操作方法
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	public boolean delete() throws HeadlessException, Exception {
		// 判断是否是末节点，不是末节点，不允许删除
		if (payOutFsObj.end_flag == 0) {
			JOptionPane.showMessageDialog(payOutFs, "支出资金来源存在子栏目,请先删除子栏目!",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 取得支出资金来源英文名
		String sEngName = payOutFsObj.pfs_ename;
		String sPfsCode = payOutFsObj.PFS_CODE;
		String sPfsName = payOutFsObj.PFS_NAME;
		// 判断该支出资金来源已作为其他栏目计算公式的内容,如果是，不允许删除
		InfoPackage infoPackage = sysIaeStruServ.judgePfsEnableDel(sEngName,
				Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(payOutFs, infoPackage.getsMessage(),
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 提示是否确定删除
		if (JOptionPane.showConfirmDialog(payOutFs, "您是否确认要删除该条记录?", "提示",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return false;

		infoPackage = sysIaeStruServ.chkFundSourceByRef(Global.loginYear,
				sPfsCode, sEngName); // 0未被使用1被引用（设入结构）2被使用（录入数据）3既被引用又被使用。
		int iReUse = Integer.parseInt(infoPackage.getObject().toString());
		MyTreeNode curNode = payOutFs.ftreePayOutFS.getSelectedNode();
		MyTreeNode parNode = (MyTreeNode) curNode.getParent();

		if (parNode.getChildCount() == 1) {// 父节点只有一个节点
			// 取得最上级只有一个儿子的节点
			MyTreeNode curNodeTmp = curNode;
			MyTreeNode parNodeTmp = parNode;
			while (parNodeTmp.getChildCount() == 1
					&& parNodeTmp != payOutFs.ftreePayOutFS.getRoot()) {
				// 判断是否只有一个儿子节点
				curNodeTmp = parNodeTmp;
				parNodeTmp = (MyTreeNode) curNodeTmp.getParent();
			}
			String sShowInfo;
			if (iReUse == 0)
				sShowInfo = "清空该资金来源到其上级[" + curNodeTmp.getUserObject()
						+ "]相关的信息吗?";
			else
				sShowInfo = "该资金来源\n" + infoPackage.getsMessage()
						+ "清空该资金来源到其上级[" + parNode.getUserObject() + "]相关的信息吗?";
			if (JOptionPane.showConfirmDialog(payOutFs, sShowInfo, "提示",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				Enumeration allNodes = curNodeTmp.breadthFirstEnumeration();
				List bookmarkList = new ArrayList();
				MyTreeNode node;
				while (allNodes.hasMoreElements()) {
					node = (MyTreeNode) allNodes.nextElement();
					bookmarkList.add(node.getBookmark());
				}
				for (int i = bookmarkList.size() - 1; i >= 0; i--) {
					dsPayOutFS.gotoBookmark(bookmarkList.get(i).toString());
					dsPayOutFS.delete();
				}
				sysIaeStruServ.delFundSource(Global.loginYear, sPfsCode, "",
						sEngName, 1, dsPayOutFS, iReUse == 0 ? false : true,
						sPfsName);

			} else {// 不清空该资金来源到其上级,清空该节点，将该节点部分信息传给父节点
				if (JOptionPane
						.showConfirmDialog(payOutFs,
								"把该资金来源对应的数据、公式全部迁移到其上级资金来源上吗?", "提示",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
					return false;
				// 删除本节点
				dsPayOutFS.delete();
				dsPayOutFS.edit();
				// 将本节点信息传给父节点
				dsPayOutFS.gotoBookmark(parNode.getBookmark());
				String sTagPfsCode = dsPayOutFS.fieldByName("pfs_code")
						.getString();
				dsPayOutFS.maskDataChange(true);
				dsPayOutFS.fieldByName("PFS_EName").setValue(
						payOutFsObj.pfs_ename);
				dsPayOutFS.fieldByName("std_type_code").setValue(
						payOutFsObj.std_type_code);
				dsPayOutFS.fieldByName("data_source").setValue(
						new Integer(payOutFsObj.data_source));
				dsPayOutFS.fieldByName("formula_det").setValue(
						payOutFsObj.formula_det);
				dsPayOutFS.fieldByName("calc_pri").setValue(
						new Integer(payOutFsObj.calc_pri));
				dsPayOutFS.fieldByName("sup_prj").setValue(
						new Integer(payOutFsObj.sup_prj));
				dsPayOutFS.fieldByName("CF_PFS_flag").setValue(
						new Integer(payOutFsObj.cf_pfs_flag));
				dsPayOutFS.fieldByName("end_flag").setValue(new Integer(1));
				dsPayOutFS.maskDataChange(false);
				sysIaeStruServ.delFundSource(Global.loginYear, sPfsCode,
						sTagPfsCode, sEngName, 2, dsPayOutFS,
						iReUse == 0 ? false : true, sPfsName);
			}
		} else {// 父节点有两个以上节点，删除时直接删除
			// 删除本节点
			dsPayOutFS.delete();
			dsPayOutFS.gotoBookmark(parNode.getBookmark());
			sysIaeStruServ.delFundSource(Global.loginYear, sPfsCode, "",
					sEngName, 1, dsPayOutFS, iReUse == 0 ? false : true,
					sPfsName);
		}
		dsPayOutFS.applyUpdate();
		// 刷新系统数据字典信息
		if (!SysUntPub.synDict(IPayOutFS.PFS_TABLE)) {
			JOptionPane.showMessageDialog(payOutFs, "刷新系统数据字典信息发生错误!", "提示",
					JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}
}
