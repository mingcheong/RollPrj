/**
 * @# GeneralFilter.java    <�ļ���>
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
 * ����˵��:ͨ�õ�������壬����ȣ����Σ���������
 *<P> Copyright 

 */
public class GeneralFilter extends AbastractFilterPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

//	private static final String dataTypeRef_BUDGET = "" + FlowNode.TYPE_DIV
//			+ "#���㵥λ+" + FlowNode.TYPE_DEPART + "#����";

	private static final String BATCH_OTHER_YEAR = "2#����";

	private static final String AllYesNo = "-1#ȫ��+��#��+��#��";

	private static String BATCH_CUR_YEAR = null;

	private FComboBox cbxYear;

	private FComboBox cbxDataType;

	private FComboBox cbxBatch;

	private IntSpinnerPanel ispnTop;

	private FCheckBox chkCleanZeroColumn;

	private FCheckBox chxShowAllInfo;//��ϸ����ʾ������Ϣ��

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
			this.setTitle("һ������ѡ��");
			FPanel pnlBack = new FPanel();
		
			cbxYear = new FComboBox();
			cbxYear.setTitle(" ��    ��");
			pnlBack.setLayout(new RowPreferedLayout(1));
			pnlBack.add(cbxYear, new TableConstraints(1, 1, true, true));
			cbxBatch = new FComboBox(" ��    ��");
			pnlBack.add(cbxBatch, new TableConstraints(1, 1, true, true));
			cbxDataType = new FComboBox(" ��������");
			//cbxDataType.setTitleAdapting(true);
			pnlBack.add(cbxDataType, new TableConstraints(1, 1, true, true));

			// by qinj at Nov 19, 2009
			cbxIsLegalinc = new FComboBox(" �Ƿ񷨶�����");
			cbxIsLegalinc.setTitleAdapting(true);
//			pnlBack.add(cbxIsLegalinc, new TableConstraints(1, 1, true, true));

			// by qinj at Nov 19, 2009
			cbxEnterManaType = new FComboBox(" �Ƿ�ι���λ");
			cbxEnterManaType.setTitleAdapting(true);
//			pnlBack.add(cbxEnterManaType,
//					new TableConstraints(1, 1, true, true));

			// ��ʾ���ݵ�ǰ���� by qinj at Oct 23, 2009
			ispnTop = new IntSpinnerPanel("����ʾ�������ǰ", "��");
			final SpinnerModel smFrozenColumn = new SpinnerNumberModel(0, 0,
					100, 1);
			ispnTop.setModel(smFrozenColumn);
			pnlBack.add(ispnTop, new TableConstraints(1, 1, true, true));

			// �������ֵ��Ϊ0���� by qinj at Oct 23, 2009
			chkCleanZeroColumn = new FCheckBox();
			chkCleanZeroColumn.setTitle("�������ֵ��Ϊ0����");
			chkCleanZeroColumn.setTitleAdapting(true);
			chkCleanZeroColumn.setTitlePosition("right");
			pnlBack.add(chkCleanZeroColumn, new TableConstraints(1, 1, true,
					true));

			chxShowAllInfo = new FCheckBox();
			chxShowAllInfo.setTitle("�Ƿ���ʾȫ����Ϣ");
			chxShowAllInfo.setTitleAdapting(true);
			chxShowAllInfo.setTitlePosition("right");
			pnlBack.add(chxShowAllInfo, new TableConstraints(1, 1, true, true));

			pnlBody.add(pnlBack, BorderLayout.NORTH);
			initSelection();
		} catch (Exception e) {
			new MessageBox("��ʼ������ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	//����Ϊ��ǰ�����ݵ�ѡ����
	private void initSelection() {
		String setYear = RepDispStub.getMethod().getYearRefString();

		//setYear = "2009#2009��+" + setYear;
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
				sb.append(i).append("#").append(Common.GetNumberOfHz(i) + "��")
						.append("+");
			}
			BATCH_CUR_YEAR = sb.substring(0, sb.length() - 1);
		}
		cbxBatch.setRefModel(BATCH_CUR_YEAR);

		cbxIsLegalinc.setRefModel(AllYesNo);
		cbxEnterManaType.setRefModel(AllYesNo);

		//Ĭ��ÿһ�׵�Ϊ��ǰ������
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
