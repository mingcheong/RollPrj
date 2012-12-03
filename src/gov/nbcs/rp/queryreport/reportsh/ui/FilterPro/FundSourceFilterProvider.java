/**
 * @# AcctFilterProvider.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import java.awt.BorderLayout;


import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;

/**
 * ����˵��:���ܿ�Ŀ�Ĺ�������
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class FundSourceFilterProvider extends AbastractFilterPanel {

	private static final long serialVersionUID = 781656912949808828L;

	private CustomTree treAcct;

	public String getFilter(String align) {
		if (isSelect()) {
			
		}
		return null;
	}

	public void init() {
		try {
			this.setTitle("���ܿ�Ŀѡ��");
			DataSet ds = AbastractFilterPanel.getSelectDs(TYPE_ACCT, LVL_3);
			treAcct = new CustomTree("���ܿ�Ŀ", ds, IPubInterface.BS_ID,
					IPubInterface.ACCT_JJ_FNAME, IPubInterface.BS_PARENT_ID,
					null, IPubInterface.ACCT_CODE, true);
			treAcct.setIsCheckBoxEnabled(true);
			FScrollPane spnlTree = new FScrollPane(treAcct);
			getBodyPanel().add(spnlTree, BorderLayout.CENTER);
		} catch (Exception e) {
			new MessageBox("��ʼ�����ܿ�Ŀѡ��ʧ��!", e.getMessage(), MessageBox.ERROR,
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

}
