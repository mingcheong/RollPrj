package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.PfTreeNode;

/**
 * ��������״̬

 * 
 */
public class SetSelectTree {

	public SetSelectTree() {
	}

	/**
	 * ��������״̬��Ϊ��ѡ��״̬��������ѡ�еĽڵ��Ϊ��ѡ��
	 * 
	 * @param customTree���õ���
	 */
	public static void setIsNoCheck(CustomTree customTree) {
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		if (customTree.isHasRelation()) {
			MyPfNode myPfNode = (MyPfNode) root.getUserObject();
			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
				return;
			} else {
				myPfNode.setIsSelect(false);
			}
		} else {
			Enumeration enuma = root.breadthFirstEnumeration();
			while (enuma.hasMoreElements()) {
				MyTreeNode node = (MyTreeNode) enuma.nextElement();
				PfTreeNode pNode = (PfTreeNode) node.getUserObject();
				pNode.setIsSelect(false);
				((DefaultTreeModel) customTree.getModel()).nodeChanged(node);
			}
		}
		customTree.updateUI();
	}

	/**
	 * �������Ĳ��ֽڵ�Ϊѡ��״̬
	 * 
	 * @param customTree
	 * @param sID,����Ϊѡ��״̬������ID����
	 */
	public static void setIsCheck(CustomTree customTree, String[] sID) {
		setIsCheck(customTree, sID, true);
	}

	/**
	 * �������Ĳ��ֽڵ�Ϊѡ��״̬
	 * 
	 * @param customTree
	 * @param sID,����Ϊѡ��״̬������ID����
	 */
	public static void setIsCheck(CustomTree customTree, List sID) {
		SetSelectTree.setIsNoCheck(customTree);
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		while (enuma.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enuma.nextElement();
			if (node != root) {
				PfTreeNode pNode = (PfTreeNode) node.getUserObject();
				if (sID != null) {
					for (int i = 0; i < sID.size(); i++) {
						if (sID.get(i).toString().equals(
								pNode.getValue().toString())) {
							pNode.setIsSelect(true);
							((DefaultTreeModel) customTree.getModel())
									.nodeChanged(node);
							break;
						}
					}
				}
			}
		}
		customTree.updateUI();
	}

	/**
	 * ����������Ŀ���շ���Ŀ��Ӧ��ϵ�����շ���Ŀ�ڵ�ѡ��״̬
	 * 
	 * @param customTree
	 *            �շ���Ŀ��
	 * @param dsCode
	 *            ������Ŀ���շ���Ŀ��Ӧ��ϵDataSet
	 * @throws Exception
	 */
	public static void setIsCheck(CustomTree customTree, DataSet dsCode,
			String sFieldName) throws Exception {
		SetSelectTree.setIsNoCheck(customTree);
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		MyTreeNode node = null;
		PfTreeNode pNode = null;
		String sTollCode = null;
		while (enuma.hasMoreElements()) {
			node = (MyTreeNode) enuma.nextElement();
			if (node == root) {
				continue;
			}
			pNode = (PfTreeNode) node.getUserObject();
			dsCode.beforeFirst();
			while (dsCode.next()) {
				sTollCode = dsCode.fieldByName(sFieldName).getString();
				if (sTollCode.equals(node.sortKeyValue())) {
					pNode.setIsSelect(true);
					((DefaultTreeModel) customTree.getModel())
							.nodeChanged(node);
					break;
				}
			}
		}
		customTree.updateUI();
	}

	/**
	 * ��������״̬��Ϊ��ѡ��״̬��������ѡ�еĽڵ��Ϊ��ѡ��
	 * 
	 * @param customTree���õ���
	 */
	public static void setCheckState(CustomTree customTree, boolean bState) {
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		while (enuma.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enuma.nextElement();
			if (node != root) {
				PfTreeNode pNode = (PfTreeNode) node.getUserObject();
				pNode.setIsSelect(bState);
			}
		}
		customTree.updateUI();
	}

	/**
	 * �������Ĳ��ֽڵ�Ϊѡ��״̬
	 * 
	 * @param customTree
	 * @param sID,����Ϊѡ��״̬������ID����
	 * @param ds
	 * @param lvlFieldName
	 * @throws Exception
	 * 
	 */
	public static String setIsCheck(CustomTree customTree, String[] sID,
			String fieldName, String lvlFieldName) throws Exception {
		DataSet ds = customTree.getDataSet();
		String bookmark = ds.toogleBookmark();
		// �������ʽ���Դ
		String notExistPfsCode = "";
		if (sID != null) {
			for (int i = 0; i < sID.length; i++) {
				String lvlId = null;
				if (ds.locate(fieldName, sID[i])) {
					lvlId = ds.fieldByName(lvlFieldName).getString();
				} else {
					if (!Common.isNullStr(notExistPfsCode)) {
						notExistPfsCode += ",";
					}
					notExistPfsCode += sID[i];
					continue;
				}
				MyTreeNode root = (MyTreeNode) customTree.getRoot();
				Enumeration enuma = root.breadthFirstEnumeration();
				while (enuma.hasMoreElements()) {
					MyTreeNode node = (MyTreeNode) enuma.nextElement();
					if (node != root) {
						MyPfNode pNode = (MyPfNode) node.getUserObject();
						if (lvlId.equals(pNode.getValue().toString())) {
							pNode.setIsSelect(true);
							((DefaultTreeModel) customTree.getModel())
									.nodeChanged(node);
						}
					}
				}
			}
		}
		customTree.updateUI();
		ds.gotoBookmark(bookmark);
		return notExistPfsCode;
	}

	/**
	 * �������Ĳ��ֽڵ�Ϊѡ��״̬
	 * 
	 * @param customTree
	 * @param sID,����Ϊѡ��״̬������ID����
	 */
	public static void setIsCheck(CustomTree customTree, String[] sID,
			boolean state) {
		SetSelectTree.setCheckState(customTree, !state);
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		while (enuma.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enuma.nextElement();
			if (node != root) {
				MyPfNode pNode = (MyPfNode) node.getUserObject();
				if (sID != null) {
					for (int i = 0; i < sID.length; i++) {
						if (sID[i].equals(pNode.getValue().toString())
								&& pNode.getIsLeaf()) {
							pNode.setIsSelect(state);
							((DefaultTreeModel) customTree.getModel())
									.nodeChanged(node);
							break;
						}
					}
				}
			}
		}
		customTree.updateUI();
	}

	/**
	 * �������Ĳ��ֽڵ�Ϊѡ��״̬
	 * 
	 * @param customTree
	 * @param sID,����Ϊѡ��״̬������ID����
	 */
	public static void setIsCheck(CustomTree customTree, String[] sID,
			boolean state, boolean isLeaf) {
		SetSelectTree.setCheckState(customTree, !state);
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		while (enuma.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enuma.nextElement();
			if (node != root) {
				MyPfNode pNode = (MyPfNode) node.getUserObject();
				if (sID != null) {
					for (int i = 0; i < sID.length; i++) {
						if (sID[i].equals(pNode.getValue().toString())) {
							if (isLeaf && !pNode.getIsLeaf()) {
								continue;
							}
							pNode.setIsSelect(state);
							((DefaultTreeModel) customTree.getModel())
									.nodeChanged(node);
							break;
						}
					}
				}
			}
		}
		customTree.updateUI();
	}
}
