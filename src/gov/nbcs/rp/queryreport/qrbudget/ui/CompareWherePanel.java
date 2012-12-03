/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.definereport.ui.ReportHeader;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

/**
 * <p>
 * Title:对比分析界面
 * </p>
 * <p>
 * Description:对比分析界面
 * </p>
 * <p>
 
 */
public class CompareWherePanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private QrBudget qrBudget;

	private CustomComboxYear cbxCompareYearA;

	private CustomComboxBatchNo cbxCompareBatchA;

	private CustomComboxYear cbxCompareYearB;

	private CustomComboxBatchNo cbxCompareBatchB;

	public CompareWherePanel(QrBudget qrBudget, ConditionObj conditionObj) {
		this.qrBudget = qrBudget;

		JLabel lblCompareA = new JLabel();
		lblCompareA.setText("将");
		JLabel lblCompareB = new JLabel();
		lblCompareB.setText("的");
		JLabel lblCompareC = new JLabel();
		lblCompareC.setText("和");
		JLabel lblCompareD = new JLabel();
		lblCompareD.setText("的");
		JLabel lblCompareE = new JLabel();
		lblCompareE.setText("进行比较");

		DataSet dsDataType = null;
		try {
//			dsDataType = PubInterfaceStub.getMethod().getOptDataTypeAllList();
			dsDataType = null;
			// dsDataType = changeBatchName(dsDataType);

			cbxCompareYearA = new CustomComboxYear();
			cbxCompareYearA.setSelectedIndex(0);
			cbxCompareBatchA = new CustomComboxBatchNo(dsDataType);

			cbxCompareYearB = new CustomComboxYear();
			cbxCompareYearB.setSelectedIndex(1);
			cbxCompareBatchB = new CustomComboxBatchNo(dsDataType);

			FPanel fpnlMain = new FPanel();
			fpnlMain.setLayout(new FlowLayout());
			fpnlMain.add(lblCompareA, new TableConstraints(1, 1, true, false));
			fpnlMain.add(cbxCompareYearA, new TableConstraints(1, 1, true,
					false));
			fpnlMain.add(lblCompareB, new TableConstraints(1, 1, true, false));
			fpnlMain.add(cbxCompareBatchA, new TableConstraints(1, 1, true,
					false));
			fpnlMain.add(lblCompareC, new TableConstraints(1, 1, true, false));
			fpnlMain.add(cbxCompareYearB, new TableConstraints(1, 1, true,
					false));
			fpnlMain.add(lblCompareD, new TableConstraints(1, 1, true, false));
			fpnlMain.add(cbxCompareBatchB, new TableConstraints(1, 1, true,
					false));
			fpnlMain.add(lblCompareE, new TableConstraints(1, 1, true, false));
			this.setLayout(new RowPreferedLayout(1));
			this.addControl(new FLabel(),
					new TableConstraints(1, 1, true, true));
			this.addControl(fpnlMain, new TableConstraints(1, 1, true, true));
			this.addControl(new FLabel(),
					new TableConstraints(1, 1, true, true));

			cbxCompareBatchA.addItemListener(new NameItemListener(qrBudget
					.getDsReportHeader(), ReportHeader.COMPARE_1));
			cbxCompareYearA.addItemListener(new NameItemListener(qrBudget
					.getDsReportHeader(), ReportHeader.COMPARE_1));
			cbxCompareYearB.addItemListener(new NameItemListener(qrBudget
					.getDsReportHeader(), ReportHeader.COMPARE_2));
			cbxCompareBatchB.addItemListener(new NameItemListener(qrBudget
					.getDsReportHeader(), ReportHeader.COMPARE_2));
			if (conditionObj != null) {
				cbxCompareYearA.setSelectedItem(conditionObj.getOneYear());
				cbxCompareYearB.setSelectedItem(conditionObj.getTwoYear());
				cbxCompareBatchA.setValue(conditionObj.getOneBatchNo(),
						conditionObj.getOneDataType());
				cbxCompareBatchB.setValue(conditionObj.getTwoBatchNo(),
						conditionObj.getTwoDataType());
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "显示对比条件界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * 定义年份组合框
	 */
	private class CustomComboxYear extends JComboBox {

		private static final long serialVersionUID = 1L;

		public CustomComboxYear() {
			this.addItem("上年数据");
			this.addItem("本年数据");
		}

		public String getSelectYear() {
			if (this.getSelectedIndex() == 0)
				return ConditionObj.PRE_YEAR;
			else if (this.getSelectedIndex() == 1)
				return ConditionObj.CUR_YEAR;
			else
				return null;
		}

	}

	/**
	 * 定义类型组合框
	 */
	private class CustomComboxBatchNo extends JComboBox {

		private static final long serialVersionUID = 1L;

		private DataSet dsDataType;

		public CustomComboxBatchNo(DataSet dsDataType) {
			this.dsDataType = dsDataType;

			try {
				dsDataType.beforeFirst();
				while (dsDataType.next()) {
					this.addItem(dsDataType.fieldByName("Name").getString());
				}

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "显示对比条件界面发生错误，错误信息："
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}

		}

		public String getBatchNo() throws Exception {
			String sBookmark = dsDataType.toggleBookmark(this
					.getSelectedIndex());
			dsDataType.gotoBookmark(sBookmark);
			return dsDataType.fieldByName("BatchNo").getString();
		}

		public String getDataType() throws Exception {
			String sBookmark = dsDataType.toggleBookmark(this
					.getSelectedIndex());
			dsDataType.gotoBookmark(sBookmark);
			return dsDataType.fieldByName("DataType").getString();

		}

		public void setValue(String batchNo, String dataType) throws Exception {
			if (dsDataType.locate("BatchNo", batchNo, "DataType", dataType)) {
				this
						.setSelectedItem(dsDataType.fieldByName("Name")
								.getString());
			}
		}

	}

	private String getCompareNameA() {
		return cbxCompareYearA.getSelectedItem().toString().substring(0, 2)
				+ cbxCompareBatchA.getSelectedItem().toString().substring(0, 2);
	}

	private String getCompareNameB() {
		return cbxCompareYearB.getSelectedItem().toString().substring(0, 2)
				+ cbxCompareBatchB.getSelectedItem().toString().substring(0, 2);
	}

	/**
	 * 改变显示内容
	 * 
	 */
	private class NameItemListener implements ItemListener {
		private DataSet dsReportHeader;

		private String sCompareFlag;

		/**
		 * 
		 * @param sFieldFname
		 *            中文字段名称
		 * @param sCompareFlag
		 *            对比标识，对比1或对比2
		 */
		public NameItemListener(DataSet dsReportHeader, String sCompareFlag) {
			this.dsReportHeader = dsReportHeader;
			this.sCompareFlag = sCompareFlag;
		}

		public void itemStateChanged(ItemEvent arg0) {
			// 改变表头资金来源显示
			String sFieldFname = null;
			if (ReportHeader.COMPARE_1.equals(sCompareFlag))
				sFieldFname = getCompareNameA();
			else if (ReportHeader.COMPARE_2.equals(sCompareFlag))
				sFieldFname = getCompareNameB();
			else
				return;
			try {
				DataSet dsReportHeaderTmp = ReportHeaderOpe.compareReport(
						dsReportHeader, sFieldFname, sCompareFlag);
				// 刷新报表表头显示
				ReportHeaderShow.showNormalHeader(dsReportHeaderTmp, qrBudget);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 得到界面选中条件
	 * 
	 * @return
	 * @throws Exception
	 */
	public ConditionObj getConditionObj() throws Exception {
		ConditionObj conditionObj = new ConditionObj();
		conditionObj.setOneYear(cbxCompareYearA.getSelectYear());
		conditionObj.setOneBatchNo(cbxCompareBatchA.getBatchNo());
		conditionObj.setOneDataType(cbxCompareBatchA.getDataType());
		conditionObj.setTwoYear(cbxCompareYearB.getSelectYear());
		conditionObj.setTwoBatchNo(cbxCompareBatchB.getBatchNo());
		conditionObj.setTwoDataType(cbxCompareBatchB.getDataType());
		return conditionObj;
	}
}
