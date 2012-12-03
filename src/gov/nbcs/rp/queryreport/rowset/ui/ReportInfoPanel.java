/**
 * @# ReportInfoPanel.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.queryreport.definereport.ui.ReportTypeList;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.control.AbstractDataField;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.XMLData;

/**
 * ����˵��:��������Ϣ���
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 * @since java 1.4.2
 */
public class ReportInfoPanel extends FPanel {

	private XMLData xmlMain;

	private static final long serialVersionUID = -684041527660272018L;

	private FTextField txtName;

	private FTextField txtTitle;

	private IntegerSpinner jspFrozenColumn;

	private FRadioGroup rdoUser;

	private ReportTypeList reportTypeList;

	private List lstType;

	public ReportInfoPanel(XMLData mainData, List lstType) {

		this.xmlMain = mainData;
		this.lstType = lstType;
		init();
	}

	private void init() {
		txtName = new FTextField();
		txtName.setId(IQrBudget.REPORT_CNAME);
		txtName.setTitle("��������:");
		txtName.addValueChangeListener(new ValueChange(txtName));
		txtTitle = new FTextField();
		txtTitle.setId(IQrBudget.TITLE);
		txtTitle.setTitle("�������:");
		txtTitle.addValueChangeListener(new ValueChange(txtTitle));

		// --------------------
		FLabel flblFrozenColumn = new FLabel();
		flblFrozenColumn.setText("������ʾ:");
		SpinnerModel smFrozenColumn = new SpinnerNumberModel(1, 1, 10, 1);
		jspFrozenColumn = new IntegerSpinner(smFrozenColumn);
		jspFrozenColumn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				xmlMain.put(IQrBudget.FIELD_COLUMN, jspFrozenColumn.getValue());

			}

		});
		FLabel flblFrozenColumn1 = new FLabel();
		flblFrozenColumn1.setText("��");
		FPanel pnlCol = new FPanel();
		pnlCol.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlCol.add(flblFrozenColumn);
		pnlCol.add(jspFrozenColumn);
		pnlCol.add(flblFrozenColumn1);
		// ----------------------

		rdoUser = new FRadioGroup("", FRadioGroup.HORIZON);
		rdoUser.setRefModel("0#����λʹ�� +1#������ʹ��+2#������λ��ͬʹ��");
		rdoUser.setTitleVisible(false);
		rdoUser.setValue("2");
		rdoUser.addValueChangeListener(new ValueChange(rdoUser));

		FPanel pnlUser = new FPanel();
		pnlUser.setTitle("�û�����");
		pnlUser.setLayout(new BorderLayout());
		pnlUser.add(rdoUser, BorderLayout.CENTER);

		// ���������Ϣ
		FPanel fpnlReportType = new FPanel();
		fpnlReportType.setTitle("�������ͣ�");
		reportTypeList = new ReportTypeList();
		fpnlReportType.setLayout(new RowPreferedLayout(1));
		fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
				true));
		// reportTypeList.addMouseListener(new TypeValueChange());
		// reportTypeList.addKeyListener(new TypeValueChange());

		this.setLayout(new RowPreferedLayout(7));
		this.add(txtName, new TableConstraints(1, 5, false, true));
		this.add(txtTitle, new TableConstraints(1, 5, false, true));
		this.add(pnlCol, new TableConstraints(1, 5, false, true));
		this.add(pnlUser, new TableConstraints(2, 5, false, true));
		this.add(fpnlReportType, new TableConstraints(5, 5, false, true));
		this.add(new JLabel(), new TableConstraints(7, 5, false, true));

		txtName.setValue(xmlMain.get(IQrBudget.REPORT_CNAME));
		txtTitle.setValue(xmlMain.get(IQrBudget.TITLE));
		reportTypeList.setSelected(xmlMain.get(IQrBudget.REPORT_ID).toString());
		Object obj = xmlMain.get(IQrBudget.FIELD_COLUMN);
		Integer num = new Integer(1);
		if (obj != null && !"".equals(obj))
			num = new Integer(obj.toString());

		jspFrozenColumn.setValue(num);
	}

	class ValueChange implements ValueChangeListener {
		AbstractDataField aField;

		public ValueChange(AbstractDataField aField) {
			this.aField = aField;
		}

		public void valueChanged(ValueChangeEvent arg0) {
			xmlMain.put(aField.getId(), aField.getValue());

		}

	}

	/**
	 * �õ�ѡ�еı�������
	 * 
	 * @return
	 */
	public List getType() {
		return reportTypeList.getSelectData();
	}

}
