/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:�Զ��屨������ѡ��ͻ���������
 * </p>
 * <p>
 * Description:�Զ��屨������ѡ��ͻ���������
 * </p>
 * <p>
 
 */
public class DefineReportTypeChoice extends FDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ���������б�
	private FList reportTypeLst = null;

	// ��������˵�������ı���
	private JTextArea typeExplainTxta = null;

	// �������ݿ�ӿ�
	private IDefineReport definReportServ = null;

	// �������͵���ֵ
	private List reportType_list = null;

	// ���屨������
	private int reportType = 0;

	/**
	 * ���캯��
	 * 
	 * @param frame��������
	 */
	public DefineReportTypeChoice() {
		super(Global.mainFrame);
		this.setModal(true);
		this.setSize(600, 450);
		this.setTitle("�½�����");
		// ���ý����ʼ������
		jbInit();
	}

	/**
	 * �����ʼ������
	 * 
	 */
	private void jbInit() {
		// �������ݿ�ӿ�
		definReportServ = DefineReportI.getMethod();

		// ȡ�ñ������͵���ֵ
		reportType_list = definReportServ.getReportSort();

		// ���屨������
		Vector reportTypeVertor = new Vector();
		Map reportTypeMa = null;
		String report_type;
		for (int i = 0; i < reportType_list.size(); i++) {
			reportTypeMa = (Map) reportType_list.get(i);
			report_type = reportTypeMa.get("reporttype_name").toString();
			reportTypeVertor.addElement(report_type);
		}

		// ���屨�������б�
		reportTypeLst = new FList(reportTypeVertor);
		DefaultListSelectionModel reportTypeMod = new DefaultListSelectionModel();
		reportTypeMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		reportTypeLst.setSelectionModel(reportTypeMod);
		reportTypeLst.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() != 2)
					return;
				selectOpe();
			}
		});

		// ���������б��valueChanged�¼�
		reportTypeLst.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (reportType_list == null)
					return;
				if (reportTypeLst.getSelectedIndex() == -1)
					return;
				Map tmpMap = (Map) reportType_list.get(reportTypeLst
						.getSelectedIndex());
				typeExplainTxta.setText(tmpMap.get("reporttype_explain")
						.toString());
			}
		});

		// ���屨�������б�������
		FScrollPane reportTypeSpnl = new FScrollPane(reportTypeLst);

		// ���屨������˵�������ı���
		typeExplainTxta = new JTextArea();
		typeExplainTxta.setEditable(false);
		typeExplainTxta.setBackground(this.getBackground());
		// ���屨������˵���������
		FScrollPane typeExplainSpnl = new FScrollPane(typeExplainTxta);

		// ���屨��ѡ�����
		FPanel choiceReportPnl = new FPanel();
		choiceReportPnl.setTitle("��ѡ��һ������");
		choiceReportPnl.setLayout(new RowPreferedLayout(5));
		// ���������б���뱨��ѡ�����
		choiceReportPnl.add(reportTypeSpnl, new TableConstraints(1, 2, true,
				true));
		// ��������˵�������ı�����뱨��ѡ�����
		choiceReportPnl.add(typeExplainSpnl, new TableConstraints(1, 3, true,
				true));

		// ���尴ť���
		CustomBtnButton buttonPnl = new CustomBtnButton();

		// ��������弰����
		FPanel mainPnl = new FPanel();
		RowPreferedLayout mainRly = new RowPreferedLayout(1);
		mainRly.setRowHeight(30);
		mainPnl.setLayout(mainRly);
		// ����ѡ�������������
		mainPnl.addControl(choiceReportPnl, new TableConstraints(1, 1, true,
				true));
		// ��ť�����������
		mainPnl.addControl(buttonPnl, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(mainPnl);

		// ���÷�����ܱ�ΪĬ��ѡ��
		if (reportType_list != null) {
			int size = reportType_list.size();
			int index = -1;
			int reporttypeid;
			for (int i = 0; i < size; i++) {
				reporttypeid = Integer.parseInt(((Map) reportType_list.get(i))
						.get("reporttype_id").toString());
				if (reporttypeid == QrBudget.TYPE_GROUP) {
					index = i;
					break;
				}
			}
			if (index != -1)
				reportTypeLst.setSelectedIndex(index);
		}
	}

	/**
	 * ���尴ť���
	 */
	private class CustomBtnButton extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		public CustomBtnButton() {
			// ���ÿ�����ʾ
			this.setAlignment(FlowLayout.RIGHT);
			// ����ȷ����
			FButton okBtn = new FButton("okBtn", "ȷ��");
			// ȷ����ť����¼�
			okBtn.addActionListener(new okActionListener());

			// ����ȡ����ť
			FButton cancelBtn = new FButton("cancelBtn", "ȡ ��");
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ����δѡ�񱨱�
					reportType = -1;
					DefineReportTypeChoice.this.setVisible(false);
				}
			});

			// ȷ����ť���밴ť���
			this.addControl(okBtn, new TableConstraints(1, 1, true, false));
			// ȡ����ť���밴ť���
			this.addControl(cancelBtn, new TableConstraints(1, 1, true, false));

		}

	}

	/**
	 * ȷ����ť����¼�
	 */
	private class okActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			selectOpe();
		}
	}

	/**
	 * ʹ�ñ������
	 * 
	 */
	private void selectOpe() {
		// �õ�ѡ�еı���
		int selectIndex = reportTypeLst.getSelectedIndex();
		Map reportTypeMa = (Map) reportType_list.get(selectIndex);
		reportType = Integer.parseInt(reportTypeMa.get("reporttype_id")
				.toString());
		DefineReportTypeChoice.this.setVisible(false);
	}

	public int getReportType() {
		return reportType;
	}

}
