/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>

 */
public class SysUntPub {

	/**
	 * 得到选中的节点编码
	 * 
	 * @param customTree
	 * @return
	 */
	public static ArrayList getCodeList(CustomTree customTree) {
		ArrayList lstResult = new ArrayList();
		// 得到根节点信息
		MyTreeNode myTreeNode = (MyTreeNode) customTree.getRoot();
		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();

		// 根据根节点信息处理全选或全部未选情况
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}

		getSelectNode(myTreeNode, lstResult);
		return lstResult;
	}

	/**
	 * 得到选中节点
	 * 
	 * @param customTree
	 */
	public static MyTreeNode[] getSelectNode(CustomTree customTree) {
		ArrayList lstResult = new ArrayList();
		// 得到根节点信息
		MyTreeNode myTreeNode = (MyTreeNode) customTree.getRoot();
		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();

		// 根据根节点信息处理全选或全部未选情况
		if (myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}

		getSelectNodeA(myTreeNode, lstResult);
		int size = lstResult.size();
		MyTreeNode[] treeNode = new MyTreeNode[size];
		for (int i = 0; i < size; i++) {
			treeNode[i] = (MyTreeNode) lstResult.get(i);
		}
		return treeNode;
	}

	private static void getSelectNodeA(MyTreeNode myTreeNode,
			ArrayList lstResult) {
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		int iCount = myTreeNode.getChildCount();
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				lstResult.add(myTreeNodeTmp);
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectNodeA(myTreeNodeTmp, lstResult);
			}
		}
	}

	/**
	 * 使用递归，根据一节点得到选中子节点的编码
	 * 
	 * @param myTreeNode
	 * @param lstResult
	 */
	private static void getSelectNode(MyTreeNode myTreeNode, ArrayList lstResult) {
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		int iCount = myTreeNode.getChildCount();
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				lstResult.add(myTreeNodeTmp.sortKeyValue());
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectNode(myTreeNodeTmp, lstResult);
			}
		}
	}

	/**
	 * 刷新数据字典信息，在增，删,改时
	 * 
	 * @param tableName
	 * @return
	 */
	public static boolean synDict(String tableName) {
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("com/foundercy/fiscalbudget/dicinfo/datadict/conf/dataDictConf.xml");
		IDataDictBO dataDictBO = (IDataDictBO) beanfac
				.getBean("fb.dataDictService");
		return dataDictBO.sysRelTable(tableName, String
				.valueOf(Global.loginYear));
	}

	/**
	 * 取DataSet中某一字段的值组成List
	 * 
	 * @param ds
	 * @param sFieldName
	 * @return
	 * @throws Exception
	 */
	public static List getFieldValueOrgList(DataSet ds, String sFieldName)
			throws Exception {
		// 组织成List
		ds.beforeFirst();
		List lstResult = new ArrayList();
		String sFieldValue = null;
		ds.beforeFirst();
		while (ds.next()) {
			sFieldValue = ds.fieldByName(sFieldName).getString();
			lstResult.add(sFieldValue);
		}
		return lstResult;
	}

	/**
	 * 根据code值得到ID值
	 * 
	 * @param lstCode
	 * @param ds
	 * @param sFieldCode
	 * @param sFieldID
	 * @return
	 * @throws Exception
	 */
	public static String[] getIdWithCode(List lstCode, DataSet ds,
			String sFieldCode, String sFieldID) throws Exception {
		String sID[] = null;
		if (ds != null && !ds.isEmpty()) {
			ds.beforeFirst();
			sID = new String[lstCode.size()];
			int i = 0;
			String sBookmark = ds.toogleBookmark();
			for (Iterator it = lstCode.iterator(); it.hasNext();) {
				String sCode = it.next().toString();

				if (ds.locate(sFieldCode, sCode))
					sID[i] = ds.fieldByName(sFieldID).getString();
				else
					sID[i] = "";
				i++;
			}
			ds.gotoBookmark(sBookmark);
		}
		return sID;
	}

	/**
	 * 得到选中的末级节点编码值，树以lvl_id，par_id组成
	 * 
	 * @param tree
	 * @param ds
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static List getLeafNodeCode(CustomTree tree, DataSet ds,
			String fieldName) throws Exception {
		MyTreeNode[] nodes = tree.getSelectedNodes(true);
		List lstResult = null;
		MyTreeNode myTreeNode = null;
		String sBookmark = null;
		String curBookmark = ds.toogleBookmark();
		for (int i = 0; i < nodes.length; i++) {
			myTreeNode = nodes[i];
			sBookmark = myTreeNode.getBookmark();
			ds.gotoBookmark(sBookmark);
			if (lstResult == null)
				lstResult = new ArrayList();
			lstResult.add(ds.fieldByName(fieldName).getString());
		}
		ds.gotoBookmark(curBookmark);
		return lstResult;
	}

}
