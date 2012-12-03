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
 * Title:分组报表定义,数据源关系设置客服端页面
 * </p>
 * <p>
 * Description:分组报表定义,数据源关系设置客服端页面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
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

	// 定义关联关系表格
	MyTree treRela = null;

	private ReportDSRelaUI reportDSRelaUI = null;

	private ReportGuideUI reportGuideUI;

	// 报表类
	private ReportQuerySource querySource;

	public ReportDSRelaSet(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.querySource = reportGuideUI.querySource;
		DataRelationPnl dataRelationPnl = new DataRelationPnl();
		this.setLayout(new RowPreferedLayout(1));
		this.add(dataRelationPnl);

		// 显示关联关系
		if (reportGuideUI.sReportId != null)
			showDatasourceRelations(treRela);

	}

	/**
	 * 选中数据源之间关系设置面板
	 */
	private class DataRelationPnl extends FPanel {

		private static final long serialVersionUID = 1L;

		public DataRelationPnl() {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("关联关系");
			DefaultTreeModel treeModel = new DefaultTreeModel(root);
			// 定义关联关系表格
			treRela = new MyTree(treeModel);
			JScrollPane fpnltalRela = new JScrollPane(treRela);

			// 关联设置
			FButton relaSetBtn = new FButton("relaSetBtn", "关联设置");
			relaSetBtn.addActionListener(new RelaSetActionListener());
			// 定义按钮面板
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
	 * 设置表格列
	 * 
	 * @param ftable需设置的表格
	 */
	void setTableColumn(FTable ftable) {
		String[][] columns = new String[][] {
				{ "dataSourceNameA", "数据源A", "100" },
				{ "fieldA", "字段名称A", "150" }, { "joinType", "关联条件", "80" },
				{ "dataSourceNameB", "数据源B", "100" },
				{ "fieldB", "字段名称B", "150" } };

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
	 * 关联关系按钮点击事件
	 */
	private class RelaSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			reportDSRelaUI = new ReportDSRelaUI(reportGuideUI);
			// 关联关系设置窗体具中显示
			Tools.centerWindow(reportDSRelaUI);
			// 显示关联关系设置窗体
			try {
				// 克隆树，从本界面到弹出窗体界面
				cloneTree(treRela, reportDSRelaUI.treRela);
				reportDSRelaUI.treRela.expandTree();
				reportDSRelaUI.setVisible(true);
				// 克隆树，从弹出窗体界面到
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
	 * 显示关联关系,根据summaryReport中信息
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

		// 关系数量
		int iCount = relationsArray.length;
		if (iCount == 0)
			return;

		// 得到父节点
		DefaultTreeModel treeModel = (DefaultTreeModel) treRela.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();
		// 是否有父节点标记,默认父节点不存在
		boolean sFlag = false;
		// 判断条件标记
		boolean sExistFlag = false;
		// 临时存放当前节点对象值
		MyNodeObject curMyNodeObject = null;
		// 临地存放当前Node
		DefaultMutableTreeNode curNode = null;
		// 新建节点
		DefaultMutableTreeNode treNode = null;

		// 数据源名称和列名称
		String sSourceIDAName = null;
		String sSourceIDBName = null;
		String sSourceColIDAName = null;
		String sSourceColIDBName = null;

		for (int i = 0; i < iCount; i++) {
			// 循环处理显示关联关系
			MyNodeObject myNodeObject = new MyNodeObject();

			relationsImpl = (SummaryDataSourceRelationsImpl) relationsArray[i];
			myNodeObject.sRelationsName = relationsImpl.getRelationsName();
			RefSource[] refSource = relationsImpl.getRefSourceArray();
			myNodeObject.sSourceIDA = refSource[0].getSourceID();
			myNodeObject.sSourceIDB = refSource[1].getSourceID();
			myNodeObject.sSourceColIDA = refSource[0].getSourceColID();
			myNodeObject.sSourceColIDB = refSource[1].getSourceColID();

			// 判断增加的父节点是否已存在,默认父节点不存在
			sFlag = false;
			int iChildCount = root.getChildCount();
			// 得到根节点下的子节点
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

			if (!sFlag) {// 父节点不存在, 增加父节点
				// 根据数据源ID得到数据源名称
				sSourceIDAName = DefinePub.getDataSourceNameWithID(querySource,
						myNodeObject.sSourceIDA);
				sSourceIDBName = DefinePub.getDataSourceNameWithID(querySource,
						myNodeObject.sSourceIDB);
				MyNodeObject myParNodeObject = new MyNodeObject();

				myParNodeObject.sRelationsName = sSourceIDAName + "="
						+ sSourceIDBName;
				myParNodeObject.setSSourceIDA(myNodeObject.sSourceIDA);
				myParNodeObject.setSSourceIDB(myNodeObject.sSourceIDB);
				// 增加父节点
				treNode = new DefaultMutableTreeNode(myParNodeObject);
				treeModel.insertNodeInto(treNode, root, root.getChildCount());
				curNode = (DefaultMutableTreeNode) root.getLastChild();
			}

			// 增加子节点
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
	 * 克隆树,只支持二层树
	 * 
	 * @param treFrom
	 *            原树
	 * @param treTo
	 *            目的树
	 */
	private void cloneTree(JTree treFrom, JTree treTo) {

		if (treFrom == null || treTo == null)
			return;

		// 原树根节点
		DefaultMutableTreeNode fromRoot = (DefaultMutableTreeNode) treFrom
				.getModel().getRoot();
		// 目的树根节点
		DefaultMutableTreeNode toRoot = (DefaultMutableTreeNode) treTo
				.getModel().getRoot();
		// 目的树Model
		DefaultTreeModel toTreeModel = (DefaultTreeModel) treTo.getModel();

		int iCount = toRoot.getChildCount();
		// 清空目的树节点
		for (int i = iCount - 1; i >= 0; i--) {
			toTreeModel.removeNodeFromParent((MutableTreeNode) toRoot
					.getChildAt(i));
		}

		// 原树存放当前节点
		DefaultMutableTreeNode froCurNode;
		// 目的树存放当前节点
		DefaultMutableTreeNode toCurNode;

		// 原树存放当前节点
		DefaultMutableTreeNode froCurChildNode;
		// 目的树存放当前叶节点
		DefaultMutableTreeNode toCurChildNode;

		iCount = fromRoot.getChildCount();
		// 叶节点
		int iChildCount = 0;
		for (int i = 0; i < iCount; i++) {
			froCurNode = (DefaultMutableTreeNode) fromRoot.getChildAt(i);
			toCurNode = (DefaultMutableTreeNode) froCurNode.clone();
			toTreeModel.insertNodeInto(toCurNode, toRoot, i);
			// 得到叶节点
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
