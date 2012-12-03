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
	 * �õ�ѡ�еĽڵ����
	 * 
	 * @param customTree
	 * @return
	 */
	public static ArrayList getCodeList(CustomTree customTree) {
		ArrayList lstResult = new ArrayList();
		// �õ����ڵ���Ϣ
		MyTreeNode myTreeNode = (MyTreeNode) customTree.getRoot();
		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();

		// ���ݸ��ڵ���Ϣ����ȫѡ��ȫ��δѡ���
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}

		getSelectNode(myTreeNode, lstResult);
		return lstResult;
	}

	/**
	 * �õ�ѡ�нڵ�
	 * 
	 * @param customTree
	 */
	public static MyTreeNode[] getSelectNode(CustomTree customTree) {
		ArrayList lstResult = new ArrayList();
		// �õ����ڵ���Ϣ
		MyTreeNode myTreeNode = (MyTreeNode) customTree.getRoot();
		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();

		// ���ݸ��ڵ���Ϣ����ȫѡ��ȫ��δѡ���
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
	 * ʹ�õݹ飬����һ�ڵ�õ�ѡ���ӽڵ�ı���
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
	 * ˢ�������ֵ���Ϣ��������ɾ,��ʱ
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
	 * ȡDataSet��ĳһ�ֶε�ֵ���List
	 * 
	 * @param ds
	 * @param sFieldName
	 * @return
	 * @throws Exception
	 */
	public static List getFieldValueOrgList(DataSet ds, String sFieldName)
			throws Exception {
		// ��֯��List
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
	 * ����codeֵ�õ�IDֵ
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
	 * �õ�ѡ�е�ĩ���ڵ����ֵ������lvl_id��par_id���
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
