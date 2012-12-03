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
 * 设置收支总表设置，保存报表
 * 

 * 
 */
public class SaveReportActionListener implements ActionListener {
	SzzbSet szzbSet;

	boolean isAll = false;// 是否保存全部，即表体是否保存

	public SaveReportActionListener(SzzbSet szzbSet) {
		this.szzbSet = szzbSet;
	}

	public void actionPerformed(ActionEvent arg0) {
		// 做保存前的判断，看是保存全部的还是只是保存明细
		CellSelection cells = szzbSet.jWorkBook.getCellSelection();
		if (cells == null || cells.getColumnSpan() == 1
				|| cells.getRowSpan() == 1) {
			if (Common.isNullStr(szzbSet.getReportID())
					|| szzbSet.isRowOrColChanged()) {// 如果是新增或增删行列的则必需要选择表休
				JOptionPane.showMessageDialog(szzbSet, "请选择表体后再保存", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			isAll = false;
		} else {
			if (Common.isNullStr(szzbSet.getReportID()))
				isAll = true;
			else {
				int iOp = JOptionPane.showConfirmDialog(szzbSet, "需要保存选择的表体吗?",
						"提示...", JOptionPane.YES_NO_CANCEL_OPTION);
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
			// 得到fb_u_qr_repset表信息
			setRepsetInfo(szzbSet.dsRepset);

			String sReportIdNew = szzbSet.dsRepset.fieldByName(
					IQrBudget.REPORT_ID).getString();

			// 设置fb_u_qr_colset表的report_id
			String curBookmark = szzbSet.dsHeader.toogleBookmark();
			szzbSet.dsHeader.beforeFirst();
			while (szzbSet.dsHeader.next()) {
				szzbSet.dsHeader.setRecordState(DataSet.FOR_APPEND);// 修改状态以便其使用INSERT,则在保存前，要先删除对应信息
				szzbSet.dsHeader.fieldByName(ISzzbSet.REPORT_ID).setValue(
						sReportIdNew);
			}
			szzbSet.dsHeader.gotoBookmark(curBookmark);

			// 设置fb_u_qr_szzb
			if (szzbSet.dsSzzb == null) {
				szzbSet.dsSzzb = DataSet.create();
			} else {
				szzbSet.dsSzzb.clearAll();
			}
			// 保存选中的表体
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

			// 修改或增加节点时，刷新树节点
			szzbSet.defineReport.refreshNodeEdit(szzbSet.dsRepset, lstType,
					szzbSet.reportID);

			// 保存报表信息
			szzbSet.dsRepset.applyUpdate();
			szzbSet.dsHeader.applyUpdate();
			if (szzbSet.isRowOrColChanged()) {
				// 保存设置信息
				String sErr = szzbSet.cellPanel.saveCols();
				if (!Common.isNullStr(sErr)) {
					JOptionPane.showMessageDialog(szzbSet, sErr, "提示",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				szzbSet.setRowOrColChanged(false);
			}

			JOptionPane.showMessageDialog(szzbSet, "保存成功！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			szzbSet.setChanged(false);
			szzbSet.cellPanel.dispData(szzbSet.getReportID());

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(szzbSet, "保存报表发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 判断填写信息
	 * 
	 * @return
	 */
	private boolean judgetFillInfo() {
		if ("".equals(szzbSet.ftitPnlBaseInfo.ftxtEditRepName.getValue()
				.toString().trim())) {
			JOptionPane.showMessageDialog(szzbSet, "请填写报表名称!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (""
				.equals(szzbSet.ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleArea
						.getValue().toString().trim())) {
			JOptionPane.showMessageDialog(szzbSet, "请获表标题!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (""
				.equals(szzbSet.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
						.getValue().toString().trim())) {
			JOptionPane.showMessageDialog(szzbSet, "请获取报表表头!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if ("".equals(szzbSet.ftitPnlBaseInfo.cbxCurrencyUnit.getSelectedItem()
				.toString())) {
			JOptionPane.showMessageDialog(szzbSet, "请选择货币类型!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		List lstType = szzbSet.ftitPnlBaseInfo.reportTypeList.getSelectData();
		if (lstType.size() == 0) {
			JOptionPane.showMessageDialog(szzbSet, "请选择报表类型!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 判断表体选中的区域
		if (!checkBodyCell()) {
			return false;
		}
		return true;
	}

	private boolean checkBodyCell() {
		if (!isAll)
			return true;

		// 表体内容
		if (szzbSet.jWorkBook.getCellSelection() == null) {
			JOptionPane.showMessageDialog(szzbSet, "请选择表体区域!", "提示",
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
		// 判断列
		if (iCol != cells.getColumn()) {
			JOptionPane.showMessageDialog(szzbSet, "与表头开始列不一致，请重新选择表体区域!",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if (iColSpan != cells.getColumnSpan()) {
			JOptionPane.showMessageDialog(szzbSet, "与表头选列数不等，请重新选择表体区域!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断行
		if (iRow > cells.getRow()
				|| iRow + iRowSpan < cells.getRow() + cells.getRowSpan()) {
			JOptionPane.showMessageDialog(szzbSet,
					"请将包含表头、标题和表体的区域选择进来，请重新选择表体区域!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;

	}

	/**
	 * 得到选中区域的信息
	 * 
	 */
	private void saveSelectCellInfo(String sReportIdNew, JWorkBook jWorkBook,
			CellSelection cells, DataSet dsSzzb) throws Exception {
		Object oValue;
		CellElement cell;
		int iCol;
		int iRow;
		// int iColStart = cells.getColumn();// 将表体的位置平移到（0,0）点，以便在显示时好处理
		// int iRowStart = cells.getRow();

		// 注,添加了一个缓存，记录已添加的单元格信息,以免重复添加
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
						new Integer(2));// 此标记已不使用

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
	 * 得到fb_u_qr_repset表信息
	 * 
	 * @throws Exception
	 */
	private void setRepsetInfo(DataSet dsRepset) throws Exception {
		if (dsRepset.isEmpty()) {
			dsRepset.append();
			dsRepset.fieldByName(IQrBudget.SET_YEAR).setValue(Global.loginYear);
			dsRepset.fieldByName(IQrBudget.REPORT_TYPE).setValue(
					new Integer(50));

			dsRepset.fieldByName(IQrBudget.REPORT_SOURCE).setValue("定制");
			dsRepset.fieldByName(IQrBudget.IS_PASSVERIFY).setValue("是");
			dsRepset.fieldByName(IQrBudget.IS_ACTIVE).setValue("是");
			dsRepset.fieldByName(IQrBudget.DATA_USER).setValue(
					szzbSet.ftitPnlBaseInfo.reportUserTypeGrp.getValue());
			dsRepset.fieldByName(IQrBudget.IS_HASBATCH).setValue("是");
			dsRepset.fieldByName(IQrBudget.IS_MULTICOND).setValue("是");
			dsRepset.fieldByName(IQrBudget.IS_END).setValue("1");
			dsRepset.fieldByName(IQrBudget.TYPE_FLAG).setValue("1");

			String sLvlIdNew = SzzbSetI.getMethod()
					.getMaxCode(IQrBudget.LVL_ID);
			dsRepset.fieldByName(IQrBudget.LVL_ID).setValue(sLvlIdNew);
			// 这里可以使用即时的编号
			String sReportIdNew = SzzbSetI.getMethod().getMaxCode(
					IQrBudget.REPORT_ID);
			dsRepset.fieldByName(IQrBudget.REPORT_ID).setValue(sReportIdNew);
			dsRepset.fieldByName(IQrBudget.RG_CODE).setValue(
					Global.getCurrRegion());
			szzbSet.setReportIDForAdd(sReportIdNew);
		} else {
			dsRepset.edit();
		}

		//添加转换参数
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
