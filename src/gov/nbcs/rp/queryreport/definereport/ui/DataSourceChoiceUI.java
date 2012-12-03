/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.table.TablePanel;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.IToSource;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSourceRelations;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryParameterImpl;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;
import com.foundercy.pf.reportcy.summary.object.source.RefSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceRelationsImpl;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:����Դѡ��ͻ��˽���
 * </p>
 * <p>
 * Description:����Դѡ��ͻ��˽���
 * </p>
 * <p>

 */
public class DataSourceChoiceUI extends FDialog {

	private static final long serialVersionUID = 1L;

	// �������ݿ�ӿ�
	private IDefineReport definReportServ = null;

	// ����Դ���
	private FTable dataSourceTable = null;

	// ����ѡ�е�����Դ
	private List lstDataSource = null;

	private GroupReport groupReport;

	private ReportQuerySource querySource;

	/**
	 * ���캯��
	 * 
	 * @param definReportServ
	 *            ���ݿ�ӿ�
	 */
	public DataSourceChoiceUI(Frame frame, IDefineReport definReportServ,
			List lstDataSource) {
		super(frame);
		this.setSize(600, 400);
		this.setTitle("����Դѡ��");
		this.setModal(true);
		// �������ݿ�ӿ�
		this.definReportServ = definReportServ;
		// ����ѡ�е�����Դ
		this.lstDataSource = lstDataSource;

		groupReport = ((ReportGuideUI) frame).fpnlDefineReport.groupReport;
		querySource = ((ReportGuideUI) frame).querySource;
		// ���ý����ʼ������Ժ
		jbInit();
	}

	/**
	 * �����ʼ������
	 * 
	 */
	private void jbInit() {

		// ��������Դ���
		dataSourceTable = new TablePanel(new String[][] {
				{ IDefineReport.OBJECT_CLASS, "���", "100" },
				{ IDefineReport.OBJECT_CNAME, "�����ͼ����", "300" } }, true);

		dataSourceTable.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseClicked(java.awt.event.MouseEvent mouseevent) {
				if (mouseevent.getButton() != 1
						|| !(mouseevent.getSource() instanceof JTable))
					return;

				Boolean boolean1;
				int j = dataSourceTable.getLeftLockedTable().getSelectedRow();
				if (j < 0)
					return;
				Object obj = dataSourceTable.getValueAt(j, "isCheck");
				if (obj instanceof Boolean) {
					boolean1 = (Boolean) obj;
					if (!boolean1.equals(Boolean.TRUE)) {
						String sourceID = dataSourceTable.getDataByIndex(j)
								.get(IDefineReport.DICID).toString();
						String sInfo = null;
						try {
							sInfo = checkIsUse(sourceID);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(
									DataSourceChoiceUI.this, "ѡ������Դ�������󣬴�����Ϣ��"
											+ e.getMessage(), "��ʾ",
									JOptionPane.ERROR_MESSAGE);
						}
						if (!Common.isNullStr(sInfo)) {
							dataSourceTable.setValueAt(Boolean.TRUE, j,
									"isCheck");
							JOptionPane.showMessageDialog(
									DataSourceChoiceUI.this, sInfo, "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
				}
				super.mouseClicked(mouseevent);
			}
		});

		// ��������Դ����п����϶����
		for (int i = 0; i < dataSourceTable.getColumnModel().getColumnCount(); i++) {
			dataSourceTable.getColumnModel().getColumn(i).setMinWidth(0);
			dataSourceTable.getColumnModel().getColumn(i).setMaxWidth(500);
		}
		// �������ݱ������
		// �õ�����Դ��Ϣ
		List dataSource_List = definReportServ.getDataSource();
		dataSourceTable.setData(dataSource_List);
		// ��ʾѡ��״̬
		showChoice();

		// ������Դ��Ϣ���
		FPanel dataSourcePnl = new FPanel();
		dataSourcePnl.setTitle("����Դ");
		dataSourcePnl.setLayout(new RowPreferedLayout(1));
		dataSourcePnl.addControl(dataSourceTable, new TableConstraints(1, 1,
				true, true));

		// ���塰ȷ������ť
		FButton okBtn = new FButton("okBtn", "ȷ��");
		okBtn.addActionListener(new OkActionListener());
		// ���塱ȡ������ť
		FButton cancelBtn = new FButton("cancelBtn", "ȡ ��");
		// ʵ�֡�ȡ������ť����¼�
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				DataSourceChoiceUI.this.setVisible(false);
			}
		});

		// ���尴ť���
		FFlowLayoutPanel btnFpnl = new FFlowLayoutPanel();
		// ���ÿ�����ʾ
		btnFpnl.setAlignment(FlowLayout.RIGHT);
		// ��ȷ������ť���밴ť���
		btnFpnl.addControl(okBtn);
		// ��ȡ������ť���밴ť���
		btnFpnl.addControl(cancelBtn);

		// ��������弰����
		FPanel mainPnl = new FPanel();
		RowPreferedLayout mainRlay = new RowPreferedLayout(1);
		mainRlay.setRowHeight(30);
		mainPnl.setLayout(mainRlay);
		// ����Դ�����������
		mainPnl.add(dataSourcePnl, new TableConstraints(1, 1, true, true));
		// ��ť�����������
		mainPnl.add(btnFpnl, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(mainPnl);

		// ���ӽ��漤���¼�
		// this.addWindowListener(new DswindowActivated());

	}

	/**
	 * ��ʾѡ��״̬
	 */
	private void showChoice() {
		// �ж�ѡ�е�����Դ�Ƿ����
		if (lstDataSource == null) {
			// �������˳�
			return;
		}

		// ��������Դ���OBJECT_ENAMEֵ
		String dataSourceList = null;
		// ����ѡ�е�����Դ��OBJECT_ENAMEֵ
		String dataSourceChoice = null;
		// ȡ������Դ����б�
		List dataList = dataSourceTable.getData();
		// ����Դ����б�ѭ��
		for (int i = 0; i < dataList.size(); i++) {
			// �赱ǰ����Դ�б�ѡ��״̬Ϊfalse
			dataSourceTable.setCheckBoxSelectedAtRow(i, false);
			// ȡ�õ�ǰ����Դ�б��IDֵ
			dataSourceList = ((Map) dataList.get(i)).get(IDefineReport.DICID)
					.toString();
			// ѡ������Դ����б�ѭ��
			for (int j = 0; j < lstDataSource.size(); j++) {
				// ȡ�õ�ǰѡ������Դ�б��IDֵ
				dataSourceChoice = ((Map) lstDataSource.get(j)).get(
						IDefineReport.DICID).toString();

				// �ж�"����Դ���IDֵ"��"ѡ������Դ�б��IDֵ"�Ƿ����
				if (dataSourceList.equals(dataSourceChoice)) {
					// �赱ǰ����Դ�б�ѡ��״̬Ϊtrue
					dataSourceTable.setCheckBoxSelectedAtRow(i, true);
					continue;
				}
			}
		}
	}

	/**
	 * ȷ����ť����¼�
	 */
	private class OkActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			// �õ�ѡ�е�����Դ
			lstDataSource = dataSourceTable.getSelectedDataByCheckBox();
			// �ж��Ƿ�ѡ�е�����Դ
			if (lstDataSource.size() == 0) {
				JOptionPane
						.showMessageDialog(DataSourceChoiceUI.this,
								"��δѡ�����ݣ��빴ѡ����Դ��", "��ʾ",
								JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// �رմ���
			DataSourceChoiceUI.this.setVisible(false);

		}
	}

	/**
	 * �õ�ѡ�е�������Ϣ����
	 * 
	 * @return ����ѡ�е�������Ϣ
	 */
	public List getDataSource() {
		return lstDataSource;
	}

	private String checkIsUse(String sourceID) throws Exception {
		// �õ�����������
		int indexs[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int row = indexs[0];

		int colCount = groupReport.getColumnCount();
		MyGroupValueImpl GroupValueImpl;
		MyCalculateValueImpl calculateValueImpl;
		IGroupAble[] groupAble;
		FunctionRef[] functionRef;
		Object value;
		// �ж��ֶ��Ƿ��ѱ�ʹ��
		for (int i = 0; i < colCount; i++) {
			if (groupReport.getCellElement(i, row) == null)
				continue;
			value = groupReport.getCellElement(i, row).getValue();

			if (value instanceof MyGroupValueImpl) { // �ж��Ƿ��Ƿ�����
				GroupValueImpl = ((MyGroupValueImpl) groupReport
						.getCellElement(i, row).getValue());
				groupAble = GroupValueImpl.getGroupAbleArray();
				for (int j = 0; j < groupAble.length; j++) {
					functionRef = groupAble[j].getFormula()
							.getFunctionRefArray();
					for (int k = 0; k < functionRef.length; k++) {
						if (sourceID.equals(functionRef[k].getSourceID())) {
							return "�ѱ�����ʹ��,������ȡ����ѡ��";
						}
					}
				}
			} else if (value instanceof MyCalculateValueImpl) { // �ж��Ƿ��Ǽ�������
				calculateValueImpl = ((MyCalculateValueImpl) groupReport
						.getCellElement(i, row).getValue());
				IMeasureAttr[] measureAttr = calculateValueImpl
						.getMeasureArray();
				for (int j = 0; j < measureAttr.length; j++) {
					if (sourceID.equals(measureAttr[j].getSourceID())) {
						return "�ѱ�����ʹ��,������ȡ����ѡ��";
					}
				}
			}
		}

		// �ж�����Դ�Ƿ������ù�������
		IParameter[] parameter = querySource.getParameterArray();
		SummaryParameterImpl summaryParameterImpl;
		IToSource[] toSource;
		String chName;
		if (parameter != null)
			for (int i = 0; i < parameter.length; i++) {
				summaryParameterImpl = (SummaryParameterImpl) parameter[i];
				chName = summaryParameterImpl.getChName();
				if (DataSourceSet.checkDefaultPara(chName))
					continue;
				toSource = summaryParameterImpl.getToSourceArray();
				for (int k = 0; k < toSource.length; k++) {
					if (sourceID.equals(toSource[k].getSourceID())) {
						return "����Դ�����ù���������������ȡ����ѡ��";
					}
				}
			}
		// �ж�����Դ�Ƿ������ù�������
		if (querySource.getDataSourceManager() != null) {
			IDataSourceRelations[] dataSourceRelations = querySource
					.getDataSourceManager().getDataSourceRelationsArray();
			if (dataSourceRelations == null)
				return "";
			SummaryDataSourceRelationsImpl dataSourceRelationsImpl;
			RefSource[] refSource;
			for (int i = 0; i < dataSourceRelations.length; i++) {
				dataSourceRelationsImpl = ((SummaryDataSourceRelationsImpl) dataSourceRelations[i]);
				refSource = dataSourceRelationsImpl.getRefSourceArray();
				for (int j = 0; j < refSource.length; j++) {
					if (sourceID.equals(refSource[j].getSourceID())) {
						return "����Դ�����ù�����ϵ��������ȡ����ѡ��";
					}
				}
			}
		}

		return "";
	}
}
