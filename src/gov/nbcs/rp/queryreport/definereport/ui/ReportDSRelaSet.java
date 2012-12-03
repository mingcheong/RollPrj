/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSourceRelations;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.source.RefSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceRelationsImpl;
import com.foundercy.pf.util.Tools;

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
 * CreateData 2011-3-21
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class ReportDSRelaSet extends FTitledPanel {

	private static final long serialVersionUID = 1L;

	// ���������ϵ���
	MyTree treRela = null;

	private ReportDSRelaUI reportDSRelaUI = null;

	private ReportGuideUI reportGuideUI;

	// ������
	private ReportQuerySource querySource;

	public ReportDSRelaSet(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.querySource = reportGuideUI.querySource;
		DataRelationPnl dataRelationPnl = new DataRelationPnl();
		this.setLayout(new RowPreferedLayout(1));
		this.add(dataRelationPnl);

		// ��ʾ������ϵ
		if (reportGuideUI.sReportId != null)
			showDatasourceRelations(treRela);

	}

	/**
	 * ѡ������Դ֮���ϵ�������
	 */
	private class DataRelationPnl extends FPanel {

		private static final long serialVersionUID = 1L;

		public DataRelationPnl() {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("������ϵ");
			DefaultTreeModel treeModel = new DefaultTreeModel(root);
			// ���������ϵ���
			treRela = new MyTree(treeModel);
			JScrollPane fpnltalRela = new JScrollPane(treRela);

			// ��������
			FButton relaSetBtn = new FButton("relaSetBtn", "��������");
			relaSetBtn.addActionListener(new RelaSetActionListener());
			// ���尴ť���
			FFlowLayoutPanel btnPnl = new FFlowLayoutPanel();
			btnPnl.addControl(relaSetBtn, new TableConstraints(1, 1, true,
					false));

			RowPreferedLayout rLay = new RowPreferedLayout(2);
			rLay.setColumnWidth(90);
			rLay.setRowHeight(23);
			rLay.setColumnGap(1);
			this.setLayout(rLay);
			this.add(fpnltalRela, new TableConstraints(1, 1, true, true));
			this.addControl(btnPnl, new TableConstraints(1, 1, true, false));
		}
	}

	/**
	 * ���ñ����
	 * 
	 * @param ftable�����õı��
	 */
	void setTableColumn(FTable ftable) {
		String[][] columns = new String[][] {
				{ "dataSourceNameA", "����ԴA", "100" },
				{ "fieldA", "�ֶ�����A", "150" }, { "joinType", "��������", "80" },
				{ "dataSourceNameB", "����ԴB", "100" },
				{ "fieldB", "�ֶ�����B", "150" } };

		for (int i = 0; i < columns.length; i++) {
			String[] col = columns[i];
			FBaseTableColumn column = new FBaseTableColumn();
			column.setId(col[0]);
			column.setTitle(col[1]);
			if (col.length == 3) {
				column.setWidth(Integer.parseInt(col[2]));
				column.setPreferredWidth(Integer.parseInt(col[2]));
				column.setMaxWidth(500);
				column.setMinWidth(0);
			}
			ftable.addColumn(column);
		}
	}

	/**
	 * ������ϵ��ť����¼�
	 */
	private class RelaSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			reportDSRelaUI = new ReportDSRelaUI(reportGuideUI);
			// ������ϵ���ô��������ʾ
			Tools.centerWindow(reportDSRelaUI);
			// ��ʾ������ϵ���ô���
			try {
				// ��¡�����ӱ����浽�����������
				cloneTree(treRela, reportDSRelaUI.treRela);
				reportDSRelaUI.treRela.expandTree();
				reportDSRelaUI.setVisible(true);
				// ��¡�����ӵ���������浽
				if (reportDSRelaUI.getResultMode() == 1) {
					cloneTree(reportDSRelaUI.treRela, treRela);
					treRela.expandTree();
				}
			} finally {
				reportDSRelaUI.dispose();
			}

		}
	}

	/**
	 * ��ʾ������ϵ,����summaryReport����Ϣ
	 * 
	 */
	private void showDatasourceRelations(JTree jTree) {
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();
		if (summaryDataSourceManagerImpl == null)
			return;

		SummaryDataSourceRelationsImpl relationsImpl = null;
		IDataSourceRelations[] relationsArray = summaryDataSourceManagerImpl
				.getDataSourceRelationsArray();
		if (relationsArray == null)
			return;

		// ��ϵ����
		int iCount = relationsArray.length;
		if (iCount == 0)
			return;

		// �õ����ڵ�
		DefaultTreeModel treeModel = (DefaultTreeModel) treRela.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();
		// �Ƿ��и��ڵ���,Ĭ�ϸ��ڵ㲻����
		boolean sFlag = false;
		// �ж��������
		boolean sExistFlag = false;
		// ��ʱ��ŵ�ǰ�ڵ����ֵ
		MyNodeObject curMyNodeObject = null;
		// �ٵش�ŵ�ǰNode
		DefaultMutableTreeNode curNode = null;
		// �½��ڵ�
		DefaultMutableTreeNode treNode = null;

		// ����Դ���ƺ�������
		String sSourceIDAName = null;
		String sSourceIDBName = null;
		String sSourceColIDAName = null;
		String sSourceColIDBName = null;

		for (int i = 0; i < iCount; i++) {
			// ѭ��������ʾ������ϵ
			MyNodeObject myNodeObject = new MyNodeObject();

			relationsImpl = (SummaryDataSourceRelationsImpl) relationsArray[i];
			myNodeObject.sRelationsName = relationsImpl.getRelationsName();
			RefSource[] refSource = relationsImpl.getRefSourceArray();
			myNodeObject.sSourceIDA = refSource[0].getSourceID();
			myNodeObject.sSourceIDB = refSource[1].getSourceID();
			myNodeObject.sSourceColIDA = refSource[0].getSourceColID();
			myNodeObject.sSourceColIDB = refSource[1].getSourceColID();

			// �ж����ӵĸ��ڵ��Ƿ��Ѵ���,Ĭ�ϸ��ڵ㲻����
			sFlag = false;
			int iChildCount = root.getChildCount();
			// �õ����ڵ��µ��ӽڵ�
			for (int j = 0; j < iChildCount; j++) {
				curNode = (DefaultMutableTreeNode) root.getChildAt(j);
				curMyNodeObject = (MyNodeObject) curNode.getUserObject();

				sExistFlag = myNodeObject.sSourceIDA.equals(curMyNodeObject
						.getSSourceIDA())
						&& myNodeObject.sSourceIDB.equals(curMyNodeObject
								.getSSourceIDB());
				if (sExistFlag) {
					sFlag = true;
					break;
				}
			}

			if (!sFlag) {// ���ڵ㲻����, ���Ӹ��ڵ�
				// ��������ԴID�õ�����Դ����
				sSourceIDAName = DefinePub.getDataSourceNameWithID(querySource,
						myNodeObject.sSourceIDA);
				sSourceIDBName = DefinePub.getDataSourceNameWithID(querySource,
						myNodeObject.sSourceIDB);
				MyNodeObject myParNodeObject = new MyNodeObject();

				myParNodeObject.sRelationsName = sSourceIDAName + "="
						+ sSourceIDBName;
				myParNodeObject.setSSourceIDA(myNodeObject.sSourceIDA);
				myParNodeObject.setSSourceIDB(myNodeObject.sSourceIDB);
				// ���Ӹ��ڵ�
				treNode = new DefaultMutableTreeNode(myParNodeObject);
				treeModel.insertNodeInto(treNode, root, root.getChildCount());
				curNode = (DefaultMutableTreeNode) root.getLastChild();
			}

			// �����ӽڵ�
			sSourceColIDAName = DefinePub.getDataSourceColNameWithID(
					querySource, myNodeObject.sSourceIDA,
					myNodeObject.sSourceColIDA);
			sSourceColIDBName = DefinePub.getDataSourceColNameWithID(
					querySource, myNodeObject.sSourceIDB,
					myNodeObject.sSourceColIDB);
			myNodeObject.sRelationsName = sSourceColIDAName + "="
					+ sSourceColIDBName;
			treNode = new DefaultMutableTreeNode(myNodeObject);
			treeModel.insertNodeInto(treNode, curNode, curNode.getChildCount());
		}
		treRela.expandTree();
	}

	/**
	 * ��¡��,ֻ֧�ֶ�����
	 * 
	 * @param treFrom
	 *            ԭ��
	 * @param treTo
	 *            Ŀ����
	 */
	private void cloneTree(JTree treFrom, JTree treTo) {

		if (treFrom == null || treTo == null)
			return;

		// ԭ�����ڵ�
		DefaultMutableTreeNode fromRoot = (DefaultMutableTreeNode) treFrom
				.getModel().getRoot();
		// Ŀ�������ڵ�
		DefaultMutableTreeNode toRoot = (DefaultMutableTreeNode) treTo
				.getModel().getRoot();
		// Ŀ����Model
		DefaultTreeModel toTreeModel = (DefaultTreeModel) treTo.getModel();

		int iCount = toRoot.getChildCount();
		// ���Ŀ�����ڵ�
		for (int i = iCount - 1; i >= 0; i--) {
			toTreeModel.removeNodeFromParent((MutableTreeNode) toRoot
					.getChildAt(i));
		}

		// ԭ����ŵ�ǰ�ڵ�
		DefaultMutableTreeNode froCurNode;
		// Ŀ������ŵ�ǰ�ڵ�
		DefaultMutableTreeNode toCurNode;

		// ԭ����ŵ�ǰ�ڵ�
		DefaultMutableTreeNode froCurChildNode;
		// Ŀ������ŵ�ǰҶ�ڵ�
		DefaultMutableTreeNode toCurChildNode;

		iCount = fromRoot.getChildCount();
		// Ҷ�ڵ�
		int iChildCount = 0;
		for (int i = 0; i < iCount; i++) {
			froCurNode = (DefaultMutableTreeNode) fromRoot.getChildAt(i);
			toCurNode = (DefaultMutableTreeNode) froCurNode.clone();
			toTreeModel.insertNodeInto(toCurNode, toRoot, i);
			// �õ�Ҷ�ڵ�
			iChildCount = froCurNode.getChildCount();
			for (int j = 0; j < iChildCount; j++) {
				froCurChildNode = (DefaultMutableTreeNode) froCurNode
						.getChildAt(j);
				toCurChildNode = (DefaultMutableTreeNode) froCurChildNode
						.clone();
				toTreeModel.insertNodeInto(toCurChildNode, toCurNode, j);
			}
		}
	}
}
