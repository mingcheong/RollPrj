/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.action;

import java.awt.event.ActionEvent;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**

 * Copyright: Copyright (c) 2011 浙江易桥
 * </p>
 * <p>
 * Company: 浙江易桥
 * </p>
 * <p>
 * CreateDate 20011-7-22
 * </p>
 * 
 * @author wuyal

 * @version 1.0
 */
 
public class DownMoveAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {
		FModulePanel modulePanel = this.getModulePanel();
		if (!(modulePanel instanceof DefineReport)) {
			return;
		}

		DefineReport defineReport = (DefineReport) modulePanel;
		CustomTree ftreReport = defineReport.getFtreReport();
		RepSetObject repSetObject = defineReport.getRepSetObject();
		DataSet dsReport = defineReport.getDsReport();
		IDefineReport definReportServ = defineReport.getDefinReportServ();

		// 得到选中节点
		MyTreeNode selectNode = ftreReport.getSelectedNode();
		if (selectNode == null || selectNode == ftreReport.getRoot()) {
			return;
		}
		MyTreeNode changeNode = (MyTreeNode) selectNode.getNextSibling();
		if (changeNode == null) {
			return;
		}

		String oneLvl = repSetObject.getLVL_ID();
		String oneId = repSetObject.getREPORT_ID();

		// 得到当前书签
		String curBookmark = dsReport.toogleBookmark();

		RepSetObject repSetObjectTmp = new RepSetObject();
		try {
			// 定位到交换节点
			dsReport.gotoBookmark(changeNode.getBookmark());
			dsReport.edit();
			BeanUtil.mapping(repSetObjectTmp, dsReport.getOriginData());
			// 设置DataSet前一节点的Lvl_id值
			dsReport.fieldByName(IQrBudget.LVL_ID).setValue(oneLvl);

			// 定位到原节点
			dsReport.gotoBookmark(curBookmark);

			String twoLvl = repSetObjectTmp.getLVL_ID();
			String twoId = repSetObjectTmp.getREPORT_ID();
			// 设置DataSet前一节点的Lvl_id值
			dsReport.fieldByName(IQrBudget.LVL_ID).setValue(twoLvl);
			dsReport.applyUpdate();
			// 根据is_end判断调整的是报表类型还是报表
			if (repSetObject.getIS_END() == 0) {
				definReportServ.changeLvlValue(oneLvl, twoLvl, oneId, twoId,
						"FB_S_PUBCODE", "LVL_ID", "CODE",
						"typeid = 'QUERYTYPE' ");
			} else {
				definReportServ.changeLvlValue(oneLvl, twoLvl, oneId, twoId,
						"FB_U_QR_REPSET", "LVL_ID", "REPORT_ID", "");
			}

			// 得到父节点
			MyTreeNode parentNOde = (MyTreeNode) selectNode.getParent();
			// 移除当前选中节点
			selectNode.removeFromParent();
			// 得到前一节点index
			int index = parentNOde.getIndex(changeNode);
			// 插入当前选中节点
			parentNOde.insert(selectNode, index + 1);
			ftreReport.updateUI();
			ftreReport.expandTo(IQrBudget.REPORT_ID, oneId);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox("发生错误，错误信息:" + e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK + MessageBox.BUTTON_CANCEL).show();
		}

	}
}
