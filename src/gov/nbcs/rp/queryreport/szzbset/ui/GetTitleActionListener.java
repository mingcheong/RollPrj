package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * 设置收支总表设置,获取表标题
 * 
 * @author qzc
 * 
 */
public class GetTitleActionListener implements ActionListener {
	SzzbSet szzbSet;

	ReportInfoPanel reportInfoPanel;

	public GetTitleActionListener(SzzbSet szzbSet,
			ReportInfoPanel reportInfoPanel) {
		this.szzbSet = szzbSet;
		this.reportInfoPanel = reportInfoPanel;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			int iIndex = szzbSet.jWorkBook.getWorkBook().getSelectedIndex();
			com.fr.report.Report report = szzbSet.jWorkBook.getWorkBook()
					.getReport(iIndex);

			String sTilteName = (String) report.getCellValue(szzbSet.jWorkBook
					.getCellSelection().getColumn(), szzbSet.jWorkBook
					.getCellSelection().getRow());
			// 报表名称
			if (sTilteName != null) {
				szzbSet.ftitPnlBaseInfo.ftxtEditReportTitle
						.setValue(sTilteName);
			}
			// 得到信息
			reportInfoPanel.getCellInfo(szzbSet.jWorkBook);
			szzbSet.isNeedFresh = true;
			szzbSet.setChanged(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(szzbSet, " 发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

}
