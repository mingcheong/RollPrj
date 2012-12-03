/**
 * @# DivSelectPanel.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;

import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;

/**
 * 功能说明:单位的选择，可能还要扩展一其它的类型选择，作为纵向的扩展
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class VSelectPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private CustomTree treeDiv;

	public VSelectPanel() {
		try {
			init();
		} catch (Exception e) {
			new MessageBox("初始化单位树失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		DataSet dsDiv = PubInterfaceStub.getMethod().getDepToDivData(
				Global.loginYear, 7, true);

		treeDiv = new CustomTree("所有单位", dsDiv, "eid",
				IPubInterface.DIV_CODE_NAME, IPubInterface.DIV_PARENT_ID, null,
				IPubInterface.DIV_CODE, true);
		FScrollPane sclTree = new FScrollPane(treeDiv);
		if (dsDiv != null && dsDiv.getRecordCount() < 120)
			treeDiv.expandAll();
		treeDiv.setIsCheckBoxEnabled(true);
		this.setLayout(new BorderLayout());
		this.add(sclTree, BorderLayout.CENTER);

	}

	public List getSelectDiv() {
		if (treeDiv.getSelectedNodeCount(true) < 1)
			return null;
		List lstResult = new java.util.ArrayList();
		DataSet typeDs = treeDiv.getDataSet();
		HashMap mapBook = new HashMap();
		MyTreeNode nodes[] = treeDiv.getSelectedNodes(false);
		int iCount = nodes.length;
		for (int i = 0; i < iCount; i++) {
			mapBook.put(nodes[i].getBookmark(), null);
		}
		String book = typeDs.toogleBookmark();
		try {
			typeDs.beforeFirst();
			while (typeDs.next()) {
				if (mapBook.containsKey(typeDs.toogleBookmark())) {
					lstResult.add(typeDs.getOriginData());
				}
			}
			return lstResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				typeDs.gotoBookmark(book);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public DataSet getHeader(String startCode, SysCodeRule cRule) {
		return null;
	}

	public String[] getFields() {
		return null;
	}

	public String check() {
		if (treeDiv.getSelectedNodeCount(true) < 1)
			return "未选择待查询单位信息！";
		return "";
	}

	public CustomTree getTreeDiv() {
		return treeDiv;
	}
	
}
