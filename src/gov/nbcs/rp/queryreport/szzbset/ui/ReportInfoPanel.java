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
 * ���ռ���������壬�ֺ�,������
 * 
 * @author qzc
 * 
 */
public class ReportInfoPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	FButton fbtnGetInfo;

	FTextField ftxtEditRepTitleArea; // ռ������

	FTextField ftxtEditTitleFont;// ����

	FTextField ftxtEditRepTitleFontSize;// �ֺ�

	public ReportInfoPanel(String sBtnTitle) {
		FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT, 2, 0);
		this.setLayout(fLayout);

		FLabel flblEditRepTitleArea = new FLabel();
		flblEditRepTitleArea.setTitle("ռ������:");
		ftxtEditRepTitleArea = new FTextField(false);
		ftxtEditRepTitleArea.setEditable(false);

		FLabel flblEditTitleFont = new FLabel();
		flblEditTitleFont.setTitle("����:");
		ftxtEditTitleFont = new FTextField(false);
		ftxtEditTitleFont.setEditable(false);

		FLabel flblEditRepTitleFontSize = new FLabel();
		flblEditRepTitleFontSize.setTitle("�ֺ�:");
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
	 * �õ�ѡ�еı�����Ϣ
	 * 
	 * @param reportUI
	 */
	public void getCellInfo(ReportUI reportUI) {
		// ��������
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

			JOptionPane.showMessageDialog(this, "��ѡ����һ���յ�����������ѡ��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// ����
		ftxtEditTitleFont.setValue(aCell.getStyle().getFRFont().getName());
		// �ֺ�
		ftxtEditRepTitleFontSize.setValue(String.valueOf(aCell.getStyle()
				.getFRFont().getSize2D()));
	}

	/**
	 * �õ�ѡ�еı�����Ϣ
	 * 
	 * @param reportUI
	 */
	public void getCellInfo(JWorkBook jWorkBook) {
		// ��������
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

			JOptionPane.showMessageDialog(this, "��ѡ����һ���յ�����������ѡ��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// ����
		ftxtEditTitleFont.setValue(aCell.getStyle().getFRFont().getName());
		// �ֺ�
		ftxtEditRepTitleFontSize.setValue(String.valueOf(aCell.getStyle()
				.getFRFont().getSize2D()));
	}
}
