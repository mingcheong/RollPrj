/**
 * @# GeneralFilter.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import java.awt.BorderLayout;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.input.IntSpinnerPanel;
import gov.nbcs.rp.queryreport.reportsh.ui.RepDispStub;

import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;

/**
 * 功能说明:通用的条件面板，有年度，批次，数据类型
 *<P> Copyright 

 */
public class GeneralFilter extends AbastractFilterPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

//	private static final String dataTypeRef_BUDGET = "" + FlowNode.TYPE_DIV
//			+ "#基层单位+" + FlowNode.TYPE_DEPART + "#财政";

	private static final String BATCH_OTHER_YEAR = "2#二上";

	private static final String AllYesNo = "-1#全部+是#是+否#否";

	private static String BATCH_CUR_YEAR = null;

	private FComboBox cbxYear;

	private FComboBox cbxDataType;

	private FComboBox cbxBatch;

	private IntSpinnerPanel ispnTop;

	private FCheckBox chkCleanZeroColumn;

	private FCheckBox chxShowAllInfo;//明细行显示所有信息，

	int curBatch;
	int curDataType;

	private FComboBox cbxIsLegalinc;

	private FComboBox cbxEnterManaType;

	public String getFilter(String align) {
		StringBuffer sb = new StringBuffer();
		if (Common.isNullStr(align))
			sb = new StringBuffer(" set_year=" + cbxYear.getValue()
					+ " and batch_no=" + cbxBatch.getValue()
					+ " and data_type=" + cbxDataType.getValue());
		else
			sb = new StringBuffer(align + ".set_year=" + cbxYear.getValue()
					+ " and " + align + ".batch_no=" + cbxBatch.getValue()
					+ " and " + align + ".data_type=" + cbxDataType.getValue());

		String alignDot = Common.isNullStr(align) ? "" : align + ".";
		if (!"-1".equals(cbxIsLegalinc.getValue())) {
			sb.append(" and " + alignDot + "IS_LEGALINC='"
					+ cbxIsLegalinc.getValue() + "'");
		}

		if (!"-1".equals(cbxEnterManaType.getValue())) {
			sb.append(" and " + alignDot + "ENTER_MANA_TYPE='"
					+ cbxEnterManaType.getValue() + "'");
		}

		return sb.toString();
	}

	public void reset() {
		cbxYear.setValue(Global.getSetYear());
		cbxDataType.setValue("" + curDataType);
		cbxBatch.setValue("" + curBatch);
		// ispnTop.setValue(new Integer(0));
		chkCleanZeroColumn.setValue(Boolean.FALSE);
		chxShowAllInfo.setValue(Boolean.FALSE);

		cbxIsLegalinc.setValue("-1");
		cbxEnterManaType.setValue("-1");
	}

	public void init() {
		try {
			this.setTitle("一般条件选择");
			FPanel pnlBack = new FPanel();
		
			cbxYear = new FComboBox();
			cbxYear.setTitle(" 年    度");
			pnlBack.setLayout(new RowPreferedLayout(1));
			pnlBack.add(cbxYear, new TableConstraints(1, 1, true, true));
			cbxBatch = new FComboBox(" 批    次");
			pnlBack.add(cbxBatch, new TableConstraints(1, 1, true, true));
			cbxDataType = new FComboBox(" 数据类型");
			//cbxDataType.setTitleAdapting(true);
			pnlBack.add(cbxDataType, new TableConstraints(1, 1, true, true));

			// by qinj at Nov 19, 2009
			cbxIsLegalinc = new FComboBox(" 是否法定增长");
			cbxIsLegalinc.setTitleAdapting(true);
//			pnlBack.add(cbxIsLegalinc, new TableConstraints(1, 1, true, true));

			// by qinj at Nov 19, 2009
			cbxEnterManaType = new FComboBox(" 是否参公单位");
			cbxEnterManaType.setTitleAdapting(true);
//			pnlBack.add(cbxEnterManaType,
//					new TableConstraints(1, 1, true, true));

			// 显示数据的前几行 by qinj at Oct 23, 2009
			ispnTop = new IntSpinnerPanel("仅显示金额最大的前", "行");
			final SpinnerModel smFrozenColumn = new SpinnerNumberModel(0, 0,
					100, 1);
			ispnTop.setModel(smFrozenColumn);
			pnlBack.add(ispnTop, new TableConstraints(1, 1, true, true));

			// 清除所有值均为0的列 by qinj at Oct 23, 2009
			chkCleanZeroColumn = new FCheckBox();
			chkCleanZeroColumn.setTitle("清除所有值均为0的列");
			chkCleanZeroColumn.setTitleAdapting(true);
			chkCleanZeroColumn.setTitlePosition("right");
			pnlBack.add(chkCleanZeroColumn, new TableConstraints(1, 1, true,
					true));

			chxShowAllInfo = new FCheckBox();
			chxShowAllInfo.setTitle("是否列示全部信息");
			chxShowAllInfo.setTitleAdapting(true);
			chxShowAllInfo.setTitlePosition("right");
			pnlBack.add(chxShowAllInfo, new TableConstraints(1, 1, true, true));

			pnlBody.add(pnlBack, BorderLayout.NORTH);
			initSelection();
		} catch (Exception e) {
			new MessageBox("初始化界面失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	//设置为当前的数据的选择项
	private void initSelection() {
		String setYear = RepDispStub.getMethod().getYearRefString();

		//setYear = "2009#2009年+" + setYear;
		cbxYear.setRefModel(setYear);
		cbxYear.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				String setYear = (String) arg0.getNewValue();
				if (!Global.getSetYear().equals(setYear)) {
					cbxBatch.setRefModel(BATCH_OTHER_YEAR);
					cbxBatch.setValue("2");
					cbxDataType.setValue("2");
					cbxDataType.setEnabled(false);
				} else {
					cbxBatch.setRefModel(BATCH_CUR_YEAR);
					cbxBatch.setValue("" + curBatch);
					cbxDataType.setValue("" + curDataType);
					cbxDataType.setEnabled(true);
				}

			}

		});



		if (BATCH_CUR_YEAR == null) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i <= curBatch; i++) {
				sb.append(i).append("#").append(Common.GetNumberOfHz(i) + "上")
						.append("+");
			}
			BATCH_CUR_YEAR = sb.substring(0, sb.length() - 1);
		}
		cbxBatch.setRefModel(BATCH_CUR_YEAR);

		cbxIsLegalinc.setRefModel(AllYesNo);
		cbxEnterManaType.setRefModel(AllYesNo);

		//默认每一套的为当前的数据
		cbxYear.setValue(Global.getSetYear());
		cbxDataType.setValue("" + curDataType);
		cbxBatch.setValue("" + curBatch);
	}

	public int getBatchNo() {
		return Integer.parseInt(cbxBatch.getValue().toString());
	}

	public int getDataType() {
		return Integer.parseInt(cbxDataType.getValue().toString());
	}

	public boolean isCleanZeroColumn() {
		return ((Boolean) chkCleanZeroColumn.getValue()).booleanValue();
	}

	public boolean isShowAllInfo() {
		return ((Boolean) chxShowAllInfo.getValue()).booleanValue();
	}

	public int getTopCount() {
		return ((Integer) ispnTop.getValue()).intValue();
	}

}
