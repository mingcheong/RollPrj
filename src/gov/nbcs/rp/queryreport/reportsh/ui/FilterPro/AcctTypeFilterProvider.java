/**
 * @# FBTypeFilterProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

/**
 * 功能说明:预算类型条件选择
 *<P> Copyright 

 */
public class AcctTypeFilterProvider extends AbastractFilterPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7035596791151187266L;

	private CustomTree treType;

	public static final String fieldName = "acct_type_code";

	public String getFilter(String align) {

		if (isSelect()) {
			MyTreeNode[] nodes = treType.getSelectedNodes(true);
			int iCount = nodes.length;
			StringBuffer sb = new StringBuffer("(");
			for (int i = 0; i < iCount; i++) {
				sb.append(fieldName).append("='").append(
						nodes[i].sortKeyValue()).append("' or ");
			}
			return sb.substring(0, sb.length() - 3) + ")";
		}
		return "";
	}

	public void init() {
//		this.setTitle("科目属性选择");
//		try {
//			DataSet ds = AbastractFilterPanel.getAcctTypeDs();
//
//			treType = new CustomTree("科目属性", ds, IBasInputTable.LVL_ID,
//					IBasInputTable.FIELD_CNAME, null, UntPub.lvlRule,
//					IBasInputTable.LVL_ID, true);
//			treType.setIsCheckBoxEnabled(true);
//			treType.expandAll();
//			FScrollPane spnl = new FScrollPane(treType);
//			this.getBodyPanel().add(spnl, BorderLayout.CENTER);
//		} catch (Exception e) {
//			new MessageBox("科目属性选择界面初始化失败!", e.getMessage(), MessageBox.ERROR,
//					MessageBox.BUTTON_OK).show();
//			e.printStackTrace();
//		}

	}

	public void reset() {
		treType.clearSelection();
		try {
			treType.reset();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		treType.expandAll();
	}

	public boolean isSelect() {
		if (treType.getSelectedNodeCount(true) < 1)
			return false;
		return true;
	}

}
