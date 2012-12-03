package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.sys.sysiaestru.ui.SetSelectTree;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

/**
 * 选择显示列

 * 
 */
public class ChoiceShowCol extends FDialog {

	private static final long serialVersionUID = 1L;

	String sReportTitle;

	DataSet dsReportHeader;

	Report report;

	ReportUI reportUI;

	CustomTree ftreReportCol;

	public ChoiceShowCol(Frame frame, String sReportTitle,
			DataSet dsReportHeader, Report report, ReportUI reportUI)
			throws Exception {
		super(frame);
		this.setSize(400, 360);
		this.setResizable(false);
		this.dispose();
		this.setModal(true);
		this.setTitle("选择显示列");
		this.sReportTitle = sReportTitle;
		this.dsReportHeader = dsReportHeader;
		this.report = report;
		this.reportUI = reportUI;
		init();
		setTreeCheck();
	}

	private void init() throws Exception {
		FLabel flblTitle = new FLabel();
		flblTitle.setTitle("可选择列");
		flblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		// 显示树
		DataSet dsReportHeaderTmp = DataSetUtil.filterBy(dsReportHeader,
				IQrBudget.IS_LEAF + "==1");
		ftreReportCol = new CustomTree(sReportTitle, dsReportHeaderTmp,
				IQrBudget.FIELD_CODE, IQrBudget.FIELD_FNAME, "", null);
		ftreReportCol.setSortKey(IQrBudget.FIELD_CODE);
		ftreReportCol.setIsCheck(true);
		ftreReportCol.setIsCheckBoxEnabled(true);
		FScrollPane fScroll = new FScrollPane(ftreReportCol);
		FPanel fPanelReportCol = new FPanel();
		fPanelReportCol.setTopInset(5);
		fPanelReportCol.setLeftInset(5);
		fPanelReportCol.setRightInset(5);
		fPanelReportCol.setLayout(new RowPreferedLayout(1));
		fPanelReportCol.addControl(fScroll);

		// 按钮Panel
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		FButton okButton = new FButton("okButton", "确定");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					setColWidth();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(ChoiceShowCol.this,
							"点击\"确定\"按钮发生错误，错误信息:" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
				ChoiceShowCol.this.dispose();
			}
		});

		FButton cancelButton = new FButton("cancelButton", "取消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChoiceShowCol.this.dispose();
			}
		});
		choosePanel.addControl(okButton);
		choosePanel.addControl(cancelButton);

		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setRowHeight(15);
		this.getContentPane().setLayout(rLay);
		this.getContentPane().add(flblTitle,
				new TableConstraints(1, 1, false, true));
		this.getContentPane().add(fPanelReportCol,
				new TableConstraints(1, 1, true, true));
		this.getContentPane().add(choosePanel,
				new TableConstraints(2, 1, false, true));
	}

	/**
	 * 设置树的状态（选中或不选中)
	 * 
	 * @throws Exception
	 * 
	 */
	private void setTreeCheck() throws Exception {
		List sID = new ArrayList();
		List lstFields = report.getReportHeader().getFields();
		for (int i = 0; i < lstFields.size(); i++) {
			if (report.getColumnWidth(i) > 0)
				sID.add(lstFields.get(i));
		}
		SetSelectTree.setIsCheck(ftreReportCol, sID);
	}

	/**
	 * 根据节点选中情况设置列宽
	 * 
	 * @throws Exception
	 * 
	 */
	private void setColWidth() throws Exception {
		MyTreeNode root = (MyTreeNode) ftreReportCol.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		List lstFields = report.getReportHeader().getFields();
		int iCol;
		String sValue;
		while (enuma.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enuma.nextElement();
			if (node == root) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject();
			sValue = pNode.getValue();
			iCol = lstFields.indexOf(sValue);
			if (pNode.getIsSelect()) {
				if (!dsReportHeader.locate(IQrBudget.FIELD_CODE, sValue)
						&& dsReportHeader
								.fieldByName(IQrBudget.FIELD_DISPWIDTH)
								.getValue() != null)
					report.setColumnWidth(iCol, 75);
				else {
					//mod by xxl 20090823有些隐藏列的宽度设置的本来就是零，所以如果是零则要给一个其它的宽度
					double colWidth = dsReportHeader.fieldByName(
							IQrBudget.FIELD_DISPWIDTH).getDouble();

					if (colWidth < 3) {
						//根据字符长度计算宽度
						String title = dsReportHeader.fieldByName(
								IQrBudget.FIELD_CNAME).getString();
						Font font = new Font("宋体", Font.PLAIN, 13);
						FontMetrics fm = Toolkit.getDefaultToolkit()
								.getFontMetrics(font);
						colWidth = fm.stringWidth(title);
						if (colWidth > 500)//过长保护
							colWidth = 500;

					}
					report.setColumnWidth(iCol, colWidth);
				}
			} else {
				report.setColumnWidth(iCol, 0);
			}
		}
		reportUI.repaint();
	}
}
