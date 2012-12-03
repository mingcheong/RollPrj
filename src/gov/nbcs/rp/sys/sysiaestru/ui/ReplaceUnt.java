package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

/**
 * 修改编码，需修改lvl_id,par_id,Fname公共单元

 * 
 */
public class ReplaceUnt {

	/**
	 * 得到选中树节点及其子节点的bookmark;
	 * 
	 * @param customTree
	 * @return
	 */

	public List getSelectTreeNodeBookmark(CustomTree customTree) {
		List lstBookmark = new ArrayList();
		MyTreeNode myTreeNode = customTree.getSelectedNode();
		Enumeration allNodes = myTreeNode.breadthFirstEnumeration();
		while (allNodes.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) allNodes.nextElement();
			// if (node == myTreeNode)
			// continue;
			lstBookmark.add(node.getBookmark());
		}
		return lstBookmark;
	}

	/**
	 * 替换lvl_id,par_id,只改变子节点的lvl_id
	 * 
	 * @param lstBookmark
	 * @param dataSet
	 * @param sLvlId,选中节点的lvl_id
	 * @param sOldLvlId,选中节点的oldlvlId
	 * @param lvlIdRule
	 * @throws Exception
	 */
	public void ReplaceLvlPar(List lstBookmark, DataSet dataSet, String sLvlId,
			String sOldLvlId, SysCodeRule lvlIdRule) throws Exception {
		String sBookmark = dataSet.toogleBookmark();
		int idx = sOldLvlId.length();
		for (int i = 0; i < lstBookmark.size(); i++) {
			dataSet.gotoBookmark(lstBookmark.get(i).toString());
			String sLvlIdTmp = dataSet.fieldByName("lvl_id").getString();
			String slvlIdNew = sLvlId + sLvlIdTmp.substring(idx);
			String sParIdNew = lvlIdRule.previous(slvlIdNew); // 获得码本级编码的上级编
			if (sParIdNew == null)
				sParIdNew = "";
			dataSet.fieldByName("lvl_id").setValue(slvlIdNew);
			dataSet.fieldByName("Par_id").setValue(sParIdNew);
			String sName = dataSet.fieldByName("name").getString();
			sName = slvlIdNew + sName.substring(sLvlIdTmp.length());
			dataSet.fieldByName("name").setValue(sName);
		}
		dataSet.gotoBookmark(sBookmark);
	}

	public void ReplaceFname(List lstBookmark, DataSet dataSet, String sFName,
			String sOldFName, String sFieldName) throws Exception {
		String sBookmark = dataSet.toogleBookmark();
		int idx = sOldFName.length();
		for (int i = 0; i < lstBookmark.size(); i++) {
			dataSet.gotoBookmark(lstBookmark.get(i).toString());
			String sPfsName = dataSet.fieldByName(sFieldName).getString();
			String slvlIdNew = sFName + sPfsName.substring(idx);
			dataSet.fieldByName(sFieldName).setValue(slvlIdNew);
		}
		dataSet.gotoBookmark(sBookmark);
	}

	/**
	 * 删除节点
	 * 
	 * @param sNewParId
	 * @param customTree
	 * @param dataSet
	 * @return
	 * @throws Exception
	 */
	public List getParNode(String sNewParId, CustomTree customTree,
			DataSet dataSet) throws Exception {
		String sBookmark = dataSet.toogleBookmark();
		List lstBookmark = new ArrayList();
		MyTreeNode node = customTree.getSelectedNode();
		MyTreeNode nodePar = (MyTreeNode) node.getParent();
		while (nodePar != customTree.getRoot() && nodePar != null) {
			if (nodePar.getChildCount() == 1) {
				nodePar.getUserObject();
				dataSet.gotoBookmark(nodePar.getBookmark());
				if (!sNewParId
						.equals(dataSet.fieldByName("lvl_id").getString()))
					lstBookmark.add(nodePar.getBookmark());
				else
					break;
			} else
				break;
			node = nodePar;
			nodePar = (MyTreeNode) node.getParent();
		}
		dataSet.gotoBookmark(sBookmark);
		return lstBookmark;
	}

	public void delParNode(List lstBookmark, DataSet dataSet) throws Exception {
		String sBookmark = dataSet.toogleBookmark();
		for (int i = 0; i < lstBookmark.size(); i++) {
			dataSet.gotoBookmark(lstBookmark.get(i).toString());
			dataSet.setRecordState(DataSet.FOR_DELETE);
		}
		dataSet.gotoBookmark(sBookmark);
	}

	/**
	 * 根据父节点的lvl_id,取得需要的节点值
	 * 
	 * @param lvlCode,父节点的lvl_id
	 * @param sFieldName
	 * @param dataSet
	 * @return
	 * @throws Exception
	 */
	public List getParNodeInfo(String lvlId, String[] sFieldName,
			DataSet dataSet) throws Exception {
		return getParNodeInfo(lvlId, sFieldName, dataSet, "lvl_id");
	}

	/**
	 * 根据父节点的lvl_id,取得需要的节点值
	 * 
	 * @param lvlCode,父节点的lvl_id
	 * @param sFieldName
	 * @param dataSet
	 * @return
	 * @throws Exception
	 */
	public List getParNodeInfo(String lvlId, String[] sFieldName,
			DataSet dataSet, String filedEname) throws Exception {
		String sBookmark = dataSet.toogleBookmark();
		if (dataSet.locate(filedEname, lvlId)) {
			List lstValue = new ArrayList();
			for (int i = 0; i < sFieldName.length; i++) {
				if (dataSet.fieldByName(sFieldName[i]).getValue() != null)
					lstValue.add(dataSet.fieldByName(sFieldName[i]).getValue()
							.toString());
				else
					lstValue.add("");
			}
			dataSet.gotoBookmark(sBookmark);
			return lstValue;
		}
		dataSet.gotoBookmark(sBookmark);
		return null;
	}

	// 判断节点不能直接改为下级节点，造成数不完整
	public boolean checkCode(String sNewLvlId, String sOldLvlId,
			SysCodeRule sysCodeRule) {
		if (sysCodeRule.levelOf(sNewLvlId) <= sysCodeRule.levelOf(sOldLvlId))
			return true;
		String sLvlIdTmp = sNewLvlId;
		while (sLvlIdTmp != null) {
			if (sLvlIdTmp.equals(sOldLvlId))
				return false;
			sLvlIdTmp = sysCodeRule.previous(sLvlIdTmp);
		}
		return true;
	}
}
