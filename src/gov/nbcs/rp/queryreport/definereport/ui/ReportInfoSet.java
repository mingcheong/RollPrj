/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomSummaryReportBasicAttr;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.szzbset.ui.SzzbSetI;

import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.reportcy.common.constants.ReportTypeConstants;
import com.foundercy.pf.reportcy.summary.object.base.SummaryReportBasicAttr;

/**
 * <p>
 * Title:���鱨�����һ����������Ϣ����)�ͷ���ҳ��
 * </p>
 * <p>
 * Description:���鱨�����һ����������Ϣ����)�ͷ���ҳ��
 * </p>
 * <p>

 */
public class ReportInfoSet extends FTitledPanel {

	private static final long serialVersionUID = 1L;

	// ��������
	private FTextField reportNameTxt = null;

	// �������
	private ReportTypeList reportTypeList;

	// �����û�����
	private FRadioGroup reportUserTypeGrp = null;

	// private FCheckBox fchkIsMoneyOp = null;

	// �Ƿ��������������ٻ���
	private FRadioGroup frdoIsMoneyOp = null;

	// // �Ƿ�����
	// private FCheckBox fchkIsMulYear = null;
	//
	// // �Ƿ��Ƿ������
	// private FCheckBox fchkIsMulRgion = null;

	// �Ƿ�����
	private FCheckBox fchkIsActice = null;

	// ������Ϣ��
	private SummaryReportBasicAttr summaryReportBasicAttr = null;

	// ����������Ϣ
	private RepSetObject repSetObject = null;

	// ����ID,���ӱ���null��"",�޸ı�������ID
	private String sReportId = null;

	// ��ƽ�����
	private ReportGuideUI reportGuideUI;

	// ��������
	private FComboBox fcbxCurrencyUnit;

	// ֧�ָ���֧���ʽ���Դ����Դ
	private FCheckBox fchkChangeSource;

	// ֧�ָ���֧���ʽ���Դ����Դ
	private FCheckBox fchkCompare;

	// �Ƿ���ʾ�ϼ���
	private FCheckBox fchkIsShowTotalRow;

	/**
	 * ���캯��
	 * 
	 * @param summaryReportBasicAttr
	 *            ������Ϣ����
	 * @param RepSetObject
	 *            ����������Ϣ
	 * @param sReportId
	 *            ����ID,���ӱ���null��"",�޸ı�������ID
	 * 
	 * 
	 */
	public ReportInfoSet(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.sReportId = reportGuideUI.sReportId;
		this.repSetObject = reportGuideUI.repSetObject;
		if (reportGuideUI.querySource != null) {
			if (reportGuideUI.querySource.getReportBasicAttr() instanceof SummaryReportBasicAttr) {
				this.summaryReportBasicAttr = (SummaryReportBasicAttr) reportGuideUI.querySource
						.getReportBasicAttr();
			}
		}
		if (summaryReportBasicAttr == null) {
			summaryReportBasicAttr = new SummaryReportBasicAttr();
			summaryReportBasicAttr
					.setReportType(ReportTypeConstants.REPORT_TYPE_8_RUNTIME_STATIC_REPORT);
		}
		// �����ʼ������
		jbInit();

		// �ж������ӱ������޸ı���
		if (!Common.isNullStr(sReportId)) {// �޸�
			// ��ʾ���������Ϣ��������
			try {
				showSummaryReportBasicAttr();
			} catch (Exception e) {
				new MessageBox(reportGuideUI, "��ʾ���������Ϣ�������ݷ������󣬴�����Ϣ:"
						+ e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
				e.printStackTrace();
			}
		} else {
			((JCheckBox) fchkIsActice.getEditor()).setSelected(true);
			((JCheckBox) fchkIsShowTotalRow.getEditor()).setSelected(true);
		}

	}

	/**
	 * �����ʼ������
	 * 
	 */
	private void jbInit() {

		// ���屨�������ı���
		reportNameTxt = new FTextField("�������ƣ�");

		// ���������Ϣ
		FPanel fpnlReportType = new FPanel();
		fpnlReportType.setTitle("�������ͣ�");
		reportTypeList = new ReportTypeList();
		fpnlReportType.setLayout(new RowPreferedLayout(1));
		fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
				true));

		// ���屨���û�����
		reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
		reportUserTypeGrp.setRefModel("0#����λʹ�� +1#������ʹ��+2#������λ��ͬʹ��");
		reportUserTypeGrp.setValue("2");
		reportUserTypeGrp.setTitleVisible(false);

		// ���屨���û��������
		FPanel reportUserTypePnl = new FPanel();
		reportUserTypePnl.setTitle("�����û�����");
		reportUserTypePnl.setLayout(new RowPreferedLayout(1));

		// �����û����͵�ѡ����뱨���û��������
		reportUserTypePnl.addControl(reportUserTypeGrp, new TableConstraints(1,
				1, true, true));

		// �Ƿ�����
		fchkIsActice = new FCheckBox("�Ƿ�����");
		fchkIsActice.setTitlePosition("RIGHT");

		fcbxCurrencyUnit = new FComboBox("�������ͣ�");
		((JComboBox) fcbxCurrencyUnit.getEditor()).setEditable(true);
		fcbxCurrencyUnit.setRefModel("#+Ԫ#Ԫ+��Ԫ#��Ԫ");
		fcbxCurrencyUnit.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				if ("��Ԫ".equals(arg0.getNewValue())) {
					frdoIsMoneyOp.setEditable(true);
					frdoIsMoneyOp.setValue("0");
				} else {
					frdoIsMoneyOp.setEditable(false);
					frdoIsMoneyOp.setValue("0");
				}

			}
		});

		// ������
		FPanel fpnlReportContent = new FPanel();
		fpnlReportContent.setLayout(new RowPreferedLayout(2));
		fpnlReportContent.addControl(fcbxCurrencyUnit, new TableConstraints(1,
				1, false, true));
		fpnlReportContent.addControl(fchkIsActice, new TableConstraints(1, 1,
				false, true));

		frdoIsMoneyOp = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoIsMoneyOp.setRefModel("0#��ת��+1#����������ٻ���+2#���ܺ�����������");
		frdoIsMoneyOp.setTitleVisible(false);
		frdoIsMoneyOp.setValue("0");
		frdoIsMoneyOp.setEditable(false);

		FPanel isMoneyOpPnl = new FPanel();
		isMoneyOpPnl.setTitle("Ԫת������Ԫѡ��");
		isMoneyOpPnl.setLayout(new RowPreferedLayout(1));
		isMoneyOpPnl.addControl(frdoIsMoneyOp, new TableConstraints(1, 1, true,
				true));

		// // �Ƿ�����
		// fchkIsMulYear = new FCheckBox("�Ƿ�����");
		// fchkIsMulYear.setTitlePosition("RIGHT");
		//
		// // �Ƿ������
		// fchkIsMulRgion = new FCheckBox("�Ƿ������");
		// fchkIsMulRgion.setTitlePosition("RIGHT");

		fchkChangeSource = new FCheckBox("֧�ֱ任֧���ʽ���Դ");
		fchkChangeSource.setTitlePosition("RIGHT");
		fchkChangeSource.setProportion(0.14f);

		fchkCompare = new FCheckBox("�Աȷ�����");
		fchkCompare.setTitlePosition("RIGHT");
		fchkCompare.setProportion(0.25f);

		fchkIsShowTotalRow = new FCheckBox("��ʾ�ܼ���");
		fchkIsShowTotalRow.setTitlePosition("RIGHT");
		fchkIsShowTotalRow.setProportion(0.25f);

		FFlowLayoutPanel fpnlChoice = new FFlowLayoutPanel();
		fpnlChoice.setAlignment(FlowLayout.LEFT);
		fpnlChoice
				.add(fchkChangeSource, new TableConstraints(1, 1, true, true));
		fpnlChoice.add(fchkCompare, new TableConstraints(1, 1, true, true));
		fpnlChoice.add(fchkIsShowTotalRow, new TableConstraints(1, 1, true,
				true));

		this.setTopInset(5);
		this.setLeftInset(5);
		// ���ò���
		RowPreferedLayout leftRlay = new RowPreferedLayout(2);
		this.setLayout(leftRlay);

		// ���������ı�����������ʾ���
		this.addControl(reportNameTxt, new TableConstraints(1, 1, false, true));

		// // �Ƿ�����ѡ�����������ʾ���
		// this.addControl(fchkIsMulYear, new TableConstraints(1, 1, false,
		// true));
		// // �Ƿ��Ƿ������ѡ�����������ʾ���
		// this
		// .addControl(fchkIsMulRgion, new TableConstraints(1, 1, false,
		// true));

		// ���屨�����������������ʾ���
		this
				.addControl(fpnlReportType, new TableConstraints(5, 1, false,
						true));
		// ���屨���û����������������ʾ���
		this.addControl(reportUserTypePnl, new TableConstraints(2, 1, false,
				true));

		// �Ƿ����ã���������
		this.addControl(fpnlReportContent, new TableConstraints(1, 1, false,
				true));

		// �Ƿ��������������ٻ���ѡ�����������ʾ���
		this.addControl(isMoneyOpPnl, new TableConstraints(2, 1, false, true));

		this.addControl(fpnlChoice, new TableConstraints(1, 1, false, true));

	}

	/**
	 * ���汨�������Ϣ
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	RepSetObject getReportBasicInfo() throws NumberFormatException, Exception {
		// �ж������ӻ����޸ı���,��������Report_Id,�޸�ʹ��ԭreportid
		if (!Common.isNullStr(sReportId)) {// �޸�
			summaryReportBasicAttr.setReportID(sReportId);
			repSetObject.setREPORT_ID(sReportId);
		} else {// ����
			String sNewReportId = SzzbSetI.getMethod().getMaxCode("REPORT_ID");

			summaryReportBasicAttr.setReportID(sNewReportId);
			repSetObject.setREPORT_ID(sNewReportId);

		}
		// ���ݽ�����д��Ϣ,���������Ϣ����Ϣ
		saveSummaryReportBasicAttr();
		// �����ѯ��������Ϣ
		saveReportRepsetInfo();
		sReportId = repSetObject.getREPORT_ID();
		return this.repSetObject;
	}

	/**
	 * ��д���
	 * 
	 * @return
	 */
	String check() {
		if (Common.isNullStr(reportNameTxt.getValue().toString())) {
			reportNameTxt.setFocus();
			return "����д��������!";
		}

		List lstType = reportTypeList.getSelectData();
		if (lstType.size() == 0) {
			return "��ѡ�񱨱�����!";
		}
		if (Common.isNullStr(fcbxCurrencyUnit.getText().toString().trim())) {
			return "��ѡ�����д��������!";
		}

		return "";
	}

	/**
	 * ���ݽ�����д��Ϣ,���������Ϣ����Ϣ
	 * 
	 */
	private void saveSummaryReportBasicAttr() {
		if (!(summaryReportBasicAttr instanceof ICustomSummaryReportBasicAttr)) {
			summaryReportBasicAttr = new MySummaryReportBasicAttr();
		}
		// ��������
		String sReportName = reportNameTxt.getValue().toString();
		summaryReportBasicAttr.setReportName(sReportName);
		summaryReportBasicAttr.setMode(1);
		// ��ǰΪ�ڼ���
		summaryReportBasicAttr.setIntPageIndex(1);
		// ÿ����ʾ����
		summaryReportBasicAttr.setPageCount(10000);
		// ������ϵͳ
		// summaryReportBasicAttr.setSysIDArray(new String[] { "101" });
		// �Ƿ��������������ٻ���
		summaryReportBasicAttr.setMoneyOp(Integer.parseInt(frdoIsMoneyOp
				.getValue().toString()));

		// // �Ƿ�����
		// int isMulYear = ("false".equals(fchkIsMulYear.getValue().toString())
		// ? 0
		// : 1);
		summaryReportBasicAttr.setIsMulYear(0);

		// �Ƿ������
		// int isMulRgion =
		// ("false".equals(fchkIsMulRgion.getValue().toString()) ? 0
		// : 1);
		summaryReportBasicAttr.setIsMulRgion(0);

		// �Ƿ���ʾ�ϼ���
		((ICustomSummaryReportBasicAttr) summaryReportBasicAttr)
				.setIsShowTotalRow(Common.estimate(this.fchkIsShowTotalRow
						.getValue()) ? "1" : "0");

		reportGuideUI.querySource.setReportBasicAttr(summaryReportBasicAttr);
	}

	/**
	 * �����ѯ��������Ϣ
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	private void saveReportRepsetInfo() throws NumberFormatException, Exception {
		// ��������
		repSetObject.setREPORT_CNAME(reportNameTxt.getValue().toString());

		if (Common.isNullStr(sReportId)) {// ����
			String sLvl = "";
		}

		// �����û�����
		repSetObject.setDATA_USER(Integer.parseInt(reportUserTypeGrp.getValue()
				.toString()));

		// ��������(���桢���鱨��ȣ�
		repSetObject.setTYPE_FLAG(IDefineReport.REPORTTYPE_GROUP);

		// // �Ƿ��������������ٻ���
		// repSetObject.setIS_MONEYOP(Integer.parseInt(frdoIsMoneyOp.getValue()
		// .toString()));

		// �Ƿ�����
		// repSetObject.setIS_MULYEAR(("false".equals(fchkIsMulYear.getValue()
		// .toString()) ? 0 : 1));
		//
		// // �Ƿ��Ƿ������
		// repSetObject.setIS_MULRGION(("false".equals(fchkIsMulRgion.getValue()
		// .toString()) ? 0 : 1));

		// �Ƿ�����
		repSetObject.setIS_ACTIVE(("false".equals(fchkIsActice.getValue()
				.toString()) ? "��" : "��"));
		// ��������
		repSetObject.setCURRENCYUNIT(fcbxCurrencyUnit.getText().toString());

		// ֧������֧���ʽ���Դ
		repSetObject.setFUNDSOURCE_FLAG(("false".equals(fchkChangeSource
				.getValue().toString()) ? "0" : "1"));

		// �Աȷ�����
		repSetObject.setCOMPARE_FLAG(("false".equals(this.fchkCompare
				.getValue().toString()) ? "0" : "1"));
	}

	/**
	 * ���ݻ�����Ϣ����Ϣ����ʾ���������Ϣ��������
	 * 
	 * @throws Exception
	 * 
	 */
	private void showSummaryReportBasicAttr() throws Exception {

		// ��������
		String sReportName = repSetObject.getREPORT_CNAME();
		reportNameTxt.setValue(sReportName);

		// ��������
		fcbxCurrencyUnit.setText(repSetObject.getCURRENCYUNIT());

		// �Ƿ��������������ٻ���
		frdoIsMoneyOp.setValue(String.valueOf(summaryReportBasicAttr
				.getMoneyOp()));

		// // �Ƿ�����
		// int isMulYear = repSetObject.getIS_MULYEAR();
		// ((JCheckBox) fchkIsMulYear.getEditor()).setSelected(Common
		// .estimate(new Integer(isMulYear)));
		// // �Ƿ������
		// int isMulRgion = repSetObject.getIS_MULRGION();
		// ((JCheckBox) fchkIsMulRgion.getEditor()).setSelected(Common
		// .estimate(new Integer(isMulRgion)));

		// �Ƿ����
		String sIsActive = repSetObject.getIS_ACTIVE();
		if ("��".equals(sIsActive))
			((JCheckBox) fchkIsActice.getEditor()).setSelected(true);
		else
			((JCheckBox) fchkIsActice.getEditor()).setSelected(false);

		// ���屨�����
		reportTypeList.setSelected(sReportId);
		// ���屨���û�����
		String sUserType = String.valueOf(repSetObject.getDATA_USER());
		reportUserTypeGrp.setValue(sUserType);

		// ֧������֧���ʽ���Դ
		((JCheckBox) fchkChangeSource.getEditor()).setSelected(Common
				.estimate(repSetObject.getFUNDSOURCE_FLAG()));

		// �Աȷ�����
		((JCheckBox) fchkCompare.getEditor()).setSelected(Common
				.estimate(repSetObject.getCOMPARE_FLAG()));

		// �Ƿ���ʾ�ϼ���
		if (summaryReportBasicAttr instanceof ICustomSummaryReportBasicAttr) {
			((JCheckBox) fchkIsShowTotalRow.getEditor())
					.setSelected(Common
							.estimate(((ICustomSummaryReportBasicAttr) summaryReportBasicAttr)
									.getIsShowTotalRow()));
		} else {
			((JCheckBox) fchkIsShowTotalRow.getEditor()).setSelected(true);
		}

	}

	List getType() {
		return reportTypeList.getSelectData();
	}
}
