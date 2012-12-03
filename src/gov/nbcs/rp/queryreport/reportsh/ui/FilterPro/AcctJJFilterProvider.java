/**
 * @# AcctJJFilterProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import java.awt.BorderLayout;


import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;

/**
 * 功能说明:经济科目条件的选择
 *<P> Copyright 
 * <P>All rights reserved.


 */
public class AcctJJFilterProvider extends AbastractFilterPanel {

	private static final long serialVersionUID = -5492450968465258893L;

	private CustomTree treAcctJJ;

//	public String getFilter(String align) {
//		if (isSelect()) {
//			try {
//				String field = "acct_code_jj";
//				String acctCodes = BaseUtil.getSelectFieldLeafOnTree(treAcctJJ,
//						field);
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
			this.setTitle("经济科目选择");
			DataSet ds = AbastractFilterPanel.getSelectDs(TYPE_ACCT_JJ, LVL_3);
			treAcctJJ = new CustomTree("经济科目", ds, IPubInterface.BSI_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BSI_PARENT_ID,
					null, IPubInterface.ACCT_CODE_JJ, true);
			treAcctJJ.setIsCheckBoxEnabled(true);
			FScrollPane spnlTree = new FScrollPane(treAcctJJ);
			getBodyPanel().add(spnlTree, BorderLayout.CENTER);
		} catch (Exception e) {
			new MessageBox("初始化经济科目选择失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	public void reset() {
		treAcctJJ.cancelSelected(false);
		treAcctJJ.repaint();
	}

	public boolean isSelect() {
		return treAcctJJ.getSelectedNodeCount(true) > 0;
	}

	public String getFilter(String align) {
		// TODO Auto-generated method stub
		return null;
	}

}
