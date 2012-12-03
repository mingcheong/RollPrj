/**
 * @# AcctFilterProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import java.awt.BorderLayout;


import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;

/**
 * 功能说明:功能科目的过滤条件
 *<P> Copyright 
 * <P>All rights reserved.
 2
 */
public class AcctFilterProvider extends AbastractFilterPanel {

	private static final long serialVersionUID = 781656912949808828L;

	private CustomTree treAcct;

//	public String getFilter(String align) {
//		if (isSelect()) {
//			try {
////				String field = "acct_code";
////				String acctCodes = BaseUtil.getSelectFieldLeafOnTree(treAcct,
////						field);
//
//				if (!Common.isNullStr(align))
//					field = align + "," + field;
//				return getLikeExp(acctCodes.split(";"), field);
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	public void init() {
		try {
			this.setTitle("功能科目选择");
			DataSet ds = AbastractFilterPanel.getSelectDs(TYPE_ACCT, LVL_3);
			treAcct = new CustomTree("功能科目", ds, IPubInterface.BS_ID,
					IPubInterface.ACCT_JJ_FNAME, IPubInterface.BS_PARENT_ID,
					null, IPubInterface.ACCT_CODE, true);
			treAcct.setIsCheckBoxEnabled(true);
			FScrollPane spnlTree = new FScrollPane(treAcct);
			getBodyPanel().add(spnlTree, BorderLayout.CENTER);
		} catch (Exception e) {
			new MessageBox("初始化功能科目选择失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	public void reset() {
		treAcct.cancelSelected(false);
		treAcct.repaint();
	}

	public boolean isSelect() {
		return treAcct.getSelectedNodeCount(true) > 0;
	}

	public String getFilter(String align) {
		// TODO Auto-generated method stub
		return null;
	}

}
