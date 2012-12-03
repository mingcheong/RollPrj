/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.DefaultFComboBoxModel;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FComboBoxItem;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.summary.object.source.RefSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceRelationsImpl;
import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:���鱨����,����Դ��ϵ���ÿͷ���ҳ��
 * </p>
 * <p>
 * Description:���鱨����,����Դ��ϵ���ÿͷ���ҳ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-25
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class ReportDSRelaUI extends FDialog {

	private static final long serialVersionUID = 1L;

	private ReportGuideUI reportGuideUI = null;

	// ��������Դ1
	private FComboBox fcbxDataSourceA = null;

	// ��������Դ2
	private FComboBox fcbxDataSourceB = null;

	// ����ԴA�ֶα��
	private FTable ftabDataSourceA = null;

	// ����ԴB�ֶα��
	private FTable ftabDataSourceB = null;

	// ����Դֵ
	private String sDataSourceRef = "";

	private String sOldSourceIDA = null;

	private String sOldSourceIDB = null;

	private String sCurSourceIDA = null;

	private String sCurSourceIDB = null;

	// ������ϵ��
	MyTree treRela;

	// 1:���OK��ť��0:ȡ����ť
	private int resultMode = 0;

	/**
	 * ���캯��
	 * 
	 */
	public ReportDSRelaUI(ReportGuideUI reportGuideUI) {
		super(reportGuideUI);
		this.setSize(700, 525);
		this.setTitle("����Դ������ϵ����");
		this.setModal(true);

		this.reportGuideUI = reportGuideUI;

		// ���ý����ʼ������
		init();
	}

	/**
	 * ��ʼ������
	 * 
	 */
	private void init() {
		// �õ�����Դ�б�
		List lstDataSource = DefinePub.getDataSource(reportGuideUI.querySource);

		// ���Vectorֵ
		Map dataSourceMap = null;
		if (lstDataSource != null) {
			for (int i = 0; i < lstDataSource.size(); i++) {
				dataSourceMap = (Map) lstDataSource.get(i);
				if (!"".equals(sDataSourceRef)) {
					sDataSourceRef = sDataSourceRef + "+";
				}
				sDataSourceRef = sDataSourceRef
						+ dataSourceMap.get(IDefineReport.SOURCE_ID) + "#"
						+ dataSourceMap.get(IDefineReport.DATASOURCE_NAME);
			}
		}

		// ��������Դ1
		fcbxDataSourceA = new FComboBox("����Դ1��");
		fcbxDataSourceA.setRefModel(sDataSourceRef);
		fcbxDataSourceA.setSelectedIndex(-1);
		((JComboBox) fcbxDataSourceA.getComponent(1))
				.addItemListener(new SelectAItemListener());

		// ��������Դ2
		fcbxDataSourceB = new FComboBox("����Դ2��");
		fcbxDataSourceB.setRefModel(sDataSourceRef);
		fcbxDataSourceB.setSelectedIndex(-1);
		((JComboBox) fcbxDataSourceB.getComponent(1))
				.addItemListener(new SelectBItemListener());

		// ����ԴA�ֶα��
		ftabDataSourceA = new MyFTable();
		// ����ԴB�ֶα��
		ftabDataSourceB = new MyFTable();

		// �õ����ڵ�
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("������ϵ");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		treRela = new MyTree(treeModel);
		JScrollPane fpnltalRela = new JScrollPane(treRela);

		// ��������
		FButton addBtn = new FButton("addBtn", "����");
		addBtn.addActionListener(new AddActionListener());
		FButton delBtn = new FButton("delBtn", "ɾ��");
		delBtn.addActionListener(new DelActionListener());
		// ���尴ť���
		FFlowLayoutPanel operBtnPnl = new FFlowLayoutPanel();
		operBtnPnl.addControl(addBtn, new TableConstraints(1, 1, true, false));
		operBtnPnl.addControl(delBtn, new TableConstraints(1, 1, true, false));
		// �����
		FPanel fpnlTable = new FPanel();
		RowPreferedLayout rLay = new RowPreferedLayout(2);
		rLay.setColumnWidth(60);
		rLay.setColumnGap(1);
		fpnlTable.setLayout(rLay);
		fpnlTable.add(fpnltalRela, new TableConstraints(1, 1, true, true));
		fpnlTable.addControl(operBtnPnl,
				new TableConstraints(1, 1, true, false));

		BtnPanel btnPanel = new BtnPanel();

		FPanel fpnlMain = new FPanel();
		fpnlMain.setTitle("����Դ������ϵ����");
		fpnlMain.setLayout(new RowPreferedLayout(2));
		fpnlMain.addControl(fcbxDataSourceA, new TableConstraints(1, 1, false,
				true));
		fpnlMain.addControl(fcbxDataSourceB, new TableConstraints(1, 1, false,
				true));
		fpnlMain.addControl(ftabDataSourceA, new TableConstraints(10, 1, false,
				true));
		fpnlMain.addControl(ftabDataSourceB, new TableConstraints(10, 1, false,
				true));
		fpnlMain.addControl(fpnlTable, new TableConstraints(1, 2, true, true));

		FPanel fpnlPane = new FPanel();
		RowPreferedLayout rLayPanel = new RowPreferedLayout(1);
		rLayPanel.setRowHeight(35);
		fpnlPane.setLayout(rLayPanel);
		fpnlPane.addControl(fpnlMain, new TableConstraints(1, 1, true, true));
		fpnlPane.addControl(btnPanel, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(fpnlPane);

	}

	/**
	 * ���ñ����
	 * 
	 * @param ftable�����õı��
	 */
	private class MyFTable extends FTable {

		private static final long serialVersionUID = 1L;

		public MyFTable() {
			super();
			init();
		}

		public MyFTable(boolean arg0) {
			super(arg0);
			init();
		}

		private void init() {
			String[][] columns = new String[][] {
					{ IDefineReport.FIELD_FNAME, "�ֶ���", "150" },
					{ IDefineReport.FIELD_TYPE, "�ֶ�����", "80" } };

			for (int i = 0; i < columns.length; i++) {
				String[] col = columns[i];
				FBaseTableColumn column = new FBaseTableColumn();
				if ("joinType".equals(col[0])) {
					column.setEditable(true);
				}
				column.setId(col[0]);
				column.setTitle(col[1]);
				if (col.length == 3) {
					column.setWidth(Integer.parseInt(col[2]));
					column.setPreferredWidth(Integer.parseInt(col[2]));
					column.setMaxWidth(500);
					column.setMinWidth(0);
				}
				this.addColumn(column);
			}
			// ���õ�ѡ
			this.getRightActiveTable().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
		}
	}

	/**
	 * ���尴ť��
	 */
	private class BtnPanel extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		public BtnPanel() {
			// ���ÿ�����ʾ
			this.setAlignment(FlowLayout.RIGHT);

			// ���塰ȷ������ť
			FButton okBtn = new FButton("saveBtn", "����");
			okBtn.addActionListener(new SaveActionListener());
			// ���塱ȡ������ť
			FButton cancelBtn = new FButton("cancelBtn", "ȡ ��");
			// ʵ�֡�ȡ������ť����¼�
			cancelBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					ReportDSRelaUI.this.setVisible(false);
					resultMode = 0;
				}

			});

			// ��ȷ������ť���밴ť���
			this.addControl(okBtn);
			// ��ȡ������ť���밴ť���
			this.addControl(cancelBtn);
		}
	}

	/**
	 * DataSourceAֵ�ı��¼�
	 */
	private class SelectAItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {

			if (fcbxDataSourceA.getSelectedIndex() == -1)
				return;

			String sSourceID = ((FComboBoxItem) arg0.getItem()).getValue()
					.toString();
			if (sSourceID.equals(sOldSourceIDA))
				return;

			if (arg0.getStateChange() == 2) {
				if (sCurSourceIDA != null) {
					if (sCurSourceIDA.equals(sOldSourceIDA)) {
						return;
					}
				}
			}

			List lstdsDictionary = DefineReportI.getMethod().getFieldWithEname(
					sSourceID);
			ftabDataSourceA.setData(lstdsDictionary);

			if (fcbxDataSourceB.getValue() == null) {
				sCurSourceIDB = "";
			} else {
				sCurSourceIDB = fcbxDataSourceB.getValue().toString();
			}
			fcbxDataSourceB.setRefModel(sDataSourceRef);

			int iCount = fcbxDataSourceB.getItemsCount();
			if ("".equals(sCurSourceIDB))
				fcbxDataSourceB.setSelectedIndex(-1);
			else
				fcbxDataSourceB.setValue(sCurSourceIDB);

			DefaultFComboBoxModel model = (DefaultFComboBoxModel) fcbxDataSourceB
					.getRefModel();
			for (int i = 0; i < iCount; i++) {
				if (sSourceID.equals(((FComboBoxItem) model.getElementAt(i))
						.getValue())) {
					sOldSourceIDA = sSourceID;
					model.removeElementAt(i);
					ftabDataSourceB.removeData();
					break;
				}
			}

		}
	}

	/**
	 * DataSourceBֵ�ı��¼�
	 */
	private class SelectBItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {

			if (fcbxDataSourceB.getSelectedIndex() == -1)
				return;

			String sSourceID = ((FComboBoxItem) arg0.getItem()).getValue()
					.toString();

			if (sSourceID.equals(sOldSourceIDB))
				return;

			if (arg0.getStateChange() == 2)
				if (sCurSourceIDB != null) {
					if (sCurSourceIDB.equals(sOldSourceIDB)) {
						return;
					}
				}

			List lstdsDictionary = DefineReportI.getMethod().getFieldWithEname(
					sSourceID);
			ftabDataSourceB.setData(lstdsDictionary);

			if (fcbxDataSourceA.getValue() == null) {
				sCurSourceIDA = "";
			} else {
				sCurSourceIDA = fcbxDataSourceA.getValue().toString();
			}
			fcbxDataSourceA.setRefModel(sDataSourceRef);
			int iCount = fcbxDataSourceA.getItemsCount();
			if ("".equals(sCurSourceIDA))
				fcbxDataSourceA.setSelectedIndex(-1);
			else
				fcbxDataSourceA.setValue(sCurSourceIDA);

			DefaultFComboBoxModel model = (DefaultFComboBoxModel) fcbxDataSourceA
					.getRefModel();
			for (int i = 0; i < iCount; i++) {
				if (sSourceID.equals(((FComboBoxItem) model.getElementAt(i))
						.getValue())) {
					sOldSourceIDB = sSourceID;
					model.removeElementAt(i);
					break;
				}
			}
		}
	}

	/**
	 * ���ӹ�����ϵ
	 */
	private class AddActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// �õ���ǰѡ�е���
			XMLData lstRelaA = ftabDataSourceA.getCurrentRow();
			if (lstRelaA == null) {
				JOptionPane.showMessageDialog(ReportDSRelaUI.this,
						"��ѡ������Դ1�ֶΣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			XMLData lstRelaB = ftabDataSourceB.getCurrentRow();
			if (lstRelaB == null) {
				JOptionPane.showMessageDialog(ReportDSRelaUI.this,
						"��ѡ������Դ2�ֶΣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// �ж��ֶ������Ƿ���ͬ
			String sFieldTypeA = lstRelaA.get(IDefineReport.FIELD_TYPE)
					.toString();
			String sFieldTypeB = lstRelaB.get(IDefineReport.FIELD_TYPE)
					.toString();
			if (!sFieldTypeA.equals(sFieldTypeB)) {
				JOptionPane.showMessageDialog(ReportDSRelaUI.this,
						"�ֶ����Ͳ���ͬ,���ܽ���������ϵ��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			MyNodeObject myNodeObject = new MyNodeObject();

			// �õ�����ԴAID������
			myNodeObject.sSourceIDA = fcbxDataSourceA.getValue().toString();
			String sSourceIDAName = fcbxDataSourceA.getRefModel()
					.getNameByValue(myNodeObject.sSourceIDA);
			myNodeObject.sSourceColIDA = lstRelaA
					.get(IDefineReport.FIELD_ENAME).toString();
			String sSourceColIDAName = lstRelaA.get(IDefineReport.FIELD_FNAME)
					.toString();

			// �õ�����ԴB��ID������
			myNodeObject.sSourceIDB = fcbxDataSourceB.getValue().toString();
			String sSourceIDBName = fcbxDataSourceB.getRefModel()
					.getNameByValue(myNodeObject.sSourceIDB);
			myNodeObject.sSourceColIDB = lstRelaB
					.get(IDefineReport.FIELD_ENAME).toString();
			String sSourceColIDBName = lstRelaB.get(IDefineReport.FIELD_FNAME)
					.toString();

			// �õ����ڵ�
			DefaultTreeModel treeModel = (DefaultTreeModel) treRela.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
					.getRoot();
			// �ж����ӵĸ��ڵ��Ƿ��Ѵ���,Ĭ�ϸ��ڵ㲻����
			boolean sFlag = false;
			MyNodeObject curMyNodeObject = null;
			DefaultMutableTreeNode curNode = null;
			int iChildCount = root.getChildCount();
			// �õ����ڵ��µ��ӽڵ�
			for (int i = 0; i < iChildCount; i++) {
				curNode = (DefaultMutableTreeNode) root.getChildAt(i);
				curMyNodeObject = (MyNodeObject) curNode.getUserObject();
				if (myNodeObject.sSourceIDA.equals(curMyNodeObject
						.getSSourceIDA())
						&& myNodeObject.sSourceIDB.equals(curMyNodeObject
								.getSSourceIDB())) {
					sFlag = true;
					break;
				}
			}

			DefaultMutableTreeNode ANode = null;
			if (!sFlag) {// ���ڵ㲻����
				// ���Ӹ��ڵ�
				MyNodeObject myParNodeObject = new MyNodeObject();
				myParNodeObject.setSSourceIDA(myNodeObject.sSourceIDA);
				myParNodeObject.setSSourceIDB(myNodeObject.sSourceIDB);
				myParNodeObject.sRelationsName = sSourceIDAName + "="
						+ sSourceIDBName;
				ANode = new DefaultMutableTreeNode(myParNodeObject);
				treeModel.insertNodeInto(ANode, root, root.getChildCount());
				curNode = (DefaultMutableTreeNode) root.getLastChild();
			}
			// �����ӽڵ�
			myNodeObject.sRelationsName = sSourceColIDAName + "="
					+ sSourceColIDBName;
			ANode = new DefaultMutableTreeNode(myNodeObject);
			treeModel.insertNodeInto(ANode, curNode, curNode.getChildCount());
			// ��λ�������ӵĽڵ�
			treRela.expendTo(ANode);
		}
	}

	/**
	 * ɾ��������ϵ
	 */
	private class DelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treRela
					.getLastSelectedPathComponent();
			if (!defaultMutableTreeNode.isLeaf())
				return;

			// �õ�������ڵ�
			DefaultMutableTreeNode parNode = (DefaultMutableTreeNode) defaultMutableTreeNode
					.getParent();
			// �õ���������ӽڵ���
			int iChildCount = parNode.getChildCount();

			DefaultTreeModel model = (DefaultTreeModel) treRela.getModel();
			model.removeNodeFromParent(defaultMutableTreeNode);
			// ����һ���ӽڵ㣬�ӽڵ�ɾ��ʱ��ɾ�����ڵ�
			if (iChildCount == 1) {
				model.removeNodeFromParent(parNode);
			}
		}
	}

	/**
	 * ���水ť����¼�
	 */
	private class SaveActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// ��������Դ������ϵ����
			saveSummaryDataSourceRelationsImpl();
			ReportDSRelaUI.this.setVisible(false);
			resultMode = 1;
		}
	}

	/**
	 * ���������ϵ��Ϣ���������
	 * 
	 */
	private void saveSummaryDataSourceRelationsImpl() {
		// ��ǰ�ڵ����
		MyNodeObject myNodeObject = null;
		// ������ϵ����
		RefSource[] refSource = null;
		// �õ�Ҷ�ڵ�
		List lstLeafNode = treRela.getIsLeafNode();
		int iCount = lstLeafNode.size();
		// ���������ϵ����
		SummaryDataSourceRelationsImpl[] summaryDataSourceRelationsImpl = new SummaryDataSourceRelationsImpl[iCount];
		for (int i = 0; i < iCount; i++) {
			summaryDataSourceRelationsImpl[i] = new SummaryDataSourceRelationsImpl();
			myNodeObject = (MyNodeObject) lstLeafNode.get(i);
			summaryDataSourceRelationsImpl[i].setRelationsID(DefinePub
					.getRandomUUID());
			// ����
			summaryDataSourceRelationsImpl[i]
					.setRelationsName(myNodeObject.sRelationsName);
			// ������ϵ����
			refSource = new RefSource[2];
			refSource[0] = new RefSource();
			refSource[1] = new RefSource();
			refSource[0].setSourceID(myNodeObject.sSourceIDA);
			refSource[1].setSourceID(myNodeObject.sSourceIDB);
			refSource[0].setSourceColID(myNodeObject.sSourceColIDA);
			refSource[1].setSourceColID(myNodeObject.sSourceColIDB);
			summaryDataSourceRelationsImpl[i].setRefSourceArray(refSource);
		}

		// ���������ϵ
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) reportGuideUI.querySource
				.getDataSourceManager();
		summaryDataSourceManagerImpl
				.setDataSourceRelationsArray(summaryDataSourceRelationsImpl);
	}

	public int getResultMode() {
		return resultMode;
	}

}
