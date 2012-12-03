package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.FlowLayout;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.ui.report.ReportUI;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.fr.cell.JWorkBook;
import com.fr.report.CellElement;

/**
 * 获得占用区域，字体，字号,公共类
 * 
 * @author qzc
 * 
 */
public class ReportInfoPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	FButton fbtnGetInfo;

	FTextField ftxtEditRepTitleArea; // 占用区域

	FTextField ftxtEditTitleFont;// 字体

	FTextField ftxtEditRepTitleFontSize;// 字号

	public ReportInfoPanel(String sBtnTitle) {
		FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT, 2, 0);
		this.setLayout(fLayout);

		FLabel flblEditRepTitleArea = new FLabel();
		flblEditRepTitleArea.setTitle("占用区域:");
		ftxtEditRepTitleArea = new FTextField(false);
		ftxtEditRepTitleArea.setEditable(false);

		FLabel flblEditTitleFont = new FLabel();
		flblEditTitleFont.setTitle("字体:");
		ftxtEditTitleFont = new FTextField(false);
		ftxtEditTitleFont.setEditable(false);

		FLabel flblEditRepTitleFontSize = new FLabel();
		flblEditRepTitleFontSize.setTitle("字号:");
		ftxtEditRepTitleFontSize = new FTextField(false);
		ftxtEditRepTitleFontSize.setEditable(false);

		fbtnGetInfo = new FButton();
		fbtnGetInfo.setTitle(sBtnTitle);
		this.addControl(flblEditRepTitleArea);
		this.addControl(ftxtEditRepTitleArea);
		this.addControl(flblEditTitleFont);
		this.addControl(ftxtEditTitleFont);
		this.addControl(flblEditRepTitleFontSize);
		this.addControl(ftxtEditRepTitleFontSize);
		this.addControl(fbtnGetInfo);
		this.setBorder(null);
	}

	/**
	 * 得到选中的报表信息
	 * 
	 * @param reportUI
	 */
	public void getCellInfo(ReportUI reportUI) {
		// 标题区域
		int i = reportUI.getCellSelection().getColumn();
		int j = reportUI.getCellSelection().getRow();
		int ispan = reportUI.getCellSelection().getColumnSpan();
		int jspan = reportUI.getCellSelection().getRowSpan();
		String sColumnName = ReportUtil.translateToColumnName(i) + (j + 1)
				+ ":" + ReportUtil.translateToColumnName(i + ispan - 1)
				+ (j + jspan);
		ftxtEditRepTitleArea.setValue(sColumnName);

		CellElement aCell = reportUI.getReport().getCellElement(
				reportUI.getCellSelection().getColumn(),
				reportUI.getCellSelection().getRow());
		if (aCell == null) {

			JOptionPane.showMessageDialog(this, "您选择了一个空的区域，请重新选择!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// 字体
		ftxtEditTitleFont.setValue(aCell.getStyle().getFRFont().getName());
		// 字号
		ftxtEditRepTitleFontSize.setValue(String.valueOf(aCell.getStyle()
				.getFRFont().getSize2D()));
	}

	/**
	 * 得到选中的报表信息
	 * 
	 * @param reportUI
	 */
	public void getCellInfo(JWorkBook jWorkBook) {
		// 标题区域
		int i = jWorkBook.getCellSelection().getColumn();
		int j = jWorkBook.getCellSelection().getRow();
		int ispan = jWorkBook.getCellSelection().getColumnSpan();
		int jspan = jWorkBook.getCellSelection().getRowSpan();
		String sColumnName = ReportUtil.translateToColumnName(i) + (j + 1)
				+ ":" + ReportUtil.translateToColumnName(i + ispan - 1)
				+ (j + jspan);
		ftxtEditRepTitleArea.setValue(sColumnName);

		CellElement aCell = jWorkBook.getReport().getCellElement(
				jWorkBook.getCellSelection().getColumn(),
				jWorkBook.getCellSelection().getRow());
		if (aCell == null) {

			JOptionPane.showMessageDialog(this, "您选择了一个空的区域，请重新选择!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// 字体
		ftxtEditTitleFont.setValue(aCell.getStyle().getFRFont().getName());
		// 字号
		ftxtEditRepTitleFontSize.setValue(String.valueOf(aCell.getStyle()
				.getFRFont().getSize2D()));
	}
}
