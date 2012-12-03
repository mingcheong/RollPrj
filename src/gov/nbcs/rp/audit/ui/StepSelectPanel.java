/**
 * @# StepSelectPanel.java    <文件名>
 */
package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.Common;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.border.BevelBorder;

import com.foundercy.pf.control.FPanel;

public class StepSelectPanel extends FPanel {

	private static final long serialVersionUID = -4437298799538344748L;

	private List lstNodePanels;

	private boolean isCanBack = true;// 记录是否可以退回

	/** 当前步骤 */
	// private int step; //
	/** 流程 信息 */
	private List lstStep;

	private Map nowStep;

	public StepSelectPanel(int step, List lstStep, Map mapStepNow,
			boolean isCanBack) {
		// this.step = step;
		this.lstStep = lstStep;
		this.isCanBack = isCanBack;
		this.nowStep = mapStepNow;
		init();
	}

	private void init() {
		FlowLayout fout = new FlowLayout(FlowLayout.LEFT);
		this.setLayout(fout);

		int iCount = lstStep.size();
		boolean befNode = true;// 记录是不是前面的节点
		lstNodePanels = new ArrayList();
		int stepnow = Integer.parseInt(Common.nonNullStr(nowStep
				.get(IPrjAudit.PrjAuditTable.audit_state)));
		int isEnd = Integer.parseInt(Common.nonNullStr(nowStep
				.get(IPrjAudit.PrjAuditTable.AUDIT_ISEND)));
		int isBack = Integer.parseInt(Common.nonNullStr(nowStep
				.get(IPrjAudit.PrjAuditTable.is_back)));
		if (isEnd == 1) {
			stepnow = stepnow + 1;
		}
		if ((stepnow == 0) && (isBack == 0)) {
			stepnow = 1;
		}
		for (int i = 0; i < iCount; i++) {
			Map map = (Map) lstStep.get(i);
			befNode = Integer.parseInt(Common.nonNullStr(map
					.get(IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_ID
							.toLowerCase()))) < stepnow;
			String text = Common.nonNullStr(map
					.get(IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_NAME
							.toLowerCase()));
			StepNodePanel aPnl = new StepNodePanel(
					text,
					befNode & isCanBack,
					i == (iCount - 1),
					stepnow == Integer
							.parseInt(Common
									.nonNullStr(map
											.get(IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_ID
													.toLowerCase()))),
					Integer
							.parseInt(Common
									.nonNullStr(map
											.get(IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_ID
													.toLowerCase()))));
			lstNodePanels.add(aPnl);
			this.add(aPnl);
		}
		this.setPreferredSize(new Dimension(600, 150));
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

	public void hasSelected(StepNodePanel node) {
		int iCount = lstNodePanels.size();
		for (int i = 0; i < iCount; i++) {
			StepNodePanel aPnl = (StepNodePanel) lstNodePanels.get(i);
			if (aPnl == node) {
				continue;
			}
			aPnl.setSelected(false);
		}
	}

	/**
	 * 取得选择的节点信息
	 * 
	 * @return
	 */
	public int getSelectBackNode() {
		int iCount = lstNodePanels.size();
		for (int i = 0; i < iCount; i++) {
			StepNodePanel aPnl = (StepNodePanel) lstNodePanels.get(i);
			if (aPnl.isSelected()) {
				return aPnl.getStepInfo();
			}
		}
		return -1;
	}
}
