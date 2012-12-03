package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkBook;
import com.fr.report.CellElement;

/**
 * ������֧�ܱ����ã����汨��
 * 

 * 
 */
public class SaveReportActionListener implements ActionListener {
	SzzbSet szzbSet;

	boolean isAll = false;// �Ƿ񱣴�ȫ�����������Ƿ񱣴�

	public SaveReportActionListener(SzzbSet szzbSet) {
		this.szzbSet = szzbSet;
	}

	public void actionPerformed(ActionEvent arg0) {
		// ������ǰ���жϣ����Ǳ���ȫ���Ļ���ֻ�Ǳ�����ϸ
		CellSelection cells = szzbSet.jWorkBook.getCellSelection();
		if (cells == null || cells.getColumnSpan() == 1
				|| cells.getRowSpan() == 1) {
			if (Common.isNullStr(szzbSet.getReportID())
					|| szzbSet.isRowOrColChanged()) {// �������������ɾ���е������Ҫѡ�����
				JOptionPane.showMessageDialog(szzbSet, "��ѡ�������ٱ���", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			isAll = false;
		} else {
			if (Common.isNullStr(szzbSet.getReportID()))
				isAll = true;
			else {
				int iOp = JOptionPane.showConfirmDialog(szzbSet, "��Ҫ����ѡ��ı�����?",
						"��ʾ...", JOptionPane.YES_NO_CANCEL_OPTION);
				if (iOp == JOptionPane.YES_OPTION) {
					isAll = true;
				} else if (iOp == JOptionPane.NO_OPTION) {
					isAll = false;
				} else
					return;
			}
		}
		// -------------------------------
		if (!judgetFillInfo())
			return;
		try {
			// �õ�fb_u_qr_repset����Ϣ
			setRepsetInfo(szzbSet.dsRepset);

			String sReportIdNew = szzbSet.dsRepset.fieldByName(
					IQrBudget.REPORT_ID).getString();

			// ����fb_u_qr_colset���report_id
			String curBookmark = szzbSet.dsHeader.toogleBookmark();
			szzbSet.dsHeader.beforeFirst();
			while (szzbSet.dsHeader.next()) {
				szzbSet.dsHeader.setRecordState(DataSet.FOR_APPEND);// �޸�״̬�Ա���ʹ��INSERT,���ڱ���ǰ��Ҫ��ɾ����Ӧ��Ϣ
				szzbSet.dsHeader.fieldByName(ISzzbSet.REPORT_ID).setValue(
						sReportIdNew);
			}
			szzbSet.dsHeader.gotoBookmark(curBookmark);

			// ����fb_u_qr_szzb
			if (szzbSet.dsSzzb == null) {
				szzbSet.dsSzzb = DataSet.create();
			} else {
				szzbSet.dsSzzb.clearAll();
			}
			// ����ѡ�еı���
			List lstType = szzbSet.ftitPnlBaseInfo.reportTypeList
					.getSelectData();
			if (isAll) {
				// szzbSet.jWorkBook.getCellSelection().getColumn();
				cells = szzbSet.jWorkBook.getCellSelection();
				szzbSet.dsSzzb = DataSet.create();
				saveSelectCellInfo(sReportIdNew, szzbSet.jWorkBook, cells,
						szzbSet.dsSzzb);

				SzzbSetI.getMethod().saveReport(szzbSet.dsRepset,
						szzbSet.dsHeader, szzbSet.dsSzzb, lstType);
				szzbSet.dsSzzb.applyUpdate();
			} else
				SzzbSetI.getMethod().saveReport(szzbSet.dsRepset,
						szzbSet.dsHeader, null, lstType);

			// �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
			szzbSet.defineReport.refreshNodeEdit(szzbSet.dsRepset, lstType,
					szzbSet.reportID);

			// ���汨����Ϣ
			szzbSet.dsRepset.applyUpdate();
			szzbSet.dsHeader.applyUpdate();
			if (szzbSet.isRowOrColChanged()) {
				// ����������Ϣ
				String sErr = szzbSet.cellPanel.saveCols();
				if (!Common.isNullStr(sErr)) {
					JOptionPane.showMessageDialog(szzbSet, sErr, "��ʾ",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				szzbSet.setRowOrColChanged(false);
			}

			JOptionPane.showMessageDialog(szzbSet, "����ɹ���", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			szzbSet.setChanged(false);
			szzbSet.cellPanel.dispData(szzbSet.getReportID());

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(szzbSet, "���汨�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �ж���д��Ϣ
	 * 
	 * @return
	 */
	private boolean judgetFillInfo() {
		if ("".equals(szzbSet.ftitPnlBaseInfo.ftxtEditRepName.getValue()
				.toString().trim())) {
			JOptionPane.showMessageDialog(szzbSet, "����д��������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (""
				.equals(szzbSet.ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleArea
						.getValue().toString().trim())) {
			JOptionPane.showMessageDialog(szzbSet, "�������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (""
				.equals(szzbSet.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
						.getValue().toString().trim())) {
			JOptionPane.showMessageDialog(szzbSet, "���ȡ�����ͷ!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if ("".equals(szzbSet.ftitPnlBaseInfo.cbxCurrencyUnit.getSelectedItem()
				.toString())) {
			JOptionPane.showMessageDialog(szzbSet, "��ѡ���������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		List lstType = szzbSet.ftitPnlBaseInfo.reportTypeList.getSelectData();
		if (lstType.size() == 0) {
			JOptionPane.showMessageDialog(szzbSet, "��ѡ�񱨱�����!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// �жϱ���ѡ�е�����
		if (!checkBodyCell()) {
			return false;
		}
		return true;
	}

	private boolean checkBodyCell() {
		if (!isAll)
			return true;

		// ��������
		if (szzbSet.jWorkBook.getCellSelection() == null) {
			JOptionPane.showMessageDialog(szzbSet, "��ѡ���������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		int iCol = szzbSet.jWorkBook.getCellSelection().getColumn();
		int iRow = szzbSet.jWorkBook.getCellSelection().getRow();
		int iColSpan = szzbSet.jWorkBook.getCellSelection().getColumnSpan();
		int iRowSpan = szzbSet.jWorkBook.getCellSelection().getRowSpan();
		CellSelection cells = ReportUtil
				.translateToNumber(szzbSet.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
						.getValue().toString());
		// �ж���
		if (iCol != cells.getColumn()) {
			JOptionPane.showMessageDialog(szzbSet, "���ͷ��ʼ�в�һ�£�������ѡ���������!",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if (iColSpan != cells.getColumnSpan()) {
			JOptionPane.showMessageDialog(szzbSet, "���ͷѡ�������ȣ�������ѡ���������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж���
		if (iRow > cells.getRow()
				|| iRow + iRowSpan < cells.getRow() + cells.getRowSpan()) {
			JOptionPane.showMessageDialog(szzbSet,
					"�뽫������ͷ������ͱ��������ѡ�������������ѡ���������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;

	}

	/**
	 * �õ�ѡ���������Ϣ
	 * 
	 */
	private void saveSelectCellInfo(String sReportIdNew, JWorkBook jWorkBook,
			CellSelection cells, DataSet dsSzzb) throws Exception {
		Object oValue;
		CellElement cell;
		int iCol;
		int iRow;
		// int iColStart = cells.getColumn();// �������λ��ƽ�Ƶ���0,0���㣬�Ա�����ʾʱ�ô���
		// int iRowStart = cells.getRow();

		// ע,�����һ�����棬��¼����ӵĵ�Ԫ����Ϣ,�����ظ����
		XMLData xmlCell = new XMLData();
		for (int i = cells.getColumn(); i < cells.getColumn()
				+ cells.getColumnSpan(); i++)
			for (int j = cells.getRow(); j < cells.getRow()
					+ cells.getRowSpan(); j++) {
				cell = jWorkBook.getReport().getCellElement(i, j);
				if (cell == null) {
					cell = new CellElement(i, j, 1, 1, null);
				}

				iCol = cell.getColumn();
				iRow = cell.getRow();
				if (xmlCell.containsKey(iRow + ":" + iCol))
					continue;
				xmlCell.put(iRow + ":" + iCol, null);
				dsSzzb.append();
				dsSzzb.fieldByName(ISzzbSet.SET_YEAR)
						.setValue(Global.loginYear);
				dsSzzb.fieldByName(ISzzbSet.REPORT_ID).setValue(sReportIdNew);

				dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMN).setValue(
						new Integer(cell.getColumn()));// - iColStart
				dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMNSPAN).setValue(
						new Integer(cell.getColumnSpan()));
				dsSzzb.fieldByName(ISzzbSet.FIELD_ROW).setValue(
						new Integer(cell.getRow()));// - iRowStart
				dsSzzb.fieldByName(ISzzbSet.FIELD_ROWSPAN).setValue(
						new Integer(cell.getRowSpan()));
				dsSzzb.fieldByName(ISzzbSet.HEADERBODY_FLAG).setValue(
						new Integer(2));// �˱���Ѳ�ʹ��

				oValue = jWorkBook.getReport().getCellValue(cell.getColumn(),
						cell.getRow());
				dsSzzb.fieldByName(ISzzbSet.FIELD_VALUE).setValue(oValue);

				dsSzzb.fieldByName(ISzzbSet.FIELD_COLWIDTH).setValue(
						new Double(jWorkBook.getReport().getColumnWidth(iCol)));
				dsSzzb.fieldByName(ISzzbSet.FIELD_ROWHEIGHT).setValue(
						new Double(jWorkBook.getReport().getRowHeight(iRow)));

				dsSzzb.fieldByName(ISzzbSet.FIELD_FONT).setValue(
						cell.getStyle().getFRFont().getFontName());
				dsSzzb.fieldByName(ISzzbSet.FIELD_FONTSIZE).setValue(
						new Integer(cell.getStyle().getFRFont().getSize()));
				dsSzzb.fieldByName(ISzzbSet.FIELD_FONTSTYPE).setValue(
						new Integer(cell.getStyle().getFRFont().getStyle()));
				dsSzzb.fieldByName(ISzzbSet.HOR_ALIGNMENT).setValue(
						new Integer(cell.getStyle().getHorizontalAlignment()));
				dsSzzb.fieldByName(ISzzbSet.VER_ALIGNMENT).setValue(
						new Integer(cell.getStyle().getVerticalAlignment()));
				dsSzzb.fieldByName(IQrBudget.RG_CODE).setValue(
						Global.getCurrRegion());
			}
	}

	/**
	 * �õ�fb_u_qr_repset����Ϣ
	 * 
	 * @throws Exception
	 */
	private void setRepsetInfo(DataSet dsRepset) throws Exception {
		if (dsRepset.isEmpty()) {
			dsRepset.append();
			dsRepset.fieldByName(IQrBudget.SET_YEAR).setValue(Global.loginYear);
			dsRepset.fieldByName(IQrBudget.REPORT_TYPE).setValue(
					new Integer(50));

			dsRepset.fieldByName(IQrBudget.REPORT_SOURCE).setValue("����");
			dsRepset.fieldByName(IQrBudget.IS_PASSVERIFY).setValue("��");
			dsRepset.fieldByName(IQrBudget.IS_ACTIVE).setValue("��");
			dsRepset.fieldByName(IQrBudget.DATA_USER).setValue(
					szzbSet.ftitPnlBaseInfo.reportUserTypeGrp.getValue());
			dsRepset.fieldByName(IQrBudget.IS_HASBATCH).setValue("��");
			dsRepset.fieldByName(IQrBudget.IS_MULTICOND).setValue("��");
			dsRepset.fieldByName(IQrBudget.IS_END).setValue("1");
			dsRepset.fieldByName(IQrBudget.TYPE_FLAG).setValue("1");

			String sLvlIdNew = SzzbSetI.getMethod()
					.getMaxCode(IQrBudget.LVL_ID);
			dsRepset.fieldByName(IQrBudget.LVL_ID).setValue(sLvlIdNew);
			// �������ʹ�ü�ʱ�ı��
			String sReportIdNew = SzzbSetI.getMethod().getMaxCode(
					IQrBudget.REPORT_ID);
			dsRepset.fieldByName(IQrBudget.REPORT_ID).setValue(sReportIdNew);
			dsRepset.fieldByName(IQrBudget.RG_CODE).setValue(
					Global.getCurrRegion());
			szzbSet.setReportIDForAdd(sReportIdNew);
		} else {
			dsRepset.edit();
		}

		//���ת������
		dsRepset.fieldByName(IQrBudget.CURRENCYUNIT).setValue(
				szzbSet.ftitPnlBaseInfo.cbxCurrencyUnit.getSelectedItem());
		if(((Boolean)szzbSet.ftitPnlBaseInfo.cbxConvert.getValue()).booleanValue()) {
			dsRepset.fieldByName(ISzzbSet.CONVERT_FIELD).setValue(
					"1");
		}else
			dsRepset.fieldByName(ISzzbSet.CONVERT_FIELD).setValue(
			"");

		dsRepset.fieldByName(IQrBudget.REPORT_CNAME).setValue(
				szzbSet.ftitPnlBaseInfo.ftxtEditRepName.getValue());
		dsRepset.fieldByName(IQrBudget.TITLE).setValue(
				szzbSet.ftitPnlBaseInfo.ftxtEditReportTitle.getValue());

		dsRepset.fieldByName(IQrBudget.TITLE_AREA).setValue(
				szzbSet.ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleArea
						.getValue());
		dsRepset.fieldByName(IQrBudget.TITLE_FONT).setValue(
				szzbSet.ftitPnlBaseInfo.reportTitlePanel.ftxtEditTitleFont
						.getValue());
		dsRepset
				.fieldByName(IQrBudget.TITLE_FONTSIZE)
				.setValue(
						szzbSet.ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleFontSize
								.getValue());

		dsRepset.fieldByName(IQrBudget.COLUMN_AREA).setValue(
				szzbSet.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
						.getValue());
		dsRepset.fieldByName(IQrBudget.COLUMN_FONT).setValue(
				szzbSet.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditTitleFont
						.getValue());
		dsRepset
				.fieldByName(IQrBudget.COLUMN_FONTSIZE)
				.setValue(
						szzbSet.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleFontSize
								.getValue());
		dsRepset.fieldByName(IQrBudget.CURRENCYUNIT).setValue(
				szzbSet.ftitPnlBaseInfo.cbxCurrencyUnit.getSelectedItem());
	}
}
