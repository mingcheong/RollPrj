/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Dialog;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.Report;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateDate 2011-9-4
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class FundSourceSetDialog extends FDialog {

	private static final long serialVersionUID = 1L;

	private ReportGuideUI reportGuideUI;

	private Report report;

	private CellElement cellElement;

	private FCheckBox fchkFundFlag;

	private FTextField ftxtName;

	private FRadioGroup frdoCompare;

	public FundSourceSetDialog(Dialog dialog) {
		super(dialog);
		this.setSize(600, 650);
		this.setTitle("表头标记设置");
		this.setModal(true);

		init();
	}

	public FundSourceSetDialog(JFrame frame) {
		this.setModal(true);
		this.setSize(300, 150);
		this.setTitle("表头标记设置");
		this.reportGuideUI = (ReportGuideUI) frame;
		report = reportGuideUI.getReport().getReport();
		// 界面
		init();
	}

	/**
	 * 界面
	 * 
	 */
	private void init() {
		CellSelection cell = reportGuideUI.getReport().getCellSelection();
		int col = cell.getColumn();
		int row = cell.getRow();
		cellElement = report.getCellElement(col, row);
		if (cellElement == null) {
			cellElement = new CellElement(col, row);
		}

		ftxtName = new FTextField("名称：");
		if (cellElement == null || cellElement.getValue() == null) {
			ftxtName.setValue("");
		} else {
			ftxtName.setValue(cellElement.getValue().toString());
		}
		ftxtName.addValueChangeListener(new NameValueChangeListener());

		fchkFundFlag = new FCheckBox("是否资金来源");
		fchkFundFlag.setTitlePosition("RIGHT");

		fchkFundFlag.addValueChangeListener(new FundFlagValueChangeListener());
		frdoCompare = new FRadioGroup("", FRadioGroup.VERTICAL);
		frdoCompare.setRefModel("0#非对比分析+1#对比分析1+2#对比分析2");
		frdoCompare.addValueChangeListener(new CompareValueChangeListener());

		FPanel fpnlMain = new FPanel();
		fpnlMain.setLayout(new RowPreferedLayout(1));
		fpnlMain.addControl(ftxtName, new TableConstraints(1, 1, false, true));
		fpnlMain.addControl(fchkFundFlag, new TableConstraints(1, 1, false,
				true));
		fpnlMain.addControl(frdoCompare,
				new TableConstraints(3, 1, false, true));
		this.getContentPane().add(fpnlMain);

		refresh();

	}

	private class CompareValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent evt) {
			if (cellElement == null) {

				cellElement.setValue(new FundSourceImpl(""));
			} else if (!(cellElement.getValue() instanceof FundSourceImpl)) {
				cellElement.setValue(new FundSourceImpl(cellElement.getValue()
						.toString()));
			}
			((FundSourceImpl) cellElement.getValue()).setCompareFlag(evt
					.getNewValue().toString());
		}
	}

	private void refresh() {
		if (cellElement != null
				&& cellElement.getValue() instanceof FundSourceImpl) {
			((JCheckBox) fchkFundFlag.getEditor())
					.setSelected(((FundSourceImpl) cellElement.getValue())
							.isReserveExecute());
			if (((FundSourceImpl) cellElement.getValue()).getCompareFlag() == null)
				frdoCompare.setValue("0");
			else
				frdoCompare.setValue(((FundSourceImpl) cellElement.getValue())
						.getCompareFlag());
		} else {
			frdoCompare.setValue("0");
		}

	}

	private class FundFlagValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent evt) {

			if (((Boolean) evt.getNewValue()).booleanValue()) {
				if (cellElement == null)
					cellElement.setValue(new FundSourceImpl(""));
				else if (!(cellElement.getValue() instanceof FundSourceImpl)) {
					cellElement.setValue(new FundSourceImpl(cellElement
							.getValue().toString()));
				}
				((FundSourceImpl) cellElement.getValue())
						.setReserveExecute(true);

			} else {
				if (cellElement.getValue() instanceof FundSourceImpl) {
					((FundSourceImpl) cellElement.getValue())
							.setReserveExecute(false);
				}
			}

		}
	}

	private class NameValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent evt) {
			if (cellElement == null)
				cellElement.setValue(new FundSourceImpl(evt.getNewValue()
						.toString()));
			else if ((cellElement.getValue() instanceof FundSourceImpl)) {
				((FundSourceImpl) cellElement.getValue()).setContent(evt
						.getNewValue().toString());
			} else {
				cellElement.setValue(new FundSourceImpl(evt.getNewValue()
						.toString()));
			}
			reportGuideUI.getReport().repaint();
		}
	}

	void populate(CellElement cellElement, Object value) {
		this.cellElement = cellElement;
		// if (value == null || !(value instanceof FundSourceImpl))
		// this.curCell = new FundSourceImpl(null);
		// else
		// this.curCell = (FundSourceImpl) value;

	}

}
