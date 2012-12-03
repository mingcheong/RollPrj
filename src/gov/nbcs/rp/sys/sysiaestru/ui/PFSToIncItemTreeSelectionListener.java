package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * 资金来源对应收入树SelectionListener事件
 * 

 * 
 */
public class PFSToIncItemTreeSelectionListener implements TreeSelectionListener {
	private PFSToIncItem pfsToIncItem = null;

	private DataSet dsPayOutFS = null; // 支出资金来源

	private DataSet dsIncItem = null; // 收入项目数据集

	private CustomTree ftreePayOutFS = null;// 支出资金来源树

	private CustomTree ftreeIncItem = null;// 收入项目

	public PFSToIncItemTreeSelectionListener(PFSToIncItem pfsToIncItem) {
		this.pfsToIncItem = pfsToIncItem;
		this.dsPayOutFS = pfsToIncItem.dsPayOutFS;
		this.dsIncItem = pfsToIncItem.dsIncItem;
		this.ftreePayOutFS = pfsToIncItem.ftreePayOutFS;
		this.ftreeIncItem = pfsToIncItem.ftreeIncItem;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// 控件修改按钮状态，不是叶节点，不可修改
			SetActionStatus setActionStatus = new SetActionStatus(dsPayOutFS,
					pfsToIncItem, ftreePayOutFS);
			setActionStatus.setState(false);
			// 显示收入项目
			ISysIaeStru sysIaeStruServ = SysIaeStruI.getMethod();
			if (dsPayOutFS.isEmpty())
				return;
			if (dsPayOutFS.fieldByName("end_flag").getInteger() == 1) {
				String sPfsCode = dsPayOutFS.fieldByName("pfs_code")
						.getString();
				pfsToIncItem.dsPfsToIncitem = sysIaeStruServ.getIncWithPfsCode(
						sPfsCode, Global.loginYear);
				String sID[] = null;
				if (!pfsToIncItem.dsPfsToIncitem.isEmpty()) {
					pfsToIncItem.dsPfsToIncitem.beforeFirst();
					sID = new String[pfsToIncItem.dsPfsToIncitem
							.getRecordCount()];
					int i = 0;
					pfsToIncItem.dsPfsToIncitem.beforeFirst();

					String sIncTypeCode;
					String sBookmark = dsIncItem.toogleBookmark();
					while (pfsToIncItem.dsPfsToIncitem.next()) {
						sIncTypeCode = pfsToIncItem.dsPfsToIncitem.fieldByName(
								IIncType.INCTYPE_CODE).getString();
						if (dsIncItem.locate(IIncType.INCTYPE_CODE,
								sIncTypeCode))
							sID[i] = dsIncItem.fieldByName(IIncType.LVL_ID)
									.getString();
						else
							sID[i] = "";
						i++;
					}
					dsIncItem.gotoBookmark(sBookmark);
				}
				SetSelectTree.setIsCheck(ftreeIncItem, sID);
			} else {
				SetSelectTree.setIsNoCheck(ftreeIncItem);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(pfsToIncItem,
					"显示资金来源对收入明细信息发生错误，错误信息：" + e1.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
