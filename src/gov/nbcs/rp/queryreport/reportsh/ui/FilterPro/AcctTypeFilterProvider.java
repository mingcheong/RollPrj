/**
 * @# FBTypeFilterProvider.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

/**
 * ����˵��:Ԥ����������ѡ��
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
//		this.setTitle("��Ŀ����ѡ��");
//		try {
//			DataSet ds = AbastractFilterPanel.getAcctTypeDs();
//
//			treType = new CustomTree("��Ŀ����", ds, IBasInputTable.LVL_ID,
//					IBasInputTable.FIELD_CNAME, null, UntPub.lvlRule,
//					IBasInputTable.LVL_ID, true);
//			treType.setIsCheckBoxEnabled(true);
//			treType.expandAll();
//			FScrollPane spnl = new FScrollPane(treType);
//			this.getBodyPanel().add(spnl, BorderLayout.CENTER);
//		} catch (Exception e) {
//			new MessageBox("��Ŀ����ѡ������ʼ��ʧ��!", e.getMessage(), MessageBox.ERROR,
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
