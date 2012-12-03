package gov.nbcs.rp.prjset.auditset.action;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

import java.util.Arrays;
import java.util.Enumeration;

import com.foundercy.pf.control.PfTreeNode;

public class PrjAuditSetAction {
	public String[] getSelElement(CustomTree tree, boolean isSel) {
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
		int i = 0;
		String[] strs = null;
		if (isSel) {
			strs = new String[tree.getSelectedNodes(true).length];
		}else{
			strs = new String[tree.getNodeCount(true, false)-tree.getSelectedNodes(true).length];
		}
		while (enumeration.hasMoreElements()) { // 开始遍历
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (isSel) {
				if (pNode.getIsSelect()) {
					// 如果该节点为选中状态，则把它加入list中
					strs[i] = (pNode.getValue());
					i++;
				}
			} else {
				if (!pNode.getIsSelect()) {
					// 如果该节点为选中状态，则把它加入list中
					strs[i] = (pNode.getValue());
					i++;
				}
			}
		}
		return strs;
	}
	
	public String[] getSelElementContent(CustomTree tree) {
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
		int i = 0;
		String[] strs = new String[tree.getSelectedNodes(true).length];
		while (enumeration.hasMoreElements()) { // 开始遍历
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (pNode.getIsSelect()) {
				// 如果该节点为选中状态，则把它加入list中
				strs[i] = (pNode.getShowContent()).substring(pNode
						.getShowContent().indexOf("]") + 1);
				i++;
			}
		}
		return strs;
	}

	public void setSelElement(CustomTree tree, String str) {
		if (Common.isNullStr(str)) {
			return;
		}
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
		if (Common.isNullStr(str)) {
			while (enumeration.hasMoreElements()) { // 开始遍历
				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
				if (!node.isLeaf()) {
					continue;
				}
				PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
				pNode.setIsSelect(false); // 设置该节点为未选取状态
			}
			return;
		}
		String[] ele = str.split(";\n");
		Arrays.sort(ele);
		while (enumeration.hasMoreElements()) { // 开始遍历
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (Arrays.binarySearch(ele, pNode.getValue()) >= 0) {
				pNode.setIsSelect(true);
			} else {
				pNode.setIsSelect(false);
			}
		}
	}

	public void setSelElement(CustomTree tree, String[] str) {
		if (str == null) {
			return;
		}
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
		if (str.length == 0) {
			while (enumeration.hasMoreElements()) { // 开始遍历
				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
				if (!node.isLeaf()) {
					continue;
				}
				PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
				pNode.setIsSelect(false); // 设置该节点为未选取状态
			}
			return;
		}
		Arrays.sort(str);
		while (enumeration.hasMoreElements()) { // 开始遍历
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (Arrays.binarySearch(str, pNode.getValue()) >= 0) {
				pNode.setIsSelect(true);
			} else {
				pNode.setIsSelect(false);
			}
		}
	}

}
