package gov.nbcs.rp.sys.besqryreport.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;

public class SelectSource extends FDialog {

	private DataSet ds;

	CustomTable table = null;

	private String sSourceValue = null;

	private String sOldSourceValue = null;

	private FTextField tfTitle;

	public SelectSource(JFrame jframe, String aOldValue) {
		super(jframe);
		try {
			this.sOldSourceValue = aOldValue;
			IBesQryReport itserv = BesStub.getMethod();
			ds = itserv.getSourceData();

			FPanel pnlBlank = new FPanel();
			FPanel pnlBase = new FPanel();
			RowPreferedLayout layBase = new RowPreferedLayout(8);
			pnlBase.setLayout(layBase);
			layBase.setColumnGap(2);
			// layBase.setRowGap( 10 );
			layBase.setColumnWidth(80);
			FLabel lbFilter = new FLabel();
			lbFilter.setText(" 定位条件");
			tfTitle = new FTextField("表名称");
			tfTitle.setProportion(0.1f);
			FButton btnOK = new FButton("btn1", "确定");
			FButton btnCancel = new FButton("btn2", "取消");
			table = new CustomTable(new String[] { "对象名称", "类别" },
					new String[] { "TNAME", "TABTYPE" }, ds, false, null);
			table.reset();
			pnlBase.addControl(lbFilter, new TableConstraints(1, 1, false,
					false));
			pnlBase.addControl(tfTitle,
					new TableConstraints(1, 6, false, false));
			pnlBase
					.addControl(table,
							new TableConstraints(17, 7, false, false));
			pnlBase.addControl(pnlBlank, new TableConstraints(1, 8, false,
					false));
			pnlBase.addControl(pnlBlank, new TableConstraints(1, 2, false,
					false));
			pnlBase.addControl(btnOK, new TableConstraints(1, 1, false, false));
			pnlBase.addControl(pnlBlank, new TableConstraints(1, 1, false,
					false));
			pnlBase.addControl(btnCancel, new TableConstraints(1, 1, false,
					false));

			pnlBase.setLeftInset(10);
			pnlBase.setRightInset(10);
			pnlBase.setTopInset(10);
			pnlBase.setBottomInset(10);
			this.setSize(600, 500);
			this.getContentPane().add(pnlBase);
			this.dispose();
			this.setTitle("查询原表选择");
			this.setModal(true);
			tfTitle.addValueChangeListener(new FilterChangeListener());
			table.getTable().getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sSourceValue = sOldSourceValue;
					setVisible(false);
				}
			});
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (ds.isEmpty()
								|| ds.bof()
								|| ds.eof()
								|| Common.isNullStr(String.valueOf(tfTitle
										.getValue())))
							sSourceValue = "";
						else
							sSourceValue = ds.fieldByName(
									IBesQryReport.sSourceTableName).getString();
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});
			table.getTable().addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						try {
							if (ds.isEmpty() || ds.bof() || ds.eof())
								sSourceValue = "";
							else
								sSourceValue = ds.fieldByName(
										IBesQryReport.sSourceTableName)
										.getString();
							setVisible(false);
						} catch (Exception ek) {
							ek.printStackTrace();
						}
					}
				}
			});
			tfTitle.setValue(aOldValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回选取的
	 */
	public String getValue() {
		return sSourceValue;
	}

	/**
	 * 
	 * 改变编辑框内容，定位到相应的记录上
	 */
	private class FilterChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent e) {
			try {
				JTable tb = new JTable();
				tb = table.getTable();
				String sValue = null;
				int idx = 0;
				String sFilterValue = (e.getNewValue() == null) ? "" : e
						.getNewValue().toString().toUpperCase();
				if (Common.isNullStr(sFilterValue))
					return;
				ds.beforeFirst();
				while (ds.next()) {
					sValue = ds.fieldByName(IBesQryReport.sSourceTableName)
							.getString();
					boolean bTrue = sValue.startsWith(sFilterValue);
					if (bTrue) {
						idx = table.bookmarkToRow(ds.toogleBookmark());
						tb.getSelectionModel().setLeadSelectionIndex(idx);
						tb.scrollRectToVisible(tb.getCellRect(idx, 1, true));
						return;
					}
				}
				ds.beforeFirst();
				while (ds.next()) {
					sValue = ds.fieldByName(IBesQryReport.sSourceTableName)
							.getString();
					int idex = sValue.indexOf(sFilterValue);
					if (idex >= 0) {
						idx = table.bookmarkToRow(ds.toogleBookmark());
						tb.getSelectionModel().setLeadSelectionIndex(idx);
						tb.scrollRectToVisible(tb.getCellRect(idx, 1, true));
						return;
					}
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}
}
